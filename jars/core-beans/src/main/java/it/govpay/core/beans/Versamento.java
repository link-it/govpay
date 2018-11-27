
package it.govpay.core.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class Versamento {

    protected String codApplicazione;
    protected String codVersamentoEnte;
    protected String iuv;
    protected String codDominio;
    protected String codUnitaOperativa;
    protected Anagrafica debitore;
    protected BigDecimal importoTotale;
    protected Date dataScadenza;
    protected Boolean aggiornabile;
    protected String codDebito;
    protected Integer annoTributario;
    protected String bundlekey;
    protected String causale;
    protected List<String> spezzoneCausale;
    protected List<Versamento.SpezzoneCausaleStrutturata> spezzoneCausaleStrutturata;
    protected List<Versamento.SingoloVersamento> singoloVersamento;

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
     * Recupera il valore della proprietà codUnitaOperativa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodUnitaOperativa() {
        return this.codUnitaOperativa;
    }

    /**
     * Imposta il valore della proprietà codUnitaOperativa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodUnitaOperativa(String value) {
        this.codUnitaOperativa = value;
    }

    /**
     * Recupera il valore della proprietà debitore.
     * 
     * @return
     *     possible object is
     *     {@link Anagrafica }
     *     
     */
    public Anagrafica getDebitore() {
        return this.debitore;
    }

    /**
     * Imposta il valore della proprietà debitore.
     * 
     * @param value
     *     allowed object is
     *     {@link Anagrafica }
     *     
     */
    public void setDebitore(Anagrafica value) {
        this.debitore = value;
    }

    /**
     * Recupera il valore della proprietà importoTotale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public BigDecimal getImportoTotale() {
        return this.importoTotale;
    }

    /**
     * Imposta il valore della proprietà importoTotale.
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
     * Recupera il valore della proprietà dataScadenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDataScadenza() {
        return this.dataScadenza;
    }

    /**
     * Imposta il valore della proprietà dataScadenza.
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
     * Recupera il valore della proprietà aggiornabile.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAggiornabile() {
        return this.aggiornabile;
    }

    /**
     * Imposta il valore della proprietà aggiornabile.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAggiornabile(Boolean value) {
        this.aggiornabile = value;
    }

    /**
     * Recupera il valore della proprietà codDebito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodDebito() {
        return this.codDebito;
    }

    /**
     * Imposta il valore della proprietà codDebito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodDebito(String value) {
        this.codDebito = value;
    }

    /**
     * Recupera il valore della proprietà annoTributario.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAnnoTributario() {
        return this.annoTributario;
    }

    /**
     * Imposta il valore della proprietà annoTributario.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAnnoTributario(Integer value) {
        this.annoTributario = value;
    }

    /**
     * Recupera il valore della proprietà bundlekey.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBundlekey() {
        return this.bundlekey;
    }

    /**
     * Imposta il valore della proprietà bundlekey.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBundlekey(String value) {
        this.bundlekey = value;
    }

    /**
     * Recupera il valore della proprietà causale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausale() {
        return this.causale;
    }

    /**
     * Imposta il valore della proprietà causale.
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
        if (this.spezzoneCausale == null) {
            this.spezzoneCausale = new ArrayList<>();
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
     * {@link Versamento.SpezzoneCausaleStrutturata }
     * 
     * 
     */
    public List<Versamento.SpezzoneCausaleStrutturata> getSpezzoneCausaleStrutturata() {
        if (this.spezzoneCausaleStrutturata == null) {
            this.spezzoneCausaleStrutturata = new ArrayList<>();
        }
        return this.spezzoneCausaleStrutturata;
    }

    /**
     * Gets the value of the singoloVersamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the singoloVersamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSingoloVersamento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Versamento.SingoloVersamento }
     * 
     * 
     */
    public List<Versamento.SingoloVersamento> getSingoloVersamento() {
        if (this.singoloVersamento == null) {
            this.singoloVersamento = new ArrayList<>();
        }
        return this.singoloVersamento;
    }


    public static class SingoloVersamento {

        protected String codSingoloVersamentoEnte;
        protected BigDecimal importo;
        protected String note;
        protected String codTributo;
        protected Versamento.SingoloVersamento.BolloTelematico bolloTelematico;
        protected Versamento.SingoloVersamento.Tributo tributo;

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
         * Recupera il valore della proprietà importo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public BigDecimal getImporto() {
            return this.importo;
        }

        /**
         * Imposta il valore della proprietà importo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setImporto(BigDecimal value) {
            this.importo = value;
        }

        /**
         * Recupera il valore della proprietà note.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNote() {
            return this.note;
        }

        /**
         * Imposta il valore della proprietà note.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNote(String value) {
            this.note = value;
        }

        /**
         * Recupera il valore della proprietà codTributo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodTributo() {
            return this.codTributo;
        }

        /**
         * Imposta il valore della proprietà codTributo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodTributo(String value) {
            this.codTributo = value;
        }

        /**
         * Recupera il valore della proprietà bolloTelematico.
         * 
         * @return
         *     possible object is
         *     {@link Versamento.SingoloVersamento.BolloTelematico }
         *     
         */
        public Versamento.SingoloVersamento.BolloTelematico getBolloTelematico() {
            return this.bolloTelematico;
        }

        /**
         * Imposta il valore della proprietà bolloTelematico.
         * 
         * @param value
         *     allowed object is
         *     {@link Versamento.SingoloVersamento.BolloTelematico }
         *     
         */
        public void setBolloTelematico(Versamento.SingoloVersamento.BolloTelematico value) {
            this.bolloTelematico = value;
        }

        /**
         * Recupera il valore della proprietà tributo.
         * 
         * @return
         *     possible object is
         *     {@link Versamento.SingoloVersamento.Tributo }
         *     
         */
        public Versamento.SingoloVersamento.Tributo getTributo() {
            return this.tributo;
        }

        /**
         * Imposta il valore della proprietà tributo.
         * 
         * @param value
         *     allowed object is
         *     {@link Versamento.SingoloVersamento.Tributo }
         *     
         */
        public void setTributo(Versamento.SingoloVersamento.Tributo value) {
            this.tributo = value;
        }


        public static class BolloTelematico {

            protected String tipo;
            protected String hash;
            protected String provincia;

            /**
             * Recupera il valore della proprietà tipo.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTipo() {
                return this.tipo;
            }

            /**
             * Imposta il valore della proprietà tipo.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTipo(String value) {
                this.tipo = value;
            }

            /**
             * Recupera il valore della proprietà hash.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getHash() {
                return this.hash;
            }

            /**
             * Imposta il valore della proprietà hash.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setHash(String value) {
                this.hash = value;
            }

            /**
             * Recupera il valore della proprietà provincia.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getProvincia() {
                return this.provincia;
            }

            /**
             * Imposta il valore della proprietà provincia.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setProvincia(String value) {
                this.provincia = value;
            }

        }


        public static class Tributo {

            protected String ibanAccredito;
            private String ibanAppoggio;
            protected TipoContabilita tipoContabilita;
            protected String codContabilita;

            /**
             * Recupera il valore della proprietà ibanAccredito.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getIbanAccredito() {
                return this.ibanAccredito;
            }

            /**
             * Imposta il valore della proprietà ibanAccredito.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setIbanAccredito(String value) {
                this.ibanAccredito = value;
            }

            /**
             * Recupera il valore della proprietà tipoContabilita.
             * 
             * @return
             *     possible object is
             *     {@link TipoContabilita }
             *     
             */
            public TipoContabilita getTipoContabilita() {
                return this.tipoContabilita;
            }

            /**
             * Imposta il valore della proprietà tipoContabilita.
             * 
             * @param value
             *     allowed object is
             *     {@link TipoContabilita }
             *     
             */
            public void setTipoContabilita(TipoContabilita value) {
                this.tipoContabilita = value;
            }

            /**
             * Recupera il valore della proprietà codContabilita.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodContabilita() {
                return this.codContabilita;
            }

            /**
             * Imposta il valore della proprietà codContabilita.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodContabilita(String value) {
                this.codContabilita = value;
            }

			public String getIbanAppoggio() {
				return ibanAppoggio;
			}

			public void setIbanAppoggio(String ibanAppoggio) {
				this.ibanAppoggio = ibanAppoggio;
			}

        }

    }


    public static class SpezzoneCausaleStrutturata {

        protected String causale;
        protected BigDecimal importo;

        /**
         * Recupera il valore della proprietà causale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCausale() {
            return this.causale;
        }

        /**
         * Imposta il valore della proprietà causale.
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
         * Recupera il valore della proprietà importo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public BigDecimal getImporto() {
            return this.importo;
        }

        /**
         * Imposta il valore della proprietà importo.
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
