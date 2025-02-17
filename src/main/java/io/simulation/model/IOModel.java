package io.simulation.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class IOModel {

    public static final int NUM_LEDS = 8;
    public static final int NUM_SWITCHES = 8;
    public static final int NUM_BUTTONS = 4;

    // Arrays für die LEDs, Switches und Buttons
    private BooleanProperty[] leds = new BooleanProperty[NUM_LEDS];
    private BooleanProperty[] switches = new BooleanProperty[NUM_SWITCHES];
    private BooleanProperty[] buttons = new BooleanProperty[NUM_BUTTONS];

    private int scale0;
    private int scale1;

    public IOModel() {
        for (int i = 0; i < NUM_LEDS; i++) {
            leds[i] = new SimpleBooleanProperty(false);
        }
        for (int i = 0; i < NUM_SWITCHES; i++) {
            switches[i] = new SimpleBooleanProperty(false);
        }
        for (int i = 0; i < NUM_BUTTONS; i++) {
            buttons[i] = new SimpleBooleanProperty(false);
        }
    }

    // Getter/Setter und Property-Zugriff für LEDs
    public BooleanProperty ledProperty(int index) {
        return leds[index];
    }

    public boolean getLed(int index) {
        return leds[index].get();
    }

    public void setLed(int index, boolean value) {
        leds[index].set(value);
    }

    // Analog für Switches
    public BooleanProperty switchProperty(int index) {
        return switches[index];
    }

    public boolean getSwitch(int index) {
        return switches[index].get();
    }

    public void setSwitch(int index, boolean value) {
        switches[index].set(value);
    }

    // Und für Buttons
    public BooleanProperty buttonProperty(int index) {
        return buttons[index];
    }

    public boolean getButton(int index) {
        return buttons[index].get();
    }

    public void setButton(int index, boolean value) {
        buttons[index].set(value);
    }

    // Methoden, um den Zustand aus einem Hex-String zu setzen:
    public void setLedsFromHex(String hexVal) {
        int val = Integer.parseInt(hexVal, 16);
        for (int i = 0; i < NUM_LEDS; i++) {
            setLed(i, ((val >> i) & 1) == 1);
        }
        System.out.println("LEDs: " + String.format("%8s", Integer.toBinaryString(val)).replace(' ', '0'));
    }

    public void setSwitchesFromHex(String hexVal) {
        int val = Integer.parseInt(hexVal, 16);
        for (int i = 0; i < NUM_SWITCHES; i++) {
            setSwitch(i, ((val >> i) & 1) == 1);
        }
    }

    public void setButtonsFromHex(String hexVal) {
        int val = Integer.parseInt(hexVal, 16);
        for (int i = 0; i < NUM_BUTTONS; i++) {
            setButton(i, ((val >> i) & 1) == 1);
        }
    }

    // Methoden, um den Zustand in einen Hex-String umzuwandeln:
    public String getButtonsAsHex() {
        int val = 0;
        for (int i = 0; i < NUM_BUTTONS; i++) {
            if(getButton(i)) {
                val |= (1 << i);
            }
        }
        String res = String.format("%02x", val);
        System.out.println("Buttons: " + res);
        return res;
    }

    public String getSwitchesAsHex() {
        int val = 0;
        for (int i = 0; i < NUM_SWITCHES; i++) {
            if(getSwitch(i)) {
                val |= (1 << i);
            }
        }
        return String.format("%02x", val);
    }

    public String getScale0AsHex() {
        String res = String.format("%04x", scale0);
        System.out.println("Scale0: " + res);
        return res;
    }

    public String getScale1AsHex() {
        return String.format("%04x", scale1);
    }

    // Getter/Setter für Scale-Werte
    public int getScale0() {
        return scale0;
    }

    public void setScale0(int scale0) {
        this.scale0 = scale0;
        // Hier könntest du auch einen PropertyChangeListener benachrichtigen, falls nötig.
    }

    public int getScale1() {
        return scale1;
    }

    public void setScale1(int scale1) {
        this.scale1 = scale1;
    }
}
