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
        InfoForFiltering info = InfoForFiltering.builder()
                .forHowManyDays(2)
                .diet("wegetariańska")
                .timeForPrepareMin(5)
                .userProducts(List.of(new IngredientDto("marchew", 0.0, "")))
                .productsToAvoid(List.of("brokuł"))
                .build();

        // when
        List<Recipe> matchingRecipes = mealsFilterRepository.findMatchingRecipes(info);

//        verify(mongoTemplate, times(3)).aggregate(any(Aggregation.class), anyString(), any(Class.class));

        // then
        System.out.println(matchingRecipes);
        assertThat(matchingRecipes.get(0).name()).isEqualTo("Kasza jaglana z warzywami");
    }
}
