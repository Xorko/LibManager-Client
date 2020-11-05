package org.libmanager.client;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        VBox content = new VBox();
        Button btn = new Button("Hello world");
        Scene scene = new Scene(root);

        root.setCenter(content);
        content.getChildren().add(btn);
        content.setAlignment(Pos.CENTER);
        primaryStage.setScene(scene);
        primaryStage.setTitle("LibManager");
        primaryStage.show();
    }
}
