package org.libmanager.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.libmanager.client.App;
import org.libmanager.client.I18n;
import org.libmanager.client.api.ServerAPI;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.DVDGenre;
import org.libmanager.client.enums.Genre;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.client.util.Converter;
import org.libmanager.client.util.DateUtil;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {

    // Book
    @FXML
    private TextField bookTitleField;
    @FXML
    private TextField bookAuthorField;
    @FXML
    private TextField bookPublisherField;
    @FXML
    private ComboBox<BookGenre> bookGenreCBox;
    @FXML
    private DatePicker bookReleaseDateDPicker;
    @FXML
    private TextField bookIsbnField;
    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableColumn<Book, String> bookTitleColumn;
    @FXML
    private TableColumn<Book, String> bookAuthorColumn;
    @FXML
    private TableColumn<Book, String> bookGenreColumn;
    @FXML
    private TableColumn<Book, String> bookReleaseDateColumn;
    @FXML
    private TableColumn<Book, String> bookPublisherColumn;
    @FXML
    private TableColumn<Book, String> bookIsbnColumn;
    @FXML
    private TableColumn<Book, Button> bookStatusColumn;

    // DVD
    @FXML
    private TextField dvdTitleField;
    @FXML
    private TextField dvdDirectorField;
    @FXML
    private ComboBox<DVDGenre> dvdGenreCBox;
    @FXML
    private DatePicker dvdReleaseDateDPicker;
    @FXML
    private TableView<DVD> dvdTable;
    @FXML
    private TableColumn<DVD, String> dvdTitleColumn;
    @FXML
    private TableColumn<DVD, String> dvdDirectorColumn;
    @FXML
    private TableColumn<DVD, String> dvdGenreColumn;
    @FXML
    private TableColumn<DVD, String> dvdReleaseDateColumn;
    @FXML
    private TableColumn<DVD, String> dvdDurationColumn;
    @FXML
    private TableColumn<DVD, Button> dvdStatusColumn;

    // App
    private App app;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        initBookTab();
        initDVDTab();
    }

    private void initBookTab() {
        bookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        bookAuthorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        bookGenreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().genreProperty().get().getName()));
        bookReleaseDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getReleaseDate())));
        bookPublisherColumn.setCellValueFactory(cellData -> cellData.getValue().publisherProperty());
        bookIsbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        bookStatusColumn.setCellValueFactory(cellData -> {
            Button btn = new Button();

            btn.setMaxWidth(100);

            if (cellData.getValue().getStatus()) {
                btn.setText(I18n.getBundle().getString("reservation.button.status.available"));
            } else {
                btn.setText(I18n.getBundle().getString("reservation.button.status.unavailable"));
                btn.setDisable(true);
            }

            btn.setOnAction(event -> {
                if (app.getLoggedInUser() != null) {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.initOwner(app.getPrimaryStage());
                    confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    confirmationAlert.setTitle(I18n.getBundle().getString("alert.confirmation.borrowing.title"));
                    confirmationAlert.setHeaderText(I18n.getBundle().getString("alert.confirmation.borrowing.book.header"));
                    Optional<ButtonType> answer = confirmationAlert.showAndWait();
                    if (answer.isPresent() && answer.get() == ButtonType.YES) {
                        //TODO: Send a request to the server to borrow the book
                        cellData.getValue().setStatus(false);
                        btn.setDisable(true);
                        btn.setText(I18n.getBundle().getString("reservation.button.status.unavailable"));
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(app.getPrimaryStage());
                    alert.setTitle(I18n.getBundle().getString("reservation.alert.label.notloggedin"));
                    alert.setHeaderText(I18n.getBundle().getString("reservation.alert.label.notloggedin"));
                    alert.setContentText(I18n.getBundle().getString("alert.warning.borrowing.book.notloggedin"));
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

        bookGenreCBox.getItems().setAll(BookGenre.values());
        bookGenreCBox.valueProperty().set(BookGenre.ANY);
        bookGenreCBox.setConverter(Converter.getBookGenreConverter());
    }

    private void initDVDTab() {
        dvdTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        dvdDirectorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        dvdGenreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().genreProperty().get().getName()));
        dvdDurationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty());
        dvdReleaseDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getReleaseDate())));
        dvdStatusColumn.setCellValueFactory(cellData -> {
            Button btn = new Button();

            btn.setMaxWidth(100);

            if (cellData.getValue().getStatus()) {
                btn.setText(I18n.getBundle().getString("reservation.button.status.available"));
            } else {
                btn.setText(I18n.getBundle().getString("reservation.button.status.unavailable"));
                btn.setDisable(true);
            }

            btn.setOnAction(event -> {
                if (app.getLoggedInUser() != null) {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.initOwner(app.getPrimaryStage());
                    confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    confirmationAlert.setTitle(I18n.getBundle().getString("alert.confirmation.borrowing.title"));
                    confirmationAlert.setHeaderText(I18n.getBundle().getString("alert.confirmation.borrowing.dvd.header"));
                    Optional<ButtonType> answer = confirmationAlert.showAndWait();
                    if (answer.isPresent() && answer.get() == ButtonType.YES) {
                        //TODO: Send a request to the server to borrow the dvd
                        cellData.getValue().setStatus(false);
                        btn.setDisable(true);
                        btn.setText(I18n.getBundle().getString("reservation.button.status.unavailable"));
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(app.getPrimaryStage());
                    alert.setTitle(I18n.getBundle().getString("reservation.alert.label.notloggedin"));
                    alert.setHeaderText(I18n.getBundle().getString("reservation.alert.label.notloggedin"));
                    alert.setContentText(I18n.getBundle().getString("alert.warning.borrowing.dvd.notloggedin"));
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

        dvdGenreCBox.getItems().setAll(DVDGenre.values());
        dvdGenreCBox.valueProperty().set(DVDGenre.ANY);
        dvdGenreCBox.setConverter(Converter.getDvdGenreConverter());
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
                            null
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
    private void handleBookReset() {
        bookTitleField.setText("");
        bookAuthorField.setText("");
        bookPublisherField.setText("");
        bookGenreCBox.valueProperty().set(BookGenre.ANY);
        bookReleaseDateDPicker.getEditor().setText("");
        bookIsbnField.setText("");
        app.getBooksFromDB();
        app.refreshTables();
    }

    @FXML
    private void handleDVDReset() {
        dvdTitleField.setText("");
        dvdDirectorField.setText("");
        dvdGenreCBox.valueProperty().set(DVDGenre.ANY);
        dvdReleaseDateDPicker.getEditor().setText("");
        app.getDVDFromDB();
        app.refreshTables();
    }

    public void setApp(App app) {
        this.app = app;
        booksTable.setItems(app.getBooksData());
        dvdTable.setItems(app.getDVDData());
    }

    public TableView<Book> getBooksTable() {
        return booksTable;
    }

    public TableView<DVD> getDvdTable() {
        return dvdTable;
    }
}