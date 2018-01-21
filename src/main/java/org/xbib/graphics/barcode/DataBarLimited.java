package org.xbib.graphics.barcode;

import java.math.BigInteger;

/**
 * Implements GS1 DataBar Limited according to ISO/IEC 24724:2011.
 * Input data should be a 12 digit Global Trade Identification Number
 * without check digit or Application Identifier [01].
 */
public class DataBarLimited extends Symbol {

    private static final int[] t_even_ltd = {
            28, 728, 6454, 203, 2408, 1, 16632
    };
    private static final int[] modules_odd_ltd = {
            17, 13, 9, 15, 11, 19, 7
    };
    private static final int[] modules_even_ltd = {
            9, 13, 17, 11, 15, 7, 19
    };
    private static final int[] widest_odd_ltd = {
            6, 5, 3, 5, 4, 8, 1
    };
    private static final int[] widest_even_ltd = {
            3, 4, 6, 4, 5, 1, 8
    };
    private static final int[] checksum_weight_ltd = { /* Table 7 */
            1, 3, 9, 27, 81, 65, 17, 51, 64, 14, 42, 37, 22, 66,
            20, 60, 2, 6, 18, 54, 73, 41, 34, 13, 39, 28, 84, 74
    };
    private static final int[] finder_pattern_ltd = {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 2, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 3, 2, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 3, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 1,
            1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 3, 2, 1, 1,
            1, 1, 1, 1, 1, 2, 1, 1, 1, 2, 3, 1, 1, 1,
            1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 3, 1, 1, 1,
            1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, 1, 1, 1,
            1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 3, 2, 1, 1,
            1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 3, 1, 1, 1,
            1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 3, 1, 1, 1,
            1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 3, 1, 1, 1,
            1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1,
            1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 3, 2, 1, 1,
            1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 3, 1, 1, 1,
            1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 3, 1, 1, 1,
            1, 2, 1, 1, 1, 2, 1, 1, 1, 1, 3, 1, 1, 1,
            1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1,
            1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 3, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 2, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 2, 2, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 3, 2, 1, 2, 1, 1, 1,
            1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 1,
            1, 1, 1, 1, 1, 2, 1, 1, 2, 2, 2, 1, 1, 1,
            1, 1, 1, 1, 1, 2, 1, 2, 2, 1, 2, 1, 1, 1,
            1, 1, 1, 1, 1, 3, 1, 1, 2, 1, 2, 1, 1, 1,
            1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 2, 2, 1, 1,
            1, 1, 1, 2, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1,
            1, 1, 1, 2, 1, 1, 1, 2, 2, 1, 2, 1, 1, 1,
            1, 1, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 1, 1,
            1, 1, 1, 3, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1,
            1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 2, 2, 1, 1,
            1, 2, 1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1,
            1, 2, 1, 1, 1, 1, 1, 2, 2, 1, 2, 1, 1, 1,
            1, 2, 1, 1, 1, 2, 1, 1, 2, 1, 2, 1, 1, 1,
            1, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1,
            1, 3, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 3, 2, 1, 2, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 2, 3, 1, 1, 2, 1, 1,
            1, 1, 1, 2, 1, 1, 1, 1, 3, 1, 1, 2, 1, 1,
            1, 2, 1, 1, 1, 1, 1, 1, 3, 1, 1, 2, 1, 1,
            1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 2, 3, 1, 1,
            1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 2, 2, 1, 1,
            1, 1, 1, 1, 1, 1, 2, 1, 1, 3, 2, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2, 2, 1, 1,
            1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 2, 2, 1, 1,
            1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 1, 1,
            1, 1, 1, 2, 1, 1, 2, 2, 1, 1, 2, 1, 1, 1,
            1, 1, 1, 2, 1, 2, 2, 1, 1, 1, 2, 1, 1, 1,
            1, 1, 1, 3, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1,
            1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 2, 2, 1, 1,
            1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 2, 1, 1, 1,
            1, 2, 1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1,
            1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 3, 1, 1,
            1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 2, 2, 1, 1,
            1, 1, 1, 1, 2, 1, 1, 1, 1, 3, 2, 1, 1, 1,
            1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 1,
            1, 1, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 1, 1,
            1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 2, 2, 1, 1,
            1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 2, 1, 1,
            1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 2, 1, 1, 1,
            1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 1,
            1, 2, 1, 1, 2, 2, 1, 1, 1, 1, 2, 1, 1, 1,
            1, 2, 1, 2, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1,
            1, 3, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1,
            1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 3, 1, 1,
            1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1,
            1, 1, 2, 1, 1, 1, 1, 1, 1, 3, 2, 1, 1, 1,
            1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 2, 1, 1,
            1, 1, 2, 1, 1, 1, 1, 2, 1, 2, 2, 1, 1, 1,
            1, 1, 2, 1, 1, 1, 1, 3, 1, 1, 2, 1, 1, 1,
            1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 2, 1, 1,
            1, 1, 2, 1, 1, 2, 1, 1, 1, 2, 2, 1, 1, 1,
            1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1,
            2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1,
            2, 1, 1, 1, 1, 1, 1, 1, 1, 3, 2, 1, 1, 1,
            2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 2, 1, 1,
            2, 1, 1, 1, 1, 1, 1, 2, 1, 2, 2, 1, 1, 1,
            2, 1, 1, 1, 1, 1, 1, 3, 1, 1, 2, 1, 1, 1,
            2, 1, 1, 1, 1, 2, 1, 1, 1, 2, 2, 1, 1, 1,
            2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 1, 1, 1,
            2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1,
            2, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 2, 1, 1 /* ГОСТ ISO/IEC 24724-2011 страница 57 */
    };

