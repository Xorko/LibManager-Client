package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.libmanager.client.App;


public class EditItemController {

    //TODO hide fields depending on item add

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField genreField;
    /*
    TODO Type
    @FXML
    private ComboBox typeCBox;
    */
    @FXML
    private DatePicker releaseDateDPicker;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField durationField;

    App app;

    public void setApp(App app) {this.app = app;}
}
