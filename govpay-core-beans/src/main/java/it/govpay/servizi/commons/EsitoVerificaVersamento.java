
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per esitoVerificaVersamento.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="esitoVerificaVersamento"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OK"/&gt;
 *     &lt;enumeration value="PAGAMENTO_SCONOSCIUTO"/&gt;
 *     &lt;enumeration value="PAGAMENTO_DUPLICATO"/&gt;
 *     &lt;enumeration value="PAGAMENTO_SCADUTO"/&gt;
 *     &lt;enumeration value="PAGAMENTO_ANNULLATO"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "esitoVerificaVersamento")
@XmlEnum
public enum EsitoVerificaVersamento {

    OK,
    PAGAMENTO_SCONOSCIUTO,
    PAGAMENTO_DUPLICATO,
    PAGAMENTO_SCADUTO,
    PAGAMENTO_ANNULLATO;

    public String value() {
        return name();
    }

    public static EsitoVerificaVersamento fromValue(String v) {
        return valueOf(v);
    }

}
