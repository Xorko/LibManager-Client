package org.libmanager.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.libmanager.client.App;
import org.libmanager.client.I18n;
import org.libmanager.client.api.ServerAPI;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.DVDGenre;
import org.libmanager.client.enums.Status;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.client.model.Item;
import org.libmanager.client.model.User;
import org.libmanager.client.util.Converter;
import org.libmanager.client.util.DateUtil;

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

    // TODO: 12/20/20 Split initialization
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

        // --- BOOKS TABLE ---
        bookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        bookAuthorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        bookGenreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().genreProperty().get().getName()));
        bookReleaseDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getReleaseDate())));
        bookPublisherColumn.setCellValueFactory(cellData -> cellData.getValue().publisherProperty());
        bookIsbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        bookStatusColumn.setCellValueFactory(cellData -> {
            Button btn = new Button();

            btn.setMaxWidth(100);

            if (!cellData.getValue().getStatus()) {
                btn.setText(I18n.getBundle().getString("button.return"));
            } else {
                btn.setText(I18n.getBundle().getString("button.available"));
                btn.setDisable(true);
            }

            btn.setOnAction(event -> {
                if (app.getLoggedInUser() != null && app.getLoggedInUser().isAdmin()) {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.initOwner(app.getPrimaryStage());
                    confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    confirmationAlert.setHeaderText(I18n.getBundle().getString("admin.alert.confirm.return.book"));
                    Optional<ButtonType> answer = confirmationAlert.showAndWait();
                    if (answer.isPresent() && answer.get() == ButtonType.YES) {
                        //TODO: Send a request to the server
                        btn.setDisable(true);
                        btn.setText(I18n.getBundle().getString("button.available"));
                        cellData.getValue().setStatus(true);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(app.getPrimaryStage());
                    alert.setHeaderText(I18n.getBundle().getString("alert.not.admin.header"));
                    alert.setContentText(I18n.getBundle().getString("alert.not.admin.book.return.content"));
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
        dvdGenreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().genreProperty().get().getName()));
        dvdDurationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty());
        dvdReleaseDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getReleaseDate())));
        dvdStatusColumn.setCellValueFactory(cellData -> {
            Button btn = new Button();

            btn.setMaxWidth(100);

            if (!cellData.getValue().getStatus()) {
                btn.setText(I18n.getBundle().getString("button.return"));
            } else {
                btn.setText(I18n.getBundle().getString("button.available"));
                btn.setDisable(true);
            }

            btn.setOnAction(event -> {
                if (app.getLoggedInUser() != null && app.getLoggedInUser().isAdmin()) {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.initOwner(app.getPrimaryStage());
                    confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    confirmationAlert.setHeaderText(I18n.getBundle().getString("admin.alert.confirm.return.dvd"));
                    Optional<ButtonType> answer = confirmationAlert.showAndWait();
                    if (answer.isPresent() && answer.get() == ButtonType.YES) {
                        //TODO: Send a request to the server
                        btn.setDisable(true);
                        btn.setText(I18n.getBundle().getString("button.available"));
                        cellData.getValue().setStatus(true);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(app.getPrimaryStage());
                    alert.setHeaderText(I18n.getBundle().getString("alert.not.admin.header"));
                    alert.setContentText(I18n.getBundle().getString("alert.not.admin.dvd.return.content"));
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
            confirmationAlert.setTitle(I18n.getBundle().getString("admin.alert.confirm.deletion.title"));
            confirmationAlert.setHeaderText(I18n.getBundle().getString("admin.alert.confirm.deletion.book"));
            confirmationAlert.setContentText(I18n.getBundle().getString("admin.alert.confirm.deletion.content"));
            Optional<ButtonType> answer = confirmationAlert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                String content = ServerAPI.callDeleteItem(app.getLoggedInUser().getToken(), selected.getId());
                try {
                    if (content != null) {
                        String code = new ObjectMapper().readTree(content).get("code").asText();
                        if (code.equals("OK"))
                            app.getBooksData().remove(selected);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.initOwner(app.getPrimaryStage());
                        alert.setTitle(I18n.getBundle().getString("server.connection.failed.alert"));
                        alert.setHeaderText(I18n.getBundle().getString("server.connection.failed"));
                        alert.showAndWait();
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            app.refreshTables();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle(I18n.getBundle().getString("admin.alert.confirm.deletion.title"));
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
            confirmationAlert.setTitle(I18n.getBundle().getString("admin.alert.confirm.deletion.title"));
            confirmationAlert.setHeaderText(I18n.getBundle().getString("admin.alert.confirm.deletion.dvd"));
            confirmationAlert.setContentText(I18n.getBundle().getString("admin.alert.confirm.deletion.content"));
            Optional<ButtonType> answer = confirmationAlert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                String content = ServerAPI.callDeleteItem(app.getLoggedInUser().getToken(), selected.getId());
                try {
                    if (content != null) {
                        String code = new ObjectMapper().readTree(content).get("code").asText();
                        if (code.equals("OK"))
                            app.getDVDData().remove(selected);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.initOwner(app.getPrimaryStage());
                        alert.setTitle(I18n.getBundle().getString("server.connection.failed.alert"));
                        alert.setHeaderText(I18n.getBundle().getString("server.connection.failed"));
                        alert.showAndWait();
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            app.refreshTables();
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
            confirmationAlert.setTitle(I18n.getBundle().getString("admin.alert.confirm.deletion.title"));
            confirmationAlert.setHeaderText(I18n.getBundle().getString("admin.alert.confirm.deletion.user"));
            confirmationAlert.setContentText(I18n.getBundle().getString("admin.alert.confirm.deletion.content"));
            Optional<ButtonType> answer = confirmationAlert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                app.getUsersData().remove(selected);
            }
            app.refreshTables();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle(I18n.getBundle().getString("alert.noselection.user.title"));
            alert.setHeaderText(I18n.getBundle().getString("alert.noselection.user.title"));
            alert.setContentText(I18n.getBundle().getString("alert.noselection.user.delete.content"));
            alert.showAndWait();
        }
    }

    @FXML
    private void handleBookSearch() {
        Task<Void> searchBooks = new Task<>() {
            @Override
            protected Void call() {
                String response = ServerAPI.callSearchBooks(
                        bookIsbnField.getText().length() == 0 ? null : bookIsbnField.getText(),
                        bookAuthorField.getText().length() == 0 ? null : bookAuthorField.getText(),
                        bookTitleField.getText().length() == 0 ? null : bookTitleField.getText(),
                        bookPublisherField.getText().length() == 0 ? null : bookPublisherField.getText(),
                        !DateUtil.validDate(bookReleaseDateDPicker.getEditor().getText()) ? null : DateUtil.formatDB(DateUtil.parse(bookReleaseDateDPicker.getEditor().getText())),
                        bookGenreCBox.valueProperty().get().toString().length() == 0 ? null : bookGenreCBox.valueProperty().get().toString(),
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
                String response = ServerAPI.callSearchDVD(
                        dvdDirectorField.getText().length() == 0 ? null : dvdDirectorField.getText(),
                        dvdTitleField.getText().length() == 0 ? null : dvdTitleField.getText(),
                        !DateUtil.validDate(dvdReleaseDateDPicker.getEditor().getText()) ? null : DateUtil.formatDB(DateUtil.parse(dvdReleaseDateDPicker.getEditor().getText())),
                        dvdGenreCBox.valueProperty().get().toString().length() == 0 ? null : dvdGenreCBox.valueProperty().get().toString(),
                        null
                );
                app.loadDVDFromJSON(response);
                return null;
            }
        };
        new Thread(searchDVDs).start();
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
        app.getBooksFromDB();
        app.refreshTables();
    }

    @FXML
    private void handleResetDVD() {
        dvdTitleField.setText("");
        dvdDirectorField.setText("");
        dvdGenreCBox.valueProperty().set(DVDGenre.ANY);
        dvdReleaseDateDPicker.getEditor().setText("");
        dvdStatusCBox.valueProperty().set(Status.ANY);
        app.getDVDFromDB();
        app.refreshTables();
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
        app.refreshTables();
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