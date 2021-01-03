package org.libmanager.client.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.libmanager.client.TestBase;
import org.libmanager.client.TestData;
import org.libmanager.client.model.Reservation;
import org.libmanager.client.model.User;
import org.libmanager.server.response.Response;
import org.mockserver.client.MockServerClient;

import java.util.ArrayList;

import static org.libmanager.client.JavaFXIds.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class AdminReservationPage {

    private final TestBase driver;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public AdminReservationPage(TestBase driver) {
        this.driver = driver;
    }

    public AdminReservationPage selectReservationTab() {
        TabPane tabPane = driver.find(ADMIN_TAB_PANE);
        tabPane.getSelectionModel().select(2);
        return this;
    }

    public TableView<Reservation> getReservationsTable() {return driver.find(RESERVATION_TABLE);}

    public Button getDeleteReservationButton(int row) {
        return driver.lookup(DELETE_RESERVATION_COLUMN).nth(row).lookup(".button").queryButton();
    }

    public AdminReservationPage deleteReservation(int row) {
        driver.clickOn(getDeleteReservationButton(row));
        return this;
    }

    public static void createReservationExpectation(MockServerClient client) {
        try {
            client.reset();

            String content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, true));

            client.when(
                    request()
                            .withMethod("POST")
                            .withPath("/reservation/delete/1")
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );


            content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<Reservation>() {{
                add(TestData.getReservation1());
            }}));

            client.when(
                    request()
                            .withMethod("POST")
                            .withPath("/reservation/all")
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
