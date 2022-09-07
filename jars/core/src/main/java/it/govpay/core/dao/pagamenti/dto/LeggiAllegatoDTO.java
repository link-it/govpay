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
public class LeggiAllegatoDTO extends BasicRequestDTO {


	public LeggiAllegatoDTO(Authentication user) {
		super(user);
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isIncludiRawContenuto() {
		return includiRawContenuto;
	}

	public void setIncludiRawContenuto(boolean includiRawContenuto) {
		this.includiRawContenuto = includiRawContenuto;
	}

	private Long id;
	
	boolean includiRawContenuto = false; 
}
