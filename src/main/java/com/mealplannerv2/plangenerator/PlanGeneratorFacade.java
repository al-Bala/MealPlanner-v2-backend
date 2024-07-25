package com.mealplannerv2.plangenerator;

import com.mealplannerv2.grocerylist.GroceryListFacade;
import com.mealplannerv2.loginandregister.LoginAndRegisterFacade;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.MealValues;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.Preferences;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.UnchangingPrefers;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.Meal;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFetcherFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.product.ProductFacade;
import com.mealplannerv2.product.dto.ChosenPacket;
import com.mealplannerv2.recipe.PlannedDayDb;
import com.mealplannerv2.recipe.RecipeForDayDb;
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
    private final PlanGeneratorMapper mapper;
    private final IngredientsCalculator ingredientsCalculator;
    private final ProductFacade productFacade;
    private final GroceryListFacade groceryListFacade;
    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final StorageFacade storageFacade;

    public PlannedDayDb createFirstDayOfPlan(Preferences preferences, List<IngredientDto> ingsFromUser){
        userIngsFacade.addAllToUserIngs(ingsFromUser);
        return createDay(preferences);
    }

    public PlannedDayDb createNextDayOfPlan(Preferences preferences){
        if (isNoMealSelected(preferences.mealsValues())){
            return null;
        }
        return createDay(preferences);
    }

    private PlannedDayDb createDay(Preferences preferences){
        System.out.println("UserIngs1: " + userIngsFacade.getUserIngs());
        System.out.println("Leftovers1: " + leftoversFacade.getLeftovers());

        UnchangingPrefers unchangingPrefers = preferences.unchangingPrefers();
        List<MealValues> mealValues = preferences.mealsValues();
        List<RecipeForDayDb> recipesForDay = new ArrayList<>();

        List<Meal> meals = Meal.getAllMeals(mealValues);
        for(Meal meal: meals){
            System.out.println("Ings to use firstly: " + storageFacade.getIngsToUseFirstly());

            DataForRecipeFiltering dataForRecipesFiltering = DataForRecipeFiltering.builder()
                    .diet(unchangingPrefers.diet())
                    .typeOfMeal(meal.getName())
                    .forHowManyDays(meal.getForHowManyDays())
                    .timeForPrepareMin(meal.getTimeMin())
                    .ingredientsToUseFirstly(storageFacade.getIngsToUseFirstly())
                    .productsToAvoid(unchangingPrefers.productsToAvoid())
                    .build();

            RecipeDto matchingRecipe = recipeFetcherFacade.fetchRecipeByPreferences(dataForRecipesFiltering);
            ingredientsCalculator.setCalculatedIngredients(matchingRecipe, unchangingPrefers.portions(), dataForRecipesFiltering.forHowManyDays());
            recipesForDay.add(new RecipeForDayDb(meal.getName(), matchingRecipe.getId().toString()));

            List<IngredientDto> ingsInRecipe = matchingRecipe.getIngredients();
            System.out.println("Ings in recipe: " + ingsInRecipe);
            List<IngredientDto> ingsToBuy = storageFacade.updateStorageAndAddToGroceryList(ingsInRecipe);
            System.out.println("Ings to buy: " + ingsToBuy);

            choosePacketsAndSaveLeftovers(ingsToBuy);
        }
        System.out.println("LeftoversBeforeSpoil: " + leftoversFacade.getLeftovers());
        // na koniec po obliczeniu resztek i dodaniu ich do storage
        leftoversFacade.updateSpoilDatesForLeftovers();

        PlannedDayDb plannedDay = PlannedDayDb.builder()
                .day(preferences.date())
                .planned_day(recipesForDay)
                .build();
//        List<PlannedDayDb> plannedDayDbs = loginAndRegisterFacade.saveGeneratedRecipe(plannedDay);
//        System.out.println(plannedDayDbs);
        System.out.println("UserIngs2: " + userIngsFacade.getUserIngs());
        System.out.println("Leftovers2: " + leftoversFacade.getLeftovers());
        return plannedDay;
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

    public PlannedDay changeLastDay(PlannedDay dayToChange){
        return null;
    }

    public Set<IngredientDto> getGroceryList(){
        return groceryListFacade.getGroceryList();
    }

}
