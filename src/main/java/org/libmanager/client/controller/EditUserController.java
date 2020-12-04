package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.libmanager.client.App;
import org.libmanager.client.model.User;
import org.libmanager.client.util.DateUtil;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EditUserController {

    @FXML
    private GridPane editUserRoot;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField addressField;
    @FXML
    private DatePicker birthdayDPicker;
    @FXML
    private TextField emailAddressField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button confirmButton;

    private App app;
    private Stage dialogStage;

    @FXML
    private void initialize() {
        usernameField.setEditable(false);

        birthdayDPicker.setVisible(true);

        birthdayDPicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return DateUtil.format(date);
            }

            @Override
            public LocalDate fromString(String string) {
                return DateUtil.parse(string);
            }
        });
    }

    public void initializeAddUser() {
        usernameLabel.setVisible(false);
        usernameField.setVisible(false);

        confirmButton.setOnAction(event -> handleAddUserConfirm());
        // Enter can be pressed instead of the confirm button
        editUserRoot.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleAddUserConfirm();
            }
        });
    }

    public void initializeEditUser(User selectedUser) {
        passwordLabel.setVisible(false);
        passwordField.setVisible(false);
        usernameField.setText(selectedUser.getUsername());
        firstNameField.setText(selectedUser.getFirstName());
        lastNameField.setText(selectedUser.getLastName());
        addressField.setText(selectedUser.getAddress());
        birthdayDPicker.getEditor().setText(DateUtil.format(selectedUser.getBirthday()));
        emailAddressField.setText(selectedUser.getEmail());

        confirmButton.setOnAction(event -> handleEditUserConfirm(selectedUser));
        // Enter can be pressed instead of the confirm button
        editUserRoot.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleEditUserConfirm(selectedUser);
            }
        });
    }

    private void handleAddUserConfirm() {
        if (fieldsAreValid(true)) {
            User u = new User();
            // TODO: Get username from server
            u.setUsername("Test user");
            u.setFirstName(firstNameField.getText());
            u.setLastName(lastNameField.getText());
            u.setAddress(addressField.getText());
            u.setBirthday(DateUtil.parse(birthdayDPicker.getEditor().getText()));
            u.setEmail(emailAddressField.getText());
            u.setRegistrationDate(LocalDate.now());
            app.getUsersData().add(u);
            app.refreshTables();
            dialogStage.close();
        }
    }

    private void handleEditUserConfirm(User u) {
        if (fieldsAreValid(false)) {
            u.setFirstName(firstNameField.getText());
            u.setLastName(lastNameField.getText());
            u.setAddress(addressField.getText());
            u.setBirthday(DateUtil.parse(birthdayDPicker.getEditor().getText()));
            u.setEmail(emailAddressField.getText());
            app.refreshTables();
            dialogStage.close();
        }
    }

    private boolean fieldsAreValid(boolean add) {
        Matcher emailMatcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
                .matcher(emailAddressField.getText());
        Matcher passwordMatcher = Pattern.compile("^(?=.[0-9])(?=.[a-z])(?=.[A-Z])(?=.[@#$%^&+=])(?=\\S+$).{8,}$")
                .matcher(passwordField.getText());
        String errMessage = "";
        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errMessage += "Pas de prénom valide\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errMessage += "Pas de nom de famille valide\n";
        }
        if (addressField.getText() == null || addressField.getText().length() == 0) {
            errMessage += "Pas d'adresse valdie\n";
        }
        if (birthdayDPicker.getEditor().getText() == null || !DateUtil.validDate(birthdayDPicker.getEditor().getText())) {
            errMessage += "Pas de date de naissance validey\n";
        }
        if (emailAddressField.getText() == null || emailAddressField.getText().length() == 0 || !emailMatcher.matches()) {
            errMessage += "Pas d'adresse email validel\n";
        }
        if (add && (passwordField.getText() == null || passwordField.getText().length() == 0)) {
            errMessage += "Pas de mot de passe valide\n";
        }
        if (errMessage.length() == 0) {
            return true;
        } else {
            // There's at least one error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Champs invalides");
            alert.setHeaderText("Veuillez corriger les champs invalides");
            alert.setContentText(errMessage);

            alert.showAndWait();

            return false;
        }
    }

    @FXML
    private void handleReset() {
        firstNameField.setText("");
        lastNameField.setText("");
        addressField.setText("");
        birthdayDPicker.getEditor().setText("");
        emailAddressField.setText("");
        passwordField.setText("");
    }

    /**
     * Handles click on cancel button
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setApp(App app) {this.app = app;}

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
