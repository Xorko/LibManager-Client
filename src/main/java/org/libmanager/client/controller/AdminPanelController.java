package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.libmanager.client.App;
import org.libmanager.client.util.I18n;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {

    @FXML
    private TabPane tabPane;

    private Tab itemsTab;
    private Tab usersTab;
    private Tab reservationsTab;

    private AdminPanelItemsController itemsController;
    private AdminPanelUsersController usersController;
    private AdminPanelReservationsController reservationsController;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        loadItemsTab();
        loadUsersTab();
        loadReservationsTab();
    }

    /**
     * Load the items tab
     */
    public void loadItemsTab() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AdminPanelItemsView.fxml"));
            loader.setResources(I18n.getBundle());

            itemsTab = loader.load();
            itemsController = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load the users tab
     */
    public void loadUsersTab() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AdminPanelUsersView.fxml"));
            loader.setResources(I18n.getBundle());

            usersTab = loader.load();
            usersController = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load the reservation tab
     */
    public void loadReservationsTab() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AdminPanelReservationsView.fxml"));
            loader.setResources(I18n.getBundle());

            reservationsTab = loader.load();
            reservationsController = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setApp(App app) {
        itemsController.setApp(app);
        usersController.setApp(app);
        reservationsController.setApp(app);
        tabPane.getTabs().addAll(itemsTab, usersTab, reservationsTab);
    }

    /**
     * Get the items tab controller
     * @return The items tab controller
     */
    public AdminPanelItemsController getItemsController() {
        return itemsController;
    }

    /**
     * Get the users tab controller
     * @return The users tab controller
     */
    public AdminPanelUsersController getUsersController() {
        return usersController;
    }

    /**
     * Get the reservations tab controller
     * @return  The reservations tab controller
     */
    public AdminPanelReservationsController getReservationsController() {
        return reservationsController;
    }
}