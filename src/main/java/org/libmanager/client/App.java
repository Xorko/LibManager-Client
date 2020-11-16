package org.libmanager.client;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.libmanager.client.controller.RootController;
import org.libmanager.client.model.DVD;
import org.libmanager.client.model.Item;
import org.libmanager.client.model.User;

import java.io.IOException;
import java.time.LocalDate;

public class App extends Application {

    private Stage primaryStage;
    private BorderPane rootView;
    private User loggedInUser;
    private ObservableList<Item> itemList;

    public static void main(String[] args) {
        DVD d = new DVD("Interstellar", "Je sais plus", "Science-Fiction", LocalDate.of(2014, 1, 1), false, "2h15");
        System.out.println(d.getAuthor());
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

            RootController controller = loader.getController();
            controller.setApp(this);
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

    public void showAdminPanelView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/view/AdminPanelView.fxml"));
            TabPane reservation = loader.load();

            rootView.setCenter(reservation);

            //TODO Controller
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoginDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/view/LoginView.fxml"));
            GridPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Login");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
