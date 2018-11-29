package org.xbib.graphics.barcode;

import org.xbib.graphics.barcode.util.TextBox;

import java.awt.geom.Rectangle2D;

import static org.xbib.graphics.barcode.HumanReadableLocation.NONE;
import static org.xbib.graphics.barcode.HumanReadableLocation.TOP;

/**
 * Implements <a href="http://en.wikipedia.org/wiki/POSTNET">POSTNET</a> and
 * <a href="http://en.wikipedia.org/wiki/Postal_Alpha_Numeric_Encoding_Technique">PLANET</a>
 * bar code symbologies.
 * POSTNET and PLANET both use numerical input data and include a modulo-10
 * check digit.
 */
public class Postnet extends Symbol {

    private static final String[] PN_TABLE = {
            "LLSSS", "SSSLL", "SSLSL", "SSLLS", "SLSSL", "SLSLS", "SLLSS", "LSSSL", "LSSLS", "LSLSS"
    };

    ;
    private static final String[] PL_TABLE = {
            "SSLLL", "LLLSS", "LLSLS", "LLSSL", "LSLLS", "LSLSL", "LSSLL", "SLLLS", "SLLSL", "SLSLL"
    };
    private Mode mode;

    public Postnet() {
        this.mode = Mode.POSTNET;
        this.defaultHeight = 12;
        setHumanReadableLocation(HumanReadableLocation.NONE);
    }

    /**
     * Returns the barcode mode (PLANET or POSTNET). The default mode is POSTNET.
     *
     * @return the barcode mode (PLANET or POSTNET)
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Sets the barcode mode (PLANET or POSTNET). The default mode is POSTNET.
     *
     * @param mode the barcode mode (PLANET or POSTNET)
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public boolean encode() {

        boolean retval;
        if (mode == Mode.POSTNET) {
            retval = makePostnet();
        } else {
            retval = makePlanet();
        }

        if (retval) {
            plotSymbol();
        }

        return retval;
    }

    private boolean makePostnet() {
        int i, sum, check_digit;
        StringBuilder dest;

        if (content.length() > 38) {
            errorMsg.append("Input too long");
            return false;
        }

        if (!(content.matches("[0-9]+"))) {
            errorMsg.append("Invalid characters in data");
            return false;
        }

        sum = 0;
        dest = new StringBuilder("L");

        for (i = 0; i < content.length(); i++) {
            dest.append(PN_TABLE[content.charAt(i) - '0']);
            sum += content.charAt(i) - '0';
        }

        check_digit = (10 - (sum % 10)) % 10;
        encodeInfo.append("Check Digit: ").append(check_digit).append("\n");

        dest.append(PN_TABLE[check_digit]);
        dest.append("L");

        encodeInfo.append("Encoding: ").append(dest).append("\n");
        readable = new StringBuilder(content);
        pattern = new String[]{dest.toString()};
        rowCount = 1;
        rowHeight = new int[]{-1};

        return true;
    }

    private boolean makePlanet() {
        int i, sum, check_digit;
        StringBuilder dest;

        if (content.length() > 38) {
            errorMsg.append("Input too long");
            return false;
        }

        if (!(content.matches("[0-9]+"))) {
            errorMsg.append("Invalid characters in data");
            return false;
        }

        sum = 0;
        dest = new StringBuilder("L");

        for (i = 0; i < content.length(); i++) {
            dest.append(PL_TABLE[content.charAt(i) - '0']);
            sum += content.charAt(i) - '0';
        }

        check_digit = (10 - (sum % 10)) % 10;
        encodeInfo.append("Check Digit: ").append(check_digit).append("\n");

        dest.append(PL_TABLE[check_digit]);
        dest.append("L");

        encodeInfo.append("Encoding: ").append(dest).append("\n");
        readable = new StringBuilder(content);
        pattern = new String[]{dest.toString()};
        rowCount = 1;
        rowHeight = new int[]{-1};

        return true;
    }

    @Override
    protected void plotSymbol() {
        int xBlock, shortHeight;
        double x, y, w, h;

        rectangles.clear();
        texts.clear();

        int baseY;
        if (getHumanReadableLocation() == TOP) {
            baseY = getTheoreticalHumanReadableHeight();
        } else {
            baseY = 0;
        }

        x = 0;
        w = moduleWidth;
        shortHeight = (int) (0.4 * defaultHeight);
        for (xBlock = 0; xBlock < pattern[0].length(); xBlock++) {
            if (pattern[0].charAt(xBlock) == 'L') {
                y = baseY;
                h = defaultHeight;
            } else {
                y = baseY + defaultHeight - shortHeight;
                h = shortHeight;
            }
            rectangles.add(new Rectangle2D.Double(x, y, w, h));
            x += (2.5 * w);
        }

        symbolWidth = (int) Math.ceil(((pattern[0].length() - 1) * 2.5 * w) + w); // final bar doesn't need extra whitespace
        symbolHeight = defaultHeight;

        if (getHumanReadableLocation() != NONE && readable.length() > 0) {
            double baseline;
            if (getHumanReadableLocation() == TOP) {
                baseline = fontSize;
            } else {
                baseline = getHeight() + fontSize;
            }
            double centerX = getWidth() / 2.0;
            texts.add(new TextBox(centerX, baseline, readable.toString()));
        }
    }

    public static enum Mode {
        PLANET, POSTNET
    }
}
