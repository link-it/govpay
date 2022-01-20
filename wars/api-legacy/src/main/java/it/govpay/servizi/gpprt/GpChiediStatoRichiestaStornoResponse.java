
package it.govpay.servizi.gpprt;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.GpResponse;
import it.govpay.servizi.commons.Pagamento;
import it.govpay.servizi.commons.StatoRevoca;


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
     * Gets the value of the storno property.
     * 
     * @return
     *     possible object is
     *     {@link GpChiediStatoRichiestaStornoResponse.Storno }
     *     
     */
    public GpChiediStatoRichiestaStornoResponse.Storno getStorno() {
        return storno;
    }

    /**
     * Sets the value of the storno property.
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
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
         * Gets the value of the ccp property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCcp() {
            return ccp;
        }

        /**
         * Sets the value of the ccp property.
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
         * Gets the value of the stato property.
         * 
         * @return
         *     possible object is
         *     {@link StatoRevoca }
         *     
         */
        public StatoRevoca getStato() {
            return stato;
        }

        /**
         * Sets the value of the stato property.
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
         * Gets the value of the descrizioneStato property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrizioneStato() {
            return descrizioneStato;
        }

        /**
         * Sets the value of the descrizioneStato property.
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
         * Gets the value of the rr property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getRr() {
            return rr;
        }

        /**
         * Sets the value of the rr property.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setRr(byte[] value) {
            this.rr = value;
        }

        /**
         * Gets the value of the er property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getEr() {
            return er;
        }

        /**
         * Sets the value of the er property.
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
            if (pagamento == null) {
                pagamento = new ArrayList<Pagamento>();
            }
            return this.pagamento;
        }

    }

}
