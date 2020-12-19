package org.libmanager.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.libmanager.client.App;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem settingsMenuItem;
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
    @FXML
    private MenuItem reservationOverviewMenuItem;

    private App app;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Handle macOS
        if (System.getProperties().getProperty("os.name").toLowerCase().contains("mac")) {
            Platform.runLater(() -> menuBar.setUseSystemMenuBar(true));
            settingsMenuItem.acceleratorProperty().set(new KeyCodeCombination(KeyCode.P, KeyCombination.META_DOWN));
            quitMenuItem.acceleratorProperty().set(new KeyCodeCombination(KeyCode.Q, KeyCombination.META_DOWN));
        }

        // Since the user will not be logged in at program startup, logout is disabled.
        // In the case where the view is reloaded, the check will be done in setApp
        logoutMenuItem.setVisible(false);
        adminMenu.setVisible(false);
        reservationOverviewMenuItem.setVisible(false);
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
    private void handlePreferences() {
        app.showSettingsView();
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
        if (app.getLoggedInUser().isAdmin()) {
            toggleAdminMenu();
            app.showReservationView();
        }
        toggleLogoutMenuItem();
        toggleLoginMenuItem();
        toggleReservationOverviewMenuItem();
        app.setLoggedInUser(null);
    }

    @FXML
    private void handleResetPassword() {
        app.showResetPasswordDialog();
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

    /**
     * toggle the visibility of reservation overview in the account menu
     */
    public void toggleReservationOverviewMenuItem() {reservationOverviewMenuItem.setVisible(!reservationOverviewMenuItem.isVisible());}

    public void handleReservationOverview() {
        app.showReservationOverview();
    }

    @FXML
    private void handleAbout() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.initOwner(app.getPrimaryStage());
        about.setTitle("LibManager");
        about.setHeaderText("LibManager");
        about.setContentText("Version 0.1\nhttps://github.com/Xorko/LibManager-Client");
        about.showAndWait();
    }

    public void setApp(App app) {
        this.app = app;
        if (app.getLoggedInUser() != null) {
            if (app.getLoggedInUser().isAdmin()) {
                app.toggleAdminMenu();
            }
            app.toggleLoginMenu();
            app.toggleLogoutMenuItem();
        }
    }
}
