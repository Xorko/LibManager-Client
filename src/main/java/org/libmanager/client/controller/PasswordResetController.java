package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.libmanager.client.App;
import org.libmanager.client.service.Requests;
import org.libmanager.client.util.I18n;
import org.libmanager.client.util.ResponseUtil;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordResetController implements Initializable {

    @FXML
    private GridPane resetRoot;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label tokenLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label confirmationLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField clearTokenField;
    @FXML
    private PasswordField hiddenTokenField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmationField;
    @FXML
    private Button button;

    private App app;
    private Stage dialogStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button.setText(I18n.getBundle().getString("button.confirm"));
        button.setOnAction((event) -> handleUsernameConfirm());

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

        // Enter can be pressed instead of the button
        // Escape can be used to close the dialog
        resetRoot.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                button.fire();
            if (event.getCode() == KeyCode.ESCAPE)
                dialogStage.close();
        });
    }

    @FXML
    private void handleUsernameConfirm() {
        if (usernameField.getText().length() != 0) {
            String response = Requests.callGetResetPassword(usernameField.getText());
            if (ResponseUtil.analyze(response, dialogStage)) {

                // Change displayed fields
                usernameLabel.setVisible(false);
                usernameField.setVisible(false);
                tokenLabel.setVisible(true);
                passwordLabel.setVisible(true);
                confirmationLabel.setVisible(true);
                hiddenTokenField.setVisible(true);
                passwordField.setVisible(true);
                confirmationField.setVisible(true);
                button.setText(I18n.getBundle().getString("button.reset"));

                // Change the action of the button
                button.setOnAction((event) -> handleResetPassword());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(dialogStage);
                alert.setHeaderText(I18n.getBundle().getString("alert.token.sent.header"));
                alert.setContentText(I18n.getBundle().getString("alert.token.sent.content"));
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setHeaderText(I18n.getBundle().getString("alert.invalidfields.header"));
            alert.setContentText(I18n.getBundle().getString("label.allfieldsmustbecompleted"));
            alert.showAndWait();
        }
    }

    @FXML
    private void handleResetPassword() {
        if (passwordFieldsAreCorrect()) {
            String response = Requests.callPostResetPassword(clearTokenField.getText(), passwordField.getText());
            if (ResponseUtil.analyze(response, dialogStage)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(dialogStage);
                alert.setHeaderText(I18n.getBundle().getString("alert.password.changed.header"));
                alert.showAndWait();

                dialogStage.close();
            }
        }
    }

    private boolean passwordFieldsAreCorrect() {
        String errMessage = "";
        Matcher passwordMatcher = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*_-]).{8,}$")
                .matcher(passwordField.getText());

        if (hiddenTokenField.getText() == null || hiddenTokenField.getText().length() == 0)
            errMessage += I18n.getBundle().getString("label.error.notoken") + '\n';
        if (passwordField.getText() == null || passwordField.getText().length() == 0 ||
                confirmationField.getText() == null || confirmationField.getLength() == 0 ||
                !passwordField.getText().equals(confirmationField.getText()))
            errMessage += I18n.getBundle().getString("label.error.passwordsmustmatch") + '\n';
        if (!passwordMatcher.matches())
            errMessage += I18n.getBundle().getString("alert.invalid.password");
        if (errMessage.length() == 0)
            return true;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setHeaderText(I18n.getBundle().getString("alert.invalidfields.header"));
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
