
package it.govpay.servizi.v2_5.gpprt;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import it.govpay.servizi.commons.ModelloPagamento;
import it.govpay.servizi.commons.TipoVersamento;
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
        if (this.psp == null) {
            this.psp = new ArrayList<>();
        }
        return this.psp;
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
         * Recupera il valore della proprietà codPsp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodPsp() {
            return this.codPsp;
        }

        /**
         * Imposta il valore della proprietà codPsp.
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
         * Recupera il valore della proprietà ragioneSociale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRagioneSociale() {
            return this.ragioneSociale;
        }

        /**
         * Imposta il valore della proprietà ragioneSociale.
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
         * Recupera il valore della proprietà urlInfo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUrlInfo() {
            return this.urlInfo;
        }

        /**
         * Imposta il valore della proprietà urlInfo.
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
         * Recupera il valore della proprietà storno.
         * 
         */
        public boolean isStorno() {
            return this.storno;
        }

        /**
         * Imposta il valore della proprietà storno.
         * 
         */
        public void setStorno(boolean value) {
            this.storno = value;
        }

        /**
         * Recupera il valore della proprietà bollo.
         * 
         */
        public boolean isBollo() {
            return this.bollo;
        }

        /**
         * Imposta il valore della proprietà bollo.
         * 
         */
        public void setBollo(boolean value) {
            this.bollo = value;
        }

        /**
         * Recupera il valore della proprietà logo.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getLogo() {
            return this.logo;
        }

        /**
         * Imposta il valore della proprietà logo.
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
            if (this.canale == null) {
                this.canale = new ArrayList<>();
            }
            return this.canale;
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
             * Recupera il valore della proprietà codCanale.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodCanale() {
                return this.codCanale;
            }

            /**
             * Imposta il valore della proprietà codCanale.
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
             * Recupera il valore della proprietà tipoVersamento.
             * 
             * @return
             *     possible object is
             *     {@link TipoVersamento }
             *     
             */
            public TipoVersamento getTipoVersamento() {
                return this.tipoVersamento;
            }

            /**
             * Imposta il valore della proprietà tipoVersamento.
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
             * Recupera il valore della proprietà modelloPagamento.
             * 
             * @return
             *     possible object is
             *     {@link ModelloPagamento }
             *     
             */
            public ModelloPagamento getModelloPagamento() {
                return this.modelloPagamento;
            }

            /**
             * Imposta il valore della proprietà modelloPagamento.
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
             * Recupera il valore della proprietà disponibilita.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDisponibilita() {
                return this.disponibilita;
            }

            /**
             * Imposta il valore della proprietà disponibilita.
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
             * Recupera il valore della proprietà descrizione.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDescrizione() {
                return this.descrizione;
            }

            /**
             * Imposta il valore della proprietà descrizione.
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
             * Recupera il valore della proprietà condizioni.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCondizioni() {
                return this.condizioni;
            }

            /**
             * Imposta il valore della proprietà condizioni.
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
             * Recupera il valore della proprietà urlInfo.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getUrlInfo() {
                return this.urlInfo;
            }

            /**
             * Imposta il valore della proprietà urlInfo.
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
             * Recupera il valore della proprietà logoServizio.
             * 
             * @return
             *     possible object is
             *     byte[]
             */
            public byte[] getLogoServizio() {
                return this.logoServizio;
            }

            /**
             * Imposta il valore della proprietà logoServizio.
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
