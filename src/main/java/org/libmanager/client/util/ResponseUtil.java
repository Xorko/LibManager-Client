package org.libmanager.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ResponseUtil {

    /**
     * Analyze a received response from the server
     * @param response  The received response
     * @param initOwner The initOwner of the alert
     * @return          True if the response code is OK, false otherwise
     */
    public static boolean analyze(String response, Stage initOwner) {
        if (response != null) {
            try {
                String code = new ObjectMapper().readTree(response).get("code").asText();
                if (code != null) {
                    if (code.equals("OK")) {
                        return true;
                    } else
                        handleErrorCode(code, initOwner);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(initOwner);
            alert.setHeaderText(I18n.getBundle().getString("server.connection.failed.alert"));
            alert.setContentText(I18n.getBundle().getString("server.connection.failed"));
        }
        return false;
    }

    /**
     * Show an alert according to the error code
     * @param code      The received error code
     * @param initOwner The owner of the alert
     */
    private static void handleErrorCode(String code, Stage initOwner) {
        if (code != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().getStylesheets().add(Config.getTheme());
            alert.initOwner(initOwner);
            switch (code) {
                case "INVALID_TOKEN":
                    alert.setHeaderText(I18n.getBundle().getString("alert.invalid.token.header"));
                    alert.setContentText(I18n.getBundle().getString("alert.invalid.token.content"));
                    break;
                case "INVALID_MAIL_TOKEN":
                    alert.setHeaderText(I18n.getBundle().getString("alert.invalid.token.header"));
                    alert.setContentText(I18n.getBundle().getString("alert.invalid.mail.token.content"));
                    break;
                case "NOT_AVAILABLE":
                    alert.setHeaderText(I18n.getBundle().getString("alert.notavailable.header"));
                    alert.setContentText(I18n.getBundle().getString("alert.notavailable.content"));
                    break;
                case "MAX_RESERVATIONS_REACHED":
                    alert.setHeaderText(I18n.getBundle().getString("alert.max.reservations.header"));
                    alert.setContentText(I18n.getBundle().getString("alert.max.reservations.content"));
                    break;
                case "NOT_FOUND":
                    alert.setHeaderText(I18n.getBundle().getString("alert.notfound.header"));
                    alert.setContentText(I18n.getBundle().getString("alert.notfound.content"));
                    break;
                case "MAX_ITEMS_REACHED":
                    alert.setHeaderText(I18n.getBundle().getString("alert.item.limit.reached.header"));
                    alert.setContentText(I18n.getBundle().getString("alert.item.limit.reached.content"));
                    break;
                case "MAX_USERS_REACHED":
                    alert.setHeaderText(I18n.getBundle().getString("alert.max.users.header"));
                    alert.setContentText(I18n.getBundle().getString("alert.max.users.content"));
                    break;
                case "INVALID_TOTAL_COPIES":
                    alert.setHeaderText(I18n.getBundle().getString("alert.invalid.copies.header"));
                    alert.setContentText(I18n.getBundle().getString("alert.invalid.copies.content"));
                    break;
                case "INTEGRITY_VIOLATION":
                    alert.setHeaderText(I18n.getBundle().getString("alert.integrity.header"));
                    alert.setContentText(I18n.getBundle().getString("alert.integrity.content"));
                    break;
                case "FORBIDDEN":
                    alert.setHeaderText(I18n.getBundle().getString("alert.forbidden.header"));
                    alert.setContentText(I18n.getBundle().getString("alert.forbidden.content"));
                    break;
            }
            alert.showAndWait();
        }
    }

}
