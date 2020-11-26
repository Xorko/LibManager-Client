package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.libmanager.client.App;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.Status;

public class AdminPanelController {

    // Book
    @FXML
    private TextField bookTitleField;
    @FXML
    private TextField bookAuthorField;
    @FXML
    private TextField bookPublisherField;
    @FXML
    private ComboBox<BookGenre> bookGenreCBox;
    @FXML
    private ComboBox<Status> bookStatusCBox;
    @FXML
    private DatePicker bookReleaseDateDPicker;
    @FXML
    private TextField bookIsbnField;
    /*
    TODO types
    @FXML
    private TableView bookTable;
    @FXML
    private  TableColumn bookTitleColumn;
    @FXML
    private TableColumn bookAuthorColumn;
    @FXML
    private TableColumn bookGenreColumn;
    @FXML
    private TableColumn bookPublisherColumn;
    @FXML
    private TableColumn bookReleaseDateColumn;
    @FXML
    private TableColumn bookIsbnColumn;
    @FXML
    private TableColumn bookStatusColumn;

    */

    //DVD Pane
    @FXML
    private TextField titleDvdField;
    @FXML
    private TextField dvdDirectorField;
    /*
    TODO enum
    @FXML
    private ComboBox dvdGenreCBox;
    @FXML
    private ComboBox dvdStatusCBox;
     */
    @FXML
    private DatePicker dvdReleaseDateDPicker;
    /*
    TODO types
    @FXML
    private  TableColumn dvdTitleColumn;
    @FXML
    private TableColumn dvdDirectorColumn;
    @FXML
    private TableColumn dvdGenreColumn;
    @FXML
    private TableColumn dvdDurationColumn;
    @FXML
    private TableColumn dvdReleaseDateColumn;
    @FXML
    private TableColumn dvdStatusColumn;
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