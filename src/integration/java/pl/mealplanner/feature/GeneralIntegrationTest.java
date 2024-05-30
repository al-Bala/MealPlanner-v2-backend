package pl.mealplanner.feature;

import com.mealplannerv2.InfoForFiltering;
import com.mealplannerv2.entity.Recipe;
import com.mealplannerv2.repository.IngredientDto;
import com.mealplannerv2.repository.MealsFilterRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mealplanner.BaseIntegrationTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GeneralIntegrationTest extends BaseIntegrationTest {
    @Autowired
    public MealsFilterRepositoryImpl mealsFilterRepository;

    @Test
    public void general_test(){
        // given
//        InfoForFiltering info = InfoForFiltering.builder()
//                .forHowManyDays(2)
//                .diet("wegetariańska")
//                .timeForPrepareMin(5)
//                .userProducts(List.of(
//                        new IngredientDto("ryż", 300.0, ""),
//                        new IngredientDto("marchew", 2.0, "")))
//                .productsToAvoid(List.of("brokuł"))
//                .build();
        InfoForFiltering info = InfoForFiltering.builder()
                .forHowManyDays(2)
                .diet("mięsna")
                .timeForPrepareMin(20)
                .userProducts(List.of(
                        new IngredientDto("truskawki", 80.0, "g"),
                        new IngredientDto("marchew", 1.0, "szt")))
//                        new IngredientDto("ryż", 300.0, "g")))
                .build();

        // when
//        List<Recipe> matchingRecipes = mealsFilterRepository.findRecipesWithMatchingIngNamesAndAmounts(info);
        List<Recipe> matchingRecipes = mealsFilterRepository.findRecipesWithMatchingIngNames(info);

        // then
        System.out.println(matchingRecipes);
    }
}
