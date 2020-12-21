package org.libmanager.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import org.libmanager.client.api.ServerAPI;
import org.libmanager.client.controller.*;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.DVDGenre;
import org.libmanager.client.enums.Genre;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.client.model.User;
import org.libmanager.client.util.DateUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

        updateData();

        initRootView();
        showReservationView();
        primaryStage.show();
    }

    /**
     * Load the root layout
     */
    public void loadRootView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootView.fxml"));
            loader.setResources(I18n.getBundle());
            rootView = loader.load();

            RootController controller = loader.getController();
            rootController = controller;
            controller.setApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the root view
     */
    public void initRootView() {
        loadRootView();
        Scene scene = new Scene(rootView);
        primaryStage.setScene(scene);
    }

    /**
     * Load the reservation panel
     */
    public void loadReservationView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ReservationView.fxml"));
            loader.setResources(I18n.getBundle());

            reservationView = loader.load();
            ReservationController controller = loader.getController();
            controller.setApp(this);
            reservationController = controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Shows the reservation panel
     */
    public void showReservationView() {
            if (reservationView == null) {
                loadReservationView();
            }
            if (rootView.getCenter() != reservationView) {
                rootView.setCenter(reservationView);
                reservationController.getBooksTable().refresh();
                reservationController.getDvdTable().refresh();
            }
    }

    /**
     * Load the admin panel
     */
    public void loadAdminPanelView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AdminPanelView.fxml"));
            loader.setResources(I18n.getBundle());
            adminPanelView = loader.load();
            AdminPanelController controller = loader.getController();
            controller.setApp(this);
            adminPanelController = controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Show the admin panel
     */
    public void showAdminPanelView() {
        if (adminPanelView == null) {
            loadAdminPanelView();
        }
        if (rootView.getCenter() != adminPanelView) {
            rootView.setCenter(adminPanelView);
            refreshTables();
        }
    }

    /**
     * Show the login menu
     */
    public void showSettingsView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/SettingsView.fxml"));
            loader.setResources(I18n.getBundle());
            BorderPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(I18n.getBundle().getString("settings.title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            SettingsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApp(this);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            dialogStage.setTitle(I18n.getBundle().getString("login.label.title"));
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

    /**
     * Show the ResetPassword dialog
     */
    public void showResetPasswordDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ResetPasswordView.fxml"));
            loader.setResources(I18n.getBundle());
            GridPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.setTitle(I18n.getBundle().getString("resetpassword.label.title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            ResetPasswordController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApp(this);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Show the EditItem dialog according to what button has been clicked
     * @param e The event
     */
    public void showEditItemView(ActionEvent e) {
        try {
            boolean noError = true;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/EditItemView.fxml"));
            loader.setResources(I18n.getBundle());
            GridPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            EditItemController controller = loader.getController();

            if (((Button) e.getSource()).getId().equals(adminPanelController.getBookAddButton().getId())) {
                controller.initializeAddBook();
                dialogStage.setTitle(I18n.getBundle().getString("edit.title.add.book"));
            }
            else if (((Button) e.getSource()).getId().equals(adminPanelController.getDVDAddButton().getId())) {
                controller.initializeAddDVD();
                dialogStage.setTitle(I18n.getBundle().getString("edit.title.add.dvd"));
            }
            else if (((Button) e.getSource()).getId().equals(adminPanelController.getBookEditButton().getId())) {
                Book selected = adminPanelController.getSelectedBook();
                if (selected != null) {
                    controller.initializeEditBook(selected);
                    dialogStage.setTitle(I18n.getBundle().getString("edit.title.editing") + selected.getTitle());
                } else {
                    noError = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(primaryStage);
                    alert.setTitle(I18n.getBundle().getString("alert.noselection.book.title"));
                    alert.setHeaderText(I18n.getBundle().getString("alert.noselection.book.title"));
                    alert.setContentText(I18n.getBundle().getString("alert.noselection.book.edit.content"));
                    alert.showAndWait();
                }
            }
            else if (((Button) e.getSource()).getId().equals(adminPanelController.getDVDEditButton().getId())) {
                DVD selected = adminPanelController.getSelectedDVD();
                if (selected != null) {
                    controller.initializeEditDVD(selected);
                    dialogStage.setTitle(I18n.getBundle().getString("edit.title.editing") + selected.getTitle());
                } else {
                    noError = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(primaryStage);
                    alert.setTitle(I18n.getBundle().getString("alert.noselection.dvd.title"));
                    alert.setHeaderText(I18n.getBundle().getString("alert.noselection.dvd.title"));
                    alert.setContentText(I18n.getBundle().getString("alert.noselection.dvd.edit.content"));
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
     * Show the EditUser dialog according to what button has been clicked
     * @param e The event
     */
    public void showEditUserView(ActionEvent e) {
        try {
            boolean noError = true;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/EditUserView.fxml"));
            loader.setResources(I18n.getBundle());
            GridPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            EditUserController controller = loader.getController();

            if (((Button) e.getSource()).getId().equals(adminPanelController.getUserAddButton().getId())) {
                controller.initializeAddUser();
                dialogStage.setTitle(I18n.getBundle().getString("edit.title.add.user"));
            }
            else if (((Button) e.getSource()).getId().equals(adminPanelController.getUserEditButton().getId())) {
                User selected = adminPanelController.getSelectedUser();
                if (selected != null) {
                    controller.initializeEditUser(selected);
                    dialogStage.setTitle(I18n.getBundle().getString("edit.title.editing") + selected.getUsername());
                } else {
                    noError = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(primaryStage);
                    alert.setTitle(I18n.getBundle().getString("alert.noselection.user.title"));
                    alert.setHeaderText(I18n.getBundle().getString("alert.noselection.user.title"));
                    alert.setContentText(I18n.getBundle().getString("alert.noselection.user.edit.content"));
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
     * Show the Overview of reserved items of the logged in user
     */
    public void showReservationOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ReservationsOverview.fxml"));
            loader.setResources(I18n.getBundle());
            GridPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(I18n.getBundle().getString("reservationoverview.label.title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            ReservationOverviewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApp(this);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the user list
     * @return  the user list
     */
    public ObservableList<User> getUsersData() {
        return usersData;
    }

    /**
     * Set the user list
     * @param usersData the user list
     */
    public void setUsersData(ObservableList<User> usersData) {
        this.usersData = usersData;
    }

    /**
     * Refresh tables content
     */
    public void refreshTables() {
        if (reservationController != null) {
            reservationController.getBooksTable().refresh();
            reservationController.getDvdTable().refresh();
        }
        if (adminPanelController != null) {
            adminPanelController.getBooksTable().refresh();
            adminPanelController.getDVDTable().refresh();
            adminPanelController.getUsersTable().refresh();
        }
    }

    /**
     * Update content lists content
     */
    public void updateData() {
        final Task<Void> updateDataFromDB = new Task<>() {
            @Override
            protected Void call() {
                getBooksFromDB();
                getDVDFromDB();
                return null;
            }
        };

        new Thread(updateDataFromDB).start();
    }

    /**
     * Load the books from a JSON server response
     * @param response  The server response (JSON)
     */
    public void loadBooksFromJSON(String response) {
        JsonNode root = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Add a module so that Jackson uses the right class for Genre
        SimpleModule module = new SimpleModule();
        module.setAbstractTypes(new SimpleAbstractTypeResolver().addMapping(Genre.class, BookGenre.class));
        mapper.registerModule(module);

        try {
            root = mapper.readTree(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (root == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(primaryStage);
            alert.setTitle(I18n.getBundle().getString("server.connection.failed.alert"));
            alert.setHeaderText(I18n.getBundle().getString("server.connection.failed"));
            alert.showAndWait();
        } else {
            if (root.get("code").asText().equals("OK")) {
                List<Book> books = null;
                CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, Book.class);
                try {
                    String collection = mapper.writeValueAsString(root.get("collection"));
                    books = mapper.readValue(collection, type);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                getBooksData().setAll(books);
            }
        }
    }

    /**
     * Load the DVDs from a JSON server response
     * @param response  The server response (JSON)
     */
    public void loadDVDFromJSON(String response) {
        JsonNode root = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Add a module so that Jackson uses the right class for Genre
        SimpleModule module = new SimpleModule();
        module.setAbstractTypes(new SimpleAbstractTypeResolver().addMapping(Genre.class, DVDGenre.class));
        mapper.registerModule(module);

        try {
            root = mapper.readTree(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (root == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(primaryStage);
            alert.setTitle(I18n.getBundle().getString("server.connection.failed.alert"));
            alert.setHeaderText(I18n.getBundle().getString("server.connection.failed"));
            alert.showAndWait();
        } else {
            if (root.get("code").asText().equals("OK")) {
                List<DVD> dvd = null;
                CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, DVD.class);
                try {
                    String collection = mapper.writeValueAsString(root.get("collection"));
                    dvd = mapper.readValue(collection, type);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                getDVDData().setAll(dvd);
            }
        }
    }

    /**
     * Gather book list from the server
     */
    public void getBooksFromDB() {
        loadBooksFromJSON(ServerAPI.callGetAllBooks());
    }

    /**
     * Gather DVD list from the server
     */
    public void getDVDFromDB() {
        loadDVDFromJSON(ServerAPI.callGetAllDVD());
    }

    /**
     * Show and hide the reservation overview menu in menu bar
     */
    public void toggleReservationOverviewMenuItem() {
        if(loggedInUser != null) {
            rootController.toggleReservationOverviewMenuItem();
        }
    }

    /**
     * Reload the 3 main views
     */
    public void reloadViews() {
        updateData();
        loadRootView();
        loadReservationView();
        loadAdminPanelView();
    }

    /**
     * Change the language
     * @param locale The new language
     */
    public void changeLanguage(Locale locale) {
        I18n.changeLanguage(locale);
        if (rootView.getCenter() == adminPanelView) {
            reloadViews();
            initRootView();
            showAdminPanelView();
        }
        else {
            reloadViews();
            initRootView();
            showReservationView();
        }
    }

    /**
     * Get the primary stage
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
     * Set the logged in user
     * @param loggedInUser  The logged in user
     */
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    /**
     * Get the logged in user
     * @return  the logged in user
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Get the books list
     * @return  the books list
     */
    public synchronized ObservableList<Book> getBooksData() {
        return booksData;
    }

    /**
     * Set the book list
     * @param booksData the book list
     */
    public synchronized void setBooksData(ObservableList<Book> booksData) {
        this.booksData = booksData;
    }

    /**
     * Get the DVD list
     * @return  the DVD list
     */
    public synchronized ObservableList<DVD> getDVDData() {
        return dvdData;
    }

    /**
     * Set the DVD list
     * @param dvdData the DVD list
     */
    public synchronized void setDVDData(ObservableList<DVD> dvdData) {
        this.dvdData = dvdData;
    }

}
