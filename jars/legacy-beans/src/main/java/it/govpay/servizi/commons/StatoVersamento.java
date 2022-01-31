
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for statoVersamento.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="statoVersamento"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NON_ESEGUITO"/&gt;
 *     &lt;enumeration value="ESEGUITO"/&gt;
 *     &lt;enumeration value="PARZIALMENTE_ESEGUITO"/&gt;
 *     &lt;enumeration value="ANNULLATO"/&gt;
 *     &lt;enumeration value="ANOMALO"/&gt;
 *     &lt;enumeration value="ESEGUITO_SENZA_RPT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "statoVersamento")
@XmlEnum
public enum StatoVersamento {

    NON_ESEGUITO,
    ESEGUITO,
    PARZIALMENTE_ESEGUITO,
    ANNULLATO,
    ANOMALO,
    ESEGUITO_SENZA_RPT;

    public String value() {
        return name();
    }

    public static StatoVersamento fromValue(String v) {
        return valueOf(v);
    }

}
