/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiRuoloDTO extends BasicCreateRequestDTO {


	public LeggiRuoloDTO(Authentication user) {
		super(user);
	}

	public String getRuolo() {
		return this.ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	private String ruolo;
	
}
