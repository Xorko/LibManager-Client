package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.libmanager.client.App;


public class EditItemController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField typeField;
    @FXML
    private DatePicker releaseDateDPicker;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField durationField;

    App app;

    public void setApp(App app) {this.app = app;}
}
