package org.libmanager.client.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import org.libmanager.client.TestBase;
import org.libmanager.client.TestData;
import org.libmanager.client.model.User;
import org.libmanager.server.response.Response;
import org.mockserver.client.MockServerClient;

import java.util.ArrayList;

import static org.libmanager.client.JavaFXIds.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class AdminUserPage {

    private final TestBase driver;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public AdminUserPage(TestBase driver) {
        this.driver = driver;
    }

    public TableView<User> getUserTable() {return driver.find(USERS_TABLE);}

    public AdminUserPage addUser() {
        driver.clickOn(ADD_USER);
        return this;
    }

    public AdminUserPage deleteUser() {
        driver.clickOn(DELETE_USER);
        return this;
    }

    public AdminUserPage editUser() {
        driver.clickOn(EDIT_USER);
        return this;
    }

    public AdminUserPage selectRow(int row) {
        getUserTable().getSelectionModel().select(row);
        return this;
    }

    public String getSelectedUsername() {
        return getUserTable().getSelectionModel().getSelectedItem().getUsername();
    }

    public AdminUserPage selectUserTab() {
        TabPane tabPane = driver.find(ADMIN_TAB_PANE);
        tabPane.getSelectionModel().select(1);
        return this;
    }

    public static void createUserExpectation(MockServerClient client) {
        try {

            client.reset();

            String content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, true));

            client.when(
                    request()
                            .withMethod("POST")
                            .withPath("/user/delete/username1")
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(content)
                    );


            content = objectMapper.writeValueAsString(new Response<>(Response.Code.OK, new ArrayList<User>() {{
                add(TestData.getUser());
            }}));

            client.when(
                    request()
                            .withMethod("POST")
                            .withPath("/user/all")
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
