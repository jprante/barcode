package org.xbib.graphics.barcode.output;

import org.junit.Test;
import org.xbib.graphics.barcode.Code3Of9;
import org.xbib.graphics.barcode.HumanReadableLocation;

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
    public void createBarcode1() throws IOException {
        Code3Of9 code3Of9 = new Code3Of9();
        code3Of9.setContent("20180123456");
        code3Of9.setHumanReadableLocation(HumanReadableLocation.BOTTOM);
        // pixels = (mm * dpi) / 25.4
        double scalingFactor = (1.0d * 72.0d) / 25.4;
        int width = (int) (code3Of9.getWidth() * scalingFactor);
        int height = (int) (code3Of9.getHeight() * scalingFactor);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        Java2DRenderer renderer = new Java2DRenderer(g2d, scalingFactor, Color.WHITE, Color.BLACK, false);
        renderer.render(code3Of9);
        g2d.dispose();
        OutputStream outputStream = Files.newOutputStream(Paths.get("build/barcode1.png"));
        ImageIO.write(bufferedImage, "png", outputStream);
        outputStream.close();
    }

    @Test
    public void createBarcode2() throws IOException {
        int width = 512;
        int height = 150;
        Code3Of9 code3Of9 = new Code3Of9();
        //code3Of9.setContent("20180123456");
        code3Of9.setContent("11111111111");
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        Java2DRenderer renderer = new Java2DRenderer(g2d, 3 * 1.0d, Color.WHITE, Color.BLACK, false);
        renderer.render(code3Of9);
        g2d.dispose();
        OutputStream outputStream = Files.newOutputStream(Paths.get("build/barcode2.png"));
        ImageIO.write(bufferedImage, "png", outputStream);
        outputStream.close();
    }
}
