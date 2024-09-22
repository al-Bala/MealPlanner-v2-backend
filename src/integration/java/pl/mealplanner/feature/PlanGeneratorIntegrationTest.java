package pl.mealplanner.feature;

import com.auth0.jwt.JWT;
import com.mealplannerv2.plangenerator.PlanGeneratorFacade;
import com.mealplannerv2.recipe.RecipeRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.mealplanner.BaseIntegrationTest;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static com.mealplannerv2.plangenerator.PlanGeneratorFacade.tempPlan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlanGeneratorIntegrationTest extends BaseIntegrationTest {

    @Autowired
    PlanGeneratorFacade planGeneratorFacade;

    @Autowired
    RecipeRepository recipeRepository;

    String token;

    @BeforeEach
    public void setup() throws Exception {
        token = JWT.create().sign(HMAC256("secret"));
    }


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
                .servletPath("/plan/firstDay")
                .cookie(new Cookie("accessToken", token))
                .content("""
                        {
                           "unchangingPrefers": {
                             "diet": "wegetariańska",
                             "portions": 2,
                             "productsToAvoid": [
                               "kiwi"
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
        System.out.println(jsonWithRecipe);

        System.out.println("TempPlan: ");
        tempPlan.days().forEach(t -> System.out.println(t.getDate() + " " + t.getPlanned_day()));
        // TODO: przeliczanie porcji w wyświetlanym przepisie

        // step 2:
        // użytkownik wybiera jeden posiłek (obiad) na kolejny dzień

        // given & when
        ResultActions perform2 = mockMvc.perform(post("/plan/nextDay")
                .servletPath("/plan/nextDay")
                .cookie(new Cookie("accessToken", token))
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

        MvcResult mvcResult2 = perform2.andExpect(status().isOk()).andReturn();
        String jsonWithRecipe2 = mvcResult2.getResponse().getContentAsString();
        System.out.println(jsonWithRecipe2);
        System.out.println("TempPlan: ");
        tempPlan.days().forEach(t -> System.out.println(t.getDate() + " " + t.getPlanned_day()));

    }
}
