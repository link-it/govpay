
package it.gov.digitpa.schemas._2011.pagamenti;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per stTipoIdentificativoUnivocoPersFG.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="stTipoIdentificativoUnivocoPersFG"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="F"/&gt;
 *     &lt;enumeration value="G"/&gt;
 *     &lt;length value="1"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "stTipoIdentificativoUnivocoPersFG")
@XmlEnum
public enum StTipoIdentificativoUnivocoPersFG {

    F,
    G;

    public String value() {
        return name();
    }

    public static StTipoIdentificativoUnivocoPersFG fromValue(String v) {
        return valueOf(v);
    }

}
