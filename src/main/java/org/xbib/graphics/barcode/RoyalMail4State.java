package org.xbib.graphics.barcode;

import java.awt.geom.Rectangle2D;
import java.util.Locale;

/**
 * Encodes data according to the Royal Mail 4-State Country Code
 * Data input can consist of numbers 0-9 and letters A-Z and usually includes
 * delivery postcode followed by house number. A check digit is calculated
 * and added.
 */
public class RoyalMail4State extends Symbol {
    /* Handles the 4 State barcodes used in the UK by Royal Mail */

    private String[] RoyalTable = {
            "TTFF", "TDAF", "TDFA", "DTAF", "DTFA", "DDAA", "TADF", "TFTF", "TFDA",
            "DATF", "DADA", "DFTA", "TAFD", "TFAD", "TFFT", "DAAD", "DAFT", "DFAT",
            "ATDF", "ADTF", "ADDA", "FTTF", "FTDA", "FDTA", "ATFD", "ADAD", "ADFT",
            "FTAD", "FTFT", "FDAT", "AADD", "AFTD", "AFDT", "FATD", "FADT", "FFTT"
    };

    private char[] krSet = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    @Override
    public boolean encode() {
        StringBuilder dest;
        int i, top = 0, bottom = 0;
        int row, column;
        int index;

        content = content.toUpperCase(Locale.ENGLISH);
        if (!(content.matches("[0-9A-Z]+"))) {
            errorMsg.append("Invalid characters in data");
            return false;
        }
        dest = new StringBuilder("A");

        for (i = 0; i < content.length(); i++) {
            index = positionOf(content.charAt(i), krSet);
            dest.append(RoyalTable[index]);
            top += (index + 1) % 6;
            bottom += ((index / 6) + 1) % 6;
        }

        /* calculate check digit */
        row = (top % 6) - 1;
        column = (bottom % 6) - 1;
        if (row == -1) {
            row = 5;
        }
        if (column == -1) {
            column = 5;
        }

        dest.append(RoyalTable[(6 * row) + column]);

        encodeInfo.append("Check Digit: ").append((6 * row) + column).append("\n");

        /* Stop character */
        dest.append("F");

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
                    h = 5;
                    break;
                case 'D':
                    y = 3;
                    h = 5;
                    break;
                case 'F':
                    y = 0;
                    h = 8;
                    break;
                case 'T':
                    y = 3;
                    h = 2;
                    break;
            }

            Rectangle2D.Double rect = new Rectangle2D.Double(x, y, w, h);
            rectangles.add(rect);

            x += 2;
        }
        symbolWidth = pattern[0].length() * 3;
        symbolHeight = 8;
    }
}
