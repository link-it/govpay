/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import javax.ws.rs.core.MediaType;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiFrDTO extends BasicCreateRequestDTO {

	private Boolean obsoleto = null;
	private String idFlusso;
	private String idDominio;
	private Date dataOraFlusso;
	private String accept;	
	private String codApplicazione;
	private Integer annoRiferimento;

	public LeggiFrDTO(Authentication user, String idFlusso) {
		super(user);
		this.idFlusso=idFlusso;
		this.accept = MediaType.APPLICATION_JSON;
	}

	public String getIdFlusso() {
		return this.idFlusso;
	}

	public void setIdFlusso(String idFlusso) {
		this.idFlusso = idFlusso;
	}

	public Boolean getObsoleto() {
		return obsoleto;
	}

	public void setObsoleto(Boolean obsoleto) {
		this.obsoleto = obsoleto;
	}

	public Date getDataOraFlusso() {
		return dataOraFlusso;
	}

	public void setDataOraFlusso(Date dataOraFlusso) {
		this.dataOraFlusso = dataOraFlusso;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public Integer getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(Integer annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

}
