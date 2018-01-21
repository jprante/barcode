package org.xbib.graphics.barcode;

import org.xbib.graphics.barcode.util.AddOn;
import org.xbib.graphics.barcode.util.TextBox;

import java.awt.geom.Rectangle2D;

import static org.xbib.graphics.barcode.HumanReadableLocation.NONE;
import static org.xbib.graphics.barcode.HumanReadableLocation.TOP;

/**
 * Implements EAN bar code symbology according to BS EN 797:1996.
 * European Article Number data can be encoded in EAN-8 or EAN-13 format
 * requiring a 7-digit or 12-digit input respectively. EAN-13 numbers map to
 * Global Trade Identification Numbers (GTIN) whereas EAN-8 symbols are
 * generally for internal use only. Check digit is calculated and should not
 * be in input data. Leading zeroes are added as required.
 */
public class Ean extends Symbol {

    private boolean useAddOn;

    private String addOnContent;
    private Mode mode;
    private boolean linkageFlag;
    private String[] EAN13Parity = {
            "AAAAAA", "AABABB", "AABBAB", "AABBBA", "ABAABB", "ABBAAB", "ABBBAA",
            "ABABAB", "ABABBA", "ABBABA"
    };
    private String[] EANsetA = {
            "3211", "2221", "2122", "1411", "1132", "1231", "1114", "1312", "1213",
            "3112"
    };
    private String[] EANsetB = {
            "1123", "1222", "2212", "1141", "2311", "1321", "4111", "2131", "3121",
            "2113"
    };

