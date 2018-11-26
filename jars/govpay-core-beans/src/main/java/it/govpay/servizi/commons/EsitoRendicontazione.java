
package it.govpay.servizi.commons;

public enum EsitoRendicontazione {

    ESEGUITO,
    REVOCATO,
    ESEGUITO_SENZA_RPT;

    public String value() {
        return this.name();
    }

    public static EsitoRendicontazione fromValue(String v) {
        return valueOf(v);
    }

}
