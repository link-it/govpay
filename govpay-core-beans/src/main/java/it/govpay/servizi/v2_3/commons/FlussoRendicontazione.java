
package it.govpay.servizi.v2_3.commons;

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
import it.govpay.servizi.commons.Anomalia;
import it.govpay.servizi.commons.EsitoRendicontazione;
import it.govpay.servizi.commons.StatoRendicontazione;
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
 *     &lt;extension base="{http://www.govpay.it/servizi/v2_3/commons/}estremiFlussoRendicontazione"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="rendicontazione" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="iur" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="importoRendicontato" type="{http://www.govpay.it/servizi/v2_3/commons/}importo_rnd"/&gt;
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
@XmlType(name = "flussoRendicontazione", propOrder = {
    "rendicontazione"
})
public class FlussoRendicontazione
    extends EstremiFlussoRendicontazione
{

    protected List<FlussoRendicontazione.Rendicontazione> rendicontazione;

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
     * {@link FlussoRendicontazione.Rendicontazione }
     * 
     * 
     */
    public List<FlussoRendicontazione.Rendicontazione> getRendicontazione() {
        if (this.rendicontazione == null) {
            this.rendicontazione = new ArrayList<>();
        }
        return this.rendicontazione;
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
     *         &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="iur" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="importoRendicontato" type="{http://www.govpay.it/servizi/v2_3/commons/}importo_rnd"/&gt;
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
        protected FlussoRendicontazione.Rendicontazione.Pagamento pagamento;

        /**
         * Recupera il valore della proprietà iuv.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIuv() {
            return this.iuv;
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
         * Recupera il valore della proprietà iur.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIur() {
            return this.iur;
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
         * Recupera il valore della proprietà importoRendicontato.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public BigDecimal getImportoRendicontato() {
            return this.importoRendicontato;
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
         * Recupera il valore della proprietà esito.
         * 
         * @return
         *     possible object is
         *     {@link EsitoRendicontazione }
         *     
         */
        public EsitoRendicontazione getEsito() {
            return this.esito;
        }

        /**
         * Imposta il valore della proprietà esito.
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
         * Recupera il valore della proprietà data.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Date getData() {
            return this.data;
        }

        /**
         * Imposta il valore della proprietà data.
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
         * Recupera il valore della proprietà stato.
         * 
         * @return
         *     possible object is
         *     {@link StatoRendicontazione }
         *     
         */
        public StatoRendicontazione getStato() {
            return this.stato;
        }

        /**
         * Imposta il valore della proprietà stato.
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
            if (this.anomalia == null) {
                this.anomalia = new ArrayList<>();
            }
            return this.anomalia;
        }

        /**
         * Recupera il valore della proprietà pagamento.
         * 
         * @return
         *     possible object is
         *     {@link FlussoRendicontazione.Rendicontazione.Pagamento }
         *     
         */
        public FlussoRendicontazione.Rendicontazione.Pagamento getPagamento() {
            return this.pagamento;
        }

        /**
         * Imposta il valore della proprietà pagamento.
         * 
         * @param value
         *     allowed object is
         *     {@link FlussoRendicontazione.Rendicontazione.Pagamento }
         *     
         */
        public void setPagamento(FlussoRendicontazione.Rendicontazione.Pagamento value) {
            this.pagamento = value;
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
             * Recupera il valore della proprietà codApplicazione.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodApplicazione() {
                return this.codApplicazione;
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
             * Recupera il valore della proprietà codVersamentoEnte.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodVersamentoEnte() {
                return this.codVersamentoEnte;
            }

            /**
             * Imposta il valore della proprietà codVersamentoEnte.
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
             * Recupera il valore della proprietà codSingoloVersamentoEnte.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodSingoloVersamentoEnte() {
                return this.codSingoloVersamentoEnte;
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

        }

    }

}
