package io.simulation.screens;

import io.simulation.MainController;
import io.simulation.model.MainModel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ScreenAlarmclock implements Screen {

    private Pane root;
    private MainModel mainModel;
    private MainController mainController;

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

        // Beispiel: 8 LEDs, 8 Switches, 4 Buttons
        Label lblLED = new Label("LEDs:");
        grid.add(lblLED, 0, 0);

        for(int i=0; i<8; i++){
            CheckBox chk = new CheckBox("LED " + i);
            // chk.setSelected(...) => Ansteuerung könnte an ein Model gebunden werden
            grid.add(chk, i+1, 0);
        }

        Label lblSwitch = new Label("Switches:");
        grid.add(lblSwitch, 0, 1);
        for(int i=0; i<8; i++){
            CheckBox sw = new CheckBox("SW " + i);
            // EventHandler => sendet was an mainController etc.
            grid.add(sw, i+1, 1);
        }

        Label lblButtons = new Label("Buttons:");
        grid.add(lblButtons, 0, 2);
        for(int i=0; i<4; i++){
            Button btn = new Button("BTN " + i);
            // btn.setOnAction(e -> ...)
            grid.add(btn, i+1, 2);
        }

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
        System.out.println("ScreenAc: habe Daten nur für mich: " + payload);

        // Dann evtl. UI aktualisieren:
        // z.B. parse 2 floats -> position, angle,
        // => update graphics
    }
}
