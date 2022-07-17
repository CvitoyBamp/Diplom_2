package ru.yandex.api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.model.Client;
import ru.yandex.model.Token;

import static org.hamcrest.Matchers.*;

public class ClientChangeDataTests {

    private Client client;
    private Client clientWithNewName;
    private Client clientWithNewEmail;
    private Client clientWithSameEmail;
    private static Response response;
    private String accessToken;
    private String refreshToken;

    @Before
    public void getTokenAndAuth(){
        client = new Client(RandomStringUtils.randomAlphabetic(7)+"@yandex.ru", RandomStringUtils.randomAlphanumeric(10),RandomStringUtils.randomAlphabetic(10));
        clientWithNewName = new Client(client.getEmail(), client.getPassword(), RandomStringUtils.randomAlphanumeric(10));
        clientWithNewEmail = new Client(RandomStringUtils.randomAlphabetic(7)+"@yandex.ru", client.getPassword(), client.getName());
         BaseClient.createClient(client);
        response = BaseClient.authClient(client).then().extract().response();
        accessToken = response.path("accessToken").toString();
        refreshToken = response.path("refreshToken").toString();
        BaseClient.logoutClient(new Token(refreshToken));
    }

    @After
    public void deleteClient(){
        BaseClient.deleteClient(client);
        BaseClient.deleteClient(clientWithNewName);
        BaseClient.deleteClient(clientWithNewEmail);
    }

    @Test
    @DisplayName("Change client email")
    @Description("Should return HTTP200 and client's email should be changed")
    public void shouldUpdateClientDataWithNewEmail(){
        BaseClient.updateClient(clientWithNewEmail, accessToken)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("user.email", equalTo(clientWithNewEmail.getEmail().toLowerCase()))
                .and()
                .body("user.name", equalTo(clientWithNewEmail.getName()));
    }

    @Test
    @DisplayName("Change client name")
    @Description("Should return HTTP200 and client's name should be changed")
    public void shouldUpdateClientDataWithNewName(){
        BaseClient.updateClient(clientWithNewName, accessToken)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("user.email", equalTo(clientWithNewName.getEmail().toLowerCase()))
                .and()
                .body("user.name", equalTo(clientWithNewName.getName()));
    }

    @Test
    @DisplayName("Request with already exist email")
    @Description("Should return HTTP403 and client's email should be the same")
    public void tryToChangeEmailWithSameData(){
        BaseClient.updateClient(client, accessToken)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @DisplayName("Try change email without auth")
    @Description("Should return HTTP401 and data shouldn't changed")
    public void tryToChangeDataWithNoAuth(){
        BaseClient.updateClient(client, "")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }

}
