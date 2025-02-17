package io.simulation.screens;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class ScreenDebug implements Screen {

    private BorderPane root;
    private TextArea debugTextArea;

    public ScreenDebug() {
        createView();
    }

    private void createView() {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        debugTextArea = new TextArea();
        debugTextArea.setStyle("-fx-control-inner-background: lightyellow;");
        root.setCenter(debugTextArea);

        // optional Buttons ...
        HBox buttons = new HBox(10);
        Button btnClear = new Button("Clear");
        btnClear.setOnAction(e -> debugTextArea.clear());
        buttons.getChildren().addAll(btnClear);
        root.setBottom(buttons);
    }

    @Override
    public Pane getView() {
        return root;
    }

    @Override
    public void handleIncomingData(String line) {
        // Bsp: wir loggen standardmäßig alles
        debugTextArea.appendText(line + "\n");
    }
}
