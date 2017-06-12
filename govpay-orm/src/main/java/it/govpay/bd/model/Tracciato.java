package it.govpay.bd.model;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.model.Operatore;
import it.govpay.orm.constants.StatoTracciatoType;

import java.util.Date;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Tracciato {

	public long getIdOperatore() {
		return idOperatore;
	}
	public void setIdOperatore(long idOperatore) {
		this.idOperatore = idOperatore;
	}
	public Date getDataCaricamento() {
		return dataCaricamento;
	}
	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}
	public StatoTracciatoType getStato() {
		return stato;
	}
	public void setStato(StatoTracciatoType stato) {
		this.stato = stato;
	}
	public long getLineaElaborazione() {
		return lineaElaborazione;
	}
	public void setLineaElaborazione(long lineaElaborazione) {
		this.lineaElaborazione = lineaElaborazione;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public long getNumLineeTotali() {
		return numLineeTotali;
	}
	public void setNumLineeTotali(long numLineeTotali) {
		this.numLineeTotali = numLineeTotali;
	}
	public long getNumOperazioniOk() {
		return numOperazioniOk;
	}
	public void setNumOperazioniOk(long numOperazioniOk) {
		this.numOperazioniOk = numOperazioniOk;
	}
	public long getNumOperazioniKo() {
		return numOperazioniKo;
	}
	public void setNumOperazioniKo(long numOperazioniKo) {
		this.numOperazioniKo = numOperazioniKo;
	}
	public String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public byte[] getRawData() {
		return rawData;
	}
	public void setRawData(byte[] rawData) {
		this.rawData = rawData;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	private long id;
	private long idOperatore;
	private Date dataCaricamento;
	private Date dataUltimoAggiornamento;
	private StatoTracciatoType stato;
	private long lineaElaborazione;
	private String descrizioneStato;
	private long numLineeTotali;
	private long numOperazioniOk;
	private long numOperazioniKo;
	private String nomeFile;
	private byte[] rawData;
	
	private Operatore operatore = null;
	
	public Operatore getOperatore(BasicBD bd) throws ServiceException {
		if(this.operatore == null) {
			try {
				this.operatore = new OperatoriBD(bd).getOperatore(idOperatore);
			} catch(NotFoundException e) {
				throw new ServiceException(e);
			} catch(MultipleResultException e) {
				throw new ServiceException(e);
			}
		}
		return this.operatore;
	}
	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}
	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}
	

}
