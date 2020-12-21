package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.libmanager.client.App;
import org.libmanager.client.I18n;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetPasswordController {

    @FXML
    private GridPane resetPasswordRoot;
    @FXML
    private TextField emailTextField;
    @FXML
    private Label titleLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private TextField codeTextField;
    @FXML
    private Label codeLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmationPasswordField;
    @FXML
    private Label newPasswordLabel;
    @FXML
    private Label confirmationLabel;
    @FXML
    private Button confirmButton;

    private App app;
    private Stage dialogStage;

    @FXML
    public void initialize() {
        resetPasswordRoot.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                handleConfirm();
            }
            if(event.getCode() == KeyCode.ESCAPE) {
                dialogStage.close();
            }
        });

        titleLabel.setText(I18n.getBundle().getString("resetpassword.email.label.title"));
        codeTextField.setVisible(false);
        codeLabel.setVisible(false);
        confirmationLabel.setVisible(false);
        confirmationPasswordField.setVisible(false);
        newPasswordLabel.setVisible(false);
        newPasswordField.setVisible(false);
        newPasswordLabel.setText(I18n.getBundle().getString("resetpassword.newpassword.label.newpassword"));
        confirmationLabel.setText(I18n.getBundle().getString("resetpassword.newpassword.label.confirm"));
        confirmButton.setText(I18n.getBundle().getString("resetpassword.button.label.next"));
    }

    public void handleConfirm() {
        if(emailTextField.isVisible()) {
            confirmEMail();
        } else if(codeTextField.isVisible()) {
            confirmCode();
        } else if(newPasswordField.isVisible()) {
            confirmNewPassword();
        }
    }

    public void confirmEMail(){
        Matcher emailMatcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
                .matcher(emailTextField.getText());

        // TODO verify that an account correspond to the email

        if(emailTextField.getText().isEmpty() || !emailMatcher.matches()) {
            errorLabel.setText(I18n.getBundle().getString("resetpassword.email.label.error"));
            errorLabel.setVisible(true);
        } else {
            errorLabel.setVisible(false);
            emailTextField.setVisible(false);
            emailLabel.setVisible(false);
            codeTextField.setVisible(true);
            codeLabel.setVisible(true);
            titleLabel.setText(I18n.getBundle().getString("resetpassword.code.label.title"));
        }
    }

    public void confirmCode() {
        // TODO check if the code is correct

        errorLabel.setVisible(false);
        codeTextField.setVisible(false);
        codeLabel.setVisible(false);
        titleLabel.setText(I18n.getBundle().getString("resetpassword.newpassword.label.title"));
        confirmButton.setText(I18n.getBundle().getString("resetpassword.button.label.confirm"));

        newPasswordLabel.setVisible(true);
        newPasswordField.setVisible(true);
        confirmationLabel.setVisible(true);
        confirmationPasswordField.setVisible(true);
    }

    public void confirmNewPassword() {

        Matcher passwordMatcher = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*_-]).{8,}$")
                .matcher(newPasswordField.getText());

        if(newPasswordField.getText().isEmpty() || confirmationPasswordField.getText().isEmpty()) {
            errorLabel.setText(I18n.getBundle().getString("resetpassword.newpassword.label.error.empty"));
            errorLabel.setVisible(true);
        } else if(!newPasswordField.getText().equals(confirmationPasswordField.getText())) {
            errorLabel.setText(I18n.getBundle().getString("resetpassword.newpassword.label.error.nomatch"));
            errorLabel.setVisible(true);
        } else if(!passwordMatcher.matches()) {
            errorLabel.setText(I18n.getBundle().getString("resetpassword.newpassword.label.error.invalid"));
            errorLabel.setVisible(true);
        } else {
            // TODO change password in DB
            dialogStage.close();
        }

    }


    public void setApp(App app) {
        this.app = app;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
