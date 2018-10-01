
package it.govpay.core.dao.commons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.govpay.model.Anagrafica.TIPO;
import it.govpay.model.Versamento.StatoVersamento;

public class Versamento {
	
    public StatoVersamento getStatoVersamento() {
		return this.statoVersamento;
	}

	public void setStatoVersamento(StatoVersamento statoVersamento) {
		this.statoVersamento = statoVersamento;
	}

	public String getCodApplicazione() {
		return this.codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getCodVersamentoEnte() {
		return this.codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

//	public String getIuv() {
//		return iuv;
//	}
//
//	public void setIuv(String iuv) {
//		this.iuv = iuv;
//	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getCodUnitaOperativa() {
		return this.codUnitaOperativa;
	}

	public void setCodUnitaOperativa(String codUnitaOperativa) {
		this.codUnitaOperativa = codUnitaOperativa;
	}

	public Anagrafica getDebitore() {
		return this.debitore;
	}

	public void setDebitore(Anagrafica debitore) {
		this.debitore = debitore;
	}

	public BigDecimal getImportoTotale() {
		return this.importoTotale;
	}

	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}

	public Date getDataScadenza() {
		return this.dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public Boolean getAggiornabile() {
		return this.aggiornabile;
	}

	public Boolean isAggiornabile() {
		return this.aggiornabile;
	}

	public void setAggiornabile(Boolean aggiornabile) {
		this.aggiornabile = aggiornabile;
	}

	public String getCodDebito() {
		return this.codDebito;
	}

	public void setCodDebito(String codDebito) {
		this.codDebito = codDebito;
	}

	public Integer getAnnoTributario() {
		return this.annoTributario;
	}

	public void setAnnoTributario(Integer annoTributario) {
		this.annoTributario = annoTributario;
	}

	public String getBundlekey() {
		return this.bundlekey;
	}

	public void setBundlekey(String bundlekey) {
		this.bundlekey = bundlekey;
	}

	public String getCausale() {
		return this.causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public List<String> getSpezzoneCausale() {
		return this.spezzoneCausale;
	}

	public void setSpezzoneCausale(List<String> spezzoneCausale) {
		this.spezzoneCausale = spezzoneCausale;
	}

	public List<Versamento.SpezzoneCausaleStrutturata> getSpezzoneCausaleStrutturata() {
		return this.spezzoneCausaleStrutturata;
	}

	public void setSpezzoneCausaleStrutturata(List<Versamento.SpezzoneCausaleStrutturata> spezzoneCausaleStrutturata) {
		this.spezzoneCausaleStrutturata = spezzoneCausaleStrutturata;
	}

	public List<Versamento.SingoloVersamento> getSingoloVersamento() {
		if(this.singoloVersamento == null) this.singoloVersamento = new ArrayList<>();
		return this.singoloVersamento;
	}

	public void setSingoloVersamento(List<Versamento.SingoloVersamento> singoloVersamento) {
		this.singoloVersamento = singoloVersamento;
	}

	public Date getDataValidita() {
		return this.dataValidita;
	}

	public void setDataValidita(Date dataValidita) {
		this.dataValidita = dataValidita;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodLotto() {
		return this.codLotto;
	}

	public void setCodLotto(String codLotto) {
		this.codLotto = codLotto;
	}

	public String getCodVersamentoLotto() {
		return this.codVersamentoLotto;
	}

	public void setCodVersamentoLotto(String codVersamentoLotto) {
		this.codVersamentoLotto = codVersamentoLotto;
	}

	public String getTassonomiaAvviso() {
		return this.tassonomiaAvviso;
	}

	public void setTassonomiaAvviso(String tassonomiaAvviso) {
		this.tassonomiaAvviso = tassonomiaAvviso;
	}

	public String getTassonomia() {
		return this.tassonomia;
	}

	public void setTassonomia(String tassonomia) {
		this.tassonomia = tassonomia;
	}

	public TIPO getTipo() {
		return this.tipo;
	}

	public void setTipo(TIPO tipo) {
		this.tipo = tipo;
	}

	public Date getDataCaricamento() {
		return this.dataCaricamento;
	}

	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}
	
	public String getDatiAllegati() {
		return this.datiAllegati;
	}

	public void setDatiAllegati(String datiAllegati) {
		this.datiAllegati = datiAllegati;
	}

	public Boolean getIncasso() {
		return this.incasso;
	}

	public void setIncasso(Boolean incasso) {
		this.incasso = incasso;
	}

	public String getAnomalie() {
		return this.anomalie;
	}

	public void setAnomalie(String anomalie) {
		this.anomalie = anomalie;
	}

	public String getNumeroAvviso() {
		return this.numeroAvviso;
	}

	public void setNumeroAvviso(String numeroAvviso) {
		this.numeroAvviso = numeroAvviso;
	}

	private StatoVersamento statoVersamento;
    private String codApplicazione;
	private String codVersamentoEnte;
//    private String iuv;
    private String nome;
    private String codDominio;
    private String codLotto;
    private String codVersamentoLotto;
    private String tassonomia;
    private String tassonomiaAvviso;
    
    private String codUnitaOperativa;
    private Anagrafica debitore;
    private BigDecimal importoTotale;
    private Date dataScadenza;
    private Date dataValidita;
    private Date dataCaricamento;
    private Boolean aggiornabile;
    private String codDebito;
    private Integer annoTributario;
    private String bundlekey;
    private String causale;
    private String datiAllegati;
	private Boolean incasso;
	private String anomalie;
	private String numeroAvviso;
    
    private TIPO tipo;
    private List<String> spezzoneCausale;
    private List<Versamento.SpezzoneCausaleStrutturata> spezzoneCausaleStrutturata;
    private List<Versamento.SingoloVersamento> singoloVersamento;

    public static class SingoloVersamento {

        private String codSingoloVersamentoEnte;
        public String getCodSingoloVersamentoEnte() {
			return this.codSingoloVersamentoEnte;
		}

		public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
			this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
		}

		public BigDecimal getImporto() {
			return this.importo;
		}

		public void setImporto(BigDecimal importo) {
			this.importo = importo;
		}

		public String getDatiAllegati() {
			return this.datiAllegati;
		}

		public void setDatiAllegati(String datiAllegati) {
			this.datiAllegati = datiAllegati;
		}

		public String getCodTributo() {
			return this.codTributo;
		}

		public void setCodTributo(String codTributo) {
			this.codTributo = codTributo;
		}

		public Versamento.SingoloVersamento.BolloTelematico getBolloTelematico() {
			return this.bolloTelematico;
		}

		public void setBolloTelematico(Versamento.SingoloVersamento.BolloTelematico bolloTelematico) {
			this.bolloTelematico = bolloTelematico;
		}

		public Versamento.SingoloVersamento.Tributo getTributo() {
			return this.tributo;
		}

		public void setTributo(Versamento.SingoloVersamento.Tributo tributo) {
			this.tributo = tributo;
		}

		public String getDescrizione() {
			return this.descrizione;
		}

		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}

		private BigDecimal importo;
		private String datiAllegati;
        private String descrizione;
        private String codTributo;
        private Versamento.SingoloVersamento.BolloTelematico bolloTelematico;
        private Versamento.SingoloVersamento.Tributo tributo;

        public static class BolloTelematico {

            private String tipo;
            private String hash;
            public String getTipo() {
				return this.tipo;
			}
			public void setTipo(String tipo) {
				this.tipo = tipo;
			}
			public String getHash() {
				return this.hash;
			}
			public void setHash(String hash) {
				this.hash = hash;
			}
			public String getProvincia() {
				return this.provincia;
			}
			public void setProvincia(String provincia) {
				this.provincia = provincia;
			}
			private String provincia;

        }

        public static class Tributo {

        	public enum TipoContabilita {

        	    CAPITOLO,
        	    SPECIALE,
        	    SIOPE,
        	    ALTRO;

        	    public String value() {
        	        return this.name();
        	    }

        	    public static TipoContabilita fromValue(String v) {
        	        return valueOf(v);
        	    }

        	}

            private String ibanAccredito;
            private String ibanAppoggio;
            private TipoContabilita tipoContabilita;
            private String codContabilita;
			public String getIbanAccredito() {
				return this.ibanAccredito;
			}
			public void setIbanAccredito(String ibanAccredito) {
				this.ibanAccredito = ibanAccredito;
			}
			public TipoContabilita getTipoContabilita() {
				return this.tipoContabilita;
			}
			public void setTipoContabilita(TipoContabilita tipoContabilita) {
				this.tipoContabilita = tipoContabilita;
			}
			public String getCodContabilita() {
				return this.codContabilita;
			}
			public void setCodContabilita(String codContabilita) {
				this.codContabilita = codContabilita;
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

        private String causale;
        public String getCausale() {
			return this.causale;
		}
		public void setCausale(String causale) {
			this.causale = causale;
		}
		public BigDecimal getImporto() {
			return this.importo;
		}
		public void setImporto(BigDecimal importo) {
			this.importo = importo;
		}
		private BigDecimal importo;

    }

}
