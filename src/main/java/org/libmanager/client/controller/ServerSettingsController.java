package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.libmanager.client.util.Config;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerSettingsController implements Initializable {

    @FXML
    private TextField protocolField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField portField;

    private SettingsController parentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        protocolField.setText(Config.getServerProtocol());
        addressField.setText(Config.getServerAddress());
        portField.setText(Config.getServerPort());
    }

    @FXML
    private void handleConfirm() {
        handleApply();
        parentController.getDialogStage().close();
    }

    @FXML
    private void handleApply() {
        Config.setServerProtocol(protocolField.getText());
        Config.setServerAddress(addressField.getText());
        Config.setServerPort(portField.getText());
    }


    @FXML
    private void handleCancel() {
        parentController.getDialogStage().close();
    }

    public void setParentController(SettingsController parentController) {
        this.parentController = parentController;
    }

}
