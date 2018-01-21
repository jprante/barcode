package org.xbib.graphics.barcode;

/**
 * Implements GS1 DataBar Expanded Omnidirectional and GS1 Expanded Stacked
 * Omnidirectional according to ISO/IEC 24724:2011.
 * DataBar expanded encodes GS1 data in either a linear or stacked
 * format.
 */
public class DataBarExpanded extends Symbol {

    private static final int[] G_SUM_EXP = {
            0, 348, 1388, 2948, 3988
    };
    private static final int[] T_EVEN_EXP = {
            4, 20, 52, 104, 204
    };
    private static final int[] MODULES_ODD_EXP = {
            12, 10, 8, 6, 4
    };
    private static final int[] MODULES_EVEN_EXP = {
            5, 7, 9, 11, 13
    };
    private static final int[] WIDEST_ODD_EXP = {
            7, 5, 4, 3, 1
    };
    private static final int[] WIDEST_EVEN_EXP = {
            2, 4, 5, 6, 8
    };
    private static final int[] CHECKSUM_WEIGHT_EXP = { /* Table 14 */
            1, 3, 9, 27, 81, 32, 96, 77, 20, 60, 180, 118, 143, 7, 21, 63, 189,
            145, 13, 39, 117, 140, 209, 205, 193, 157, 49, 147, 19, 57, 171, 91,
            62, 186, 136, 197, 169, 85, 44, 132, 185, 133, 188, 142, 4, 12, 36,
            108, 113, 128, 173, 97, 80, 29, 87, 50, 150, 28, 84, 41, 123, 158, 52,
            156, 46, 138, 203, 187, 139, 206, 196, 166, 76, 17, 51, 153, 37, 111,
            122, 155, 43, 129, 176, 106, 107, 110, 119, 146, 16, 48, 144, 10, 30,
            90, 59, 177, 109, 116, 137, 200, 178, 112, 125, 164, 70, 210, 208, 202,
            184, 130, 179, 115, 134, 191, 151, 31, 93, 68, 204, 190, 148, 22, 66,
            198, 172, 94, 71, 2, 6, 18, 54, 162, 64, 192, 154, 40, 120, 149, 25,
            75, 14, 42, 126, 167, 79, 26, 78, 23, 69, 207, 199, 175, 103, 98, 83,
            38, 114, 131, 182, 124, 161, 61, 183, 127, 170, 88, 53, 159, 55, 165,
            73, 8, 24, 72, 5, 15, 45, 135, 194, 160, 58, 174, 100, 89
    };
    private static final int[] FINDER_PATTERN_EXP = { /* Table 15 */
            1, 8, 4, 1, 1, 1, 1, 4, 8, 1, 3, 6, 4, 1, 1, 1, 1, 4, 6, 3, 3, 4, 6, 1,
            1, 1, 1, 6, 4, 3, 3, 2, 8, 1, 1, 1, 1, 8, 2, 3, 2, 6, 5, 1, 1, 1, 1, 5,
            6, 2, 2, 2, 9, 1, 1, 1, 1, 9, 2, 2
    };
    private static final int[] FINDER_SEQUENCE = { /* Table 16 */
            1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 4, 3, 0, 0, 0, 0, 0, 0, 0, 0, 1, 6,
            3, 8, 0, 0, 0, 0, 0, 0, 0, 1, 10, 3, 8, 5, 0, 0, 0, 0, 0, 0, 1, 10, 3,
            8, 7, 12, 0, 0, 0, 0, 0, 1, 10, 3, 8, 9, 12, 11, 0, 0, 0, 0, 1, 2, 3,
            4, 5, 6, 7, 8, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 10, 9, 0, 0, 1, 2, 3, 4,
            5, 6, 7, 10, 11, 12, 0, 1, 2, 3, 4, 5, 8, 7, 10, 9, 12, 11
    };
    private static final int[] WEIGHT_ROWS = {
            0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 6,
            3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 10, 3, 4,
            13, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 18, 3, 4, 13,
            14, 7, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 18, 3, 4, 13, 14,
            11, 12, 21, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 18, 3, 4, 13, 14,
            15, 16, 21, 22, 19, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7,
            8, 9, 10, 11, 12, 13, 14, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 10, 11, 12, 17, 18, 15, 16, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 10, 11, 12, 17, 18, 19, 20, 21, 22, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8,
            13, 14, 11, 12, 17, 18, 15, 16, 21, 22, 19, 20
    };

    private String source;
    private StringBuilder binaryString = new StringBuilder();
    private String generalField;
    private EncodeMode[] generalFieldType;
    private int[] widths = new int[8];
    private boolean linkageFlag;

    private int preferredNoOfColumns = 0;
    private dbeMode symbolType;

    public DataBarExpanded() {
        linkageFlag = false;
        inputDataType = DataType.GS1;
    }

    private static int calculateRemainder(int binaryStringLength) {
        int remainder = 12 - (binaryStringLength % 12);
        if (remainder == 12) {
            remainder = 0;
        }
        if (binaryStringLength < 36) {
            remainder = 36 - binaryStringLength;
        }
        return remainder;
    }

    /**
     * Set the width of a stacked symbol by selecting the number
     * of "columns" or symbol segments in each row of data.
     *
     * @param columns Number of segments in each row
     */
    public void setNoOfColumns(int columns) {
        preferredNoOfColumns = columns;
    }

    ;

    @Override
    public void setDataType(DataType dummy) {
        // Do nothing!
    }

    /**
     * Set symbology to DataBar Expanded Stacked
     */
    public void setStacked() {
        symbolType = dbeMode.STACKED;
    }

    /**
     * Set symbology to DataBar Expanded
     */
    public void setNotStacked() {
        symbolType = dbeMode.UNSTACKED;
    }

    protected void setLinkageFlag() {
        linkageFlag = true;
    }

    protected void unsetLinkageFlag() {
        linkageFlag = false;
    }

