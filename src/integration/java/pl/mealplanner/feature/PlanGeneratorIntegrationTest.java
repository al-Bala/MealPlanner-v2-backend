package pl.mealplanner.feature;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import pl.mealplanner.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class PlanGeneratorIntegrationTest extends BaseIntegrationTest {

    @Test
    @WithMockUser
    public void plan_generator_test() throws Exception {
        //step 1: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        // given & when
        ResultActions failedLoginRequest = mockMvc.perform(post("/plan/preferences")
                .content("""
                        {
                          "preferencesInfo": {
                            "diet": "wegetariańska",
                            "portions": 4,
                            "productsToAvoid": ["oliwki"],
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
                            "firstDayOfPlan": "2024-06-12"
                          },
                          "dayInfo": {
                            "mealsInfo": [
                              {
                                "mealName": "DINNER",
                                "timeMin": 30,
                                "for2days": false
                              }
                            ]
                          }
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        System.out.println(failedLoginRequest.andReturn().getResponse().getContentAsString());

    }
}
