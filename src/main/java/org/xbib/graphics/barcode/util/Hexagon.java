package org.xbib.graphics.barcode.util;

/**
 * Calculate a set of points to make a hexagon.
 */
public class Hexagon {

    private static final double INK_SPREAD = 1.25;

    private static final double[] OFFSET_X = {0.0, 0.86, 0.86, 0.0, -0.86, -0.86};

    private static final double[] OFFSET_Y = {1.0, 0.5, -0.5, -1.0, -0.5, 0.5};

    public final double[] pointX = new double[6];
    public final double[] pointY = new double[6];

    public Hexagon(double centreX, double centreY) {
        for (int i = 0; i < 6; i++) {
            pointX[i] = centreX + (OFFSET_X[i] * INK_SPREAD);
            pointY[i] = centreY + (OFFSET_Y[i] * INK_SPREAD);
        }
    }
}
