package pl.mealplanner.feature;

import com.mealplannerv2.plangenerator.DataForRecipeFiltering;
import com.mealplannerv2.plangenerator.recipefilter.infrastructure.db.RecipeFilterRepositoryImpl;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.plangenerator.recipefilter.model.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mealplanner.BaseIntegrationTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GeneralIntegrationTest extends BaseIntegrationTest {
    @Autowired
    public RecipeFilterRepositoryImpl mealsFilterRepository;

    @Test
    public void general_test(){

        // ktos podaje preferencje: 2, mięsna, 20, {truskawki, 80}, {marchew, 1}
        // wykonanie wyszukiwania z nazwamai i ilościami - brak odpowiedzi
        // wykonanie wyszukiwania z nazwamai - znajduje
        // zapisuje

        // given
//        InfoForFiltering info = InfoForFiltering.builder()
//                .forHowManyDays(2)
//                .diet("wegetariańska")
//                .timeForPrepareMin(5)
//                .ingredientsToUseFirstly(List.of(
//                        new IngredientDto("ryż", 300.0, ""),
//                        new IngredientDto("marchew", 2.0, "")))
//                .productsToAvoid(List.of("brokuł"))
//                .build();
        DataForRecipeFiltering info = DataForRecipeFiltering.builder()
                .forHowManyDays(2)
                .diet("mięsna")
                .timeForPrepareMin(20)
                .ingredientsToUseFirstly(List.of(
                        new Ingredient("truskawki", 80.0, "g"),
                        new Ingredient("marchew", 1.0, "szt")))
//                        new IngredientDto("ryż", 300.0, "g")))
                .build();

        // when
        List<Recipe> matchingRecipes1 = mealsFilterRepository.findRecipesWithMatchingIngNamesAndAmounts(info);
        System.out.println("1 " + matchingRecipes1);
        List<Recipe> matchingRecipes2 = mealsFilterRepository.findRecipesWithMatchingIngNames(info);
        System.out.println("2 " + matchingRecipes2);

    }
}
