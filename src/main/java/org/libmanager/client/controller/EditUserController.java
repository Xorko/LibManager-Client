package org.libmanager.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
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
import org.libmanager.client.api.ServerAPI;
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
        // Generate username when first name and last name fields aren't empty and are unfocused
        firstNameField.focusedProperty().addListener((event -> {
            if (!firstNameField.isFocused() && firstNameField.getText().length() != 0 && lastNameField.getText().length() != 0) {
                //usernameField.setText(generateUsername(firstNameField.getText(), lastNameField.getText()));
                //generateUsername(firstNameField.getText(), lastNameField.getText());
            }
        }));

        lastNameField.focusedProperty().addListener((event -> {
            if (!lastNameField.isFocused() && firstNameField.getText().length() != 0 && lastNameField.getText().length() != 0) {
                //usernameField.setText(generateUsername(firstNameField.getText(), lastNameField.getText()));
                generateUsername(firstNameField.getText(), lastNameField.getText());
            }
        }));

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
     * */
    private void generateUsername(String firstname, String lastname) {
        final String[] firstName = {firstname};
        final String[] lastName = {lastname};

        final Task<String> generateUsernameTask = new Task<>() {
            @Override
            protected String call() {
                String username = "";
                do {
                    if (lastName[0].length() > 10) lastName[0] = lastName[0].substring(0, 10);
                    lastName[0] = lastName[0].replaceAll("\\s+", "");
                    firstName[0] = firstName[0].substring(0, 1).replaceAll("\\s+", "");

                    username += firstName[0].toLowerCase() + ".";
                    username += lastName[0].toLowerCase();
                    username += (new Random().nextInt(200));
                } while (!usernameIsUnique(username));
                return username;
            }
        };

        generateUsernameTask.setOnSucceeded(event -> usernameField.setText(generateUsernameTask.getValue()));

        new Thread(generateUsernameTask).start();
    }

    /**
     * Check if a username is unique
     * @return true if the username is unique, false otherwise
     */
    private boolean usernameIsUnique(String username) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(ServerAPI.callCheckUsername(username));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (root != null) {
            if (root.get("code").asText().equals("OK")) {
                return !root.get("content").asBoolean();
            }
        }
        return false;
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
