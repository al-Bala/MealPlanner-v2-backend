package pl.mealplanner.feature;

import com.auth0.jwt.JWT;
import com.mealplannerv2.auth.dto.UserDto;
import com.mealplannerv2.plangenerator.PlanGeneratorFacade;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.response.CreateDayResponse;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.response.ResultRecipe;
import com.mealplannerv2.user.model.SavedPrefers;
import com.mealplannerv2.user.model.Plan;
import com.mealplannerv2.recipe.RecipeRepository;
import com.mealplannerv2.user.UserFacade;
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
        ResultActions getSavePrefsAction = mockMvc.perform(get("/users/testUser/prefs")
                .servletPath("/users/testUser/prefs")
                .header("Authorization", "Bearer " + token)
        );
        // then
        MvcResult getPrefsResult = getSavePrefsAction.andExpect(status().isOk()).andReturn();
        String jsonWithPrefers = getPrefsResult.getResponse().getContentAsString();
        SavedPrefers savedPrefersResult = objectMapper.readValue(jsonWithPrefers, SavedPrefers.class);
        assertAll(
                () -> assertThat(savedPrefersResult.getDietId()).isEqualTo("66f7f883326bd5fde1b7f779"),
                () -> assertThat(savedPrefersResult.getPortions()).isEqualTo(2),
                () -> assertThat(savedPrefersResult.getProducts_to_avoid()).containsOnly("olives")
        );
        log.info("[1]: {}", jsonWithPrefers);


    // 3) user zmienia diete, ustawia początkową date na 12.09.2024, wypełnia pozostałe preferencje
        log.info("Chosen date: 12-09-2024");
        // i przechodzi dalej --> system aktualizuje preferencje
        // given & when
        mockMvc.perform(put("/users/testUser/prefs")
                .servletPath("/users/testUser/prefs")
                .header("Authorization", "Bearer " + token)
                .content("""
                        {
                            "dietId": "66f7f86a326bd5fde1b7f775",
                            "portions": 2,
                            "products_to_avoid": [
                              "olives"
                            ]
                         }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        SavedPrefers testUserPrefs = userFacade.getByUsername("testUser").getPreferences();
        assertThat(testUserPrefs.getDietId()).isEqualTo("66f7f86a326bd5fde1b7f775");
        log.info("[2]: {}", testUserPrefs);


    // 4) user wybiera jeden posiłek (obiad) na pierwszy dzień --> system wyświetla wygenerowane przepisy
        ResultActions createFirstDayAction = mockMvc.perform(post("/generator/days/first")
                .servletPath("/generator/days/first")
                .header("Authorization", "Bearer " + token)
                .content("""
                        {
                           "savedPrefers": {
                             "dietId": "66f7f86a326bd5fde1b7f775",
                             "portions": 2,
                             "products_to_avoid": [
                               "olives"
                             ]
                           },
                           "userProducts": [
                             {
                               "name": "carrot",
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
        String jsonWithFirstDayResult = firstDayResult.getResponse().getContentAsString();
        CreateDayResponse createDayResponse = objectMapper.readValue(jsonWithFirstDayResult, CreateDayResponse.class);
        log.info("[3]: {}", createDayResponse);
        ResultRecipe oneResultRecipe = createDayResponse.resultDay().resultRecipes().get(0);
        assertAll(
//                () -> assertThat(createDayResponse.message()).isEqualTo(""),
                () -> assertThat(createDayResponse.resultDay().resultRecipes().size()).isEqualTo(1),
                () -> assertThat(oneResultRecipe.mealTypeName()).isEqualTo("DINNER"),
                () -> assertThat(oneResultRecipe.recipeId()).isEqualTo("6577660abbac733a111c9421"),
                () -> assertThat(oneResultRecipe.recipeName()).isEqualTo("Millet groats with vegetables")
        );


    // 5) user zmienia przepisy na dany dzień

//        ResultActions changeDayAction = mockMvc.perform(post("/plan/day/change")
//                .servletPath("/plan/day/change")
//                .cookie(new Cookie("accessToken", token))
//                .content("""
//                        {
//                           "savedPrefers": {
//                             "dietId": "66f7f86a326bd5fde1b7f775",
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


    // 6) system wyświetla inne przepisy


    // 7) user przechodzi do kolejnego dnia --> jednocześnie zatwierdza przepisy na dany dzień
        ResultActions saveDayInTempPlanAction = mockMvc.perform(post("/generator/days/accept")
                .servletPath("/generator/days/accept")
                .header("Authorization", "Bearer " + token)
                  .content("""
                        {
                           "portions": 2,
                           "tempRecipes": [
                             {
                               "typeOfMeal": "DINNER",
                               "recipeId": "6577660abbac733a111c9421",
                               "recipeName": "Millet groats with vegetables",
                               "forHowManyDays": 1,
                               "isRepeated": false
                             }
                           ]
                         }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        saveDayInTempPlanAction.andExpect(status().isOk());
        log.info("[4]: Day accepted");


    // 8) user wybiera jeden posiłek (obiad) na kolejny dzień --> system wyświetla wygenerowane przepisy
        ResultActions createNextDayAction = mockMvc.perform(post("/generator/days/next")
                .servletPath("/generator/days/next")
                .header("Authorization", "Bearer " + token)
                .content("""
                        {
                           "savedPrefers": {
                             "dietId": "66f7f86a326bd5fde1b7f775",
                             "portions": 2,
                             "products_to_avoid": [
                               "olives"
                             ]
                           },
                           "mealsValues": [
                             {
                               "mealId": "DINNER",
                               "timeMin": -1,
                               "forHowManyDays": 1
                             }
                           ],
                           "usedRecipesNames": ["Millet groats with vegetables"]
                         }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        MvcResult nextDayResult = createNextDayAction.andExpect(status().isOk()).andReturn();
        String jsonWithNextDayRecipe = nextDayResult.getResponse().getContentAsString();
        CreateDayResponse createNextDayResponse = objectMapper.readValue(jsonWithNextDayRecipe, CreateDayResponse.class);
        log.info("[5]: {}", createNextDayResponse);
        ResultRecipe oneNextResultRecipe = createNextDayResponse.resultDay().resultRecipes().get(0);
        assertAll(
//                () -> assertThat(createDayResponse.message()).isEqualTo(""),
                () -> assertThat(createDayResponse.resultDay().resultRecipes().size()).isEqualTo(1),
                () -> assertThat(oneNextResultRecipe.mealTypeName()).isEqualTo("DINNER"),
                () -> assertThat(oneNextResultRecipe.recipeId()).isEqualTo("6577660abbac733a111c9427"),
                () -> assertThat(oneNextResultRecipe.recipeName()).isEqualTo("Pasta with vegetables and pesto")
        );


    // 9) user przechodzi do kolejnego dnia --> jednocześnie zatwierdza przepisy na kolejny dzień
        ResultActions saveNextDayAction = mockMvc.perform(post("/generator/days/accept")
                .servletPath("/generator/days/accept")
                .header("Authorization", "Bearer " + token)
                .content("""
                        {
                           "portions": 2,
                           "tempRecipes": [
                             {
                               "typeOfMeal": "DINNER",
                               "recipeId": "6577660abbac733a111c9427",
                               "recipeName": "Pasta with vegetables and pesto",
                               "forHowManyDays": 1,
                               "isRepeated": false
                             }
                           ]
                         }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        saveNextDayAction.andExpect(status().isOk());
        log.info("[6]: Next day accepted");

    // 10) user zatwierdza cały plan ale system zwraca błąd
        ResultActions failSavePlanAction = mockMvc.perform(post("/users/testUser/plans")
                .servletPath("/users/testUser/plans")
                .header("Authorization", "Bearer " + token)
                  .content("""
                        {
                           "startDateText": "2024-09-12",
                           "daysToSave": [
                             {
                               "date": "",
                               "plannedRecipes": [
                                  {
                                    "typeOfMeal": "DINNER",
                                    "recipeId": "6577660abbac733a111c9421",
                                    "recipeName": "Millet groats with vegetables",
                                    "forHowManyDays": 1
                                  }
                               ]
                             },
                             {
                               "date": "",
                               "plannedRecipes": [
                                  {
                                    "typeOfMeal": "DINNER",
                                    "recipeId": "6577660abbac733a111c9427",
                                    "recipeName": "Pasta with vegetables and pesto",
                                    "forHowManyDays": 1
                                  }
                               ]
                             }
                           ]
                         }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        MvcResult failSavePlanResult = failSavePlanAction.andExpect(status().isBadRequest()).andReturn();
        String jsonWithFailSaveResponse = failSavePlanResult.getResponse().getContentAsString();
        log.info("[7]: {}", jsonWithFailSaveResponse);
        assertThat(jsonWithFailSaveResponse).isEqualTo("Duplicate Plan");

    // 11) system wyświetla komunikat o istniejącym już planie (nadpisać czy zmienić date?)
    // 12) user zmienia początkową date na 14.09.2024 i zatwierdza plan --> system zapisuje cały plan w bazie
        log.info("Changed date: 14-09-2024");
        ResultActions savePlanAction = mockMvc.perform(post("/users/testUser/plans")
                .servletPath("/users/testUser/plans")
                .header("Authorization", "Bearer " + token)
                .content("""
                        {
                           "startDateText": "2024-09-14",
                           "daysToSave": [
                             {
                               "date": "",
                               "plannedRecipes": [
                                  {
                                    "typeOfMeal": "DINNER",
                                    "recipeId": "6577660abbac733a111c9421",
                                    "recipeName": "Millet groats with vegetables",
                                    "forHowManyDays": 1
                                  }
                               ]
                             },
                             {
                               "date": "",
                               "plannedRecipes": [
                                  {
                                    "typeOfMeal": "DINNER",
                                    "recipeId": "6577660abbac733a111c9427",
                                    "recipeName": "Pasta with vegetables and pesto",
                                    "forHowManyDays": 1
                                  }
                               ]
                             }
                           ]
                         }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        MvcResult savePlanResult = savePlanAction.andExpect(status().isOk()).andReturn();
        String jsonWithSaveResponse = savePlanResult.getResponse().getContentAsString();
        log.info("[8]: {}", jsonWithSaveResponse);
        assertThat(jsonWithSaveResponse).isEqualTo("Saved new plan");
        log.info(userPlans());

        // TODO: przeliczanie porcji w wyświetlanym przepisie
    }

    private @NotNull StringBuilder userPlans() {
        UserDto user = userFacade.getByUsername("testUser");
        System.out.println(user);
        List<Plan> plans = user.getPlans();
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
