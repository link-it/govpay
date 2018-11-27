
package it.govpay.core.beans;

public enum StatoRevoca {

    RR_ATTIVATA,
    RR_ERRORE_INVIO_A_NODO,
    RR_ACCETTATA_NODO,
    RR_RIFIUTATA_NODO,
    ER_ACCETTATA_PA,
    ER_RIFIUTATA_PA;

    public String value() {
        return this.name();
    }

    public static StatoRevoca fromValue(String v) {
        return valueOf(v);
    }

}
