package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.libmanager.client.App;
import org.libmanager.client.I18n;
import org.libmanager.client.enums.Language;
import org.libmanager.client.util.Converter;

import java.net.URL;
import java.util.ResourceBundle;

public class GeneralSettingsController implements Initializable {

    @FXML
    private ComboBox<Language> languageCBox;
    @FXML
    private Button confirmBtn;
    @FXML
    private Button applyBtn;
    @FXML
    private Button cancelBtn;

    private SettingsController parentController;
    private App app;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        languageCBox.getItems().setAll(Language.values());
        languageCBox.setConverter(Converter.getLanguageConverter());
        languageCBox.valueProperty().set(Language.valueOf(I18n.getLocale().toString()));
    }

    @FXML
    private void handleConfirm() {
        handleApply();
        parentController.getDialogStage().close();
    }

    @FXML
    private void handleApply() {
        if (!languageCBox.valueProperty().get().toString().equals(I18n.getLocale().toString())) {
            app.changeLanguage(languageCBox.valueProperty().get().getLocale());
            parentController.loadGeneral();
        }
    }

    @FXML
    private void handleCancel() {
        parentController.getDialogStage().close();
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void setParentController(SettingsController parentController) {
        this.parentController = parentController;
    }
}
