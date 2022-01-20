
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
 * <p>Java class for flussoRendicontazione_2.3 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="flussoRendicontazione_2.3"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.govpay.it/servizi/commons/}estremiFlussoRendicontazione_2.3"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="rendicontazione" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="iur" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="importoRendicontato" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
 *                   &lt;element name="esito" type="{http://www.govpay.it/servizi/commons/}esitoRendicontazione"/&gt;
 *                   &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="stato" type="{http://www.govpay.it/servizi/commons/}statoRendicontazione"/&gt;
 *                   &lt;element name="anomalia" type="{http://www.govpay.it/servizi/commons/}anomalia" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                   &lt;element name="pagamento" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                             &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                             &lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
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
@XmlType(name = "flussoRendicontazione_2.3", propOrder = {
    "rendicontazione"
})
public class FlussoRendicontazione23
    extends EstremiFlussoRendicontazione23
{

    protected List<FlussoRendicontazione23 .Rendicontazione> rendicontazione;

    /**
     * Gets the value of the rendicontazione property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rendicontazione property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRendicontazione().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FlussoRendicontazione23 .Rendicontazione }
     * 
     * 
     */
    public List<FlussoRendicontazione23 .Rendicontazione> getRendicontazione() {
        if (rendicontazione == null) {
            rendicontazione = new ArrayList<FlussoRendicontazione23 .Rendicontazione>();
        }
        return this.rendicontazione;
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
     *         &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="iur" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="importoRendicontato" type="{http://www.govpay.it/servizi/commons/}importo"/&gt;
     *         &lt;element name="esito" type="{http://www.govpay.it/servizi/commons/}esitoRendicontazione"/&gt;
     *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="stato" type="{http://www.govpay.it/servizi/commons/}statoRendicontazione"/&gt;
     *         &lt;element name="anomalia" type="{http://www.govpay.it/servizi/commons/}anomalia" maxOccurs="unbounded" minOccurs="0"/&gt;
     *         &lt;element name="pagamento" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *                   &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *                   &lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
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
        "iuv",
        "iur",
        "importoRendicontato",
        "esito",
        "data",
        "stato",
        "anomalia",
        "pagamento"
    })
    public static class Rendicontazione {

        @XmlElement(required = true)
        protected String iuv;
        @XmlElement(required = true)
        protected String iur;
        @XmlElement(required = true, type = String.class)
        @XmlJavaTypeAdapter(Adapter1 .class)
        @XmlSchemaType(name = "decimal")
        protected BigDecimal importoRendicontato;
        @XmlElement(required = true)
        @XmlSchemaType(name = "string")
        protected EsitoRendicontazione esito;
        @XmlElement(required = true, type = String.class)
        @XmlJavaTypeAdapter(Adapter2 .class)
        @XmlSchemaType(name = "dateTime")
        protected Date data;
        @XmlElement(required = true)
        @XmlSchemaType(name = "string")
        protected StatoRendicontazione stato;
        protected List<Anomalia> anomalia;
        protected FlussoRendicontazione23 .Rendicontazione.Pagamento pagamento;

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
         * Gets the value of the iur property.
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
         * Sets the value of the iur property.
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
         * Gets the value of the importoRendicontato property.
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
         * Sets the value of the importoRendicontato property.
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
         * Gets the value of the esito property.
         * 
         * @return
         *     possible object is
         *     {@link EsitoRendicontazione }
         *     
         */
        public EsitoRendicontazione getEsito() {
            return esito;
        }

        /**
         * Sets the value of the esito property.
         * 
         * @param value
         *     allowed object is
         *     {@link EsitoRendicontazione }
         *     
         */
        public void setEsito(EsitoRendicontazione value) {
            this.esito = value;
        }

        /**
         * Gets the value of the data property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Date getData() {
            return data;
        }

        /**
         * Sets the value of the data property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setData(Date value) {
            this.data = value;
        }

        /**
         * Gets the value of the stato property.
         * 
         * @return
         *     possible object is
         *     {@link StatoRendicontazione }
         *     
         */
        public StatoRendicontazione getStato() {
            return stato;
        }

        /**
         * Sets the value of the stato property.
         * 
         * @param value
         *     allowed object is
         *     {@link StatoRendicontazione }
         *     
         */
        public void setStato(StatoRendicontazione value) {
            this.stato = value;
        }

        /**
         * Gets the value of the anomalia property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the anomalia property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAnomalia().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Anomalia }
         * 
         * 
         */
        public List<Anomalia> getAnomalia() {
            if (anomalia == null) {
                anomalia = new ArrayList<Anomalia>();
            }
            return this.anomalia;
        }

        /**
         * Gets the value of the pagamento property.
         * 
         * @return
         *     possible object is
         *     {@link FlussoRendicontazione23 .Rendicontazione.Pagamento }
         *     
         */
        public FlussoRendicontazione23 .Rendicontazione.Pagamento getPagamento() {
            return pagamento;
        }

        /**
         * Sets the value of the pagamento property.
         * 
         * @param value
         *     allowed object is
         *     {@link FlussoRendicontazione23 .Rendicontazione.Pagamento }
         *     
         */
        public void setPagamento(FlussoRendicontazione23 .Rendicontazione.Pagamento value) {
            this.pagamento = value;
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
         *         &lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
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
            "codSingoloVersamentoEnte"
        })
        public static class Pagamento {

            @XmlElement(required = true)
            protected String codApplicazione;
            @XmlElement(required = true)
            protected String codVersamentoEnte;
            @XmlElement(required = true)
            protected String codSingoloVersamentoEnte;

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
             * Gets the value of the codSingoloVersamentoEnte property.
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
             * Sets the value of the codSingoloVersamentoEnte property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodSingoloVersamentoEnte(String value) {
                this.codSingoloVersamentoEnte = value;
            }

        }

    }

}
