
package it.govpay.servizi.commons;

public enum TipoSceltaWisp {

    SI,
    NO,
    PAGA_DOPO;

    public String value() {
        return this.name();
    }

    public static TipoSceltaWisp fromValue(String v) {
        return valueOf(v);
    }

}
