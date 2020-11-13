package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.libmanager.client.App;
import org.mindrot.jbcrypt.BCrypt;

public class LoginController {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Label errorMessage;

    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    private boolean handleLogin() {
        if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
            String hashed = BCrypt.hashpw(password.getText(), BCrypt.gensalt());
            //TODO Send login request to the server and analyze the reply
            //TODO Set the error message according to the reply
            return true;
        }
        errorMessage.setText("Fields must be completed");
        errorMessage.setVisible(true);
        return false;
    }
}
