package org.libmanager.client.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import org.libmanager.client.TestBase;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.Status;
import org.libmanager.client.model.Book;
import org.libmanager.server.response.Response;
import org.mockito.Mock;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Parameter;
import org.mockserver.verify.VerificationTimes;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.libmanager.client.JavaFXIds.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class AddBookPage {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final TestBase driver;

    public AddBookPage(TestBase driver) {
        this.driver = driver;
    }

    public AddBookPage addBook(String title, String author, String publisher, BookGenre genre, String releaseDate, String isbn, int copies) {
        fillFields(title, author, publisher, genre, releaseDate,isbn, copies);
        confirm();
        return this;
    }

    public AddBookPage fillFields(String title, String author, String publisher, BookGenre genre, String releaseDate, String isbn, int copies) {
        clearOutInputFields();
        enterBookTitle(title).enterBookAuthor(author).enterBookPublisher(publisher).enterBookGenre(genre).
                enterBookReleaseDate(releaseDate).enterBookIsbn(isbn).enterBookCopies(copies);
        return this;
    }

    public AddBookPage clearOutInputFields() {
        clearBookTitle().clearBookAuthor().clearBookPublisher().clearBookGenre().clearBookReleaseDate().clearBookIsbn()
                .clearBookCopies();
        return this;
    }

    public AddBookPage confirm() {
        driver.clickOn(CONFIRM_ADD);
        return this;
    }

    public boolean allFieldsClear() {
        return getBookTitle().getText().isEmpty() && getBookAuthor().getText().isEmpty() &&
                getBookPublisher().getText().isEmpty() && getBookGenre().getValue().equals(BookGenre.ANY) &&
                getBookReleaseDate().getEditor().getText().isEmpty() && getBookIsbn().getText().isEmpty() &&
                getBookCopies().getValue().equals(1);
    }

    public AddBookPage cancel() {
        driver.clickOn(CANCEL_ADD);
        return this;
    }

    public AddBookPage reset() {
        driver.clickOn(RESET_ADD);
        return this;
    }

    public TextField getBookTitle() {
        return driver.find(ADD_TITLE);
    }

    public TextField getBookAuthor() {
        return driver.find(ADD_AUTHOR);
    }

    public TextField getBookPublisher() {
        return driver.find(ADD_PUBLISHER);
    }

    public ComboBox<BookGenre> getBookGenre() {
        return driver.find(ADD_GENRE);
    }

    public DatePicker getBookReleaseDate() {
        return driver.find(ADD_RELEASE_DATE);
    }

    public TextField getBookIsbn() {
        return driver.find(ADD_ISBN);
    }

    public Spinner<Integer> getBookCopies() {
        return  driver.find(ADD_COPIES);
    }

    public AddBookPage clearBookTitle() {
        getBookTitle().clear();
        return this;
    }

    public AddBookPage clearBookAuthor() {
        getBookAuthor().clear();
        return this;
    }

    public AddBookPage clearBookPublisher() {
        getBookPublisher().clear();
        return this;
    }

    public AddBookPage clearBookGenre() {
        Platform.runLater(() ->  getBookGenre().getSelectionModel().select(BookGenre.ANY));
        return this;
    }

    public AddBookPage clearBookReleaseDate() {
        getBookReleaseDate().getEditor().clear();
        return this;
    }

    public AddBookPage clearBookIsbn() {
        getBookIsbn().clear();
        return this;
    }

    public AddBookPage clearBookCopies() {
        getBookCopies().getValueFactory().setValue(1);
        return this;
    }

    public AddBookPage enterBookTitle(String title) {
        getBookTitle().setText(title);
        return this;
    }

    public AddBookPage enterBookAuthor(String author) {
        getBookAuthor().setText(author);
        return this;
    }

    public AddBookPage enterBookPublisher(String publisher) {
        getBookPublisher().setText(publisher);
        return this;
    }

    public AddBookPage enterBookGenre(BookGenre genre) {
        Platform.runLater(() -> getBookGenre().getSelectionModel().select(genre));
        return this;
    }

    public AddBookPage enterBookReleaseDate(String releaseDate) {
        getBookReleaseDate().getEditor().setText(releaseDate);
        return this;
    }

    public AddBookPage enterBookIsbn(String isbn) {
        getBookIsbn().setText(isbn);
        return this;
    }

    public AddBookPage enterBookCopies(int copies) {
        getBookCopies().getValueFactory().setValue(copies);
        return this;
    }

    public static void createAddBookExpectation(MockServerClient client) {
        try {
            client.reset();

            String content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<Book>()));

            client.when(
                    request()
                            .withMethod("POST")
                            .withPath("/item/book/add")
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );


            client.when(
                    request()
                            .withMethod("GET")
                            .withPath("/item/book/all")
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void verifyRequestSent(MockServerClient client) {
        client.verify(
                request().withMethod("POST")
                        .withPath("/item/book/add"),
                VerificationTimes.exactly(1)
        );
    }

}
