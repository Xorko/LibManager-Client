package org.libmanager.client.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.libmanager.client.App;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.DVDGenre;
import org.libmanager.client.enums.Status;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.client.model.User;
import org.libmanager.client.util.DateUtil;

import java.time.LocalDate;
import java.util.Optional;

public class AdminPanelController {

    // Book
    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TextField bookTitleField;
    @FXML
    private TextField bookAuthorField;
    @FXML
    private TextField bookPublisherField;
    @FXML
    private ComboBox<BookGenre> bookGenreCBox;
    @FXML
    private ComboBox<Status> bookStatusCBox;
    @FXML
    private DatePicker bookReleaseDateDPicker;
    @FXML
    private TextField bookIsbnField;
    @FXML
    private TableColumn<Book, String> bookTitleColumn;
    @FXML
    private TableColumn<Book, String> bookAuthorColumn;
    @FXML
    private TableColumn<Book, String> bookGenreColumn;
    @FXML
    private TableColumn<Book, String> bookPublisherColumn;
    @FXML
    private TableColumn<Book, String> bookReleaseDateColumn;
    @FXML
    private TableColumn<Book, String> bookIsbnColumn;
    @FXML
    private TableColumn<Book, Button> bookStatusColumn;

    //DVD
    @FXML
    private TextField dvdTitleField;
    @FXML
    private TextField dvdDirectorField;
    @FXML
    private ComboBox<DVDGenre> dvdGenreCBox;
    @FXML
    private ComboBox<Status> dvdStatusCBox;
    @FXML
    private DatePicker dvdReleaseDateDPicker;
    @FXML
    private TableView<DVD> dvdTable;
    @FXML
    private  TableColumn<DVD, String> dvdTitleColumn;
    @FXML
    private TableColumn<DVD, String> dvdDirectorColumn;
    @FXML
    private TableColumn<DVD, String> dvdGenreColumn;
    @FXML
    private TableColumn<DVD, String> dvdDurationColumn;
    @FXML
    private TableColumn<DVD, String> dvdReleaseDateColumn;
    @FXML
    private TableColumn<DVD, Button> dvdStatusColumn;

    // Users
    @FXML
    private TextField usernameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField addressField;
    @FXML
    private DatePicker birthdayDPicker;
    @FXML
    private DatePicker registrationDateDPicker;
    @FXML
    private TextField emailAddressField;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> addressColumn;
    @FXML
    private TableColumn<User, String> birthdayColumn;
    @FXML
    private TableColumn<User, String> emailAddressColumn;
    @FXML
    private TableColumn<User, String> registrationDateColumn;
    @FXML
    private Button bookAddButton;
    @FXML
    private Button bookEditButton;
    @FXML
    private Button bookDeleteButton;
    @FXML
    private Button dvdAddButton;
    @FXML
    private Button dvdEditButton;
    @FXML
    private Button dvdDeleteButton;
    @FXML
    private Button userAddButton;
    @FXML
    private Button userEditButton;
    @FXML
    private Button userDeleteButton;

    // App
    private App app;

