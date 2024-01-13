package ru;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.constants.Api;
import ru.pojo.UserRegister;
import ru.steps.*;

public class CreateUserTest {
    StepsForUser steps = new StepsForUser();

    @Before
    public void setUp() {
        RestAssured.baseURI= Api.BASE_URI;
    }

    @Test
    @DisplayName("Register user with correct data set")
    public void createUserIsSuccessful(){
        UserRegister userRegister = steps.createCorrectUserRegister();

        Response registerApiResponse = steps.getResponseFromApiCreate(userRegister);
        steps.checkResponseStatusCode(registerApiResponse, 200, true);

        String userBearer = steps.getBearerTokenFromResponse(registerApiResponse);
        steps.removeUserByBearer(userBearer);
    }

    @Test
    @DisplayName("Register user exist data set")
    public void createUserIsUnsuccessful(){
        UserRegister userRegister = steps.createCorrectUserRegister();

        Response registerApiResponse1 = steps.getResponseFromApiCreate(userRegister);
        steps.checkResponseStatusCode(registerApiResponse1, 200, true);

        Response registerApiResponse2 = steps.getResponseFromApiCreate(userRegister);
        steps.checkResponseStatusCode(registerApiResponse2, 403, false, "User already exists");

        String userBearer = steps.getBearerTokenFromResponse(registerApiResponse1);
        steps.removeUserByBearer(userBearer);
    }

    @Test
    @DisplayName("Register user with incorrect data set")
    public void createUserWithIncorrectDateFailed(){
        UserRegister userRegister = steps.createIncorrectUserRegister();

        Response registerApiResponse = steps.getResponseFromApiCreate(userRegister);
        steps.checkResponseStatusCode(registerApiResponse, 403, false, "Email, password and name are required fields");
    }

}
