package org.xbib.graphics.barcode;

import java.math.BigInteger;

/**
 * Implements GS1 DataBar Omnidirectional and GS1 DataBar Truncated according to ISO/IEC 24724:2011.
 * Input data should be a 13 digit Global Trade Identification Number
 * without check digit or Application Identifier [01].
 */
public class DataBar14 extends Symbol {

    private int[] g_sum_table = {
            0, 161, 961, 2015, 2715, 0, 336, 1036, 1516
    };
    private int[] t_table = {
            1, 10, 34, 70, 126, 4, 20, 48, 81
    };
    private int[] widths = new int[8];
    private int[] modules_odd = {
            12, 10, 8, 6, 4, 5, 7, 9, 11
    };
    private int[] modules_even = {
            4, 6, 8, 10, 12, 10, 8, 6, 4
    };
    private int[] widest_odd = {
            8, 6, 4, 3, 1, 2, 4, 6, 8
    };
    private int[] widest_even = {
            1, 3, 5, 6, 8, 7, 5, 3, 1
    };
    private int[] checksum_weight = { /* Table 5 */
            1, 3, 9, 27, 2, 6, 18, 54, 4, 12, 36, 29, 8, 24, 72, 58, 16, 48, 65,
            37, 32, 17, 51, 74, 64, 34, 23, 69, 49, 68, 46, 59
    };
    private int[] finder_pattern = {
            3, 8, 2, 1, 1, 3, 5, 5, 1, 1, 3, 3, 7, 1, 1, 3, 1, 9, 1, 1, 2, 7, 4,
            1, 1, 2, 5, 6, 1, 1, 2, 3, 8, 1, 1, 1, 5, 7, 1, 1, 1, 3, 9, 1, 1
    };

    private boolean linkageFlag;
    private gb14Mode symbolType;

    private boolean[][] grid = new boolean[5][100];
    private boolean[] seperator = new boolean[100];

    public DataBar14() {
        linkageFlag = false;
        symbolType = gb14Mode.LINEAR;
    }

    @Override
    public void setDataType(DataType dummy) {
        // Do nothing!
    }

    protected void setLinkageFlag() {
        linkageFlag = true;
    }

    protected void unsetLinkageFlag() {
        linkageFlag = false;
    }

    /**
     * Set symbol type to DataBar-14
     */
    public void setLinearMode() {
        symbolType = gb14Mode.LINEAR;
    }

    /**
     * Set symbol type to DataBar-14 Omnidirectional
     */
    public void setOmnidirectionalMode() {
        symbolType = gb14Mode.OMNI;
    }

    /**
     * Set symbol type to DataBar-14 Omnidirectional Stacked
     */
    public void setStackedMode() {
        symbolType = gb14Mode.STACKED;
    }

