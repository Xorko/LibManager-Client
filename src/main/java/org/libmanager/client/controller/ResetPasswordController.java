package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.libmanager.client.App;
import org.libmanager.client.I18n;

import java.net.URL;
import java.util.ResourceBundle;

public class ResetPasswordController implements Initializable {

    @FXML
    private TextField clearTokenField;
    @FXML
    private PasswordField hiddenTokenField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField newPasswordConfirmationField;

    private App app;
    private Stage dialogStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Hide token when not focused
        hiddenTokenField.textProperty().bindBidirectional(clearTokenField.textProperty());

        clearTokenField.focusedProperty().addListener(event -> {
            if (!clearTokenField.isFocused()) {
                clearTokenField.setVisible(false);
                hiddenTokenField.setVisible(true);
            }
        });

        hiddenTokenField.focusedProperty().addListener(event -> {
            if (hiddenTokenField.isFocused()) {
                hiddenTokenField.setVisible(false);
                clearTokenField.setVisible(true);
                clearTokenField.requestFocus();
                clearTokenField.selectEnd();
            }
        });
    }

    @FXML
    private void handleResetPassword() {
        if (fieldsAreCorrect()) {
            // TODO: Send password modification request
        }
    }

    private boolean fieldsAreCorrect() {
        String errMessage = "";
        if (hiddenTokenField.getText() == null || hiddenTokenField.getText().length() == 0) {
            errMessage += I18n.getBundle().getString("resetpassword.label.error.notoken") + '\n';
        }
        if (newPasswordField.getText() == null || newPasswordField.getText().length() == 0 ||
                newPasswordConfirmationField.getText() == null || newPasswordConfirmationField.getLength() == 0 ||
                !newPasswordField.getText().equals(newPasswordConfirmationField.getText())) {
            errMessage += I18n.getBundle().getString("resetpassword.label.error.passwordsmustmatch") + '\n';
        }
        if (errMessage.length() == 0) {
            return true;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle(I18n.getBundle().getString("login.label.invalidfields.title"));
        alert.setHeaderText(I18n.getBundle().getString("resetpassword.label.error.invalidfields.header"));
        alert.setContentText(errMessage);

        alert.showAndWait();

        return false;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

}
