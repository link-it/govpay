
package it.govpay.servizi.commons;

public enum StatoTransazione {

    RPT_ATTIVATA,
    RPT_ERRORE_INVIO_A_NODO,
    RPT_RICEVUTA_NODO,
    RPT_RIFIUTATA_NODO,
    RPT_ACCETTATA_NODO,
    RPT_RIFIUTATA_PSP,
    RPT_ACCETTATA_PSP,
    RPT_ERRORE_INVIO_A_PSP,
    RPT_INVIATA_A_PSP,
    RPT_DECORSI_TERMINI,
    RT_RICEVUTA_NODO,
    RT_RIFIUTATA_NODO,
    RT_ACCETTATA_NODO,
    RT_ACCETTATA_PA,
    RT_RIFIUTATA_PA,
    RT_ESITO_SCONOSCIUTO_PA;

    public String value() {
        return this.name();
    }

    public static StatoTransazione fromValue(String v) {
        return valueOf(v);
    }

}
