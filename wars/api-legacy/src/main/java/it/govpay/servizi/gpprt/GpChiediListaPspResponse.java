
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
import it.govpay.servizi.commons.ModelloPagamento;
import it.govpay.servizi.commons.TipoVersamento;


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
 *         &lt;element name="psp" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="codPsp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                   &lt;element name="ragioneSociale" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="urlInfo" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *                   &lt;element name="storno" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                   &lt;element name="bollo" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                   &lt;element name="logo" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *                   &lt;element name="canale" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="codCanale" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *                             &lt;element name="tipoVersamento" type="{http://www.govpay.it/servizi/commons/}tipoVersamento"/&gt;
 *                             &lt;element name="modelloPagamento" type="{http://www.govpay.it/servizi/commons/}modelloPagamento"/&gt;
 *                             &lt;element name="disponibilita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="descrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="condizioni" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="urlInfo" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *                             &lt;element name="logoServizio" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
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
@XmlType(name = "", propOrder = {
    "psp"
})
@XmlRootElement(name = "gpChiediListaPspResponse")
public class GpChiediListaPspResponse
    extends GpResponse
{

    protected List<GpChiediListaPspResponse.Psp> psp;

    /**
     * Gets the value of the psp property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the psp property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPsp().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GpChiediListaPspResponse.Psp }
     * 
     * 
     */
    public List<GpChiediListaPspResponse.Psp> getPsp() {
        if (psp == null) {
            psp = new ArrayList<GpChiediListaPspResponse.Psp>();
        }
        return this.psp;
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
     *         &lt;element name="codPsp" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *         &lt;element name="ragioneSociale" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="urlInfo" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
     *         &lt;element name="storno" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *         &lt;element name="bollo" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *         &lt;element name="logo" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
     *         &lt;element name="canale" maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="codCanale" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
     *                   &lt;element name="tipoVersamento" type="{http://www.govpay.it/servizi/commons/}tipoVersamento"/&gt;
     *                   &lt;element name="modelloPagamento" type="{http://www.govpay.it/servizi/commons/}modelloPagamento"/&gt;
     *                   &lt;element name="disponibilita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="descrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="condizioni" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="urlInfo" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
     *                   &lt;element name="logoServizio" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
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
        "codPsp",
        "ragioneSociale",
        "urlInfo",
        "storno",
        "bollo",
        "logo",
        "canale"
    })
    public static class Psp {

        @XmlElement(required = true)
        protected String codPsp;
        @XmlElement(required = true)
        protected String ragioneSociale;
        @XmlSchemaType(name = "anyURI")
        protected String urlInfo;
        protected boolean storno;
        protected boolean bollo;
        @XmlElement(required = true)
        protected byte[] logo;
        protected List<GpChiediListaPspResponse.Psp.Canale> canale;

        /**
         * Gets the value of the codPsp property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodPsp() {
            return codPsp;
        }

        /**
         * Sets the value of the codPsp property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodPsp(String value) {
            this.codPsp = value;
        }

        /**
         * Gets the value of the ragioneSociale property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRagioneSociale() {
            return ragioneSociale;
        }

        /**
         * Sets the value of the ragioneSociale property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRagioneSociale(String value) {
            this.ragioneSociale = value;
        }

        /**
         * Gets the value of the urlInfo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUrlInfo() {
            return urlInfo;
        }

        /**
         * Sets the value of the urlInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUrlInfo(String value) {
            this.urlInfo = value;
        }

        /**
         * Gets the value of the storno property.
         * 
         */
        public boolean isStorno() {
            return storno;
        }

        /**
         * Sets the value of the storno property.
         * 
         */
        public void setStorno(boolean value) {
            this.storno = value;
        }

        /**
         * Gets the value of the bollo property.
         * 
         */
        public boolean isBollo() {
            return bollo;
        }

        /**
         * Sets the value of the bollo property.
         * 
         */
        public void setBollo(boolean value) {
            this.bollo = value;
        }

        /**
         * Gets the value of the logo property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getLogo() {
            return logo;
        }

        /**
         * Sets the value of the logo property.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setLogo(byte[] value) {
            this.logo = value;
        }

        /**
         * Gets the value of the canale property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the canale property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCanale().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GpChiediListaPspResponse.Psp.Canale }
         * 
         * 
         */
        public List<GpChiediListaPspResponse.Psp.Canale> getCanale() {
            if (canale == null) {
                canale = new ArrayList<GpChiediListaPspResponse.Psp.Canale>();
            }
            return this.canale;
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
         *         &lt;element name="codCanale" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
         *         &lt;element name="tipoVersamento" type="{http://www.govpay.it/servizi/commons/}tipoVersamento"/&gt;
         *         &lt;element name="modelloPagamento" type="{http://www.govpay.it/servizi/commons/}modelloPagamento"/&gt;
         *         &lt;element name="disponibilita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="descrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="condizioni" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="urlInfo" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
         *         &lt;element name="logoServizio" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
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
            "codCanale",
            "tipoVersamento",
            "modelloPagamento",
            "disponibilita",
            "descrizione",
            "condizioni",
            "urlInfo",
            "logoServizio"
        })
        public static class Canale {

            @XmlElement(required = true)
            protected String codCanale;
            @XmlElement(required = true)
            @XmlSchemaType(name = "string")
            protected TipoVersamento tipoVersamento;
            @XmlElement(required = true)
            @XmlSchemaType(name = "string")
            protected ModelloPagamento modelloPagamento;
            protected String disponibilita;
            protected String descrizione;
            protected String condizioni;
            @XmlSchemaType(name = "anyURI")
            protected String urlInfo;
            @XmlElement(required = true)
            protected byte[] logoServizio;

            /**
             * Gets the value of the codCanale property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodCanale() {
                return codCanale;
            }

            /**
             * Sets the value of the codCanale property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodCanale(String value) {
                this.codCanale = value;
            }

            /**
             * Gets the value of the tipoVersamento property.
             * 
             * @return
             *     possible object is
             *     {@link TipoVersamento }
             *     
             */
            public TipoVersamento getTipoVersamento() {
                return tipoVersamento;
            }

            /**
             * Sets the value of the tipoVersamento property.
             * 
             * @param value
             *     allowed object is
             *     {@link TipoVersamento }
             *     
             */
            public void setTipoVersamento(TipoVersamento value) {
                this.tipoVersamento = value;
            }

            /**
             * Gets the value of the modelloPagamento property.
             * 
             * @return
             *     possible object is
             *     {@link ModelloPagamento }
             *     
             */
            public ModelloPagamento getModelloPagamento() {
                return modelloPagamento;
            }

            /**
             * Sets the value of the modelloPagamento property.
             * 
             * @param value
             *     allowed object is
             *     {@link ModelloPagamento }
             *     
             */
            public void setModelloPagamento(ModelloPagamento value) {
                this.modelloPagamento = value;
            }

            /**
             * Gets the value of the disponibilita property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDisponibilita() {
                return disponibilita;
            }

            /**
             * Sets the value of the disponibilita property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDisponibilita(String value) {
                this.disponibilita = value;
            }

            /**
             * Gets the value of the descrizione property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDescrizione() {
                return descrizione;
            }

            /**
             * Sets the value of the descrizione property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDescrizione(String value) {
                this.descrizione = value;
            }

            /**
             * Gets the value of the condizioni property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCondizioni() {
                return condizioni;
            }

            /**
             * Sets the value of the condizioni property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCondizioni(String value) {
                this.condizioni = value;
            }

            /**
             * Gets the value of the urlInfo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getUrlInfo() {
                return urlInfo;
            }

            /**
             * Sets the value of the urlInfo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setUrlInfo(String value) {
                this.urlInfo = value;
            }

            /**
             * Gets the value of the logoServizio property.
             * 
             * @return
             *     possible object is
             *     byte[]
             */
            public byte[] getLogoServizio() {
                return logoServizio;
            }

            /**
             * Sets the value of the logoServizio property.
             * 
             * @param value
             *     allowed object is
             *     byte[]
             */
            public void setLogoServizio(byte[] value) {
                this.logoServizio = value;
            }

        }

    }

}
