package org.libmanager.client.controller;

import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.libmanager.client.AppOnAdmin;
import org.libmanager.client.TestBase;
import org.libmanager.client.TestData;
import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.Status;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.Book;
import org.libmanager.client.pages.AdminBookPage;
import org.libmanager.client.pages.AdminUserPage;
import org.libmanager.client.util.I18n;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.WindowMatchers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminBookTest extends TestBase {

    private AdminBookPage adminBookPage;

    @BeforeAll
    static void beforeAll(MockServerClient client) {
        AdminBookPage.createBookExpectations(client);
    }

    @Override
    @BeforeEach
    public void beforeEachTest() throws Exception {
        FxToolkit.setupApplication(AppOnAdmin.class);
        adminBookPage = new AdminBookPage(this);
    }

    @Test
    @DisplayName("At the initialization, all the books must be displayed in the book table view")
    @Tag("Initialization")
    public void Initialize_WhenInitializeView_AllBooksMustBeInTableView() {
        TableView<Book> booksTable = adminBookPage.getBookTable();

        assertThat(booksTable.getItems().get(0)).usingRecursiveComparison().isEqualTo(TestData.getBook());
        assertThat(booksTable.getItems().get(1)).usingRecursiveComparison().isEqualTo(TestData.getBook1());
    }

    @Test
    @DisplayName("When searching the first book, only this book must be in the table")
    @Tag("BookSearch")
    public void HandleBookSearch_WhenSearchingFirstBook_OnlyFirstBookMustBeInTheTable() throws InterruptedException {
        TableView<Book> booksTable = adminBookPage.getBookTable();

        adminBookPage.searchBook("title", "author", "publisher", BookGenre.BIOGRAPHY, LocalDate.EPOCH, "qwerty", Status.AVAILABLE);

        ensureEventQueueComplete();
        Thread.sleep(200);
        assertThat(booksTable.getItems().size()).isEqualTo(1);
        assertThat(booksTable.getItems().get(0)).
                usingRecursiveComparison()
                .ignoringFields("availableCopies")
                .isEqualTo(TestData.getBook());
    }

    @Test
    @DisplayName("When searching the second book, only this book must be in the table")
    @Tag("BookSearch")
    public void HandleBookSearch_WhenSearchingSecondBook_OnlySecondBookMustBeInTheTable() throws InterruptedException {
        TableView<Book> booksTable = adminBookPage.getBookTable();

        adminBookPage.searchBook("title1", "author", "publisher1", BookGenre.BIOGRAPHY, LocalDate.EPOCH, "azerty", Status.AVAILABLE);

        ensureEventQueueComplete();
        Thread.sleep(200);
        assertThat(booksTable.getItems().size()).isEqualTo(1);
        assertThat(booksTable.getItems().get(0)).
                usingRecursiveComparison()
                .ignoringFields("availableCopies")
                .isEqualTo(TestData.getBook1());
    }

    @Test
    @DisplayName("When searching both books, the two books must be in the table")
    @Tag("BookSearch")
    public void HandleBookSearch_WhenSearchingBothBook_BothBookMustBeInTheTable() throws InterruptedException {
        TableView<Book> booksTable = adminBookPage.getBookTable();

        adminBookPage.searchBook("", "author", "", BookGenre.ANY, LocalDate.EPOCH, "", Status.AVAILABLE);

        ensureEventQueueComplete();
        Thread.sleep(200);
        assertThat(booksTable.getItems().size()).isEqualTo(2);
        assertThat(booksTable.getItems().get(0)).
                usingRecursiveComparison()
                .ignoringFields("availableCopies")
                .isEqualTo(TestData.getBook());
        assertThat(booksTable.getItems().get(1)).
                usingRecursiveComparison()
                .ignoringFields("availableCopies")
                .isEqualTo(TestData.getBook1());
    }

    @Test
    @DisplayName("When search has no result, the table must be empty")
    @Tag("BookSearch")
    public void HandleBookSearch_WhenNoSearchResult_tableMustBeEmpty() throws InterruptedException {
        TableView<Book> booksTable = adminBookPage.getBookTable();

        adminBookPage.searchBook("No", "No", "", BookGenre.ANY, LocalDate.EPOCH, "No", Status.AVAILABLE);

        ensureEventQueueComplete();
        Thread.sleep(200);
        assertThat(booksTable.getItems().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("When pressing reset button, all fields must be cleared")
    @Tag("Reset")
    public void HandleBookReset_WhenResetPressed_MustClearAllFields() {
        adminBookPage.fillFields("foo", "foo", "foo", BookGenre.BIOGRAPHY, LocalDate.EPOCH, "foo", Status.UNAVAILABLE);

        adminBookPage.reset();

        assertThat(adminBookPage.allFieldsClear()).isTrue();
    }

    @Test
    @DisplayName("When pressing reset button, all book must reappear in table")
    @Tag("Reset")
    public void HandleBookReset_WhenResetPressed_AllBooksMustBeInTable() throws InterruptedException {
        TableView<Book> booksTable = adminBookPage.getBookTable();
        adminBookPage.searchBook("title1", "author", "publisher1", BookGenre.BIOGRAPHY, LocalDate.EPOCH, "azerty", Status.AVAILABLE);
        Thread.sleep(200);

        adminBookPage.reset();

        assertThat(booksTable.getItems().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("When the add Book button is pressed, the view to add Book must be displayed")
    @Tag("Add")
    public void HandleItemManagementButtons_WhenAddBook_MustShowAddBookView() {
        String expectedTitle = I18n.getBundle().getString("label.add.book");

        adminBookPage.addBook();

        assertThat(WindowMatchers.isShowing().matches(window(expectedTitle))).isTrue();
    }

    @Test
    @DisplayName("If no Book are selected when pressing edit, an alert must be displayed")
    @Tag("CantEdit")
    public void HandleItemManagementButtons_WhenNoBookSelected_MustShowAlert() {
        String expectedAlert = I18n.getBundle().getString("alert.noselection.book.title");

        adminBookPage.editBook();

        assertThat(WindowMatchers.isShowing().matches(window(expectedAlert))).isTrue();
    }

    @ParameterizedTest(name = "Trying on row {0}")
    @ValueSource(ints = {0, 1})
    @DisplayName("If an Book is selected when pressing edit, the edit view must be displayed")
    @Tag("Edit")
    public void HandleItemManagementButtons_WhenBookSelected_MustShowEditView(int arg1){
        adminBookPage.selectRow(arg1);
        String expectedTitle = I18n.getBundle().getString("label.editing.title") + adminBookPage.getSelectedTitle();

        adminBookPage.editBook();

        assertThat(WindowMatchers.isShowing().matches(window(expectedTitle))).isTrue();
    }

    @Test
    @DisplayName("If no Book are selected when pressing delete, an alert must be displayed")
    @Tag("NotDelete")
    public void HandleDeleteBook_WhenNoBookSelected_MustShowAlert() {
        String expectedAlert = I18n.getBundle().getString("alert.confirm.deletion.title");

        adminBookPage.deleteBook();

        assertThat(WindowMatchers.isShowing().matches(window((expectedAlert)))).isTrue();
    }

    @ParameterizedTest(name = "Trying on row {0}")
    @ValueSource(ints = {0, 1})
    @DisplayName("If a Book is selected when pressing delete, a confirmation alert must be displayed")
    @Tag("Delete")
    public void HandleDeleteBook_WhenBookSelected_MustShowConfirmationAlert(int arg1) {
        //GIVEN
        adminBookPage.selectRow(arg1);
        String expectedAlert = I18n.getBundle().getString("alert.confirm.deletion.title");

        //WHEN
        adminBookPage.deleteBook();

        //THEN
        assertThat(getAlertTitle()).isEqualTo(expectedAlert);
    }

    @Test
    @DisplayName("When A Book is deleted, it must no longer be in the Table")
    @Tag("Delete")
    public void HandleDeleteBook_WhenConfirmDelete_BookMustNoLongerBeInTable() throws InterruptedException {
        adminBookPage.selectRow(0);
        TableView<Book> booksTable = adminBookPage.getBookTable();

        adminBookPage.deleteBook();
        clickOn(lookup(ButtonType.YES.getText()).queryButton());

        Thread.sleep(200);
        assertThat(booksTable.getItems().size()).isEqualTo(1);
        assertThat(booksTable.getItems().get(0).getId()).isEqualTo(2);
    }

    @Test
    public void HandleDeleteBook_WhenBookHasMultipleCopies_NumberOfCopiesMustDecrease() {
    }

}

