/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 31 gen 2018 $
 * 
 */
public class LeggiRicevutaDTO extends BasicCreateRequestDTO {

	public enum FormatoRicevuta  {PDF, JSON, XML, RAW};
	/*
	 * @param user
	 */
	public LeggiRicevutaDTO(Authentication user) {
		super(user);
	}
	 
	private String idDominio;
	private String iuv;
	private String ccp;
	private FormatoRicevuta formato;
	
	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCcp() {
		return this.ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}

	public FormatoRicevuta getFormato() {
		return formato;
	}

	public void setFormato(FormatoRicevuta formato) {
		this.formato = formato;
	}
}
