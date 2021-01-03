package org.libmanager.client.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.libmanager.client.AppOnAddBook;
import org.libmanager.client.AppOnReservation;
import org.libmanager.client.TestBase;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.pages.AddBookPage;
import org.libmanager.client.util.I18n;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.WindowMatchers;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AddBookTest extends TestBase {

    private AddBookPage addBookPage;

    @BeforeAll
    static void beforeAll(MockServerClient client) {
        AddBookPage.createAddBookExpectation(client);
    }

    @Override
    @BeforeEach
    public void beforeEachTest() throws Exception {
        FxToolkit.setupApplication(AppOnAddBook.class);
        addBookPage = new AddBookPage(this);
    }

    @Test
    @DisplayName("If a field is empty,  the book is not add and an error must be displayed")
    public void HandleAddBook_WhenEmptyFields_MustShowError() {
        String expectedAlert = I18n.getBundle().getString("label.invalidfields");

        addBookPage.confirm();

        assertThat(WindowMatchers.isShowing().matches(window(expectedAlert))).isTrue();
    }

    @Test
    @DisplayName("If the release date field has incorrect date format, an error must be displayed")
    public void HandleAddBook_WhenInvalidReleaseDate_MustShowError() {
        String expectedAlert = I18n.getBundle().getString("label.invalidfields");

        addBookPage.addBook("title", "author", "publisher", BookGenre.BIOGRAPHY, "Invalid", "AZER", 8);

        assertThat(WindowMatchers.isShowing().matches(window(expectedAlert))).isTrue();
    }

    @Test
    @DisplayName("When pressing reset, all fields must be cleared")
    public void HandleReset_WhenReset_MustClearAllFields() {
        addBookPage.fillFields("title", "author", "publisher", BookGenre.BIOGRAPHY, "12/12/2012", "AZER", 8);

        addBookPage.reset();

        assertThat(addBookPage.allFieldsClear()).isTrue();
    }

    @Test
    @DisplayName("When pressing cancel, the add book window must close")
    public void HandleCancel_WhenCancel_WindowMustClose() {
        addBookPage.cancel();

        assertThrows(NoSuchElementException.class, () -> WindowMatchers.isShowing().matches(window(I18n.getBundle().getString("label.add.book"))));
    }

    @Test
    @DisplayName("When a book is added, a request must be sent to the server")
    public void HandleAddBook_WhenAddBook_MustSendRequestToServer(MockServerClient client) {
        addBookPage.addBook("title", "author", "publisher", BookGenre.BIOGRAPHY, "12/12/2012", "AZER", 8);
        addBookPage.verifyRequestSent(client);
    }

    @Test
    @DisplayName("When a book is added, the add book window must close")
    public void handleAddBook_WhenAddBook_WindowMustClose() throws InterruptedException {
        addBookPage.addBook("title", "author", "publisher", BookGenre.BIOGRAPHY, "12/12/2012", "AZER", 8);
        assertThrows(NoSuchElementException.class, () -> WindowMatchers.isShowing().matches(window(I18n.getBundle().getString("label.add.book"))));
    }
}
