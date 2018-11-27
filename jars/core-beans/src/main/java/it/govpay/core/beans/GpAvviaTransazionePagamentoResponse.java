
package it.govpay.core.beans;

import java.util.ArrayList;
import java.util.List;
public class GpAvviaTransazionePagamentoResponse
    extends GpResponse
{

    protected String urlRedirect;
    protected String pspSessionId;
    protected List<GpAvviaTransazionePagamentoResponse.RifTransazione> rifTransazione;

    /**
     * Recupera il valore della proprietà urlRedirect.
     * 
     * @return
     *     possible object is	
     *     {@link String }
     *     
     */
    public String getUrlRedirect() {
        return this.urlRedirect;
    }

    /**
     * Imposta il valore della proprietà urlRedirect.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlRedirect(String value) {
        this.urlRedirect = value;
    }

    /**
     * Recupera il valore della proprietà pspSessionId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPspSessionId() {
        return this.pspSessionId;
    }

    /**
     * Imposta il valore della proprietà pspSessionId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPspSessionId(String value) {
        this.pspSessionId = value;
    }

    /**
     * Gets the value of the rifTransazione property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rifTransazione property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRifTransazione().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GpAvviaTransazionePagamentoResponse.RifTransazione }
     * 
     * 
     */
    public List<GpAvviaTransazionePagamentoResponse.RifTransazione> getRifTransazione() {
        if (this.rifTransazione == null) {
            this.rifTransazione = new ArrayList<>();
        }
        return this.rifTransazione;
    }


    public static class RifTransazione {

        protected String codApplicazione;
        protected String codVersamentoEnte;
        protected String codDominio;
        protected String iuv;
        protected String ccp;

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

    }

}
