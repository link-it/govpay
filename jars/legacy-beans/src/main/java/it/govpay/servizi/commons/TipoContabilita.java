
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoContabilita.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoContabilita"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CAPITOLO"/&gt;
 *     &lt;enumeration value="SPECIALE"/&gt;
 *     &lt;enumeration value="SIOPE"/&gt;
 *     &lt;enumeration value="ALTRO"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "tipoContabilita")
@XmlEnum
public enum TipoContabilita {

    CAPITOLO,
    SPECIALE,
    SIOPE,
    ALTRO;

    public String value() {
        return name();
    }

    public static TipoContabilita fromValue(String v) {
        return valueOf(v);
    }

}
