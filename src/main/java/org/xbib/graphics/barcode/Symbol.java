package org.xbib.graphics.barcode;

import org.xbib.graphics.barcode.util.Hexagon;
import org.xbib.graphics.barcode.util.TextBox;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.xbib.graphics.barcode.HumanReadableLocation.BOTTOM;
import static org.xbib.graphics.barcode.HumanReadableLocation.NONE;
import static org.xbib.graphics.barcode.HumanReadableLocation.TOP;

/**
 * Generic barcode symbology class.
 * TODO: Setting attributes like module width, font size, etc should probably throw
 * an exception if set *after* encoding has already been completed.
 */
public abstract class Symbol {

    public List<Rectangle2D.Double> rectangles = new ArrayList<>();

    public List<TextBox> texts = new ArrayList<>();

    public List<Hexagon> hexagons = new ArrayList<>();

    public List<Ellipse2D.Double> target = new ArrayList<>();

    protected String content;

    protected StringBuilder readable;

    protected String[] pattern;

    protected int rowCount = 0;

    protected int[] rowHeight;

    protected StringBuilder errorMsg = new StringBuilder();

    protected int symbolHeight = 0;

    protected int symbolWidth = 0;

    protected int defaultHeight = 40;

    private HumanReadableLocation humanReadableLocation = BOTTOM;

    protected StringBuilder encodeInfo = new StringBuilder();

    protected byte[] inputBytes;

    protected DataType inputDataType = DataType.ECI;

    int moduleWidth = 1;

    double fontSize = 8;

    boolean readerInit;

    int eciMode = 3;

    private int quietZoneHorizontal = 0;

    private int quietZoneVertical = 0;

    private String fontName = "Helvetica";

    /**
     *
     */
    public Symbol() {
        unsetReaderInit();
    }

    /**
     * Inserts the specified array into the specified original array at the specified index.
     *
     * @param original the original array into which we want to insert another array
     * @param index    the index at which we want to insert the array
     * @param inserted the array that we want to insert
     * @return the combined array
     */
    static int[] insert(int[] original, int index, int[] inserted) {
        int[] modified = new int[original.length + inserted.length];
        System.arraycopy(original, 0, modified, 0, index);
        System.arraycopy(inserted, 0, modified, index, inserted.length);
        System.arraycopy(original, index, modified, index + inserted.length, modified.length - index - inserted.length);
        return modified;
    }

    /**
     * Returns true if the specified array contains the specified value.
     *
     * @param values the array to check in
     * @param value  the value to check for
     * @return true if the specified array contains the specified value
     */
    static boolean contains(int[] values, int value) {
        for (int value1 : values) {
            if (value1 == value) {
                return true;
            }
        }
        return false;
    }

    private static boolean roughlyEqual(double d1, double d2) {
        return Math.abs(d1 - d2) < 0.0001;
    }

    /**
     * Sets the type of input data. This setting influences what
     * pre-processing is done on data before encoding in the symbol.
     * For example: for <code>GS1</code> mode the AI data will be used to
     * calculate the position of 'FNC1' characters.
     * Valid values are:
     * <ul>
     * <li><code>UTF8</code> (default) Unicode encoding
     * <li><code>LATIN1</code> ISO 8859-1 (Latin-1) encoding
     * <li><code>BINARY</code> Byte encoding mode
     * <li><code>GS1</code> Application Identifier and data pairs in "[AI]DATA" format
     * <li><code>HIBC</code> Health Industry Bar Code number (without check digit)
     * <li><code>ECI</code> Extended Channel Interpretations
     * </ul>
     *
     * @param dataType A <code>DataType</code> value which specifies the type of data
     */
    public void setDataType(DataType dataType) {
        inputDataType = dataType;
    }

    /**
     * Prefixes symbol data with a "Reader Initialisation" or "Reader
     * Programming" instruction.
     */
    public final void setReaderInit() {
        readerInit = true;
    }

