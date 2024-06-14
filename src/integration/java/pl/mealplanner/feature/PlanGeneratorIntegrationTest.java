package pl.mealplanner.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mealplannerv2.plangenerator.PlannedDay;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.mealplanner.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlanGeneratorIntegrationTest extends BaseIntegrationTest {

    @Test
    @WithMockUser
    public void plan_generator_test() throws Exception {
        // 1: użytkownik podaje preferencje i zwracany jest przepis na pierwszy dzień a resztki są zapisane w mapie
        // given & when
        ResultActions perform = mockMvc.perform(post("/plan/firstDay")
                .content("""
                        {
                           "unchangingPrefers": {
                             "diet": "wegetariańska",
                             "portions": 4,
                             "productsToAvoid": [
                               "oliwki"
                             ]
                           },
                           "userProducts": [
                             {
                               "name": "marchew",
                               "anyAmountUnit": {
                                 "amount": 200,
                                 "unit": "g"
                               },
                               "mainAmountUnit": null
                             }
                           ],
                           "firstDayOfPlan": "2024-06-12",
                           "mealsValues": [
                             {
                               "mealName": "DINNER",
                               "timeMin": 30,
                               "forHowManyDays": 2
                             }
                           ]
                         }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        MvcResult mvcResult2 = perform.andExpect(status().isOk()).andReturn();
        String jsonWithRecipe = mvcResult2.getResponse().getContentAsString();
        PlannedDay recipe = objectMapper.readValue(jsonWithRecipe, new TypeReference<>(){});

        System.out.println(recipe);

        // 2: użytkownik podaje informacje na kolejny dzień planu i
        // zwracany jest przepis na kolejny dzień a resztki są zapisane w mapie
        // given & when
        ResultActions perform2 = mockMvc.perform(post("/plan/nextDay")
                .content("""
                        {
                           "unchangingPrefers": {
                             "diet": "wegetariańska",
                             "portions": 4,
                             "productsToAvoid": [
                               "oliwki"
                             ]
                           },
                           "firstDayOfPlan": "2024-06-13",
                           "mealsValues": [
                             {
                               "mealName": "BREAKFAST",
                               "timeMin": null,
                               "forHowManyDays": null
                             },
                             {
                               "mealName": "DINNER",
                               "timeMin": null,
                               "forHowManyDays": 1
                             }
                           ]
                         }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        System.out.println(perform2.andReturn().getResponse().getContentAsString());

    }
}
