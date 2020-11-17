/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

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

	public LeggiFrDTO(Authentication user, String idFlusso) {
		super(user);
		this.idFlusso=idFlusso;
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

}
