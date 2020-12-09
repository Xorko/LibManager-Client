package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.libmanager.client.App;
import org.libmanager.client.I18n;
import org.libmanager.client.model.User;
import org.libmanager.client.util.DateUtil;

import java.net.URL;
import java.time.LocalDate;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EditUserController implements Initializable {

    @FXML
    private RowConstraints passwordRow;
    @FXML
    private GridPane editUserRoot;
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
    public void initialize(URL location, ResourceBundle resources) {
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
        firstNameField.setOnKeyTyped(event -> usernameField.setText(generateUsername(firstNameField.getText(), lastNameField.getText())));
        lastNameField.setOnKeyTyped(event -> usernameField.setText(generateUsername(firstNameField.getText(), lastNameField.getText())));

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
        passwordRow.setPercentHeight(0);
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
            u.setUsername(usernameField.getText());
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

    /**
     * Generate random username from first name and last name of the user
     * @param firstname First name of the user
     * @param lastname Last name of the user
     * @return String username
     * */
    private String generateUsername(String firstname, String lastname) {
        String username = "";
        do {
            if (lastname.length() > 10) lastname = lastname.substring(0, 10);
            lastname = lastname.replaceAll("\\s+", "");
            firstname = firstname.substring(0, 1).replaceAll("\\s+", "");

            username += firstname.toLowerCase() + ".";
            username += lastname.toLowerCase();
            username += (new Random().nextInt(200));
        } while (!usernameIsUnique(username));
        return username;
    }

    /**
     * Check if a username is unique
     * @return true if the username is unique, false otherwise
     */
    private boolean usernameIsUnique(String username) {
        // TODO: Request
        return true;
    }

    private boolean fieldsAreValid(boolean add) {
        Matcher emailMatcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
                .matcher(emailAddressField.getText());
        Matcher passwordMatcher = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*_-]).{8,}$")
                .matcher(passwordField.getText());
        String errMessage = "";
        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errMessage += I18n.getBundle().getString("edit.user.alert.invalid.firstname") + "\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errMessage += I18n.getBundle().getString("edit.user.alert.invalid.lastname") + "\n";
        }
        if (addressField.getText() == null || addressField.getText().length() == 0) {
            errMessage += I18n.getBundle().getString("edit.user.alert.invalid.address") + "\n";
        }
        if (birthdayDPicker.getEditor().getText() == null || !DateUtil.validDate(birthdayDPicker.getEditor().getText())) {
            errMessage += I18n.getBundle().getString("edit.user.alert.invalid.birthday") + "\n";
        }
        if (emailAddressField.getText() == null || emailAddressField.getText().length() == 0 || !emailMatcher.matches()) {
            errMessage += I18n.getBundle().getString("edit.user.alert.invalid.email") + "\n";
        }
        if (add && (passwordField.getText() == null || !passwordMatcher.matches())) {
            errMessage += I18n.getBundle().getString("edit.user.alert.invalid.password") + "\n";
        }
        if (errMessage.length() == 0) {
            return true;
        } else {
            // There's at least one error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle(I18n.getBundle().getString("login.label.invalidfields"));
            alert.setHeaderText(I18n.getBundle().getString("edit.alert.incorrectfields.header"));
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
