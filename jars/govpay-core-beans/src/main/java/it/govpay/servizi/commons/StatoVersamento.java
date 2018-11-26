
package it.govpay.servizi.commons;

public enum StatoVersamento {

    NON_ESEGUITO,
    ESEGUITO,
    PARZIALMENTE_ESEGUITO,
    ANNULLATO,
    ANOMALO,
    ESEGUITO_SENZA_RPT;

    public String value() {
        return this.name();
    }

    public static StatoVersamento fromValue(String v) {
        return valueOf(v);
    }

}
