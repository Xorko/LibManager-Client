package org.libmanager.client.controller;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.libmanager.client.App;
import org.libmanager.client.AppOnLogin;
import org.libmanager.client.TestBase;
import org.libmanager.client.pages.LoginPage;
import org.libmanager.client.util.I18n;
import org.libmanager.server.response.Response;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.netty.MockServer;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.WindowMatchers;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginTest extends TestBase {

    private static final String INVALID_USERNAME = "Invalid";
    private static final String INVALID_PASSWORD = "Invalid";
    private static final String VALID_USERNAME = "Valid";
    private static final String VALID_PASSWORD = "Valid";

    private LoginPage loginPage;

    @BeforeAll
    static void beforeAll(MockServerClient client) {
        LoginPage.createLoginExpectations(client);
    }

    @Override
    @BeforeEach
    public void beforeEachTest() throws Exception {
        FxToolkit.setupApplication(AppOnLogin.class);
        loginPage = new LoginPage(this);
    }

    // Login Unsuccessful

    @Test
    @DisplayName("If the user doesn't fill a field, error shown : fields empty")
    @Tag("ErrorLog")
    public void HandleLogin_MustShowError_WhenAFieldIsEmpty() {
        String expectedError = I18n.getBundle().getString("label.allfieldsmustbecompleted");

        loginPage.login("", "");

        ensureEventQueueComplete();
        assertThat(loginPage.errorLabel().getText()).isEqualTo(expectedError);
    }

    @Test
    @DisplayName("If username field is incorrect, an error must be shown : Username incorrect")
    @Tag("ErrorLog")
    public void HandleLogin_MustShowError_WhenUsernameIsIncorrect() {
        String expectedError = I18n.getBundle().getString("label.incorrect.username");

        loginPage.login(INVALID_USERNAME, VALID_PASSWORD);

        ensureEventQueueComplete();
        assertThat(loginPage.errorLabel().getText()).isEqualTo(expectedError);
        assertThat(loginPage.errorLabel().isVisible()).isTrue();
    }

    @Test
    @DisplayName("If the username is correct and the password incorrect, incorrect password error must be shown")
    @Tag("ErrorLog")
    public void HandleLogin_MustShowError_WhenPasswordIsIncorrect() {
        String expectedError = I18n.getBundle().getString("label.incorrect.password");

        loginPage.login(VALID_USERNAME, INVALID_PASSWORD);

        ensureEventQueueComplete();
        assertThat(loginPage.errorLabel().getText()).isEqualTo(expectedError);
        assertThat(loginPage.errorLabel().isVisible()).isTrue();
    }

    // Login With Success
    @Test
    @DisplayName("When a user logs the login dialog must close")
    public void HandleLogin_MustCloseDialog_WhenLoginSuccessful() {

        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        ensureEventQueueComplete();
        assertThrows(NoSuchElementException.class, () -> WindowMatchers.isShowing().matches(window(I18n.getBundle().getString("label.login.title"))));
    }
}
