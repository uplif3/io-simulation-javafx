package io.simulation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class LogViewController {

    @FXML
    private TextArea logTextArea;
    @FXML
    private Button btnSync;
    @FXML
    private Button btnClear;

    private boolean autoScroll = true;

    @FXML
    public void initialize() {
        // Setze die Aktion für den Clear-Button:
        btnClear.setOnAction(e -> logTextArea.clear());

        // Setze die Aktion für den Sync-/Unsync-Button:
        btnSync.setOnAction(e -> toggleSync());
    }

    private void toggleSync() {
        autoScroll = !autoScroll;
        btnSync.setText(autoScroll ? "Unsync" : "Sync");
        if (autoScroll) {
            // Wenn autoScroll aktiviert wird, scrollt die TextArea nach unten
            logTextArea.setScrollTop(Double.MAX_VALUE);
        }
    }

    /**
     * Diese Methode wird von außen (z.B. vom MainController) aufgerufen, um Log-Nachrichten hinzuzufügen.
     *
     * @param msg die hinzuzufügende Nachricht
     */
    public void handleLog(String msg) {
        logTextArea.appendText(msg + "\n");
        if (autoScroll) {
            logTextArea.setScrollTop(Double.MAX_VALUE);
        }
    }

    /**
     * Optional: Methode, um eingehende Nachrichten direkt zu verarbeiten.
     *
     * @param line die empfangene Zeile
     */
    public void handleIncomingData(String line) {
        handleLog(line);
    }
}
