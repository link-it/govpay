
package it.govpay.core.beans;

public enum TipoVersamento {

    BBT,
    BP,
    AD,
    CP,
    PO,
    OBEP,
    OTH,
    JIF;

    public String value() {
        return this.name();
    }

    public static TipoVersamento fromValue(String v) {
        return valueOf(v);
    }

}
