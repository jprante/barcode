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
 * Renders symbologies to EPS (Encapsulated PostScript).
 */
public class PostScriptRenderer implements SymbolRenderer {

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
    private final Color forground;

    /**
     * Creates a new PostScript renderer.
     *
     * @param out           the output stream to render to
     * @param scale the magnification factor to apply
     * @param background         the paper (background) color
     * @param forground           the ink (foreground) color
     */
    public PostScriptRenderer(OutputStream out, double scale, Color background, Color forground) {
        this.out = out;
        this.scale = scale;
        this.background = background;
        this.forground = forground;
    }

    @Override
    public void render(Symbol symbol) throws IOException {

        // All y dimensions are reversed because EPS origin (0,0) is at the bottom left, not top left

        String content = symbol.getContent();
        int width = (int) (symbol.getWidth() * scale);
        int height = (int) (symbol.getHeight() * scale);
        int marginX = (int) (symbol.getQuietZoneHorizontal() * scale);
        int marginY = (int) (symbol.getQuietZoneVertical() * scale);

        String title;
        if (content == null || content.isEmpty()) {
            title = "OkapiBarcode Generated Symbol";
        } else {
            title = content;
        }

        try (ExtendedOutputStreamWriter writer = new ExtendedOutputStreamWriter(out, "%.2f")) {

            // Header
            writer.append("%!PS-Adobe-3.0 EPSF-3.0\n");
            writer.append("%%Creator: OkapiBarcode\n");
            writer.append("%%Title: ").append(title).append('\n');
            writer.append("%%Pages: 0\n");
            writer.append("%%BoundingBox: 0 0 ").appendInt(width).append(" ").appendInt(height).append("\n");
            writer.append("%%EndComments\n");

            // Definitions
            writer.append("/TL { setlinewidth moveto lineto stroke } bind def\n");
            writer.append("/TC { moveto 0 360 arc 360 0 arcn fill } bind def\n");
            writer.append("/TH { 0 setlinewidth moveto lineto lineto lineto lineto lineto closepath fill } bind def\n");
            writer.append("/TB { 2 copy } bind def\n");
            writer.append("/TR { newpath 4 1 roll exch moveto 1 index 0 rlineto 0 exch rlineto neg 0 rlineto closepath fill } bind def\n");
            writer.append("/TE { pop pop } bind def\n");

            // Background
            writer.append("newpath\n");
            writer.append(forground.getRed() / 255.0).append(" ")
                    .append(forground.getGreen() / 255.0).append(" ")
                    .append(forground.getBlue() / 255.0).append(" setrgbcolor\n");
            writer.append(background.getRed() / 255.0).append(" ")
                    .append(background.getGreen() / 255.0).append(" ")
                    .append(background.getBlue() / 255.0).append(" setrgbcolor\n");
            writer.append(height).append(" 0.00 TB 0.00 ").append(width).append(" TR\n");

            // Rectangles
            for (int i = 0; i < symbol.rectangles.size(); i++) {
                Rectangle2D.Double rect = symbol.rectangles.get(i);
                if (i == 0) {
                    writer.append("TE\n");
                    writer.append(forground.getRed() / 255.0).append(" ")
                            .append(forground.getGreen() / 255.0).append(" ")
                            .append(forground.getBlue() / 255.0).append(" setrgbcolor\n");
                    writer.append(rect.height * scale).append(" ")
                            .append(height - ((rect.y + rect.height) * scale) - marginY).append(" TB ")
                            .append((rect.x * scale) + marginX).append(" ")
                            .append(rect.width * scale).append(" TR\n");
                } else {
                    Rectangle2D.Double prev = symbol.rectangles.get(i - 1);
                    if (notRoughlyEqual(rect.height, prev.height) || notRoughlyEqual(rect.y, prev.y)) {
                        writer.append("TE\n");
                        writer.append(forground.getRed() / 255.0).append(" ")
                                .append(forground.getGreen() / 255.0).append(" ")
                                .append(forground.getBlue() / 255.0).append(" setrgbcolor\n");
                        writer.append(rect.height * scale).append(" ")
                                .append(height - ((rect.y + rect.height) * scale) - marginY).append(" ");
                    }
                    writer.append("TB ").append((rect.x * scale) + marginX).append(" ").append(rect.width * scale).append(" TR\n");
                }
            }

            // Text
            for (int i = 0; i < symbol.texts.size(); i++) {
                TextBox text = symbol.texts.get(i);
                if (i == 0) {
                    writer.append("TE\n");
                    ;
                    writer.append(forground.getRed() / 255.0).append(" ")
                            .append(forground.getGreen() / 255.0).append(" ")
                            .append(forground.getBlue() / 255.0).append(" setrgbcolor\n");
                }
                writer.append("matrix currentmatrix\n");
                writer.append("/").append(symbol.getFontName()).append(" findfont\n");
                writer.append(symbol.getFontSize() * scale).append(" scalefont setfont\n");
                writer.append(" 0 0 moveto ").append((text.x * scale) + marginX).append(" ")
                        .append(height - (text.y * scale) - marginY).append(" translate 0.00 rotate 0 0 moveto\n");
                writer.append(" (").append(text.text).append(") stringwidth\n");
                writer.append("pop\n");
                writer.append("-2 div 0 rmoveto\n");
                writer.append(" (").append(text.text).append(") show\n");
                writer.append("setmatrix\n");
            }

            // Circles
            // Because MaxiCode size is fixed, this ignores magnification
            for (int i = 0; i < symbol.target.size(); i += 2) {
                Ellipse2D.Double ellipse1 = symbol.target.get(i);
                Ellipse2D.Double ellipse2 = symbol.target.get(i + 1);
                if (i == 0) {
                    writer.append("TE\n");
                    writer.append(forground.getRed() / 255.0).append(" ")
                            .append(forground.getGreen() / 255.0).append(" ")
                            .append(forground.getBlue() / 255.0).append(" setrgbcolor\n");
                    writer.append(forground.getRed() / 255.0).append(" ")
                            .append(forground.getGreen() / 255.0).append(" ")
                            .append(forground.getBlue() / 255.0).append(" setrgbcolor\n");
                }
                double x1 = ellipse1.x + (ellipse1.width / 2);
                double x2 = ellipse2.x + (ellipse2.width / 2);
                double y1 = height - ellipse1.y - (ellipse1.width / 2);
                double y2 = height - ellipse2.y - (ellipse2.width / 2);
                double r1 = ellipse1.width / 2;
                double r2 = ellipse2.width / 2;
                writer.append(x1 + marginX)
                        .append(" ").append(y1 - marginY)
                        .append(" ").append(r1)
                        .append(" ").append(x2 + marginX)
                        .append(" ").append(y2 - marginY)
                        .append(" ").append(r2)
                        .append(" ").append(x2 + r2 + marginX)
                        .append(" ").append(y2 - marginY)
                        .append(" TC\n");
            }

            // Hexagons
            // Because MaxiCode size is fixed, this ignores magnification
            for (int i = 0; i < symbol.hexagons.size(); i++) {
                Hexagon hexagon = symbol.hexagons.get(i);
                for (int j = 0; j < 6; j++) {
                    writer.append(hexagon.pointX[j] + marginX).append(" ").append((height - hexagon.pointY[j]) - marginY).append(" ");
                }
                writer.append(" TH\n");
            }

            // Footer
            writer.append("\nshowpage\n");
        }
    }

    private static boolean notRoughlyEqual(double d1, double d2) {
        return !(Math.abs(d1 - d2) < 0.0001);
    }
}
