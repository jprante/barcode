package org.xbib.graphics.barcode;

import org.xbib.graphics.barcode.util.TextBox;

import java.awt.geom.Rectangle2D;

/**
 * USPS Intelligent Mail Package Barcode (IMpb)<br>
 * A linear barcode based on GS1-128. Includes additional data checks.
 * Specification at https://ribbs.usps.gov/intelligentmail_package/documents/tech_guides/BarcodePackageIMSpec.pdf
 */
public class UspsPackage extends Symbol {

    @Override
    public boolean encode() {
        StringBuilder hrt;
        StringBuilder spacedHrt;
        boolean fourTwenty = false;
        int bracketCount = 0;

        if (!(content.matches("[0-9\\[\\]]+"))) {
            /* Input must be numeric only */
            errorMsg.append("Invalid IMpd data");
            return false;
        }

        if ((content.length() % 2) != 0) {
            /* Input must be even length */
            errorMsg.append("Invalid IMpd data");
            return false;
        }

        Code128 code128 = new Code128();
        code128.unsetCc();
        code128.setDataType(DataType.GS1);
        code128.setContent(content);

        if (content.length() > 4) {
            fourTwenty = ((content.charAt(1) == '4') && (content.charAt(2) == '2') &&
                    (content.charAt(3) == '0'));
        }

        hrt = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '[') {
                bracketCount++;
            }
            if (!(fourTwenty && bracketCount < 2)) {
                if ((content.charAt(i) >= '0') && (content.charAt(i) <= '9')) {
                    hrt.append(content.charAt(i));
                }
            }
        }

        spacedHrt = new StringBuilder();
        for (int i = 0; i < hrt.length(); i++) {
            spacedHrt.append(hrt.charAt(i));
            if (i % 4 == 3) {
                spacedHrt.append(" ");
            }
        }

        readable = new StringBuilder(spacedHrt.toString());
        pattern = new String[1];
        pattern[0] = code128.pattern[0];
        rowCount = 1;
        rowHeight = new int[1];
        rowHeight[0] = -1;
        plotSymbol();

        return true;
    }

    @Override
    protected void plotSymbol() {
        int xBlock;
        int x, y, w, h;
        boolean black;
        int offset = 20;
        int yoffset = 15;
        String banner = "USPS TRACKING #";

        rectangles.clear();
        texts.clear();
        y = yoffset;
        h = 0;
        black = true;
        x = 0;
        for (xBlock = 0; xBlock < pattern[0].length(); xBlock++) {
            w = pattern[0].charAt(xBlock) - '0';
            if (black) {
                if (rowHeight[0] == -1) {
                    h = defaultHeight;
                } else {
                    h = rowHeight[0];
                }
                if (w != 0 && h != 0) {
                    Rectangle2D.Double rect = new Rectangle2D.Double(x + offset, y, w, h);
                    rectangles.add(rect);
                }
                symbolWidth = x + w + (2 * offset);
            }
            black = !black;
            x += w;
        }
        symbolHeight = h + (2 * yoffset);

        // Add boundary bars
        Rectangle2D.Double topBar = new Rectangle2D.Double(0, 0, symbolWidth, 2);
        Rectangle2D.Double bottomBar = new Rectangle2D.Double(0, symbolHeight - 2, symbolWidth, 2);
        rectangles.add(topBar);
        rectangles.add(bottomBar);

        double centerX = getWidth() / 2.0;
        texts.add(new TextBox(centerX, getHeight() - 6.0, readable.toString()));
        texts.add(new TextBox(centerX, 12.0, banner));
    }
}
