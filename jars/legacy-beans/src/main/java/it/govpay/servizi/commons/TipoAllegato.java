
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoAllegato.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoAllegato"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ES"/&gt;
 *     &lt;enumeration value="BD"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "tipoAllegato")
@XmlEnum
public enum TipoAllegato {


    /**
     * Esito del pagamento prodotto dal PSP
     * 
     */
    ES,

    /**
     * Marca da Bollo Digitale
     * 
     */
    BD;

    public String value() {
        return name();
    }

    public static TipoAllegato fromValue(String v) {
        return valueOf(v);
    }

}
