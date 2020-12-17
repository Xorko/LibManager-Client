package org.libmanager.client.controller;

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
import org.mindrot.jbcrypt.BCrypt;

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
            app.toggleReservationOverviewMenuItem();
            dialogStage.close();
            return true;
        }
        errorMessage.setText(I18n.getBundle().getString("login.label.allfieldsmustbecompleted"));
        errorMessage.setVisible(true);
        return false;
    }

    @FXML
    private void handleForgottenPassword() {
        app.showResetPasswordDialog();
    }
}
