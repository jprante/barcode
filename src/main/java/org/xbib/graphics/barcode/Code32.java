package org.xbib.graphics.barcode;

/**
 * Implements Code 32, also known as Italian Pharmacode, A variation of Code
 * 39 used by the Italian Ministry of Health ("Ministero della Sanità")
 * Requires a numeric input up to 8 digits in length. Check digit is
 * calculated.
 */
public class Code32 extends Symbol {
    private char[] tabella = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'B', 'C', 'D', 'F',
            'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z'
    };

    @Override
    public boolean encode() {
        int i, checksum, checkpart, checkdigit;
        int pharmacode, remainder, devisor;
        StringBuilder localstr;
        StringBuilder risultante;
        int[] codeword = new int[6];
        Code3Of9 c39 = new Code3Of9();

        if (content.length() > 8) {
            errorMsg.append("Input too long");
            return false;
        }

        if (!(content.matches("[0-9]+"))) {
            errorMsg.append("Invalid characters in input");
            return false;
        }

        /* Add leading zeros as required */
        localstr = new StringBuilder();
        for (i = content.length(); i < 8; i++) {
            localstr.append("0");
        }
        localstr.append(content);

        /* Calculate the check digit */
        checksum = 0;
        checkpart = 0;
        for (i = 0; i < 4; i++) {
            checkpart = Character.getNumericValue(localstr.charAt(i * 2));
            checksum += checkpart;
            checkpart = 2 * Character.getNumericValue(localstr.charAt((i * 2) + 1));
            if (checkpart >= 10) {
                checksum += (checkpart - 10) + 1;
            } else {
                checksum += checkpart;
            }
        }

        /* Add check digit to data string */
        checkdigit = checksum % 10;
        localstr.append((char) (checkdigit + '0'));
        encodeInfo.append("Check Digit: ").append((char) (checkdigit + '0'));
        encodeInfo.append('\n');

        /* Convert string into an integer value */
        pharmacode = 0;
        for (i = 0; i < localstr.length(); i++) {
            pharmacode *= 10;
            pharmacode += Character.getNumericValue(localstr.charAt(i));
        }

        /* Convert from decimal to base-32 */
        devisor = 33554432;
        for (i = 5; i >= 0; i--) {
            codeword[i] = pharmacode / devisor;
            remainder = pharmacode % devisor;
            pharmacode = remainder;
            devisor /= 32;
        }

        /* Look up values in 'Tabella di conversione' */
        risultante = new StringBuilder();
        for (i = 5; i >= 0; i--) {
            risultante.append(tabella[codeword[i]]);
        }

        /* Plot the barcode using Code 39 */

        readable = new StringBuilder("A").append(localstr);
        pattern = new String[1];
        rowCount = 1;
        rowHeight = new int[1];
        rowHeight[0] = -1;
        encodeInfo.append("Code 39 Equivalent: ").append(risultante).append('\n');
        try {
            c39.setContent(risultante.toString());
        } catch (Exception e) {
            errorMsg.append(e.getMessage());
            return false;
        }

        this.pattern[0] = c39.pattern[0];
        this.plotSymbol();
        return true;
    }
}
