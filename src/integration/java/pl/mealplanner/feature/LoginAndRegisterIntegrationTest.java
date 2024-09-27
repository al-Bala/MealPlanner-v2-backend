package pl.mealplanner.feature;

import com.mealplannerv2.auth.infrastructure.controller.dto.AuthResponse;
import com.mealplannerv2.auth.infrastructure.controller.error.response.RegisterErrorResponse;
import jakarta.servlet.http.Cookie;
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

        //step 1: user tried to get JWT accessToken by requesting POST /login with userId=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        // given & when
        ResultActions failedLoginRequest = mockMvc.perform(post("/auth/login")
                .servletPath("/auth/login")
                .content("""
                        {
                        "username": "someUser",
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


        //step 3: user made POST /register with existing userId and email and system returned status BAD_REQUEST(400)
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


        //step 4: user made POST /register with userId=someUser, email=someEmail and system registered user with status CREATED(201)
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
        String registerActionResultJson = registerActionResult.getResponse().getContentAsString();
        AuthResponse registerResult = objectMapper.readValue(registerActionResultJson, AuthResponse.class);
        assertAll(
                () -> assertThat(registerResult.username()).isEqualTo("someUser"),
                () -> assertThat(registerResult.accessToken()).isEqualTo("")
//                () -> assertThat(invalidRegisterResult.id()).isNotNull()
        );


        //step 5: user tried to log in (get JWT accessToken) by requesting POST /login with userId=someUser, password=somePassword
        // and system returned status OK(200) and accessToken=AAAA.BBBB.CCC and refreshToken=AAAA.BBBB.CCC
        // given & when
        ResultActions successLoginRequest = mockMvc.perform(post("/auth/login")
                .servletPath("/auth/login")
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
        MvcResult mvcResult = successLoginRequest.andExpect(status().isOk()).andReturn();
        Cookie accessToken = mvcResult.getResponse().getCookie("accessToken");
        Cookie refreshToken = mvcResult.getResponse().getCookie("refreshToken");
        String username = mvcResult.getResponse().getContentAsString();
        assertAll(
                () -> assertThat(username).isEqualTo("someUser"),
                () -> assertThat(accessToken.getValue()).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$")),
                () -> assertThat(refreshToken.getValue()).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$")),
                () -> assertThat(accessToken.getValue()).isNotEqualTo(refreshToken.getValue())
        );


//    //step 5: user made GET /plan with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200)
//    // given & when
//    ResultActions perform = mockMvc.perform(get("/plan")
//            .header("Authorization", "Bearer " + accessToken)
//            .contentType(MediaType.APPLICATION_JSON_VALUE)
//    );
//    // then
//    perform.andExpect(status().isOk());
    }
}