    private boolean linkageFlag;
    private int[] widths = new int[8];

    public DataBarLimited() {
        linkageFlag = false;
    }

    @Override
    public void setDataType(DataType dummy) {
        // Do nothing!
    }

    protected void setLinkageFlag() {
        linkageFlag = true;
    }

    @Override
    public boolean encode() {
        BigInteger accum;
        BigInteger left_reg;
        BigInteger right_reg;
        int left_group;
        int right_group;
        int i, j;
        int left_character;
        int right_character;
        int left_odd;
        int right_odd;
        int left_even;
        int right_even;
        int[] left_widths = new int[14];
        int[] right_widths = new int[14];
        int checksum;
        int[] check_elements = new int[14];
        int[] total_widths = new int[46];
        StringBuilder bin;
        StringBuilder notbin;
        boolean bar_latch;
        int writer;
        int check_digit;
        int count = 0;
        StringBuilder hrt;
        int compositeOffset = 0;

        if (content.length() > 13) {
            errorMsg.append("Input too long");
            return false;
        }

        if (!(content.matches("[0-9]+?"))) {
            errorMsg.append("Invalid characters in input");
            return false;
        }

        if (content.length() == 13) {
            if ((content.charAt(0) != '0') && (content.charAt(0) != '1')) {
                errorMsg.append("Input out of range");
                return false;
            }
        }

        accum = new BigInteger(content);

        if (linkageFlag) {
            /* Add symbol linkage flag */
            accum = accum.add(new BigInteger("2015133531096"));
        }

        /* Calculate left and right pair values */
        left_reg = accum.divide(new BigInteger("2013571"));
        right_reg = accum.mod(new BigInteger("2013571"));

        left_group = 0;
        if (left_reg.compareTo(new BigInteger("183063")) > 0) {
            left_group = 1;
        }
        if (left_reg.compareTo(new BigInteger("820063")) > 0) {
            left_group = 2;
        }
        if (left_reg.compareTo(new BigInteger("1000775")) > 0) {
            left_group = 3;
        }
        if (left_reg.compareTo(new BigInteger("1491020")) > 0) {
            left_group = 4;
        }
        if (left_reg.compareTo(new BigInteger("1979844")) > 0) {
            left_group = 5;
        }
        if (left_reg.compareTo(new BigInteger("1996938")) > 0) {
            left_group = 6;
        }

        right_group = 0;
        if (right_reg.compareTo(new BigInteger("183063")) > 0) {
            right_group = 1;
        }
        if (right_reg.compareTo(new BigInteger("820063")) > 0) {
            right_group = 2;
        }
        if (right_reg.compareTo(new BigInteger("1000775")) > 0) {
            right_group = 3;
        }
        if (right_reg.compareTo(new BigInteger("1491020")) > 0) {
            right_group = 4;
        }
        if (right_reg.compareTo(new BigInteger("1979844")) > 0) {
            right_group = 5;
        }
        if (right_reg.compareTo(new BigInteger("1996938")) > 0) {
            right_group = 6;
        }

        encodeInfo.append("Data Characters: ").append(Integer.toString(left_group + 1)).append(" ").append(Integer.toString(right_group + 1)).append("\n");

        switch (left_group) {
            case 1:
                left_reg = left_reg.subtract(new BigInteger("183064"));
                break;
            case 2:
                left_reg = left_reg.subtract(new BigInteger("820064"));
                break;
            case 3:
                left_reg = left_reg.subtract(new BigInteger("1000776"));
                break;
            case 4:
                left_reg = left_reg.subtract(new BigInteger("1491021"));
                break;
            case 5:
                left_reg = left_reg.subtract(new BigInteger("1979845"));
                break;
            case 6:
                left_reg = left_reg.subtract(new BigInteger("1996939"));
                break;
        }

        switch (right_group) {
            case 1:
                right_reg = right_reg.subtract(new BigInteger("183064"));
                break;
            case 2:
                right_reg = right_reg.subtract(new BigInteger("820064"));
                break;
            case 3:
                right_reg = right_reg.subtract(new BigInteger("1000776"));
                break;
            case 4:
                right_reg = right_reg.subtract(new BigInteger("1491021"));
                break;
            case 5:
                right_reg = right_reg.subtract(new BigInteger("1979845"));
                break;
            case 6:
                right_reg = right_reg.subtract(new BigInteger("1996939"));
                break;
        }

        left_character = left_reg.intValue();
        right_character = right_reg.intValue();

        left_odd = left_character / t_even_ltd[left_group];
        left_even = left_character % t_even_ltd[left_group];
        right_odd = right_character / t_even_ltd[right_group];
        right_even = right_character % t_even_ltd[right_group];

        getWidths(left_odd, modules_odd_ltd[left_group], 7, widest_odd_ltd[left_group], 1);
        left_widths[0] = widths[0];
        left_widths[2] = widths[1];
        left_widths[4] = widths[2];
        left_widths[6] = widths[3];
        left_widths[8] = widths[4];
        left_widths[10] = widths[5];
        left_widths[12] = widths[6];
        getWidths(left_even, modules_even_ltd[left_group], 7, widest_even_ltd[left_group], 0);
        left_widths[1] = widths[0];
        left_widths[3] = widths[1];
        left_widths[5] = widths[2];
        left_widths[7] = widths[3];
        left_widths[9] = widths[4];
        left_widths[11] = widths[5];
        left_widths[13] = widths[6];
        getWidths(right_odd, modules_odd_ltd[right_group], 7, widest_odd_ltd[right_group], 1);
        right_widths[0] = widths[0];
        right_widths[2] = widths[1];
        right_widths[4] = widths[2];
        right_widths[6] = widths[3];
        right_widths[8] = widths[4];
        right_widths[10] = widths[5];
        right_widths[12] = widths[6];
        getWidths(right_even, modules_even_ltd[right_group], 7, widest_even_ltd[right_group], 0);
        right_widths[1] = widths[0];
        right_widths[3] = widths[1];
        right_widths[5] = widths[2];
        right_widths[7] = widths[3];
        right_widths[9] = widths[4];
        right_widths[11] = widths[5];
        right_widths[13] = widths[6];

        checksum = 0;
        /* Calculate the checksum */
        for (i = 0; i < 14; i++) {
            checksum += checksum_weight_ltd[i] * left_widths[i];
            checksum += checksum_weight_ltd[i + 14] * right_widths[i];
        }
        checksum %= 89;

        encodeInfo.append("Checksum: ").append(Integer.toString(checksum)).append("\n");

        for (i = 0; i < 14; i++) {
            check_elements[i] = finder_pattern_ltd[i + (checksum * 14)];
        }

        total_widths[0] = 1;
        total_widths[1] = 1;
        total_widths[44] = 1;
        total_widths[45] = 1;
        for (i = 0; i < 14; i++) {
            total_widths[i + 2] = left_widths[i];
            total_widths[i + 16] = check_elements[i];
            total_widths[i + 30] = right_widths[i];
        }

        bin = new StringBuilder();
        notbin = new StringBuilder();
        writer = 0;
        bar_latch = false;
        for (i = 0; i < 46; i++) {
            for (j = 0; j < total_widths[i]; j++) {
                if (bar_latch) {
                    bin.append("1");
                    notbin.append("0");
                } else {
                    bin.append("0");
                    notbin.append("1");
                }
                writer++;
            }
            bar_latch = !bar_latch;
        }

        if (symbolWidth < (writer + 20)) {
            symbolWidth = writer + 20;
        }

        /* Calculate check digit from Annex A and place human readable text */

        readable = new StringBuilder("(01)");
        hrt = new StringBuilder();
        for (i = content.length(); i < 13; i++) {
            hrt.append("0");
        }
        hrt.append(content);

        for (i = 0; i < 13; i++) {
            count += (hrt.charAt(i) - '0');

            if ((i & 1) == 0) {
                count += 2 * (hrt.charAt(i) - '0');
            }
        }

        check_digit = 10 - (count % 10);
        if (check_digit == 10) {
            check_digit = 0;
        }

        hrt.append((char) (check_digit + '0'));
        readable.append(hrt);

        if (linkageFlag) {
            compositeOffset = 1;
        }

        rowCount = 1 + compositeOffset;
        rowHeight = new int[1 + compositeOffset];
        rowHeight[compositeOffset] = -1;
        pattern = new String[1 + compositeOffset];
        pattern[compositeOffset] = "0:" + bin2pat(bin.toString());

        if (linkageFlag) {
            // Add composite symbol seperator
            notbin = new StringBuilder(notbin.substring(4, 70));
            rowHeight[0] = 1;
            pattern[0] = "0:04" + bin2pat(notbin.toString());
        }

        plotSymbol();
        return true;
    }

