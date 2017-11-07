package it.govpay.bd.model;

import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;

public class OperazioneIncasso extends Operazione{

	public OperazioneIncasso() {
	}

	public OperazioneIncasso(Operazione operazione){
		this.setCodVersamentoEnte(operazione.getCodVersamentoEnte());
		this.setDatiRichiesta(operazione.getDatiRichiesta());
		this.setDatiRisposta(operazione.getDatiRisposta());
		this.setDettaglioEsito(operazione.getDettaglioEsito());
		this.setId(operazione.getId());
		this.setIdApplicazione(operazione.getIdApplicazione());
		this.setIdOperazione(operazione.getIdOperazione());
		this.setIdTracciato(operazione.getIdTracciato());
		this.setLineaElaborazione(operazione.getLineaElaborazione());
		this.setStato(operazione.getStato());
		this.setTipoOperazione(operazione.getTipoOperazione());
		this.setCodDominio(operazione.getCodDominio());
		this.setTrn(operazione.getTrn());
	}

	// richiesta
	private String dispositivo;
	private String causale;
	private Double importo;
	private Date dataContabile;
	private Date dataValuta;

	// risposta
	private List<SingoloIncasso> listaSingoloIncasso;

	private transient Dominio dominio;

	public Dominio getDominio(BasicBD bd) throws ServiceException, NotFoundException {
		if(dominio == null) {
			dominio = AnagraficaManager.getDominio(bd, this.getCodDominio());
		} 
		return dominio;
	}

	public String getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public Double getImporto() {
		return importo;
	}

	public void setImporto(Double importo) {
		this.importo = importo;
	}

	public Date getDataContabile() {
		return dataContabile;
	}

	public void setDataContabile(Date dataContabile) {
		this.dataContabile = dataContabile;
	}

	public Date getDataValuta() {
		return dataValuta;
	}

	public void setDataValuta(Date dataValuta) {
		this.dataValuta = dataValuta;
	}

	public List<SingoloIncasso> getListaSingoloIncasso() {
		return listaSingoloIncasso;
	}

	public void setListaSingoloIncasso(List<SingoloIncasso> listaSingoloIncasso) {
		this.listaSingoloIncasso = listaSingoloIncasso;
	}

	public class SingoloIncasso {
		private String iuv;
		private String iur;
		private Date dataPagamento;
		private String codVersamentoEnte;
		private String codSingoloVersamentoEnte;
		private String faultCode;
		private String faultString;
		private String faultDescription;
		private StatoOperazioneType stato;
		private String esito;
		private String descrizioneEsito;
		private String codDominio;
		private Double importo;
		private String trn;
		

		public String getIuv() {
			return iuv;
		}

		public void setIuv(String iuv) {
			this.iuv = iuv;
		}

		public String getIur() {
			return iur;
		}

		public void setIur(String iur) {
			this.iur = iur;
		}

		public Date getDataPagamento() {
			return dataPagamento;
		}

		public void setDataPagamento(Date dataPagamento) {
			this.dataPagamento = dataPagamento;
		}

		public String getCodVersamentoEnte() {
			return codVersamentoEnte;
		}

		public void setCodVersamentoEnte(String codVersamentoEnte) {
			this.codVersamentoEnte = codVersamentoEnte;
		}

		public String getCodSingoloVersamentoEnte() {
			return codSingoloVersamentoEnte;
		}

		public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
			this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
		}

		public String getFaultCode() {
			return faultCode;
		}

		public void setFaultCode(String faultCode) {
			this.faultCode = faultCode;
		}

		public String getFaultString() {
			return faultString;
		}

		public void setFaultString(String faultString) {
			this.faultString = faultString;
		}

		public String getFaultDescription() {
			return faultDescription;
		}

		public void setFaultDescription(String faultDescription) {
			this.faultDescription = faultDescription;
		}

		public StatoOperazioneType getStato() {
			return stato;
		}

		public void setStato(StatoOperazioneType stato) {
			this.stato = stato;
		}

		public String getEsito() {
			return esito;
		}

		public void setEsito(String esito) {
			this.esito = esito;
		}

		public String getDescrizioneEsito() {
			return descrizioneEsito;
		}

		public void setDescrizioneEsito(String descrizioneEsito) {
			this.descrizioneEsito = descrizioneEsito;
		}

		public String getCodDominio() {
			return codDominio;
		}

		public void setCodDominio(String codDominio) {
			this.codDominio = codDominio;
		}

		public Double getImporto() {
			return importo;
		}

		public void setImporto(Double importo) {
			this.importo = importo;
		}

		public String getTrn() {
			return trn;
		}

		public void setTrn(String trn) {
			this.trn = trn;
		}

	}
}
