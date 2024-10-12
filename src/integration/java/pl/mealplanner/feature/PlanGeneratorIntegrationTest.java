package pl.mealplanner.feature;

import com.auth0.jwt.JWT;
import com.mealplannerv2.plangenerator.PlanGeneratorFacade;
import com.mealplannerv2.user.model.SavedPrefers;
import com.mealplannerv2.user.model.Plan;
import com.mealplannerv2.recipe.RecipeRepository;
import com.mealplannerv2.user.UserFacade;
import jakarta.servlet.http.Cookie;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.mealplanner.BaseIntegrationTest;

import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class PlanGeneratorIntegrationTest extends BaseIntegrationTest {

    @Autowired
    PlanGeneratorFacade planGeneratorFacade;

    @Autowired
    UserFacade userFacade;

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
        // 1) user ma w bazie zapisany plan: 12 - 13.09.2024
        log.info(userPlans());
        // 2) user otwiera okno generatora --> jego zapisane preferencje autmatycznie sie uzupełniają
        // given & when
        ResultActions getSavePrefsAction = mockMvc.perform(get("/users/6672e2e8be614a1616e7552f/prefs")
                .servletPath("/users/6672e2e8be614a1616e7552f/prefs")
                .cookie(new Cookie("accessToken", token))
        );
        // then
        MvcResult getPrefsResult = getSavePrefsAction.andExpect(status().isOk()).andReturn();
        String jsonWithPrefers = getPrefsResult.getResponse().getContentAsString();
        SavedPrefers savedPrefersResult = objectMapper.readValue(jsonWithPrefers, SavedPrefers.class);
        assertAll(
                () -> assertThat(savedPrefersResult.getDietId()).isEqualTo("66f7f883326bd5fde1b7f779"),
                () -> assertThat(savedPrefersResult.getPortions()).isEqualTo(2),
                () -> assertThat(savedPrefersResult.getProducts_to_avoid()).containsOnly("oliwki")
        );
        log.info("[1]: {}", jsonWithPrefers);
//        System.out.println("[1] -- " + jsonWithPrefers);

        // 3) user zmienia diete, ustawia początkową date na 12.09.2024, wypełnia pozostałe preferencje
        log.info("Chosen date: 12-09-2024");

        // i przechodzi dalej --> system aktualizuje preferencje
        // given & when
        mockMvc.perform(put("/users/6672e2e8be614a1616e7552f/prefs")
                .servletPath("/users/6672e2e8be614a1616e7552f/prefs")
                .cookie(new Cookie("accessToken", token))
                .content("""
                        {
                            "diet": "wegetarianska",
                            "portions": 2,
                            "products_to_avoid": [
                              "oliwki"
                            ]
                         }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        SavedPrefers testUserPrefs = userFacade.getByUsername("testUser").getPreferences();
        assertThat(testUserPrefs.getDietId()).isEqualTo("66f7f86a326bd5fde1b7f775");
        log.info("[2]: {}", testUserPrefs);
//        System.out.println("[2] -- " + testUserPrefs);

        // 6) user wybiera jeden posiłek (obiad) na pierwszy dzień --> system wyświetla wygenerowane przepisy

        ResultActions createFirstDayAction = mockMvc.perform(post("/generator/days/first")
                .servletPath("/generator/plannedDays/first")
                .cookie(new Cookie("accessToken", token))
                .content("""
                        {
                           "savedPrefers": {
                             "diet": "wegetariańska",
                             "portions": 2,
                             "products_to_avoid": [
                               "oliwki"
                             ]
                           },
                           "userProducts": [
                             {
                               "name": "marchew",
                               "amount": 200,
                               "unit": "g"
                             }
                           ],
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
        MvcResult firstDayResult = createFirstDayAction.andExpect(status().isOk()).andReturn();
        String jsonWithFirstDayRecipe = firstDayResult.getResponse().getContentAsString();
        log.info("[3]: {}", jsonWithFirstDayRecipe);
//        System.out.println("[3] -- " + jsonWithFirstDayRecipe);

        // 8) user zmienia przepisy na dany dzień

//        ResultActions changeDayAction = mockMvc.perform(post("/plan/day/change")
//                .servletPath("/plan/day/change")
//                .cookie(new Cookie("accessToken", token))
//                .content("""
//                        {
//                           "savedPrefers": {
//                             "dietId": "wegetariańska",
//                             "portions": 2,
//                             "products_to_avoid": [
//                               "kiwi"
//                             ]
//                           },
//                           "mealsValues": [
//                             {
//                               "mealId": "DINNER",
//                               "timeMin": -1,
//                               "forHowManyDays": 1
//                             }
//                           ]
//                         }
//                        """.trim())
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//        );

        // 9) system wyświetla inne przepisy

        // 10) user zatwierdza przepisy na dany dzień

        ResultActions saveDayInTempPlanAction = mockMvc.perform(post("/users/6672e2e8be614a1616e7552f/temp-plan/days")
                .servletPath("/users/6672e2e8be614a1616e7552f/temp-plan/plannedDays")
                .cookie(new Cookie("accessToken", token))
                .content(jsonWithFirstDayRecipe)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        MvcResult tempPlanResult = saveDayInTempPlanAction.andExpect(status().isOk()).andReturn();
        String jsonWithTempPlan = tempPlanResult.getResponse().getContentAsString();
        log.info("[4]: {}", jsonWithTempPlan);
//        System.out.println("[4] -- " + jsonWithTempPlan);

        // 11) user wybiera jeden posiłek (obiad) na kolejny dzień --> system wyświetla wygenerowane przepisy

        ResultActions createNextDayAction = mockMvc.perform(post("/generator/days/next")
                .servletPath("/generator/plannedDays/next")
                .cookie(new Cookie("accessToken", token))
                .content("""
                        {
                           "savedPrefers": {
                             "diet": "wegetariańska",
                             "portions": 2,
                             "products_to_avoid": [
                               "oliwki"
                             ]
                           },
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
        MvcResult nextDayResult = createNextDayAction.andExpect(status().isOk()).andReturn();
        String jsonWithNextDayRecipe = nextDayResult.getResponse().getContentAsString();
//        System.out.println("[5] -- " + jsonWithNextDayRecipe);
        log.info("[5]: {}", jsonWithNextDayRecipe);

        // 10) user zatwierdza przepisy na kolejny dzień

        ResultActions saveNextDayInTempPlanAction = mockMvc.perform(post("/users/6672e2e8be614a1616e7552f/temp-plan/days")
                .servletPath("/users/6672e2e8be614a1616e7552f/temp-plan/plannedDays")
                .cookie(new Cookie("accessToken", token))
                .content(jsonWithFirstDayRecipe)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        MvcResult nextTempPlanResult = saveNextDayInTempPlanAction.andExpect(status().isOk()).andReturn();
        String jsonWithNextTempPlan = nextTempPlanResult.getResponse().getContentAsString();
//        System.out.println("[6] -- " + jsonWithNextTempPlan);
        log.info("[6]: {}", jsonWithNextTempPlan);

        // 10) user zatwierdza cały plan ale system zwraca błąd
        ResultActions failSavePlanAction = mockMvc.perform(post("/users/6672e2e8be614a1616e7552f/plans")
                .servletPath("/users/6672e2e8be614a1616e7552f/plans")
                .cookie(new Cookie("accessToken", token))
                .content("2024-09-12")
                .contentType(MediaType.TEXT_PLAIN)
        );
        MvcResult failSavePlanResult = failSavePlanAction.andExpect(status().isBadRequest()).andReturn();
        String jsonWithFailSaveResponse = failSavePlanResult.getResponse().getContentAsString();
//        System.out.println("[7] -- " + jsonWithFailSaveResponse);
        log.info("[7]: {}", jsonWithFailSaveResponse);

        // 11) system wyświetla komunikat o istniejącym już planie (nadpisać czy zmienić date?)
        // 12) user zmienia początkową date na 14.09.2024 i zatwierdza plan --> system zapisuje cały plan w bazie
        log.info("Changed date: 14-09-2024");
        ResultActions savePlanAction = mockMvc.perform(post("/users/6672e2e8be614a1616e7552f/plans")
                .servletPath("/users/6672e2e8be614a1616e7552f/plans")
                .cookie(new Cookie("accessToken", token))
                .content("2024-09-14")
                .contentType(MediaType.TEXT_PLAIN)
        );
        MvcResult savePlanResult = savePlanAction.andExpect(status().isOk()).andReturn();
        String jsonWithSaveResponse = savePlanResult.getResponse().getContentAsString();
//        System.out.println("[8] -- " + jsonWithSaveResponse);
        log.info("[8]: {}", jsonWithSaveResponse);

//        List<Plan> plans2 = userFacade.getByUsername("testUser").getPlans();
//        System.out.println("[8a] -- History");
//        plans2.forEach(p -> {
//            System.out.println("Plan: ");
//            p.plannedDays().forEach(d -> System.out.println(d.getDate() + " " + d.getPlanned_day()));
//        });
        log.info(userPlans());

        // TODO: przeliczanie porcji w wyświetlanym przepisie
    }

    private @NotNull StringBuilder userPlans() {
        List<Plan> plans = userFacade.getByUsername("testUser").getPlans();
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("[0] -- History:");
        plans.forEach(p -> {
            logMessage.append("\n");
            logMessage.append("Plan ").append(" (dates): ");
            p.plannedDays().forEach(d -> logMessage.append(" | ").append(d.getDate()));
        });
        return logMessage;
    }
}
