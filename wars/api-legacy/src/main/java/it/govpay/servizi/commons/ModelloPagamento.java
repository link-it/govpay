
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for modelloPagamento.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="modelloPagamento"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IMMEDIATO"/&gt;
 *     &lt;enumeration value="IMMEDIATO_MULTIBENEFICIARIO"/&gt;
 *     &lt;enumeration value="DIFFERITO"/&gt;
 *     &lt;enumeration value="ATTIVATO_PRESSO_PSP"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "modelloPagamento")
@XmlEnum
public enum ModelloPagamento {

    IMMEDIATO,
    IMMEDIATO_MULTIBENEFICIARIO,
    DIFFERITO,
    ATTIVATO_PRESSO_PSP;

    public String value() {
        return name();
    }

    public static ModelloPagamento fromValue(String v) {
        return valueOf(v);
    }

}
