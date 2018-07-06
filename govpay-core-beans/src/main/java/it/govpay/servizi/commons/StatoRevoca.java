
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per statoRevoca.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="statoRevoca"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="RR_ATTIVATA"/&gt;
 *     &lt;enumeration value="RR_ERRORE_INVIO_A_NODO"/&gt;
 *     &lt;enumeration value="RR_ACCETTATA_NODO"/&gt;
 *     &lt;enumeration value="RR_RIFIUTATA_NODO"/&gt;
 *     &lt;enumeration value="ER_ACCETTATA_PA"/&gt;
 *     &lt;enumeration value="ER_RIFIUTATA_PA"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "statoRevoca")
@XmlEnum
public enum StatoRevoca {

    RR_ATTIVATA,
    RR_ERRORE_INVIO_A_NODO,
    RR_ACCETTATA_NODO,
    RR_RIFIUTATA_NODO,
    ER_ACCETTATA_PA,
    ER_RIFIUTATA_PA;

    public String value() {
        return name();
    }

    public static StatoRevoca fromValue(String v) {
        return valueOf(v);
    }

}
