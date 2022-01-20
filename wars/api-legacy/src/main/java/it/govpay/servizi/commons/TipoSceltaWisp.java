
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoSceltaWisp.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoSceltaWisp"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SI"/&gt;
 *     &lt;enumeration value="NO"/&gt;
 *     &lt;enumeration value="PAGA_DOPO"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "tipoSceltaWisp")
@XmlEnum
public enum TipoSceltaWisp {

    SI,
    NO,
    PAGA_DOPO;

    public String value() {
        return name();
    }

    public static TipoSceltaWisp fromValue(String v) {
        return valueOf(v);
    }

}
