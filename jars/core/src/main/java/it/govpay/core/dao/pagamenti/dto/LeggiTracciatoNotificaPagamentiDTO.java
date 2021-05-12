/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicRequestDTO;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiTracciatoNotificaPagamentiDTO extends BasicRequestDTO {


	public LeggiTracciatoNotificaPagamentiDTO(Authentication user) {
		super(user);
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public boolean isIncludiRaw() {
		return includiRaw;
	}

	public void setIncludiRaw(boolean includiRaw) {
		this.includiRaw = includiRaw;
	}

	private Long id;
	private String identificativo;
	boolean includiRaw = false; 
}
