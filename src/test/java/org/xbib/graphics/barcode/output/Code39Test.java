package org.xbib.graphics.barcode.output;

import org.junit.Test;
import org.xbib.graphics.barcode.Code3Of9;
import org.xbib.graphics.barcode.HumanReadableLocation;
import org.xbib.graphics.barcode.Symbol;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Code39Test {

    @Test
    public void createCode39() throws IOException {
        Code3Of9 code3Of9 = new Code3Of9();
        code3Of9.setContent("20180123456");
        code3Of9.setHumanReadableLocation(HumanReadableLocation.BOTTOM);
        BufferedImage bufferedImage = draw(code3Of9, 1.0d, 72.0d);
        if (bufferedImage != null) {
            OutputStream outputStream = Files.newOutputStream(Paths.get("build/barcode.png"));
            ImageIO.write(bufferedImage, "png", outputStream);
            outputStream.close();
        }
    }

    private static BufferedImage draw(Symbol symbol, double millimeter, double dpi) {
        // pixels = (mm * dpi) / 25.4
        double scalingFactor = (millimeter * dpi) / 25.4;
        int width = (int) (symbol.getWidth() * scalingFactor);
        int height = (int) (symbol.getHeight() * scalingFactor);
        if (width > 0 && height > 0) {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g2d = img.createGraphics();
            g2d.setPaint(Color.WHITE);
            g2d.fillRect(0, 0, width, height);
            Java2DRenderer renderer = new Java2DRenderer(g2d, scalingFactor, Color.WHITE, Color.BLACK, false);
            renderer.render(symbol);
            g2d.dispose();
            return img;
        }
        return null;
    }

}
