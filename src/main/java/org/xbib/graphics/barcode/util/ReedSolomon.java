package org.xbib.graphics.barcode.util;

/**
 */
public class ReedSolomon {
    private int[] res;
    private int logmod;
    private int rlen;
    private int[] logt;
    private int[] alog;
    private int[] rspoly;

    public int getResult(int count) {
        return res[count];
    }

    public void init_gf(int poly) {
        int m, b, p, v;

        // Find the top bit, and hence the symbol size
        for (b = 1, m = 0; b <= poly; b <<= 1) {
            m++;
        }
        b >>= 1;
        m--;

        // Calculate the log/alog tables
        logmod = (1 << m) - 1;
        logt = new int[logmod + 1];
        alog = new int[logmod];

        for (p = 1, v = 0; v < logmod; v++) {
            alog[v] = p;
            logt[p] = v;
            p <<= 1;
            if ((p & b) != 0) {
                p ^= poly;
            }
        }
    }

    public void init_code(int nsym, int index) {
        int i, k;

        rspoly = new int[nsym + 1];

        rlen = nsym;

        rspoly[0] = 1;
        for (i = 1; i <= nsym; i++) {
            rspoly[i] = 1;
            for (k = i - 1; k > 0; k--) {
                if (rspoly[k] != 0) {
                    rspoly[k] = alog[(logt[rspoly[k]] + index) % logmod];
                }
                rspoly[k] ^= rspoly[k - 1];
            }
            rspoly[0] = alog[(logt[rspoly[0]] + index) % logmod];
            index++;
        }
    }

    public void encode(int len, int[] data) {
        int i, k, m;

        res = new int[rlen];
        for (i = 0; i < rlen; i++) {
            res[i] = 0;
        }
        for (i = 0; i < len; i++) {
            m = res[rlen - 1] ^ data[i];
            for (k = rlen - 1; k > 0; k--) {
                if ((m != 0) && (rspoly[k] != 0)) {
                    res[k] = res[k - 1] ^ alog[(logt[m] + logt[rspoly[k]]) % logmod];
                } else {
                    res[k] = res[k - 1];
                }
            }
            if ((m != 0) && (rspoly[0] != 0)) {
                res[0] = alog[(logt[m] + logt[rspoly[0]]) % logmod];
            } else {
                res[0] = 0;
            }
        }
    }
}
