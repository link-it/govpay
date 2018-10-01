
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per statoVersamento.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
        return this.name();
    }

    public static StatoVersamento fromValue(String v) {
        return valueOf(v);
    }

}
