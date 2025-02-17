package io.simulation.screens;

import io.simulation.MainController;
import io.simulation.controller.SeesawController;
import io.simulation.model.SeesawModel;
import io.simulation.screens.SeesawCanvas;
import io.simulation.screens.SeesawGraphCanvas;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;

public class ScreenSeesaw implements Screen {

    private Pane root;
    private MainController mainController;
    // MVC-Komponenten:
    private SeesawModel seesawModel;
    private SeesawController seesawController;
    private SeesawCanvas seesawCanvas;
    private SeesawGraphCanvas graphCanvas;

    public ScreenSeesaw(MainController mainController) {
        this.mainController = mainController;
        // Model, Views und Controller initialisieren:
        seesawModel = new SeesawModel();
        seesawCanvas = new SeesawCanvas(600, 300);
        graphCanvas = new SeesawGraphCanvas();
        seesawController = new SeesawController(seesawModel, seesawCanvas, graphCanvas);
        createView();
    }

    private void createView() {
        // Beispielhaft: vertikale Anordnung der beiden Canvas
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        pane.getChildren().addAll(seesawCanvas, graphCanvas);
        this.root = pane;
    }

    @Override
    public Pane getView() {
        return root;
    }

    @Override
    public void handleIncomingData(String line) {
        System.out.println("ScreenSeesaw: empfangen: " + line);
        seesawController.processPacket(line);
    }
}
