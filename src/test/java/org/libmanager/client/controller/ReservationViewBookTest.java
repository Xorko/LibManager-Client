package org.libmanager.client.controller;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import org.junit.jupiter.api.*;
import org.libmanager.client.AppOnReservation;
import org.libmanager.client.TestBase;
import org.libmanager.client.pages.ReservationPage;
import org.libmanager.client.util.I18n;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.WindowMatchers;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationViewBookTest extends TestBase {

    private ReservationPage reservationPage;

    @BeforeAll
    static void beforeAll(MockServerClient client) {
        ReservationPage.createExpectations(client);
    }

    @Override
    @BeforeEach
    public void beforeEachTest() throws Exception {
        FxToolkit.setupApplication(AppOnReservation.class);
        reservationPage = new ReservationPage(this);
    }

    @Test
    @DisplayName("When a book is unavailable, the button to reserve must be disabled")
    @Tag("CannotReserve")
    public void HandleReserve_WhenBookNotAvailable_ButtonMustBeDisable() {
        Button status = reservationPage.getBookStatusButton(2);
        assertThat(status.isDisabled()).isTrue();
    }

    @Test
    @DisplayName("When the book is available, the button to reserve must be available")
    @Tag("Reserve")
    public void HandleReserve_WhenBookAvailable_ButtonMustBeDisabled() {
        Button status = reservationPage.getBookStatusButton(1);

        assertThat(status.isDisabled()).isFalse();
    }

    @Test
    @DisplayName("When a book is reserved, a confirmation alert must be displayed")
    public void HandleReserve_WhenButtonReservePressed_AlertMustBeShown() {
        String expectedAlert = I18n.getBundle().getString("alert.confirmation.borrowing.title");

        reservationPage.reserveBook(1);

        assertThat(WindowMatchers.isShowing().matches(window(expectedAlert))).isTrue();
    }

    @Test
    @DisplayName("When a book is reserved and its the only copy, the book must no longer be available")
    public void handleReserve_WhenReserveConfirmedAndOnlyOnCopy_ReserveButtonMustBeDisabled(MockServerClient client){
        Button status = reservationPage.getBookStatusButton(2);

        reservationPage.reserveBook(1);
        reservationPage.getAllUnavailable(client);
        clickOn(lookup(ButtonType.YES.getText()).queryButton());

        assertThat(status.isDisabled()).isTrue();
    }

    @Test
    @DisplayName("When a book is reserved and its not the only copy, the book must still longer be available")
    public void handleReserve_WhenReserveConfirmedAndNotOnlyOnCopy_ReserveButtonMustBeEnabled(MockServerClient client) {
        Button status = reservationPage.getBookStatusButton(1);

        reservationPage.reserveBook(1);
        reservationPage.expectationNotOnlyCopy(client);
        reservationPage.confirm();

        assertThat(status.isDisabled()).isFalse();
    }

}
