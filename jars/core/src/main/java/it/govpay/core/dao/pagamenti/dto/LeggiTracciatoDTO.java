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
public class LeggiTracciatoDTO extends BasicRequestDTO {


	public LeggiTracciatoDTO(Authentication user) {
		super(user);
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean isIncludiRawRichiesta() {
		return includiRawRichiesta;
	}

	public void setIncludiRawRichiesta(boolean includiRawRichiesta) {
		this.includiRawRichiesta = includiRawRichiesta;
	}

	public boolean isIncludiRawEsito() {
		return includiRawEsito;
	}

	public void setIncludiRawEsito(boolean includiRawEsito) {
		this.includiRawEsito = includiRawEsito;
	}

	public boolean isIncludiZipStampe() {
		return includiZipStampe;
	}

	public void setIncludiZipStampe(boolean includiZipStampe) {
		this.includiZipStampe = includiZipStampe;
	}

	private Long id;
	
	boolean includiRawRichiesta = false; 
	boolean includiRawEsito= false; 
	boolean includiZipStampe = false;
}
