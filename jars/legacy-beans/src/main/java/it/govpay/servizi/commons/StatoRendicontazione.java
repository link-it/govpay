
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for statoRendicontazione.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="statoRendicontazione"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OK"/&gt;
 *     &lt;enumeration value="ANOMALA"/&gt;
 *     &lt;enumeration value="ALTRO_INTERMEDIARIO"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "statoRendicontazione")
@XmlEnum
public enum StatoRendicontazione {

    OK,
    ANOMALA,
    ALTRO_INTERMEDIARIO;

    public String value() {
        return name();
    }

    public static StatoRendicontazione fromValue(String v) {
        return valueOf(v);
    }

}
