package ru.steps;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Assert;
import ru.constants.Api;
import ru.constants.ContentType;
import ru.pojo.Order.OrderAuthorizedResponse;
import ru.pojo.Order.OrderRequest;
import ru.pojo.Order.OrderUnauthorizedResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class StepsForOrder {

    private List<String> correctIngredients = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa79", "61c0c5a71d1f82001bdaaa77", "61c0c5a71d1f82001bdaaa6c"));
    private String incorrectIngredient = "60d3b41abdacab0026a733c6";

    /* Получить ответ от сервиса создания заказа, пользователь не авторизован */
    @Step("Get authorized order response")
    public Response getOrderResponse(OrderRequest request, String userBearer){
        Response response = given()
                .header("Content-type", ContentType.CONTENT_TYPE)
                .and()
                .header("Authorization", userBearer)
                .and()
                .body(request)
                .when()
                .post(Api.ORDERS_API_ENDPOINT);
        return response;
    }

    /* Получить ответ от сервиса создания заказа, пользователь не авторизован */
    @Step("Get unauthorized order response")
    public Response getOrderResponse(OrderRequest request){
        Response response = given()
                .header("Content-type", ContentType.CONTENT_TYPE)
                .and()
                .body(request)
                .when()
                .post(Api.ORDERS_API_ENDPOINT);
        return response;
    }

    /* Добавить существующие ингридиенты */
    @Step("Add correct ingredients to order")
    public void addCorrectIngredientToOrder(ArrayList<String> ingredients){
        ingredients.addAll(correctIngredients);
    }

    /* Добавить несуществующий ингридиент */
    @Step("Add incorrect ingredient to order")
    public void addIncorrectIngredientToOrder(ArrayList<String> ingredients){
        ingredients.add(incorrectIngredient);
    }

    /* Получить ответ класса OrderAuthorizedResponse */
    @Step("Get order authorized response")
    public OrderAuthorizedResponse getOrderAuthorizedResponse(Response response){
        String responseBody = response.getBody().asString();
        Gson gson = new Gson();
        OrderAuthorizedResponse orderAuthorizedResponse = gson.fromJson(responseBody, OrderAuthorizedResponse.class);
        return orderAuthorizedResponse;
    }

    /* Получить ответ класса OrderUnauthorizedResponse */
    @Step("Get order unauthorized response")
    public OrderUnauthorizedResponse getOrderUnauthorizedResponse(Response response){
        String responseBody = response.getBody().asString();
        Gson gson = new Gson();
        OrderUnauthorizedResponse orderUnauthorizedResponse = gson.fromJson(responseBody, OrderUnauthorizedResponse.class);
        return orderUnauthorizedResponse;
    }

    /* Проверить значения в заказе авторизованного пользователя */
    @Step("Check authorized response values")
    public void checkAuthorizedResponseValues(OrderAuthorizedResponse response, String ownerName, String orderName){
        Assert.assertEquals(ownerName, response.getOrder().getOwner().getName());
        Assert.assertEquals(orderName, response.getName());
        Assert.assertTrue(response.getOrder().getNumber() > 0);
    }

    /* Проверить значения в заказе авторизованного пользователя */
    @Step("Check unauthorized response values")
    public void checkUnauthorizedResponseValues(OrderUnauthorizedResponse response, String orderName){
        Assert.assertEquals(orderName, response.getName());
        Assert.assertTrue(response.getOrder().getNumber() > 0);
    }


    /* Проверка кодов ответов */
    /* Успешный ответ */
    @Step("Check response is success and status code is 200")
    public void checkStatusCodeIs200(Response response){
        response
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Step("Check response is not success and status code is 400, check response message")
    public void checkStatusCodeIs400(Response response, String expectMessage){
        response
                .then()
                .assertThat()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo(expectMessage));
    }
}
