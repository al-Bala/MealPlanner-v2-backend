package pl.mealplanner.feature;

import com.mealplannerv2.auth.infrastructure.controller.dto.AuthResponse;
import com.mealplannerv2.auth.infrastructure.controller.error.response.RegisterErrorResponse;
import com.mealplannerv2.user.controller.response.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.mealplanner.BaseIntegrationTest;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginAndRegisterIntegrationTest extends BaseIntegrationTest {

    @Test
    public void login_and_register() throws Exception {

        //step 1: user tried to get JWT accessToken by requesting POST /login with email=someEmail, password=somePassword and system returned UNAUTHORIZED(401)
        // given & when
        ResultActions failedLoginRequest = mockMvc.perform(post("/auth/login")
                .servletPath("/auth/login")
                .content("""
                        {
                        "email": "someEmail",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        failedLoginRequest
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("""
                        {
                          "message": "Bad Credentials"
                        }
                        """.trim()));


        //step 2: user made GET /plan with no jwt accessToken and system returned UNAUTHORIZED(401)
        // given & when
        ResultActions failedGetPlanRequest = mockMvc.perform(get("/plan")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
//        failedGetPlanRequest.andExpect(status().isForbidden());
        failedGetPlanRequest.andExpect(status().isUnauthorized());


        //step 3: user made POST /register with existing username and email and system returned status BAD_REQUEST(400)
        // given & when
        ResultActions invalidRegisterAction = mockMvc.perform(post("/auth/register")
                .servletPath("/auth/register")
                .content("""
                        {
                        "username": "testUser",
                        "email": "email@email.pl",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult invalidRegisterActionResult = invalidRegisterAction.andExpect(status().isBadRequest()).andReturn();
        String invalidRegisterActionResultJson = invalidRegisterActionResult.getResponse().getContentAsString();
        RegisterErrorResponse invalidRegisterResult = objectMapper.readValue(invalidRegisterActionResultJson, RegisterErrorResponse.class);
        assertAll(
                () -> assertThat(invalidRegisterResult.isUsernameValid()).isFalse(),
                () -> assertThat(invalidRegisterResult.isEmailValid()).isFalse()
        );


        //step 4: user made POST /register with username=someUser, email=someEmail and system registered user with status CREATED(201)
        // given & when
        ResultActions registerAction = mockMvc.perform(post("/auth/register")
                .servletPath("/auth/register")
                .content("""
                        {
                        "username": "someUser",
                        "email": "someEmail",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult registerActionResult = registerAction.andExpect(status().isCreated()).andReturn();
        String username = registerActionResult.getResponse().getContentAsString();
        assertAll(
                () -> assertThat(username).isEqualTo("someUser")
        );


        //step 5: user tried to log in (get JWT accessToken) by requesting POST /login with email=someEmail, password=somePassword
        // and system returned status OK(200) and accessToken=AAAA.BBBB.CCC and refreshToken=AAAA.BBBB.CCC
        // given & when
        ResultActions successLoginRequest = mockMvc.perform(post("/auth/login")
                .servletPath("/auth/login")
                .content("""
                        {
                        "email": "someEmail",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult mvcResult = successLoginRequest.andExpect(status().isOk()).andReturn();
        String successLoginResult = mvcResult.getResponse().getContentAsString();
        AuthResponse loginResult = objectMapper.readValue(successLoginResult, AuthResponse.class);
        String accessToken = loginResult.accessToken();
        assertAll(
                () -> assertThat(loginResult.username()).isEqualTo("someUser"),
                () -> assertThat(accessToken).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$"))
        );


        //step 5: user made GET /users/{username}/profile with header “Authorization: Bearer AAAA.BBBB.CCC”
        // and system returned OK(200) with profile data
        // given & when
        ResultActions showProfile = mockMvc.perform(get("/users/" + loginResult.username() + "/profile")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult getProfileAction = showProfile.andExpect(status().isOk()).andReturn();
        String profileResult = getProfileAction.getResponse().getContentAsString();
        Profile profile = objectMapper.readValue(profileResult, Profile.class);
        assertAll(
                () -> assertThat(profile.username()).isEqualTo("someUser"),
                () -> assertThat(profile.plans()).isEmpty()
        );
    }
}
