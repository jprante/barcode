package org.xbib.graphics.barcode;

import java.awt.geom.Rectangle2D;

/**
 * Implements the Two-Track Pharmacode bar code symbology.
 * Pharmacode Two-Track is an alternative system to Pharmacode One-Track used
 * for the identification of pharmaceuticals. The symbology is able to encode
 * whole numbers between 4 and 64570080.
 */
public class Pharmacode2Track extends Symbol {

    @Override
    public boolean encode() {
        int i, tester = 0;
        StringBuilder inter;
        StringBuilder dest;

        if (content.length() > 8) {
            errorMsg.append("Input too long");
            return false;
        }

        if (!(content.matches("[0-9]+"))) {
            errorMsg.append("Invalid characters in data");
            return false;
        }

        for (i = 0; i < content.length(); i++) {
            tester *= 10;
            tester += Character.getNumericValue(content.charAt(i));
        }

        if ((tester < 4) || (tester > 64570080)) {
            errorMsg.append("Data out of range");
            return false;
        }

        inter = new StringBuilder();
        do {
            switch (tester % 3) {
                case 0:
                    inter.append("F");
                    tester = (tester - 3) / 3;
                    break;
                case 1:
                    inter.append("D");
                    tester = (tester - 1) / 3;
                    break;
                case 2:
                    inter.append("A");
                    tester = (tester - 2) / 3;
                    break;
            }
        }
        while (tester != 0);

        dest = new StringBuilder();
        for (i = (inter.length() - 1); i >= 0; i--) {
            dest.append(inter.charAt(i));
        }

        encodeInfo.append("Encoding: ").append(dest).append("\n");

        readable = new StringBuilder();
        pattern = new String[1];
        pattern[0] = dest.toString();
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

        rectangles.clear();
        x = 0;
        w = 1;
        y = 0;
        h = 0;
        for (xBlock = 0; xBlock < pattern[0].length(); xBlock++) {
            switch (pattern[0].charAt(xBlock)) {
                case 'A':
                    y = 0;
                    h = defaultHeight / 2;
                    break;
                case 'D':
                    y = defaultHeight / 2;
                    h = defaultHeight / 2;
                    break;
                case 'F':
                    y = 0;
                    h = defaultHeight;
                    break;
            }

            Rectangle2D.Double rect = new Rectangle2D.Double(x, y, w, h);
            rectangles.add(rect);

            x += 2;
        }
        symbolWidth = pattern[0].length() * 2;
        symbolHeight = defaultHeight;
    }
}
