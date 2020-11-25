package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import org.libmanager.client.App;

public class AdminPanelController {

    // Book Pane
    @FXML
    private TextField titleBookField;
    @FXML
    private TextField authorBookField;
    @FXML
    private TextField publisherField;
    /*
    TODO enum
    @FXML
    private ComboBox genreBookCBox;
    @FXML
    private ComboBox stateBookCBox;
     */
    @FXML
    private DatePicker releaseDateBookDPicker;
    @FXML
    private TextField isbnField;
    /*
    TODO types
    @FXML
    private  TableColumn titleBookColumn;
    @FXML
    private TableColumn authorColumn;
    @FXML
    private TableColumn genreBookColumn;
    @FXML
    private TableColumn publisherColumn;
    @FXML
    private TableColumn releaseDateBookColumn;
    @FXML
    private TableColumn isbnColumn;
    @FXML
    private TableColumn stateBookColumn;
    @FXML
    private TableView bookTable;
    */

    //DVD Pane
    @FXML
    private TextField titleDvdField;
    @FXML
    private TextField directorField;
    /*
    TODO enum
    @FXML
    private ComboBox genreDvdCBox;
    @FXML
    private ComboBox stateDvdCBox;
     */
    @FXML
    private DatePicker releaseDateDvdDPicker;
    /*
    TODO types
    @FXML
    private  TableColumn titleDvdColumn;
    @FXML
    private TableColumn directorColumn;
    @FXML
    private TableColumn genreDvdColumn;
    @FXML
    private TableColumn durationColumn;
    @FXML
    private TableColumn releaseDateDvdColumn;
    @FXML
    private TableColumn stateDvdColumn;
    @FXML
    private TableView dvdTable;
    */

    // Users Pane
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

    // App
    App app;

    public void setApp(App app) {this.app = app;}
}
