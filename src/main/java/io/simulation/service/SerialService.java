package io.simulation.service;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class SerialService {

    private SerialPort serialPort;
    private ExecutorService executorService;
    private volatile boolean running;

    /**
     * Öffnet den angegebenen seriellen Port mit der angegebenen Baudrate.
     * @return true, wenn erfolgreich geöffnet
     */
    public boolean openPort(String portName, int baudRate) {
        // Falls schon offen, erstmal schließen
        closePort();

        // Port-Objekt holen
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(baudRate);

        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

        // Versuchen zu öffnen
        boolean ok = serialPort.openPort();
        if (ok) {
            System.out.println("SerialService: Port " + portName + " mit Baud " + baudRate + " geöffnet.");
        } else {
            System.err.println("SerialService: Konnte Port " + portName + " nicht öffnen!");
            serialPort = null;
        }
        return ok;
    }

    /**
     * Startet einen Thread, der kontinuierlich Daten vom seriellen Port liest
     * und Zeilen (oder komplette Nachrichten) an den dataCallback übergibt.
     *
     * @param dataCallback Methode, die pro eingehender (Text‑)Zeile aufgerufen wird
     */
    public void startReading(Consumer<String> dataCallback) {
        if (serialPort == null || !serialPort.isOpen()) {
            System.err.println("SerialService: Port ist nicht offen – kann nicht lesen!");
            return;
        }

        running = true;
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try (InputStream in = serialPort.getInputStream()) {
                StringBuilder buffer = new StringBuilder();
                int data;
                while (running && (data = in.read()) != -1) {
                    char c = (char) data;
                    if (c == '\n' || c == '\r') {
                        // komplette Zeile empfangen
                        String line = buffer.toString();
                        buffer.setLength(0);

                        if (!line.isEmpty()) {
                            // An den Callback weiterleiten:
                            // Da wir evtl. ins UI wollen, hier Platform.runLater
                            // => Der Callback kann dann selbst entscheiden
                            // ob er direkt UI updatet oder erst parse o.ä.
                            Platform.runLater(() -> dataCallback.accept(line));
                        }
                    } else {
                        // Zeichen anhängen
                        buffer.append(c);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("SerialService: Lese-Thread beendet.");
        });
    }

    /**
     * Schließt den Port und stoppt den Lese-Thread.
     */
    public void closePort() {
        running = false;
        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("SerialService: Port geschlossen.");
        }
        serialPort = null;
    }

    /**
     * Sendet einen String an den offenen Port (z.B. ein Kommando + "\n").
     */
    public void write(String message) {
        if (serialPort != null && serialPort.isOpen()) {
            byte[] bytes = message.getBytes();
            serialPort.writeBytes(bytes, bytes.length);
        } else {
            System.err.println("SerialService: Port nicht offen, kann nicht senden!");
        }
    }

    public boolean isOpen() {
        return (serialPort != null && serialPort.isOpen());
    }
}

