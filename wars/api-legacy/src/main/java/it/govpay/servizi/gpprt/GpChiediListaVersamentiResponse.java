
package it.govpay.servizi.gpprt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import it.govpay.servizi.commons.GpResponse;
import it.govpay.servizi.commons.StatoVersamento;
import org.w3._2001.xmlschema.Adapter1;
import org.w3._2001.xmlschema.Adapter2;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.govpay.it/servizi/commons/}gpResponse"&gt;
 *       &lt;sequence minOccurs="0"&gt;
 *         &lt;element name="versamento" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *                   &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *                   &lt;element name="numeroAvviso" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *                   &lt;element name="qrCode" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *                   &lt;element name="barCode" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *                   &lt;element name="stato" type="{http://www.govpay.it/servizi/commons/}statoVersamento"/&gt;
 *                   &lt;element name="importoTotale" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
 *                   &lt;choice&gt;
 *                     &lt;element name="causale" type="{http://www.govpay.it/servizi/commons/}string140" minOccurs="0"/&gt;
 *                     &lt;element name="spezzoneCausale" type="{http://www.govpay.it/servizi/commons/}string35" maxOccurs="6" minOccurs="0"/&gt;
 *                     &lt;element name="spezzoneCausaleStrutturata" maxOccurs="6" minOccurs="0"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="causale" type="{http://www.govpay.it/servizi/commons/}string20"/&gt;
 *                               &lt;element name="importo" type="{http://www.govpay.it/servizi/commons/}importo10"/&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                   &lt;/choice&gt;
 *                   &lt;element name="dataScadenza" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
