package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import org.libmanager.client.App;

public class AdminPanelController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField publisherField;
    /*
    TODO enum
    @FXML
    private ComboBox genreCBox;
    @FXML
    private ComboBox typeCBox;
    @FXML
    private ComboBox stateCBox;
     */
    @FXML
    private DatePicker releaseDateDPicker;
    @FXML
    private TextField isbnField;
    /*
    TODO types
    @FXML
    private  TableColumn titleColumn;
    @FXML
    private TableColumn authorColumn;
    @FXML
    private TableColumn genreColumn;
    @FXML
    private TableColumn typeColumn;
    @FXML
    private TableColumn releaseDateColumn;
    @FXML
    private TableColumn isbnColumn;
    @FXML
    private TableColumn stateColumn;
    @FXML
    private TableColumn durationColumn;
    @FXML
    private TableView itemsTable;
    */
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
    /*
    TODO types
    @FXML
    private TableColumn usernameColumn;
    @FXML
    private TableColumn firstNameColumn;
    @FXML
    private TableColumn lastNameColumn;
    @FXML
    private TableColumn addressColumn;
    @FXML
    private TableColumn birthdayColumn;
    @FXML
    private TableColumn emailAddressColumn;
    @FXML
    private TableView usersTable;
    */

    App app;

    public void setApp(App app) {this.app = app;}
}
