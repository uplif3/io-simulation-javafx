package io.simulation.controller;

import io.simulation.model.SeesawModel;
import io.simulation.screens.SeesawCanvas;
import io.simulation.screens.SeesawGraphCanvas;

public class SeesawController {
    private SeesawModel model;
    private SeesawCanvas seesawCanvas;
    private SeesawGraphCanvas graphCanvas;

    public SeesawController(SeesawModel model, SeesawCanvas seesawCanvas, SeesawGraphCanvas graphCanvas) {
        this.model = model;
        this.seesawCanvas = seesawCanvas;
        this.graphCanvas = graphCanvas;
    }

    public void processPacket(String packet) {
        model.updateFromPacket(packet);
        // Aktualisiere beide Views mit den aktuellen Modelwerten:
        seesawCanvas.redraw(model);
        graphCanvas.updateGraphData(model.getReference(), model.getBall(), model.getAngle());
    }
}
