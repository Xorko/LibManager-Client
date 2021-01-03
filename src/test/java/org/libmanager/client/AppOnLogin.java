package org.libmanager.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.libmanager.client.App;
import org.libmanager.client.controller.LoginController;
import org.libmanager.client.util.I18n;

import java.io.IOException;

public class AppOnLogin extends App {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("LibManager");
        super.setPrimaryStage(primaryStage);
        super.initRootView();
        showLoginDialog();
    }

    public void showLoginDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/LoginView.fxml"));
            loader.setResources(I18n.getBundle());
            GridPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.setTitle(I18n.getBundle().getString("label.login.title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(super.getPrimaryStage());
            Scene scene = new Scene(dialog);
            dialogStage.setScene(scene);

            LoginController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApp(this);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
