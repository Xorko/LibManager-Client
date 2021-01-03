package org.libmanager.client.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.libmanager.client.AppOnAdmin;
import org.libmanager.client.TestBase;
import org.libmanager.client.TestData;
import org.libmanager.client.enums.DVDGenre;
import org.libmanager.client.enums.Status;
import org.libmanager.client.model.DVD;
import org.libmanager.client.pages.AdminDvdPage;
import org.libmanager.client.pages.LoginPage;
import org.libmanager.client.util.I18n;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.WindowMatchers;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

public class AdminDvdTest extends TestBase {

    private AdminDvdPage adminDvdPage;

    @BeforeAll
    static void beforeAll(MockServerClient client) {
        AdminDvdPage.createDvdExpectations(client);
    }

    @Override
    @BeforeEach
    public void beforeEachTest() throws Exception {
        FxToolkit.setupApplication(AppOnAdmin.class);
        adminDvdPage = new AdminDvdPage(this);
        adminDvdPage.selectDvdTab();
    }

    @Test
    @DisplayName("At the initialization, all the dvds must be displayed in the dvd table view")
    @Tag("Initialization")
    public void Initialize_WhenInitializeView_AllDVDsMustBeInTableView() {
        TableView<DVD> dvdTable = adminDvdPage.getDvdTable();

        assertThat(dvdTable.getItems().get(0)).usingRecursiveComparison().isEqualTo(TestData.getDvd());
        assertThat(dvdTable.getItems().get(1)).usingRecursiveComparison().isEqualTo(TestData.getDvd1());
    }

    @DisplayName("When searching a Dvd, only this Dvd must be in the table")
    @ParameterizedTest()
    @MethodSource("dvdToSearch")
    @Tag("DvdSearch")
    public void HandleDvdSearch_WhenSearchingDvd_OnlyThisDvdMustBeInTheTable(DVD dvd) throws InterruptedException {
        TableView<DVD> dvdTable = adminDvdPage.getDvdTable();

        adminDvdPage.searchDvd(dvd.getTitle(), dvd.getAuthor(), (DVDGenre) dvd.getGenre(), dvd.getReleaseDate(),
                dvd.getStatus() ? Status.AVAILABLE : Status.UNAVAILABLE);

        ensureEventQueueComplete();
        Thread.sleep(200);
        assertThat(dvdTable.getItems().size()).isEqualTo(1);
        assertThat(dvdTable.getItems().get(0)).usingRecursiveComparison().ignoringFields("availableCopies").isEqualTo(dvd);
    }

    @Test
    @DisplayName("When searching both Dvds, the two Dvds must be in the table")
    @Tag("DvdSearch")
    public void HandleDvdSearch_WhenSearchingBothDvd_BothDvdMustBeInTheTable() throws InterruptedException {
        TableView<DVD> dvdTableView = adminDvdPage.getDvdTable();

        adminDvdPage.searchDvd("", "author", DVDGenre.ANY, LocalDate.EPOCH, Status.AVAILABLE);

        ensureEventQueueComplete();
        Thread.sleep(200);
        assertThat(dvdTableView.getItems().size()).isEqualTo(2);
        assertThat(dvdTableView.getItems().get(0))
                .usingRecursiveComparison()
                .ignoringFields("availableCopies")
                .isEqualTo(TestData.getDvd());
        assertThat(dvdTableView.getItems().get(1))
                .usingRecursiveComparison()
                .ignoringFields("availableCopies")
                .isEqualTo(TestData.getDvd1());
    }

    private static Stream<Arguments> dvdToSearch() {
        return Stream.of(
                Arguments.arguments(TestData.getDvd()),
                Arguments.arguments(TestData.getDvd1())
        );
    }

    @Test
    @DisplayName("When search has no result, the table must be empty")
    @Tag("DvdSearch")
    public void HandleDvdSearch_WhenNoSearchResult_tableMustBeEmpty() throws InterruptedException {
        TableView<DVD> dvdTableView = adminDvdPage.getDvdTable();

        adminDvdPage.searchDvd("", "", DVDGenre.ANY, LocalDate.EPOCH, Status.UNAVAILABLE);

        ensureEventQueueComplete();
        Thread.sleep(200);
        assertThat(dvdTableView.getItems().isEmpty()).isTrue();

    }

