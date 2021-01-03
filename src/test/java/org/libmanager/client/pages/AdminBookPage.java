package org.libmanager.client.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.control.*;
import org.libmanager.client.TestBase;
import org.libmanager.client.TestData;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.Status;
import org.libmanager.client.model.Book;
import org.libmanager.server.response.Response;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.Times;
import org.mockserver.model.Parameter;


import java.time.LocalDate;
import java.util.ArrayList;


import static org.libmanager.client.JavaFXIds.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


public class AdminBookPage {

    private final TestBase driver;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public AdminBookPage(TestBase driver) {
        this.driver = driver;
    }

    public AdminBookPage searchBook(String title, String author, String publisher, BookGenre genre, LocalDate releaseDate, String isbn, Status status) {
        fillFields(title, author, publisher, genre, releaseDate, isbn, status);
        submit();
        return this;
    }

    public AdminBookPage submit() {
        driver.clickOn(BOOK_SEARCH_BUTTON);
        return this;
    }

    public AdminBookPage enterBookTitle(String title) {
        TextField titleField = driver.find(SEARCH_BOOK_TITLE);
        titleField.setText(title);
        return this;
    }

    public AdminBookPage enterBookAuthor(String author) {
        TextField authorField = driver.find(SEARCH_BOOK_AUTHOR);
        authorField.setText(author);
        return this;
    }

    public AdminBookPage enterBookPublisher(String publisher) {
        TextField publisherField = driver.find(SEARCH_BOOK_PUBLISHER);
        publisherField.setText(publisher);
        return this;
    }

    public AdminBookPage enterBookGenre(BookGenre genre) {
        ComboBox<BookGenre> genreComboBox = driver.find(SEARCH_BOOK_GENRE);
        Platform.runLater(() -> genreComboBox.getSelectionModel().select(genre));
        return this;
    }

    public AdminBookPage enterBookReleaseDate(LocalDate releaseDate) {
        DatePicker releaseDateDatePicker = driver.find(SEARCH_BOOK_RELEASE_DATE);
        releaseDateDatePicker.getEditor().setText(releaseDate.toString());
        return this;
    }

    public AdminBookPage enterBookIsbn(String isbn) {
        TextField isbnField = driver.find(SEARCH_BOOK_ISBN);
        isbnField.setText(isbn);
        return this;
    }

    public AdminBookPage enterBookStatus(Status status) {
        ComboBox<Status> statusComboBox = driver.find(SEARCH_BOOK_STATUS);
        Platform.runLater(() -> statusComboBox.getSelectionModel().select(status));
        return this;
    }

    public AdminBookPage clearOutInputFields() {
        clearBookTitle().clearBookAuthor().clearBookPublisher().clearBookGenre().clearBookReleaseDate().clearBookIsbn()
                .clearBookStatus();
        return this;
    }

    public AdminBookPage clearBookTitle() {
        TextField titleField = driver.find(SEARCH_BOOK_TITLE);
        titleField.clear();
        return this;
    }

    public AdminBookPage clearBookAuthor() {
        TextField authorField = driver.find(SEARCH_BOOK_AUTHOR);
        authorField.clear();
        return this;
    }

    public AdminBookPage clearBookPublisher() {
        TextField publisherField = driver.find(SEARCH_BOOK_PUBLISHER);
        publisherField.clear();
        return this;
    }

    public AdminBookPage clearBookGenre() {
        ComboBox<BookGenre> genreComboBox = driver.find(SEARCH_BOOK_GENRE);
        Platform.runLater(() ->  genreComboBox.getSelectionModel().select(BookGenre.ANY));
        return this;
    }

    public AdminBookPage clearBookReleaseDate() {
        DatePicker releaseDateDatePicker = driver.find(SEARCH_BOOK_RELEASE_DATE);
        releaseDateDatePicker.getEditor().clear();
        return this;
    }

    public AdminBookPage clearBookIsbn() {
        TextField isbnField = driver.find(SEARCH_BOOK_ISBN);
        isbnField.clear();
        return this;
    }

    public AdminBookPage clearBookStatus() {
        ComboBox<Status> statusComboBox = driver.find(SEARCH_BOOK_STATUS);
        Platform.runLater(() ->  statusComboBox.getSelectionModel().select(Status.ANY));
        return this;
    }

    public TableView<Book> getBookTable() {
        return driver.find(BOOKS_TABLE);
    }

