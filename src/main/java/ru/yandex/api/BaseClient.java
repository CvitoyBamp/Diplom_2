package ru.yandex.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.model.Client;
import ru.yandex.model.Token;

import static io.restassured.RestAssured.given;
import static ru.yandex.api.BaseApi.*;

public class BaseClient {

    @Step("Create DELETE request to /api/auth/user")
    public static Response deleteClient(Client client) {
        return given()
                .header("Content-type", "application/json")
                .body(client)
                .delete(updateClientAPI);
    }

    @Step("Create POST request to /api/auth/register")
    public static Response createClient(Client client) {
        return given()
                .header("Content-type", "application/json")
                .body(client)
                .post(createClientAPI);
    }

    @Step("Create POST request to /api/auth/login")
    public static Response authClient(Client client) {
        return given()
                .header("Content-type", "application/json")
                .body(new Client(client.getEmail(), client.getPassword()))
                .post(authClientAPI);
    }

    @Step("Create POST request to /api/auth/logout")
    public static Response logoutClient(Token token) {
        return given()
                .header("Content-type", "application/json")
                .body(token)
                .post(logoutClientAPI);
    }

    @Step("Create PATCH request to /api/auth/user")
    public static Response updateClient(Client client, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(client)
                .patch(updateClientAPI);
    }
}
