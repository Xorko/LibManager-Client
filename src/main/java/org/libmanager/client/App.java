package org.libmanager.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.libmanager.client.controller.*;
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
    private ReservationController reservationController;
    private AdminPanelController adminPanelController;
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

    /**
     * Initializes the root view
     */
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

    /**
     * Shows the reservation panel
     */
    public void showReservationView() {
        try {
            if (reservationView == null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(App.class.getResource("/view/ReservationView.fxml"));
                reservationView = loader.load();
                ReservationController controller = loader.getController();
                controller.setApp(this);
                reservationController = controller;
            }
            if (rootView.getCenter() != reservationView) {
                rootView.setCenter(reservationView);
                reservationController.getBooksTable().refresh();
                reservationController.getDvdTable().refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the admin panel
     */
    public void showAdminPanelView() {
        try {
            if (adminPanelView == null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(App.class.getResource("/view/AdminPanelView.fxml"));
                adminPanelView = loader.load();
                AdminPanelController controller = loader.getController();
                controller.setApp(this);
                adminPanelController = controller;
            }
            if (rootView.getCenter() != adminPanelView) {
                rootView.setCenter(adminPanelView);
                refreshTables();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the login
     */
    public void showLoginDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/view/LoginView.fxml"));
            GridPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Connexion");
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

    public void showEditItemView(ActionEvent e) {
        try {
            boolean noError = true;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/view/EditItemView.fxml"));
            GridPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            EditItemController controller = loader.getController();

            if (((Button) e.getSource()).getId().equals(adminPanelController.getBookAddButton().getId())) {
                controller.initializeAddBook();
                dialogStage.setTitle("Ajouter un livre");
            }
            else if (((Button) e.getSource()).getId().equals(adminPanelController.getDVDAddButton().getId())) {
                controller.initializeAddDvd();
                dialogStage.setTitle("Ajouter un DVD");
            }
            else if (((Button) e.getSource()).getId().equals(adminPanelController.getBookEditButton().getId())) {
                Book selected = adminPanelController.getSelectedBook();
                if (selected != null) {
                    controller.initializeEditBook(selected);
                    dialogStage.setTitle("Edition de " + selected.getTitle());
                } else {
                    noError = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(primaryStage);
                    alert.setTitle("Aucun livre sélectionné");
                    alert.setHeaderText("Aucun livre sélectionné");
                    alert.setContentText("Vous devez sélectionner un livre pour pouvoir l'éditer");
                    alert.showAndWait();
                }
            }
            else if (((Button) e.getSource()).getId().equals(adminPanelController.getDVDEditButton().getId())) {
                DVD selected = adminPanelController.getSelectedDVD();
                if (selected != null) {
                    controller.initializeEditDvd(selected);
                    dialogStage.setTitle("Edition de " + selected.getTitle());
                } else {
                    noError = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(primaryStage);
                    alert.setTitle("Aucun DVD sélectionné");
                    alert.setHeaderText("Aucun DVD sélectionné");
                    alert.setContentText("Vous devez sélectionner un DVD pour pouvoir l'éditer");
                    alert.showAndWait();
                }
            }

            if (noError) {
                controller.setDialogStage(dialogStage);
                controller.setApp(this);

                dialogStage.showAndWait();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void showEditUserView(ActionEvent e) {
        try {
            boolean noError = true;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/view/EditUserView.fxml"));
            GridPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            EditUserController controller = loader.getController();

            if (((Button) e.getSource()).getId().equals(adminPanelController.getUserAddButton().getId())) {
                controller.initializeAddUser();
                dialogStage.setTitle("Ajouter un utilisateur");
            }
            else if (((Button) e.getSource()).getId().equals(adminPanelController.getUserEditButton().getId())) {
                User selected = adminPanelController.getSelectedUser();
                if (selected != null) {
                    controller.initializeEditUser(selected);
                    dialogStage.setTitle("Edition de " + selected.getUsername());
                } else {
                    noError = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(primaryStage);
                    alert.setTitle("Aucun utilisateur sélectionné");
                    alert.setHeaderText("Aucun utilisateur sélectionné");
                    alert.setContentText("Vous devez sélectionner un utilisateur pour pouvoir l'éditer");
                    alert.showAndWait();
                }
            }

            if (noError) {
                controller.setDialogStage(dialogStage);
                controller.setApp(this);

                dialogStage.showAndWait();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    /**
     * Gets the primary stage
     * @return  the primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Show or hide the admin menu in the menu bar
     */
    public void toggleAdminMenu() {
        if (loggedInUser != null && loggedInUser.isAdmin())
            rootController.toggleAdminMenu();
    }

    /**
     * Show or hide login in the account menu
     */
    public void toggleLoginMenu() {
        if (loggedInUser != null) {
            rootController.toggleLoginMenuItem();
        }
    }

    /**
     * Show or hide logout in the account menu
     */
    public void toggleLogoutMenuItem() {
        if (loggedInUser != null) {
            rootController.toggleLogoutMenuItem();
        }
    }

    /**
     * Sets the logged in user
     * @param loggedInUser  The logged in user
     */
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    /**
     * Gets the logged in user
     * @return  the logged in user
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public BorderPane getRootView() {
        return rootView;
    }

    /**
     * Gets the books list
     * @return  the books list
     */
    public ObservableList<Book> getBooksData() {
        return booksData;
    }

    /**
     * Sets the book list
     * @param booksData the book list
     */
    public void setBooksData(ObservableList<Book> booksData) {
        this.booksData = booksData;
    }

    /**
     * Gets the DVD list
     * @return  the DVD list
     */
    public ObservableList<DVD> getDVDData() {
        return dvdData;
    }

    /**
     * Sets the DVD list
     * @param dvdData the DVD list
     */
    public void setDVDData(ObservableList<DVD> dvdData) {
        this.dvdData = dvdData;
    }

    /**
     * Gets the user list
     * @return  the user list
     */
    public ObservableList<User> getUsersData() {
        return usersData;
    }

    /**
     * Sets the user list
     * @param usersData the user list
     */
    public void setUsersData(ObservableList<User> usersData) {
        this.usersData = usersData;
    }

    /**
     * Refresh tables content
     */
    public void refreshTables() {
        reservationController.getBooksTable().refresh();
        reservationController.getDvdTable().refresh();
        adminPanelController.getBooksTable().refresh();
        adminPanelController.getDVDTable().refresh();
        adminPanelController.getUsersTable().refresh();
    }

    /**
     * Updates lists content
     */
    public void updateData() {
        //TODO
    }
}
