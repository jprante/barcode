package org.xbib.graphics.barcode;

/**
 * Implements Korea Post Barcode. Input should consist of of a six-digit
 * number. A Modulo-10 check digit is calculated and added, and should not form
 * part of the input data.
 */
public class KoreaPost extends Symbol {

    String[] koreaTable = {
            "1313150613", "0713131313", "0417131313", "1506131313", "0413171313",
            "17171313", "1315061313", "0413131713", "17131713", "13171713"
    };

    @Override
    public boolean encode() {
        StringBuilder accumulator = new StringBuilder();

        if (!(content.matches("[0-9]+"))) {
            errorMsg.append("Invalid characters in input");
            return false;
        }

        if (content.length() > 6) {
            errorMsg.append("Input data too long");
            return false;
        }

        StringBuilder add_zero = new StringBuilder();
        int i, j, total = 0, checkd;

        for (i = 0; i < (6 - content.length()); i++) {
            add_zero.append("0");
        }
        add_zero.append(content);


        for (i = 0; i < add_zero.length(); i++) {
            j = Character.getNumericValue(add_zero.charAt(i));
            accumulator.append(koreaTable[j]);
            total += j;
        }

        checkd = 10 - (total % 10);
        if (checkd == 10) {
            checkd = 0;
        }
        encodeInfo.append("Check Digit: ").append(checkd).append("\n");

        accumulator.append(koreaTable[checkd]);

        readable.append(add_zero).append(checkd);
        pattern = new String[1];
        pattern[0] = accumulator.toString();
        rowCount = 1;
        rowHeight = new int[1];
        rowHeight[0] = -1;
        plotSymbol();
        return true;
    }
}
