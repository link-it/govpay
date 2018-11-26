
package it.govpay.servizi.commons;

public enum TipoRendicontazione {

    ESEGUITO,
    REVOCATO,
    ESEGUITO_SENZA_RPT;

    public String value() {
        return this.name();
    }

    public static TipoRendicontazione fromValue(String v) {
        return valueOf(v);
    }

}