    @Test
    @DisplayName("When pressing reset button, all fields must be cleared")
    @Tag("Reset")
    public void HandleDvdReset_WhenResetPressed_MustClearAllFields() {
        adminDvdPage.fillFields("foo", "foo", DVDGenre.ANY, LocalDate.EPOCH, Status.UNAVAILABLE);

        adminDvdPage.reset();

        assertThat(adminDvdPage.allFieldsClear()).isTrue();
    }

    @Test
    @DisplayName("When pressing reset button, all Dvd must reappear in table")
    @Tag("Reset")
    public void HandleDvdReset_WhenResetPressed_AllDvdsMustBeInTable() throws InterruptedException {
        TableView<DVD> dvdTableView = adminDvdPage.getDvdTable();
        adminDvdPage.searchDvd("dvd1", "", DVDGenre.ANY, LocalDate.EPOCH, Status.AVAILABLE);
        Thread.sleep(200);

        adminDvdPage.reset();

        assertThat(dvdTableView.getItems().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("When the add Dvd button is pressed, the view to add Dvd must be displayed")
    @Tag("Add")
    public void HandleItemManagementButtons_WhenAddDvd_MustShowAddDvdView() {
        String expectedTitle = I18n.getBundle().getString("label.add.dvd");

        adminDvdPage.addDvd();

        assertThat(WindowMatchers.isShowing().matches(window(expectedTitle))).isTrue();
    }

    @Test
    @DisplayName("If no Dvd are selected when pressing edit, an alert must be displayed")
    @Tag("CantEdit")
    public void HandleItemManagementButtons_WhenNoDvdSelected_MustShowAlert() {
        String expectedAlert = I18n.getBundle().getString("alert.noselection.dvd.title");

        adminDvdPage.editDvd();

        assertThat(WindowMatchers.isShowing().matches(window(expectedAlert))).isTrue();
    }

    @ParameterizedTest(name = "Trying on row {0}")
    @ValueSource(ints = {0, 1})
    @DisplayName("If an Dvd is selected when pressing edit, the edit view must be displayed")
    @Tag("Edit")
    public void HandleItemManagementButtons_WhenDvdSelected_MustShowEditView(int arg1){
        adminDvdPage.selectRow(arg1);
        String expectedTitle = I18n.getBundle().getString("label.editing.title") + adminDvdPage.getSelectedTitle();

        adminDvdPage.editDvd();

        assertThat(WindowMatchers.isShowing().matches(window(expectedTitle))).isTrue();
    }

    @Test
    @DisplayName("If no Dvd are selected when pressing delete, an alert must be displayed")
    @Tag("NotDelete")
    public void HandleDeleteDvd_WhenNoDvdSelected_MustShowAlert() {
        String expectedAlert = I18n.getBundle().getString("alert.noselection.dvd.title");

        adminDvdPage.deleteDvd();

        assertThat(WindowMatchers.isShowing().matches(window((expectedAlert)))).isTrue();
    }

    @ParameterizedTest(name = "Trying on row {0}")
    @ValueSource(ints = {0, 1})
    @DisplayName("If a Dvd is selected when pressing delete, a confirmation alert must be displayed")
    @Tag("Delete")
    public void HandleDeleteDvd_WhenDvdSelected_MustShowConfirmationAlert(int arg1) {
        //GIVEN
        adminDvdPage.selectRow(arg1);
        String expectedAlert = I18n.getBundle().getString("alert.confirm.deletion.title");

        //WHEN
        adminDvdPage.deleteDvd();

        //THEN
        assertThat(getAlertTitle()).isEqualTo(expectedAlert);
    }

    @Test
    @DisplayName("When A Dvd is deleted, it must no longer be in the Table")
    @Tag("Delete")
    public void HandleDeleteDvd_WhenConfirmDelete_DvdMustNoLongerBeInTable() throws InterruptedException {
        adminDvdPage.selectRow(0);
        TableView<DVD> DvdsTable = adminDvdPage.getDvdTable();

        adminDvdPage.deleteDvd();
        clickOn(lookup(ButtonType.YES.getText()).queryButton());

        Thread.sleep(200);
        assertThat(DvdsTable.getItems().size()).isEqualTo(1);
        assertThat(DvdsTable.getItems().get(0).getId()).isEqualTo(2);
    }

    @Test
    public void HandleDeleteDvd_WhenDvdHasMultipleCopies_NumberOfCopiesMustDecrease() {
    }
}
