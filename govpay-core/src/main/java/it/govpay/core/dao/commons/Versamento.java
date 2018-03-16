
package it.govpay.core.dao.commons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.govpay.model.Anagrafica.TIPO;
import it.govpay.model.Versamento.StatoVersamento;

public class Versamento {
	
    public StatoVersamento getStatoVersamento() {
		return statoVersamento;
	}

	public void setStatoVersamento(StatoVersamento statoVersamento) {
		this.statoVersamento = statoVersamento;
	}

	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
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
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getCodUnitaOperativa() {
		return codUnitaOperativa;
	}

	public void setCodUnitaOperativa(String codUnitaOperativa) {
		this.codUnitaOperativa = codUnitaOperativa;
	}

	public Anagrafica getDebitore() {
		return debitore;
	}

	public void setDebitore(Anagrafica debitore) {
		this.debitore = debitore;
	}

	public BigDecimal getImportoTotale() {
		return importoTotale;
	}

	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public Boolean getAggiornabile() {
		return aggiornabile;
	}

	public Boolean isAggiornabile() {
		return aggiornabile;
	}

	public void setAggiornabile(Boolean aggiornabile) {
		this.aggiornabile = aggiornabile;
	}

	public String getCodDebito() {
		return codDebito;
	}

	public void setCodDebito(String codDebito) {
		this.codDebito = codDebito;
	}

	public Integer getAnnoTributario() {
		return annoTributario;
	}

	public void setAnnoTributario(Integer annoTributario) {
		this.annoTributario = annoTributario;
	}

	public String getBundlekey() {
		return bundlekey;
	}

	public void setBundlekey(String bundlekey) {
		this.bundlekey = bundlekey;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public List<String> getSpezzoneCausale() {
		return spezzoneCausale;
	}

	public void setSpezzoneCausale(List<String> spezzoneCausale) {
		this.spezzoneCausale = spezzoneCausale;
	}

	public List<Versamento.SpezzoneCausaleStrutturata> getSpezzoneCausaleStrutturata() {
		return spezzoneCausaleStrutturata;
	}

	public void setSpezzoneCausaleStrutturata(List<Versamento.SpezzoneCausaleStrutturata> spezzoneCausaleStrutturata) {
		this.spezzoneCausaleStrutturata = spezzoneCausaleStrutturata;
	}

	public List<Versamento.SingoloVersamento> getSingoloVersamento() {
		if(singoloVersamento == null) singoloVersamento = new ArrayList<Versamento.SingoloVersamento>();
		return singoloVersamento;
	}

	public void setSingoloVersamento(List<Versamento.SingoloVersamento> singoloVersamento) {
		this.singoloVersamento = singoloVersamento;
	}

	public Date getDataValidita() {
		return dataValidita;
	}

	public void setDataValidita(Date dataValidita) {
		this.dataValidita = dataValidita;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodLotto() {
		return codLotto;
	}

	public void setCodLotto(String codLotto) {
		this.codLotto = codLotto;
	}

	public String getCodVersamentoLotto() {
		return codVersamentoLotto;
	}

	public void setCodVersamentoLotto(String codVersamentoLotto) {
		this.codVersamentoLotto = codVersamentoLotto;
	}

	public String getTassonomiaAvviso() {
		return tassonomiaAvviso;
	}

	public void setTassonomiaAvviso(String tassonomiaAvviso) {
		this.tassonomiaAvviso = tassonomiaAvviso;
	}

	public String getTassonomia() {
		return tassonomia;
	}

	public void setTassonomia(String tassonomia) {
		this.tassonomia = tassonomia;
	}

	public TIPO getTipo() {
		return tipo;
	}

	public void setTipo(TIPO tipo) {
		this.tipo = tipo;
	}

	public Date getDataCaricamento() {
		return dataCaricamento;
	}

	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}
	
	public String getDatiAllegati() {
		return datiAllegati;
	}

	public void setDatiAllegati(String datiAllegati) {
		this.datiAllegati = datiAllegati;
	}

	public Boolean getIncasso() {
		return incasso;
	}

	public void setIncasso(Boolean incasso) {
		this.incasso = incasso;
	}

	public String getAnomalie() {
		return anomalie;
	}

	public void setAnomalie(String anomalie) {
		this.anomalie = anomalie;
	}

	public String getNumeroAvviso() {
		return numeroAvviso;
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
			return codSingoloVersamentoEnte;
		}

		public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
			this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
		}

		public BigDecimal getImporto() {
			return importo;
		}

		public void setImporto(BigDecimal importo) {
			this.importo = importo;
		}

		public String getDatiAllegati() {
			return datiAllegati;
		}

		public void setDatiAllegati(String datiAllegati) {
			this.datiAllegati = datiAllegati;
		}

		public String getCodTributo() {
			return codTributo;
		}

		public void setCodTributo(String codTributo) {
			this.codTributo = codTributo;
		}

		public Versamento.SingoloVersamento.BolloTelematico getBolloTelematico() {
			return bolloTelematico;
		}

		public void setBolloTelematico(Versamento.SingoloVersamento.BolloTelematico bolloTelematico) {
			this.bolloTelematico = bolloTelematico;
		}

		public Versamento.SingoloVersamento.Tributo getTributo() {
			return tributo;
		}

		public void setTributo(Versamento.SingoloVersamento.Tributo tributo) {
			this.tributo = tributo;
		}

		public String getDescrizione() {
			return descrizione;
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
				return tipo;
			}
			public void setTipo(String tipo) {
				this.tipo = tipo;
			}
			public String getHash() {
				return hash;
			}
			public void setHash(String hash) {
				this.hash = hash;
			}
			public String getProvincia() {
				return provincia;
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
        	        return name();
        	    }

        	    public static TipoContabilita fromValue(String v) {
        	        return valueOf(v);
        	    }

        	}

            private String ibanAccredito;
            private TipoContabilita tipoContabilita;
            private String codContabilita;
			public String getIbanAccredito() {
				return ibanAccredito;
			}
			public void setIbanAccredito(String ibanAccredito) {
				this.ibanAccredito = ibanAccredito;
			}
			public TipoContabilita getTipoContabilita() {
				return tipoContabilita;
			}
			public void setTipoContabilita(TipoContabilita tipoContabilita) {
				this.tipoContabilita = tipoContabilita;
			}
			public String getCodContabilita() {
				return codContabilita;
			}
			public void setCodContabilita(String codContabilita) {
				this.codContabilita = codContabilita;
			}

        }

    }

    public static class SpezzoneCausaleStrutturata {

        private String causale;
        public String getCausale() {
			return causale;
		}
		public void setCausale(String causale) {
			this.causale = causale;
		}
		public BigDecimal getImporto() {
			return importo;
		}
		public void setImporto(BigDecimal importo) {
			this.importo = importo;
		}
		private BigDecimal importo;

    }

}
