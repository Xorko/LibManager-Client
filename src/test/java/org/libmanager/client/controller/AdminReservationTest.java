package org.libmanager.client.controller;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.libmanager.client.AppOnAdmin;
import org.libmanager.client.TestBase;
import org.libmanager.client.model.Reservation;
import org.libmanager.client.pages.AdminBookPage;
import org.libmanager.client.pages.AdminReservationPage;
import org.libmanager.client.util.I18n;
import org.mockito.Mock;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.testfx.api.FxToolkit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AdminReservationTest extends TestBase {

    private AdminReservationPage adminReservationPage;

    @BeforeAll
    static void beforeAll(MockServerClient client) {
        AdminReservationPage.createReservationExpectation(client);
    }

    @Override
    @BeforeEach
    public void beforeEachTest() throws Exception {
        FxToolkit.setupApplication(AppOnAdmin.class);
        adminReservationPage = new AdminReservationPage(this);
        adminReservationPage.selectReservationTab();
    }

    @Test
    @DisplayName("When the delete button is pressed, an confirmation alert must be displayed")
    public void HandleDeleteReservation_WhenDeleteReservation_MustShowAlert() {
        String expectedTitle = "Confirmation";

        adminReservationPage.deleteReservation(1);

        assertThat(getAlertTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("When the reservation is deleted(item returned), the Reservation must no longer be in table")
    public void HandleDeleteReservation_WhenDeleteReservation_DeleteMustNoLongerBeInTable() {
        TableView<Reservation> reservationsTable = adminReservationPage.getReservationsTable();

        adminReservationPage.deleteReservation(1);
        clickOn(lookup(ButtonType.YES.getText()).queryButton());

        assertThat(reservationsTable.getItems().size()).isEqualTo(1);
        assertThat(reservationsTable.getItems().get(0).getId()).isEqualTo(2);
    }
}
