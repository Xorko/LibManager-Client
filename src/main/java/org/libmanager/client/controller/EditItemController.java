package org.libmanager.client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.libmanager.client.App;
import org.libmanager.client.I18n;
import org.libmanager.client.component.DurationSpinner;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.DVDGenre;
import org.libmanager.client.enums.Genre;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.client.util.Converter;
import org.libmanager.client.util.DateUtil;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;


public class EditItemController implements Initializable {

    @FXML
    private RowConstraints isbnRow;
    @FXML
    private GridPane editItemRoot;
    @FXML
    private TextField titleField;
    @FXML
    private Label authorLabel;
    @FXML
    private TextField authorField;
    @FXML
    private ComboBox<Genre> genreCBox;
    @FXML
    private DatePicker releaseDateDPicker;
    @FXML
    private Label publisherLabel;
    @FXML
    private TextField publisherField;
    @FXML
    private Label isbnLabel;
    @FXML
    private TextField isbnField;
    @FXML
    private Label durationLabel;
    @FXML
    private DurationSpinner durationSpinner;
    @FXML
    private Button confirmButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button cancelButton;

    private Stage dialogStage;
    private App app;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        titleField.setVisible(true);
        authorField.setVisible(true);
        genreCBox.setVisible(true);
        releaseDateDPicker.setVisible(true);

