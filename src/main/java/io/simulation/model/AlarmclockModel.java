package io.simulation.model;

import java.util.HashMap;
import java.util.Map;

public class AlarmclockModel {

    // Reine Felder (kein JavaFX-Code)
    private String raw = "";
    private String hoursTens = "0";
    private String hoursOnes = "0";
    private String minutesTens = "0";
    private String minutesOnes = "0";
    private boolean alarmActive = false;
    private boolean beepActive = false;
    private boolean colonOn = false;

    // Mapping von Segment-Codes zu Ziffern
    private final Map<String, Character> segMap = new HashMap<>();

    public AlarmclockModel() {
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
    }

    // Getter
    public String getRaw() {
        return raw;
    }

    public String getHoursTens() {
        return hoursTens;
    }

    public String getHoursOnes() {
        return hoursOnes;
    }

    public String getMinutesTens() {
        return minutesTens;
    }

    public String getMinutesOnes() {
        return minutesOnes;
    }

    public boolean isAlarmActive() {
        return alarmActive;
    }

    public boolean isBeepActive() {
        return beepActive;
    }

    public boolean isColonOn() {
        return colonOn;
    }



    /**
     * Aktualisiert die internen Felder (Ziffern und Flags) anhand des raw-Strings.
     *
     * Standard-MVVM-Lösung ging davon aus:
     * - Byte 3 (raw[0..2]) → hoursTens
     * - Byte 2 (raw[2..4]) → hoursOnes
     * - Byte 1 (raw[4..6]) → minutesTens
     * - Byte 0 (raw[6..8]) → minutesOnes
     *
     * Um "00:71" (statt "00:17") zu vermeiden und die Flags richtig zuzuordnen,
     * tauschen wir hier die Minuten-Ziffern und auch die Flags entsprechend:
     * - Minuten-Tens: aus Byte 0
     * - Minuten-Ones: aus Byte 1
     * - Beep-Flag: von Byte 1
     * - Colon-Flag: von Byte 0
     * - Alarm bleibt von Byte 2.
     */
    // Schnittstelle, damit der Controller den Hex-String setzen kann
    public void setHexString(String hexData) {
        try {
            // Lese 4 Bytes aus dem Hex-String
            byte b3 = (byte) Integer.parseInt(hexData.substring(0, 2), 16);
            byte b2 = (byte) Integer.parseInt(hexData.substring(2, 4), 16);
            byte b1 = (byte) Integer.parseInt(hexData.substring(4, 6), 16);
            byte b0 = (byte) Integer.parseInt(hexData.substring(6, 8), 16);

            // Beibehalten der Stunden-Ziffern
            hoursTens = String.valueOf(decodeDigit(b3));
            hoursOnes = String.valueOf(decodeDigit(b2));
            // Minuten: Tausche hier die Reihenfolge, um "17" statt "71" zu erhalten
            minutesTens = String.valueOf(decodeDigit(b1));
            minutesOnes = String.valueOf(decodeDigit(b0));

            // Flag-Zuordnung anpassen:
            // Ursprünglich: alarmActive aus b2, beepActive aus b0, colonOn aus b1
            // Jetzt: Alarm bleibt aus b2,
            //       Beep soll aus b1 kommen (statt b0),
            //       und Colon aus b0 (statt b1).
            alarmActive = (b2 & 0x80) != 0;
            beepActive  = (b0 & 0x80) != 0;
            colonOn     = (b1 & 0x80) != 0;
        } catch (Exception ex) {
            clearProperties();
        }
    }


    private void clearProperties() {
        hoursTens = " ";
        hoursOnes = " ";
        minutesTens = " ";
        minutesOnes = " ";
        alarmActive = false;
        beepActive = false;
        colonOn = false;
    }

    private char decodeDigit(byte b) {
        byte value = (byte) (b & 0x7F); // Entferne Bit 7
        String hexStr = String.format("%02x", value).toLowerCase();
        return segMap.getOrDefault(hexStr, '?');
    }
}
