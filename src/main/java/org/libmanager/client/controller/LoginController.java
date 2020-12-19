package org.libmanager.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.libmanager.client.App;
import org.libmanager.client.I18n;
import org.libmanager.client.model.User;
import org.libmanager.client.api.ServerAPI;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private GridPane loginRoot;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Hyperlink forgottenPassword;
    @FXML
    private Label errorMessage;

    private Stage dialogStage;

    private App app;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Enter can be pressed instead of the login button
        loginRoot.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleLogin() {
        if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
            //String hashed = BCrypt.hashpw(password.getText(), BCrypt.gensalt());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = null;
            try {
                root = mapper.readTree(ServerAPI.callLogin(username.getText(), password.getText()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (root != null) {
                // True if the user can log in
                boolean valid = root.get("valid").asBoolean();
                // True if the user is admin
                boolean admin = root.get("admin").asBoolean();
                String token = root.get("token").asText();
                String username = root.get("username").asText();
                if (!valid) {
                    errorMessage.setText(I18n.getBundle().getString("login.label.incorrect"));
                    errorMessage.setVisible(true);
                } else {
                    errorMessage.setVisible(false);
                    app.setLoggedInUser(new User(username, token, admin));
                    if (admin) {
                        app.toggleAdminMenu();
                    }
                    app.toggleLoginMenu();
                    app.toggleLogoutMenuItem();
                    app.toggleReservationOverviewMenuItem();
                    // May cause segfault if not run later
                    Platform.runLater(() -> dialogStage.close());
                }
            }
        } else {
            errorMessage.setText(I18n.getBundle().getString("login.label.allfieldsmustbecompleted"));
            errorMessage.setVisible(true);
        }
    }

    @FXML
    private void handleForgottenPassword() {
        app.showResetPasswordDialog();
    }
}