    @FXML
    private void initialize() {
        bookGenreCBox.getItems().setAll(BookGenre.values());
        dvdGenreCBox.getItems().setAll(DVDGenre.values());
        bookStatusCBox.getItems().setAll(Status.values());
        dvdStatusCBox.getItems().setAll(Status.values());

        // --- BOOKS TABLE ---
        bookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        bookAuthorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        bookGenreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().genreProperty().get().toString()));
        bookReleaseDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getReleaseDate())));
        bookPublisherColumn.setCellValueFactory(cellData -> cellData.getValue().publisherProperty());
        bookIsbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        bookStatusColumn.setCellValueFactory(cellData -> {
            Button btn = new Button();

            btn.setMaxWidth(100);

            if (!cellData.getValue().getStatus()) {
                btn.setText("Rendre");
            } else {
                btn.setText("Disponible");
                btn.setDisable(true);
            }

            btn.setOnAction(event -> {
                if (app.getLoggedInUser() != null && app.getLoggedInUser().isAdmin()) {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.initOwner(app.getPrimaryStage());
                    confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    confirmationAlert.setHeaderText("Voulez vous vraiment rendre disponible ce livre ?");
                    Optional<ButtonType> answer = confirmationAlert.showAndWait();
                    if (answer.isPresent() && answer.get() == ButtonType.YES) {
                        //TODO: Send a request to the server
                        btn.setDisable(true);
                        btn.setText("Disponible");
                        cellData.getValue().setStatus(Status.AVAILABLE.isAvailable());
                        System.out.println("Book back in stock");
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(app.getPrimaryStage());
                    alert.setHeaderText("Vous n'êtes pas connecté en tant qu'administrateur");
                    alert.setContentText("Veuillez vous connecter avant d'emprunter un livre.");
                    alert.showAndWait();
                }
            });

            return new SimpleObjectProperty<>(btn);
        });

        // Books table columns dimensions
        bookTitleColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.25));
        bookAuthorColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.16));
        bookGenreColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.12));
        bookReleaseDateColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.1));
        bookPublisherColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.13));
        bookIsbnColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.12));
        bookStatusColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.115));

        // --- DVD TABLE ---
        dvdTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        dvdDirectorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        dvdGenreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().genreProperty().get().toString()));
        dvdDurationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty());
        dvdReleaseDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getReleaseDate())));
        dvdStatusColumn.setCellValueFactory(cellData -> {
            Button btn = new Button();

            btn.setMaxWidth(100);

            if (!cellData.getValue().getStatus()) {
                btn.setText("Rendre");
            } else {
                btn.setText("Disponible");
                btn.setDisable(true);
            }

            btn.setOnAction(event -> {
                if (app.getLoggedInUser() != null && app.getLoggedInUser().isAdmin()) {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.initOwner(app.getPrimaryStage());
                    confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    confirmationAlert.setHeaderText("Voulez vous vraiment rendre disponible ce DVD ?");
                    Optional<ButtonType> answer = confirmationAlert.showAndWait();
                    if (answer.isPresent() && answer.get() == ButtonType.YES) {
                        //TODO: Send a request to the server
                        btn.setDisable(true);
                        btn.setText("Disponible");
                        cellData.getValue().setStatus(Status.AVAILABLE.isAvailable());
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(app.getPrimaryStage());
                    alert.setHeaderText("Vous n'êtes pas connecté en tant qu'administrateur");
                    alert.setContentText("Veuillez vous connecter avant d'emprunter un DVD.");
                    alert.showAndWait();
                }
            });

            return new SimpleObjectProperty<>(btn);
        });

        // DVD table columns dimensions
        dvdTitleColumn.prefWidthProperty().bind(dvdTable.widthProperty().multiply(0.295));
        dvdDirectorColumn.prefWidthProperty().bind(dvdTable.widthProperty().multiply(0.25));
        dvdGenreColumn.prefWidthProperty().bind(dvdTable.widthProperty().multiply(0.15));
        dvdDurationColumn.prefWidthProperty().bind(dvdTable.widthProperty().multiply(0.09));
        dvdReleaseDateColumn.prefWidthProperty().bind(dvdTable.widthProperty().multiply(0.1));
        dvdStatusColumn.prefWidthProperty().bind(dvdTable.widthProperty().multiply(0.11));

        // --- USERS TABLE ---
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        birthdayColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getBirthday())));
        emailAddressColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        registrationDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getRegistrationDate())));

        // --- Users table columns dimensions ---
        usernameColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.1));
        firstNameColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        lastNameColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        addressColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        emailAddressColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        birthdayColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        registrationDateColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));

        // --- DATE PICKERS ---
        bookReleaseDateDPicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return DateUtil.format(date);
            }

            @Override
            public LocalDate fromString(String string) {
                return DateUtil.parse(string);
            }
        });

        dvdReleaseDateDPicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return DateUtil.format(date);
            }

            @Override
            public LocalDate fromString(String string) {
                return DateUtil.parse(string);
            }
        });

        birthdayDPicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return DateUtil.format(date);
            }

            @Override
            public LocalDate fromString(String string) {
                return DateUtil.parse(string);
            }
        });

        registrationDateDPicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return DateUtil.format(date);
            }

            @Override
            public LocalDate fromString(String string) {
                return DateUtil.parse(string);
            }
        });
    }

    @FXML
    private void handleItemManagementButtons(ActionEvent e) {
        app.showEditItemView(e);
    }

    @FXML
    private void handleUserManagementButtons(ActionEvent e) {
        app.showEditUserView(e);
    }

    @FXML
    private void handleDeleteBook() {
        Book selected = getSelectedBook();
        if (selected != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            confirmationAlert.initOwner(app.getPrimaryStage());
            confirmationAlert.setTitle("Voulez vous vraiment supprimer ce livre ?");
            confirmationAlert.setHeaderText("Voulez vous vraiment supprimer ce livre ?");
            confirmationAlert.setContentText("Cette action est irreversible");
            Optional<ButtonType> answer = confirmationAlert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                app.getBooksData().remove(selected);
            }
            app.refreshTables();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Aucun livre sélectionné");
            alert.setHeaderText("Aucun livre sélectionné");
            alert.setContentText("Veuillez sélectionner un livre afin de le supprimer");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteDVD() {
        DVD selected = getSelectedDVD();
        if (selected != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            confirmationAlert.initOwner(app.getPrimaryStage());
            confirmationAlert.setTitle("Voulez vous vraiment supprimer ce DVD ?");
            confirmationAlert.setHeaderText("Voulez vous vraiment supprimer ce DVD ?");
            confirmationAlert.setContentText("Cette action est irreversible");
            Optional<ButtonType> answer = confirmationAlert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                app.getDVDData().remove(selected);
            }
            app.refreshTables();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Aucun DVD sélectionné");
            alert.setHeaderText("Aucun DVD sélectionné");
            alert.setContentText("Veuillez sélectionner un DVD afin de le supprimer");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selected = getSelectedUser();
        if (selected != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            confirmationAlert.initOwner(app.getPrimaryStage());
            confirmationAlert.setTitle("Voulez vous vraiment supprimer cet utilisateur ?");
            confirmationAlert.setHeaderText("Voulez vous vraiment supprimer cet utilisateur ?");
            confirmationAlert.setContentText("Cette action est irreversible");
            Optional<ButtonType> answer = confirmationAlert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                app.getUsersData().remove(selected);
            }
            app.refreshTables();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Aucun utilisateur sélectionné");
            alert.setHeaderText("Aucun utilisateur sélectionné");
            alert.setContentText("Veuillez sélectionner un utilisateur afin de le supprimer");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleResetBook() {
        bookTitleField.setText("");
        bookAuthorField.setText("");
        bookPublisherField.setText("");
        bookGenreCBox.valueProperty().set(null);
        bookReleaseDateDPicker.getEditor().setText("");
        bookIsbnField.setText("");
        bookStatusCBox.valueProperty().set(null);
    }

    @FXML
    private void handleResetDVD() {
        dvdTitleField.setText("");
        dvdDirectorField.setText("");
        dvdGenreCBox.valueProperty().set(null);
        dvdReleaseDateDPicker.getEditor().setText("");
        dvdStatusCBox.valueProperty().set(null);
    }

    @FXML
    private void handleResetUser() {
        usernameField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        addressField.setText("");
        birthdayDPicker.getEditor().setText("");
        registrationDateDPicker.getEditor().setText("");
        emailAddressField.setText("");
    }

    public void setApp(App app) {
        this.app = app;
        booksTable.setItems(app.getBooksData());
        dvdTable.setItems(app.getDVDData());
        usersTable.setItems(app.getUsersData());
    }

    public TableView<Book> getBooksTable() {
        return booksTable;
    }

    public TableView<DVD> getDVDTable() {
        return dvdTable;
    }

    public TableView<User> getUsersTable() {
        return usersTable;
    }

    public Book getSelectedBook() {
        return booksTable.getSelectionModel().getSelectedItem();
    }

    public DVD getSelectedDVD() {
        return dvdTable.getSelectionModel().getSelectedItem();
    }

    public User getSelectedUser() {
        return usersTable.getSelectionModel().getSelectedItem();
    }

    public Button getBookAddButton() {
        return bookAddButton;
    }

    public Button getBookEditButton() {
        return bookEditButton;
    }

    public Button getDVDAddButton() {
        return dvdAddButton;
    }

    public Button getDVDEditButton() {
        return dvdEditButton;
    }

    public Button getUserAddButton() {
        return userAddButton;
    }

    public Button getUserEditButton() {
        return userEditButton;
    }
}