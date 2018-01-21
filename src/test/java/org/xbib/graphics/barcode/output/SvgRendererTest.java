package org.xbib.graphics.barcode.output;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.xbib.graphics.barcode.Code93;
import org.xbib.graphics.barcode.MaxiCode;
import org.xbib.graphics.barcode.Symbol;
import org.xbib.graphics.barcode.output.SvgRenderer;

/**
 * Tests for {@link SvgRenderer}.
 */
public class SvgRendererTest {

    private Locale originalDefaultLocale;

    @Before
    public void before() {
        // ensure use of correct decimal separator (period), regardless of default locale
        originalDefaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.GERMANY);
    }

    @After
    public void after() {
        Locale.setDefault(originalDefaultLocale);
    }

    @Test
    public void testCode93Basic() throws IOException {
        Code93 code93 = new Code93();
        code93.setContent("123456789");
        test(code93, 1, Color.WHITE, Color.BLACK, 5, "code93-basic.svg");
    }

    @Test
    public void testCode93Margin() throws IOException {
        Code93 code93 = new Code93();
        code93.setContent("123456789");
        test(code93, 1, Color.WHITE, Color.BLACK, 20, "code93-margin-size-20.svg");
    }

    @Test
    public void testCode93Magnification() throws IOException {
        Code93 code93 = new Code93();
        code93.setContent("123456789");
        test(code93, 2, Color.WHITE, Color.BLACK, 5, "code93-magnification-2.svg");
    }

    @Test
    public void testCode93Colors() throws IOException {
        Code93 code93 = new Code93();
        code93.setContent("123456789");
        test(code93, 1, Color.GREEN, Color.RED, 5, "code93-colors.svg");
    }

    @Test
    public void testCode93CustomFont() throws IOException {
        Code93 code93 = new Code93();
        code93.setFontName("Arial");
        code93.setFontSize(26);
        code93.setContent("123456789");
        test(code93, 1, Color.WHITE, Color.BLACK, 5, "code93-custom-font.svg");
    }

    @Test
    public void testMaxiCodeBasic() throws IOException {
        MaxiCode maxicode = new MaxiCode();
        maxicode.setMode(4);
        maxicode.setContent("123456789");
        test(maxicode, 1, Color.WHITE, Color.BLACK, 5, "maxicode-basic.svg");
    }

    private void test(Symbol symbol, double magnification, Color paper, Color ink, int margin, String expectationFile) throws IOException {

        symbol.setQuietZoneHorizontal(margin);
        symbol.setQuietZoneVertical(margin);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SvgRenderer renderer = new SvgRenderer(baos, magnification, paper, ink);
        renderer.render(symbol);
        String actual = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        BufferedReader actualReader = new BufferedReader(new StringReader(actual));

        InputStream is = getClass().getResourceAsStream(expectationFile);
        byte[] expectedBytes = new byte[is.available()];
        is.read(expectedBytes);
        String expected = new String(expectedBytes, StandardCharsets.UTF_8);
        BufferedReader expectedReader = new BufferedReader(new StringReader(expected));

        int line = 1;
        String actualLine = actualReader.readLine();
        String expectedLine = expectedReader.readLine();
        while (actualLine != null && expectedLine != null) {
            assertEquals("Line " + line, expectedLine, actualLine);
            actualLine = actualReader.readLine();
            expectedLine = expectedReader.readLine();
            line++;
        }
    }
}
