package io.simulation.view.segment;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SeesawGraphCanvas extends Canvas {

    private final int GRAPH_WIDTH = 600;
    private final int GRAPH_HEIGHT = 150;
    private final int GRAPH_MAX_POINTS = 200;

    private final double[] referenceGraph = new double[GRAPH_MAX_POINTS];
    private final double[] ballGraph = new double[GRAPH_MAX_POINTS];
    private final double[] angleGraph = new double[GRAPH_MAX_POINTS];

    private int currentIndex = 0;
    private int count = 0;

    public SeesawGraphCanvas() {
        super(600, 150);
        for (int i = 0; i < GRAPH_MAX_POINTS; i++) {
            referenceGraph[i] = Double.NaN;
            ballGraph[i] = Double.NaN;
            angleGraph[i] = Double.NaN;
        }
    }

    /**
     * Aktualisiert die Graphdaten anhand der aktuellen Werte und
     * zeichnet den Graph neu.
     *
     * @param reference aktueller Reference-Wert (Bereich ca. -0.6 bis +0.6)
     * @param ball      aktueller Ball-Wert (Bereich ca. -0.6 bis +0.6)
     * @param angle     aktueller Winkel (ca. -15 bis +15 Grad)
     */
    public void updateGraphData(double reference, double ball, double angle) {
        double referenceScaled = (reference / 0.6) * (GRAPH_HEIGHT / 2.0);
        double ballScaled = (ball / 0.6) * (GRAPH_HEIGHT / 2.0);
        double angleScaled = (angle / 15.0) * (GRAPH_HEIGHT / 2.0);

        referenceGraph[currentIndex] = referenceScaled;
        ballGraph[currentIndex] = ballScaled;
        angleGraph[currentIndex] = angleScaled;

        currentIndex++;
        if (currentIndex >= GRAPH_MAX_POINTS) {
            currentIndex = 0;
        }
        if (count < GRAPH_MAX_POINTS) {
            count++;
        }

        drawGraph();
    }

    private void drawGraph() {
        GraphicsContext gc = getGraphicsContext2D();
        // Canvas leeren
        gc.clearRect(0, 0, getWidth(), getHeight());

        // Basis-Position
        double baseX = 10;
        double baseY = 10;

        // Hintergrund (schwarzes Rechteck)
        gc.setFill(Color.BLACK);
        gc.fillRect(baseX, baseY, GRAPH_WIDTH, GRAPH_HEIGHT);

        // Raster zeichnen: vertikale Linien alle 50 Pixel, horizontale alle 30 Pixel
        gc.setStroke(Color.rgb(0x40, 0x40, 0x40));
        gc.setLineWidth(1);
        // Vertikale Linien
        for (int i = 0; i <= GRAPH_WIDTH; i += 50) {
            double x = baseX + i;
            gc.strokeLine(x, baseY, x, baseY + GRAPH_HEIGHT);
        }
        // Horizontale Linien
        for (int i = 0; i <= GRAPH_HEIGHT; i += 30) {
            double y = baseY + i;
            gc.strokeLine(baseX, y, baseX + GRAPH_WIDTH, y);
        }

        // Farben für die Kurven:
        Color refColor = Color.rgb(0xFC, 0xF8, 0x00);
        Color ballColor = Color.rgb(0xF0, 0xF0, 0xF0);
        Color angleColor = Color.rgb(0x00, 0x4D, 0xE6);

        // Berechne den Abstand zwischen den einzelnen Punkten auf der X-Achse
        double xStep = (GRAPH_MAX_POINTS > 1) ? (double) GRAPH_WIDTH / (GRAPH_MAX_POINTS - 1) : GRAPH_WIDTH;

        // Zeichne die drei Kurven
        drawRingCurve(gc, referenceGraph, count, xStep, refColor, baseX, baseY);
        drawRingCurve(gc, ballGraph, count, xStep, ballColor, baseX, baseY);
        drawRingCurve(gc, angleGraph, count, xStep, angleColor, baseX, baseY);

        // Zeichne einen vertikalen Cursor an der aktuellen Position
        double cursorX = baseX + currentIndex * xStep;
        gc.setStroke(Color.ORANGE);
        gc.setLineWidth(1);
        gc.strokeLine(cursorX, baseY, cursorX, baseY + GRAPH_HEIGHT);
    }

    /**
     * Zeichnet einen Ringdiagramm‑Stil (also mit Lücken an der Stelle, an der der Ring
     * überschrieben wird) für die übergebenen Daten.
     *
     * @param gc      GraphicsContext zum Zeichnen
     * @param data    Array mit den skalieren Daten
     * @param count   Anzahl der gültigen Datenpunkte
     * @param xStep   Schrittweite in X-Richtung
     * @param color   Farbe der Linie
     * @param baseX   Basis X-Position
     * @param baseY   Basis Y-Position
     */
    private void drawRingCurve(GraphicsContext gc, double[] data, int count, double xStep, Color color, double baseX, double baseY) {
        gc.setStroke(color);
        gc.setLineWidth(2);
        int lastIndex = count - 1;
        for (int i = 0; i < lastIndex; i++) {
            int i2 = i + 1;
            // Wenn i2 gerade der aktuell überschriebenen Stelle entspricht, überspringen
            if (i2 == currentIndex) continue;

            double x1 = baseX + i * xStep;
            double x2 = baseX + i2 * xStep;
            double y1 = baseY + (GRAPH_HEIGHT / 2.0) - data[i];
            double y2 = baseY + (GRAPH_HEIGHT / 2.0) - data[i2];

            if (!Double.isNaN(y1) && !Double.isNaN(y2)) {
                gc.strokeLine(x1, y1, x2, y2);
            }
        }
    }
}
