package io.simulation;

import com.fazecast.jSerialComm.SerialPort;
import io.simulation.model.MainModel;
import io.simulation.screens.Screen;
import io.simulation.service.SerialService;
import io.simulation.screens.ScreenIO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MainController {

    @FXML
    private ComboBox<String> serialPortComboBox;
    @FXML
    private Button connectButton;
    @FXML
    private Button disconnectButton;

    // Container aus FXML für die einzelnen Bereiche
    @FXML
    private AnchorPane dynamicScreenContainer;
    @FXML
    private AnchorPane ioViewContainer;
    @FXML
    private AnchorPane logContainer;
    @FXML
    private AnchorPane debugContainer;
    @FXML
    private SplitPane leftSplitPane;

    private SerialService serialService;
    private MainModel mainModel;

    // Optional: ScreenManager, der Screens laden kann
    private ScreenManager screenManager;

    @FXML
    public void initialize() {
        this.mainModel = new MainModel();
        this.serialService = new SerialService();

        // 1) Serielle Ports auflisten und ComboBox füllen
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
            serialPortComboBox.getItems().add(port.getSystemPortName());
        }
        if (!serialPortComboBox.getItems().isEmpty()) {
            serialPortComboBox.getSelectionModel().selectFirst();
        }

        // 2) ScreenManager initialisieren
        this.screenManager = new ScreenManager(this, mainModel);

        screenManager.showScreen("0", ioViewContainer);
        screenManager.showScreen("L", logContainer);
        screenManager.showScreen("D", debugContainer);

        leftSplitPane.getItems().remove(dynamicScreenContainer);
    }

    @FXML
    public void handleConnect() {
        String selectedPort = serialPortComboBox.getValue();
        if (selectedPort != null) {
            boolean ok = serialService.openPort(selectedPort, 9600);
            if (ok) {
                System.out.println("Verbunden mit " + selectedPort);
                connectButton.setDisable(true);
                disconnectButton.setDisable(false);
                serialPortComboBox.setDisable(true);

                serialService.startReading(this::parseAndDispatch);
            } else {
                System.out.println("Fehler beim Verbinden mit " + selectedPort);
            }
        }
    }

    @FXML
    public void handleDisconnect() {
        if (serialService.isOpen()) {
            serialService.closePort();
            System.out.println("Verbindung getrennt");
            connectButton.setDisable(false);
            disconnectButton.setDisable(true);
            serialPortComboBox.setDisable(false);
        }
    }



    // Wird z. B. von ScreenManager genutzt, um den dynamischen Bereich zu leeren
    public void unloadDynamicScreen() {
        dynamicScreenContainer.getChildren().clear();
        leftSplitPane.getItems().remove(dynamicScreenContainer);
    }


    // Damit der ScreenManager den dynamischen Container erkennen kann
    public AnchorPane getDynamicScreenContainer() {
        return dynamicScreenContainer;
    }

    public void SerialWriteLine(String msg){
        serialService.write(msg + "\n");
    }

    public void loadDynamicScreen(String screenId) {
        // Prüfen, ob dynamicScreenContainer schon im SplitPane ist
        if (!leftSplitPane.getItems().contains(dynamicScreenContainer)) {
            // Oberhalb (Index 0) einfügen, sodass dynamicScreenContainer über ioViewContainer liegt
            leftSplitPane.getItems().add(0, dynamicScreenContainer);
        }
        // Anschließend den gewünschten Screen hineinladen
        screenManager.showScreen(screenId, dynamicScreenContainer);
    }


    public void SerialWrite(String msg){
        serialService.write(msg);
    }

    private void parseAndDispatch(String line) {
        if (line.isEmpty()) return;
        System.out.println(line);
        // Beispiel-Protokoll:
        // Erstes Zeichen: 'd' = Setter, '?' = Request, ...
        // Zweites Zeichen: '0'..'9' = Screen-ID
        // => d2ABCD = "Setter an Screen 2 mit Payload 'ABCD'"

        char prefix = line.charAt(0); // 'd', '?', etc.
        if (line.length() < 2) {
            // Minimallänge unterschritten
            System.out.println("Unvollständige Nachricht: " + line);
            return;
        }
        char screenChar = line.charAt(1);
        String screenId = String.valueOf(screenChar); // z. B. '2' => 2

        switch (prefix) {

            case 'd': // "setter"
                String data = line.substring(1);
                // Nur an Screen 'screenId' weiterleiten
                if(data.startsWith("S")){
                    if(data.substring(1).equals("0")){
                        unloadDynamicScreen();
                        break;
                    }
                    loadDynamicScreen(data.substring(1));
                }
                else {
                    screenManager.handleSetterMessage(screenId, line);
                }
                break;
            case '?': // "request"
                // ...
                // screenManager.handleRequesterMessage(screenId, line);
                break;
            default:
                System.out.println("Unbekanntes Kommando: " + prefix + " / " + line);
        }
    }


    // Getter/Setter etc. wenn nötig
}
