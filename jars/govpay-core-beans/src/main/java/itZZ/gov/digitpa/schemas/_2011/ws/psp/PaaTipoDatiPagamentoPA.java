
package itZZ.gov.digitpa.schemas._2011.ws.psp;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import org.w3._2001.xmlschema.Adapter1;


/**
 * <p>Classe Java per paaTipoDatiPagamentoPA complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="paaTipoDatiPagamentoPA"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="importoSingoloVersamento" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stImporto"/&gt;
 *         &lt;element name="ibanAccredito" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stIBANIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="bicAccredito" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stBICIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="enteBeneficiario" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}ctEnteBeneficiario" minOccurs="0"/&gt;
 *         &lt;element name="credenzialiPagatore" type="{http://www.digitpa.gov.it/schemas/2011/Pagamenti/}stText35" minOccurs="0"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="causaleVersamento" type="{http://ws.pagamenti.telematici.gov/}stText140"/&gt;
 *           &lt;element name="spezzoniCausaleVersamento" type="{http://ws.pagamenti.telematici.gov/}ctSpezzoniCausaleVersamento"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paaTipoDatiPagamentoPA", propOrder = {
    "importoSingoloVersamento",
    "ibanAccredito",
    "bicAccredito",
    "enteBeneficiario",
    "credenzialiPagatore",
    "causaleVersamento",
    "spezzoniCausaleVersamento"
})
public class PaaTipoDatiPagamentoPA {

    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "decimal")
    protected BigDecimal importoSingoloVersamento;
    protected String ibanAccredito;
    protected String bicAccredito;
    protected CtEnteBeneficiario enteBeneficiario;
    protected String credenzialiPagatore;
    protected String causaleVersamento;
    protected CtSpezzoniCausaleVersamento spezzoniCausaleVersamento;

    /**
     * Recupera il valore della proprietà importoSingoloVersamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getImportoSingoloVersamento() {
        return this.importoSingoloVersamento;
    }

    /**
     * Imposta il valore della proprietà importoSingoloVersamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImportoSingoloVersamento(BigDecimal value) {
        this.importoSingoloVersamento = value;
    }

    /**
     * Recupera il valore della proprietà ibanAccredito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIbanAccredito() {
        return this.ibanAccredito;
    }

    /**
     * Imposta il valore della proprietà ibanAccredito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIbanAccredito(String value) {
        this.ibanAccredito = value;
    }

    /**
     * Recupera il valore della proprietà bicAccredito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBicAccredito() {
        return this.bicAccredito;
    }

    /**
     * Imposta il valore della proprietà bicAccredito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBicAccredito(String value) {
        this.bicAccredito = value;
    }

    /**
     * Recupera il valore della proprietà enteBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link CtEnteBeneficiario }
     *     
     */
    public CtEnteBeneficiario getEnteBeneficiario() {
        return this.enteBeneficiario;
    }

    /**
     * Imposta il valore della proprietà enteBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link CtEnteBeneficiario }
     *     
     */
    public void setEnteBeneficiario(CtEnteBeneficiario value) {
        this.enteBeneficiario = value;
    }

    /**
     * Recupera il valore della proprietà credenzialiPagatore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCredenzialiPagatore() {
        return this.credenzialiPagatore;
    }

    /**
     * Imposta il valore della proprietà credenzialiPagatore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCredenzialiPagatore(String value) {
        this.credenzialiPagatore = value;
    }

    /**
     * Recupera il valore della proprietà causaleVersamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausaleVersamento() {
        return this.causaleVersamento;
    }

    /**
     * Imposta il valore della proprietà causaleVersamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCausaleVersamento(String value) {
        this.causaleVersamento = value;
    }

    /**
     * Recupera il valore della proprietà spezzoniCausaleVersamento.
     * 
     * @return
     *     possible object is
     *     {@link CtSpezzoniCausaleVersamento }
     *     
     */
    public CtSpezzoniCausaleVersamento getSpezzoniCausaleVersamento() {
        return this.spezzoniCausaleVersamento;
    }

    /**
     * Imposta il valore della proprietà spezzoniCausaleVersamento.
     * 
     * @param value
     *     allowed object is
     *     {@link CtSpezzoniCausaleVersamento }
     *     
     */
    public void setSpezzoniCausaleVersamento(CtSpezzoniCausaleVersamento value) {
        this.spezzoniCausaleVersamento = value;
    }

}
