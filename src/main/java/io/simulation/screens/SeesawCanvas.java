package io.simulation.screens;

import io.simulation.model.SeesawModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SeesawCanvas extends Canvas {

    public SeesawCanvas(double width, double height) {
        super(width, height);
    }

    public SeesawCanvas() {
        super(600, 300);
    }

    /**
     * Zeichnet die Wippe anhand der aktuellen Zustände aus dem Model.
     */
    public void redraw(SeesawModel model) {
        GraphicsContext gc = getGraphicsContext2D();
        double canvasWidth = getWidth();
        double canvasHeight = getHeight();
        // Canvas leeren
        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        double marginBottom = 10.0;
        double baseX = canvasWidth / 2;
        double baseY = canvasHeight - marginBottom;

        // Farben (analog zu C#)
        Color colorStand = Color.rgb(0x60, 0x60, 0x60);
        Color colorRamp = Color.rgb(0x00, 0x4D, 0xE6);
        Color colorBall = Color.rgb(0xF0, 0xF0, 0xF0);
        Color colorRef = Color.rgb(0xFC, 0xF8, 0x00);
        Color colorBoingOn = Color.rgb(0xCC, 0x00, 0x00);
        Color colorBoingOff = Color.rgb(0x60, 0x60, 0x60);

        double seesawWidth = 420.0;
        double standWidth = 36.0;
        double standHeight = 70.0;
        double rBall = 9.0;
        double markerWidth = 6.0;
        double markerHeight = 11.0;

        // --- Hilfsmethode: ToCanvas ---
        // ToCanvas(basePt, relX, relY) = (baseX + relX, baseY - relY)
        // --------------------------------

        // Stand (Dreieck)
        double sp1x = baseX - standWidth / 2;
        double sp1y = baseY;
        double sp2x = baseX;
        double sp2y = baseY - standHeight;
        double sp3x = baseX + standWidth / 2;
        double sp3y = baseY;
        gc.setFill(colorStand);
        gc.fillPolygon(new double[]{sp1x, sp2x, sp3x}, new double[]{sp1y, sp2y, sp3y}, 3);

        // Reference Marker
        double ref = model.getReference();
        double markerRawX = ref / 0.6 * ((seesawWidth / 2) - 1.5 * rBall);
        double markerRawY = 180.0 - 1.0;
        double m1x = baseX + (markerRawX - markerWidth);
        double m1y = baseY - markerRawY;
        double m2x = baseX + (markerRawX + markerWidth);
        double m2y = baseY - markerRawY;
        double m3x = baseX + markerRawX;
        double m3y = baseY - (markerRawY - markerHeight);
        gc.setFill(colorRef);
        gc.fillPolygon(new double[]{m1x, m2x, m3x}, new double[]{m1y, m2y, m3y}, 3);

        // Ramp-Line (Wippe)
        double angleDeg = model.getAngle();
        double angleRad = Math.toRadians(angleDeg);
        double tanAngle = Math.tan(angleRad);
        double seesawPivotY = 90.0;
        double seesawHalfWidth = seesawWidth / 2.0;
        double yOffset = tanAngle * seesawHalfWidth;
        double yLeft = seesawPivotY + yOffset;
        double yRight = seesawPivotY - yOffset;
        double rampP1x = baseX - seesawHalfWidth;
        double rampP1y = baseY - yLeft;
        double rampP2x = baseX + seesawHalfWidth;
        double rampP2y = baseY - yRight;
        gc.setStroke(colorRamp);
        gc.setLineWidth(3.0);
        gc.strokeLine(rampP1x, rampP1y, rampP2x, rampP2y);

        // Ball
        double ball = model.getBall();
        double ballX = ball / 0.6 * seesawHalfWidth;
        double ballY = seesawPivotY - tanAngle * ballX + rBall;
        double ballCenterX = baseX + ballX;
        double ballCenterY = baseY - ballY;
        gc.setFill(colorBall);
        gc.fillOval(ballCenterX - rBall, ballCenterY - rBall, rBall * 2, rBall * 2);

        // Boing-Text
        Color boingColor = model.isBoing() ? colorBoingOn : colorBoingOff;
        gc.setFill(boingColor);
        gc.setFont(new Font("Verdana", 14));
        String boingText = "Boing!";
        double textX = baseX - seesawWidth / 2 - 40;
        double textY = baseY - 10;
        gc.fillText(boingText, textX, textY);
    }
}
