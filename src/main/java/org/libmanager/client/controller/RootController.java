package org.libmanager.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.libmanager.client.App;
import org.libmanager.client.model.User;
import org.libmanager.client.util.I18n;

import java.io.IOException;
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
    private MenuItem usernameMenuItem;
    @FXML
    private MenuItem loginMenuItem;
    @FXML
    private MenuItem reservationOverviewMenuItem;
    @FXML
    private MenuItem logoutMenuItem;
    @FXML
    private Menu adminMenu;

    private App app;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Handle macOS
        if (System.getProperties().getProperty("os.name").toLowerCase().contains("mac")) {
            Platform.runLater(() -> menuBar.setUseSystemMenuBar(true));
            settingsMenuItem.acceleratorProperty().set(new KeyCodeCombination(KeyCode.P, KeyCombination.META_DOWN));
            quitMenuItem.acceleratorProperty().set(new KeyCodeCombination(KeyCode.Q, KeyCombination.META_DOWN));
        }

        // Since the user will not be logged in at program startup, these are invisible.
        // In the case where the view is reloaded, the check will be done in setApp
        usernameMenuItem.setVisible(false);
        reservationOverviewMenuItem.setVisible(false);
        logoutMenuItem.setVisible(false);
        adminMenu.setVisible(false);
    }

    /**
     * Show the admin panel
     */
    @FXML
    private void handleGotoAdminPanel() {
        app.showAdminPanelView();
    }

    /**
     * Show the reservation panel
     */
    @FXML
    private void handleGotoReservation() {
        app.showReservationView();
    }

    @FXML
    private void handleSettings() {
        showSettingsView();
    }

    /**
     * Quit the application
     */
    @FXML
    private void handleQuit() {
        System.exit(0);
    }

    /**
     * Show the reservation overview for the currently logged in user
     */
    @FXML
    private void handleReservations() {
        showReservationOverview();
    }

    /**
     * Log in a user
     */
    @FXML
    private void handleLogin() {
        showLoginDialog();
    }

    /**
     * Log out the currently logged in user
     */
    @FXML
    private void handleLogout() {
        if (app.getLoggedInUser().isAdmin()) {
            toggleAdminMenu();
            app.showReservationView();
        }
        toggleUsernameMenuItem(null);
        app.toggleReservationOverview();
        toggleLogoutMenuItem();
        toggleLoginMenuItem();
        app.setLoggedInUser(null);
    }

    /**
     * Show the password reset dialog
     */
    @FXML
    private void handleResetPassword() {
        app.showPasswordResetDialog();
    }

    /**
     * Show an alert with information on the software
     */
    @FXML
    private void handleAbout() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.initOwner(app.getPrimaryStage());
        about.setTitle("LibManager");
        about.setHeaderText("LibManager");
        about.setContentText("Version 0.1");
        about.showAndWait();
    }

    /**
     * Show the login dialog
     */
    public void showLoginDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/LoginView.fxml"));
            loader.setResources(I18n.getBundle());
            GridPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.setTitle(I18n.getBundle().getString("label.login.title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(app.getPrimaryStage());
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            LoginController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApp(app);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show the settings menu
     */
    public void showSettingsView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/SettingsView.fxml"));
            loader.setResources(I18n.getBundle());
            BorderPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(I18n.getBundle().getString("label.settings.title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(app.getPrimaryStage());
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            SettingsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApp(app);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showReservationOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ReservationOverviewView.fxml"));
            loader.setResources(I18n.getBundle());
            GridPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.setTitle(I18n.getBundle().getString("label.reservations"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(app.getPrimaryStage());
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            ReservationOverviewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApp(app);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Toggle the visibility of admin menu
     */
    public void toggleAdminMenu() {
        adminMenu.setVisible(!adminMenu.isVisible());
    }

    /**
     * Toggle the visibility of the username of the currently logged in user
     * @param user  The currently logged in user
     */
    public void toggleUsernameMenuItem(User user) {
        if (!usernameMenuItem.isVisible())
            usernameMenuItem.setText(user.getUsername());
        usernameMenuItem.setVisible(!usernameMenuItem.isVisible());
    }

    /**
     * Toggle the visibility of the reservation overview menu item
     */
    public void toggleReservationMenuItem() {
        reservationOverviewMenuItem.setVisible(!reservationOverviewMenuItem.isVisible());
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

    public void setApp(App app) {
        this.app = app;
        if (app.getLoggedInUser() != null) {
            if (app.getLoggedInUser().isAdmin()) {
                app.toggleAdminMenu();
            }
            app.toggleUsernameMenuItem();
            app.toggleReservationOverview();
            app.toggleLoginMenuItem();
            app.toggleLogoutMenuItem();
        }
    }
}
