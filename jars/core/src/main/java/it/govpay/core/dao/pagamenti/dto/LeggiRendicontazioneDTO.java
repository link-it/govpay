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
public class LeggiRendicontazioneDTO extends BasicCreateRequestDTO {


	public LeggiRendicontazioneDTO(Authentication user, String idFlusso) {
		super(user);
		this.idFlusso=idFlusso;
	}

	public String getIdFlusso() {
		return this.idFlusso;
	}

	public void setIdFlusso(String idFlusso) {
		this.idFlusso = idFlusso;
	}

	private String idFlusso;
}
