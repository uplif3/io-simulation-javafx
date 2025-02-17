package io.simulation.controller;

import io.simulation.model.AlarmclockModel;
import io.simulation.view.segment.SegmentColon;
import io.simulation.view.segment.SegmentDigit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AlarmclockViewController {

    @FXML private SegmentDigit hoursTens;
    @FXML private SegmentDigit hoursOnes;
    @FXML private SegmentDigit minutesTens;
    @FXML private SegmentDigit minutesOnes;
    @FXML private SegmentColon colon;
    @FXML private Label alarmLabel;
    @FXML private Label beepLabel;

    // Reines Model (kein ViewModel mehr)
    private AlarmclockModel model = new AlarmclockModel();

    @FXML
    public void initialize() {
        // Beim Initialisieren wird die Ansicht gesetzt
        updateView();
    }

    // Wird vom MainController (z. B. via serielle Daten) aufgerufen:
    public void setHexString(String hex) {
        model.setHexString(hex);
        updateView();
    }

    public void handleIncomingData(String data) {
        setHexString(data);
    }

    /**
     * Aktualisiert die UI anhand der Werte im Model.
     * Es wird davon ausgegangen, dass SegmentDigit und SegmentColon über
     * Methoden wie setDigit(String) bzw. setOn(boolean) verfügen.
     */
    private void updateView() {
        hoursTens.setDigit(model.getHoursTens());
        hoursOnes.setDigit(model.getHoursOnes());
        minutesTens.setDigit(model.getMinutesTens());
        minutesOnes.setDigit(model.getMinutesOnes());
        colon.setOn(model.isColonOn());
        alarmLabel.setVisible(model.isAlarmActive());
        beepLabel.setVisible(model.isBeepActive());
    }

    public void setMainController(MainController mainController) {
        // Optional: Referenz auf den MainController, falls benötigt
    }
}
