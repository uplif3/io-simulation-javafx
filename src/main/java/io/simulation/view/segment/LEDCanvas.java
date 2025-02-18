package io.simulation.view.segment;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LEDCanvas extends Canvas {
    private boolean isOn = false;
    private final double radius;

    public LEDCanvas(double radius) {
        super(radius * 2, radius * 2);
        this.radius = radius;
        drawLED();
    }

    public void setOn(boolean state) {
        this.isOn = state;
        drawLED();
    }

    public boolean isOn() {
        return isOn;
    }

    private void drawLED() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        Color fillColor = isOn ? Color.GREEN : Color.DARKGREEN;
        gc.setFill(fillColor);
        gc.fillOval(0, 0, getWidth(), getHeight());

        gc.setStroke(Color.BLACK);
        gc.strokeOval(0, 0, getWidth(), getHeight());

        // Simulierte Leuchteffekte für eingeschaltete LED
        if (isOn) {
            gc.setFill(Color.rgb(50, 255, 50, 0.5)); // Grüner Glow-Effekt
            gc.fillOval(radius * 0.2, radius * 0.2, radius * 1.6, radius * 1.6);
        }
    }
}

