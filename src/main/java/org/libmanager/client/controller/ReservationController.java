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

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField publisherField;
    /*
    TODO Enum for ComboBox
    @FXML
    private ComboBox genreCBox;
    @FXML
    private ComboBox typeCBox;
    */
    @FXML
    private DatePicker releaseDateDPicker;
    @FXML
    private TextField isbnField;
    /*
    TODO types
    @FXML
    private TableColumn titleColumn;
    @FXML
    private TableColumn authorColumn;
    @FXML
    private TableColumn genreColumn;
    @FXML
    private TableColumn typeColumn;
    @FXML
    private TableColumn releaseDateColumn;
    @FXML
    private TableColumn publisherColumn;
    @FXML
    private TableColumn isbnColumn;
    @FXML
    private TableColumn stateColumn;
    @FXML
    private TableColumn durationColumn;
    @FXML
    private TableView itemsTable;
    */

    App app;

    public void setApp(App app) {this.app = app;}
}
