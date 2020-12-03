package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.libmanager.client.App;
import org.libmanager.client.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class LoginController {

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
    private void initialize() {
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
    private boolean handleLogin() {
        if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
            String hashed = BCrypt.hashpw(password.getText(), BCrypt.gensalt());
            //TODO Send login request to the server and analyze the reply
            //TODO Set the error message according to the reply

            // False admin for test purposes
            User usr = new User("admin", "12345", true);
            app.setLoggedInUser(usr);
            if (usr.isAdmin()) {
                app.toggleAdminMenu();
            }
            app.toggleLoginMenu();
            app.toggleLogoutMenuItem();
            dialogStage.close();
            return true;
        }
        errorMessage.setText("Tous les champs doivent être complétés");
        errorMessage.setVisible(true);
        return false;
    }
}
