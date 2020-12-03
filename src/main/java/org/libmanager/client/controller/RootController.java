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

        // Since the user will not be logged in at program startup, logout is disabled
        logoutMenuItem.setVisible(false);
    }

    @FXML
    private void handleGotoAdminPanel() {
        app.showAdminPanelView();
    }

    @FXML
    private void handleGotoReservation() {
        app.showReservationView();
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
        if (app.getLoggedInUser().isAdmin()) {
            toggleAdminMenu();
        }
        toggleLogoutMenuItem();
        toggleLoginMenuItem();
        app.setLoggedInUser(null);
    }

    /**
     * Toggle the visibility of admin menu
     */
    public void toggleAdminMenu() {
        adminMenu.setVisible(!adminMenu.isVisible());
    }

    /**
     * Toggle the visibility of login in the account menu
     */
    public void toggleLoginMenuItem() {
        loginMenuItem.setVisible(!loginMenuItem.isVisible());
    }

    /**
     * Toggle the visibility of logout in the account menu
     */
    public void toggleLogoutMenuItem() {
        logoutMenuItem.setVisible(!logoutMenuItem.isVisible());
    }

    @FXML
    private void handleAbout() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.initOwner(app.getPrimaryStage());
        about.setHeaderText("LibManager");
        about.setContentText("Version 0.1\nhttps://github.com/Xorko/LibManager-Client");
        about.showAndWait();
    }

    public void setApp(App app) {
        this.app = app;
    }
}
