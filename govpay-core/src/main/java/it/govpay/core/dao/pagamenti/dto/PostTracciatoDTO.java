package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.core.dao.commons.Versamento;
import it.govpay.model.IAutorizzato;

public class PostTracciatoDTO extends BasicCreateRequestDTO  {
	
	public PostTracciatoDTO(IAutorizzato user) {
		super(user);
	}
	
	private String idDominio;
	private String nomeFile;
	private byte [] contenuto;
	private List<Versamento> inserimenti;
	private List<Versamento> annullamenti;
	
	public List<Versamento> getInserimenti() {
		return inserimenti;
	}
	public void setInserimenti(List<Versamento> inserimenti) {
		this.inserimenti = inserimenti;
	}
	public List<Versamento> getAnnullamenti() {
		return annullamenti;
	}
	public void setAnnullamenti(List<Versamento> annullamenti) {
		this.annullamenti = annullamenti;
	}
	public String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public byte[] getContenuto() {
		return contenuto;
	}
	public void setContenuto(byte[] contenuto) {
		this.contenuto = contenuto;
	}
	public String getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	
}
