package org.xbib.graphics.barcode;

/**
 * Implements the MSI (Modified Plessey) bar code symbology.
 * MSI Plessey can encode a string of numeric digits and has a range
 * of check digit options.
 */
public class MsiPlessey extends Symbol {

    private final String[] MSI_PlessTable = {
            "12121212", "12121221", "12122112", "12122121", "12211212", "12211221",
            "12212112", "12212121", "21121212", "21121221"
    };
    private CheckDigit checkOption;

    public MsiPlessey() {
        checkOption = CheckDigit.NONE;
    }

    /**
     * Set the check digit scheme to use. Options are: None, Modulo-10,
     * 2 x Modulo-10, Modulo-11 and Modulo-11 &amp; 10.
     *
     * @param checkMode Type of check digit to add to symbol
     */
    public void setCheckDigit(CheckDigit checkMode) {
        checkOption = checkMode;
    }

    @Override
    public boolean encode() {
        StringBuilder intermediate;
        int length = content.length();
        int i;
        StringBuilder evenString;
        StringBuilder oddString;
        String addupString;
        int spacer;
        int addup;
        int weight;
        int checkDigit1;
        int checkDigit2;

        if (!(content.matches("[0-9]+"))) {
            errorMsg.append("Invalid characters in input");
            return false;
        }

        intermediate = new StringBuilder("21"); // Start
        for (i = 0; i < length; i++) {
            intermediate.append(MSI_PlessTable[Character.getNumericValue(content.charAt(i))]);
        }

        readable = new StringBuilder(content);

        if ((checkOption == CheckDigit.MOD10) || (checkOption == CheckDigit.MOD10_MOD10)) {
            /* Add Modulo-10 check digit */
            evenString = new StringBuilder();
            oddString = new StringBuilder();

            spacer = content.length() & 1;

            for (i = content.length() - 1; i >= 0; i--) {
                if (spacer == 1) {
                    if ((i & 1) != 0) {
                        evenString.insert(0, content.charAt(i));
                    } else {
                        oddString.insert(0, content.charAt(i));
                    }
                } else {
                    if ((i & 1) != 0) {
                        oddString.insert(0, content.charAt(i));
                    } else {
                        evenString.insert(0, content.charAt(i));
                    }
                }
            }

            if (oddString.length() == 0) {
                addupString = "0";
            } else {
                addupString = Integer.toString(Integer.parseInt(oddString.toString()) * 2);
            }

            addupString += evenString;

            addup = 0;
            for (i = 0; i < addupString.length(); i++) {
                addup += addupString.charAt(i) - '0';
            }

            checkDigit1 = 10 - (addup % 10);
            if (checkDigit1 == 10) {
                checkDigit1 = 0;
            }

            intermediate.append(MSI_PlessTable[checkDigit1]);
            readable.append(checkDigit1);
        }

        if ((checkOption == CheckDigit.MOD11) || (checkOption == CheckDigit.MOD11_MOD10)) {
            /* Add a Modulo-11 check digit */
            weight = 2;
            addup = 0;
            for (i = content.length() - 1; i >= 0; i--) {
                addup += (content.charAt(i) - '0') * weight;
                weight++;

                if (weight == 8) {
                    weight = 2;
                }
            }

            checkDigit1 = 11 - (addup % 11);

            if (checkDigit1 == 11) {
                checkDigit1 = 0;
            }

            readable.append(checkDigit1);
            if (checkDigit1 == 10) {
                intermediate.append(MSI_PlessTable[1]);
                intermediate.append(MSI_PlessTable[0]);
            } else {
                intermediate.append(MSI_PlessTable[checkDigit1]);
            }
        }

        if ((checkOption == CheckDigit.MOD10_MOD10) || (checkOption == CheckDigit.MOD11_MOD10)) {
            /* Add a second Modulo-10 check digit */
            evenString = new StringBuilder();
            oddString = new StringBuilder();

            spacer = readable.length() & 1;

            for (i = readable.length() - 1; i >= 0; i--) {
                if (spacer == 1) {
                    if ((i & 1) != 0) {
                        evenString.insert(0, readable.charAt(i));
                    } else {
                        oddString.insert(0, readable.charAt(i));
                    }
                } else {
                    if ((i & 1) != 0) {
                        oddString.insert(0, readable.charAt(i));
                    } else {
                        evenString.insert(0, readable.charAt(i));
                    }
                }
            }

            if (oddString.length() == 0) {
                addupString = "0";
            } else {
                addupString = Integer.toString(Integer.parseInt(oddString.toString()) * 2);
            }

            addupString += evenString;

            addup = 0;
            for (i = 0; i < addupString.length(); i++) {
                addup += addupString.charAt(i) - '0';
            }

            checkDigit2 = 10 - (addup % 10);
            if (checkDigit2 == 10) {
                checkDigit2 = 0;
            }

            intermediate.append(MSI_PlessTable[checkDigit2]);
            readable.append(checkDigit2);
        }

        intermediate.append("121"); // Stop

        pattern = new String[1];
        pattern[0] = intermediate.toString();
        rowCount = 1;
        rowHeight = new int[1];
        rowHeight[0] = -1;
        plotSymbol();
        return true;
    }

    public enum CheckDigit {
        NONE, MOD10, MOD10_MOD10, MOD11, MOD11_MOD10
    }
}
