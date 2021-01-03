package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import org.libmanager.client.App;
import org.libmanager.client.enums.Language;
import org.libmanager.client.enums.Theme;
import org.libmanager.client.util.Config;
import org.libmanager.client.util.Converter;
import org.libmanager.client.util.I18n;

import java.net.URL;
import java.util.ResourceBundle;

public class GeneralSettingsController implements Initializable {

    @FXML
    private ComboBox<Language> languageCBox;
    @FXML
    private ComboBox<Theme> themeCBox;

    private SettingsController parentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        languageCBox.getItems().setAll(Language.values());
        languageCBox.setConverter(Converter.getLanguageConverter());
        languageCBox.valueProperty().set(Language.valueOf(I18n.getLocale().toString()));

        themeCBox.getItems().setAll(Theme.values());
        themeCBox.valueProperty().set(Config.getTheme().equals("/css/lighttheme.css") ? Theme.LIGHT : Theme.DARK);
        themeCBox.setConverter(Converter.getThemeConverter());
    }

    @FXML
    private void handleConfirm() {
        handleApply();
        parentController.getDialogStage().close();
    }

    @FXML
    private void handleApply() {
        if (!languageCBox.valueProperty().get().toString().equals(I18n.getLocale().toString())) {
            parentController.getApp().changeLanguage(languageCBox.valueProperty().get().getLocale());
            parentController.loadGeneral();
        }
        handleThemeChange();
    }

    private void handleThemeChange() {
        switch (themeCBox.valueProperty().get()) {
            case LIGHT:
                Config.setTheme("/css/lighttheme.css");
                parentController.getApp().getRootView().getStylesheets().remove("/css/darktheme.css");
                parentController.getApp().getRootView().getStylesheets().add("/css/lighttheme.css");
                parentController.getRoot().getStylesheets().remove("/css/darktheme.css");
                parentController.getRoot().getStylesheets().add("/css/lighttheme.css");
                break;
            case DARK:
                Config.setTheme("/css/darktheme.css");
                parentController.getApp().getRootView().getStylesheets().remove("/css/lightheme.css");
                parentController.getApp().getRootView().getStylesheets().add("/css/darktheme.css");
                parentController.getRoot().getStylesheets().remove("/css/lighttheme.css");
                parentController.getRoot().getStylesheets().add("/css/darktheme.css");
                break;
        }
    }

    @FXML
    private void handleCancel() {
        parentController.getDialogStage().close();
    }

    public void setParentController(SettingsController parentController) {
        this.parentController = parentController;
    }
}
