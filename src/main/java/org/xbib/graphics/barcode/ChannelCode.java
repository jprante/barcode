package org.xbib.graphics.barcode;

/**
 * Implements Channel Code according to ANSI/AIM BC12-1998.
 * Channel code encodes whole integer values between 0 and 7,742,862.
 */
public class ChannelCode extends Symbol {
    private int[] space = new int[11];
    private int[] bar = new int[11];
    private double currentValue;
    private double targetValue;
    private String horizontalSpacing;
    private int requestedNumberOfChannels = 0;

    /**
     * Set the number of channels used to encode data. This setting will be
     * ignored if the value to be encoded requires more channels.
     *
     * @param channels Number of channels in range 3 - 8
     */
    public void setNumberOfChannels(int channels) {
        if ((channels >= 3) && (channels <= 7)) {
            requestedNumberOfChannels = channels;
        }
    }

    @Override
    @SuppressWarnings("fallthrough")
    public boolean encode() {
        int numberOfChannels;
        int i;
        int leadingZeroCount;

        targetValue = 0;
        horizontalSpacing = "";

        if (content.length() > 7) {
            errorMsg.append("Input too long");
            return false;
        }

        if (!(content.matches("[0-9]+"))) {
            errorMsg.append("Invalid characters in input");
            return false;
        }

        if ((requestedNumberOfChannels <= 2) || (requestedNumberOfChannels > 8)) {
            numberOfChannels = 3;
        } else {
            numberOfChannels = requestedNumberOfChannels;
        }

        for (i = 0; i < content.length(); i++) {
            targetValue *= 10;
            targetValue += Character.getNumericValue(content.charAt(i));
        }

        switch (numberOfChannels) {
            case 3:
                if (targetValue > 26) {
                    numberOfChannels++;
                }
            case 4:
                if (targetValue > 292) {
                    numberOfChannels++;
                }
            case 5:
                if (targetValue > 3493) {
                    numberOfChannels++;
                }
            case 6:
                if (targetValue > 44072) {
                    numberOfChannels++;
                }
            case 7:
                if (targetValue > 576688) {
                    numberOfChannels++;
                }
            case 8:
                if (targetValue > 7742862) {
                    numberOfChannels++;
                }
        }

        if (numberOfChannels == 9) {
            errorMsg.append("Value out of range");
            return false;
        }

        encodeInfo.append("Channels Used: ").append(numberOfChannels).append('\n');

        for (i = 0; i < 11; i++) {
            bar[i] = 0;
            space[i] = 0;
        }

        bar[0] = space[1] = bar[1] = space[2] = bar[2] = 1;
        currentValue = 0;
        nextSpace(numberOfChannels, 3, numberOfChannels, numberOfChannels);

        leadingZeroCount = numberOfChannels - 1 - content.length();

        readable = new StringBuilder();
        for (i = 0; i < leadingZeroCount; i++) {
            readable.append("0");
        }
        readable.append(content);

        pattern = new String[1];
        pattern[0] = horizontalSpacing;
        rowCount = 1;
        rowHeight = new int[1];
        rowHeight[0] = -1;
        plotSymbol();
        return true;
    }

    private void nextSpace(int channels, int i, int maxSpace, int maxBar) {
        int s;

        for (s = (i < channels + 2) ? 1 : maxSpace; s <= maxSpace; s++) {
            space[i] = s;
            nextBar(channels, i, maxBar, maxSpace + 1 - s);
        }
    }

    private void nextBar(int channels, int i, int maxBar, int maxSpace) {
        int b;

        b = (space[i] + bar[i - 1] + space[i - 1] + bar[i - 2] > 4) ? 1 : 2;
        if (i < channels + 2) {
            for (; b <= maxBar; b++) {
                bar[i] = b;
                nextSpace(channels, i + 1, maxSpace, maxBar + 1 - b);
            }
        } else if (b <= maxBar) {
            bar[i] = maxBar;
            checkIfDone();
            currentValue++;
        }
    }

    private void checkIfDone() {
        int i;

        if (currentValue == targetValue) {
            /* Target reached - save the generated pattern */
            horizontalSpacing = "11110";
            for (i = 0; i < 11; i++) {
                horizontalSpacing += (char) (space[i] + '0');
                horizontalSpacing += (char) (bar[i] + '0');
            }
        }
    }
}