    private int getCombinations(int n, int r) {
        int i, j;
        int maxDenom, minDenom;
        int val;

        if (n - r > r) {
            minDenom = r;
            maxDenom = n - r;
        } else {
            minDenom = n - r;
            maxDenom = r;
        }
        val = 1;
        j = 1;
        for (i = n; i > maxDenom; i--) {
            val *= i;
            if (j <= minDenom) {
                val /= j;
                j++;
            }
        }
        for (; j <= minDenom; j++) {
            val /= j;
        }
        return (val);
    }

    private void getWidths(int val, int n, int elements, int maxWidth, int noNarrow) {
        int bar;
        int elmWidth;
        int mxwElement;
        int subVal, lessVal;
        int narrowMask = 0;
        for (bar = 0; bar < elements - 1; bar++) {
            for (elmWidth = 1, narrowMask |= (1 << bar); ;
                 elmWidth++, narrowMask &= ~(1 << bar)) {
                /* get all combinations */
                subVal = getCombinations(n - elmWidth - 1, elements - bar - 2);
                /* less combinations with no single-module element */
                if ((noNarrow == 0) && (narrowMask == 0)
                        && (n - elmWidth - (elements - bar - 1) >= elements - bar - 1)) {
                    subVal -= getCombinations(n - elmWidth - (elements - bar), elements - bar - 2);
                }
                /* less combinations with elements > maxVal */
                if (elements - bar - 1 > 1) {
                    lessVal = 0;
                    for (mxwElement = n - elmWidth - (elements - bar - 2);
                         mxwElement > maxWidth;
                         mxwElement--) {
                        lessVal += getCombinations(n - elmWidth - mxwElement - 1, elements - bar - 3);
                    }
                    subVal -= lessVal * (elements - 1 - bar);
                } else if (n - elmWidth > maxWidth) {
                    subVal--;
                }
                val -= subVal;
                if (val < 0) break;
            }
            val += subVal;
            n -= elmWidth;
            widths[bar] = elmWidth;
        }
        widths[bar] = n;
    }
}
