package org.libmanager.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.libmanager.client.controller.ReservationController;
import org.libmanager.client.util.I18n;

import java.io.IOException;

public class AppOnReservation extends App{
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

        showReservationView();
    }

    public void reservationView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ReservationView.fxml"));
            loader.setResources(I18n.getBundle());

            GridPane reservationView = loader.load();
            ReservationController controller = loader.getController();
            controller.setApp(this);
            super.getRootView().setCenter(reservationView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
