package ru.yandex.api;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.model.Client;
import ru.yandex.model.Ingredients;
import ru.yandex.model.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class OrderTests {

    private ArrayList<String> ingredientsList;
    private Client client;
    private static Response response;
    private String accessToken;
    private String refreshToken;

    @Step("Initialization of test data")
    @Before
    public void getIngredientsData(){
       ingredientsList =  BaseOrder.getIngredients().then().extract().path("data._id");
       client = new Client(RandomStringUtils.randomAlphabetic(7)+"@yandex.ru", RandomStringUtils.randomAlphanumeric(10),RandomStringUtils.randomAlphabetic(10));
       BaseClient.createClient(client);
       response = BaseClient.authClient(client).then().extract().response();
       accessToken = response.path("accessToken").toString();
       refreshToken = response.path("refreshToken").toString();
    }

    @Step("Creating an order")
    @Test
    @DisplayName("Ingredients list correctly returned")
    @Description("Should return HTTP200 and list of ingredients")
    public void shouldReturnIngredientsList(){
        BaseOrder.getIngredients()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("data", notNullValue());
    }

    @Step("Creating an order")
    @Test
    @DisplayName("Order list of the authorized user")
    @Description("Should return HTTP200 and list of client order")
    public void shouldReturnClientOrderListWithAuth() {
       BaseOrder.getClientOrder(accessToken)
               .then()
               .assertThat()
               .statusCode(HttpStatus.SC_OK)
               .and()
               .body("success", is(true));
    }

    @Step("Creating an order")
    @Test
    @DisplayName("Order list of the unauthorized user")
    @Description("Should return HTTP401 because of unauthorized user")
    public void shouldReturnErrorWithNoAuth() {
        BaseOrder.getClientOrder("")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Creating an order")
    @Test
    @DisplayName("Create order of the authorized user")
    @Description("Should return HTTP200 and create order")
    public void shouldCreateOrderWithAuth() {
        BaseOrder.createOrder(new Ingredients(new ArrayList<>(ingredientsList.subList(0, 2))), accessToken)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("name", notNullValue())
                .and()
                .body("order", notNullValue());
    }

    @Step("Creating an order")
    @Test
    @DisplayName("Create order of the unauthorized user")
    @Description("Should return HTTP401 because of unauthorized user")
    public void shouldCreateOrderWithNoAuth() {
        BaseOrder.createOrder(new Ingredients(new ArrayList<>(ingredientsList.subList(4, 5))), "")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("name", notNullValue())
                .and()
                .body("order", notNullValue());
    }

    @Step("Creating an order")
    @Test
    @DisplayName("Create order with invalid hash of Ingredient")
    @Description("Should return HTTP500 because of uinvalid hash of Ingredient")
    public void shouldReturnErrorWhileInvalidHashIngredient() {
        BaseOrder.createOrder(new Ingredients(new ArrayList<String>(Collections.singleton(RandomStringUtils.randomAlphanumeric(24)))), accessToken)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Creating an order")
    @Test
    @DisplayName("Create order with no ingredient")
    @Description("Should return HTTP400 because of null Ingredient")
    public void shouldReturnErrorWhileNoDataIngredient() {
        BaseOrder.createOrder(new Ingredients(null), accessToken)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

}
