package io.simulation.screens;

import javafx.beans.property.*;
import java.util.HashMap;
import java.util.Map;

public class AlarmclockViewModel {

    private final StringProperty raw = new SimpleStringProperty("");
    private final StringProperty hoursTens = new SimpleStringProperty("0");
    private final StringProperty hoursOnes = new SimpleStringProperty("0");
    private final StringProperty minutesTens = new SimpleStringProperty("0");
    private final StringProperty minutesOnes = new SimpleStringProperty("0");
    private final BooleanProperty alarmActive = new SimpleBooleanProperty(false);
    private final BooleanProperty beepActive = new SimpleBooleanProperty(false);
    private final BooleanProperty colonOn = new SimpleBooleanProperty(false);

    private final Map<String, Character> segMap = new HashMap<>();

    public AlarmclockViewModel() {
        // Mapping wie im C#-Beispiel
        segMap.put("3f", '0');
        segMap.put("06", '1');
        segMap.put("5b", '2');
        segMap.put("4f", '3');
        segMap.put("66", '4');
        segMap.put("6d", '5');
        segMap.put("7d", '6');
        segMap.put("07", '7');
        segMap.put("7f", '8');
        segMap.put("6f", '9');

        // Bei Änderung des Raw-Strings die Anzeige aktualisieren
        raw.addListener((obs, oldVal, newVal) -> updateProperties());
    }

    public StringProperty rawProperty() { return raw; }
    public void setRaw(String value) { raw.set(value); }
    public String getRaw() { return raw.get(); }

    public StringProperty hoursTensProperty() { return hoursTens; }
    public StringProperty hoursOnesProperty() { return hoursOnes; }
    public StringProperty minutesTensProperty() { return minutesTens; }
    public StringProperty minutesOnesProperty() { return minutesOnes; }
    public BooleanProperty alarmActiveProperty() { return alarmActive; }
    public BooleanProperty beepActiveProperty() { return beepActive; }
    public BooleanProperty colonOnProperty() { return colonOn; }

    private void updateProperties() {
        String r = getRaw();
        if (r == null || r.length() < 8) {
            hoursTens.set(" ");
            hoursOnes.set(" ");
            minutesTens.set(" ");
            minutesOnes.set(" ");
            alarmActive.set(false);
            beepActive.set(false);
            colonOn.set(false);
            return;
        }

        try {
            // Die 4 Bytes aus dem Hex-String
            byte byte3 = (byte) Integer.parseInt(r.substring(0, 2), 16);
            byte byte2 = (byte) Integer.parseInt(r.substring(2, 4), 16);
            byte byte1 = (byte) Integer.parseInt(r.substring(4, 6), 16);
            byte byte0 = (byte) Integer.parseInt(r.substring(6, 8), 16);

            hoursTens.set(String.valueOf(decodeDigit(byte3)));
            hoursOnes.set(String.valueOf(decodeDigit(byte2)));
            minutesTens.set(String.valueOf(decodeDigit(byte1)));
            minutesOnes.set(String.valueOf(decodeDigit(byte0)));

            // Bit 7 von Byte2 = ALARM, Bit 7 von Byte0 = BEEP, Bit 7 von Byte1 = Doppelpunkt
            alarmActive.set((byte2 & 0x80) != 0);
            beepActive.set((byte0 & 0x80) != 0);
            colonOn.set((byte1 & 0x80) != 0);
        } catch (Exception ex) {
            hoursTens.set(" ");
            hoursOnes.set(" ");
            minutesTens.set(" ");
            minutesOnes.set(" ");
            alarmActive.set(false);
            beepActive.set(false);
            colonOn.set(false);
        }
    }

    private char decodeDigit(byte b) {
        byte value = (byte) (b & 0x7F); // Bit 7 entfernen
        String hexStr = String.format("%02x", value).toLowerCase();
        return segMap.getOrDefault(hexStr, '?');
    }

    // Hilfsmethode, um den Hex-String zu setzen (z. B. vom Controller aufgerufen)
    public void setHexString(String hexData) {
        setRaw(hexData);
    }
}
