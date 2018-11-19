
package it.gov.digitpa.schemas._2011.pagamenti;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per stAutenticazioneSoggetto.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="stAutenticazioneSoggetto"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;maxLength value="4"/&gt;
 *     &lt;enumeration value="CNS"/&gt;
 *     &lt;enumeration value="USR"/&gt;
 *     &lt;enumeration value="OTH"/&gt;
 *     &lt;enumeration value="N/A"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "stAutenticazioneSoggetto")
@XmlEnum
public enum StAutenticazioneSoggetto {

    CNS("CNS"),
    USR("USR"),
    OTH("OTH"),
    @XmlEnumValue("N/A")
    N_A("N/A");
    private final String value;

    StAutenticazioneSoggetto(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return this.value;
    }

    public static StAutenticazioneSoggetto fromValue(String v) {
        for (StAutenticazioneSoggetto c: StAutenticazioneSoggetto.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
