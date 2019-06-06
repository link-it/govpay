
package it.govpay.core.beans;

public enum EsitoVerificaVersamento {

    OK,
    PAGAMENTO_SCONOSCIUTO,
    PAGAMENTO_DUPLICATO,
    PAGAMENTO_SCADUTO,
    PAGAMENTO_ANNULLATO;

    public String value() {
        return this.name();
    }

    public static EsitoVerificaVersamento fromValue(String v) {
        return valueOf(v);
    }

}
