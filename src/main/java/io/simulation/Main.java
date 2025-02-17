package io.simulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        primaryStage.setScene(new Scene(loader.load(), 1000, 600));
        primaryStage.setTitle("IO Simulation - Virtual Peripherals");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