    @Override
    public boolean encode() {
        BigInteger accum;
        BigInteger left_reg;
        BigInteger right_reg;
        int[] data_character = new int[4];
        int[] data_group = new int[4];
        int[] v_odd = new int[4];
        int[] v_even = new int[4];
        int i;
        int[][] data_widths = new int[8][4];
        int checksum;
        int c_left;
        int c_right;
        int[] total_widths = new int[46];
        int writer;
        char latch;
        int j;
        int count;
        int check_digit;
        String hrt;
        String bin;
        int compositeOffset = 0;

        if (content.length() > 13) {
            errorMsg.append("Input too long");
            return false;
        }

        if (!(content.matches("[0-9]+?"))) {
            errorMsg.append("Invalid characters in input");
            return false;
        }

        accum = new BigInteger(content);
        if (linkageFlag) {
            accum = accum.add(new BigInteger("10000000000000"));
            compositeOffset = 1;
        }

        /* Calculate left and right pair values */
        left_reg = accum.divide(new BigInteger("4537077"));
        right_reg = accum.mod(new BigInteger("4537077"));

        /* Calculate four data characters */
        accum = left_reg.divide(new BigInteger("1597"));
        data_character[0] = accum.intValue();
        accum = left_reg.mod(new BigInteger("1597"));
        data_character[1] = accum.intValue();
        accum = right_reg.divide(new BigInteger("1597"));
        data_character[2] = accum.intValue();
        accum = right_reg.mod(new BigInteger("1597"));
        data_character[3] = accum.intValue();

        encodeInfo.append("Data characters: ");
        for (i = 0; i < 4; i++) {
            encodeInfo.append(Integer.toString(data_character[i])).append(" ");
        }
        encodeInfo.append("\n");

        /* Calculate odd and even subset values */
        if ((data_character[0] >= 0) && (data_character[0] <= 160)) {
            data_group[0] = 0;
        }
        if ((data_character[0] >= 161) && (data_character[0] <= 960)) {
            data_group[0] = 1;
        }
        if ((data_character[0] >= 961) && (data_character[0] <= 2014)) {
            data_group[0] = 2;
        }
        if ((data_character[0] >= 2015) && (data_character[0] <= 2714)) {
            data_group[0] = 3;
        }
        if ((data_character[0] >= 2715) && (data_character[0] <= 2840)) {
            data_group[0] = 4;
        }
        if ((data_character[1] >= 0) && (data_character[1] <= 335)) {
            data_group[1] = 5;
        }
        if ((data_character[1] >= 336) && (data_character[1] <= 1035)) {
            data_group[1] = 6;
        }
        if ((data_character[1] >= 1036) && (data_character[1] <= 1515)) {
            data_group[1] = 7;
        }
        if ((data_character[1] >= 1516) && (data_character[1] <= 1596)) {
            data_group[1] = 8;
        }
        if ((data_character[3] >= 0) && (data_character[3] <= 335)) {
            data_group[3] = 5;
        }
        if ((data_character[3] >= 336) && (data_character[3] <= 1035)) {
            data_group[3] = 6;
        }
        if ((data_character[3] >= 1036) && (data_character[3] <= 1515)) {
            data_group[3] = 7;
        }
        if ((data_character[3] >= 1516) && (data_character[3] <= 1596)) {
            data_group[3] = 8;
        }
        if ((data_character[2] >= 0) && (data_character[2] <= 160)) {
            data_group[2] = 0;
        }
        if ((data_character[2] >= 161) && (data_character[2] <= 960)) {
            data_group[2] = 1;
        }
        if ((data_character[2] >= 961) && (data_character[2] <= 2014)) {
            data_group[2] = 2;
        }
        if ((data_character[2] >= 2015) && (data_character[2] <= 2714)) {
            data_group[2] = 3;
        }
        if ((data_character[2] >= 2715) && (data_character[2] <= 2840)) {
            data_group[2] = 4;
        }

        v_odd[0] = (data_character[0] - g_sum_table[data_group[0]]) / t_table[data_group[0]];
        v_even[0] = (data_character[0] - g_sum_table[data_group[0]]) % t_table[data_group[0]];
        v_odd[1] = (data_character[1] - g_sum_table[data_group[1]]) % t_table[data_group[1]];
        v_even[1] = (data_character[1] - g_sum_table[data_group[1]]) / t_table[data_group[1]];
        v_odd[3] = (data_character[3] - g_sum_table[data_group[3]]) % t_table[data_group[3]];
        v_even[3] = (data_character[3] - g_sum_table[data_group[3]]) / t_table[data_group[3]];
        v_odd[2] = (data_character[2] - g_sum_table[data_group[2]]) / t_table[data_group[2]];
        v_even[2] = (data_character[2] - g_sum_table[data_group[2]]) % t_table[data_group[2]];

        /* Use RSS subset width algorithm */
        for (i = 0; i < 4; i++) {
            if ((i == 0) || (i == 2)) {
                getWidths(v_odd[i], modules_odd[data_group[i]], 4, widest_odd[data_group[i]], 1);
                data_widths[0][i] = widths[0];
                data_widths[2][i] = widths[1];
                data_widths[4][i] = widths[2];
                data_widths[6][i] = widths[3];
                getWidths(v_even[i], modules_even[data_group[i]], 4, widest_even[data_group[i]], 0);
                data_widths[1][i] = widths[0];
                data_widths[3][i] = widths[1];
                data_widths[5][i] = widths[2];
                data_widths[7][i] = widths[3];
            } else {
                getWidths(v_odd[i], modules_odd[data_group[i]], 4, widest_odd[data_group[i]], 0);
                data_widths[0][i] = widths[0];
                data_widths[2][i] = widths[1];
                data_widths[4][i] = widths[2];
                data_widths[6][i] = widths[3];
                getWidths(v_even[i], modules_even[data_group[i]], 4, widest_even[data_group[i]], 1);
                data_widths[1][i] = widths[0];
                data_widths[3][i] = widths[1];
                data_widths[5][i] = widths[2];
                data_widths[7][i] = widths[3];
            }
        }

        checksum = 0;
        /* Calculate the checksum */
        for (i = 0; i < 8; i++) {
            checksum += checksum_weight[i] * data_widths[i][0];
            checksum += checksum_weight[i + 8] * data_widths[i][1];
            checksum += checksum_weight[i + 16] * data_widths[i][2];
            checksum += checksum_weight[i + 24] * data_widths[i][3];
        }
        checksum %= 79;

        /* Calculate the two check characters */
        if (checksum >= 8) {
            checksum++;
        }
        if (checksum >= 72) {
            checksum++;
        }
        c_left = checksum / 9;
        c_right = checksum % 9;

        encodeInfo.append("Checksum: ").append(Integer.toString(checksum)).append("\n");

        /* Put element widths together */
        total_widths[0] = 1;
        total_widths[1] = 1;
        total_widths[44] = 1;
        total_widths[45] = 1;
        for (i = 0; i < 8; i++) {
            total_widths[i + 2] = data_widths[i][0];
            total_widths[i + 15] = data_widths[7 - i][1];
            total_widths[i + 23] = data_widths[i][3];
            total_widths[i + 36] = data_widths[7 - i][2];
        }
        for (i = 0; i < 5; i++) {
            total_widths[i + 10] = finder_pattern[i + (5 * c_left)];
            total_widths[i + 31] = finder_pattern[(4 - i) + (5 * c_right)];
        }

        rowCount = 0;
        for (i = 0; i < 100; i++) {
            seperator[i] = false;
        }
        /* Put this data into the symbol */
        if (symbolType == gb14Mode.LINEAR) {
            writer = 0;
            latch = '0';
            for (i = 0; i < 46; i++) {
                for (j = 0; j < total_widths[i]; j++) {
                    if (latch == '1') {
                        setGridModule(rowCount, writer);
                    }
                    writer++;
                }
                if (latch == '1') {
                    latch = '0';
                } else {
                    latch = '1';
                }
            }
            if (symbolWidth < writer) {
                symbolWidth = writer;
            }

            if (linkageFlag) {
                /* separator pattern for composite symbol */
                for (i = 4; i < 92; i++) {
                    seperator[i] = (!(grid[0][i]));
                }
                latch = '1';
                for (i = 16; i < 32; i++) {
                    if (!(grid[0][i])) {
                        if (latch == '1') {
                            seperator[i] = true;
                            latch = '0';
                        } else {
                            seperator[i] = false;
                            latch = '1';
                        }
                    } else {
                        seperator[i] = false;
                        latch = '1';
                    }
                }
                latch = '1';
                for (i = 63; i < 78; i++) {
                    if (!(grid[0][i])) {
                        if (latch == '1') {
                            seperator[i] = true;
                            latch = '0';
                        } else {
                            seperator[i] = false;
                            latch = '1';
                        }
                    } else {
                        seperator[i] = false;
                        latch = '1';
                    }
                }
            }
            rowCount = rowCount + 1;

            count = 0;
            check_digit = 0;

            /* Calculate check digit from Annex A and place human readable text */
            readable = new StringBuilder("(01)");
            hrt = "";
            for (i = content.length(); i < 13; i++) {
                hrt += "0";
            }
            hrt += content;

            for (i = 0; i < 13; i++) {
                count += hrt.charAt(i) - '0';

                if ((i & 1) == 0) {
                    count += 2 * (hrt.charAt(i) - '0');
                }
            }

            check_digit = 10 - (count % 10);
            if (check_digit == 10) {
                check_digit = 0;
            }
            hrt += (char) (check_digit + '0');

            readable.append(hrt);
        }

        if (symbolType == gb14Mode.STACKED) {
            /* top row */
            writer = 0;
            latch = '0';
            for (i = 0; i < 23; i++) {
                for (j = 0; j < total_widths[i]; j++) {
                    if (latch == '1') {
                        setGridModule(rowCount, writer);
                    } else {
                        unsetGridModule(rowCount, writer);
                    }
                    writer++;
                }
                if (latch == '1') {
                    latch = '0';
                } else {
                    latch = '1';
                }
            }
            setGridModule(rowCount, writer);
            unsetGridModule(rowCount, writer + 1);

            /* bottom row */
            rowCount = rowCount + 2;
            setGridModule(rowCount, 0);
            unsetGridModule(rowCount, 1);
            writer = 0;
            latch = '1';
            for (i = 23; i < 46; i++) {
                for (j = 0; j < total_widths[i]; j++) {
                    if (latch == '1') {
                        setGridModule(rowCount, writer + 2);
                    } else {
                        unsetGridModule(rowCount, writer + 2);
                    }
                    writer++;
                }
                if (latch == '1') {
                    latch = '0';
                } else {
                    latch = '1';
                }
            }

            /* separator pattern */
            for (i = 4; i < 46; i++) {
                if (gridModuleIsSet(rowCount - 2, i) == gridModuleIsSet(rowCount, i)) {
                    if (!(gridModuleIsSet(rowCount - 2, i))) {
                        setGridModule(rowCount - 1, i);
                    }
                } else {
                    if (!(gridModuleIsSet(rowCount - 1, i - 1))) {
                        setGridModule(rowCount - 1, i);
                    }
                }
            }

            if (linkageFlag) {
                /* separator pattern for composite symbol */
                for (i = 4; i < 46; i++) {
                    seperator[i] = (!(grid[0][i]));
                }
                latch = '1';
                for (i = 16; i < 32; i++) {
                    if (!(grid[0][i])) {
                        if (latch == '1') {
                            seperator[i] = true;
                            latch = '0';
                        } else {
                            seperator[i] = false;
                            latch = '1';
                        }
                    } else {
                        seperator[i] = false;
                        latch = '1';
                    }
                }
            }
            rowCount = rowCount + 1;
            if (symbolWidth < 50) {
                symbolWidth = 50;
            }
        }

        if (symbolType == gb14Mode.OMNI) {
            /* top row */
            writer = 0;
            latch = '0';
            for (i = 0; i < 23; i++) {
                for (j = 0; j < total_widths[i]; j++) {
                    if (latch == '1') {
                        setGridModule(rowCount, writer);
                    } else {
                        unsetGridModule(rowCount, writer);
                    }
                    writer++;
                }
                latch = (latch == '1' ? '0' : '1');
            }
            setGridModule(rowCount, writer);
            unsetGridModule(rowCount, writer + 1);

            /* bottom row */
            rowCount = rowCount + 4;
            setGridModule(rowCount, 0);
            unsetGridModule(rowCount, 1);
            writer = 0;
            latch = '1';
            for (i = 23; i < 46; i++) {
                for (j = 0; j < total_widths[i]; j++) {
                    if (latch == '1') {
                        setGridModule(rowCount, writer + 2);
                    } else {
                        unsetGridModule(rowCount, writer + 2);
                    }
                    writer++;
                }
                if (latch == '1') {
                    latch = '0';
                } else {
                    latch = '1';
                }
            }

            /* middle separator */
            for (i = 5; i < 46; i += 2) {
                setGridModule(rowCount - 2, i);
            }

            /* top separator */
            for (i = 4; i < 46; i++) {
                if (!(gridModuleIsSet(rowCount - 4, i))) {
                    setGridModule(rowCount - 3, i);
                }
            }
            latch = '1';
            for (i = 17; i < 33; i++) {
                if (!(gridModuleIsSet(rowCount - 4, i))) {
                    if (latch == '1') {
                        setGridModule(rowCount - 3, i);
                        latch = '0';
                    } else {
                        unsetGridModule(rowCount - 3, i);
                        latch = '1';
                    }
                } else {
                    unsetGridModule(rowCount - 3, i);
                    latch = '1';
                }
            }

            /* bottom separator */
            for (i = 4; i < 46; i++) {
                if (!(gridModuleIsSet(rowCount, i))) {
                    setGridModule(rowCount - 1, i);
                }
            }
            latch = '1';
            for (i = 16; i < 32; i++) {
                if (!(gridModuleIsSet(rowCount, i))) {
                    if (latch == '1') {
                        setGridModule(rowCount - 1, i);
                        latch = '0';
                    } else {
                        unsetGridModule(rowCount - 1, i);
                        latch = '1';
                    }
                } else {
                    unsetGridModule(rowCount - 1, i);
                    latch = '1';
                }
            }

            if (symbolWidth < 50) {
                symbolWidth = 50;
            }
            if (linkageFlag) {
                /* separator pattern for composite symbol */
                for (i = 4; i < 46; i++) {
                    seperator[i] = (!(grid[0][i]));
                }
                latch = '1';
                for (i = 16; i < 32; i++) {
                    if (!(grid[0][i])) {
                        if (latch == '1') {
                            seperator[i] = true;
                            latch = '0';
                        } else {
                            seperator[i] = false;
                            latch = '1';
                        }
                    } else {
                        seperator[i] = false;
                        latch = '1';
                    }
                }
            }
            rowCount = rowCount + 1;
        }

        pattern = new String[rowCount + compositeOffset];
        rowHeight = new int[rowCount + compositeOffset];

        if (linkageFlag) {
            bin = "";
            for (j = 0; j < symbolWidth; j++) {
                if (seperator[j]) {
                    bin += "1";
                } else {
                    bin += "0";
                }
            }
            pattern[0] = bin2pat(bin);
            rowHeight[0] = 1;
        }

        for (i = 0; i < rowCount; i++) {
            bin = "";
            for (j = 0; j < symbolWidth; j++) {
                if (grid[i][j]) {
                    bin += "1";
                } else {
                    bin += "0";
                }
            }
            pattern[i + compositeOffset] = bin2pat(bin);
        }

        if (symbolType == gb14Mode.LINEAR) {
            rowHeight[0 + compositeOffset] = -1;
        }
        if (symbolType == gb14Mode.STACKED) {
            rowHeight[0 + compositeOffset] = 5;
            rowHeight[1 + compositeOffset] = 1;
            rowHeight[2 + compositeOffset] = 7;
        }
        if (symbolType == gb14Mode.OMNI) {
            rowHeight[0 + compositeOffset] = -1;
            rowHeight[1 + compositeOffset] = 1;
            rowHeight[2 + compositeOffset] = 1;
            rowHeight[3 + compositeOffset] = 1;
            rowHeight[4 + compositeOffset] = -1;
        }

        if (linkageFlag) {
            rowCount++;
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

    private void setGridModule(int row, int column) {
        grid[row][column] = true;
    }

    private void unsetGridModule(int row, int column) {
        grid[row][column] = false;
    }

    private boolean gridModuleIsSet(int row, int column) {
        return grid[row][column];
    }

    private enum gb14Mode {
        LINEAR, OMNI, STACKED
    }
}
