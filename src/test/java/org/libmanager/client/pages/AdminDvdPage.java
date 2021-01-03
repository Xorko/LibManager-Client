package org.libmanager.client.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.control.*;
import org.libmanager.client.TestBase;
import org.libmanager.client.TestData;
import org.libmanager.client.enums.DVDGenre;
import org.libmanager.client.enums.Status;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.server.response.Response;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.Times;
import org.mockserver.model.Parameter;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.libmanager.client.JavaFXIds.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class AdminDvdPage {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final TestBase driver;

    public AdminDvdPage(TestBase driver) {
        this.driver = driver;
    }

    public AdminDvdPage selectDvdTab() {
        TabPane itemsTabPane = driver.find(ITEMS_TAB_PANE);
        itemsTabPane.getSelectionModel().select(1);
        return this;
    }

    public AdminDvdPage searchDvd(String title, String director, DVDGenre genre, LocalDate releaseDate, Status status) {
        fillFields(title, director, genre, releaseDate, status);
        submit();
        return this;
    }

    public AdminDvdPage fillFields(String title, String director, DVDGenre genre, LocalDate releaseDate, Status status) {
        clearOutInputFields();
        enterDvdTitle(title).enterDvdDirector(director).enterDvdGenre(genre).enterDvdReleaseDate(releaseDate).
                enterDvdStatus(status);
        return this;
    }

    public AdminDvdPage clearOutInputFields() {
        clearDvdTitle().clearDvdDirector().clearDvdGenre().clearDvdReleaseDate().clearDvdStatus();
        return this;
    }

    public AdminDvdPage submit() {
        driver.clickOn(DVD_SEARCH_BUTTON);
        return this;
    }

    public boolean allFieldsClear() {
        return getDvdTitle().getText().isEmpty() && getDvdDirector().getText().isEmpty()
                && getDvdGenre().getValue().equals(DVDGenre.ANY) && getBookReleaseDate().getEditor().getText().isEmpty()
                && getDvdStatus().getValue().equals(Status.ANY);
    }

    public TextField getDvdTitle() {return driver.find(SEARCH_DVD_TITLE);}

    public TextField getDvdDirector() {return driver.find(SEARCH_DVD_DIRECTOR);}

    public ComboBox<DVDGenre> getDvdGenre() {return driver.find(SEARCH_DVD_GENRE);}

    public DatePicker getBookReleaseDate() {return driver.find(SEARCH_DVD_RELEASE_DATE);}

    public ComboBox<Status> getDvdStatus() {return driver.find(SEARCH_DVD_STATUS);}

    public TableView<DVD> getDvdTable() {return driver.find(DVD_TABLE);}

    public AdminDvdPage clearDvdTitle() {
        getDvdTitle().clear();
        return this;
    }

    public AdminDvdPage clearDvdDirector() {
        getDvdDirector().clear();
        return this;
    }

    public AdminDvdPage clearDvdGenre() {
        Platform.runLater(() -> getDvdGenre().getSelectionModel().select(DVDGenre.ANY));
        return this;
    }

    public AdminDvdPage clearDvdReleaseDate() {
        getBookReleaseDate().getEditor().clear();
        return this;
    }

    public AdminDvdPage clearDvdStatus() {
        Platform.runLater(() -> getDvdStatus().getSelectionModel().select(Status.ANY));
        return this;
    }

    public AdminDvdPage enterDvdTitle(String title) {
        getDvdTitle().setText(title);
        return this;
    }

    public AdminDvdPage enterDvdDirector(String director) {
        getDvdDirector().setText(director);
        return this;
    }

    public AdminDvdPage enterDvdGenre(DVDGenre genre) {
        Platform.runLater(() -> getDvdGenre().getSelectionModel().select(genre));
        return this;
    }

    public AdminDvdPage enterDvdReleaseDate(LocalDate releaseDate) {
        getBookReleaseDate().getEditor().setText(releaseDate.toString());
        return this;
    }

    public AdminDvdPage enterDvdStatus(Status status) {
        Platform.runLater(() -> getDvdStatus().getSelectionModel().select(status));
        return this;
    }

    public AdminDvdPage reset() {
        driver.clickOn(DVD_RESET_BUTTON);
        return this;
    }

    public AdminDvdPage addDvd() {
        driver.clickOn(ADD_DVD);
        return this;
    }

    public AdminDvdPage deleteDvd() {
        driver.clickOn(DELETE_DVD);
        return this;
    }

    public AdminDvdPage editDvd() {
        driver.clickOn(EDIT_DVD);
        return this;
    }

    public AdminDvdPage selectRow(int row) {
        getDvdTable().getSelectionModel().select(row);
        return this;
    }

    public String getSelectedTitle() {return getDvdTable().getSelectionModel().getSelectedItem().getTitle();}

    public static void createDvdExpectations(MockServerClient client) {
        try {

            client.reset();

            String content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<Book>()));

            client.when(
                    request()
                            .withMethod("GET")
                            .withPath("/item/dvd/search")
                            .withQueryStringParameters(Parameter.param("status", "0")
                            )
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );

            content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<DVD>(){{
                add(TestData.getDvd());
            }}));

            client.when(
                    request()
                            .withMethod("GET")
                            .withPath("/item/dvd/search")
                            .withQueryStringParameters(Parameter.param("genre", "ADVENTURE"),
                                    Parameter.param("title", "dvd"),
                                    Parameter.param("director", "author"),
                                    Parameter.param("status", "1")
                            )
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );

            content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<DVD>(){{
                add(TestData.getDvd1());
            }}));

            client.when(
                    request()
                            .withMethod("GET")
                            .withPath("/item/dvd/search")
                            .withQueryStringParameters(Parameter.param("genre", "CRIMINAL"),
                                    Parameter.param("title", "dvd1"),
                                    Parameter.param("director", "author"),
                                    Parameter.param("status", "1")
                            )
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );

            content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<DVD>(){{
                add(TestData.getDvd());
                add(TestData.getDvd1());
            }}));

            client.when(
                    request()
                            .withMethod("GET")
                            .withPath("/item/dvd/search")
                            .withQueryStringParameters(Parameter.param("genre", "CRIMINAL"),
                                    Parameter.param("title", "dvd1"),
                                    Parameter.param("director", "author"),
                                    Parameter.param("status", "1")
                            )
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


            content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<DVD>() {{
                add(TestData.getDvd1());
            }}));

            client.when(
                    request()
                            .withMethod("GET")
                            .withPath("/item/dvd/all"),
                    Times.exactly(1)
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );

            content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<DVD>(){{
                add(TestData.getDvd());
                add(TestData.getDvd1());
            }}));

            client.when(
                    request()
                            .withMethod("GET")
                            .withPath("/item/dvd/all")
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
