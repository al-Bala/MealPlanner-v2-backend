package pl.mealplanner.feature;

import com.mealplannerv2.plangenerator.RecipeFilters;
import com.mealplannerv2.plangenerator.recipefilter.infrastructure.db.RecipeFilterRepositoryImpl;
import com.mealplannerv2.recipe.model.Ingredient;
import com.mealplannerv2.recipe.model.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mealplanner.BaseIntegrationTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseIntegrationTest extends BaseIntegrationTest {
    @Autowired
    public RecipeFilterRepositoryImpl mealsFilterRepository;

    @Test
    public void database_test(){
        // given
//        InfoForFiltering info = InfoForFiltering.builder()
//                .forHowManyDays(2)
//                .dietId("wegetariańska")
//                .timeForPrepareMin(5)
//                .ingredientsToUseFirstly(List.of(
//                        new IngredientDto("ryż", 300.0, ""),
//                        new IngredientDto("marchew", 2.0, "")))
//                .productsToAvoid(List.of("brokuł"))
//                .build();
        RecipeFilters info = RecipeFilters.builder()
                .forHowManyDays(2)
                .dietId("66f7f883326bd5fde1b7f779")
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
