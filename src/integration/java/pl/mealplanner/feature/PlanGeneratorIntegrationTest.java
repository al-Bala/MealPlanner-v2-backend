package pl.mealplanner.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mealplannerv2.plangenerator.PlanGeneratorFacade;
import com.mealplannerv2.plangenerator.recipefilter.model.Recipe;
import com.mealplannerv2.recipe.PlannedDayDb;
import com.mealplannerv2.recipe.RecipeRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.mealplanner.BaseIntegrationTest;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlanGeneratorIntegrationTest extends BaseIntegrationTest {

    @Autowired
    PlanGeneratorFacade planGeneratorFacade;

    @Autowired
    RecipeRepository recipeRepository;

    @Test
    @WithMockUser(username = "testUser")
    public void plan_generator_test() throws Exception {
        // step 1:
            // a) użytkownik wypełnia wszystkie preferencje i wybiera jeden posiłek (obiad) na pierwszy dzień
            // b) składniki użytkownika zostają zapisane
            // c) program dla każdego posiłku:
                // - znajduje pasujący przepis
                // - przelicza ilość składników
                // - dodaje przepis do listy
                // - aktualizuje składniki w storage
                // - dla każdego składnika wybiera opakowanie i przelicza resztki
            // d) daty przydatności zostają zaaktualizowane
            // e) zwracany jest przepis na pierwszy dzień

        // given & when
        ResultActions perform = mockMvc.perform(post("/plan/firstDay")
                .content("""
                        {
                           "unchangingPrefers": {
                             "diet": "wegetariańska",
                             "portions": 2,
                             "productsToAvoid": [
                               "brokul"
                             ]
                           },
                           "userProducts": [
                             {
                               "name": "marchew",
                               "amount": 200,
                               "unit": "g"
                             }
                           ],
                           "date": "2024-06-12",
                           "mealsValues": [
                             {
                               "mealId": "DINNER",
                               "timeMin": -1,
                               "forHowManyDays": 1
                             }
                           ]
                         }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();
        String jsonWithRecipe = mvcResult.getResponse().getContentAsString();
        PlannedDayDb recipe = objectMapper.readValue(jsonWithRecipe, new TypeReference<>(){});

        // TODO: przeliczanie porcji w wyświetlanym przepisie
        List<Optional<Recipe>> list = recipe.planned_day().stream()
                .map(r -> recipeRepository.findById(new ObjectId(r.recipeId())))
                .toList();
        System.out.println("Recipe: " + list);

        // step 2:
        // użytkownik wybiera jeden posiłek (obiad) na kolejny dzień

        // given & when
        ResultActions perform2 = mockMvc.perform(post("/plan/nextDay")
                .content("""
                        {
                           "date": "2024-06-13",
                           "mealsValues": [
                             {
                               "mealId": "DINNER",
                               "timeMin": -1,
                               "forHowManyDays": 1
                             }
                           ]
                         }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        MvcResult mvcResult2 = perform.andExpect(status().isOk()).andReturn();
        String jsonWithRecipe2 = mvcResult2.getResponse().getContentAsString();
        PlannedDayDb recipe2 = objectMapper.readValue(jsonWithRecipe2, new TypeReference<>(){});

        List<Optional<Recipe>> list2 = recipe2.planned_day().stream()
                .map(r -> recipeRepository.findById(new ObjectId(r.recipeId())))
                .toList();
        System.out.println("Recipe2: " + list2);

    }
}
