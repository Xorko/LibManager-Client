package org.libmanager.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.libmanager.client.AppOnAdmin;
import org.libmanager.client.TestBase;
import org.libmanager.client.TestData;
import org.libmanager.client.model.User;
import org.libmanager.client.model.User;
import org.libmanager.client.pages.AdminDvdPage;
import org.libmanager.client.pages.AdminUserPage;
import org.libmanager.client.util.I18n;
import org.libmanager.server.response.Response;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.WindowMatchers;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class AdminUserTest extends TestBase {

    private AdminUserPage adminUserPage;

    @BeforeAll
    static void beforeAll(MockServerClient client) {
        AdminUserPage.createUserExpectation(client);
    }

    @Override
    @BeforeEach
    public void beforeEachTest() throws Exception {
        FxToolkit.setupApplication(AppOnAdmin.class);
        adminUserPage = new AdminUserPage(this);
        adminUserPage.selectUserTab();
    }

    @Test
    @DisplayName("At the initialization, all the Users must be displayed in the User table view")
    @Tag("Initialization")
    public void Initialize_WhenInitializeView_AllUsersMustBeInTableView() {
        TableView<User> usersTable = adminUserPage.getUserTable();

        assertThat(usersTable.getItems().get(0)).usingRecursiveComparison().isEqualTo(TestData.getUser());
        assertThat(usersTable.getItems().get(1)).usingRecursiveComparison().isEqualTo(TestData.getUser1());
    }

    @Test
    @DisplayName("When the add User button is pressed, the view to add User must be displayed")
    @Tag("Add")
    public void HandleItemManagementButtons_WhenAddUser_MustShowAddUserView() {
        String expectedTitle = I18n.getBundle().getString("label.add.user");

        adminUserPage.addUser();

        Assertions.assertThat(WindowMatchers.isShowing().matches(window(expectedTitle))).isTrue();
    }

    @Test
    @DisplayName("If no User are selected when pressing edit, an alert must be displayed")
    @Tag("CantEdit")
    public void HandleItemManagementButtons_WhenNoUserSelected_MustShowAlert() {
        String expectedAlert = I18n.getBundle().getString("alert.noselection.user.title");

        adminUserPage.editUser();

        Assertions.assertThat(WindowMatchers.isShowing().matches(window(expectedAlert))).isTrue();
    }

    @ParameterizedTest(name = "Trying on row {0}")
    @ValueSource(ints = {0, 1})
    @DisplayName("If an User is selected when pressing edit, the edit view must be displayed")
    @Tag("Edit")
    public void HandleItemManagementButtons_WhenUserSelected_MustShowEditView(int arg1){
        adminUserPage.selectRow(arg1);
        String expectedTitle = I18n.getBundle().getString("label.editing.title") + adminUserPage.getSelectedUsername();

        adminUserPage.editUser();

        Assertions.assertThat(WindowMatchers.isShowing().matches(window(expectedTitle))).isTrue();
    }

    @Test
    @DisplayName("If no User are selected when pressing delete, an alert must be displayed")
    @Tag("NotDelete")
    public void HandleDeleteUser_WhenNoUserSelected_MustShowAlert() {
        String expectedAlert = I18n.getBundle().getString("alert.noselection.user.title");

        adminUserPage.deleteUser();

        Assertions.assertThat(WindowMatchers.isShowing().matches(window((expectedAlert)))).isTrue();
    }

    @ParameterizedTest(name = "Trying on row {0}")
    @ValueSource(ints = {0, 1})
    @DisplayName("If a User is selected when pressing delete, a confirmation alert must be displayed")
    @Tag("Delete")
    public void HandleDeleteUser_WhenUserSelected_MustShowConfirmationAlert(int arg1) {
        //GIVEN
        adminUserPage.selectRow(arg1);
        String expectedAlert = I18n.getBundle().getString("alert.confirm.deletion.title");

        //WHEN
        adminUserPage.deleteUser();

        //THEN
        Assertions.assertThat(getAlertTitle()).isEqualTo(expectedAlert);
    }

    @Test
    @DisplayName("When A User is deleted, it must no longer be in the Table")
    @Tag("Delete")
    public void HandleDeleteUser_WhenConfirmDelete_UserMustNoLongerBeInTable() throws InterruptedException {
        adminUserPage.selectRow(1);
        TableView<User> UsersTable = adminUserPage.getUserTable();

        adminUserPage.deleteUser();
        clickOn(lookup(ButtonType.YES.getText()).queryButton());

        Thread.sleep(200);
        Assertions.assertThat(UsersTable.getItems().size()).isEqualTo(1);
        Assertions.assertThat(UsersTable.getItems().get(0).getUsername()).isEqualTo("username");
    }

}
