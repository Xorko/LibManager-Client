package org.libmanager.client.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.libmanager.client.App;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.DVDGenre;
import org.libmanager.client.enums.Status;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.client.service.Requests;
import org.libmanager.client.util.Converter;
import org.libmanager.client.util.DateUtil;
import org.libmanager.client.util.I18n;
import org.libmanager.client.util.ResponseUtil;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminPanelItemsController implements Initializable {

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

    // Buttons
    @FXML
    private Button bookAddButton;
    @FXML
    private Button bookEditButton;
    @FXML
    private Button dvdAddButton;
    @FXML
    private Button dvdEditButton;

    private App app;

    @Override
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

    /**
     * Handle add and edit buttons for items
     * @param e The event
     */
    @FXML
    private void handleItemManagementButtons(ActionEvent e) {
        showEditItemView(e);
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
            dialogStage.initOwner(app.getPrimaryStage());
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            EditItemController controller = loader.getController();

            // The source of the event is the "Add book" button
            if (((Button) e.getSource()).getId().equals(getBookAddButton().getId())) {
                controller.initializeAddBook();
                dialogStage.setTitle(I18n.getBundle().getString("label.add.book"));
            }
            // The source of the event is the "Add DVD" button
            else if (((Button) e.getSource()).getId().equals(getDVDAddButton().getId())) {
                controller.initializeAddDVD();
                dialogStage.setTitle(I18n.getBundle().getString("label.add.dvd"));
            }
            // The source of the event is the "Edit book" button
            else if (((Button) e.getSource()).getId().equals(getBookEditButton().getId())) {
                Book selected = getSelectedBook();
                if (selected != null) {
                    controller.initializeEditBook(selected);
                    dialogStage.setTitle(I18n.getBundle().getString("label.editing.title") + selected.getTitle());
                } else {
                    noError = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(app.getPrimaryStage());
                    alert.setTitle(I18n.getBundle().getString("alert.noselection.book.title"));
                    alert.setHeaderText(I18n.getBundle().getString("alert.noselection.book.title"));
                    alert.setContentText(I18n.getBundle().getString("alert.noselection.book.edit.content"));
                    alert.showAndWait();
                }
            }
            // The source of the event is the "Edit DVD" button
            else if (((Button) e.getSource()).getId().equals(getDVDEditButton().getId())) {
                DVD selected = getSelectedDVD();
                if (selected != null) {
                    controller.initializeEditDVD(selected);
                    dialogStage.setTitle(I18n.getBundle().getString("label.editing.title") + selected.getTitle());
                } else {
                    noError = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(app.getPrimaryStage());
                    alert.setTitle(I18n.getBundle().getString("alert.noselection.dvd.title"));
                    alert.setHeaderText(I18n.getBundle().getString("alert.noselection.dvd.title"));
                    alert.setContentText(I18n.getBundle().getString("alert.noselection.dvd.edit.content"));
                    alert.showAndWait();
                }
            }

            if (noError) {
                controller.setDialogStage(dialogStage);
                controller.setApp(app);

                dialogStage.showAndWait();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public Book getSelectedBook() {
        return booksTable.getSelectionModel().getSelectedItem();
    }

    public DVD getSelectedDVD() {
        return dvdTable.getSelectionModel().getSelectedItem();
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

    public void setApp(App app) {
        this.app = app;
        booksTable.setItems(app.getBooksData());
        dvdTable.setItems(app.getDVDData());
    }
}
