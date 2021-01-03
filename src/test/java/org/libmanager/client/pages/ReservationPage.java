package org.libmanager.client.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import org.libmanager.client.TestBase;
import org.libmanager.client.TestData;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.client.model.Reservation;
import org.libmanager.server.response.Response;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.Times;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.libmanager.client.JavaFXIds.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class ReservationPage {

    private final TestBase driver;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ReservationPage(TestBase driver) {
        this.driver = driver;
    }

    public TableView<Reservation> getReservationBooksTable() {return driver.find(RESERVATION_BOOKS_TABLE);}

    public Button getBookStatusButton(int row) {
        return driver.lookup(RESERVE_BOOK_COLUMN).nth(row).lookup(".button").queryButton();
    }

    public ReservationPage reserveBook(int row) {
        driver.clickOn(getBookStatusButton(row));
        return this;
    }

    public ReservationPage confirm() {
        driver.clickOn(driver.lookup(ButtonType.YES.getText()).queryButton());
        return this;
    }

    public static void createExpectations(MockServerClient client) {
        try {
            client.reset();

            String content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, true));

            client.when(
                    request()
                            .withMethod("POST")
                            .withPath("/reservation/add/1")
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

    public void getAllUnavailable(MockServerClient client){
        try {
            client.reset();
            String content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<Book>(){{
                add(new Book(1, "book", "author", BookGenre.BIOGRAPHY.toString(), LocalDate.EPOCH, 0, 2, "publisher", "qwerty"));
                add(TestData.getBook1());
            }}));

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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void expectationNotOnlyCopy(MockServerClient client){
        try {
            client.reset();
            String content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<Book>(){{
                add(TestData.getBook());
                add(TestData.getBook1());
            }}));

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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
