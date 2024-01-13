package ru;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.constants.Api;
import ru.pojo.UserLogin;
import ru.pojo.UserRegister;
import ru.steps.*;

public class LoginUserTest {

    StepsForUser steps = new StepsForUser();
    UserRegister userRegister;
    String userBearer;

    @Before
    public void setUp() {
        RestAssured.baseURI= Api.BASE_URI;
        userRegister = steps.createCorrectUserRegister();

        Response registerApiResponse = steps.getResponseFromApiCreate(userRegister);
        steps.checkResponseStatusCode(registerApiResponse, 200, true);

        userBearer = steps.getBearerTokenFromResponse(registerApiResponse);

    }

    @Test
    @DisplayName("Login with correct credentials")
    public void loginUserIsSuccessful(){
        UserLogin userLogin =  steps.createCorrectUserLogin();
        Response loginResponse  = steps.getResponseFromApiLogin(userLogin);
        steps.checkResponseStatusCode(loginResponse, 200, true);
    }

    @Test
    @DisplayName("Login with incorrect credentials: incorrect password")
    public void loginUserIsFailedIncorrectPassword(){
        UserLogin userLogin =  steps.createIncorrectUserLoginWithIncorrectPassword();
        Response loginResponse  = steps.getResponseFromApiLogin(userLogin);
        steps.checkResponseStatusCode(loginResponse, 401, false, "email or password are incorrect");
    }

    @Test
    @DisplayName("Login with incorrect credentials: incorrect email")
    public void loginUserIsFailedIncorrectEmail(){
        UserLogin userLogin =  steps.createIncorrectUserLoginWithIncorrectEmail();
        Response loginResponse  = steps.getResponseFromApiLogin(userLogin);
        steps.checkResponseStatusCode(loginResponse, 401, false, "email or password are incorrect");
    }

    @After
    public void deleteUserAfterTests(){
        steps.removeUserByBearer(userBearer);
    }

}
