
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per esitoRendicontazione.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="esitoRendicontazione"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ESEGUITO"/&gt;
 *     &lt;enumeration value="REVOCATO"/&gt;
 *     &lt;enumeration value="ESEGUITO_SENZA_RPT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "esitoRendicontazione")
@XmlEnum
public enum EsitoRendicontazione {

    ESEGUITO,
    REVOCATO,
    ESEGUITO_SENZA_RPT;

    public String value() {
        return name();
    }

    public static EsitoRendicontazione fromValue(String v) {
        return valueOf(v);
    }

}
