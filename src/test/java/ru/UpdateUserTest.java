package ru;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.constants.Api;
import ru.pojo.User.UserRegister;
import ru.steps.*;
import ru.testdata.UserData;

public class UpdateUserTest {

    StepsForUser steps = new StepsForUser();
    UserRegister userRegister;
    String userBearer;

    @Before
    public void setUp() {
        RestAssured.baseURI= Api.BASE_URI;
        userRegister = steps.createCorrectUserRegister();

        Response registerApiResponse = steps.getResponseFromApiCreate(userRegister);
        steps.checkResponseStatusCode(registerApiResponse, 200, true);

        userRegister.setEmail(UserData.INCORRECT_EMAIL);
        userRegister.setPassword(UserData.INCORRECT_PASSWORD);
        userRegister.setName(UserData.INCORRECT_NAME);

        userBearer = steps.getBearerTokenFromResponse(registerApiResponse);

    }

    @Test
    @DisplayName("Check update user data with authorization is success")
    public void updateUserDataWithAuthorization(){
        steps.getAuthorizedUpdateUserApi(userRegister, userBearer);
    }

    @Test
    @DisplayName("Check update user data without authorization is failed")
    public void updateUserDataWithoutAuthorizationIsFailed(){
        steps.getUnauthorizedUpdateUserApi(userRegister);
    }

    @After
    public void deleteUserAfterTests(){
        steps.removeUserByBearer(userBearer);
    }

}
