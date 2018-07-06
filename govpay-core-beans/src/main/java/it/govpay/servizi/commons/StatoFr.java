
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per statoFr.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="statoFr"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ACCETTATA"/&gt;
 *     &lt;enumeration value="ANOMALA"/&gt;
 *     &lt;enumeration value="RIFIUTATA"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "statoFr")
@XmlEnum
public enum StatoFr {

    ACCETTATA,
    ANOMALA,

    /**
     * Presente per retrocompatibilita v2.x
     * 
     */
    RIFIUTATA;

    public String value() {
        return name();
    }

    public static StatoFr fromValue(String v) {
        return valueOf(v);
    }

}
