
package it.govpay.servizi.commons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
        if (this.pagamento == null) {
            this.pagamento = new ArrayList<>();
        }
        return this.pagamento;
    }


    public static class Pagamento {

        protected String codApplicazione;
        protected String codSingoloVersamentoEnte;
        protected String codDominio;
        protected String iuv;
        protected BigDecimal importoRendicontato;
        protected String iur;
        protected Date dataRendicontazione;
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
         * Recupera il valore della proprietà dataRendicontazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Date getDataRendicontazione() {
            return this.dataRendicontazione;
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
            return this.esitoRendicontazione;
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