    /**
     * Removes "Reader Initialisation" or "Reader Programming" instruction
     * from symbol data.
     */
    private void unsetReaderInit() {
        readerInit = false;
    }

    /**
     * Returns the default bar height for this symbol.
     *
     * @return the default bar height for this symbol
     */
    public int getBarHeight() {
        return defaultHeight;
    }

    /**
     * Sets the default bar height for this symbol (default value is <code>40</code>).
     *
     * @param barHeight the default bar height for this symbol
     */
    public void setBarHeight(int barHeight) {
        this.defaultHeight = barHeight;
    }

    /**
     * Returns the module width for this symbol.
     *
     * @return the module width for this symbol
     */
    public int getModuleWidth() {
        return moduleWidth;
    }

    /**
     * Sets the module width for this symbol (default value is <code>1</code>).
     *
     * @param moduleWidth the module width for this symbol
     */
    public void setModuleWidth(int moduleWidth) {
        this.moduleWidth = moduleWidth;
    }

    /**
     * Returns the horizontal quiet zone (white space) added to the left and to the right of this symbol.
     *
     * @return the horizontal quiet zone (white space) added to the left and to the right of this symbol
     */
    public int getQuietZoneHorizontal() {
        return quietZoneHorizontal;
    }

    /**
     * Sets the horizontal quiet zone (white space) added to the left and to the right of this symbol.
     *
     * @param quietZoneHorizontal the horizontal quiet zone (white space) added to the left and to the right of this symbol
     */
    public void setQuietZoneHorizontal(int quietZoneHorizontal) {
        this.quietZoneHorizontal = quietZoneHorizontal;
    }

    /**
     * Returns the vertical quiet zone (white space) added above and below this symbol.
     *
     * @return the vertical quiet zone (white space) added above and below this symbol
     */
    public int getQuietZoneVertical() {
        return quietZoneVertical;
    }

    /**
     * Sets the vertical quiet zone (white space) added above and below this symbol.
     *
     * @param quietZoneVertical the vertical quiet zone (white space) added above and below this symbol
     */
    public void setQuietZoneVertical(int quietZoneVertical) {
        this.quietZoneVertical = quietZoneVertical;
    }

    /**
     * Returns the name of the font to use to render the human-readable text.
     *
     * @return the name of the font to use to render the human-readable text
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * Sets the name of the font to use to render the human-readable text (default value is <code>Helvetica</code>).
     *
     * @param fontName the name of the font to use to render the human-readable text
     */
    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    /**
     * Returns the size of the font to use to render the human-readable text.
     *
     * @return the size of the font to use to render the human-readable text
     */
    public double getFontSize() {
        return fontSize;
    }

    /**
     * Sets the size of the font to use to render the human-readable text (default value is <code>8</code>).
     *
     * @param fontSize the size of the font to use to render the human-readable text
     */
    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * Gets the width of the encoded symbol, including the horizontal quiet zone.
     *
     * @return the width of the encoded symbol
     */
    public int getWidth() {
        return symbolWidth + (2 * quietZoneHorizontal);
    }

    /**
     * Returns the height of the symbol, including the human-readable text, if any, as well as the vertical
     * quiet zone. This height is an approximation, since it is calculated without access to a font engine.
     *
     * @return the height of the symbol, including the human-readable text, if any, as well as the vertical
     * quiet zone
     */
    public int getHeight() {
        return symbolHeight + getHumanReadableHeight() + (2 * quietZoneVertical);
    }

    /**
     * Returns the height of the human-readable text, including the space between the text and other symbols.
     * This height is an approximation, since it is calculated without access to a font engine.
     *
     * @return the height of the human-readable text
     */
    public int getHumanReadableHeight() {
        if (texts.isEmpty()) {
            return 0;
        } else {
            return getTheoreticalHumanReadableHeight();
        }
    }

