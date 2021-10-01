package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Operatore;
import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.core.dao.commons.Versamento;
import it.govpay.model.Tracciato.FORMATO_TRACCIATO;

public class PostTracciatoDTO extends BasicCreateRequestDTO  {
	
	public PostTracciatoDTO(Authentication user) {
		super(user);
	}
	
	private String idDominio;
	private String idTipoPendenza;
	private String nomeFile;
	private byte [] contenuto;
	private List<Versamento> inserimenti;
	private List<Versamento> annullamenti;
	private Operatore operatore;
	private FORMATO_TRACCIATO formato;
	private boolean stampaAvvisi;
	
	public List<Versamento> getInserimenti() {
		return this.inserimenti;
	}
	public void setInserimenti(List<Versamento> inserimenti) {
		this.inserimenti = inserimenti;
	}
	public List<Versamento> getAnnullamenti() {
		return this.annullamenti;
	}
	public void setAnnullamenti(List<Versamento> annullamenti) {
		this.annullamenti = annullamenti;
	}
	public String getNomeFile() {
		return this.nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public byte[] getContenuto() {
		return this.contenuto;
	}
	public void setContenuto(byte[] contenuto) {
		this.contenuto = contenuto;
	}
	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public Operatore getOperatore() {
		return operatore;
	}
	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}
	public String getIdTipoPendenza() {
		return idTipoPendenza;
	}
	public void setIdTipoPendenza(String idTipoPendenza) {
		this.idTipoPendenza = idTipoPendenza;
	}
	public FORMATO_TRACCIATO getFormato() {
		return formato;
	}
	public void setFormato(FORMATO_TRACCIATO formato) {
		this.formato = formato;
	}
	public boolean isStampaAvvisi() {
		return stampaAvvisi;
	}
	public void setStampaAvvisi(boolean stampaAvvisi) {
		this.stampaAvvisi = stampaAvvisi;
	}
}
