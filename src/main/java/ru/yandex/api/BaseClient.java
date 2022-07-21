package ru.yandex.api;

import io.restassured.response.Response;
import ru.yandex.model.Client;
import ru.yandex.model.Token;

import static io.restassured.RestAssured.given;
import static ru.yandex.api.BaseApi.*;

public class BaseClient {

    public static Response deleteClient(Client client) {
        return given()
                .header("Content-type", "application/json")
                .body(client)
                .delete(updateClientAPI);
    }

    public static Response createClient(Client client) {
        return given()
                .header("Content-type", "application/json")
                .body(client)
                .post(createClientAPI);
    }

    public static Response authClient(Client client) {
        return given()
                .header("Content-type", "application/json")
                .body(new Client(client.getEmail(), client.getPassword()))
                .post(authClientAPI);
    }

    public static Response logoutClient(Token token) {
        return given()
                .header("Content-type", "application/json")
                .body(token)
                .post(logoutClientAPI);
    }

    public static Response updateClient(Client client, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(client)
                .patch(updateClientAPI);
    }
}
