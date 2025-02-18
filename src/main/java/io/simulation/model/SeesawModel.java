package io.simulation.model;

public class SeesawModel {
    private double reference;
    private double ball;
    private double angle;
    private boolean boing;

    public double getReference() {
        return reference;
    }
    public void setReference(double reference) {
        this.reference = reference;
    }
    public double getBall() {
        return ball;
    }
    public void setBall(double ball) {
        this.ball = ball;
    }
    public double getAngle() {
        return angle;
    }
    public void setAngle(double angle) {
        this.angle = angle;
    }
    public boolean isBoing() {
        return boing;
    }
    public void setBoing(boolean boing) {
        this.boing = boing;
    }

    /**
     * Parst einen Packet-String und aktualisiert die Werte.
     *
     * Format:
     * - Die ersten 2 Zeichen werden übersprungen.
     * - Anschließend folgen:
     *     - 4 Hex-Zeichen für Reference,
     *     - 4 Hex-Zeichen für Ball,
     *     - 4 Hex-Zeichen für Angle,
     *     - 1 Zeichen für Boing (z.B. 't' für true).
     *
     * Insgesamt müssen also mindestens 15 Zeichen vorhanden sein.
     */
    public void updateFromPacket(String packet) {
        if (packet == null || packet.length() < 13) return;

        try {
            int rawReference = parseSignedHex(packet.substring(0, 4));
            int rawBall = parseSignedHex(packet.substring(4, 8));
            int rawAngle = parseSignedHex(packet.substring(8, 12));

            this.reference = rawReference / 50000.0;
            this.ball = rawBall / 50000.0;
            this.angle = rawAngle / 2000.0;
            this.boing = (packet.charAt(12) == 't');
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private int parseSignedHex(String hexStr) {
        int num = Integer.parseInt(hexStr, 16);
        if ((num & 0x8000) != 0) {
            num -= 0x10000;
        }
        return num;
    }
}
