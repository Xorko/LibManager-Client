package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.libmanager.client.App;


public class EditUserController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField addressField;
    @FXML
    private DatePicker birthdayDatePicker;
    @FXML
    private TextField emailAddressField;

    App app;

    public void setApp(App app) {this.app = app;}
}