    @Override
    public boolean encode() {
        int i;
        int j;
        int k;
        int dataChars;
        int[] vs = new int[21];
        int[] group = new int[21];
        int[] vOdd = new int[21];
        int[] vEven = new int[21];
        int[][] charWidths = new int[21][8];
        int checksum;
        int row;
        int checkChar;
        int cGroup;
        int cOdd;
        int cEven;
        int[] checkWidths = new int[8];
        int patternWidth;
        int[] elements = new int[235];
        int codeblocks;
        int stackRows;
        int blocksPerRow;
        int currentBlock;
        int currentRow;
        boolean specialCaseRow;
        int elementsInSub;
        int reader;
        int writer;
        int[] subElements = new int[235];
        int l;
        int symbolRow;
        String seperatorBinary;
        String seperatorPattern;
        boolean black;
        boolean leftToRight;
        int compositeOffset;

        source = content;

        if (linkageFlag) {
            binaryString = new StringBuilder("1");
            compositeOffset = 1;
        } else {
            binaryString = new StringBuilder("0");
            compositeOffset = 0;
        }
        if (!calculateBinaryString()) {
            return false;
        }

        dataChars = binaryString.length() / 12;

        encodeInfo.append("Data characters: ");
        for (i = 0; i < dataChars; i++) {
            vs[i] = 0;
            for (j = 0; j < 12; j++) {
                if (binaryString.charAt((i * 12) + j) == '1') {
                    vs[i] += 2048 >> j;
                }
            }
            encodeInfo.append(Integer.toString(vs[i])).append(" ");
        }
        encodeInfo.append("\n");

        for (i = 0; i < dataChars; i++) {
            if (vs[i] <= 347) {
                group[i] = 1;
            }
            if ((vs[i] >= 348) && (vs[i] <= 1387)) {
                group[i] = 2;
            }
            if ((vs[i] >= 1388) && (vs[i] <= 2947)) {
                group[i] = 3;
            }
            if ((vs[i] >= 2948) && (vs[i] <= 3987)) {
                group[i] = 4;
            }
            if (vs[i] >= 3988) {
                group[i] = 5;
            }
            vOdd[i] = (vs[i] - G_SUM_EXP[group[i] - 1]) / T_EVEN_EXP[group[i] - 1];
            vEven[i] = (vs[i] - G_SUM_EXP[group[i] - 1]) % T_EVEN_EXP[group[i] - 1];

            getWidths(vOdd[i], MODULES_ODD_EXP[group[i] - 1], 4, WIDEST_ODD_EXP[group[i] - 1], 0);
            charWidths[i][0] = widths[0];
            charWidths[i][2] = widths[1];
            charWidths[i][4] = widths[2];
            charWidths[i][6] = widths[3];
            getWidths(vEven[i], MODULES_EVEN_EXP[group[i] - 1], 4, WIDEST_EVEN_EXP[group[i] - 1], 1);
            charWidths[i][1] = widths[0];
            charWidths[i][3] = widths[1];
            charWidths[i][5] = widths[2];
            charWidths[i][7] = widths[3];
        }

        /* 7.2.6 Check character */
        /* The checksum value is equal to the mod 211 residue of the weighted sum of the widths of the
	   elements in the data characters. */
        checksum = 0;
        for (i = 0; i < dataChars; i++) {
            row = WEIGHT_ROWS[(((dataChars - 2) / 2) * 21) + i];
            for (j = 0; j < 8; j++) {
                checksum += (charWidths[i][j] * CHECKSUM_WEIGHT_EXP[(row * 8) + j]);

            }
        }

        checkChar = (211 * ((dataChars + 1) - 4)) + (checksum % 211);

        encodeInfo.append("Check Character: ").append(Integer.toString(checkChar)).append("\n");

        cGroup = 1;
        if ((checkChar >= 348) && (checkChar <= 1387)) {
            cGroup = 2;
        }
        if ((checkChar >= 1388) && (checkChar <= 2947)) {
            cGroup = 3;
        }
        if ((checkChar >= 2948) && (checkChar <= 3987)) {
            cGroup = 4;
        }
        if (checkChar >= 3988) {
            cGroup = 5;
        }

        cOdd = (checkChar - G_SUM_EXP[cGroup - 1]) / T_EVEN_EXP[cGroup - 1];
        cEven = (checkChar - G_SUM_EXP[cGroup - 1]) % T_EVEN_EXP[cGroup - 1];

        getWidths(cOdd, MODULES_ODD_EXP[cGroup - 1], 4, WIDEST_ODD_EXP[cGroup - 1], 0);
        checkWidths[0] = widths[0];
        checkWidths[2] = widths[1];
        checkWidths[4] = widths[2];
        checkWidths[6] = widths[3];
        getWidths(cEven, MODULES_EVEN_EXP[cGroup - 1], 4, WIDEST_EVEN_EXP[cGroup - 1], 1);
        checkWidths[1] = widths[0];
        checkWidths[3] = widths[1];
        checkWidths[5] = widths[2];
        checkWidths[7] = widths[3];

        /* Initialise element array */
        patternWidth = ((((dataChars + 1) / 2) + ((dataChars + 1) & 1)) * 5) + ((dataChars + 1) * 8) + 4;
        for (i = 0; i < patternWidth; i++) {
            elements[i] = 0;
        }

        elements[0] = 1;
        elements[1] = 1;
        elements[patternWidth - 2] = 1;
        elements[patternWidth - 1] = 1;

        /* Put finder patterns in element array */
        for (i = 0; i < (((dataChars + 1) / 2) + ((dataChars + 1) & 1)); i++) {
            k = ((((((dataChars + 1) - 2) / 2) + ((dataChars + 1) & 1)) - 1) * 11) + i;
            for (j = 0; j < 5; j++) {
                elements[(21 * i) + j + 10] = FINDER_PATTERN_EXP[((FINDER_SEQUENCE[k] - 1) * 5) + j];
            }
        }

        /* Put check character in element array */
        for (i = 0; i < 8; i++) {
            elements[i + 2] = checkWidths[i];
        }

        /* Put forward reading data characters in element array */
        for (i = 1; i < dataChars; i += 2) {
            for (j = 0; j < 8; j++) {
                elements[(((i - 1) / 2) * 21) + 23 + j] = charWidths[i][j];
            }
        }

        /* Put reversed data characters in element array */
        for (i = 0; i < dataChars; i += 2) {
            for (j = 0; j < 8; j++) {
                elements[((i / 2) * 21) + 15 + j] = charWidths[i][7 - j];
            }
        }


        if (symbolType == dbeMode.UNSTACKED) {
            /* Copy elements into symbol */
            rowCount = 1 + compositeOffset;
            rowHeight = new int[1 + compositeOffset];
            rowHeight[0 + compositeOffset] = -1;
            pattern = new String[1 + compositeOffset];

            pattern[0 + compositeOffset] = "0";
            writer = 0;
            black = false;
            seperatorBinary = "";
            for (i = 0; i < patternWidth; i++) {
                pattern[0 + compositeOffset] += (char) (elements[i] + '0');
                for (j = 0; j < elements[i]; j++) {
                    if (black) {
                        seperatorBinary += "0";
                    } else {
                        seperatorBinary += "1";
                    }
                }

                black = !(black);
                writer += elements[i];
            }
            seperatorBinary = "0000" + seperatorBinary.substring(4, writer - 4);
            for (j = 0; j < (writer / 49); j++) {
                k = (49 * j) + 18;
                for (i = 0; i < 15; i++) {
                    if ((seperatorBinary.charAt(i + k - 1) == '1')
                            && (seperatorBinary.charAt(i + k) == '1')) {
                        seperatorBinary = seperatorBinary.substring(0, (i + k))
                                + "0" + seperatorBinary.substring(i + k + 1);
                    }
                }
            }
            if (linkageFlag) {
                // Add composite code seperator
                pattern[0] = bin2pat(seperatorBinary);
                rowHeight[0] = 1;
            }

        } else {
            /* RSS Expanded Stacked */
            codeblocks = (dataChars + 1) / 2 + ((dataChars + 1) % 2);

            blocksPerRow = preferredNoOfColumns;
            if ((blocksPerRow < 1) || (blocksPerRow > 10)) {
                blocksPerRow = 2;
            }

            if (linkageFlag && (blocksPerRow == 1)) {
                /* "There shall be a minimum of four symbol characters in the
                first row of an RSS Expanded Stacked symbol when it is the linear
                component of an EAN.UCC Composite symbol." */
                blocksPerRow = 2;
            }

            stackRows = codeblocks / blocksPerRow;
            if (codeblocks % blocksPerRow > 0) {
                stackRows++;
            }

            rowCount = (stackRows * 4) - 3;
            rowHeight = new int[rowCount + compositeOffset];
            pattern = new String[rowCount + compositeOffset];
            symbolRow = 0;

            currentBlock = 0;
            for (currentRow = 1; currentRow <= stackRows; currentRow++) {
                for (i = 0; i < 235; i++) {
                    subElements[i] = 0;
                }
                specialCaseRow = false;

                /* Row Start */
                subElements[0] = 1;
                subElements[1] = 1;
                elementsInSub = 2;

                /* Row Data */
                reader = 0;
                do {
                    if ((((blocksPerRow & 1) != 0) || ((currentRow & 1) != 0))
                            || ((currentRow == stackRows)
                            && (codeblocks != (currentRow * blocksPerRow))
                            && ((((currentRow * blocksPerRow) - codeblocks) & 1)) != 0)) {
                        /* left to right */
                        leftToRight = true;
                        i = 2 + (currentBlock * 21);
                        for (j = 0; j < 21; j++) {
                            if ((i + j) < patternWidth) {
                                subElements[j + (reader * 21) + 2] = elements[i + j];
                                elementsInSub++;
                            }
                        }
                    } else {
                        /* right to left */
                        leftToRight = false;
                        if ((currentRow * blocksPerRow) < codeblocks) {
                            /* a full row */
                            i = 2 + (((currentRow * blocksPerRow) - reader - 1) * 21);
                            for (j = 0; j < 21; j++) {
                                if ((i + j) < patternWidth) {
                                    subElements[(20 - j) + (reader * 21) + 2] = elements[i + j];
                                    elementsInSub++;
                                }
                            }
                        } else {
                            /* a partial row */
                            k = ((currentRow * blocksPerRow) - codeblocks);
                            l = (currentRow * blocksPerRow) - reader - 1;
                            i = 2 + ((l - k) * 21);
                            for (j = 0; j < 21; j++) {
                                if ((i + j) < patternWidth) {
                                    subElements[(20 - j) + (reader * 21) + 2] = elements[i + j];
                                    elementsInSub++;
                                }
                            }
                        }
                    }
                    reader++;
                    currentBlock++;
                } while ((reader < blocksPerRow) && (currentBlock < codeblocks));

                /* Row Stop */
                subElements[elementsInSub] = 1;
                subElements[elementsInSub + 1] = 1;
                elementsInSub += 2;

                pattern[symbolRow + compositeOffset] = "";
                black = true;
                rowHeight[symbolRow + compositeOffset] = -1;

                if ((currentRow & 1) != 0) {
                    pattern[symbolRow + compositeOffset] = "0";
                    black = false;
                } else {
                    if ((currentRow == stackRows)
                            && (codeblocks != (currentRow * blocksPerRow))
                            && ((((currentRow * blocksPerRow) - codeblocks) & 1) != 0)) {
                        /* Special case bottom row */
                        specialCaseRow = true;
                        subElements[0] = 2;
                        pattern[symbolRow + compositeOffset] = "0";
                        black = false;
                    }
                }

                writer = 0;

                seperatorBinary = "";
                for (i = 0; i < elementsInSub; i++) {
                    pattern[symbolRow + compositeOffset] += (char) (subElements[i] + '0');
                    for (j = 0; j < subElements[i]; j++) {
                        if (black) {
                            seperatorBinary += "0";
                        } else {
                            seperatorBinary += "1";
                        }
                    }

                    black = !(black);
                    writer += subElements[i];
                }
                seperatorBinary = "0000" + seperatorBinary.substring(4, writer - 4);
                for (j = 0; j < reader; j++) {
                    k = (49 * j) + (specialCaseRow ? 19 : 18);
                    if (leftToRight) {
                        for (i = 0; i < 15; i++) {
                            if ((seperatorBinary.charAt(i + k - 1) == '1')
                                    && (seperatorBinary.charAt(i + k) == '1')) {
                                seperatorBinary = seperatorBinary.substring(0, (i + k))
                                        + "0" + seperatorBinary.substring(i + k + 1);
                            }
                        }
                    } else {
                        for (i = 14; i >= 0; i--) {
                            if ((seperatorBinary.charAt(i + k + 1) == '1')
                                    && (seperatorBinary.charAt(i + k) == '1')) {
                                seperatorBinary = seperatorBinary.substring(0, (i + k))
                                        + "0" + seperatorBinary.substring(i + k + 1);
                            }
                        }
                    }
                }
                seperatorPattern = bin2pat(seperatorBinary);

                if ((currentRow == 1) && linkageFlag) {
                    // Add composite code seperator
                    rowHeight[0] = 1;
                    pattern[0] = seperatorPattern;
                }

                if (currentRow != 1) {
                    /* middle separator pattern (above current row) */
                    pattern[symbolRow - 2 + compositeOffset] = "05";
                    for (j = 5; j < (49 * blocksPerRow); j += 2) {
                        pattern[symbolRow - 2 + compositeOffset] += "11";
                    }
                    rowHeight[symbolRow - 2 + compositeOffset] = 1;
                    /* bottom separator pattern (above current row) */
                    rowHeight[symbolRow - 1 + compositeOffset] = 1;
                    pattern[symbolRow - 1 + compositeOffset] = seperatorPattern;
                }

                if (currentRow != stackRows) {
                    rowHeight[symbolRow + 1 + compositeOffset] = 1;
                    pattern[symbolRow + 1 + compositeOffset] = seperatorPattern;
                }

                symbolRow += 4;
            }
            readable = new StringBuilder();
            rowCount += compositeOffset;
        }

        plotSymbol();
        return true;
    }

