/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import it.govpay.core.dao.anagrafica.dto.BasicRequestDTO;
import it.govpay.model.IAutorizzato;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiOperazioneDTO extends BasicRequestDTO {


	public LeggiOperazioneDTO(IAutorizzato user) {
		super(user);
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Long id;
	
}
