/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.model.IAutorizzato;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiPspDTO extends BasicCreateRequestDTO {


	public LeggiPspDTO(IAutorizzato user) {
		super(user);
	}
	
	private String idPsp;

	public String getIdPsp() {
		return idPsp;
	}

	public void setIdPsp(String idPsp) {
		this.idPsp = idPsp;
	}
	
}
