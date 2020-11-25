package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import org.libmanager.client.App;
import org.libmanager.client.model.Item;

public class ReservationController {

    // Book
    @FXML
    private TextField bookTitleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField publisherField;
    /*
    TODO Enum for ComboBox
    @FXML
    private ComboBox bookGenreCBox;
    */
    @FXML
    private DatePicker bookReleaseDateDPicker;
    @FXML
    private TextField isbnField;
    /*
    TODO types
    @FXML
    private TableColumn bookTitleColumn;
    @FXML
    private TableColumn authorColumn;
    @FXML
    private TableColumn bookGenreColumn;
    @FXML
    private TableColumn bookReleaseDateColumn;
    @FXML
    private TableColumn publisherColumn;
    @FXML
    private TableColumn isbnColumn;
    @FXML
    private TableColumn bookStateColumn;
    @FXML
    private TableView bookTable;
    */

    // DVD
    @FXML
    private TextField dvdTitleField;
    @FXML
    private TextField directorField;
    /*
    TODO Enum for ComboBox
    @FXML
    private ComboBox dvdGenreCBox;
    */
    @FXML
    private DatePicker dvdReleaseDateDPicker;
    /*
    TODO types
    @FXML
    private TableColumn dvdTitleColumn;
    @FXML
    private TableColumn directorColumn;
    @FXML
    private TableColumn dvdGenreColumn;
    @FXML
    private TableColumn dvdReleaseDateColumn;
    @FXML
    private TableColumn durationColumn;
    @FXML
    private TableColumn bookStateColumn;
    @FXML
    private TableView dvdTable;
    */

    // App
    App app;

    public void setApp(App app) {this.app = app;}
}
