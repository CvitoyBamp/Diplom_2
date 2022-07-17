package ru.yandex.api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.model.Client;

import static org.hamcrest.Matchers.equalTo;

public class ClientLoginTests {

    private Client client;
    private Client clientWithoutEmail;
    private Client clientWithoutPassword;
    private Client clientWithoutName;

    @Before
    public void clientData(){
        client = new Client(RandomStringUtils.randomAlphabetic(7)+"@yandex.ru", RandomStringUtils.randomAlphanumeric(10),RandomStringUtils.randomAlphabetic(10));
        clientWithoutEmail = new Client(null, client.getPassword(), client.getName());
        clientWithoutPassword = new Client(client.getEmail(), null, client.getName());
        clientWithoutName = new Client(client.getEmail(), client.getPassword(), null);
    }

    @After
    public void deleteClient(){
        BaseClient.deleteClient(client);
    }

    @Test
    @DisplayName("Create new client")
    @Description("Should return HTTP200 and create new client")
    public void shouldCreateClientStatusOk(){
        BaseClient.createClient(client)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Create client with same data")
    @Description("Should return HTTP403 and \"User already exists\" message")
    public void shouldReturn403WhileCreatingClientWithSameData() {
        BaseClient.createClient(client);
        BaseClient.createClient(client)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Create client with no email-data")
    @Description("Should return HTTP403 and \"Email, password and name are required fields\" message")
    public void shouldReturn403WhileCreatingClientWithNoEmailData() {
        BaseClient.createClient(clientWithoutEmail)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create client with no password")
    @Description("Should return HTTP403 and \"Email, password and name are required fields\" message")
    public void shouldReturn403WhileCreatingClientWithNoPasswordData() {
        BaseClient.createClient(clientWithoutPassword)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create client with no name")
    @Description("Should return HTTP403 and \"Email, password and name are required fields\" message")
    public void shouldReturn403WhileCreatingClientWithNoNameData() {
        BaseClient.createClient(clientWithoutName)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
