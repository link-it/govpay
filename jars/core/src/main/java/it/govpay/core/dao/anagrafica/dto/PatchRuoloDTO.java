/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.pagamenti.dto.AbstractPatchDTO;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 28 giu 2018 $
 * 
 */
public class PatchRuoloDTO extends AbstractPatchDTO {

	private String idRuolo;
	
	public PatchRuoloDTO(Authentication user) {
		super(user);
	}

	public String getIdRuolo() {
		return this.idRuolo;
	}

	public void setIdRuolo(String idRuolo) {
		this.idRuolo = idRuolo;
	}

	
}
