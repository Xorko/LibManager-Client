package org.libmanager.client.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.libmanager.client.TestBase;
import org.libmanager.client.TestData;
import org.libmanager.client.util.I18n;
import org.libmanager.server.response.AuthenticatedUser;
import org.libmanager.server.response.Response;
import org.mockserver.client.MockServerClient;
import org.testfx.matcher.base.WindowMatchers;

import java.time.LocalDate;

import static org.libmanager.client.JavaFXIds.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


public class LoginPage {

    private final TestBase driver;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public LoginPage(TestBase driver) {
        this.driver = driver;
    }

    public LoginPage login(String username, String password) {
        clearOutInputFields();
        enterUsername(username).enterPassword(password);
        submit();
        return this;
    }

    public LoginPage enterUsername(String username) {
        TextField usernameField = driver.find(USERNAME_FIELD);
        usernameField.setText(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        TextField passwordField = driver.find(PASSWORD_FIELD);
        passwordField.setText(password);
        return this;
    }

    public LoginPage submit() {
        driver.clickOn(LOGIN_BUTTON);
        return this;
    }

    public LoginPage clearOutInputFields() {
        clearUsername().clearPassword();
        return this;
    }

    public LoginPage clearUsername() {
        TextField username = driver.find(USERNAME_FIELD);
        username.clear();
        return this;
    }

    public LoginPage clearPassword() {
        TextField password = driver.find(PASSWORD_FIELD);
        password.clear();
        return this;
    }

    public Label errorLabel() {
        return driver.find(ERROR_MESSAGE);
    }

    public static void createLoginExpectations(MockServerClient client) {
        try {
            client.reset();
            String content = objectMapper.writeValueAsString(new Response<>(Response.Code.NOT_FOUND, TestData.getInvalidUser()));

            client.when(
                    request()
                            .withMethod("POST")
                            .withPath("/account/login")
                            .withBody("username=Invalid&password=Valid")
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );

            content = objectMapper.writeValueAsString(new Response<>(Response.Code.INVALID_PASSWORD, TestData.getInvalidUser()));

            client.when(
                    request()
                            .withMethod("POST")
                            .withPath("/account/login")
                            .withBody("username=Valid&password=Invalid")
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );

            content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, TestData.getValidUser()));

            client.when(
                    request()
                            .withMethod("POST")
                            .withPath("/account/login")
                            .withBody("username=Valid&password=Valid")
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
