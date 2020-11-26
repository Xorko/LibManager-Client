package org.libmanager.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.libmanager.client.controller.ReservationController;
import org.libmanager.client.controller.RootController;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.client.model.User;

import java.io.IOException;
import java.time.LocalDate;

public class App extends Application {

    private Stage primaryStage;
    private BorderPane rootView;
    private User loggedInUser;
    private ObservableList<Book> booksData = FXCollections.observableArrayList();
    private ObservableList<DVD> dvdData = FXCollections.observableArrayList();
    private ObservableList<User> usersData = FXCollections.observableArrayList();

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
            TabPane reservation = loader.load();

            rootView.setCenter(reservation);

            ReservationController controller = loader.getController();
            controller.setApp(this);
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

    public ObservableList<Book> getBooksData() {
        return booksData;
    }

    public void setBooksData(ObservableList<Book> booksData) {
        this.booksData = booksData;
    }

    public ObservableList<DVD> getDvdData() {
        return dvdData;
    }

    public void setDvdData(ObservableList<DVD> dvdData) {
        this.dvdData = dvdData;
    }

    public ObservableList<User> getUsersData() {
        return usersData;
    }

    public void setUsersData(ObservableList<User> usersData) {
        this.usersData = usersData;
    }
}
