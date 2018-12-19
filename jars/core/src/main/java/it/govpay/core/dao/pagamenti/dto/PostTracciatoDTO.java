package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.core.dao.commons.Versamento;

public class PostTracciatoDTO extends BasicCreateRequestDTO  {
	
	public PostTracciatoDTO(Authentication user) {
		super(user);
	}
	
	private String idDominio;
	private String nomeFile;
	private byte [] contenuto;
	private List<Versamento> inserimenti;
	private List<Versamento> annullamenti;
	
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
	
}
