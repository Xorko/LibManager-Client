package org.libmanager.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.libmanager.client.controller.EditItemController;
import org.libmanager.client.util.Config;
import org.libmanager.client.util.I18n;

import java.io.IOException;

public class AppOnAddBook extends App{
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("LibManager");
        super.setPrimaryStage(primaryStage);
        super.initRootView();
        super.setLoggedInUser(TestData.getUser());
        showAddItem();
    }

    public void showAddItem() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/EditItemView.fxml"));
            loader.setResources(I18n.getBundle());
            GridPane dialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(super.getPrimaryStage());
            Scene scene = new Scene(dialog);
            scene.getStylesheets().add(Config.getTheme());
            dialogStage.setScene(scene);

            EditItemController controller = loader.getController();
            controller.initializeAddBook();
            dialogStage.setTitle(I18n.getBundle().getString("label.add.book"));

            controller.setDialogStage(dialogStage);
            controller.setApp(this);

            dialogStage.show();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
