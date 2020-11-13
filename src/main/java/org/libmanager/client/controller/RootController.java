package org.libmanager.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.libmanager.client.App;

public class RootController {
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem quitMenuItem;
    @FXML
    private MenuItem loginMenuItem;
    @FXML
    private MenuItem logoutMenuItem;
    @FXML
    private Menu adminMenu;
    @FXML
    private MenuItem adminPanelMenuItem;
    @FXML
    private MenuItem reservationMenuItem;
    @FXML
    private MenuItem aboutMenuItem;

    private App app;

    @FXML
    private void initialize() {
        // Handle macOS
        if (System.getProperties().getProperty("os.name").toLowerCase().contains("mac")) {
            Platform.runLater(() -> menuBar.setUseSystemMenuBar(true));
            quitMenuItem.acceleratorProperty().set(new KeyCodeCombination(KeyCode.Q, KeyCombination.META_DOWN));
        }
    }

    @FXML
    private void handleQuit() {
        System.exit(0);
    }

    @FXML
    private void handleLogin() {
        app.showLoginDialog();
    }

    @FXML
    private void handleLogout() {
        //TODO If logged in user is admin, hide admin menu and go to reservation view
        app.setLoggedInUser(null);
    }

    @FXML
    private void handleAbout() {
        Alert about = new Alert(Alert.AlertType.CONFIRMATION);
        about.initOwner(app.getPrimaryStage());
        about.setHeaderText("LibManager");
        about.setContentText("Version 0.1\nhttps://github.com/Xorko/LibManager-Client");
        about.showAndWait();
    }

    public void setApp(App app) {
        this.app = app;
    }
}