    private boolean calculateBinaryString() {
        /* Handles all data encodation from section 7.2.5 of ISO/IEC 24724 */
        EncodeMode lastMode = EncodeMode.NUMERIC;
        int encodingMethod, i, j, readPosn;
        boolean latch;
        int remainder, d1, d2, value;
        StringBuilder padstring;
        double weight;
        int groupVal;
        int currentLength;
        String patch;

        readPosn = 0;

        /* Decide whether a compressed data field is required and if so what
	method to use - method 2 = no compressed data field */

        if ((source.length() >= 16) && ((source.charAt(0) == '0')
                && (source.charAt(1) == '1'))) {
            /* (01) and other AIs */
            encodingMethod = 1;
        } else {
            /* any AIs */
            encodingMethod = 2;
        }

        if (((source.length() >= 20) && (encodingMethod == 1))
                && ((source.charAt(2) == '9') && (source.charAt(16) == '3'))) {
            /* Possibly encoding method > 2 */

            if ((source.length() >= 26) && (source.charAt(17) == '1')) {
                /* Methods 3, 7, 9, 11 and 13 */

                if (source.charAt(18) == '0') {
                    /* (01) and (310x) */
                    /* In kilos */

                    weight = 0.0;
                    for (i = 0; i < 6; i++) {
                        weight *= 10;
                        weight += (source.charAt(20 + i) - '0');
                    }

                    if (weight < 99999.0) { /* Maximum weight = 99999 */

                        if ((source.charAt(19) == '3') && (source.length() == 26)) {
                            /* (01) and (3103) */
                            weight /= 1000.0;

                            if (weight <= 32.767) {
                                encodingMethod = 3;
                            }
                        }

                        if (source.length() == 34) {
                            if ((source.charAt(26) == '1') && (source.charAt(27) == '1')) {
                                /* (01), (310x) and (11) - metric weight and production date */
                                encodingMethod = 7;
                            }

                            if ((source.charAt(26) == '1') && (source.charAt(27) == '3')) {
                                /* (01), (310x) and (13) - metric weight and packaging date */
                                encodingMethod = 9;
                            }

                            if ((source.charAt(26) == '1') && (source.charAt(27) == '5')) {
                                /* (01), (310x) and (15) - metric weight and "best before" date */
                                encodingMethod = 11;
                            }

                            if ((source.charAt(26) == '1') && (source.charAt(27) == '7')) {
                                /* (01), (310x) and (17) - metric weight and expiration date */
                                encodingMethod = 13;
                            }
                        }
                    }
                }
            }

            if ((source.length() >= 26) && (source.charAt(17) == '2')) {
                /* Methods 4, 8, 10, 12 and 14 */

                if (source.charAt(18) == '0') {
                    /* (01) and (320x) */
                    /* In pounds */

                    weight = 0.0;
                    for (i = 0; i < 6; i++) {
                        weight *= 10;
                        weight += (source.charAt(20 + i) - '0');
                    }

                    if (weight < 99999.0) { /* Maximum weight = 99999 */

                        if (((source.charAt(19) == '2') || (source.charAt(19) == '3'))
                                && (source.length() == 26)) {
                            /* (01) and (3202)/(3203) */

                            if (source.charAt(19) == '3') {
                                weight /= 1000.0;
                                if (weight <= 22.767) {
                                    encodingMethod = 4;
                                }
                            } else {
                                weight /= 100.0;
                                if (weight <= 99.99) {
                                    encodingMethod = 4;
                                }
                            }

                        }

                        if (source.length() == 34) {
                            if ((source.charAt(26) == '1') && (source.charAt(27) == '1')) {
                                /* (01), (320x) and (11) - English weight and production date */
                                encodingMethod = 8;
                            }

                            if ((source.charAt(26) == '1') && (source.charAt(27) == '3')) {
                                /* (01), (320x) and (13) - English weight and packaging date */
                                encodingMethod = 10;
                            }

                            if ((source.charAt(26) == '1') && (source.charAt(27) == '5')) {
                                /* (01), (320x) and (15) - English weight and "best before" date */
                                encodingMethod = 12;
                            }

                            if ((source.charAt(26) == '1') && (source.charAt(27) == '7')) {
                                /* (01), (320x) and (17) - English weight and expiration date */
                                encodingMethod = 14;
                            }
                        }
                    }
                }
            }

            if (source.charAt(17) == '9') {
                /* Methods 5 and 6 */
                if ((source.charAt(18) == '2') && ((source.charAt(19) >= '0')
                        && (source.charAt(19) <= '3'))) {
                    /* (01) and (392x) */
                    encodingMethod = 5;
                }
                if ((source.charAt(18) == '3') && ((source.charAt(19) >= '0')
                        && (source.charAt(19) <= '3'))) {
                    /* (01) and (393x) */
                    encodingMethod = 6;
                }
            }
        }

        encodeInfo.append("Encoding Method: ").append(Integer.toString(encodingMethod)).append("\n");
        switch (encodingMethod) { /* Encoding method - Table 10 */
            case 1:
                binaryString.append("1XX");
                readPosn = 16;
                break;
            case 2:
                binaryString.append("00XX");
                readPosn = 0;
                break;
            case 3:
                binaryString.append("0100");
                readPosn = source.length();
                break;
            case 4:
                binaryString.append("0101");
                readPosn = source.length();
                break;
            case 5:
                binaryString.append("01100XX");
                readPosn = 20;
                break;
            case 6:
                binaryString.append("01101XX");
                readPosn = 23;
                break;
            case 7:
                binaryString.append("0111000");
                readPosn = source.length();
                break;
            case 8:
                binaryString.append("0111001");
                readPosn = source.length();
                break;
            case 9:
                binaryString.append("0111010");
                readPosn = source.length();
                break;
            case 10:
                binaryString.append("0111011");
                readPosn = source.length();
                break;
            case 11:
                binaryString.append("0111100");
                readPosn = source.length();
                break;
            case 12:
                binaryString.append("0111101");
                readPosn = source.length();
                break;
            case 13:
                binaryString.append("0111110");
                readPosn = source.length();
                break;
            case 14:
                binaryString.append("0111111");
                readPosn = source.length();
                break;
        }


        /* Variable length symbol bit field is just given a place holder (XX)
	for the time being */

        /* Verify that the data to be placed in the compressed data field is all
	numeric data before carrying out compression */
        for (i = 0; i < readPosn; i++) {
            if ((source.charAt(i) < '0') || (source.charAt(i) > '9')) {
                if ((source.charAt(i) != '[') && (source.charAt(i) != ']')) {
                    /* Something is wrong */
                    errorMsg.append("Invalid characters in input data");
                    return false;
                }
            }
        }

        /* Now encode the compressed data field */

        if (encodingMethod == 1) {
            /* Encoding method field "1" - general item identification data */
            groupVal = source.charAt(2) - '0';

            for (j = 0; j < 4; j++) {
                if ((groupVal & (0x08 >> j)) == 0) {
                    binaryString.append("0");
                } else {
                    binaryString.append("1");
                }
            }

            for (i = 1; i < 5; i++) {
                groupVal = 100 * (source.charAt(i * 3) - '0');
                groupVal += 10 * (source.charAt((i * 3) + 1) - '0');
                groupVal += source.charAt((i * 3) + 2) - '0';

                for (j = 0; j < 10; j++) {
                    if ((groupVal & (0x200 >> j)) == 0) {
                        binaryString.append("0");
                    } else {
                        binaryString.append("1");
                    }
                }
            }
        }

        if (encodingMethod == 3) {
            /* Encoding method field "0100" - variable weight item
		(0,001 kilogram icrements) */

            for (i = 1; i < 5; i++) {
                groupVal = 100 * (source.charAt(i * 3) - '0');
                groupVal += 10 * (source.charAt((i * 3) + 1) - '0');
                groupVal += (source.charAt((i * 3) + 2) - '0');

                for (j = 0; j < 10; j++) {
                    if ((groupVal & (0x200 >> j)) == 0) {
                        binaryString.append("0");
                    } else {
                        binaryString.append("1");
                    }
                }
            }

            groupVal = 0;
            for (i = 0; i < 6; i++) {
                groupVal *= 10;
                groupVal += source.charAt(20 + i) - '0';
            }

            for (j = 0; j < 15; j++) {
                if ((groupVal & (0x4000 >> j)) == 0) {
                    binaryString.append("0");
                } else {
                    binaryString.append("1");
                }
            }
        }

        if (encodingMethod == 4) {
            /* Encoding method field "0101" - variable weight item (0,01 or
		0,001 pound increment) */

            for (i = 1; i < 5; i++) {
                groupVal = 100 * (source.charAt(i * 3) - '0');
                groupVal += 10 * (source.charAt((i * 3) + 1) - '0');
                groupVal += (source.charAt((i * 3) + 2) - '0');

                for (j = 0; j < 10; j++) {
                    if ((groupVal & (0x200 >> j)) == 0) {
                        binaryString.append("0");
                    } else {
                        binaryString.append("1");
                    }
                }
            }


            groupVal = 0;
            for (i = 0; i < 6; i++) {
                groupVal *= 10;
                groupVal += source.charAt(20 + i) - '0';
            }

            if (source.charAt(19) == '3') {
                groupVal = groupVal + 10000;
            }

            for (j = 0; j < 15; j++) {
                if ((groupVal & (0x4000 >> j)) == 0) {
                    binaryString.append("0");
                } else {
                    binaryString.append("1");
                }
            }
        }

        if (encodingMethod >= 7) {
            /* Encoding method fields "0111000" through "0111111" - variable
		weight item plus date */

            for (i = 1; i < 5; i++) {
                groupVal = 100 * (source.charAt(i * 3) - '0');
                groupVal += 10 * (source.charAt((i * 3) + 1) - '0');
                groupVal += (source.charAt((i * 3) + 2) - '0');

                for (j = 0; j < 10; j++) {
                    if ((groupVal & (0x200 >> j)) == 0) {
                        binaryString.append("0");
                    } else {
                        binaryString.append("1");
                    }
                }
            }

            groupVal = source.charAt(19) - '0';

            for (i = 0; i < 5; i++) {
                groupVal *= 10;
                groupVal += source.charAt(21 + i) - '0';
            }

            for (j = 0; j < 20; j++) {
                if ((groupVal & (0x80000 >> j)) == 0) {
                    binaryString.append("0");
                } else {
                    binaryString.append("1");
                }
            }

            if (source.length() == 34) {
                /* Date information is included */
                groupVal = ((10 * (source.charAt(28) - '0'))
                        + (source.charAt(29) - '0')) * 384;
                groupVal += (((10 * (source.charAt(30) - '0'))
                        + (source.charAt(31) - '0')) - 1) * 32;
                groupVal += (10 * (source.charAt(32) - '0'))
                        + (source.charAt(33) - '0');
            } else {
                groupVal = 38400;
            }

            for (j = 0; j < 16; j++) {
                if ((groupVal & (0x8000 >> j)) == 0) {
                    binaryString.append("0");
                } else {
                    binaryString.append("1");
                }
            }
        }

        if (encodingMethod == 5) {
            /* Encoding method field "01100" - variable measure item and price */

            for (i = 1; i < 5; i++) {
                groupVal = 100 * (source.charAt(i * 3) - '0');
                groupVal += 10 * (source.charAt((i * 3) + 1) - '0');
                groupVal += (source.charAt((i * 3) + 2) - '0');

                for (j = 0; j < 10; j++) {
                    if ((groupVal & (0x200 >> j)) == 0) {
                        binaryString.append("0");
                    } else {
                        binaryString.append("1");
                    }
                }
            }

            switch (source.charAt(19)) {
                case '0':
                    binaryString.append("00");
                    break;
                case '1':
                    binaryString.append("01");
                    break;
                case '2':
                    binaryString.append("10");
                    break;
                case '3':
                    binaryString.append("11");
                    break;
            }
        }

        if (encodingMethod == 6) {
            /* Encoding method "01101" - variable measure item and price with ISO 4217
		Currency Code */

            for (i = 1; i < 5; i++) {
                groupVal = 100 * (source.charAt(i * 3) - '0');
                groupVal += 10 * (source.charAt((i * 3) + 1) - '0');
                groupVal += (source.charAt((i * 3) + 2) - '0');

                for (j = 0; j < 10; j++) {
                    if ((groupVal & (0x200 >> j)) == 0) {
                        binaryString.append("0");
                    } else {
                        binaryString.append("1");
                    }
                }
            }

            switch (source.charAt(19)) {
                case '0':
                    binaryString.append("00");
                    break;
                case '1':
                    binaryString.append("01");
                    break;
                case '2':
                    binaryString.append("10");
                    break;
                case '3':
                    binaryString.append("11");
                    break;
            }

            groupVal = 0;
            for (i = 0; i < 3; i++) {
                groupVal *= 10;
                groupVal += source.charAt(20 + i) - '0';
            }

            for (j = 0; j < 10; j++) {
                if ((groupVal & (0x200 >> j)) == 0) {
                    binaryString.append("0");
                } else {
                    binaryString.append("1");
                }
            }
        }

        /* The compressed data field has been processed if appropriate - the
	rest of the data (if any) goes into a general-purpose data compaction field */

        generalField = source.substring(readPosn);
        generalFieldType = new EncodeMode[generalField.length()];

        if (generalField.length() != 0) {
            latch = false;
            for (i = 0; i < generalField.length(); i++) {
                /* Table 13 - ISO/IEC 646 encodation */
                if ((generalField.charAt(i) < ' ') || (generalField.charAt(i) > 'z')) {
                    generalFieldType[i] = EncodeMode.INVALID_CHAR;
                    latch = true;
                } else {
                    generalFieldType[i] = EncodeMode.ISOIEC;
                }

                if (generalField.charAt(i) == '#') {
                    generalFieldType[i] = EncodeMode.INVALID_CHAR;
                    latch = true;
                }
                if (generalField.charAt(i) == '$') {
                    generalFieldType[i] = EncodeMode.INVALID_CHAR;
                    latch = true;
                }
                if (generalField.charAt(i) == '@') {
                    generalFieldType[i] = EncodeMode.INVALID_CHAR;
                    latch = true;
                }
                if (generalField.charAt(i) == 92) {
                    generalFieldType[i] = EncodeMode.INVALID_CHAR;
                    latch = true;
                }
                if (generalField.charAt(i) == '^') {
                    generalFieldType[i] = EncodeMode.INVALID_CHAR;
                    latch = true;
                }
                if (generalField.charAt(i) == 96) {
                    generalFieldType[i] = EncodeMode.INVALID_CHAR;
                    latch = true;
                }

                /* Table 12 - Alphanumeric encodation */
                if ((generalField.charAt(i) >= 'A') && (generalField.charAt(i) <= 'Z')) {
                    generalFieldType[i] = EncodeMode.ALPHA_OR_ISO;
                }
                if (generalField.charAt(i) == '*') {
                    generalFieldType[i] = EncodeMode.ALPHA_OR_ISO;
                }
                if (generalField.charAt(i) == ',') {
                    generalFieldType[i] = EncodeMode.ALPHA_OR_ISO;
                }
                if (generalField.charAt(i) == '-') {
                    generalFieldType[i] = EncodeMode.ALPHA_OR_ISO;
                }
                if (generalField.charAt(i) == '.') {
                    generalFieldType[i] = EncodeMode.ALPHA_OR_ISO;
                }
                if (generalField.charAt(i) == '/') {
                    generalFieldType[i] = EncodeMode.ALPHA_OR_ISO;
                }

                /* Numeric encodation */
                if ((generalField.charAt(i) >= '0') && (generalField.charAt(i) <= '9')) {
                    generalFieldType[i] = EncodeMode.ANY_ENC;
                }
                if (generalField.charAt(i) == '[') {
                    /* FNC1 can be encoded in any system */
                    generalFieldType[i] = EncodeMode.ANY_ENC;
                }
            }

            if (latch) {
                errorMsg.append("Invalid characters in input data");
                return false;
            }

            for (i = 0; i < generalField.length() - 1; i++) {
                if ((generalFieldType[i] == EncodeMode.ISOIEC)
                        && (generalField.charAt(i + 1) == '[')) {
                    generalFieldType[i + 1] = EncodeMode.ISOIEC;
                }
            }

            for (i = 0; i < generalField.length() - 1; i++) {
                if ((generalFieldType[i] == EncodeMode.ALPHA_OR_ISO)
                        && (generalField.charAt(i + 1) == '[')) {
                    generalFieldType[i + 1] = EncodeMode.ALPHA_OR_ISO;
                }
            }

            latch = applyGeneralFieldRules();

            /* Set initial mode if not NUMERIC */
            if (generalFieldType[0] == EncodeMode.ALPHA) {
                binaryString.append("0000"); /* Alphanumeric latch */
                lastMode = EncodeMode.ALPHA;
            }
            if (generalFieldType[0] == EncodeMode.ISOIEC) {
                binaryString.append("0000"); /* Alphanumeric latch */
                binaryString.append("00100"); /* ISO/IEC 646 latch */
                lastMode = EncodeMode.ISOIEC;
            }

            i = 0;
            do {
                switch (generalFieldType[i]) {
                    case NUMERIC:

                        if (lastMode != EncodeMode.NUMERIC) {
                            binaryString.append("000"); /* Numeric latch */
                        }

                        if (generalField.charAt(i) != '[') {
                            d1 = generalField.charAt(i) - '0';
                        } else {
                            d1 = 10;
                        }

                        if (generalField.charAt(i + 1) != '[') {
                            d2 = generalField.charAt(i + 1) - '0';
                        } else {
                            d2 = 10;
                        }

                        value = (11 * d1) + d2 + 8;

                        for (j = 0; j < 7; j++) {
                            if ((value & (0x40 >> j)) != 0) {
                                binaryString.append("1");
                            } else {
                                binaryString.append("0");
                            }
                        }

                        i += 2;
                        lastMode = EncodeMode.NUMERIC;
                        break;

                    case ALPHA:
                        if (i != 0) {
                            if (lastMode == EncodeMode.NUMERIC) {
                                binaryString.append("0000"); /* Alphanumeric latch */
                            }
                            if (lastMode == EncodeMode.ISOIEC) {
                                binaryString.append("00100"); /* Alphanumeric latch */
                            }
                        }

                        if ((generalField.charAt(i) >= '0') && (generalField.charAt(i) <= '9')) {

                            value = generalField.charAt(i) - 43;

                            for (j = 0; j < 5; j++) {
                                if ((value & (0x10 >> j)) != 0) {
                                    binaryString.append("1");
                                } else {
                                    binaryString.append("0");
                                }
                            }
                        }

                        if ((generalField.charAt(i) >= 'A') && (generalField.charAt(i) <= 'Z')) {

                            value = generalField.charAt(i) - 33;

                            for (j = 0; j < 6; j++) {
                                if ((value & (0x20 >> j)) != 0) {
                                    binaryString.append("1");
                                } else {
                                    binaryString.append("0");
                                }
                            }
                        }

                        lastMode = EncodeMode.ALPHA;
                        if (generalField.charAt(i) == '[') {
                            binaryString.append("01111");
                            lastMode = EncodeMode.NUMERIC;
                        } /* FNC1/Numeric latch */
                        if (generalField.charAt(i) == '*') {
                            binaryString.append("111010"); /* asterisk */
                        }
                        if (generalField.charAt(i) == ',') {
                            binaryString.append("111011"); /* comma */
                        }
                        if (generalField.charAt(i) == '-') {
                            binaryString.append("111100"); /* minus or hyphen */
                        }
                        if (generalField.charAt(i) == '.') {
                            binaryString.append("111101"); /* period or full stop */
                        }
                        if (generalField.charAt(i) == '/') {
                            binaryString.append("111110"); /* slash or solidus */
                        }

                        i++;
                        break;

                    case ISOIEC:
                        if (i != 0) {
                            if (lastMode == EncodeMode.NUMERIC) {
                                binaryString.append("0000"); /* Alphanumeric latch */
                                binaryString.append("00100"); /* ISO/IEC 646 latch */
                            }
                            if (lastMode == EncodeMode.ALPHA) {
                                binaryString.append("00100"); /* ISO/IEC 646 latch */
                            }
                        }

                        if ((generalField.charAt(i) >= '0')
                                && (generalField.charAt(i) <= '9')) {

                            value = generalField.charAt(i) - 43;

                            for (j = 0; j < 5; j++) {
                                if ((value & (0x10 >> j)) != 0) {
                                    binaryString.append("1");
                                } else {
                                    binaryString.append("0");
                                }
                            }
                        }

                        if ((generalField.charAt(i) >= 'A')
                                && (generalField.charAt(i) <= 'Z')) {

                            value = generalField.charAt(i) - 1;

                            for (j = 0; j < 7; j++) {
                                if ((value & (0x40 >> j)) != 0) {
                                    binaryString.append("1");
                                } else {
                                    binaryString.append("0");
                                }
                            }
                        }

                        if ((generalField.charAt(i) >= 'a')
                                && (generalField.charAt(i) <= 'z')) {

                            value = generalField.charAt(i) - 7;

                            for (j = 0; j < 7; j++) {
                                if ((value & (0x40 >> j)) != 0) {
                                    binaryString.append("1");
                                } else {
                                    binaryString.append("0");
                                }
                            }
                        }

                        lastMode = EncodeMode.ISOIEC;
                        if (generalField.charAt(i) == '[') {
                            binaryString.append("01111");
                            lastMode = EncodeMode.NUMERIC;
                        } /* FNC1/Numeric latch */
                        if (generalField.charAt(i) == '!') {
                            binaryString.append("11101000"); /* exclamation mark */
                        }
                        if (generalField.charAt(i) == 34) {
                            binaryString.append("11101001"); /* quotation mark */
                        }
                        if (generalField.charAt(i) == 37) {
                            binaryString.append("11101010"); /* percent sign */
                        }
                        if (generalField.charAt(i) == '&') {
                            binaryString.append("11101011"); /* ampersand */
                        }
                        if (generalField.charAt(i) == 39) {
                            binaryString.append("11101100"); /* apostrophe */
                        }
                        if (generalField.charAt(i) == '(') {
                            binaryString.append("11101101"); /* left parenthesis */
                        }
                        if (generalField.charAt(i) == ')') {
                            binaryString.append("11101110"); /* right parenthesis */
                        }
                        if (generalField.charAt(i) == '*') {
                            binaryString.append("11101111"); /* asterisk */
                        }
                        if (generalField.charAt(i) == '+') {
                            binaryString.append("11110000"); /* plus sign */
                        }
                        if (generalField.charAt(i) == ',') {
                            binaryString.append("11110001"); /* comma */
                        }
                        if (generalField.charAt(i) == '-') {
                            binaryString.append("11110010"); /* minus or hyphen */
                        }
                        if (generalField.charAt(i) == '.') {
                            binaryString.append("11110011"); /* period or full stop */
                        }
                        if (generalField.charAt(i) == '/') {
                            binaryString.append("11110100"); /* slash or solidus */
                        }
                        if (generalField.charAt(i) == ':') {
                            binaryString.append("11110101"); /* colon */
                        }
                        if (generalField.charAt(i) == ';') {
                            binaryString.append("11110110"); /* semicolon */
                        }
                        if (generalField.charAt(i) == '<') {
                            binaryString.append("11110111"); /* less-than sign */
                        }
                        if (generalField.charAt(i) == '=') {
                            binaryString.append("11111000"); /* equals sign */
                        }
                        if (generalField.charAt(i) == '>') {
                            binaryString.append("11111001"); /* greater-than sign */
                        }
                        if (generalField.charAt(i) == '?') {
                            binaryString.append("11111010"); /* question mark */
                        }
                        if (generalField.charAt(i) == '_') {
                            binaryString.append("11111011"); /* underline or low line */
                        }
                        if (generalField.charAt(i) == ' ') {
                            binaryString.append("11111100"); /* space */
                        }

                        i++;
                        break;
                }
                currentLength = i;
                if (latch) {
                    currentLength++;
                }
            } while (currentLength < generalField.length());

            remainder = calculateRemainder(binaryString.length());

            if (latch) {
                /* There is still one more numeric digit to encode */

                if (lastMode == EncodeMode.NUMERIC) {
                    if ((remainder >= 4) && (remainder <= 6)) {
                        value = generalField.charAt(i) - '0';
                        value++;

                        for (j = 0; j < 4; j++) {
                            if ((value & (0x08 >> j)) != 0) {
                                binaryString.append("1");
                            } else {
                                binaryString.append("0");
                            }
                        }
                    } else {
                        d1 = generalField.charAt(i) - '0';
                        d2 = 10;

                        value = (11 * d1) + d2 + 8;

                        for (j = 0; j < 7; j++) {
                            if ((value & (0x40 >> j)) != 0) {
                                binaryString.append("1");
                            } else {
                                binaryString.append("0");
                            }
                        }
                    }
                } else {
                    value = generalField.charAt(i) - 43;

                    for (j = 0; j < 5; j++) {
                        if ((value & (0x10 >> j)) != 0) {
                            binaryString.append("1");
                        } else {
                            binaryString.append("0");
                        }
                    }
                }
            }
        }

        if (binaryString.length() > 252) {
            errorMsg.append("Input too long");
            return false;
        }

        remainder = calculateRemainder(binaryString.length());

        /* Now add padding to binary string (7.2.5.5.4) */
        i = remainder;
        if ((generalField.length() != 0) && (lastMode == EncodeMode.NUMERIC)) {
            padstring = new StringBuilder("0000");
            i -= 4;
        } else {
            padstring = new StringBuilder();
        }
        for (; i > 0; i -= 5) {
            padstring.append("00100");
        }

        binaryString.append(padstring.substring(0, remainder));

        /* Patch variable length symbol bit field */
        patch = "";
        if ((((binaryString.length() / 12) + 1) & 1) == 0) {
            patch += "0";
        } else {
            patch += "1";
        }
        if (binaryString.length() <= 156) {
            patch += "0";
        } else {
            patch += "1";
        }

        if (encodingMethod == 1) {
            binaryString = new StringBuilder(binaryString.substring(0, 2))
                    .append(patch)
                    .append(binaryString.substring(4));
        }
        if (encodingMethod == 2) {
            binaryString = new StringBuilder(binaryString.substring(0, 3))
                    .append(patch)
                    .append(binaryString.substring(5));
        }
        if ((encodingMethod == 5) || (encodingMethod == 6)) {
            binaryString = new StringBuilder(binaryString.substring(0, 6))
                    .append(patch)
                    .append(binaryString.substring(8));
        }

        encodeInfo.append("Binary length: ").append(Integer.toString(binaryString.length())).append("\n");
        displayBinaryString();
        return true;
    }

