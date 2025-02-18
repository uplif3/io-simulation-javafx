package io.simulation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class DebugViewController {

    @FXML private TextArea debugTextArea;
    @FXML private Button btnClear;

    @FXML
    public void initialize() {
        btnClear.setOnAction(e -> debugTextArea.clear());
    }

    /**
     * Methode zum Empfangen eingehender Daten.
     * So kannst du von außen (z. B. MainController)
     * Nachrichten in dieses Debug-Fenster schreiben.
     */
    public void handleIncomingData(String line) {
        debugTextArea.appendText(line + "\n");
    }
}
