package io.simulation.screens;

import javafx.scene.layout.Pane;

public interface Screen {
    /**
     * Gibt das JavaFX-Pane (oder ein anderes Node-Objekt) zurück,
     * das diesen Screen repräsentiert.
     */
    Pane getView();

    /**
     * Optional: Methode, um Daten vom Haupt-Controller oder Model zu empfangen
     * und den Screen zu aktualisieren.
     */
    void handleIncomingData(String line);
}
