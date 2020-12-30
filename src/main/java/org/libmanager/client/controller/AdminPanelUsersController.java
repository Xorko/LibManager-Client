package org.libmanager.client.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.libmanager.client.App;
import org.libmanager.client.model.User;
import org.libmanager.client.service.Requests;
import org.libmanager.client.util.DateUtil;
import org.libmanager.client.util.I18n;
import org.libmanager.client.util.ResponseUtil;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminPanelUsersController implements Initializable {

    // Users
    @FXML
    private TextField userUsernameField;
    @FXML
    private TextField userFirstNameField;
    @FXML
    private TextField userLastNameField;
    @FXML
    private TextField userAddressField;
    @FXML
    private DatePicker userBirthdayDPicker;
    @FXML
    private DatePicker userRegistrationDateDPicker;
    @FXML
    private TextField userEmailAddressField;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> userUsernameColumn;
    @FXML
    private TableColumn<User, String> userFirstNameColumn;
    @FXML
    private TableColumn<User, String> userLastNameColumn;
    @FXML
    private TableColumn<User, String> userAddressColumn;
    @FXML
    private TableColumn<User, String> userBirthdayColumn;
    @FXML
    private TableColumn<User, String> userEmailAddressColumn;
    @FXML
    private TableColumn<User, String> userRegistrationDateColumn;
    @FXML
    private Button userAddButton;
    @FXML
    private Button userEditButton;

    private App app;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // --- USERS TABLE ---
        userUsernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        userFirstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        userLastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        userAddressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        userBirthdayColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getBirthday())));
        userEmailAddressColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        userRegistrationDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtil.format(cellData.getValue().getRegistrationDate())));

        // --- Users table columns dimensions ---
        userUsernameColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.1));
        userFirstNameColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        userLastNameColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        userAddressColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        userEmailAddressColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        userBirthdayColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        userRegistrationDateColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));

        userBirthdayDPicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return DateUtil.format(date);
            }

            @Override
            public LocalDate fromString(String string) {
                return DateUtil.parse(string);
            }
        });

        userRegistrationDateDPicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return DateUtil.format(date);
            }

            @Override
            public LocalDate fromString(String string) {
                return DateUtil.parse(string);
            }
        });
    }

    /**
     * Handle add and edit buttons for users
     * @param e The event
     */
    @FXML
    private void handleUserManagementButtons(ActionEvent e) {
        showEditUserView(e);
    }

    @FXML
    private void handleDeleteUser() {
        User selected = getSelectedUser();
        if (selected != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            confirmationAlert.initOwner(app.getPrimaryStage());
            confirmationAlert.setTitle(I18n.getBundle().getString("alert.confirm.deletion.title"));
            confirmationAlert.setHeaderText(I18n.getBundle().getString("alert.confirm.deletion.user"));
            confirmationAlert.setContentText(I18n.getBundle().getString("alert.confirm.deletion.content"));
            Optional<ButtonType> answer = confirmationAlert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                String response = Requests.callDeleteUser(app.getLoggedInUser().getToken(), selected.getUsername());
                if (ResponseUtil.analyze(response, app.getPrimaryStage()))
                    app.loadAllUsers();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle(I18n.getBundle().getString("alert.noselection.user.title"));
            alert.setHeaderText(I18n.getBundle().getString("alert.noselection.user.title"));
            alert.setContentText(I18n.getBundle().getString("alert.noselection.user.delete.content"));
            alert.showAndWait();
        }
    }

    /**
     * Search users
     */
    @FXML
    private void handleUserSearch() {
        Task<Void> searchUsers = new Task<>() {
            @Override
            protected Void call() {
                String response = Requests.callSearchUser(
                        app.getLoggedInUser().getToken(),
                        userUsernameField.getText().length() == 0 ? null : userUsernameField.getText(),
                        userEmailAddressField.getText().length() == 0 ? null : userEmailAddressField.getText(),
                        userFirstNameField.getText().length() == 0 ? null : userFirstNameField.getText(),
                        userLastNameField.getText().length() == 0 ? null : userLastNameField.getText(),
                        userAddressField.getText().length() == 0 ? null : userAddressField.getText(),
                        !DateUtil.validDate(userBirthdayDPicker.getEditor().getText()) ? null : DateUtil.formatDB(DateUtil.parse(userBirthdayDPicker.getEditor().getText())),
                        !DateUtil.validDate(userRegistrationDateDPicker.getEditor().getText()) ? null : DateUtil.formatDB(DateUtil.parse(userRegistrationDateDPicker.getEditor().getText()))
                );
                app.loadUsersFromJSON(response);
                return null;
            }
        };
        new Thread(searchUsers).start();
    }

    /**
     * Reset the user list and clear all search fields
     */
    @FXML
    private void handleResetUser() {
        userUsernameField.setText("");
        userFirstNameField.setText("");
        userLastNameField.setText("");
        userAddressField.setText("");
        userBirthdayDPicker.getEditor().setText("");
        userRegistrationDateDPicker.getEditor().setText("");
        userEmailAddressField.setText("");

        Task<Void> getData = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                app.loadAllUsers();
                return null;
            }
        };
        new Thread(getData).start();
    }

    /**
     * Show the EditUser dialog according to what button has been clicked
     * @param e The event
     */
    public void showEditUserView(ActionEvent e) {
        try {
            boolean noError = true;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/EditUserView.fxml"));
            loader.setResources(I18n.getBundle());
            GridPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(app.getPrimaryStage());
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            EditUserController controller = loader.getController();

            if (((Button) e.getSource()).getId().equals(getUserAddButton().getId())) {
                controller.initializeAddUser();
                dialogStage.setTitle(I18n.getBundle().getString("label.add.user"));
            }
            else if (((Button) e.getSource()).getId().equals(getUserEditButton().getId())) {
                User selected = getSelectedUser();
                if (selected != null) {
                    controller.initializeEditUser(selected);
                    dialogStage.setTitle(I18n.getBundle().getString("label.editing.title") + selected.getUsername());
                } else {
                    noError = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(app.getPrimaryStage());
                    alert.setTitle(I18n.getBundle().getString("alert.noselection.user.title"));
                    alert.setHeaderText(I18n.getBundle().getString("alert.noselection.user.title"));
                    alert.setContentText(I18n.getBundle().getString("alert.noselection.user.edit.content"));
                    alert.showAndWait();
                }
            }

            if (noError) {
                controller.setDialogStage(dialogStage);
                controller.setApp(app);

                dialogStage.showAndWait();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setApp(App app) {
        this.app = app;
        usersTable.setItems(app.getUsersData());
    }
    public User getSelectedUser() {
        return usersTable.getSelectionModel().getSelectedItem();
    }

    public Button getUserAddButton() {
        return userAddButton;
    }

    public Button getUserEditButton() {
        return userEditButton;
    }

}
