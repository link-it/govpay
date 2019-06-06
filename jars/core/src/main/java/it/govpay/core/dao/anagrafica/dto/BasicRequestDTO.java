package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import org.springframework.security.core.Authentication;

public class BasicRequestDTO {
	
	private List<Long> idDomini;
	private List<String> codDomini;
	private List<Long> idTipiVersamento;
	private List<String> codTipiVersamento;
	private Authentication authentication;
 	
	public BasicRequestDTO(Authentication authentication) {
		this.authentication = authentication;
		this.idDomini = null;
		this.codDomini = null;
		this.idTipiVersamento = null;
		this.codTipiVersamento = null;
	}
	
	public Authentication getUser() {
		return this.authentication;
	}

	public List<Long> getIdDomini() {
		return idDomini;
	}

	public void setIdDomini(List<Long> idDomini) {
		this.idDomini = idDomini;
	}

	public List<String> getCodDomini() {
		return codDomini;
	}

	public void setCodDomini(List<String> codDomini) {
		this.codDomini = codDomini;
	}

	public List<Long> getIdTipiVersamento() {
		return idTipiVersamento;
	}

	public void setIdTipiVersamento(List<Long> idTipiVersamento) {
		this.idTipiVersamento = idTipiVersamento;
	}

	public List<String> getCodTipiVersamento() {
		return codTipiVersamento;
	}

	public void setCodTipiVersamento(List<String> codTipiVersamento) {
		this.codTipiVersamento = codTipiVersamento;
	}
	
	
}
