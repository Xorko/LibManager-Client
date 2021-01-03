package org.libmanager.client.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.libmanager.client.App;
import org.libmanager.client.enums.ItemType;
import org.libmanager.client.model.Reservation;
import org.libmanager.client.service.Requests;
import org.libmanager.client.util.Config;
import org.libmanager.client.util.Converter;
import org.libmanager.client.util.DateUtil;
import org.libmanager.client.util.I18n;
import org.libmanager.client.util.ResponseUtil;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminPanelReservationsController implements Initializable {

    @FXML
    private TextField reservationIdField;
    @FXML
    private TextField reservationUsernameField;
    @FXML
    private TextField reservationTitleField;
    @FXML
    private DatePicker reservationDateDPicker;
    @FXML
    private ComboBox<ItemType> reservationTypeCBox;
    @FXML
    private TableView<Reservation> reservationsTable;
    @FXML
    private TableColumn<Reservation, Long> reservationIdColumn;
    @FXML
    private TableColumn<Reservation, String> reservationTypeColumn;
    @FXML
    private TableColumn<Reservation, String> reservationUsernameColumn;
    @FXML
    private TableColumn<Reservation, String> reservationTitleColumn;
    @FXML
    private TableColumn<Reservation, String> reservationDateColumn;
    @FXML
    private TableColumn<Reservation, Button> reservationDeleteColumn;

    private App app;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reservationTypeCBox.getItems().setAll(ItemType.values());
        reservationTypeCBox.valueProperty().set(ItemType.ANY);
        reservationTypeCBox.setConverter(Converter.getTypeConverter());

        // --- RESERVATIONS TABLE ---
        reservationIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        reservationUsernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        reservationDateColumn.setCellValueFactory(cellData ->  new SimpleStringProperty(DateUtil.format(cellData.getValue().getReservationDate())));
        reservationTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        reservationTypeColumn.setCellValueFactory(cellData -> {
            SimpleStringProperty typeProperty = new SimpleStringProperty(null);

            switch (cellData.getValue().itemTypeProperty().getValue()) {
                case "BOOK":
                    typeProperty.set(I18n.getBundle().getString("label.book"));
                    break;
                case "DVD":
                    typeProperty.set(I18n.getBundle().getString("label.dvd"));
                    break;
                default:
                    typeProperty.set(I18n.getBundle().getString("enum.unknown"));
            }
            return typeProperty;
        });
        reservationDeleteColumn.setCellValueFactory(cellData -> {
            Button btn = new Button();

            btn.setMaxWidth(100);
            btn.setText(I18n.getBundle().getString("button.return"));

            btn.setOnAction(event -> handleDeleteReservation(cellData.getValue()));

            return new SimpleObjectProperty<>(btn);
        });

        // --- Reservations table columns dimensions ---
        reservationIdColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.09));
        reservationTypeColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.1));
        reservationUsernameColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.2));
        reservationDateColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.13));
        reservationTitleColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.33));
        reservationDeleteColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.15));

        reservationDateDPicker.setConverter(new StringConverter<>() {
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
     * Delete a reservation
     * @param reservation The reservation to delete
     */
    private void handleDeleteReservation(Reservation reservation) {
        if (app.getLoggedInUser() != null && app.getLoggedInUser().isAdmin()) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.getDialogPane().getStylesheets().add(Config.getTheme());
            confirmationAlert.initOwner(app.getPrimaryStage());
            confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            confirmationAlert.setHeaderText(I18n.getBundle().getString("alert.confirm.return"));
            Optional<ButtonType> answer = confirmationAlert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                String response = Requests.callDeleteReservation(app.getLoggedInUser().getToken(), Long.toString(reservation.getId()));
                if (ResponseUtil.analyze(response, app.getPrimaryStage()))
                    app.loadAllReservations();
            }
        }
    }

    @FXML
    private void handleReservationSearch() {
        Task<Void> searchReservations = new Task<>() {
            @Override
            protected Void call() {
                String response = Requests.callSearchReservation(
                        app.getLoggedInUser().getToken(),
                        reservationIdField.getText().length() == 0 ? null : reservationIdField.getText(),
                        reservationUsernameField.getText().length() == 0 ? null: reservationUsernameField.getText(),
                        reservationUsernameField.getText().length() == 0 ? null : reservationUsernameField.getText(),
                        reservationTypeCBox.valueProperty().get() == ItemType.ANY ? null : reservationTypeCBox.valueProperty().get().toString(),
                        !DateUtil.validDate(reservationDateDPicker.getEditor().getText()) ? null : DateUtil.formatDB(DateUtil.parse(reservationDateDPicker.getEditor().getText()))
                );
                app.loadReservationsFromJSON(response);
                return null;
            }
        };
        new Thread(searchReservations).start();
    }

    @FXML
    private void handleResetReservation() {
        reservationIdField.setText("");
        reservationUsernameField.setText("");
        reservationDateDPicker.getEditor().setText("");
        reservationTitleField.setText("");
        reservationTypeCBox.valueProperty().set(ItemType.ANY);

        Task<Void> getData = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                app.loadAllReservations();
                return null;
            }
        };
        new Thread(getData).start();
    }

    public void setApp(App app) {
        this.app = app;
        reservationsTable.setItems(app.getReservationsData());
    }
}
