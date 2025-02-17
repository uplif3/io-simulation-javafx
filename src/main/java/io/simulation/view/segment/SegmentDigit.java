package io.simulation.view.segment;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class SegmentDigit extends AnchorPane {

    private final StringProperty digit = new SimpleStringProperty("0");

    @FXML
    private Rectangle segA, segB, segC, segD, segE, segF, segG;

    public SegmentDigit() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SegmentDigit.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        digit.addListener((obs, oldVal, newVal) -> updateSegments(newVal));
        updateSegments(getDigit());
    }


    public String getDigit() {
        return digit.get();
    }

    public void setDigit(String value) {
        digit.set(value);
    }

    public StringProperty digitProperty() {
        return digit;
    }

    private void updateSegments(String newDigit) {
        boolean segAOn = false, segBOn = false, segCOn = false,
                segDOn = false, segEOn = false, segFOn = false, segGOn = false;

        switch (newDigit) {
            case "0": segAOn = segBOn = segCOn = segDOn = segEOn = segFOn = true; break;
            case "1": segBOn = segCOn = true; break;
            case "2": segAOn = segBOn = segDOn = segEOn = segGOn = true; break;
            case "3": segAOn = segBOn = segCOn = segDOn = segGOn = true; break;
            case "4": segBOn = segCOn = segFOn = segGOn = true; break;
            case "5": segAOn = segCOn = segDOn = segFOn = segGOn = true; break;
            case "6": segAOn = segCOn = segDOn = segEOn = segFOn = segGOn = true; break;
            case "7": segAOn = segBOn = segCOn = true; break;
            case "8": segAOn = segBOn = segCOn = segDOn = segEOn = segFOn = segGOn = true; break;
            case "9": segAOn = segBOn = segCOn = segDOn = segFOn = segGOn = true; break;
            default: break;
        }

        segA.setOpacity(segAOn ? 1 : 0.1);
        segB.setOpacity(segBOn ? 1 : 0.1);
        segC.setOpacity(segCOn ? 1 : 0.1);
        segD.setOpacity(segDOn ? 1 : 0.1);
        segE.setOpacity(segEOn ? 1 : 0.1);
        segF.setOpacity(segFOn ? 1 : 0.1);
        segG.setOpacity(segGOn ? 1 : 0.1);
    }
}
