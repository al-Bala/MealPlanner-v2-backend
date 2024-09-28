package com.mealplannerv2.plangenerator;

import com.mealplannerv2.ChangedRecipesList;
import com.mealplannerv2.grocerylist.GroceryListFacade;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.MealValues;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.Preferences;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.SavedPrefers;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.Meal;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFetcherFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.product.ProductFacade;
import com.mealplannerv2.product.dto.ChosenPacket;
import com.mealplannerv2.recipe.DayPlan;
import com.mealplannerv2.recipe.RecipeDay;
import com.mealplannerv2.recipe.RecipeFacade;
import com.mealplannerv2.storage.IngredientDto;
import com.mealplannerv2.storage.StorageFacade;
import com.mealplannerv2.storage.leftovers.LeftoversFacade;
import com.mealplannerv2.storage.useringredients.UserIngsFacade;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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


    public DayPlan createFirstDayOfPlan(Preferences preferences, List<IngredientDto> ingsFromUser){
        ChangedRecipesList.usedRecipes.clear();
        userIngsFacade.addAllToUserIngs(ingsFromUser);
        return findMatchingRecipes(preferences);
    }

    public DayPlan createNextDayOfPlan(Preferences preferences, List<DayPlan> tempDays){
        List<String> usedRecipes = tempDays.stream()
                .flatMap(day -> day.getPlanned_day().stream())
                .map(RecipeDay::recipeName)
                .toList();

        //TODO: czy tak to przechowywać?
        ChangedRecipesList.usedRecipes.addAll(usedRecipes);

        if (isNoMealSelected(preferences.mealsValues())){
            return null;
        }
        return findMatchingRecipes(preferences);
    }

    public DayPlan changeLastDay(Preferences preferences, List<RecipeDay> dayToChange){
        List<String> recipesToChange = dayToChange.stream()
                .map(RecipeDay::recipeName)
                .toList();
        ChangedRecipesList.usedRecipes.addAll(recipesToChange);
        return findMatchingRecipes(preferences);
    }

    private DayPlan findMatchingRecipes(Preferences preferences){
        System.out.println("UserIngs1: " + userIngsFacade.getUserIngs());
        System.out.println("Leftovers1: " + leftoversFacade.getLeftovers());

        SavedPrefers savedPrefers = preferences.savedPrefers();
        List<MealValues> mealValues = preferences.mealsValues();
        List<RecipeDay> recipesForDay = new ArrayList<>();

        List<Meal> meals = Meal.getAllMeals(mealValues);
        for(Meal meal: meals){
            System.out.println("Ings to use firstly: " + storageFacade.getIngsToUseFirstly());

            RecipeFilters recipeFilters = RecipeFilters.builder()
                    .diet(savedPrefers.getDiet().getName())
                    .typeOfMeal(meal.getName())
                    .forHowManyDays(meal.getForHowManyDays())
                    .timeForPrepareMin(meal.getTimeMin())
                    .ingredientsToUseFirstly(storageFacade.getIngsToUseFirstly())
                    .productsToAvoid(savedPrefers.getProducts_to_avoid())
                    .build();

            RecipeDto matchingRecipe = recipeFetcherFacade.fetchRecipeByPreferences(recipeFilters);
            recipesForDay.add(new RecipeDay(
                    meal.getName(),
                    matchingRecipe.getId().toString(),
                    matchingRecipe.getName(),
                    recipeFilters.forHowManyDays()
            ));
        }
        DayPlan plannedDay = DayPlan.builder()
                .planned_day(recipesForDay)
                .build();

        System.out.println("UserIngs2: " + userIngsFacade.getUserIngs());
        System.out.println("Leftovers2: " + leftoversFacade.getLeftovers());

        return plannedDay;
    }

    public void processLeftoversAndGroceryList(int portions, List<RecipeDay> tempDay){
        for(RecipeDay recipe : tempDay){
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

    private static boolean isNoMealSelected(List<MealValues> mealsValues) {
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
