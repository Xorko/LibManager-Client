package org.libmanager.client;


import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;

import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;


import java.util.concurrent.TimeoutException;



@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = {8080})
public abstract class TestBase extends ApplicationTest {

    private Stage primaryStage;

    @BeforeAll
    public static void setupHeadlessMode() {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }

    }

    @BeforeEach
    public void beforeEachTest() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(App.class);
    }

    @AfterEach
    public void afterEachTest() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        stage.show();
    }


    /*Helper method to retrieve Java FX GUI components*/
    public <T extends Node> T find(final String query) {
        return (T) lookup(query).queryAll().iterator().next();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void ensureEventQueueComplete(){
        WaitForAsyncUtils.waitForFxEvents(1);
    }

    /**
     * Return the current alert title
     * Only way to get the title for showAndWait() alerts
     * @return alert title
     */
    public String getAlertTitle() {
        String alertTitle = "";
        for (Window w : this.listWindows()) {
            if (!((Stage) w).getModality().toString().equals("NONE")) {
                alertTitle = ((Stage) w).getTitle();
            }
        }
        return alertTitle;
    }
}
