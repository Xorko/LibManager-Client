package org.libmanager.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private Stage primaryStage;
    private BorderPane rootView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("LibManager");
        initRootView();
        showReservationView();
        primaryStage.show();
    }

    public void initRootView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/view/RootView.fxml"));
            rootView = loader.load();

            Scene scene = new Scene(rootView);
            primaryStage.setScene(scene);

            //TODO Controller
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showReservationView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/view/ReservationView.fxml"));
            GridPane reservation = loader.load();

            rootView.setCenter(reservation);

            //TODO Controller
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
