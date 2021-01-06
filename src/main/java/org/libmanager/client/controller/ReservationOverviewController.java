package org.libmanager.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.libmanager.client.App;
import org.libmanager.client.model.Reservation;
import org.libmanager.client.service.Requests;
import org.libmanager.client.util.Config;
import org.libmanager.client.util.I18n;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

public class ReservationOverviewController implements Initializable {

    @FXML
    private TableView<Reservation> reservationsTable;
    @FXML
    private TableColumn<Reservation, Long> idColumn;
    @FXML
    private TableColumn<Reservation, String> titleColumn;
    @FXML
    private TableColumn<Reservation, String> typeColumn;
    @FXML
    private Label statusLabel;

    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    private Stage dialogStage;
    private App app;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        reservationsTable.setItems(reservations);
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        typeColumn.setCellValueFactory(cellData -> {
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

        idColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.25));
        typeColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.25));
        titleColumn.prefWidthProperty().bind(reservationsTable.widthProperty().multiply(0.495));
    }

    private void loadReservations() {
        Task<Void> loadReservations = new Task<>() {
            @Override
            protected Void call() {
                JsonNode root = null;
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                String response = Requests.callGetReservationsByUser(app.getLoggedInUser().getToken());
                try {
                    root = mapper.readTree(response);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                if (root == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getStylesheets().add(Config.getTheme());
                    alert.initOwner(dialogStage);
                    alert.setTitle(I18n.getBundle().getString("server.connection.failed.alert"));
                    alert.setHeaderText(I18n.getBundle().getString("server.connection.failed"));
                    alert.showAndWait();
                } else {
                    if (root.get("code").asText().equals("OK")) {
                        List<Reservation> reservations = null;
                        CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, Reservation.class);
                        try {
                            String content = mapper.writeValueAsString(root.get("content"));
                            reservations = mapper.readValue(content, type);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        getReservations().setAll(reservations);
                    }
                }
                return null;
            }
        };
        new Thread(loadReservations).start();

        loadReservations.setOnSucceeded(event -> setStatusText());
    }

    /**
     *
     */
    private void setStatusText() {
        int borrowedBooks = calculateNumberOfBorrowedBooks();
        int borrowedDVDs = calculateNumberOfBorrowedDVDs();
        if (!app.getLoggedInUser().isAdmin()) {
            int maxBooks = calculateBookLimit();
            int maxDVDs = calculateDVDLimit();
            statusLabel.setText(
                    I18n.getBundle().getString("label.limits") + "\t" + I18n.getBundle().getString("label.limits.books")
                            + borrowedBooks + "/" + maxBooks + "\t" + I18n.getBundle().getString("label.limits.dvds")
                            + borrowedDVDs + "/" + maxDVDs
            );
        }
        else {
            statusLabel.setText(
                    I18n.getBundle().getString("label.limits") + "\t" + I18n.getBundle().getString("label.limits.books")
                            + borrowedBooks + "\t" + I18n.getBundle().getString("label.limits.dvds")
                            + borrowedDVDs
            );
        }
    }

    /**
     * Calculate the current number of books borrowed by the user
     * @return The current number of books borrowed by the user
     */
    private int calculateNumberOfBorrowedBooks() {
        int nbBooks = 0;
        for (Reservation r : reservations) {
            if (r.getItemType().equals("BOOK"))
                nbBooks++;
        }
        return nbBooks;
    }

    /**
     * Calculate the current number of DVDs borrowed by the user
     * @return The current number of DVDs borrowed by the user
     */
    private int calculateNumberOfBorrowedDVDs() {
        int nbDVDs = 0;
        for (Reservation r : reservations) {
            if (r.getItemType().equals("DVD"))
                nbDVDs++;
        }
        return nbDVDs;
    }

    /**
     * Calculate the maximum number of books the user can borrow
     * @return The maximum number of books the user can borrow
     */
    private int calculateBookLimit() {
        long membershipDuration = ChronoUnit.YEARS.between(app.getLoggedInUser().getRegistrationDate(), LocalDate.now());
        boolean isAdult = ChronoUnit.YEARS.between(app.getLoggedInUser().getBirthday(), LocalDate.now()) >= 12;

        if (isAdult) {
            // During his first year of membership, the adult user can borrow up to 4 books
            if (membershipDuration < 1)
                return 4;
                // During his second year of membership, the adult user can borrow up to 5 books
            else if (membershipDuration < 2)
                return  5;
                // After three years of membership, the adult user can borrow up to 7 books
            else
                return 7;
        }
        // Children can borrow up to 5 books
        else
            return 5;
    }

    /**
     * Calculate the maximum number of DVDs the user can borrow
     * @return The maximum number of DVDS the user can borrow
     */
    private int calculateDVDLimit() {
        long membershipDuration = ChronoUnit.YEARS.between(app.getLoggedInUser().getRegistrationDate(), LocalDate.now());
        boolean isAdult = ChronoUnit.YEARS.between(app.getLoggedInUser().getBirthday(), LocalDate.now()) >= 12;

        if (isAdult) {
            // During his first year of membership, the adult user can borrow up to 2 DVDs
            if (membershipDuration < 1)
                return 2;
                // During his second year of membership, the adult user can borrow up to 3 DVDs
            else if (membershipDuration < 2)
                return 3;
                // After three years of membership, the adult user can borrow up to 5 DVDs
            else
                return 5;
        }
        // Children can't borrow DVDs
        else
            return 0;
    }

    public synchronized ObservableList<Reservation> getReservations() {
        return reservations;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setApp(App app) {
        this.app = app;
        loadReservations();
    }
}
