package ru;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.constants.Api;
import ru.pojo.Order.OrderAuthorizedResponse;
import ru.pojo.Order.OrderRequest;
import ru.pojo.Order.OrderUnauthorizedResponse;
import ru.pojo.User.UserRegister;
import ru.steps.StepsForOrder;
import ru.steps.StepsForUser;
import ru.testdata.UserData;


public class OrderTest {
    StepsForUser userSteps = new StepsForUser();
    StepsForOrder orderSteps = new StepsForOrder();
    UserRegister userRegister;
    OrderRequest orderRequest;
    String userBearer;
    String expectOrderName = "Фалленианский краторный экзо-плантаго бургер";

    @Before
    public void setUp() {
        RestAssured.baseURI= Api.BASE_URI;
        userRegister = userSteps.createCorrectUserRegister();
        orderRequest = new OrderRequest();

        Response registerApiResponse = userSteps.getResponseFromApiCreate(userRegister);
        userSteps.checkResponseStatusCode(registerApiResponse, 200, true);


        userBearer = userSteps.getBearerTokenFromResponse(registerApiResponse);
    }

    @Test
    @DisplayName("Create authorized order is success. Create correct order is success.")
    public void createAuthorizedOrderIsSuccess(){
        orderSteps.addCorrectIngredientToOrder(orderRequest.getIngredients());
        Response response = orderSteps.getOrderResponse(orderRequest, userBearer);
        orderSteps.checkStatusCodeIs200(response);
        OrderAuthorizedResponse orderAuthorizedResponse = orderSteps.getOrderAuthorizedResponse(response);
        orderSteps.checkAuthorizedResponseValues(orderAuthorizedResponse, UserData.CORRECT_LOGIN, expectOrderName);
    }

    @Test
    @DisplayName("Create unauthorized order is success")
    public void createUnauthorizedOrderIsSuccess(){
        orderSteps.addCorrectIngredientToOrder(orderRequest.getIngredients());
        Response response = orderSteps.getOrderResponse(orderRequest);
        orderSteps.checkStatusCodeIs200(response);

        OrderUnauthorizedResponse orderUnauthorizedResponse = orderSteps.getOrderUnauthorizedResponse(response);
        orderSteps.checkUnauthorizedResponseValues(orderUnauthorizedResponse, expectOrderName);
    }

    @Test
    @DisplayName("Create empty order is failed")
    public void createEmpryOrderIsFailed(){
        String errorMessage = "Ingredient ids must be provided";
        Response response = orderSteps.getOrderResponse(orderRequest);
        orderSteps.checkStatusCodeIs400(response,
                errorMessage);
    }

    @Test
    @DisplayName("Create order with incorrect ingredients is failed")
    public void createIncorrectIngredientOrderIsFailed(){
        String errorMessage = "One or more ids provided are incorrect";
        orderSteps.addIncorrectIngredientToOrder(orderRequest.getIngredients());

        Response response = orderSteps.getOrderResponse(orderRequest);
        orderSteps.checkStatusCodeIs400(response, errorMessage);
    }

    @Test
    @DisplayName("Get authorized user orders")
    public void getAuthorizedUserOrders(){
        /* создать заказы для пользователя */
        for (int i = 0; i < 3; i++){
            orderSteps.addCorrectIngredientToOrder(orderRequest.getIngredients());
            Response response = orderSteps.getOrderResponse(orderRequest, userBearer);
            orderSteps.checkStatusCodeIs200(response);
        }

        Response response = orderSteps.getUserOrdersResponse(userBearer);
        orderSteps.checkStatusCodeIs200(response);

        orderSteps.checkAuthorizedUserOrdersIsExist(response);

    }

    @Test
    @DisplayName("Get unauthorized user orders")
    public void getUnauthorizedUserOrders(){
        Response response = orderSteps.getUserOrdersResponse();
        orderSteps.checkStatusCodeIs401(response);
    }

    @After
    public void deleteUserAfterTests(){
        userSteps.removeUserByBearer(userBearer);
    }
}
