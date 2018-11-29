package org.xbib.graphics.barcode;

/**
 * Implements Code 3 of 9 Extended, also known as Code 39e and Code39+.
 * Supports encoding of all characters in the 7-bit ASCII table. A
 * modulo-43 check digit can be added if required.
 */
public class Code3Of9Extended extends Symbol {

    private final String[] ECode39 = {
            "%U", "$A", "$B", "$C", "$D", "$E", "$F", "$G", "$H", "$I", "$J", "$K",
            "$L", "$M", "$N", "$O", "$P", "$Q", "$R", "$S", "$T", "$U", "$V", "$W",
            "$X", "$Y", "$Z", "%A", "%B", "%C", "%D", "%E", " ", "/A", "/B", "/C",
            "/D", "/E", "/F", "/G", "/H", "/I", "/J", "/K", "/L", "-", ".", "/O",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "/Z", "%F", "%G",
            "%H", "%I", "%J", "%V", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
            "X", "Y", "Z", "%K", "%L", "%M", "%N", "%O", "%W", "+A", "+B", "+C",
            "+D", "+E", "+F", "+G", "+H", "+I", "+J", "+K", "+L", "+M", "+N", "+O",
            "+P", "+Q", "+R", "+S", "+T", "+U", "+V", "+W", "+X", "+Y", "+Z", "%P",
            "%Q", "%R", "%S", "%T"
    };
    private CheckDigit checkOption;

    public Code3Of9Extended() {
        checkOption = CheckDigit.NONE;
    }

    /**
     * Select addition of optional Modulo-43 check digit or encoding without
     * check digit.
     *
     * @param checkMode Check digit option
     */
    public void setCheckDigit(CheckDigit checkMode) {
        checkOption = checkMode;
    }

    @Override
    public boolean encode() {
        StringBuilder buffer = new StringBuilder();
        int l = content.length();
        int asciicode;
        Code3Of9 c = new Code3Of9();

        if (checkOption == CheckDigit.MOD43) {
            c.setCheckDigit(Code3Of9.CheckDigit.MOD43);
        }

        if (!content.matches("[\u0000-\u007F]+")) {
            errorMsg.append("Invalid characters in input data");
            return false;
        }

        for (int i = 0; i < l; i++) {
            asciicode = content.charAt(i);
            buffer.append(ECode39[asciicode]);
        }

        try {
            c.setContent(buffer.toString());
        } catch (Exception e) {
            errorMsg.append(e.getMessage());
            return false;
        }
        readable = new StringBuilder(content);
        pattern = new String[1];
        pattern[0] = c.pattern[0];
        rowCount = 1;
        rowHeight = new int[1];
        rowHeight[0] = -1;
        plotSymbol();
        return true;
    }

    public enum CheckDigit {
        NONE, MOD43
    }
}
