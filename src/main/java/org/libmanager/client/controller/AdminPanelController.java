package org.libmanager.client.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.libmanager.client.App;
import org.libmanager.client.enums.ItemType;
import org.libmanager.client.util.I18n;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.DVDGenre;
import org.libmanager.client.enums.Status;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.client.model.Reservation;
import org.libmanager.client.model.User;
import org.libmanager.client.service.Requests;
import org.libmanager.client.util.Converter;
import org.libmanager.client.util.DateUtil;
import org.libmanager.client.util.ResponseUtil;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {

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
    private TableColumn<Book, Integer> bookAvailableCopiesColumn;
    @FXML
    private TableColumn<Book, Integer> bookTotalCopiesColumn;

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
    @FXML
    private TableColumn<DVD, Integer> dvdAvailableCopiesColumn;
    @FXML
    private TableColumn<DVD, Integer> dvdTotalCopiesColumn;

    // Users
    @FXML
    private TextField userUsernameField;
    @FXML
    private TextField userFirstNameField;
    @FXML
    private TextField userLastNameField;
    @FXML
    private TextField userAddressField;
    @FXML
    private DatePicker userBirthdayDPicker;
    @FXML
    private DatePicker userRegistrationDateDPicker;
    @FXML
    private TextField userEmailAddressField;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> userUsernameColumn;
    @FXML
    private TableColumn<User, String> userFirstNameColumn;
    @FXML
    private TableColumn<User, String> userLastNameColumn;
    @FXML
    private TableColumn<User, String> userAddressColumn;
    @FXML
    private TableColumn<User, String> userBirthdayColumn;
    @FXML
    private TableColumn<User, String> userEmailAddressColumn;
    @FXML
    private TableColumn<User, String> userRegistrationDateColumn;
    @FXML
    private TableColumn<User, Button> userReservationsColumn;

    // Reservations
    @FXML
    private TextField reservationIdField;
    @FXML
    private TextField reservationUsernameField;
    @FXML
    private TextField reservationTitleField;
    @FXML
    private ComboBox<ItemType> reservationTypeCBox;
    @FXML
    private TableView<Reservation> reservationsTable;
    @FXML
    private TableColumn<Reservation, Long> reservationIdColumn;
    @FXML
    private TableColumn<Reservation, String> reservationTypeColumn;
    @FXML
    private TableColumn<Reservation, String> reservationUsernameColumn;
    @FXML
    private TableColumn<Reservation, String> reservationTitleColumn;
    @FXML
    private TableColumn<Reservation, Button> reservationDeleteColumn;

    // Buttons
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

    // TODO: 12/20/20 Split tabs in multiple views
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        bookGenreCBox.getItems().setAll(BookGenre.values());
        bookGenreCBox.valueProperty().set(BookGenre.ANY);
        bookGenreCBox.setConverter(Converter.getBookGenreConverter());

        dvdGenreCBox.getItems().setAll(DVDGenre.values());
        dvdGenreCBox.valueProperty().set(DVDGenre.ANY);
        dvdGenreCBox.setConverter(Converter.getDvdGenreConverter());

        bookStatusCBox.getItems().setAll(Status.values());
        bookStatusCBox.valueProperty().set(Status.ANY);
        bookStatusCBox.setConverter(Converter.getStatusConverter());

        dvdStatusCBox.getItems().setAll(Status.values());
        dvdStatusCBox.valueProperty().set(Status.ANY);
        dvdStatusCBox.setConverter(Converter.getStatusConverter());

        reservationTypeCBox.getItems().setAll(ItemType.values());
        reservationTypeCBox.valueProperty().set(ItemType.ANY);
        reservationTypeCBox.setConverter(Converter.getTypeConverter());

        // --- BOOKS TABLE ---
        bookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        bookAuthorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        bookGenreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().genreProperty().get().getName()));
        bookReleaseDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getReleaseDate())));
        bookPublisherColumn.setCellValueFactory(cellData -> cellData.getValue().publisherProperty());
        bookIsbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        bookAvailableCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().availableCopiesProperty().asObject());
        bookTotalCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().totalCopiesProperty().asObject());

        // Books table columns dimensions
        bookTitleColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.25));
        bookAuthorColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.16));
        bookGenreColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.12));
        bookReleaseDateColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.1));
        bookPublisherColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.13));
        bookIsbnColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.05));
        bookAvailableCopiesColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.1));
        bookTotalCopiesColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.1));

        // --- DVD TABLE ---
        dvdTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        dvdDirectorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        dvdGenreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().genreProperty().get().getName()));
        dvdDurationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty());
        dvdReleaseDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getReleaseDate())));
        dvdAvailableCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().availableCopiesProperty().asObject());
        dvdTotalCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().availableCopiesProperty().asObject());

        // DVD table columns dimensions
        dvdTitleColumn.prefWidthProperty().bind(dvdTable.widthProperty().multiply(0.25));
        dvdDirectorColumn.prefWidthProperty().bind(dvdTable.widthProperty().multiply(0.20));
        dvdGenreColumn.prefWidthProperty().bind(dvdTable.widthProperty().multiply(0.15));
        dvdDurationColumn.prefWidthProperty().bind(dvdTable.widthProperty().multiply(0.09));
        dvdReleaseDateColumn.prefWidthProperty().bind(dvdTable.widthProperty().multiply(0.1));
        dvdAvailableCopiesColumn.prefWidthProperty().bind(dvdTable.widthProperty().multiply(0.05));
        dvdTotalCopiesColumn.prefWidthProperty().bind(dvdTable.widthProperty().multiply(0.05));

        // --- USERS TABLE ---
        userUsernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        userFirstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        userLastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        userAddressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        userBirthdayColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getBirthday())));
        userEmailAddressColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        userRegistrationDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getRegistrationDate())));

        // --- Users table columns dimensions ---
        userUsernameColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.1));
        userFirstNameColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        userLastNameColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        userAddressColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        userEmailAddressColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        userBirthdayColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        userRegistrationDateColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));

        // --- RESERVATIONS TABLE ---
        reservationIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        reservationUsernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        reservationTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        reservationTypeColumn.setCellValueFactory(cellData -> {
            SimpleStringProperty typeProperty = new SimpleStringProperty(null);

            switch (cellData.getValue().itemTypeProperty().getValue()) {
                case "BOOK":
                    typeProperty.set(I18n.getBundle().getString("label.book"));
                    break;
                case "DVD":
                    typeProperty.set(I18n.getBundle().getString("label.dvd"));
                    break;
                default:
                    typeProperty.set(I18n.getBundle().getString("enum.unknown"));
            }
            return typeProperty;
        });
        reservationDeleteColumn.setCellValueFactory(cellData -> {
            Button btn = new Button();

            btn.setMaxWidth(100);
            btn.setText(I18n.getBundle().getString("button.return"));

            btn.setOnAction(event -> handleDeleteReservation(cellData.getValue()));

            return new SimpleObjectProperty<>(btn);
        });

        // --- Reservations table columns dimensions ---
        reservationIdColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.25));
        reservationTypeColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.1));
        reservationUsernameColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.25));
        reservationTitleColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.25));
        reservationDeleteColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.15));

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

        userBirthdayDPicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return DateUtil.format(date);
            }

            @Override
            public LocalDate fromString(String string) {
                return DateUtil.parse(string);
            }
        });

        userRegistrationDateDPicker.setConverter(new StringConverter<>() {
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

    /**
     * Handle add and edit buttons for items
     * @param e The event
     */
    @FXML
    private void handleItemManagementButtons(ActionEvent e) {
        app.showEditItemView(e);
    }

    /**
     * Handle add and edit buttons for users
     * @param e The event
     */
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
            confirmationAlert.setTitle(I18n.getBundle().getString("alert.confirm.deletion.title"));
            confirmationAlert.setHeaderText(I18n.getBundle().getString("alert.confirm.deletion.book"));
            confirmationAlert.setContentText(I18n.getBundle().getString("alert.confirm.deletion.content"));
            Optional<ButtonType> answer = confirmationAlert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                String response = Requests.callDeleteItem(app.getLoggedInUser().getToken(), Long.toString(selected.getId()));
                if (ResponseUtil.analyze(response, app.getPrimaryStage()))
                    app.loadAllBooks();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle(I18n.getBundle().getString("alert.confirm.deletion.title"));
            alert.setHeaderText(I18n.getBundle().getString("alert.noselection.book.title"));
            alert.setContentText(I18n.getBundle().getString("alert.noselection.book.delete.content"));
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
            confirmationAlert.setTitle(I18n.getBundle().getString("alert.confirm.deletion.title"));
            confirmationAlert.setHeaderText(I18n.getBundle().getString("alert.confirm.deletion.dvd"));
            confirmationAlert.setContentText(I18n.getBundle().getString("alert.confirm.deletion.content"));
            Optional<ButtonType> answer = confirmationAlert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                String response = Requests.callDeleteItem(app.getLoggedInUser().getToken(), Long.toString(selected.getId()));
                if (ResponseUtil.analyze(response, app.getPrimaryStage()))
                    app.loadAllDVDs();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle(I18n.getBundle().getString("alert.noselection.dvd.title"));
            alert.setHeaderText(I18n.getBundle().getString("alert.noselection.dvd.title"));
            alert.setContentText(I18n.getBundle().getString("alert.noselection.dvd.delete.content"));
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
            confirmationAlert.setTitle(I18n.getBundle().getString("alert.confirm.deletion.title"));
            confirmationAlert.setHeaderText(I18n.getBundle().getString("alert.confirm.deletion.user"));
            confirmationAlert.setContentText(I18n.getBundle().getString("alert.confirm.deletion.content"));
            Optional<ButtonType> answer = confirmationAlert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                String response = Requests.callDeleteUser(app.getLoggedInUser().getToken(), selected.getUsername());
                if (ResponseUtil.analyze(response, app.getPrimaryStage()))
                    app.loadAllUsers();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle(I18n.getBundle().getString("alert.noselection.user.title"));
            alert.setHeaderText(I18n.getBundle().getString("alert.noselection.user.title"));
            alert.setContentText(I18n.getBundle().getString("alert.noselection.user.delete.content"));
            alert.showAndWait();
        }
    }

    /**
     * Delete a reservation
     * @param reservation The reservation to delete
     */
    private void handleDeleteReservation(Reservation reservation) {
        if (app.getLoggedInUser() != null && app.getLoggedInUser().isAdmin()) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.initOwner(app.getPrimaryStage());
            confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            confirmationAlert.setHeaderText(I18n.getBundle().getString("alert.confirm.return"));
            Optional<ButtonType> answer = confirmationAlert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                String response = Requests.callDeleteReservation(app.getLoggedInUser().getToken(), Long.toString(reservation.getId()));
                if (ResponseUtil.analyze(response, app.getPrimaryStage()))
                    app.loadAllReservations();
            }
        }
    }

    @FXML
    private void handleBookSearch() {
        Task<Void> searchBooks = new Task<>() {
            @Override
            protected Void call() {
                String response = Requests.callSearchBooks(
                        bookIsbnField.getText().length() == 0 ? null : bookIsbnField.getText(),
                        bookAuthorField.getText().length() == 0 ? null : bookAuthorField.getText(),
                        bookTitleField.getText().length() == 0 ? null : bookTitleField.getText(),
                        bookPublisherField.getText().length() == 0 ? null : bookPublisherField.getText(),
                        !DateUtil.validDate(bookReleaseDateDPicker.getEditor().getText()) ? null : DateUtil.formatDB(DateUtil.parse(bookReleaseDateDPicker.getEditor().getText())),
                        bookGenreCBox.valueProperty().get() == BookGenre.ANY ? null : bookGenreCBox.valueProperty().get().toString(),
                        bookStatusCBox.getValue().getStatus() == 2 ? null : Integer.toString(bookStatusCBox.getValue().getStatus())
                );
                app.loadBooksFromJSON(response);
                return null;
            }
        };
        new Thread(searchBooks).start();
    }

    @FXML
    private void handleDVDSearch() {
        Task<Void> searchDVDs = new Task<>() {
            @Override
            protected Void call() {
                String response = Requests.callSearchDVD(
                        dvdDirectorField.getText().length() == 0 ? null : dvdDirectorField.getText(),
                        dvdTitleField.getText().length() == 0 ? null : dvdTitleField.getText(),
                        !DateUtil.validDate(dvdReleaseDateDPicker.getEditor().getText()) ? null : DateUtil.formatDB(DateUtil.parse(dvdReleaseDateDPicker.getEditor().getText())),
                        dvdGenreCBox.valueProperty().get() == DVDGenre.ANY ? null : dvdGenreCBox.valueProperty().get().toString(),
                        dvdStatusCBox.getValue().getStatus() == 2 ? null : Integer.toString(dvdStatusCBox.getValue().getStatus())
                );
                app.loadDVDsFromJSON(response);
                return null;
            }
        };
        new Thread(searchDVDs).start();
    }

    @FXML
    private void handleUserSearch() {
        Task<Void> searchUsers = new Task<>() {
            @Override
            protected Void call() {
                String response = Requests.callSearchUser(
                        app.getLoggedInUser().getToken(),
                        userUsernameField.getText().length() == 0 ? null : userUsernameField.getText(),
                        userEmailAddressField.getText().length() == 0 ? null : userEmailAddressField.getText(),
                        userFirstNameField.getText().length() == 0 ? null : userFirstNameField.getText(),
                        userLastNameField.getText().length() == 0 ? null : userLastNameField.getText(),
                        userAddressField.getText().length() == 0 ? null : userAddressField.getText(),
                        !DateUtil.validDate(userBirthdayDPicker.getEditor().getText()) ? null : DateUtil.formatDB(DateUtil.parse(userBirthdayDPicker.getEditor().getText())),
                        !DateUtil.validDate(userRegistrationDateDPicker.getEditor().getText()) ? null : DateUtil.formatDB(DateUtil.parse(userRegistrationDateDPicker.getEditor().getText()))
                );
                app.loadUsersFromJSON(response);
                return null;
            }
        };
        new Thread(searchUsers).start();
    }

    @FXML
    private void handleReservationSearch() {
        Task<Void> searchReservations = new Task<>() {
            @Override
            protected Void call() {
                String response = Requests.callSearchReservation(
                        app.getLoggedInUser().getToken(),
                        reservationIdField.getText().length() == 0 ? null : reservationIdField.getText(),
                        reservationUsernameField.getText().length() == 0 ? null: reservationUsernameField.getText(),
                        reservationUsernameField.getText().length() == 0 ? null : reservationUsernameField.getText(),
                        reservationTypeCBox.valueProperty().get() == ItemType.ANY ? null : reservationTypeCBox.valueProperty().get().toString()
                );
                app.loadReservationsFromJSON(response);
                return null;
            }
        };
        new Thread(searchReservations).start();
    }

    @FXML
    private void handleResetBook() {
        bookTitleField.setText("");
        bookAuthorField.setText("");
        bookPublisherField.setText("");
        bookGenreCBox.valueProperty().set(BookGenre.ANY);
        bookReleaseDateDPicker.getEditor().setText("");
        bookIsbnField.setText("");
        bookStatusCBox.valueProperty().set(Status.ANY);

        Task<Void> getData = new Task<>() {
            @Override
            protected Void call() {
                app.loadAllBooks();
                return null;
            }
        };
        new Thread(getData).start();
    }

    @FXML
    private void handleResetDVD() {
        dvdTitleField.setText("");
        dvdDirectorField.setText("");
        dvdGenreCBox.valueProperty().set(DVDGenre.ANY);
        dvdReleaseDateDPicker.getEditor().setText("");
        dvdStatusCBox.valueProperty().set(Status.ANY);

        Task<Void> getData = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                app.loadAllDVDs();
                return null;
            }
        };
        new Thread(getData).start();
    }

    @FXML
    private void handleResetUser() {
        userUsernameField.setText("");
        userFirstNameField.setText("");
        userLastNameField.setText("");
        userAddressField.setText("");
        userBirthdayDPicker.getEditor().setText("");
        userRegistrationDateDPicker.getEditor().setText("");
        userEmailAddressField.setText("");

        Task<Void> getData = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                app.loadAllUsers();
                return null;
            }
        };
        new Thread(getData).start();
    }

    @FXML
    private void handleResetReservation() {
        reservationIdField.setText("");
        reservationUsernameField.setText("");
        reservationTitleField.setText("");

        Task<Void> getData = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                app.loadAllReservations();
                return null;
            }
        };
        new Thread(getData).start();
    }

    public void setApp(App app) {
        this.app = app;
        booksTable.setItems(app.getBooksData());
        dvdTable.setItems(app.getDVDData());
        usersTable.setItems(app.getUsersData());
        reservationsTable.setItems(app.getReservationsData());
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