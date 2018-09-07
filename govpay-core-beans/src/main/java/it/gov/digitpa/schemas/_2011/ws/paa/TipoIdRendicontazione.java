
package it.gov.digitpa.schemas._2011.ws.paa;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter2;


/**
 * <p>Classe Java per tipoIdRendicontazione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="tipoIdRendicontazione"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identificativoFlusso" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dataOraFlusso" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoIdRendicontazione", propOrder = {
    "identificativoFlusso",
    "dataOraFlusso"
})
public class TipoIdRendicontazione {

    @XmlElement(required = true)
    protected String identificativoFlusso;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date dataOraFlusso;

    /**
     * Recupera il valore della proprietà identificativoFlusso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoFlusso() {
        return this.identificativoFlusso;
    }

    /**
     * Imposta il valore della proprietà identificativoFlusso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoFlusso(String value) {
        this.identificativoFlusso = value;
    }

    /**
     * Recupera il valore della proprietà dataOraFlusso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataOraFlusso() {
        return this.dataOraFlusso;
    }

    /**
     * Imposta il valore della proprietà dataOraFlusso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataOraFlusso(Date value) {
        this.dataOraFlusso = value;
    }

}
