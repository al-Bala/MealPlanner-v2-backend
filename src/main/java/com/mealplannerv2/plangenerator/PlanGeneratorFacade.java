package com.mealplannerv2.plangenerator;

import com.mealplannerv2.ChangedRecipesList;
import com.mealplannerv2.grocerylist.GroceryListFacade;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.MealType;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.TempRecipe;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.PanelValuesForMeal;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.PreferencesDto;
import com.mealplannerv2.user.model.SavedPrefers;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.MealWithType;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFetcherFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.product.ProductFacade;
import com.mealplannerv2.product.dto.ChosenPacket;
import com.mealplannerv2.recipe.*;
import com.mealplannerv2.storage.IngredientDto;
import com.mealplannerv2.storage.StorageFacade;
import com.mealplannerv2.storage.leftovers.LeftoversFacade;
import com.mealplannerv2.storage.useringredients.UserIngsFacade;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.response.ResultDay;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.response.ResultRecipe;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.*;

@Log4j2
@AllArgsConstructor
@Component
public class PlanGeneratorFacade {
    private final RecipeFetcherFacade recipeFetcherFacade;
    private final LeftoversFacade leftoversFacade;
    private final UserIngsFacade userIngsFacade;
    private final IngredientsCalculator ingredientsCalculator;
    private final ProductFacade productFacade;
    private final GroceryListFacade groceryListFacade;
    private final StorageFacade storageFacade;
    private final RecipeFacade recipeFacade;


    public ResultDay createFirstDayOfPlan(PreferencesDto preferencesDto, List<IngredientDto> ingsFromUser){
        leftoversFacade.clearLeftovers();
        ChangedRecipesList.usedRecipes.clear();
        userIngsFacade.addAllToUserIngs(ingsFromUser);
        return findMatchingRecipes(preferencesDto);
    }

    public ResultDay createNextDayOfPlan(PreferencesDto preferencesDto, List<String> usedRecipesNames){
        //TODO: czy tak to przechowywać?
        ChangedRecipesList.usedRecipes.addAll(usedRecipesNames);

        if (isNoMealSelected(preferencesDto.mealsValues())){
            return new ResultDay(Collections.emptyList());
        }
        return findMatchingRecipes(preferencesDto);
    }

    public ResultDay changeLastDay(PreferencesDto preferencesDto, List<String> recipesNamesToChange){
        ChangedRecipesList.usedRecipes.addAll(recipesNamesToChange);
        return findMatchingRecipes(preferencesDto);
    }

    private ResultDay findMatchingRecipes(PreferencesDto preferencesDto){
        System.out.println("UserIngs1: " + userIngsFacade.getUserIngs());
        System.out.println("Leftovers1: " + leftoversFacade.getLeftovers());

        SavedPrefers savedPrefers = preferencesDto.savedPrefers();
        List<PanelValuesForMeal> panelValuesForAllMeals = preferencesDto.mealsValues();
        List<MealWithType> allMealsWithType = MealWithType.setTypeForAll(panelValuesForAllMeals);

        // have to start planning from Dinner (highest priority)
        List<MealWithType> allSortedMealsWithType = MealWithType.sortByPriority(allMealsWithType);

        Map<String, ResultRecipe> allCurrentDayRecipes = new HashMap<>(initialWithEmptyRecipesForAllMealTypes());

        for(MealWithType mealWithType : allSortedMealsWithType){
            System.out.println("Ings to use firstly: " + storageFacade.getIngsToUseFirstly());

            RecipeFilters recipeFilters = RecipeFilters.builder()
                    .dietId(savedPrefers.getDietId())
                    .typeOfMeal(mealWithType.getMealType().getId())
                    .forHowManyDays(mealWithType.getForHowManyDays())
                    .timeForPrepareMin(mealWithType.getTimeMin())
                    .ingredientsToUseFirstly(storageFacade.getIngsToUseFirstly())
                    .productsToAvoid(savedPrefers.getProducts_to_avoid())
                    .build();

            RecipeDto matchingRecipe = recipeFetcherFacade.fetchRecipeByPreferences(recipeFilters);

            ResultRecipe resultRecipeForMeal = ResultRecipe.builder()
                    .nameOfMealType(mealWithType.getMealType().getName())
                    .recipeId(matchingRecipe.getId().toString())
                    .recipeName(matchingRecipe.getName())
                    .build();
            allCurrentDayRecipes.put(mealWithType.getMealType().getId(), resultRecipeForMeal);
        }

        List<ResultRecipe> resultRecipes = allCurrentDayRecipes.values().stream().toList();
        ResultDay resultDay = ResultDay.builder()
                .resultRecipes(resultRecipes)
                .build();

        System.out.println("UserIngs2: " + userIngsFacade.getUserIngs());
        System.out.println("Leftovers2: " + leftoversFacade.getLeftovers());

        return resultDay;
    }

    private static Map<String, ResultRecipe> initialWithEmptyRecipesForAllMealTypes() {
        Map<String, ResultRecipe> initialResultRecipes = new HashMap<>();
        for(MealType mealType : MealType.values()){
            ResultRecipe emptyResultRecipe = createEmptyResultRecipe(mealType);
            initialResultRecipes.put(mealType.getId(), emptyResultRecipe);
        }
        return initialResultRecipes;
    }

    private static ResultRecipe createEmptyResultRecipe(MealType mealType) {
        return ResultRecipe.builder()
                .nameOfMealType(mealType.getName())
                .recipeId("SKIPPED")
                .recipeName("")
                .build();
    }

    public void processLeftoversAndGroceryList(int portions, List<TempRecipe> plannedRecipes){
        for(TempRecipe recipe : plannedRecipes){
            RecipeDto recipeDto = recipeFacade.getById(recipe.recipeId());
            List<IngredientDto> calculatedIngs = ingredientsCalculator.calculateIngredients(recipeDto, portions, recipe.forHowManyDays());
            List<IngredientDto> ingsToBuy = storageFacade.updateStorageAndAddToGroceryList(calculatedIngs);
            choosePacketsAndSaveLeftovers(ingsToBuy);
        }
        leftoversFacade.updateSpoilDatesForLeftovers();
    }

    private void choosePacketsAndSaveLeftovers(List<IngredientDto> updatedIngs) {
        List<ChosenPacket> allNeededPackets = productFacade.choosePacketForEachIngredient(updatedIngs);
        List<IngredientDto> leftovers = allNeededPackets.stream()
                .filter(packet -> packet.getLeftovers() > 0)
                .map(packet -> new IngredientDto(
                        packet.getIngredientDto().getName(),
                        packet.getLeftovers(),
                        packet.getIngredientDto().getUnit()))
//                .filter(ing -> ing.getAmount() > 0)
                .toList();
        leftoversFacade.convertAndAddToLeftovers(leftovers);
    }

    private static boolean isNoMealSelected(List<PanelValuesForMeal> mealsValues) {
        if(mealsValues.isEmpty()){
            log.info("Skip day");
            return true;
        }
        return false;
    }

    public Set<IngredientDto> getGroceryList(){
        return groceryListFacade.getGroceryList();
    }

}
