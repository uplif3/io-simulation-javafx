package io.simulation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Beispiel: Ein einfacher Button, der "Hello, JavaFX!" anzeigt
        Button button = new Button("Hello, JavaFX!");
        Scene scene = new Scene(button, 400, 300);

        primaryStage.setTitle("JavaFX-Anwendung mit Serieller Schnittstelle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Startet die JavaFX-Anwendung
        launch(args);
    }
}