        releaseDateDPicker.setConverter(new StringConverter<>() {
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
     * Initializes the view with "Add book" settings
     */
    public void initializeAddBook() {
        ObservableList<BookGenre> bookGenres = FXCollections.observableArrayList(Arrays.asList(BookGenre.values()));
        bookGenres.remove(BookGenre.ANY);
        genreCBox.getItems().setAll(bookGenres);
        genreCBox.setConverter(Converter.getGenreConverter());
        genreCBox.valueProperty().set(BookGenre.ANY);
        durationLabel.setVisible(false);
        durationSpinner.setVisible(false);
        confirmButton.setOnAction(event -> handleAddBookConfirm());
        // Enter can be pressed instead of the confirm button
        editItemRoot.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleAddBookConfirm();
            }
        });
    }

    /**
     * Initializes the view with "Add DVD" settings
     */
    public void initializeAddDVD() {
        ObservableList<DVDGenre> dvdGenres = FXCollections.observableArrayList(Arrays.asList(DVDGenre.values()));
        dvdGenres.remove(DVDGenre.ANY);
        genreCBox.getItems().setAll(dvdGenres);
        genreCBox.valueProperty().set(DVDGenre.ANY);
        genreCBox.setConverter(Converter.getGenreConverter());
        authorLabel.setText(I18n.getBundle().getString("label.director"));
        publisherLabel.setVisible(false);
        publisherField.setVisible(false);
        isbnLabel.setVisible(false);
        isbnField.setVisible(false);
        isbnRow.setPercentHeight(0);
        confirmButton.setOnAction(event -> handleAddDvdConfirm());
        // Enter can be pressed instead of the confirm button
        editItemRoot.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleAddDvdConfirm();
            }
        });
    }

    /**
     * Initializes the view with "Edit book" settings
     * @param selectedBook  the selected book
     */
    public void initializeEditBook(Book selectedBook) {
        initializeAddBook();
        titleField.setText(selectedBook.getTitle());
        authorField.setText(selectedBook.getAuthor());
        publisherField.setText(selectedBook.getPublisher());
        genreCBox.valueProperty().set(selectedBook.getGenre());
        releaseDateDPicker.getEditor().setText(DateUtil.format(selectedBook.getReleaseDate()));
        isbnField.setText(selectedBook.getIsbn());
        confirmButton.setOnAction(event -> handleEditBookConfirm(selectedBook));
        // Enter can be pressed instead of the confirm button
        editItemRoot.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleEditBookConfirm(selectedBook);
            }
        });
    }

    /**
     * Initializes the view with "Edit DVD" settings
     * @param selectedDVD   the selected DVD
     */
    public void initializeEditDVD(DVD selectedDVD) {
        initializeAddDVD();
        titleField.setText(selectedDVD.getTitle());
        authorField.setText(selectedDVD.getAuthor());
        genreCBox.valueProperty().set(selectedDVD.getGenre());
        releaseDateDPicker.getEditor().setText(DateUtil.format(selectedDVD.getReleaseDate()));
        durationSpinner.getEditor().setText(selectedDVD.getDuration());
        confirmButton.setOnAction(event -> handleEditDVDConfirm(selectedDVD));
        // Enter can be pressed instead of the confirm button
        editItemRoot.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleEditDVDConfirm(selectedDVD);
            }
        });
    }

    /**
     * Handles confirmation button click when adding book
     */
    private void handleAddBookConfirm() {
        if (fieldsAreValid(true)) {
            Book b = new Book();
            b.setTitle(titleField.getText());
            b.setAuthor(authorField.getText());
            b.setPublisher(publisherField.getText());
            b.setGenre(genreCBox.valueProperty().get());
            b.setReleaseDate(DateUtil.parse(releaseDateDPicker.getEditor().getText()));
            b.setIsbn(isbnField.getText());
            b.setStatus(true);
            app.getBooksData().add(b);
            app.refreshTables();
            dialogStage.close();
        }
    }

    /**
     * Handles confirmation button click when adding DVD
     */
    private void handleAddDvdConfirm() {
        if (fieldsAreValid(false)) {
            DVD d = new DVD();
            d.setTitle(titleField.getText());
            d.setAuthor(authorField.getText());
            d.setGenre(genreCBox.valueProperty().get());
            d.setReleaseDate(DateUtil.parse(releaseDateDPicker.getEditor().getText()));
            d.setDuration(durationSpinner.getEditor().getText());
            d.setStatus(true);
            app.getDVDData().add(d);
            app.refreshTables();
            dialogStage.close();
        }
    }

    /**
     * Handles confirmation button click when editing book
     * @param b  the edited book
     */
    private void handleEditBookConfirm(Book b) {
        if (fieldsAreValid(true)) {
            b.setTitle(titleField.getText());
            b.setAuthor(authorField.getText());
            b.setPublisher(publisherField.getText());
            b.setGenre(genreCBox.valueProperty().get());
            b.setReleaseDate(DateUtil.parse(releaseDateDPicker.getEditor().getText()));
            b.setIsbn(isbnField.getText());
            app.refreshTables();
            dialogStage.close();
        }
    }

    private void handleEditDVDConfirm(DVD d) {
        if (fieldsAreValid(false)) {
            d.setTitle(titleField.getText());
            d.setAuthor(authorField.getText());
            d.setGenre(genreCBox.valueProperty().get());
            d.setDuration(durationSpinner.getEditor().getText());
            app.refreshTables();
            dialogStage.close();
        }
    }

    /**
     * Check if fields are valid
     * @param book  true if checking fields for a book, false if checking fields for a DVD
     * @return  true if fields are valid, false otherwise
     */
    private boolean fieldsAreValid(boolean book) {
        String errMessage = "";
        if (titleField.getText() == null || titleField.getText().length() == 0) {
            errMessage += I18n.getBundle().getString("edit.item.alert.invalid.title") + "\n";
        }
        if (authorField.getText() == null || authorField.getText().length() == 0) {
            if (book)
                errMessage += I18n.getBundle().getString("edit.item.alert.invalid.author") + "\n";
            else
                errMessage += I18n.getBundle().getString("edit.item.alert.invalid.director") + "\n";
        }
        if (book && (publisherField.getText() == null || publisherField.getText().length() == 0)) {
            errMessage += I18n.getBundle().getString("edit.item.alert.invalid.publisher") + "\n";
        }
        if (genreCBox.valueProperty().get() == null || genreCBox.valueProperty().get() == BookGenre.ANY || genreCBox.valueProperty().get() == DVDGenre.ANY) {
            errMessage += I18n.getBundle().getString("edit.item.alert.invalid.genre") + "\n";
        }
        if (releaseDateDPicker.getEditor().getText() == null || !DateUtil.validDate(releaseDateDPicker.getEditor().getText())) {
            errMessage += I18n.getBundle().getString("edit.item.alert.invalid.releasedate") + "\n";
        }
        if (book && (isbnField.getText() == null || isbnField.getText().length() == 0)) {
            errMessage += I18n.getBundle().getString("edit.item.alert.invalid.isbn") + "\n";
        }
        if(!book && (durationSpinner.getEditor().getText() == null || !durationSpinner.getEditor().getText().matches("[0-9]+h[0-9]{0,2}"))) {
            errMessage += I18n.getBundle().getString("edit.item.alert.invalid.duration") + "\n";
        }
        if (errMessage.length() == 0) {
            return true;
        }
        // There's at least one error
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle(I18n.getBundle().getString("login.label.invalidfields"));
        alert.setHeaderText(I18n.getBundle().getString("edit.alert.incorrectfields.header"));
        alert.setContentText(errMessage);

        alert.showAndWait();

        return false;
    }

    /**
     * Handles click on cancel button
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Handles click on reset button
     */
    @FXML
    private void handleReset() {
        titleField.setText("");
        authorField.setText("");
        releaseDateDPicker.getEditor().setText("");
        genreCBox.valueProperty().set(null);
        publisherField.setText("");
        durationSpinner.getEditor().setText("");
        isbnField.setText("");
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setApp(App app) {this.app = app;}
}
