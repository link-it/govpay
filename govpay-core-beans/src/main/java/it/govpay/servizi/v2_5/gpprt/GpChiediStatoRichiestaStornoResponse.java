
package it.govpay.servizi.v2_5.gpprt;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.Pagamento;
import it.govpay.servizi.commons.StatoRevoca;
import it.govpay.servizi.v2_3.commons.GpResponse;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.govpay.it/servizi/v2_3/commons/}gpResponse"&gt;
 *       &lt;sequence minOccurs="0"&gt;
 *         &lt;element name="storno"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="ccp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="stato" type="{http://www.govpay.it/servizi/commons/}statoRevoca"/&gt;
 *                   &lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="rr" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *                   &lt;element name="er" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *                   &lt;element name="pagamento" type="{http://www.govpay.it/servizi/commons/}pagamento" maxOccurs="5" minOccurs="0"/&gt;
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
    "storno"
})
@XmlRootElement(name = "gpChiediStatoRichiestaStornoResponse")
public class GpChiediStatoRichiestaStornoResponse
    extends GpResponse
{

    protected GpChiediStatoRichiestaStornoResponse.Storno storno;

    /**
     * Recupera il valore della proprietà storno.
     * 
     * @return
     *     possible object is
     *     {@link GpChiediStatoRichiestaStornoResponse.Storno }
     *     
     */
    public GpChiediStatoRichiestaStornoResponse.Storno getStorno() {
        return this.storno;
    }

    /**
     * Imposta il valore della proprietà storno.
     * 
     * @param value
     *     allowed object is
     *     {@link GpChiediStatoRichiestaStornoResponse.Storno }
     *     
     */
    public void setStorno(GpChiediStatoRichiestaStornoResponse.Storno value) {
        this.storno = value;
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
     *         &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="ccp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="stato" type="{http://www.govpay.it/servizi/commons/}statoRevoca"/&gt;
     *         &lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="rr" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
     *         &lt;element name="er" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
     *         &lt;element name="pagamento" type="{http://www.govpay.it/servizi/commons/}pagamento" maxOccurs="5" minOccurs="0"/&gt;
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
        "codDominio",
        "iuv",
        "ccp",
        "stato",
        "descrizioneStato",
        "rr",
        "er",
        "pagamento"
    })
    public static class Storno {

        @XmlElement(required = true)
        protected String codDominio;
        @XmlElement(required = true)
        protected String iuv;
        @XmlElement(required = true)
        protected String ccp;
        @XmlElement(required = true)
        @XmlSchemaType(name = "string")
        protected StatoRevoca stato;
        protected String descrizioneStato;
        @XmlElement(required = true)
        protected byte[] rr;
        protected byte[] er;
        protected List<Pagamento> pagamento;

        /**
         * Recupera il valore della proprietà codDominio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodDominio() {
            return this.codDominio;
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
         * Recupera il valore della proprietà ccp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCcp() {
            return this.ccp;
        }

        /**
         * Imposta il valore della proprietà ccp.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCcp(String value) {
            this.ccp = value;
        }

        /**
         * Recupera il valore della proprietà stato.
         * 
         * @return
         *     possible object is
         *     {@link StatoRevoca }
         *     
         */
        public StatoRevoca getStato() {
            return this.stato;
        }

        /**
         * Imposta il valore della proprietà stato.
         * 
         * @param value
         *     allowed object is
         *     {@link StatoRevoca }
         *     
         */
        public void setStato(StatoRevoca value) {
            this.stato = value;
        }

        /**
         * Recupera il valore della proprietà descrizioneStato.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrizioneStato() {
            return this.descrizioneStato;
        }

        /**
         * Imposta il valore della proprietà descrizioneStato.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrizioneStato(String value) {
            this.descrizioneStato = value;
        }

        /**
         * Recupera il valore della proprietà rr.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getRr() {
            return this.rr;
        }

        /**
         * Imposta il valore della proprietà rr.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setRr(byte[] value) {
            this.rr = value;
        }

        /**
         * Recupera il valore della proprietà er.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getEr() {
            return this.er;
        }

        /**
         * Imposta il valore della proprietà er.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setEr(byte[] value) {
            this.er = value;
        }

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
         * {@link Pagamento }
         * 
         * 
         */
        public List<Pagamento> getPagamento() {
            if (this.pagamento == null) {
                this.pagamento = new ArrayList<>();
            }
            return this.pagamento;
        }

    }

}
