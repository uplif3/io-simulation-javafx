package io.simulation.controller;

import io.simulation.model.IOModel;
import io.simulation.view.segment.LEDCanvas;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

import java.util.HashSet;
import java.util.Set;

public class IOViewController {

    // Hier bindest du alle FXML-Elemente per fx:id an
    @FXML private HBox ledContainer;
    @FXML private CheckBox switch0, switch1, switch2, switch3, switch4, switch5, switch6, switch7; // ... bis switch7
    @FXML private Button button0, button1, button2, button3;    // ... bis button3
    @FXML private Slider slider0, slider1;

    private final LEDCanvas[] ledArray = new LEDCanvas[8];
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    private IOModel ioModel;
    private MainController mainController;

    /**
     * Wird direkt nach dem Laden von FXML aufgerufen.
     */
    @FXML
    public void initialize() {
        // Falls du das Model direkt hier anlegen willst:
        this.ioModel = new IOModel();

        // LEDs initialisieren
        for (int i = 7; i >= 0; i--) {
            ledArray[i] = new LEDCanvas(10);
            ledContainer.getChildren().add(ledArray[i]);

            // Property-Bindung der LEDs an das Modell
            int finalI = i;
            ioModel.ledProperty(i).addListener((obs, oldVal, newVal) -> ledArray[finalI].setOn(newVal));
        }


        // setDisable(true) kannst du optional im FXML oder hier per code machen.

        // Beispiel: Switch-Bindung + Listener
        switch0.selectedProperty().bindBidirectional(ioModel.switchProperty(0));
        switch0.selectedProperty().addListener((obs, ov, nv) -> sendSwitches());
        switch1.selectedProperty().bindBidirectional(ioModel.switchProperty(1));
        switch1.selectedProperty().addListener((obs, ov, nv) -> sendSwitches());
        switch2.selectedProperty().bindBidirectional(ioModel.switchProperty(2));
        switch2.selectedProperty().addListener((obs, ov, nv) -> sendSwitches());
        switch3.selectedProperty().bindBidirectional(ioModel.switchProperty(3));
        switch3.selectedProperty().addListener((obs, ov, nv) -> sendSwitches());
        switch4.selectedProperty().bindBidirectional(ioModel.switchProperty(4));
        switch4.selectedProperty().addListener((obs, ov, nv) -> sendSwitches());
        switch5.selectedProperty().bindBidirectional(ioModel.switchProperty(5));
        switch5.selectedProperty().addListener((obs, ov, nv) -> sendSwitches());
        switch6.selectedProperty().bindBidirectional(ioModel.switchProperty(6));
        switch6.selectedProperty().addListener((obs, ov, nv) -> sendSwitches());
        switch7.selectedProperty().bindBidirectional(ioModel.switchProperty(7));
        switch7.selectedProperty().addListener((obs, ov, nv) -> sendSwitches());


        // Buttons => OnAction
        button0.setOnAction(e -> toggleButton(0));
        button1.setOnAction(e -> toggleButton(1));
        button2.setOnAction(e -> toggleButton(2));
        button3.setOnAction(e -> toggleButton(3));

        // Scale0
        slider0.valueProperty().addListener((obs, ov, nv) -> {
            int newVal = nv.intValue();
            ioModel.setScale0(newVal);
            sendScale0();
        });
        // Scale1
        slider1.valueProperty().addListener((obs, ov, nv) -> {
            int newVal = nv.intValue();
            ioModel.setScale1(newVal);
            sendScale1();
        });
    }

    /**
     * Registriert Keybindings für die Buttons
     */
    public void setupKeyBindings(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            if (!pressedKeys.contains(code)) { // Verhindert Wiederholung
                pressedKeys.add(code);
                switch (code) {
                    case DIGIT1 -> pressButton(3);
                    case DIGIT2 -> pressButton(2);
                    case DIGIT3 -> pressButton(1);
                    case DIGIT4 -> pressButton(0);
                }
            }
        });

        scene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (pressedKeys.contains(code)) {
                pressedKeys.remove(code);
                switch (code) {
                    case DIGIT1 -> releaseButton(3);
                    case DIGIT2 -> releaseButton(2);
                    case DIGIT3 -> releaseButton(1);
                    case DIGIT4 -> releaseButton(0);
                }
            }
        });
    }

    private void pressButton(int index) {
        ioModel.setButton(index, true);
        getButton(index).setStyle("-fx-background-color: lightblue;"); // Visuelle Bestätigung
        if (mainController != null) {
            mainController.SerialWriteLine("d02" + ioModel.getButtonsAsHex());
        }
    }

    private void releaseButton(int index) {
        ioModel.setButton(index, false);
        getButton(index).setStyle(""); // Standard-Stil wiederherstellen
        if (mainController != null) {
            mainController.SerialWriteLine("d02" + ioModel.getButtonsAsHex());
        }
    }

    private Button getButton(int index) {
        return switch (index) {
            case 0 -> button0;
            case 1 -> button1;
            case 2 -> button2;
            case 3 -> button3;
            default -> throw new IllegalArgumentException("Ungültiger Button-Index: " + index);
        };
    }

    /**
     * Falls du einen MainController oder ScreenManager brauchst, kannst du ihn
     * nach dem FXML-Load „injecten“:
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Hilfsmethoden, um an den Serial-Port zu schreiben:

    private void sendSwitches() {
        // Schicke "d01" + hex
        if (mainController != null) {
            mainController.SerialWriteLine("d01" + ioModel.getSwitchesAsHex());
        }
    }

    private void toggleButton(int index) {
        ioModel.setButton(index, !ioModel.getButton(index));
        if (mainController != null) {
            mainController.SerialWriteLine("d02" + ioModel.getButtonsAsHex());
        }
    }

    private void sendScale0() {
        if (mainController != null) {
            mainController.SerialWriteLine("d0a" + ioModel.getScale0AsHex());
        }
    }

    private void sendScale1() {
        if (mainController != null) {
            mainController.SerialWriteLine("d0b" + ioModel.getScale1AsHex());
        }
    }

    // -------------------------------------------------------------------------------------------
    // Wenn du weiterhin "handleIncomingData" für diesen Screen brauchst, kannst du einfach
    // eine öffentliche Methode definieren (analog zum alten Interface):
    // -------------------------------------------------------------------------------------------
    public void handleIncomingData(String line) {
        // z.B. "d2abc1234" => substring(2)
        String payload = line.substring(2);
        System.out.println("IOViewController empfängt: " + payload);

        ioModel.setLedsFromHex(payload);
        // etc.
    }

    // Getter, falls von außen jemand das IOModel braucht:
    public IOModel getIoModel() {
        return ioModel;
    }
}
