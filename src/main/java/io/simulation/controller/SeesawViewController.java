package io.simulation.controller;

import io.simulation.model.SeesawModel;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import io.simulation.screens.SeesawCanvas;
import io.simulation.screens.SeesawGraphCanvas;

public class SeesawViewController {

    @FXML
    private SeesawCanvas seesawCanvas = new SeesawCanvas(300,150); // Wird aus FXML injiziert

    @FXML
    private SeesawGraphCanvas graphCanvas = new SeesawGraphCanvas();  // Wird aus FXML injiziert

    private SeesawModel model;
    private SeesawController seesawController;

    /**
     * Diese Methode wird automatisch nach dem Laden der FXML aufgerufen.
     */
    @FXML
    private void initialize() {
        // Model initialisieren
        model = new SeesawModel();
        // SeesawController initialisieren, der Model und beide Canvas verwaltet.
        seesawController = new SeesawController(model, seesawCanvas, graphCanvas);
    }

    /**
     * Methode zum Verarbeiten eingehender Daten.
     * Diese kann vom MainController oder einer anderen Komponente aufgerufen werden.
     */
    public void handleIncomingData(String data) {
        seesawController.processPacket(data);
    }

    public void setMainController(MainController main){

    }
}
