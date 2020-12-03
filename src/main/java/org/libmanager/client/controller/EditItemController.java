package org.libmanager.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.libmanager.client.App;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.DVDGenre;
import org.libmanager.client.enums.Genre;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.client.util.DateUtil;

import java.time.LocalDate;


public class EditItemController {

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
    private TextField durationField;
    @FXML
    private Button confirmButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button cancelButton;

    private Stage dialogStage;
    private App app;

    @FXML
    private void initialize() {
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
        genreCBox.getItems().setAll(BookGenre.values());
        durationLabel.setVisible(false);
        durationField.setVisible(false);
        confirmButton.setOnAction(event -> handleAddBookConfirm());
    }

    /**
     * Initializes the view with "Add DVD" settings
     */
    public void initializeAddDvd() {
        genreCBox.getItems().setAll(DVDGenre.values());
        authorLabel.setText("Réalisateur");
        publisherLabel.setVisible(false);
        publisherField.setVisible(false);
        isbnLabel.setVisible(false);
        isbnField.setVisible(false);
        confirmButton.setOnAction(event -> handleAddDvdConfirm());
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
    }

    /**
     * Initializes the view with "Edit DVD" settings
     * @param selectedDVD   the selected DVD
     */
    public void initializeEditDvd(DVD selectedDVD) {
        initializeAddDvd();
        titleField.setText(selectedDVD.getTitle());
        authorField.setText(selectedDVD.getAuthor());
        genreCBox.valueProperty().set(selectedDVD.getGenre());
        releaseDateDPicker.getEditor().setText(DateUtil.format(selectedDVD.getReleaseDate()));
        durationField.setText(selectedDVD.getDuration());
        confirmButton.setOnAction(event -> handleEditDVDConfirm(selectedDVD));

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
            d.setDuration(durationField.getText());
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
            d.setDuration(durationField.getText());
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
            errMessage += "Pas de titre valide\n";
        }
        if (authorField.getText() == null || authorField.getText().length() == 0) {
            if (book)
                errMessage += "Pas d'auteur valide\n";
            else
                errMessage += "Pas de réalisateur valide\n";
        }
        if (book && (publisherField.getText() == null || publisherField.getText().length() == 0)) {
            errMessage += "Pas d'éditeur valide\n";
        }
        if (genreCBox.valueProperty().get() == null) {
            errMessage += "Pas de genre sélectionné\n";
        }
        if (releaseDateDPicker.getEditor().getText() == null || !DateUtil.validDate(releaseDateDPicker.getEditor().getText())) {
            errMessage += "Pas de date valide\n";
        }
        if (book && (isbnField.getText() == null || isbnField.getText().length() == 0)) {
            errMessage += "Pas d'ISBN valide\n";
        }
        // durationField text should be at least 2 characters long because we need to put at least one number and an unit (sec/min/h)
        // TextField should be replaced by a spinner later or at least we need to implement a validator for it
        if(!book && (durationField.getText() == null || durationField.getText().length() < 2)) {
            errMessage += "Pas de durée valide";
        }
        if (errMessage.length() == 0) {
            return true;
        } else {
            // There's at least one error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Champs invalides");
            alert.setHeaderText("Veuillez corriger les champs invalides");
            alert.setContentText(errMessage);

            alert.showAndWait();

            return false;
        }
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
        durationField.setText("");
        isbnField.setText("");
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setApp(App app) {this.app = app;}
}
