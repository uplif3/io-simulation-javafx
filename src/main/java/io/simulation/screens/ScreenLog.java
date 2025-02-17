package io.simulation.screens;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class ScreenLog implements Screen {

    private BorderPane root;
    private TextArea logTextArea;
    private boolean autoScroll = true;

    public ScreenLog() {
        createView();
    }

    private void createView() {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        logTextArea = new TextArea();
        logTextArea.setWrapText(true);
        root.setCenter(logTextArea);

        // Unten z. B. Button-Leiste
        HBox buttons = new HBox(10);
        Button btnSync = new Button("Unsync");
        btnSync.setOnAction(e -> toggleSync(btnSync));
        Button btnClear = new Button("Clear");
        btnClear.setOnAction(e -> logTextArea.clear());
        buttons.getChildren().addAll(btnSync, btnClear);
        root.setBottom(buttons);
    }

    private void toggleSync(Button btn) {
        autoScroll = !autoScroll;
        btn.setText(autoScroll ? "Unsync" : "Sync");
        if (autoScroll) {
            logTextArea.setScrollTop(Double.MAX_VALUE);
        }
    }

    @Override
    public Pane getView() {
        return root;
    }

    // Hier kannst du einkommende Daten speziell für Log behandeln
    @Override
    public void handleIncomingData(String line) {
        // Beispiel: Wir nehmen an, wir wollen "L..."-Nachrichten loggen
        // oder wir rufen handleLog(line)
        handleLog(line);
    }

    public void handleLog(String msg) {
        logTextArea.appendText(msg + "\n");
        if (autoScroll) {
            logTextArea.setScrollTop(Double.MAX_VALUE);
        }
    }
}
