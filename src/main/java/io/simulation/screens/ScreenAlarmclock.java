package io.simulation.screens;

import io.simulation.MainController;
import io.simulation.model.MainModel;
import io.simulation.controller.AlarmclockViewController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class ScreenAlarmclock implements Screen {

    private Pane root;
    private MainModel mainModel;
    private MainController mainController;
    private AlarmclockViewController alarmController; // Referenz zum Controller

    public ScreenAlarmclock(MainModel mainModel, MainController mainController) {
        this.mainModel = mainModel;
        this.mainController = mainController;
        createView();
    }

    private void createView() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        try {
            // Lade die Alarmclock-View-FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlarmclockView.fxml"));
            Pane alarmclockView = loader.load();

            // Speichere den Controller
            alarmController = loader.getController();

            grid.add(alarmclockView, 0, 1);
        } catch (IOException e) {
            e.printStackTrace();
            grid.add(new Label("Fehler beim Laden der Alarmclock View"), 0, 1);
        }

        this.root = grid;
    }

    @Override
    public Pane getView() {
        return root;
    }

    @Override
    public void handleIncomingData(String line) {
        // Beispiel: "d2abc1234" – ab Position 2 wird der Hex-String extrahiert
        String payload = line.substring(2);
        System.out.println("ScreenAlarmclock: habe Daten nur für mich: " + payload);

        // Übergebe den Hex-String an den Controller, damit die Anzeige aktualisiert wird
        if (alarmController != null) {
            alarmController.setHexString(payload);
        }
    }
}
