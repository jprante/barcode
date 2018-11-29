package org.xbib.graphics.barcode;

import org.xbib.graphics.barcode.util.ReedSolomon;

/**
 * Implements Aztec Runes bar code symbology.
 * According to ISO/IEC 24778:2008 Annex A
 * Aztec Runes is a fixed-size matrix symbology which can encode whole
 * integer values between 0 and 255.
 */
public class AztecRune extends Symbol {

    private int[] bitPlacementMap = {
            1, 1, 2, 3, 4, 5, 6, 7, 8, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            29, 1, 0, 0, 0, 0, 0, 0, 0, 1, 9,
            28, 1, 0, 1, 1, 1, 1, 1, 0, 1, 10,
            27, 1, 0, 1, 0, 0, 0, 1, 0, 1, 11,
            26, 1, 0, 1, 0, 1, 0, 1, 0, 1, 12,
            25, 1, 0, 1, 0, 0, 0, 1, 0, 1, 13,
            24, 1, 0, 1, 1, 1, 1, 1, 0, 1, 14,
            23, 1, 0, 0, 0, 0, 0, 0, 0, 1, 15,
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            0, 0, 22, 21, 20, 19, 18, 17, 16, 0, 0
    };

    @Override
    public boolean encode() {
        int decimalValue = 0;
        int i;
        int row;
        int column;
        StringBuilder binaryDataStream;
        StringBuilder reversedBinaryDataStream;
        int[] dataCodeword = new int[3];
        int[] errorCorrectionCodeword = new int[6];
        ReedSolomon rs = new ReedSolomon();
        StringBuilder rowBinary;

        if (content.length() > 3) {
            errorMsg.append("Input too large");
            return false;
        }

        if (!(content.matches("[0-9]+"))) {
            errorMsg.append("Invalid input data");
            return false;
        }

        switch (content.length()) {
            case 3:
                decimalValue = 100 * (content.charAt(0) - '0');
                decimalValue += 10 * (content.charAt(1) - '0');
                decimalValue += (content.charAt(2) - '0');
                break;
            case 2:
                decimalValue = 10 * (content.charAt(0) - '0');
                decimalValue += (content.charAt(1) - '0');
                break;
            case 1:
                decimalValue = (content.charAt(0) - '0');
                break;
        }

        if (decimalValue > 255) {
            errorMsg.append("Input too large");
            return false;
        }

        binaryDataStream = new StringBuilder();
        for (i = 0x80; i > 0; i = i >> 1) {
            if ((decimalValue & i) != 0) {
                binaryDataStream.append("1");
            } else {
                binaryDataStream.append("0");
            }
        }

        dataCodeword[0] = 0;
        dataCodeword[1] = 0;

        for (i = 0; i < 2; i++) {
            if (binaryDataStream.charAt(i * 4) == '1') {
                dataCodeword[i] += 8;
            }
            if (binaryDataStream.charAt((i * 4) + 1) == '1') {
                dataCodeword[i] += 4;
            }
            if (binaryDataStream.charAt((i * 4) + 2) == '1') {
                dataCodeword[i] += 2;
            }
            if (binaryDataStream.charAt((i * 4) + 3) == '1') {
                dataCodeword[i] += 1;
            }
        }

        rs.init_gf(0x13);
        rs.init_code(5, 1);
        rs.encode(2, dataCodeword);

        for (i = 0; i < 5; i++) {
            errorCorrectionCodeword[i] = rs.getResult(i);
        }

        for (i = 0; i < 5; i++) {
            if ((errorCorrectionCodeword[4 - i] & 0x08) != 0) {
                binaryDataStream.append('1');
            } else {
                binaryDataStream.append('0');
            }
            if ((errorCorrectionCodeword[4 - i] & 0x04) != 0) {
                binaryDataStream.append('1');
            } else {
                binaryDataStream.append('0');
            }
            if ((errorCorrectionCodeword[4 - i] & 0x02) != 0) {
                binaryDataStream.append('1');
            } else {
                binaryDataStream.append('0');
            }
            if ((errorCorrectionCodeword[4 - i] & 0x01) != 0) {
                binaryDataStream.append('1');
            } else {
                binaryDataStream.append('0');
            }
        }

        reversedBinaryDataStream = new StringBuilder();
        for (i = 0; i < binaryDataStream.length(); i++) {
            if ((i & 1) == 0) {
                if (binaryDataStream.charAt(i) == '0') {
                    reversedBinaryDataStream.append("1");
                } else {
                    reversedBinaryDataStream.append("0");
                }
            } else {
                reversedBinaryDataStream.append(binaryDataStream.charAt(i));
            }
        }

        encodeInfo.append("Binary: ").append(reversedBinaryDataStream).append("\n");

        rowBinary = new StringBuilder();
        readable = new StringBuilder();
        pattern = new String[11];
        rowCount = 11;
        rowHeight = new int[11];
        for (row = 0; row < 11; row++) {
            for (column = 0; column < 11; column++) {
                if (bitPlacementMap[(row * 11) + column] == 1) {
                    rowBinary.append("1");
                }
                if (bitPlacementMap[(row * 11) + column] == 0) {
                    rowBinary.append("0");
                }
                if (bitPlacementMap[(row * 11) + column] >= 2) {
                    rowBinary.append(reversedBinaryDataStream.charAt(bitPlacementMap[(row * 11) + column] - 2));
                }
            }
            pattern[row] = bin2pat(rowBinary.toString());
            rowHeight[row] = 1;
            rowBinary = new StringBuilder();
        }

        plotSymbol();
        return true;
    }
}
