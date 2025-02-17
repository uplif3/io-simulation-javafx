package io.simulation.view.segment;

import java.io.IOException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Ellipse;

public class SegmentColon extends AnchorPane {

    private final BooleanProperty isOn = new SimpleBooleanProperty(false);

    @FXML
    private Ellipse dotTop, dotBottom;

    public SegmentColon() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SegmentColon.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        isOn.addListener((obs, oldVal, newVal) -> updateColon(newVal));
        updateColon(isOn.get());
    }

    public boolean isOn() {
        return isOn.get();
    }

    public void setOn(boolean value) {
        isOn.set(value);
    }

    public BooleanProperty isOnProperty() {
        return isOn;
    }

    private void updateColon(boolean on) {
        double opacity = on ? 1.0 : 0.1;
        dotTop.setOpacity(opacity);
        dotBottom.setOpacity(opacity);
    }
}
