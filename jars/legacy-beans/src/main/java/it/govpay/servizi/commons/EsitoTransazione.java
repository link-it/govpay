
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for esitoTransazione.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="esitoTransazione"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PAGAMENTO_ESEGUITO"/&gt;
 *     &lt;enumeration value="PAGAMENTO_NON_ESEGUITO"/&gt;
 *     &lt;enumeration value="PAGAMENTO_PARZIALMENTE_ESEGUITO"/&gt;
 *     &lt;enumeration value="DECORRENZA_TERMINI"/&gt;
 *     &lt;enumeration value="DECORRENZA_TERMINI_PARZIALE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "esitoTransazione")
@XmlEnum
public enum EsitoTransazione {

    PAGAMENTO_ESEGUITO,
    PAGAMENTO_NON_ESEGUITO,
    PAGAMENTO_PARZIALMENTE_ESEGUITO,
    DECORRENZA_TERMINI,
    DECORRENZA_TERMINI_PARZIALE;

    public String value() {
        return name();
    }

    public static EsitoTransazione fromValue(String v) {
        return valueOf(v);
    }

}
