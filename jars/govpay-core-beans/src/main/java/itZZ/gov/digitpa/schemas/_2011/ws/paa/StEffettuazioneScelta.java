
package itZZ.gov.digitpa.schemas._2011.ws.paa;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per stEffettuazioneScelta.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="stEffettuazioneScelta"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SI"/&gt;
 *     &lt;enumeration value="NO"/&gt;
 *     &lt;enumeration value="PO"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "stEffettuazioneScelta")
@XmlEnum
public enum StEffettuazioneScelta {

    SI,
    NO,
    PO;

    public String value() {
        return this.name();
    }

    public static StEffettuazioneScelta fromValue(String v) {
        return valueOf(v);
    }

}
