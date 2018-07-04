
package it.govpay.servizi.commons;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per tipoVersamento.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoVersamento"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="BBT"/&gt;
 *     &lt;enumeration value="BP"/&gt;
 *     &lt;enumeration value="AD"/&gt;
 *     &lt;enumeration value="CP"/&gt;
 *     &lt;enumeration value="OBEP"/&gt;
 *     &lt;enumeration value="PO"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "tipoVersamento")
@XmlEnum
public enum TipoVersamento {


    /**
     * Bonifico Bancario di Tesoreria
     * 
     */
    BBT,

    /**
     * Bollettino Postale
     * 
     */
    BP,

    /**
     * Addebito diretto
     * 
     */
    AD,

    /**
     * Carta di Pagamento
     * 
     */
    CP,

    /**
     * On-line Banking e-payment
     * 
     */
    OBEP,

    /**
     * Pagamento attivato presso PSP
     * 
     */
    PO;

    public String value() {
        return name();
    }

    public static TipoVersamento fromValue(String v) {
        return valueOf(v);
    }

}
