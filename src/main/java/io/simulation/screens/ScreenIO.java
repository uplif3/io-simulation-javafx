package io.simulation.screens;

import io.simulation.MainController;
import io.simulation.model.IOModel;
import io.simulation.model.MainModel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ScreenIO implements Screen {

    private Pane root;
    private MainModel mainModel;
    private MainController mainController;
    private IOModel ioModel;

    public ScreenIO(MainModel mainModel, MainController mainController) {
        this.mainModel = mainModel;
        this.mainController = mainController;
        this.ioModel = new IOModel();
        createView();
    }

    private void createView() {
         // Zugriff auf dein IOModel
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // LEDs
        Label lblLED = new Label("LEDs:");
        grid.add(lblLED, 0, 0);
        for (int i = 0; i < 8; i++) {
            CheckBox chk = new CheckBox("LED " + i);
            // Bidirektionale Bindung: UI-Komponente und Model bleiben synchron
            chk.selectedProperty().bindBidirectional(ioModel.ledProperty(i));
            chk.setDisable(true);
            grid.add(chk, i + 1, 0);
        }

        // Switches
        Label lblSwitch = new Label("Switches:");
        grid.add(lblSwitch, 0, 1);
        for (int i = 0; i < 8; i++) {
            CheckBox sw = new CheckBox("SW " + i);
            sw.selectedProperty().bindBidirectional(ioModel.switchProperty(i));
            ioModel.switchProperty(i).addListener((obs, oldVal, newVal) -> {
                mainController.SerialWriteLine("d01" + ioModel.getSwitchesAsHex());
            });

            grid.add(sw, i + 1, 1);
        }

        // Buttons
        Label lblButtons = new Label("Buttons:");
        grid.add(lblButtons, 0, 2);
        for (int i = 0; i < 4; i++) {
            Button btn = new Button("BTN " + i);
            // Hier könntest du einen EventHandler definieren, der z. B. das IOModel aktualisiert oder einen Befehl an den MainController sendet.
            int btnIndex = i; // final für Lambda
            btn.setOnAction(e -> {
                // Beispiel: Toggle den Button-Zustand im Modell
                ioModel.setButton(btnIndex, !ioModel.getButton(btnIndex));
                // Sende den neuen Button-Zustand als Hex
                mainController.SerialWriteLine("d02" + ioModel.getButtonsAsHex());
            });
            grid.add(btn, i + 1, 2);
        }

        // Scale0
        Label lblScale0 = new Label("Scale0:");
        grid.add(lblScale0, 0, 3);

        // Slider für Scale0 (Beispiel: Wertebereich 0 bis 1023)
        Slider slider0 = new Slider(0, 1023, 0);
        slider0.setShowTickMarks(true);
        slider0.setShowTickLabels(true);
        slider0.setMajorTickUnit(256);
        slider0.setBlockIncrement(1);
        // Listener für Veränderungen
        slider0.valueProperty().addListener((obs, oldVal, newVal) -> {
            int newInt = newVal.intValue();
            ioModel.setScale0(newInt);
            // Sende den neuen Scale0-Wert in Hex
            mainController.SerialWriteLine("d0a" + ioModel.getScale0AsHex());
        });
        // Füge den Slider in die gleiche Zeile (row 3) ein, Spalten kannst du anpassen
        grid.add(slider0, 1, 3, 8, 1);

        // Scale1
        Label lblScale1 = new Label("Scale1:");
        grid.add(lblScale1, 0, 4);

        // Slider für Scale1
        Slider slider1 = new Slider(0, 1023, 0);
        slider1.setShowTickMarks(true);
        slider1.setShowTickLabels(true);
        slider1.setMajorTickUnit(256);
        slider1.setBlockIncrement(1);
        slider1.valueProperty().addListener((obs, oldVal, newVal) -> {
            int newInt = newVal.intValue();
            ioModel.setScale1(newInt);
            // Sende den neuen Scale1-Wert in Hex
            mainController.SerialWriteLine("d0b" + ioModel.getScale1AsHex());
        });
        grid.add(slider1, 1, 4, 8, 1);

        this.root = grid;
    }


    @Override
    public Pane getView() {
        return root;
    }

    @Override
    public void handleIncomingData(String line) {
        // z.B. "d2abc1234",
        // => Ab positions[2..] parsen wir "abc1234"
        // => interpretieren => UI updaten
        String payload = line.substring(2);
        // deine Logik ...
        System.out.println("ScreenIO: habe Daten nur für mich: " + payload);

        ioModel.setLedsFromHex(payload);
        // Dann evtl. UI aktualisieren:
        // z.B. parse 2 floats -> position, angle,
        // => update graphics
    }
}
