package org.xbib.graphics.barcode;

/**
 * Implements Code 39 bar code symbology according to ISO/IEC 16388:2007.
 * Input data can be of any length and supports the characters 0-9, A-Z, dash
 * (-), full stop (.), space, asterisk (*), dollar ($), slash (/), plus (+)
 * and percent (%). The standard does not require a check digit but a
 * modulo-43 check digit can be added if required.
 */
public class Code3Of9 extends Symbol {

    private static final String[] CODE_39 = {
            "1112212111", "2112111121", "1122111121", "2122111111", "1112211121",
            "2112211111", "1122211111", "1112112121", "2112112111", "1122112111",
            "2111121121", "1121121121", "2121121111", "1111221121", "2111221111",
            "1121221111", "1111122121", "2111122111", "1121122111", "1111222111",
            "2111111221", "1121111221", "2121111211", "1111211221", "2111211211",
            "1121211211", "1111112221", "2111112211", "1121112211", "1111212211",
            "2211111121", "1221111121", "2221111111", "1211211121", "2211211111",
            "1221211111", "1211112121", "2211112111", "1221112111", "1212121111",
            "1212111211", "1211121211", "1112121211"
    };
    private static final char[] LOOKUP = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '-', '.', ' ', '$', '/', '+',
            '%'
    };
    private CheckDigit checkOption = CheckDigit.NONE;
    /**
     * Ratio of wide bar width to narrow bar width.
     */
    private double moduleWidthRatio = 2;

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
     * between {@code 2} and {@code 3}. The default value is {@code 2}.
     *
     * @param moduleWidthRatio the ratio of wide bar width to narrow bar width
     */
    public void setModuleWidthRatio(double moduleWidthRatio) {
        this.moduleWidthRatio = moduleWidthRatio;
    }

    /**
     * Select addition of optional Modulo-43 check digit or encoding without
     * check digit.
     *
     * @param checkMode Check digit option.
     */
    public void setCheckDigit(CheckDigit checkMode) {
        checkOption = checkMode;
    }

    @Override
    public boolean encode() {
        if (!(content.matches("[0-9A-Z\\. \\-$/+%]+"))) {
            errorMsg.append("Invalid characters in input");
            return false;
        }
        StringBuilder p = new StringBuilder();
        StringBuilder dest = new StringBuilder();
        int l = content.length();
        int charval;
        char thischar;
        int counter = 0;
        char check_digit = ' ';
        dest.append("1211212111");
        for (int i = 0; i < l; i++) {
            thischar = content.charAt(i);
            charval = positionOf(thischar, LOOKUP);
            counter += charval;
            p.append(CODE_39[charval]);
        }
        dest.append(p);
        if (checkOption == CheckDigit.MOD43) {
            counter = counter % 43;
            if (counter < 10) {
                check_digit = (char) (counter + '0');
            } else {
                if (counter < 36) {
                    check_digit = (char) ((counter - 10) + 'A');
                } else {
                    switch (counter) {
                        case 36:
                            check_digit = '-';
                            break;
                        case 37:
                            check_digit = '.';
                            break;
                        case 38:
                            check_digit = ' ';
                            break;
                        case 39:
                            check_digit = '$';
                            break;
                        case 40:
                            check_digit = '/';
                            break;
                        case 41:
                            check_digit = '+';
                            break;
                        default:
                            check_digit = 37;
                            break;
                    }
                }
            }
            charval = positionOf(check_digit, LOOKUP);
            p.append(CODE_39[charval]);
            if (check_digit == ' ') {
                check_digit = '_';
            }
        }
        dest.append("121121211");
        if (checkOption == CheckDigit.MOD43) {
            readable = new StringBuilder("*").append(content).append(check_digit).append("*");
            encodeInfo.append("Check Digit: ").append(check_digit).append("\n");
        } else {
            readable = new StringBuilder("*").append(content).append("*");
        }
        pattern = new String[]{dest.toString()};
        rowCount = 1;
        rowHeight = new int[]{-1};
        plotSymbol();
        return true;
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
    protected int[] getCodewords() {
        return getPatternAsCodewords(10);
    }

    public enum CheckDigit {
        NONE, MOD43
    }
}
