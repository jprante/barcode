package org.xbib.graphics.barcode;

import java.awt.geom.Rectangle2D;
import java.util.Locale;

/**
 * Implements Dutch Post KIX Code as used by Royal Dutch TPG Post
 * (Netherlands). Input data can consist of digits 0-9 and characters A-Z.
 * Input should be 11 characters in length. No check digit is added.
 */
public class KixCode extends Symbol {

    /* Handles Dutch Post TNT KIX symbols */
    /* The same as RM4SCC but without check digit */
    /* Specification at http://www.tntpost.nl/zakelijk/klantenservice/downloads/kIX_code/download.aspx */

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
        int i;

        content = content.toUpperCase(Locale.ENGLISH);

        if (!(content.matches("[0-9A-Z]+"))) {
            errorMsg.append("Invalid characters in data");
            return false;
        }

        dest = new StringBuilder();

        for (i = 0; i < content.length(); i++) {
            dest.append(RoyalTable[positionOf(content.charAt(i), krSet)]);
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