    public boolean allFieldsClear() {
        return getBookTitle().getText().isEmpty() && getBookAuthor().getText().isEmpty() &&
                getBookPublisher().getText().isEmpty() && getBookGenre().getValue().equals(BookGenre.ANY) &&
                getBookReleaseDate().getEditor().getText().isEmpty() && getBookIsbn().getText().isEmpty() &&
                getBookStatus().getValue().equals(Status.ANY);
    }

    public TextField getBookTitle() {
        return driver.find(SEARCH_BOOK_TITLE);
    }

    public TextField getBookAuthor() {
        return driver.find(SEARCH_BOOK_AUTHOR);
    }

    public TextField getBookPublisher() {
        return driver.find(SEARCH_BOOK_PUBLISHER);
    }

    public ComboBox<BookGenre> getBookGenre() {
        return driver.find(SEARCH_BOOK_GENRE);
    }

    public DatePicker getBookReleaseDate() {
        return driver.find(SEARCH_BOOK_RELEASE_DATE);
    }

    public TextField getBookIsbn() {
        return driver.find(SEARCH_BOOK_ISBN);
    }

    public ComboBox<Status> getBookStatus() {
        return  driver.find(SEARCH_BOOK_STATUS);
    }

    public AdminBookPage fillFields(String title, String author, String publisher, BookGenre genre, LocalDate releaseDate, String isbn, Status status) {
        clearOutInputFields();
        enterBookTitle(title).enterBookAuthor(author).enterBookPublisher(publisher).enterBookGenre(genre).
                enterBookReleaseDate(releaseDate).enterBookIsbn(isbn).enterBookStatus(status);
        return this;
    }

    public AdminBookPage reset() {
        driver.clickOn(BOOK_RESET_BUTTON);
        return this;
    }

    public AdminBookPage addBook() {
        driver.clickOn(ADD_BOOK);
        return this;
    }

    public AdminBookPage editBook() {
        driver.clickOn(EDIT_BOOK);
        return this;
    }

    public AdminBookPage selectRow(int row) {
        getBookTable().getSelectionModel().select(row);
        return this;
    }

    public String getSelectedTitle() {
        return getBookTable().getSelectionModel().getSelectedItem().getTitle();
    }

    public AdminBookPage deleteBook() {
        driver.clickOn(DELETE_BOOK);
        return this;
    }

    public static void createBookExpectations(MockServerClient client) {
        try {
            client.reset();

            String content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<Book>()));

            client.when(
                    request()
                            .withMethod("GET")
                            .withPath("/item/book/search")
                            .withQueryStringParameters(Parameter.param("title", "No"),
                                    Parameter.param("author", "No"),
                                    Parameter.param("isbn", "NO"),
                                    Parameter.param("status", "1"))
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );

            content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<Book>() {{
                add(TestData.getBook());
            }}));

            client.when(
                    request()
                            .withMethod("GET")
                            .withPath("/item/book/search")
                            .withQueryStringParameters(Parameter.param("author", "author"),
                                    Parameter.param("isbn", "qwerty"),
                                    Parameter.param("genre", "BIOGRAPHY"),
                                    Parameter.param("publisher", "publisher"),
                                    Parameter.param("title", "title"),
                                    Parameter.param("status", "1")
                            )

            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );

            content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<Book>() {{
                add(TestData.getBook1());
            }}));

            client.when(
                    request()
                            .withMethod("GET")
                            .withPath("/item/book/search")
                            .withQueryStringParameters(Parameter.param("author", "author"),
                                    Parameter.param("isbn", "azerty"),
                                    Parameter.param("genre", "BIOGRAPHY"),
                                    Parameter.param("publisher", "publisher1"),
                                    Parameter.param("title", "title1"),
                                    Parameter.param("status", "1")
                            )
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );

            content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<Book>() {{
                add(TestData.getBook());
                add(TestData.getBook1());
            }}));

            client.when(
                    request()
                            .withMethod("GET")
                            .withPath("/item/book/search")
                            .withQueryStringParameters(Parameter.param("author", "author"),
                                    Parameter.param("status", "1"))
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );

            client.when(
                    request()
                            .withMethod("GET")
                            .withPath("/item/book/all"),
                    Times.exactly(1)
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );

            content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, true));

            client.when(
                    request()
                            .withMethod("POST")
                            .withPath("/item/delete/1")
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );


            content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<Book>() {{
                add(TestData.getBook1());
            }}));

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
}
