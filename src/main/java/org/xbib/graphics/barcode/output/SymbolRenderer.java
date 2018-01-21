package org.xbib.graphics.barcode.output;

import org.xbib.graphics.barcode.Symbol;

import java.io.IOException;

/**
 * Renders symbols to some output format.
 */
public interface SymbolRenderer {

    /**
     * Renders the specified symbology.
     *
     * @param symbol the symbology to render
     * @throws IOException if there is an I/O error
     */
    void render(Symbol symbol) throws IOException;

}
