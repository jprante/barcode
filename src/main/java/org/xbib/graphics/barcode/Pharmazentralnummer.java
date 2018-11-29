package org.xbib.graphics.barcode;

/**
 * PZN8 is a Code 39 based symbology used by the pharmaceutical industry in
 * Germany. PZN8 encodes a 7 digit number and includes a modulo-10 check digit.
 */
public class Pharmazentralnummer extends Symbol {

    /* Pharmazentral Nummer is a Code 3 of 9 symbol with an extra
     * check digit. Now generates PZN-8.
     */
    @Override
    public boolean encode() {
        int l = content.length();
        StringBuilder localstr;
        int zeroes, count = 0, check_digit;
        Code3Of9 c = new Code3Of9();

        if (l > 7) {
            errorMsg.append("Input data too long");
            return false;
        }

        if (!(content.matches("[0-9]+"))) {
            errorMsg.append("Invalid characters in input");
            return false;
        }

        localstr = new StringBuilder("-");
        zeroes = 7 - l + 1;
        for (int i = 1; i < zeroes; i++)
            localstr.append('0');

        localstr.append(content);

        for (int i = 1; i < 8; i++) {
            count += i * Character.getNumericValue(localstr.charAt(i));
        }

        check_digit = count % 11;
        if (check_digit == 11) {
            check_digit = 0;
        }
        if (check_digit == 10) {
            errorMsg.append("Not a valid PZN identifier");
            return false;
        }

        encodeInfo.append("Check Digit: ").append(check_digit).append("\n");

        localstr.append((char) (check_digit + '0'));

        try {
            c.setContent(localstr.toString());
        } catch (Exception e) {
            errorMsg.append(e.getMessage());
            return false;
        }

        readable = new StringBuilder("PZN").append(localstr);
        pattern = new String[1];
        pattern[0] = c.pattern[0];
        rowCount = 1;
        rowHeight = new int[1];
        rowHeight[0] = -1;
        plotSymbol();
        return true;
    }
}
