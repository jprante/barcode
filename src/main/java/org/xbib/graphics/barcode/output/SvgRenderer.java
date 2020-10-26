package org.xbib.graphics.barcode.output;

import org.xbib.graphics.barcode.util.Hexagon;
import org.xbib.graphics.barcode.Symbol;
import org.xbib.graphics.barcode.util.TextBox;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Renders symbologies to SVG (Scalable Vector Graphics).
 */
public class SvgRenderer implements SymbolRenderer {

    /**
     * The output stream to render to.
     */
    private final OutputStream out;

    /**
     * The magnification factor to apply.
     */
    private final double scale;

    /**
     * The paper (background) color.
     */
    private final Color background;

    /**
     * The ink (foreground) color.
     */
    private final Color foreground;

    /**
     * Creates a new SVG renderer.
     *
     * @param out           the output stream to render to
     * @param scale the magnification factor to apply
     * @param background         the paper (background) color
     * @param foreground           the ink (foreground) color
     */
    public SvgRenderer(OutputStream out, double scale, Color background, Color foreground) {
        this.out = out;
        this.scale = scale;
        this.background = background;
        this.foreground = foreground;
    }

    @Override
    public void render(Symbol symbol) throws IOException {
        String content = symbol.getContent();
        int width = (int) (symbol.getWidth() * scale);
        int height = (int) (symbol.getHeight() * scale);
        int marginX = (int) (symbol.getQuietZoneHorizontal() * scale);
        int marginY = (int) (symbol.getQuietZoneVertical() * scale);

        String title;
        if (content == null || content.isEmpty()) {
            title = "OkapiBarcode Generated Symbol";
        } else {
            title = content.replaceAll("[\u0000-\u001f]", "");
        }

        String fgColour = String.format("%02X", foreground.getRed())
                + String.format("%02X", foreground.getGreen())
                + String.format("%02X", foreground.getBlue());

        String bgColour = String.format("%02X", background.getRed())
                + String.format("%02X", background.getGreen())
                + String.format("%02X", background.getBlue());

        try (ExtendedOutputStreamWriter writer = new ExtendedOutputStreamWriter(out, "%.2f")) {

            // Header
            writer.append("<?xml version=\"1.0\" standalone=\"no\"?>\n");
            writer.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"\n");
            writer.append("   \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n");
            writer.append("<svg width=\"").appendInt(width)
                    .append("\" height=\"").appendInt(height)
                    .append("\" version=\"1.1")
                    .append("\" xmlns=\"http://www.w3.org/2000/svg\">\n");
            writer.append("   <desc>").append(title).append("</desc>\n");
            writer.append("   <g id=\"barcode\" fill=\"#").append(fgColour).append("\">\n");
            writer.append("      <rect x=\"0\" y=\"0\" width=\"").appendInt(width)
                    .append("\" height=\"").appendInt(height)
                    .append("\" fill=\"#").append(bgColour).append("\" />\n");

            // Rectangles
            for (int i = 0; i < symbol.rectangles.size(); i++) {
                Rectangle2D.Double rect = symbol.rectangles.get(i);
                writer.append("      <rect x=\"").append((rect.x * scale) + marginX)
                        .append("\" y=\"").append((rect.y * scale) + marginY)
                        .append("\" width=\"").append(rect.width * scale)
                        .append("\" height=\"").append(rect.height * scale)
                        .append("\" />\n");
            }

            // Text
            for (int i = 0; i < symbol.texts.size(); i++) {
                TextBox text = symbol.texts.get(i);
                writer.append("      <text x=\"").append((text.x * scale) + marginX)
                        .append("\" y=\"").append((text.y * scale) + marginY)
                        .append("\" text-anchor=\"middle\"\n");
                writer.append("         font-family=\"").append(symbol.getFontName())
                        .append("\" font-size=\"").append(symbol.getFontSize() * scale)
                        .append("\" fill=\"#").append(fgColour).append("\">\n");
                writer.append("         ").append(text.text).append("\n");
                writer.append("      </text>\n");
            }

            // Circles
            for (int i = 0; i < symbol.target.size(); i++) {
                Ellipse2D.Double ellipse = symbol.target.get(i);
                String color;
                if ((i & 1) == 0) {
                    color = fgColour;
                } else {
                    color = bgColour;
                }
                writer.append("      <circle cx=\"").append(((ellipse.x + (ellipse.width / 2)) * scale) + marginX)
                        .append("\" cy=\"").append(((ellipse.y + (ellipse.width / 2)) * scale) + marginY)
                        .append("\" r=\"").append((ellipse.width / 2) * scale)
                        .append("\" fill=\"#").append(color).append("\" />\n");
            }

            // Hexagons
            for (int i = 0; i < symbol.hexagons.size(); i++) {
                Hexagon hexagon = symbol.hexagons.get(i);
                writer.append("      <path d=\"");
                for (int j = 0; j < 6; j++) {
                    if (j == 0) {
                        writer.append("M ");
                    } else {
                        writer.append("L ");
                    }
                    writer.append((hexagon.pointX[j] * scale) + marginX).append(" ")
                            .append((hexagon.pointY[j] * scale) + marginY).append(" ");
                }
                writer.append("Z\" />\n");
            }

            // Footer
            writer.append("   </g>\n");
            writer.append("</svg>\n");
        }
    }
}
