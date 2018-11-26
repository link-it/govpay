
package it.govpay.servizi.commons;

public enum EsitoTransazione {

    PAGAMENTO_ESEGUITO,
    PAGAMENTO_NON_ESEGUITO,
    PAGAMENTO_PARZIALMENTE_ESEGUITO,
    DECORRENZA_TERMINI,
    DECORRENZA_TERMINI_PARZIALE;

    public String value() {
        return this.name();
    }

    public static EsitoTransazione fromValue(String v) {
        return valueOf(v);
    }

}
