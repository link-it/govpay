
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per statoTransazione.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="statoTransazione"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="RPT_ATTIVATA"/&gt;
 *     &lt;enumeration value="RPT_ERRORE_INVIO_A_NODO"/&gt;
 *     &lt;enumeration value="RPT_RICEVUTA_NODO"/&gt;
 *     &lt;enumeration value="RPT_RIFIUTATA_NODO"/&gt;
 *     &lt;enumeration value="RPT_ACCETTATA_NODO"/&gt;
 *     &lt;enumeration value="RPT_RIFIUTATA_PSP"/&gt;
 *     &lt;enumeration value="RPT_ACCETTATA_PSP"/&gt;
 *     &lt;enumeration value="RPT_ERRORE_INVIO_A_PSP"/&gt;
 *     &lt;enumeration value="RPT_INVIATA_A_PSP"/&gt;
 *     &lt;enumeration value="RPT_DECORSI_TERMINI"/&gt;
 *     &lt;enumeration value="RT_RICEVUTA_NODO"/&gt;
 *     &lt;enumeration value="RT_RIFIUTATA_NODO"/&gt;
 *     &lt;enumeration value="RT_ACCETTATA_NODO"/&gt;
 *     &lt;enumeration value="RT_ACCETTATA_PA"/&gt;
 *     &lt;enumeration value="RT_RIFIUTATA_PA"/&gt;
 *     &lt;enumeration value="RT_ESITO_SCONOSCIUTO_PA"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "statoTransazione")
@XmlEnum
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
        return name();
    }

    public static StatoTransazione fromValue(String v) {
        return valueOf(v);
    }

}
