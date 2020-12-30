package org.libmanager.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.libmanager.client.App;
import org.libmanager.client.util.DateUtil;
import org.libmanager.client.util.I18n;
import org.libmanager.client.model.User;
import org.libmanager.client.service.Requests;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private GridPane loginRoot;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Label errorMessage;

    private Stage dialogStage;

    private App app;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Enter can be pressed instead of the login button
        // Escape can be used to close the dialog
        loginRoot.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                handleLogin();
            if (event.getCode() == KeyCode.ESCAPE)
                dialogStage.close();
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
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = null;
            try {
                String content = Requests.callLogin(username.getText(), password.getText());
                if (content != null) {
                    root = mapper.readTree(content);
                } else {
                    errorMessage.setText(I18n.getBundle().getString("server.connection.failed"));
                    errorMessage.setVisible(true);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (root != null) {
                JsonNode receivedUser = root.get("content");
                // True if the user can log in
                boolean valid = receivedUser.get("valid").asBoolean();
                // True if the user is admin
                boolean admin = receivedUser.get("admin").asBoolean();
                String token = receivedUser.get("token").asText();
                String username = receivedUser.get("username").asText();
                LocalDate birthday = DateUtil.parse(receivedUser.get("birthday").asText());
                LocalDate registrationDate = DateUtil.parse(receivedUser.get("registrationDate").asText());
                if (!valid) {
                    if (root.get("code").asText().equals("INVALID_PASSWORD"))
                        errorMessage.setText(I18n.getBundle().getString("label.incorrect.password"));
                    else
                        errorMessage.setText(I18n.getBundle().getString("label.incorrect.username"));
                    errorMessage.setVisible(true);
                } else {
                    errorMessage.setVisible(false);
                    app.setLoggedInUser(new User(username, token, admin, birthday, registrationDate));
                    if (admin) {
                        app.toggleAdminMenu();
                    }
                    app.toggleUsernameMenuItem();
                    app.toggleReservationOverview();
                    app.toggleLoginMenuItem();
                    app.toggleLogoutMenuItem();
                    // May cause segfault if not run later
                    Platform.runLater(() -> dialogStage.close());
                }
            } else {
                errorMessage.setText("Can't connect to the server");
                errorMessage.setVisible(true);
            }
        } else {
            errorMessage.setText(I18n.getBundle().getString("label.allfieldsmustbecompleted"));
            errorMessage.setVisible(true);
        }
    }

    @FXML
    private void handleForgottenPassword() {
        app.showPasswordResetDialog();
    }
}
