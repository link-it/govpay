
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per modelloPagamento.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
        return this.name();
    }

    public static ModelloPagamento fromValue(String v) {
        return valueOf(v);
    }

}
