package ru.yandex.api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.model.Client;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ClientAuthTests {

    private Client client;
    private Client clientWithInvalidPassword;
    private Client clientWithInvalidEmail;
    private Client clientWithNullData;

    @Before
    public void createClient(){
        client = new Client(RandomStringUtils.randomAlphabetic(7)+"@yandex.ru", RandomStringUtils.randomAlphanumeric(10),RandomStringUtils.randomAlphabetic(10));
        clientWithInvalidPassword = new Client(client.getEmail(), RandomStringUtils.randomAlphanumeric(10));
        clientWithInvalidEmail = new Client(RandomStringUtils.randomAlphanumeric(10), client.getPassword());
        clientWithNullData = new Client(null, client.getPassword());
        BaseClient.createClient(client);
    }

    @After
    public void deleteClient(){
        BaseClient.deleteClient(client);
    }

    @Test(timeout = 15000)
    @DisplayName("Auth client with exist data")
    @Description("Should return HTTP200 and client should be authorized")
    public void shouldAuthClientWithExistData(){
        BaseClient.authClient(client)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue());
    }

    @Test(timeout = 15000)
    @DisplayName("Auth client with invalid password")
    @Description("Should return HTTP401 and client shouldn't be authorized")
    public void shouldNotBeAuthorizedWithInvalidPassword(){
        BaseClient.authClient(clientWithInvalidPassword)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test(timeout = 15000)
    @DisplayName("Auth client with invalid email")
    @Description("Should return HTTP401 and client shouldn't be authorized")
    public void shouldNotBeAuthorizedWithInvalidEmail(){
        BaseClient.authClient(clientWithInvalidEmail)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test(timeout = 15000)
    @DisplayName("Auth client with no data email")
    @Description("Should return HTTP401 and client shouldn't be authorized")
    public void shouldNotBeAuthorizedWithNullEmail(){
        BaseClient.authClient(clientWithNullData)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }




}
