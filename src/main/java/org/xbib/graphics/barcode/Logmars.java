package org.xbib.graphics.barcode;

/**
 * Implements the LOGMARS (Logistics Applications of Automated Marking
 * and Reading Symbols) standard used by the US Department of Defense.
 * Input data can be of any length and supports the characters 0-9, A-Z, dash
 * (-), full stop (.), space, dollar ($), slash (/), plus (+) and percent (%).
 * A Modulo-43 check digit is calculated and added, and should not form part
 * of the input data.
 */
public class Logmars extends Symbol {

    private static final String[] CODE39LM = {
            "1113313111", "3113111131", "1133111131", "3133111111", "1113311131",
            "3113311111", "1133311111", "1113113131", "3113113111", "1133113111",
            "3111131131", "1131131131", "3131131111", "1111331131", "3111331111",
            "1131331111", "1111133131", "3111133111", "1131133111", "1111333111",
            "3111111331", "1131111331", "3131111311", "1111311331", "3111311311",
            "1131311311", "1111113331", "3111113311", "1131113311", "1111313311",
            "3311111131", "1331111131", "3331111111", "1311311131", "3311311111",
            "1331311111", "1311113131", "3311113111", "1331113111", "1313131111",
            "1313111311", "1311131311", "1113131311"
    };

    private static final char[] LOOKUP = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '-', '.', ' ', '$', '/', '+',
            '%'
    };

    /**
     * Ratio of wide bar width to narrow bar width.
     */
    private double moduleWidthRatio = 3;

    /**
     * Returns the ratio of wide bar width to narrow bar width.
     *
     * @return the ratio of wide bar width to narrow bar width
     */
    public double getModuleWidthRatio() {
        return moduleWidthRatio;
    }

    /**
     * Sets the ratio of wide bar width to narrow bar width. Valid values are usually
     * between {@code 2} and {@code 3}. The default value is {@code 3}.
     *
     * @param moduleWidthRatio the ratio of wide bar width to narrow bar width
     */
    public void setModuleWidthRatio(double moduleWidthRatio) {
        this.moduleWidthRatio = moduleWidthRatio;
    }

    @Override
    protected double getModuleWidth(int originalWidth) {
        if (originalWidth == 1) {
            return 1;
        } else {
            return moduleWidthRatio;
        }
    }

    @Override
    public boolean encode() {

        if (!(content.matches("[0-9A-Z\\. \\-$/+%]+"))) {
            errorMsg.append("Invalid characters in input");
            return false;
        }

        StringBuilder p = new StringBuilder();
        int l = content.length();
        int charval, counter = 0;
        char thischar;
        char checkDigit;
        for (int i = 0; i < l; i++) {
            thischar = content.charAt(i);
            charval = positionOf(thischar, LOOKUP);
            counter += charval;
            p.append(CODE39LM[charval]);
        }

        counter = counter % 43;
        checkDigit = LOOKUP[counter];
        encodeInfo.append("Check Digit: ").append(checkDigit).append("\n");
        p.append(CODE39LM[counter]);

        readable = new StringBuilder(content).append(checkDigit);
        pattern = new String[]{"1311313111" + p + "131131311"};
        rowCount = 1;
        rowHeight = new int[]{-1};
        plotSymbol();
        return true;
    }
}
