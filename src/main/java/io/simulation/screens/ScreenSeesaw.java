package io.simulation.screens;

import io.simulation.MainController;
import io.simulation.model.MainModel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ScreenSeesaw implements Screen {

    private Pane root;
    private MainModel mainModel;
    private MainController mainController;

    public ScreenSeesaw(MainModel mainModel, MainController mainController) {
        this.mainModel = mainModel;
        this.mainController = mainController;
        createView();
    }

    private void createView() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Seesaw"), 0, 0);

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
        System.out.println("ScreenSeesaw: habe Daten nur für mich: " + payload);

        // Dann evtl. UI aktualisieren:
        // z.B. parse 2 floats -> position, angle,
        // => update graphics
    }
}