@XmlType(name = "", propOrder = {
    "versamento"
})
@XmlRootElement(name = "gpChiediListaVersamentiResponse")
public class GpChiediListaVersamentiResponse
    extends GpResponse
{

    protected List<GpChiediListaVersamentiResponse.Versamento> versamento;

    /**
     * Gets the value of the versamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the versamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVersamento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GpChiediListaVersamentiResponse.Versamento }
     * 
     * 
     */
    public List<GpChiediListaVersamentiResponse.Versamento> getVersamento() {
        if (versamento == null) {
            versamento = new ArrayList<GpChiediListaVersamentiResponse.Versamento>();
        }
        return this.versamento;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
     *         &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
     *         &lt;element name="numeroAvviso" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
     *         &lt;element name="qrCode" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
     *         &lt;element name="barCode" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
     *         &lt;element name="stato" type="{http://www.govpay.it/servizi/commons/}statoVersamento"/&gt;
     *         &lt;element name="importoTotale" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
     *         &lt;choice&gt;
     *           &lt;element name="causale" type="{http://www.govpay.it/servizi/commons/}string140" minOccurs="0"/&gt;
     *           &lt;element name="spezzoneCausale" type="{http://www.govpay.it/servizi/commons/}string35" maxOccurs="6" minOccurs="0"/&gt;
     *           &lt;element name="spezzoneCausaleStrutturata" maxOccurs="6" minOccurs="0"&gt;
     *             &lt;complexType&gt;
     *               &lt;complexContent&gt;
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                   &lt;sequence&gt;
     *                     &lt;element name="causale" type="{http://www.govpay.it/servizi/commons/}string20"/&gt;
     *                     &lt;element name="importo" type="{http://www.govpay.it/servizi/commons/}importo10"/&gt;
     *                   &lt;/sequence&gt;
     *                 &lt;/restriction&gt;
     *               &lt;/complexContent&gt;
     *             &lt;/complexType&gt;
     *           &lt;/element&gt;
     *         &lt;/choice&gt;
     *         &lt;element name="dataScadenza" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
        "codVersamentoEnte",
        "codDominio",
        "iuv",
        "numeroAvviso",
        "qrCode",
        "barCode",
        "stato",
        "importoTotale",
        "causale",
        "spezzoneCausale",
        "spezzoneCausaleStrutturata",
        "dataScadenza"
    })
    public static class Versamento {

        @XmlElement(required = true)
        protected String codApplicazione;
        @XmlElement(required = true)
        protected String codVersamentoEnte;
        protected String codDominio;
        protected String iuv;
        protected String numeroAvviso;
        protected byte[] qrCode;
        protected byte[] barCode;
        @XmlElement(required = true)
        @XmlSchemaType(name = "string")
        protected StatoVersamento stato;
        @XmlElement(required = true, type = String.class)
        @XmlJavaTypeAdapter(Adapter1 .class)
        @XmlSchemaType(name = "decimal")
        protected BigDecimal importoTotale;
        protected String causale;
        protected List<String> spezzoneCausale;
        protected List<GpChiediListaVersamentiResponse.Versamento.SpezzoneCausaleStrutturata> spezzoneCausaleStrutturata;
        @XmlElement(type = String.class)
        @XmlJavaTypeAdapter(Adapter2 .class)
        @XmlSchemaType(name = "dateTime")
        protected Date dataScadenza;

        /**
         * Gets the value of the codApplicazione property.
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
         * Sets the value of the codApplicazione property.
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
         * Gets the value of the codVersamentoEnte property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodVersamentoEnte() {
            return codVersamentoEnte;
        }

        /**
         * Sets the value of the codVersamentoEnte property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodVersamentoEnte(String value) {
            this.codVersamentoEnte = value;
        }

        /**
         * Gets the value of the codDominio property.
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
         * Sets the value of the codDominio property.
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
         * Gets the value of the iuv property.
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
         * Sets the value of the iuv property.
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
         * Gets the value of the numeroAvviso property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumeroAvviso() {
            return numeroAvviso;
        }

        /**
         * Sets the value of the numeroAvviso property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumeroAvviso(String value) {
            this.numeroAvviso = value;
        }

        /**
         * Gets the value of the qrCode property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getQrCode() {
            return qrCode;
        }

        /**
         * Sets the value of the qrCode property.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setQrCode(byte[] value) {
            this.qrCode = value;
        }

        /**
         * Gets the value of the barCode property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getBarCode() {
            return barCode;
        }

        /**
         * Sets the value of the barCode property.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setBarCode(byte[] value) {
            this.barCode = value;
        }

        /**
         * Gets the value of the stato property.
         * 
         * @return
         *     possible object is
         *     {@link StatoVersamento }
         *     
         */
        public StatoVersamento getStato() {
            return stato;
        }

        /**
         * Sets the value of the stato property.
         * 
         * @param value
         *     allowed object is
         *     {@link StatoVersamento }
         *     
         */
        public void setStato(StatoVersamento value) {
            this.stato = value;
        }

        /**
         * Gets the value of the importoTotale property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public BigDecimal getImportoTotale() {
            return importoTotale;
        }

        /**
         * Sets the value of the importoTotale property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setImportoTotale(BigDecimal value) {
            this.importoTotale = value;
        }

        /**
         * Gets the value of the causale property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCausale() {
            return causale;
        }

        /**
         * Sets the value of the causale property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCausale(String value) {
            this.causale = value;
        }

        /**
         * Gets the value of the spezzoneCausale property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the spezzoneCausale property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSpezzoneCausale().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getSpezzoneCausale() {
            if (spezzoneCausale == null) {
                spezzoneCausale = new ArrayList<String>();
            }
            return this.spezzoneCausale;
        }

        /**
         * Gets the value of the spezzoneCausaleStrutturata property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the spezzoneCausaleStrutturata property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSpezzoneCausaleStrutturata().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GpChiediListaVersamentiResponse.Versamento.SpezzoneCausaleStrutturata }
         * 
         * 
         */
        public List<GpChiediListaVersamentiResponse.Versamento.SpezzoneCausaleStrutturata> getSpezzoneCausaleStrutturata() {
            if (spezzoneCausaleStrutturata == null) {
                spezzoneCausaleStrutturata = new ArrayList<GpChiediListaVersamentiResponse.Versamento.SpezzoneCausaleStrutturata>();
            }
            return this.spezzoneCausaleStrutturata;
        }

        /**
         * Gets the value of the dataScadenza property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Date getDataScadenza() {
            return dataScadenza;
        }

        /**
         * Sets the value of the dataScadenza property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDataScadenza(Date value) {
            this.dataScadenza = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="causale" type="{http://www.govpay.it/servizi/commons/}string20"/&gt;
         *         &lt;element name="importo" type="{http://www.govpay.it/servizi/commons/}importo10"/&gt;
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
            "causale",
            "importo"
        })
        public static class SpezzoneCausaleStrutturata {

            @XmlElement(required = true)
            protected String causale;
            @XmlElement(required = true, type = String.class)
            @XmlJavaTypeAdapter(Adapter1 .class)
            @XmlSchemaType(name = "decimal")
            protected BigDecimal importo;

            /**
             * Gets the value of the causale property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCausale() {
                return causale;
            }

            /**
             * Sets the value of the causale property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCausale(String value) {
                this.causale = value;
            }

            /**
             * Gets the value of the importo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public BigDecimal getImporto() {
                return importo;
            }

            /**
             * Sets the value of the importo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setImporto(BigDecimal value) {
                this.importo = value;
            }

        }

    }

}
