package ru.yandex.api;

import io.restassured.response.Response;
import ru.yandex.model.Client;
import ru.yandex.model.Ingredients;

import static io.restassured.RestAssured.given;
import static ru.yandex.api.BaseAPI.*;

public class BaseOrder {

    public static Response getIngredients() {
        return given()
                .header("Content-type", "application/json")
                .get(getIngredientsAPI);
    }

    public static Response createOrder(Ingredients ingredients, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(ingredients)
                .post(createOrderAPI);
    }

    public static Response getClientOrder(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .get(createOrderAPI);
    }
}
