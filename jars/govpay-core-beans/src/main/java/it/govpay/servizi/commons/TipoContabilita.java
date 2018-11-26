
package it.govpay.servizi.commons;

public enum TipoContabilita {

    CAPITOLO,
    SPECIALE,
    SIOPE,
    ALTRO;

    public String value() {
        return this.name();
    }

    public static TipoContabilita fromValue(String v) {
        return valueOf(v);
    }

}
