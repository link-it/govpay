
package it.govpay.servizi.v2_3.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per mittente.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="mittente"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NodoDeiPagamentiSPC"/&gt;
 *     &lt;enumeration value="PSP"/&gt;
 *     &lt;enumeration value="GovPay"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "mittente")
@XmlEnum
public enum Mittente {

    @XmlEnumValue("NodoDeiPagamentiSPC")
    NODO_DEI_PAGAMENTI_SPC("NodoDeiPagamentiSPC"),
    PSP("PSP"),
    @XmlEnumValue("GovPay")
    GOV_PAY("GovPay");
    private final String value;

    Mittente(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Mittente fromValue(String v) {
        for (Mittente c: Mittente.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
