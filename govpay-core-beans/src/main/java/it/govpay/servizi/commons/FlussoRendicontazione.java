
package it.govpay.servizi.commons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter1;
import org.w3._2001.xmlschema.Adapter2;


/**
 * <p>Classe Java per flussoRendicontazione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="flussoRendicontazione"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.govpay.it/servizi/commons/}estremiFlussoRendicontazione"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pagamento" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *                   &lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *                   &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *                   &lt;element name="importoRendicontato" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
 *                   &lt;element name="iur" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="dataRendicontazione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="esitoRendicontazione" type="{http://www.govpay.it/servizi/commons/}tipoRendicontazione"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "flussoRendicontazione", propOrder = {
    "pagamento"
})
public class FlussoRendicontazione
    extends EstremiFlussoRendicontazione
{

    protected List<FlussoRendicontazione.Pagamento> pagamento;

    /**
     * Gets the value of the pagamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pagamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPagamento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FlussoRendicontazione.Pagamento }
     * 
     * 
     */
    public List<FlussoRendicontazione.Pagamento> getPagamento() {
        if (pagamento == null) {
            pagamento = new ArrayList<FlussoRendicontazione.Pagamento>();
        }
        return this.pagamento;
    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
     *         &lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
     *         &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
     *         &lt;element name="importoRendicontato" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
     *         &lt;element name="iur" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="dataRendicontazione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="esitoRendicontazione" type="{http://www.govpay.it/servizi/commons/}tipoRendicontazione"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "codApplicazione",
        "codSingoloVersamentoEnte",
        "codDominio",
        "iuv",
        "importoRendicontato",
        "iur",
        "dataRendicontazione",
        "esitoRendicontazione"
    })
    public static class Pagamento {

        protected String codApplicazione;
        @XmlElement(required = true)
        protected String codSingoloVersamentoEnte;
        protected String codDominio;
        protected String iuv;
        @XmlElement(required = true, type = String.class)
        @XmlJavaTypeAdapter(Adapter1 .class)
        @XmlSchemaType(name = "decimal")
        protected BigDecimal importoRendicontato;
        @XmlElement(required = true)
        protected String iur;
        @XmlElement(required = true, type = String.class)
        @XmlJavaTypeAdapter(Adapter2 .class)
        @XmlSchemaType(name = "dateTime")
        protected Date dataRendicontazione;
        @XmlElement(required = true)
        @XmlSchemaType(name = "string")
        protected TipoRendicontazione esitoRendicontazione;

        /**
         * Recupera il valore della proprietà codApplicazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodApplicazione() {
            return codApplicazione;
        }

        /**
         * Imposta il valore della proprietà codApplicazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodApplicazione(String value) {
            this.codApplicazione = value;
        }

        /**
         * Recupera il valore della proprietà codSingoloVersamentoEnte.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodSingoloVersamentoEnte() {
            return codSingoloVersamentoEnte;
        }

        /**
         * Imposta il valore della proprietà codSingoloVersamentoEnte.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodSingoloVersamentoEnte(String value) {
            this.codSingoloVersamentoEnte = value;
        }

        /**
         * Recupera il valore della proprietà codDominio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodDominio() {
            return codDominio;
        }

        /**
         * Imposta il valore della proprietà codDominio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodDominio(String value) {
            this.codDominio = value;
        }

        /**
         * Recupera il valore della proprietà iuv.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIuv() {
            return iuv;
        }

        /**
         * Imposta il valore della proprietà iuv.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIuv(String value) {
            this.iuv = value;
        }

        /**
         * Recupera il valore della proprietà importoRendicontato.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public BigDecimal getImportoRendicontato() {
            return importoRendicontato;
        }

        /**
         * Imposta il valore della proprietà importoRendicontato.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setImportoRendicontato(BigDecimal value) {
            this.importoRendicontato = value;
        }

        /**
         * Recupera il valore della proprietà iur.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIur() {
            return iur;
        }

        /**
         * Imposta il valore della proprietà iur.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIur(String value) {
            this.iur = value;
        }

        /**
         * Recupera il valore della proprietà dataRendicontazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Date getDataRendicontazione() {
            return dataRendicontazione;
        }

        /**
         * Imposta il valore della proprietà dataRendicontazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDataRendicontazione(Date value) {
            this.dataRendicontazione = value;
        }

        /**
         * Recupera il valore della proprietà esitoRendicontazione.
         * 
         * @return
         *     possible object is
         *     {@link TipoRendicontazione }
         *     
         */
        public TipoRendicontazione getEsitoRendicontazione() {
            return esitoRendicontazione;
        }

        /**
         * Imposta il valore della proprietà esitoRendicontazione.
         * 
         * @param value
         *     allowed object is
         *     {@link TipoRendicontazione }
         *     
         */
        public void setEsitoRendicontazione(TipoRendicontazione value) {
            this.esitoRendicontazione = value;
        }

    }

}
