package org.libmanager.client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.libmanager.client.App;
import org.libmanager.client.util.Config;
import org.libmanager.client.util.I18n;
import org.libmanager.client.enums.AdminSettings;
import org.libmanager.client.enums.GlobalSettings;
import org.libmanager.client.enums.Settings;
import org.libmanager.client.util.Converter;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private ListView<Settings> settingsListView;

    private GridPane generalSettings;
    private GridPane serverSettings;

    ObservableList<Settings> settings;

    private App app;
    private Stage dialogStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        settings = FXCollections.observableArrayList(GlobalSettings.values());
        settingsListView.setItems(settings);
        settingsListView.setCellFactory(lv -> {
            TextFieldListCell<Settings> cell = new TextFieldListCell<>();
            cell.setConverter(Converter.getSettingConverter());
            return cell;
        });
        settingsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        settingsListView.getSelectionModel().getSelectedItems();
        settingsListView.getSelectionModel().select(0);
        settingsListView.getSelectionModel().selectedItemProperty().addListener(event -> {
            String selected = settingsListView.getSelectionModel().selectedItemProperty().get().getKey();
            if (selected.equals(GlobalSettings.GENERAL.getKey())) {
                showGeneral();
            }
            else if (selected.equals(AdminSettings.SERVER.getKey())) {
                showServer();
            }
            else
                root.setCenter(null);
        });
    }

    public void loadGeneral() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/GeneralSettingsView.fxml"));
            loader.setResources(I18n.getBundle());
            generalSettings = loader.load();
            root.getStylesheets().add(Config.getTheme());
            GeneralSettingsController controller = loader.getController();
            controller.setParentController(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadServer() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ServerSettingsView.fxml"));
            loader.setResources(I18n.getBundle());
            serverSettings = loader.load();
            root.getStylesheets().add(Config.getTheme());
            ServerSettingsController controller = loader.getController();
            controller.setParentController(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showGeneral() {
        if (generalSettings == null)
            loadGeneral();
        root.setCenter(generalSettings);
    }

    public void showServer() {
        if (serverSettings == null)
            loadServer();
        root.setCenter(serverSettings);
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
        if (app.getLoggedInUser() != null && app.getLoggedInUser().isAdmin()) {
            settings.addAll(Arrays.asList(AdminSettings.values()));
        }
        showGeneral();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public BorderPane getRoot() {
        return root;
    }
}
