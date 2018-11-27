
package it.govpay.core.beans;

public enum StatoFr {

    ACCETTATA,
    ANOMALA,

    /**
     * Presente per retrocompatibilita v2.x
     * 
     */
    RIFIUTATA;

    public String value() {
        return this.name();
    }

    public static StatoFr fromValue(String v) {
        return valueOf(v);
    }

}
