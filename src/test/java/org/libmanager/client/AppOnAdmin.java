package org.libmanager.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.libmanager.client.controller.AdminPanelController;
import org.libmanager.client.util.I18n;

import java.io.IOException;


public class AppOnAdmin extends App{
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("LibManager");
        super.setPrimaryStage(primaryStage);
        super.initRootView();
        super.setLoggedInUser(TestData.getUser());

        getBooksData().add(TestData.getBook());
        getBooksData().add(TestData.getBook1());

        getDVDData().add(TestData.getDvd());
        getDVDData().add(TestData.getDvd1());

        getUsersData().add(TestData.getUser());
        getUsersData().add(TestData.getUser1());

        getReservationsData().add(TestData.getReservation());
        getReservationsData().add(TestData.getReservation1());

        showAdminPanel();
    }

    public void showAdminPanel() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AdminPanelView.fxml"));
            loader.setResources(I18n.getBundle());
            TabPane adminPanelView = loader.load();
            AdminPanelController controller = loader.getController();
            controller.setApp(this);
            super.getRootView().setCenter(adminPanelView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
