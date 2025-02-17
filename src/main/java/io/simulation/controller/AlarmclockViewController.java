package io.simulation.controller;

import io.simulation.screens.AlarmclockViewModel;
import io.simulation.view.segment.SegmentColon;
import io.simulation.view.segment.SegmentDigit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AlarmclockViewController {

    @FXML
    private SegmentDigit hoursTens;
    @FXML
    private SegmentDigit hoursOnes;
    @FXML
    private SegmentDigit minutesTens;
    @FXML
    private SegmentDigit minutesOnes;
    @FXML
    private SegmentColon colon;
    @FXML
    private Label alarmLabel;
    @FXML
    private Label beepLabel;

    private final AlarmclockViewModel viewModel = new AlarmclockViewModel();

    @FXML
    public void initialize() {
        // Bindung der Siebensegmentanzeigen an die ViewModel-Properties
        hoursTens.digitProperty().bind(viewModel.hoursTensProperty());
        hoursOnes.digitProperty().bind(viewModel.hoursOnesProperty());
        minutesTens.digitProperty().bind(viewModel.minutesTensProperty());
        minutesOnes.digitProperty().bind(viewModel.minutesOnesProperty());
        colon.isOnProperty().bind(viewModel.colonOnProperty());

        // Sichtbarkeit der Alarm-/Beep-Labels an die ViewModel-BooleanProperties binden
        alarmLabel.visibleProperty().bind(viewModel.alarmActiveProperty());
        beepLabel.visibleProperty().bind(viewModel.beepActiveProperty());
    }

    // Methode, um vom Screen (oder einer anderen Quelle) den Hex-String zu übergeben:
    public void setHexString(String hex) {
        viewModel.setHexString(hex);
    }

    public AlarmclockViewModel getViewModel() {
        return viewModel;
    }
}
