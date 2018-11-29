package org.xbib.graphics.barcode;

/**
 * Implements the <a href="http://en.wikipedia.org/wiki/Pharmacode">Pharmacode</a>
 * bar code symbology.
 * Pharmacode is used for the identification of pharmaceuticals. The symbology
 * is able to encode whole numbers between 3 and 131070.
 */
public class Pharmacode extends Symbol {

    @Override
    public boolean encode() {
        int tester = 0;
        int i;

        StringBuilder inter = new StringBuilder();
        StringBuilder dest = new StringBuilder();

        if (content.length() > 6) {
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

        if ((tester < 3) || (tester > 131070)) {
            errorMsg.append("Data out of range");
            return false;
        }

        do {
            if ((tester & 1) == 0) {
                inter.append("W");
                tester = (tester - 2) / 2;
            } else {
                inter.append("N");
                tester = (tester - 1) / 2;
            }
        } while (tester != 0);

        for (i = inter.length() - 1; i >= 0; i--) {
            if (inter.charAt(i) == 'W') {
                dest.append("32");
            } else {
                dest.append("12");
            }
        }

        readable = new StringBuilder();
        pattern = new String[1];
        pattern[0] = dest.toString();
        rowCount = 1;
        rowHeight = new int[1];
        rowHeight[0] = -1;
        plotSymbol();
        return true;
    }
}
