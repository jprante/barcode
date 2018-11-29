package org.xbib.graphics.barcode.output;

import org.xbib.graphics.barcode.HumanReadableLocation;
import org.xbib.graphics.barcode.util.Hexagon;
import org.xbib.graphics.barcode.Symbol;
import org.xbib.graphics.barcode.util.TextBox;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Renders symbologies using the Java 2D API.
 */
public class Java2DRenderer implements SymbolRenderer {

    /**
     * The graphics to render to.
     */
    private final Graphics2D g2d;

    /**
     * The scaling factor.
     */
    private final double scalingFactor;

    /**
     * The paper (background) color.
     */
    private final Color paper;

    /**
     * The ink (foreground) color.
     */
    private final Color ink;

    private final boolean antialias;

    /**
     * Creates a new Java 2D renderer.
     *
     * @param g2d           the graphics to render to
     * @param scalingFactor the scaling factor to apply
     * @param paper         the paper (background) color
     * @param ink           the ink (foreground) color
     * @param antialias if true give anti alias hint
     */
    public Java2DRenderer(Graphics2D g2d, double scalingFactor,
                          Color paper, Color ink, boolean antialias) {
        this.g2d = g2d;
        this.scalingFactor = scalingFactor;
        this.paper = paper;
        this.ink = ink;
        this.antialias = antialias;
    }

    @Override
    public void render(Symbol symbol) {

        RenderingHints renderingHints = g2d.getRenderingHints();
        if (antialias) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        int marginX = (int) (symbol.getQuietZoneHorizontal() * scalingFactor);
        int marginY = (int) (symbol.getQuietZoneVertical() * scalingFactor);

        Color oldColor = g2d.getColor();
        g2d.setColor(ink);

        for (Rectangle2D.Double rect : symbol.rectangles) {
            double x = (rect.x * scalingFactor) + marginX;
            double y = (rect.y * scalingFactor) + marginY;
            double w = rect.width * scalingFactor;
            double h = rect.height * scalingFactor;
            g2d.fill(new Rectangle((int) x, (int) y, (int) w, (int) h));
        }

        if (symbol.getHumanReadableLocation() != HumanReadableLocation.NONE) {
            Map<TextAttribute, Object> attributes = new HashMap<>();
            attributes.put(TextAttribute.TRACKING, 0);
            Font f = new Font(symbol.getFontName(), Font.PLAIN, (int) (symbol.getFontSize() * scalingFactor)).deriveFont(attributes);

            Font oldFont = g2d.getFont();
            g2d.setFont(f);
            FontMetrics fm = g2d.getFontMetrics();
            for (TextBox text : symbol.texts) {
                Rectangle2D bounds = fm.getStringBounds(text.text, g2d);
                float x = (float) ((text.x * scalingFactor) - (bounds.getWidth() / 2)) + marginX;
                float y = (float) (text.y * scalingFactor) + marginY;
                g2d.drawString(text.text, x, y);
            }
            g2d.setFont(oldFont);
        }

        for (Hexagon hexagon : symbol.hexagons) {
            Polygon polygon = new Polygon();
            for (int j = 0; j < 6; j++) {
                polygon.addPoint((int) ((hexagon.pointX[j] * scalingFactor) + marginX),
                        (int) ((hexagon.pointY[j] * scalingFactor) + marginY));
            }
            g2d.fill(polygon);
        }

        for (int i = 0; i < symbol.target.size(); i++) {
            Ellipse2D.Double ellipse = symbol.target.get(i);
            double x = (ellipse.x * scalingFactor) + marginX;
            double y = (ellipse.y * scalingFactor) + marginY;
            double w = (ellipse.width * scalingFactor) + marginX;
            double h = (ellipse.height * scalingFactor) + marginY;
            if ((i & 1) == 0) {
                g2d.setColor(ink);
            } else {
                g2d.setColor(paper);
            }
            g2d.fill(new Ellipse2D.Double(x, y, w, h));
        }

        g2d.setColor(oldColor);
        g2d.setRenderingHints(renderingHints);
    }
}