    private void displayBinaryString() {
        int i, nibble;
        /* Display binary string as hexadecimal */

        encodeInfo.append("Binary String: ");
        nibble = 0;
        for (i = 0; i < binaryString.length(); i++) {
            switch (i % 4) {
                case 0:
                    if (binaryString.charAt(i) == '1') {
                        nibble += 8;
                    }
                    break;
                case 1:
                    if (binaryString.charAt(i) == '1') {
                        nibble += 4;
                    }
                    break;
                case 2:
                    if (binaryString.charAt(i) == '1') {
                        nibble += 2;
                    }
                    break;
                case 3:
                    if (binaryString.charAt(i) == '1') {
                        nibble += 1;
                    }
                    encodeInfo.append(Integer.toHexString(nibble));
                    nibble = 0;
                    break;
            }
        }

        if ((binaryString.length() % 4) != 0) {
            encodeInfo.append(Integer.toHexString(nibble));
        }
        encodeInfo.append("\n");
    }

    private boolean applyGeneralFieldRules() {
        /* Attempts to apply encoding rules from secions 7.2.5.5.1 to 7.2.5.5.3
	of ISO/IEC 24724:2006 */

        int blockCount, i, j, k;
        EncodeMode current, next, last;
        int[] blockLength = new int[200];
        EncodeMode[] blockType = new EncodeMode[200];

        blockCount = 0;

        blockLength[blockCount] = 1;
        blockType[blockCount] = generalFieldType[0];

        for (i = 1; i < generalField.length(); i++) {
            current = generalFieldType[i];
            last = generalFieldType[i - 1];

            if (current == last) {
                blockLength[blockCount] = blockLength[blockCount] + 1;
            } else {
                blockCount++;
                blockLength[blockCount] = 1;
                blockType[blockCount] = generalFieldType[i];
            }
        }

        blockCount++;

        for (i = 0; i < blockCount; i++) {
            current = blockType[i];
            next = blockType[i + 1];

            if ((current == EncodeMode.ISOIEC) && (i != (blockCount - 1))) {
                if ((next == EncodeMode.ANY_ENC) && (blockLength[i + 1] >= 4)) {
                    blockType[i + 1] = EncodeMode.NUMERIC;
                }
                if ((next == EncodeMode.ANY_ENC) && (blockLength[i + 1] < 4)) {
                    blockType[i + 1] = EncodeMode.ISOIEC;
                }
                if ((next == EncodeMode.ALPHA_OR_ISO) && (blockLength[i + 1] >= 5)) {
                    blockType[i + 1] = EncodeMode.ALPHA;
                }
                if ((next == EncodeMode.ALPHA_OR_ISO) && (blockLength[i + 1] < 5)) {
                    blockType[i + 1] = EncodeMode.ISOIEC;
                }
            }

            if (current == EncodeMode.ALPHA_OR_ISO) {
                blockType[i] = EncodeMode.ALPHA;
                current = EncodeMode.ALPHA;
            }

            if ((current == EncodeMode.ALPHA) && (i != (blockCount - 1))) {
                if ((next == EncodeMode.ANY_ENC) && (blockLength[i + 1] >= 6)) {
                    blockType[i + 1] = EncodeMode.NUMERIC;
                }
                if ((next == EncodeMode.ANY_ENC) && (blockLength[i + 1] < 6)) {
                    if ((i == blockCount - 2) && (blockLength[i + 1] >= 4)) {
                        blockType[i + 1] = EncodeMode.NUMERIC;
                    } else {
                        blockType[i + 1] = EncodeMode.ALPHA;
                    }
                }
            }

            if (current == EncodeMode.ANY_ENC) {
                blockType[i] = EncodeMode.NUMERIC;
            }
        }

        if (blockCount > 1) {
            i = 1;
            while (i < blockCount) {
                if (blockType[i - 1] == blockType[i]) {
                    /* bring together */
                    blockLength[i - 1] = blockLength[i - 1] + blockLength[i];
                    j = i + 1;

                    /* decreace the list */
                    while (j < blockCount) {
                        blockLength[j - 1] = blockLength[j];
                        blockType[j - 1] = blockType[j];
                        j++;
                    }
                    blockCount--;
                    i--;
                }
                i++;
            }
        }

        for (i = 0; i < blockCount - 1; i++) {
            if ((blockType[i] == EncodeMode.NUMERIC) && ((blockLength[i] & 1) != 0)) {
                /* Odd size numeric block */
                blockLength[i] = blockLength[i] - 1;
                blockLength[i + 1] = blockLength[i + 1] + 1;
            }
        }

        j = 0;
        for (i = 0; i < blockCount; i++) {
            for (k = 0; k < blockLength[i]; k++) {
                generalFieldType[j] = blockType[i];
                j++;
            }
        }

        /* If the last block is numeric and an odd size, further
    processing needs to be done outside this procedure */
        return (blockType[blockCount - 1] == EncodeMode.NUMERIC)
                && ((blockLength[blockCount - 1] & 1) != 0);
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
                    subVal -= getCombinations(n - elmWidth - (elements - bar),
                            elements - bar - 2);
                }
                /* less combinations with elements > maxVal */
                if (elements - bar - 1 > 1) {
                    lessVal = 0;
                    for (mxwElement = n - elmWidth - (elements - bar - 2);
                         mxwElement > maxWidth;
                         mxwElement--) {
                        lessVal += getCombinations(n - elmWidth - mxwElement - 1,
                                elements - bar - 3);
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

    private enum dbeMode {
        UNSTACKED, STACKED
    }

    private enum EncodeMode {
        NUMERIC, ALPHA, ISOIEC, INVALID_CHAR, ANY_ENC, ALPHA_OR_ISO
    }
}
