/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import it.govpay.model.IAutorizzato;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 28 giu 2018 $
 * 
 */
public class RuoloPatchDTO extends AbstractPatchDTO {

	private String idRuolo;
	
	public RuoloPatchDTO(IAutorizzato user) {
		super(user);
	}

	public String getIdRuolo() {
		return this.idRuolo;
	}

	public void setIdRuolo(String idRuolo) {
		this.idRuolo = idRuolo;
	}

	
}