    public Ean() {
        mode = Mode.EAN13;
        useAddOn = false;
        addOnContent = "";
        linkageFlag = false;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    protected void setLinkageFlag() {
        linkageFlag = true;
    }

    @Override
    public void setHumanReadableLocation(HumanReadableLocation humanReadableLocation) {
        if (humanReadableLocation == TOP) {
            throw new IllegalArgumentException("Cannot display human-readable text above EAN bar codes.");
        } else {
            super.setHumanReadableLocation(humanReadableLocation);
        }
    }

    @Override
    public boolean encode() {
        boolean retval = false;
        AddOn addOn = new AddOn();
        String addOnData;

        separateContent();

        if (content.length() == 0) {
            errorMsg.append("Missing EAN data");
            retval = false;
        } else {
            switch (mode) {
                case EAN8:
                    retval = ean8();
                    break;
                case EAN13:
                    retval = ean13();
                    break;
            }
        }

        if ((retval) && (useAddOn)) {
            addOnData = addOn.calcAddOn(addOnContent);
            if (addOnData.length() == 0) {
                errorMsg.append("Invalid Add-On data");
                retval = false;
            } else {
                pattern[0] = pattern[0] + "9" + addOnData;

                //add leading zeroes to add-on text
                if (addOnContent.length() == 1) {
                    addOnContent = "0" + addOnContent;
                }
                if (addOnContent.length() == 3) {
                    addOnContent = "0" + addOnContent;
                }
                if (addOnContent.length() == 4) {
                    addOnContent = "0" + addOnContent;
                }
            }
        }

        if (retval) {
            plotSymbol();
        }

        return retval;
    }

    private void separateContent() {
        int splitPoint;

        splitPoint = content.indexOf('+');
        if (splitPoint != -1) {
            // There is a '+' in the input data, use an add-on EAN2 or EAN5
            useAddOn = true;
            addOnContent = content.substring(splitPoint + 1);
            content = content.substring(0, splitPoint);
        }
    }

    private boolean ean13() {
        StringBuilder accumulator = new StringBuilder();
        StringBuilder dest;
        String parity;
        int i;

        if (!(content.matches("[0-9]+"))) {
            errorMsg.append("Invalid characters in input");
            return false;
        }

        if (content.length() > 12) {
            errorMsg.append("Input data too long");
            return false;
        }

        for (i = content.length(); i < 12; i++) {
            accumulator.append("0");
        }
        accumulator.append(content);

        accumulator.append(calcDigit(accumulator.toString()));

        parity = EAN13Parity[accumulator.charAt(0) - '0'];

        encodeInfo.append("Parity Digit: ").append(accumulator.charAt(0)).append("\n");

        /* Start character */
        dest = new StringBuilder("111");

        for (i = 1; i < 13; i++) {
            if (i == 7) {
                dest.append("11111");
            }

            if ((i >= 1) && (i <= 6)) {
                if (parity.charAt(i - 1) == 'B') {
                    dest.append(EANsetB[accumulator.charAt(i) - '0']);
                } else {
                    dest.append(EANsetA[accumulator.charAt(i) - '0']);
                }
            } else {
                dest.append(EANsetA[accumulator.charAt(i) - '0']);
            }
        }

        dest.append("111");

        readable = new StringBuilder(accumulator.toString());
        pattern = new String[1];
        pattern[0] = dest.toString();
        rowCount = 1;
        rowHeight = new int[1];
        rowHeight[0] = -1;
        return true;
    }

    private boolean ean8() {
        StringBuilder accumulator = new StringBuilder();
        int i;
        StringBuilder dest;

        if (!(content.matches("[0-9]+"))) {
            errorMsg.append("Invalid characters in input");
            return false;
        }

        if (content.length() > 7) {
            errorMsg.append("Input data too long");
            return false;
        }

        for (i = content.length(); i < 7; i++) {
            accumulator.append("0");
        }
        accumulator.append(content);

        accumulator.append(calcDigit(accumulator.toString()));

        dest = new StringBuilder("111");
        for (i = 0; i < 8; i++) {
            if (i == 4) {
                dest.append("11111");
            }
            dest.append(EANsetA[Character.getNumericValue(accumulator.charAt(i))]);
        }
        dest.append("111");

        readable = new StringBuilder(accumulator.toString());
        pattern = new String[1];
        pattern[0] = dest.toString();
        rowCount = 1;
        rowHeight = new int[1];
        rowHeight[0] = -1;
        return true;
    }

    private char calcDigit(String x) {
        int count = 0;
        int c, cdigit;
        int p = 0;
        for (int i = x.length() - 1; i >= 0; i--) {
            c = Character.getNumericValue(x.charAt(i));
            if ((p % 2) == 0) {
                c = c * 3;
            }
            count += c;
            p++;
        }
        cdigit = 10 - (count % 10);
        if (cdigit == 10) {
            cdigit = 0;
        }

        encodeInfo.append("Check Digit: ").append(cdigit).append("\n");

        return (char) (cdigit + '0');
    }

    @Override
    protected void plotSymbol() {
        int xBlock;
        int x, y, w, h;
        boolean black;
        int compositeOffset = 0;
        int shortLongDiff = 5;

        rectangles.clear();
        texts.clear();
        black = true;
        x = 0;
        if (linkageFlag) {
            compositeOffset = 6;
        }
        for (xBlock = 0; xBlock < pattern[0].length(); xBlock++) {
            if (black) {
                y = 0;
                black = false;
                w = pattern[0].charAt(xBlock) - '0';
                h = defaultHeight;
                /* Add extension to guide bars */
                if (mode == Mode.EAN13) {
                    if ((x < 3) || (x > 91)) {
                        h += shortLongDiff;
                    }
                    if ((x > 45) && (x < 49)) {
                        h += shortLongDiff;
                    }
                    if (x > 95) {
                        // Drop add-on
                        h -= 8;
                        y = 8;
                    }
                    if (linkageFlag) {
                        if ((x == 0) || (x == 94)) {
                            h += 2;
                            y -= 2;
                        }
                    }
                }
                if (mode == Mode.EAN8) {
                    if ((x < 3) || (x > 62)) {
                        h += shortLongDiff;
                    }
                    if ((x > 30) && (x < 35)) {
                        h += shortLongDiff;
                    }
                    if (x > 66) {
                        // Drop add-on
                        h -= 8;
                        y = 8;
                    }
                    if (linkageFlag) {
                        if ((x == 0) || (x == 66)) {
                            h += 2;
                            y -= 2;
                        }
                    }
                }
                Rectangle2D.Double rect = new Rectangle2D.Double(x + 6, y + compositeOffset, w, h);
                rectangles.add(rect);
                if ((x + w + 12) > symbolWidth) {
                    symbolWidth = x + w + 12;
                }
            } else {
                black = true;
            }
            x += (double) (pattern[0].charAt(xBlock) - '0');

        }

        if (linkageFlag) {
            // Add separator for composite symbology
            if (mode == Mode.EAN13) {
                rectangles.add(new Rectangle2D.Double(6, 0, 1, 2));
                rectangles.add(new Rectangle2D.Double(94 + 6, 0, 1, 2));
                rectangles.add(new Rectangle2D.Double(-1 + 6, 2, 1, 2));
                rectangles.add(new Rectangle2D.Double(95 + 6, 2, 1, 2));
            } else {
                rectangles.add(new Rectangle2D.Double(6, 0, 1, 2));
                rectangles.add(new Rectangle2D.Double(66 + 6, 0, 1, 2));
                rectangles.add(new Rectangle2D.Double(-1 + 6, 2, 1, 2));
                rectangles.add(new Rectangle2D.Double(67 + 6, 2, 1, 2));
            }
        }

        symbolHeight = defaultHeight + 5;

        /* Now add the text */
        if (humanReadableLocation != NONE) {
            double baseline = getHeight() + fontSize - shortLongDiff + compositeOffset;
            double addOnBaseline = 6.0 + compositeOffset;
            if (mode == Mode.EAN13) {
                texts.add(new TextBox(3, baseline, readable.substring(0, 1)));
                texts.add(new TextBox(30, baseline, readable.substring(1, 7)));
                texts.add(new TextBox(77, baseline, readable.substring(7, 13)));
                if (useAddOn) {
                    if (addOnContent.length() == 2) {
                        texts.add(new TextBox(118, addOnBaseline, addOnContent));
                    } else {
                        texts.add(new TextBox(133, addOnBaseline, addOnContent));
                    }
                }
            } else { // EAN8
                texts.add(new TextBox(23, baseline, readable.substring(0, 4)));
                texts.add(new TextBox(55, baseline, readable.substring(4, 8)));
                if (useAddOn) {
                    if (addOnContent.length() == 2) {
                        texts.add(new TextBox(93, addOnBaseline, addOnContent));
                    } else {
                        texts.add(new TextBox(105, addOnBaseline, addOnContent));
                    }
                }
            }
        }
    }

    public enum Mode {
        EAN8, EAN13
    }
}
