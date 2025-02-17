package io.simulation.controller;

import com.fazecast.jSerialComm.SerialPort;
import io.simulation.model.MainModel;
import io.simulation.service.SerialService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController {

    // FXML-gebundene UI-Knoten aus MainView.fxml
    @FXML private ComboBox<String> serialPortComboBox;
    @FXML private Button connectButton;
    @FXML private Button disconnectButton;
    @FXML private AnchorPane dynamicScreenContainer;
    @FXML private AnchorPane ioViewContainer;
    @FXML private AnchorPane logContainer;
    @FXML private AnchorPane debugContainer;
    @FXML private SplitPane leftSplitPane;

    // Services & Models
    private SerialService serialService;
    private MainModel mainModel;

    // Referenzen auf die aktuell geladenen Screen-Controller
    private IOViewController ioViewController;
    private LogViewController logViewController;
    private DebugViewController debugViewController;
    private AlarmclockViewController alarmclockViewController;
    private SeesawViewController seesawViewController;
    // ... weitere Screens bei Bedarf

    @FXML
    public void initialize() {
        // Initialisierung des Models und des SerialService
        mainModel = new MainModel();
        serialService = new SerialService();

        // Serielle Ports ermitteln und in die ComboBox füllen
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
            serialPortComboBox.getItems().add(port.getSystemPortName());
        }
        if (!serialPortComboBox.getItems().isEmpty()) {
            serialPortComboBox.getSelectionModel().selectFirst();
        }

        // Zunächst wird der IO-Screen in den vorgesehenen Container geladen
        showIOView();
        showDebugView();
        showLogView();

        // Optional: logContainer und debugContainer ebenfalls laden
        // (Hier kannst du z. B. noch weitere Views laden, wenn gewünscht)

        // Der dynamische Container wird erst dann im SplitPane angezeigt, wenn er benötigt wird
        leftSplitPane.getItems().remove(dynamicScreenContainer);
    }

    // ============================
    // Methoden zum Laden von Screens (direkt per FXML)
    // ============================

    /**
     * Lädt den IO-Screen in den ioViewContainer.
     */
    public void showIOView() {
        ioViewController = loadFXMLInto("/view/IOView.fxml", ioViewContainer);
        if (ioViewController != null) {
            ioViewController.setMainController(this);
        }
    }

    public void showLogView() {
        logViewController = loadFXMLInto("/view/LogView.fxml", logContainer);
    }

    public void showDebugView() {
        debugViewController = loadFXMLInto("/view/DebugView.fxml", debugContainer);
    }

    /**
     * Lädt den Alarmclock-Screen in den dynamicScreenContainer.
     */
    public void showAlarmclockView() {
        alarmclockViewController = loadFXMLInto("/view/AlarmclockView.fxml", dynamicScreenContainer);
        if (alarmclockViewController != null) {
            alarmclockViewController.setMainController(this);
            addDynamicContainerIfMissing();
        }
    }

    /**
     * Lädt den Seesaw-Screen in den dynamicScreenContainer.
     */
    public void showSeesawView() {
        seesawViewController = loadFXMLInto("/view/SeesawView.fxml", dynamicScreenContainer);
        if (seesawViewController != null) {
            seesawViewController.setMainController(this);
            addDynamicContainerIfMissing();
        }
    }

    /**
     * Hilfsmethode, um den dynamicScreenContainer in den leftSplitPane einzufügen,
     * falls er noch nicht vorhanden ist.
     */
    private void addDynamicContainerIfMissing() {
        if (!leftSplitPane.getItems().contains(dynamicScreenContainer)) {
            leftSplitPane.getItems().add(0, dynamicScreenContainer);
        }
    }

    /**
     * Allgemeine Methode, die ein FXML lädt und dessen Root in den übergebenen AnchorPane einfügt.
     * Gibt den Controller des geladenen FXMLs zurück.
     *
     * @param fxmlPath  Pfad zur FXML-Datei (z. B. "/view/IOView.fxml")
     * @param container Der AnchorPane-Container, in den das geladene Root-Element eingefügt werden soll
     * @return Der Controller der geladenen FXML, oder null bei Fehlern.
     */
    private <T> T loadFXMLInto(String fxmlPath, AnchorPane container) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            container.getChildren().clear();
            container.getChildren().add(root);
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Entfernt den dynamischen Screen, indem der dynamicScreenContainer geleert
     * und aus dem SplitPane entfernt wird.
     */
    public void unloadDynamicScreen() {
        dynamicScreenContainer.getChildren().clear();
        leftSplitPane.getItems().remove(dynamicScreenContainer);
    }

    // ============================
    // Serielle Verbindung
    // ============================

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

                // Starte das Lesen des seriellen Ports und leite die Nachrichten an parseAndDispatch weiter
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

    // ============================
    // Dynamisches Umschalten & Dispatch
    // ============================

    /**
     * Diese Methode verarbeitet eingehende Nachrichten und leitet sie
     * an den entsprechenden Screen-Controller weiter.
     *
     * Das Protokoll erwartet:
     * - Erstes Zeichen: Befehlstyp ('d' = Setter, '?' = Request, ...)
     * - Zweites Zeichen: Screen-ID
     * Beispiel: "d2ABCD" = Setter an Screen 2 mit Payload "ABCD"
     *
     * @param line die eingehende Nachricht
     */
    private void parseAndDispatch(String line) {
        if (line.isEmpty()) return;
        System.out.println("Empfangen: " + line);

        char prefix = line.charAt(0);
        if (line.length() < 2) {
            System.out.println("Unvollständige Nachricht: " + line);
            return;
        }
        char screenChar = line.charAt(1);
        String screenId = String.valueOf(screenChar);

        switch (prefix) {
            case 'd': // Setter
                String data = line.substring(1);
                if (data.startsWith("S")) {
                    // Dynamischer Screen-Befehl: S0 = dynamic screen unload, Sx = laden
                    if (data.substring(1).equals("0")) {
                        unloadDynamicScreen();
                        break;
                    }
                    loadDynamicScreen(data.substring(1));
                } else {
                    // Dispatch an statische Screens anhand der Screen-ID
                    switch (screenId) {
                        case "0":
                            if (ioViewController != null) {
                                ioViewController.handleIncomingData(line);
                            }
                            break;
                        case "1":
                            if (alarmclockViewController != null) {
                                alarmclockViewController.handleIncomingData(line.substring(2));
                            }
                            break;
                        case "2":
                            if (seesawViewController != null) {
                                seesawViewController.handleIncomingData(line);
                            }
                            break;
                        case "D":
                            if (debugViewController != null) {
                                debugViewController.handleIncomingData(line);
                            }
                            break;
                        case "L":
                            if (logViewController != null) {
                                logViewController.handleIncomingData(line);
                            }
                            break;
                        default:
                            System.out.println("Unbekannte Screen-ID: " + screenId + " => " + line);
                    }
                }
                break;
            case '?': // Request
                // Hier kannst du die Logik für Anfragen einbauen
                break;
            default:
                System.out.println("Unbekanntes Kommando: " + prefix + " / " + line);
        }
    }

    /**
     * Lädt basierend auf der übergebenen Screen-ID einen dynamischen Screen
     * in den dynamicScreenContainer.
     *
     * @param screenId die ID des dynamischen Screens (z. B. "1" für Alarmclock, "2" für Seesaw)
     */
    public void loadDynamicScreen(String screenId) {
        addDynamicContainerIfMissing();
        switch (screenId) {
            case "1":
                showAlarmclockView();
                break;
            case "2":
                showSeesawView();
                break;
            default:
                System.out.println("Dynamischer Screen für ID " + screenId + " nicht definiert.");
        }
    }

    // ============================
    // Methoden für Aufrufe aus den Child-Controllern
    // ============================

    /**
     * Wird von den Child-Controllern aufgerufen, um Nachrichten über den SerialPort zu senden.
     *
     * @param msg die Nachricht, die gesendet werden soll
     */
    public void SerialWriteLine(String msg) {
        serialService.write(msg + "\n");
    }

    public void SerialWrite(String msg) {
        serialService.write(msg);
    }
}