    /**
     * Returns the height of the human-readable text, assuming this symbol had human-readable text.
     *
     * @return the height of the human-readable text, assuming this symbol had human-readable text
     */
    int getTheoreticalHumanReadableHeight() {
        return (int) Math.ceil(fontSize * 1.2); // 0.2 space between bars and text
    }

    /**
     * Returns a human readable summary of the decisions made by the encoder when creating a symbol.
     *
     * @return a human readable summary of the decisions made by the encoder when creating a symbol
     */
    public String getEncodeInfo() {
        return encodeInfo.toString();
    }

    /**
     * Returns the location of the human-readable text.
     *
     * @return the location of the human-readable text
     */
    public HumanReadableLocation getHumanReadableLocation() {
        return humanReadableLocation;
    }

    /**
     * Sets the location of the human-readable text (default value is {@link HumanReadableLocation#BOTTOM}).
     *
     * @param humanReadableLocation the location of the human-readable text
     */
    public void setHumanReadableLocation(HumanReadableLocation humanReadableLocation) {
        this.humanReadableLocation = humanReadableLocation;
    }

    protected int positionOf(char thischar, char[] LookUp) {
        int i, outval = 0;

        for (i = 0; i < LookUp.length; i++) {
            if (thischar == LookUp[i]) {
                outval = i;
            }
        }
        return outval;
    }

    protected String bin2pat(String bin) {
        boolean black;
        int i, l;
        StringBuilder pat = new StringBuilder();

        black = true;
        l = 0;
        for (i = 0; i < bin.length(); i++) {
            if (black) {
                if (bin.charAt(i) == '1') {
                    l++;
                } else {
                    black = false;
                    pat.append((char) (l + '0'));
                    l = 1;
                }
            } else {
                if (bin.charAt(i) == '0') {
                    l++;
                } else {
                    black = true;
                    pat.append((char) (l + '0'));
                    l = 1;
                }
            }
        }
        pat.append((char) (l + '0'));

        return pat.toString();
    }

    public String getContent() {
        return content;
    }

    /**
     * Set the data to be encoded. Input data will be assumed to be of
     * the type set by <code>setDataType</code>.
     *
     * @param inputData A <code>String</code> containing the data to encode
     */
    public void setContent(String inputData) {
        int i;
        content = inputData;
        if (inputDataType == DataType.GS1) {
            content = gs1SanityCheck(inputData);
        }
        if (inputDataType == DataType.GS1) {
            readable = new StringBuilder();
            for (i = 0; i < inputData.length(); i++) {
                switch (inputData.charAt(i)) {
                    case '[':
                        readable.append('(');
                        break;
                    case ']':
                        readable.append(')');
                        break;
                    default:
                        readable.append(inputData.charAt(i));
                        break;
                }
            }
        }
        if (inputDataType == DataType.HIBC) {
            content = hibcProcess(inputData);
        }
        if (!content.isEmpty()) {
            if (!encode()) {
                throw new IllegalStateException(errorMsg.toString());
            }
        } else {
            throw new IllegalStateException("No input data");
        }
    }

    void eciProcess() {
        int qmarksBefore, qmarksAfter;
        int i;

        qmarksBefore = 0;
        for (i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '?') {
                qmarksBefore++;
            }
        }

