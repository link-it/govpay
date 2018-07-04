
package it.gov.digitpa.schemas._2011.pagamenti;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per stTipoAllegatoRicevuta.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="stTipoAllegatoRicevuta"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ES"/&gt;
 *     &lt;enumeration value="BD"/&gt;
 *     &lt;length value="2"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "stTipoAllegatoRicevuta")
@XmlEnum
public enum StTipoAllegatoRicevuta {

    ES,
    BD;

    public String value() {
        return name();
    }

    public static StTipoAllegatoRicevuta fromValue(String v) {
        return valueOf(v);
    }

}
