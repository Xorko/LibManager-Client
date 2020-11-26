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
import org.libmanager.client.controller.LoginController;
import org.libmanager.client.controller.ReservationController;
import org.libmanager.client.controller.RootController;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.client.model.User;

import java.io.IOException;

public class App extends Application {

    private Stage primaryStage;
    private User loggedInUser;
    private BorderPane rootView;
    private TabPane reservationView;
    private TabPane adminPanelView;
    private RootController rootController;
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
            rootController = controller;
            controller.setApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showReservationView() {
        try {
            if (reservationView == null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(App.class.getResource("/view/ReservationView.fxml"));
                reservationView = loader.load();
                ReservationController controller = loader.getController();
                controller.setApp(this);
            }
            if (rootView.getCenter() != reservationView)
                rootView.setCenter(reservationView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAdminPanelView() {
        try {
            if (adminPanelView == null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(App.class.getResource("/view/AdminPanelView.fxml"));
                adminPanelView = loader.load();
                //TODO Controller
            }
            if (rootView.getCenter() != adminPanelView)
                rootView.setCenter(adminPanelView);
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

            LoginController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApp(this);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void toggleAdminMenu() {
        if (loggedInUser != null && loggedInUser.isAdmin())
            rootController.toggleAdminMenu();
    }

    public void toggleLogoutMenuItem() {
        if (loggedInUser != null) {
            rootController.toggleLogoutMenuItem();
        }
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
