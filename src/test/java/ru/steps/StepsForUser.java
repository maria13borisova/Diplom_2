package ru.steps;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Assert;
import ru.constants.Api;
import ru.constants.ContentType;
import ru.pojo.User.UserLogin;
import ru.pojo.User.UserRegister;
import ru.pojo.User.UserResponse;
import ru.testdata.UserData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class StepsForUser {

    public StepsForUser(){

    }
    /* Получить ответ от сервиса Регистрации */
    @Step("Get response from API Register user")
    public Response getResponseFromApiCreate(UserRegister user){
        Response response = given()
                .header("Content-type", ContentType.CONTENT_TYPE)
                .and()
                .body(user)
                .when()
                .post(Api.USER_REGISTER_API_ENDPOINT);
        return response;
    }

    /* Получить ответ от сервиса Логина */
    @Step("Get response from API Login user")
    public Response getResponseFromApiLogin(UserLogin user){
        Response response = given()
                .header("Content-type", ContentType.CONTENT_TYPE)
                .and()
                .body(user)
                .when()
                .post(Api.USER_LOGIN_API_ENDPOINT);
        return response;
    }

    /* Обновить данные пользователя */
    @Step("Update user with authorization is successful")
    public void getAuthorizedUpdateUserApi(UserRegister user, String userBearer){
        given()
                .header("Authorization", userBearer)
                .when()
                .patch(Api.USER_DELETE_OR_PATCH_API_ENDPOINT)
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    /* Обновить данные пользователя */
    @Step("Update user without authorization is failed")
    public void getUnauthorizedUpdateUserApi(UserRegister user){
        given()
                .when()
                .patch(Api.USER_DELETE_OR_PATCH_API_ENDPOINT)
                .then()
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    /* Создать UserRegister с корректными значениями */
    @Step("Create correct user register object")
    public UserRegister createCorrectUserRegister(){
        return new UserRegister(UserData.CORRECT_EMAIL, UserData.CORRECT_PASSWORD, UserData.CORRECT_LOGIN);
    }

    /* Создать UserRegister с некорректными значениями */
    @Step("Create incorrect user register object")
    public UserRegister createIncorrectUserRegister(){
        return new UserRegister(UserData.CORRECT_EMAIL, null, UserData.CORRECT_LOGIN);
    }

    /* Создать UserLogin с корректными значениями */
    @Step("Create correct user login object")
    public UserLogin createCorrectUserLogin(){
        return new UserLogin(UserData.CORRECT_EMAIL, UserData.CORRECT_PASSWORD);
    }

    /* Создать UserLogin с некорректным паролем */
    @Step("Create incorrect user login object, incorrect pasword")
    public UserLogin createIncorrectUserLoginWithIncorrectPassword(){
        return new UserLogin(UserData.CORRECT_EMAIL, UserData.INCORRECT_PASSWORD);
    }

    /* Создать UserLogin с некорректным email */
    @Step("Create incorrect user login object, incorrect email")
    public UserLogin createIncorrectUserLoginWithIncorrectEmail(){
        return new UserLogin(UserData.INCORRECT_EMAIL, UserData.CORRECT_PASSWORD);
    }

    /* проверить ответ, 3 входных параметра */
    @Step("Check response status code and success status")
    public void checkResponseStatusCode(Response response, int expectStatusCode, boolean successStatus){
        int actualStatusCode = response.getStatusCode();
        boolean actualSuccessStatus = response.jsonPath().get("success");
        boolean expectedSuccessStatus = true;
        Assert.assertEquals(("Check status code is " + expectStatusCode), expectStatusCode, response.getStatusCode());
        Assert.assertEquals(("Check success is " + successStatus), successStatus, response.jsonPath().get("success"));
    }

    /* проверить ответ, 4 входных параметра */
    @Step("Check response status code, success status and message")
    public void checkResponseStatusCode(Response response, int expectStatusCode, boolean successStatus, String message){
        int actualStatusCode = response.getStatusCode();
        boolean actualSuccessStatus = response.jsonPath().get("success");
        boolean expectedSuccessStatus = true;
        Assert.assertEquals(("Check status code is " + expectStatusCode), expectStatusCode, response.getStatusCode());
        Assert.assertEquals(("Check success is " + successStatus), successStatus, response.jsonPath().get("success"));
        Assert.assertEquals(("Check message is " + message), message, response.jsonPath().get("message"));
    }

    /* получить токен авторизации */
    @Step("Get Bearer token from response")
    public String getBearerTokenFromResponse(Response response){
        String responseBody = response.getBody().asString();
        Gson gson = new Gson();
        UserResponse userResponse = gson.fromJson(responseBody, UserResponse.class);
        String accessToken = userResponse.getAccessToken();
        return accessToken;
    }

    /* Удаление пользователя после тестов */
    @Step("Remove user after test")
    public void removeUserByBearer(String userBearer){
        given()
                .header("Authorization", userBearer)
                .when()
                .delete(Api.USER_DELETE_OR_PATCH_API_ENDPOINT)
                .then()
                .assertThat()
                .statusCode(202)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

}