        qmarksAfter = eciEncode("ISO8859_1");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 3;
            encodeInfo.append("Encoding in ISO 8859-1 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_2");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 4;
            encodeInfo.append("Encoding in ISO 8859-2 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_3");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 5;
            encodeInfo.append("Encoding in ISO 8859-3 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_4");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 6;
            encodeInfo.append("Encoding in ISO 8859-4 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_5");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 7;
            encodeInfo.append("Encoding in ISO 8859-5 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_6");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 8;
            encodeInfo.append("Encoding in ISO 8859-6 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_7");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 9;
            encodeInfo.append("Encoding in ISO 8859-7 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_8");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 10;
            encodeInfo.append("Encoding in ISO 8859-8 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_9");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 11;
            encodeInfo.append("Encoding in ISO 8859-9 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_10");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 12;
            encodeInfo.append("Encoding in ISO 8859-10 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_11");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 13;
            encodeInfo.append("Encoding in ISO 8859-11 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_13");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 15;
            encodeInfo.append("Encoding in ISO 8859-13 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_14");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 16;
            encodeInfo.append("Encoding in ISO 8859-14 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_15");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 17;
            encodeInfo.append("Encoding in ISO 8859-15 character set\n");
            return;
        }

        qmarksAfter = eciEncode("ISO8859_16");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 18;
            encodeInfo.append("Encoding in ISO 8859-16 character set\n");
            return;
        }

        qmarksAfter = eciEncode("Windows_1250");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 21;
            encodeInfo.append("Encoding in Windows-1250 character set\n");
            return;
        }

        qmarksAfter = eciEncode("Windows_1251");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 22;
            encodeInfo.append("Encoding in Windows-1251 character set\n");
            return;
        }

        qmarksAfter = eciEncode("Windows_1252");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 23;
            encodeInfo.append("Encoding in Windows-1252 character set\n");
            return;
        }

        qmarksAfter = eciEncode("Windows_1256");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 24;
            encodeInfo.append("Encoding in Windows-1256 character set\n");
            return;
        }

        qmarksAfter = eciEncode("SJIS");
        if (qmarksAfter == qmarksBefore) {
            eciMode = 20;
            encodeInfo.append("Encoding in Shift-JIS character set\n");
            return;
        }

        /* default */
        qmarksAfter = eciEncode("UTF8");
        eciMode = 26;
        encodeInfo.append("Encoding in UTF-8 character set\n");
    }

    private int eciEncode(String charset) {
        /* getBytes replaces unconverted characters to '?', so count
           the number of question marks to find if conversion was sucessful.
        */
        int i, qmarksAfter;

        try {
            inputBytes = content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            return -1;
        }

        qmarksAfter = 0;
        for (i = 0; i < inputBytes.length; i++) {
            if (inputBytes[i] == '?') {
                qmarksAfter++;
            }
        }

        return qmarksAfter;
    }

    abstract boolean encode();

    protected void plotSymbol() {
        int xBlock, yBlock;
        double x, y, w, h;
        boolean black;
        rectangles.clear();
        texts.clear();
        int baseY;
        if (humanReadableLocation == TOP) {
            baseY = getTheoreticalHumanReadableHeight();
        } else {
            baseY = 0;
        }
        h = 0;
        y = baseY;
        for (yBlock = 0; yBlock < rowCount; yBlock++) {
            black = true;
            x = 0;
            for (xBlock = 0; xBlock < pattern[yBlock].length(); xBlock++) {
                char c = pattern[yBlock].charAt(xBlock);
                w = getModuleWidth(c - '0') * moduleWidth;
                if (black) {
                    if (rowHeight[yBlock] == -1) {
                        h = defaultHeight;
                    } else {
                        h = rowHeight[yBlock];
                    }
                    if (w != 0 && h != 0) {
                        Rectangle2D.Double rect = new Rectangle2D.Double(x, y, w, h);
                        rectangles.add(rect);
                    }
                    if (x + w > symbolWidth) {
                        symbolWidth = (int) Math.ceil(x + w);
                    }
                }
                black = !black;
                x += w;
            }
            if ((y - baseY + h) > symbolHeight) {
                symbolHeight = (int) Math.ceil(y - baseY + h);
            }
            y += h;
        }
        mergeVerticalBlocks();
        if (humanReadableLocation != NONE && readable.length() > 0) {
            double baseline;
            if (humanReadableLocation == TOP) {
                baseline = fontSize;
            } else {
                baseline = getHeight() + fontSize;
            }
            double centerX = getWidth() / 2.0;
            texts.add(new TextBox(centerX, baseline, readable.toString()));
        }
    }

    /**
     * Returns the module width to use for the specified original module width, taking into account any module width ratio
     * customizations. Intended to be overridden by subclasses that support such module width ratio customization.
     *
     * @param originalWidth the original module width
     * @return the module width to use for the specified original module width
     */
    protected double getModuleWidth(int originalWidth) {
        return originalWidth;
    }

    /**
     * Search for rectangles which have the same width and x position, and
     * which join together vertically and merge them together to reduce the
     * number of rectangles needed to describe a symbol.
     */
    void mergeVerticalBlocks() {
        for (int i = 0; i < rectangles.size() - 1; i++) {
            for (int j = i + 1; j < rectangles.size(); j++) {
                Rectangle2D.Double firstRect = rectangles.get(i);
                Rectangle2D.Double secondRect = rectangles.get(j);
                if (roughlyEqual(firstRect.x, secondRect.x) && roughlyEqual(firstRect.width, secondRect.width)) {
                    if (roughlyEqual(firstRect.y + firstRect.height, secondRect.y)) {
                        firstRect.height += secondRect.height;
                        rectangles.set(i, firstRect);
                        rectangles.remove(j);
                    }
                }
            }
        }
    }

    private String gs1SanityCheck(String source) {
        // Enforce compliance with GS1 General Specification
        // http://www.gs1.org/docs/gsmp/barcodes/GS1_General_Specifications.pdf

        StringBuilder reduced = new StringBuilder();

        int i, j, lastAi;
        boolean aiLatch;
        int bracketLevel, maxBracketLevel, aiLength, maxAiLength, minAiLength;
        int[] aiValue = new int[100];
        int[] aiLocation = new int[100];
        int aiCount;
        int[] dataLocation = new int[100];
        int[] dataLength = new int[100];
        int srcLen = source.length();
        int errorLatch;

        /* Detect extended ASCII characters */
        for (i = 0; i < srcLen; i++) {
            if (source.charAt(i) >= 128) {
                errorMsg.append("Extended ASCII characters are not supported by GS1");
                return "";
            }
            if (source.charAt(i) < 32) {
                errorMsg.append("Control characters are not supported by GS1");
                return "";
            }
        }

        if (source.charAt(0) != '[') {
            errorMsg.append("Data does not start with an AI");
            return "";
        }

        /* Check the position of the brackets */
        bracketLevel = 0;
        maxBracketLevel = 0;
        aiLength = 0;
        maxAiLength = 0;
        minAiLength = 5;
        j = 0;
        aiLatch = false;
        for (i = 0; i < srcLen; i++) {
            aiLength += j;
            if (((j == 1) && (source.charAt(i) != ']'))
                    && ((source.charAt(i) < '0') || (source.charAt(i) > '9'))) {
                aiLatch = true;
            }
            if (source.charAt(i) == '[') {
                bracketLevel++;
                j = 1;
            }
            if (source.charAt(i) == ']') {
                bracketLevel--;
                if (aiLength < minAiLength) {
                    minAiLength = aiLength;
                }
                j = 0;
                aiLength = 0;
            }
            if (bracketLevel > maxBracketLevel) {
                maxBracketLevel = bracketLevel;
            }
            if (aiLength > maxAiLength) {
                maxAiLength = aiLength;
            }
        }
        minAiLength--;

        if (bracketLevel != 0) {
            /* Not all brackets are closed */
            errorMsg.append("Malformed AI in input data (brackets don't match)");
            return "";
        }

        if (maxBracketLevel > 1) {
            /* Nested brackets */
            errorMsg.append("Found nested brackets in input data");
            return "";
        }

        if (maxAiLength > 4) {
            /* AI is too long */
            errorMsg.append("Invalid AI in input data (AI too long)");
            return "";
        }

        if (minAiLength <= 1) {
            /* AI is too short */
            errorMsg.append("Invalid AI in input data (AI too short)");
            return "";
        }

        if (aiLatch) {
            /* Non-numeric data in AI */
            errorMsg.append("Invalid AI in input data (non-numeric characters in AI)");
            return "";
        }

        aiCount = 0;
        for (i = 1; i < srcLen; i++) {
            if (source.charAt(i - 1) == '[') {
                aiLocation[aiCount] = i;
                aiValue[aiCount] = 0;
                for (j = 0; source.charAt(i + j) != ']'; j++) {
                    aiValue[aiCount] *= 10;
                    aiValue[aiCount] += Character.getNumericValue(source.charAt(i + j));
                }
                aiCount++;
            }
        }

        for (i = 0; i < aiCount; i++) {
            dataLocation[i] = aiLocation[i] + 3;
            if (aiValue[i] >= 100) {
                dataLocation[i]++;
            }
            if (aiValue[i] >= 1000) {
                dataLocation[i]++;
            }
            dataLength[i] = source.length() - dataLocation[i];
            for (j = source.length() - 1; j >= dataLocation[i]; j--) {
                if (source.charAt(j) == '[') {
                    dataLength[i] = j - dataLocation[i];
                }
            }
        }

        for (i = 0; i < aiCount; i++) {
            if (dataLength[i] == 0) {
                /* No data for given AI */
                errorMsg.append("Empty data field in input data");
                return "";
            }
        }

        errorLatch = 0;
        for (i = 0; i < aiCount; i++) {
            switch (aiValue[i]) {
                case 0:
                    if (dataLength[i] != 18) {
                        errorLatch = 1;
                    }
                    break;
                case 1:
                case 2:
                case 3:
                    if (dataLength[i] != 14) {
                        errorLatch = 1;
                    }
                    break;
                case 4:
                    if (dataLength[i] != 16) {
                        errorLatch = 1;
                    }
                    break;
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                    if (dataLength[i] != 6) {
                        errorLatch = 1;
                    }
                    break;
                case 20:
                    if (dataLength[i] != 2) {
                        errorLatch = 1;
                    }
                    break;
                case 23:
                case 24:
                case 25:
                case 39:
                case 40:
                case 41:
                case 42:
                case 70:
                case 80:
                case 81:
                    errorLatch = 2;
                    break;
            }
            if (
                    ((aiValue[i] >= 100) && (aiValue[i] <= 179))
                            || ((aiValue[i] >= 1000) && (aiValue[i] <= 1799))
                            || ((aiValue[i] >= 200) && (aiValue[i] <= 229))
                            || ((aiValue[i] >= 2000) && (aiValue[i] <= 2299))
                            || ((aiValue[i] >= 300) && (aiValue[i] <= 309))
                            || ((aiValue[i] >= 3000) && (aiValue[i] <= 3099))
                            || ((aiValue[i] >= 31) && (aiValue[i] <= 36))
                            || ((aiValue[i] >= 310) && (aiValue[i] <= 369))) {
                errorLatch = 2;
            }
            if ((aiValue[i] >= 3100) && (aiValue[i] <= 3699) && dataLength[i] != 6) {
                errorLatch = 1;
            }
            if (
                    ((aiValue[i] >= 370) && (aiValue[i] <= 379))
                            || ((aiValue[i] >= 3700) && (aiValue[i] <= 3799))) {
                errorLatch = 2;
            }
            if ((aiValue[i] >= 410) && (aiValue[i] <= 415)) {
                if (dataLength[i] != 13) {
                    errorLatch = 1;
                }
            }
            if (
                    ((aiValue[i] >= 4100) && (aiValue[i] <= 4199))
                            || ((aiValue[i] >= 700) && (aiValue[i] <= 703))
                            || ((aiValue[i] >= 800) && (aiValue[i] <= 810))
                            || ((aiValue[i] >= 900) && (aiValue[i] <= 999))
                            || ((aiValue[i] >= 9000) && (aiValue[i] <= 9999))) {
                errorLatch = 2;
            }

            if (errorLatch == 1) {
                errorMsg = new StringBuilder("Invalid data length for AI");
                return "";
            }

            if (errorLatch == 2) {
                errorMsg = new StringBuilder("Invalid AI value");
                return "";
            }
        }

        /* Resolve AI data - put resulting string in 'reduced' */
        aiLatch = false;
        for (i = 0; i < srcLen; i++) {
            if ((source.charAt(i) != '[') && (source.charAt(i) != ']')) {
                reduced.append(source.charAt(i));
            }
            if (source.charAt(i) == '[') {
                /* Start of an AI string */
                if (aiLatch) {
                    reduced.append('[');
                }
                lastAi = (10 * Character.getNumericValue(source.charAt(i + 1)))
                        + Character.getNumericValue(source.charAt(i + 2));
                aiLatch = ((lastAi < 0) || (lastAi > 4))
                        && ((lastAi < 11) || (lastAi > 20))
                        && (lastAi != 23) /* legacy support - see 5.3.8.2.2 */
                        && ((lastAi < 31) || (lastAi > 36))
                        && (lastAi != 41);
            }
            /* The ']' character is simply dropped from the input */
        }

        /* the character '[' in the reduced string refers to the FNC1 character */
        return reduced.toString();
    }

    private String hibcProcess(String source) {
        char[] hibcCharTable = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z', '-', '.', ' ', '$',
                '/', '+', '%'};

        int counter, i;
        String toProcess;
        char checkDigit;

        if (source.length() > 36) {
            errorMsg = new StringBuilder("Data too long for HIBC LIC");
            return "";
        }
        source = source.toUpperCase();
        if (!(source.matches("[A-Z0-9-\\. \\$/+\\%]+?"))) {
            errorMsg = new StringBuilder("Invalid characters in input");
            return "";
        }

        counter = 41;
        for (i = 0; i < source.length(); i++) {
            counter += positionOf(source.charAt(i), hibcCharTable);
        }
        counter = counter % 43;

        if (counter < 10) {
            checkDigit = (char) (counter + '0');
        } else {
            if (counter < 36) {
                checkDigit = (char) ((counter - 10) + 'A');
            } else {
                switch (counter) {
                    case 36:
                        checkDigit = '-';
                        break;
                    case 37:
                        checkDigit = '.';
                        break;
                    case 38:
                        checkDigit = ' ';
                        break;
                    case 39:
                        checkDigit = '$';
                        break;
                    case 40:
                        checkDigit = '/';
                        break;
                    case 41:
                        checkDigit = '+';
                        break;
                    case 42:
                        checkDigit = '%';
                        break;
                    default:
                        checkDigit = ' ';
                        break; /* Keep compiler happy */
                }
            }
        }

        encodeInfo.append("HIBC Check Digit: ").append(counter).append(" (").append(checkDigit).append(")\n");

        toProcess = "+" + source + checkDigit;
        return toProcess;
    }

    /**
     * Returns the intermediate coding of this bar code. Symbol types that use the test
     * infrastructure should override this method.
     *
     * @return the intermediate coding of this bar code
     */
    protected int[] getCodewords() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns this bar code's pattern, converted into a set of corresponding codewords.
     * Useful for bar codes that encode their content as a pattern.
     *
     * @param size the number of digits in each codeword
     * @return this bar code's pattern, converted into a set of corresponding codewords
     */
    int[] getPatternAsCodewords(int size) {
        if (pattern == null || pattern.length == 0) {
            return new int[0];
        } else {
            int count = (int) Math.ceil(pattern[0].length() / (double) size);
            int[] codewords = new int[pattern.length * count];
            for (int i = 0; i < pattern.length; i++) {
                String row = pattern[i];
                for (int j = 0; j < count; j++) {
                    int substringStart = j * size;
                    int substringEnd = Math.min((j + 1) * size, row.length());
                    codewords[(i * count) + j] = Integer.parseInt(row.substring(substringStart, substringEnd));
                }
            }
            return codewords;
        }
    }

    public enum DataType {
        UTF8, LATIN1, BINARY, GS1, HIBC, ECI
    }
}
