package org.libmanager.client.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.libmanager.client.App;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.DVDGenre;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.client.util.DateUtil;

import java.time.LocalDate;
import java.util.Optional;

public class ReservationController {

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
    private void initialize() {
        bookGenreCBox.getItems().setAll(BookGenre.values());
        dvdGenreCBox.getItems().setAll(DVDGenre.values());

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

            if (cellData.getValue().getStatus()) {
                btn.setText("Emprunter");
            } else {
                btn.setText("Indisponible");
                btn.setDisable(true);
            }

            btn.setOnAction(event -> {
                if (app.getLoggedInUser() != null) {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.initOwner(app.getPrimaryStage());
                    confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    confirmationAlert.setTitle("Confirmation de l'emprunt");
                    confirmationAlert.setHeaderText("Voulez vous vraiment emprunter ce livre ?");
                    Optional<ButtonType> answer = confirmationAlert.showAndWait();
                    if (answer.isPresent() && answer.get() == ButtonType.YES) {
                        //TODO: Send a request to the server to borrow the book
                        cellData.getValue().setStatus(false);
                        btn.setDisable(true);
                        btn.setText("Indisponible");
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(app.getPrimaryStage());
                    alert.setTitle("Vous n'êtes pas connecté");
                    alert.setHeaderText("Vous n'êtes pas connecté");
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

            if (cellData.getValue().getStatus()) {
                btn.setText("Emprunter");
            } else {
                btn.setText("Indisponible");
                btn.setDisable(true);
            }

            btn.setOnAction(event -> {
                if (app.getLoggedInUser() != null) {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.initOwner(app.getPrimaryStage());
                    confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    confirmationAlert.setTitle("Confirmation de l'emprunt");
                    confirmationAlert.setHeaderText("Voulez vous vraiment emprunter ce DVD ?");
                    Optional<ButtonType> answer = confirmationAlert.showAndWait();
                    if (answer.isPresent() && answer.get() == ButtonType.YES) {
                        //TODO: Send a request to the server to borrow the dvd
                        cellData.getValue().setStatus(false);
                        btn.setDisable(true);
                        btn.setText("Indisponible");
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(app.getPrimaryStage());
                    alert.setTitle("Vous n'êtes pas connecté");
                    alert.setHeaderText("Vous n'êtes pas connecté");
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
    }

    @FXML
    private void handleBookSearch() {
        //TODO
    }

    @FXML
    private void handleDVDSearch() {
        //TODO
    }

    @FXML
    private void handleBookReset() {
        bookTitleField.setText("");
        bookAuthorField.setText("");
        bookPublisherField.setText("");
        bookGenreCBox.valueProperty().set(null);
        bookReleaseDateDPicker.getEditor().setText("");
        bookIsbnField.setText("");
        //TODO: Reload list ?
    }

    @FXML
    private void handleDVDReset() {
        dvdTitleField.setText("");
        dvdDirectorField.setText("");
        dvdGenreCBox.valueProperty().set(null);
        dvdReleaseDateDPicker.getEditor().setText("");
        //TODO: Reload list ?
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