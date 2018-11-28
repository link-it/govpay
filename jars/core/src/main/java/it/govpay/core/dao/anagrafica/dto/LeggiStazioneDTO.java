/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

import it.govpay.model.Canale.TipoVersamento;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiStazioneDTO extends BasicCreateRequestDTO {


	public LeggiStazioneDTO(Authentication user) {
		super(user);
	}
	
	public String getIdCanale() {
		return this.idCanale;
	}

	public void setIdCanale(String idCanale) {
		this.idCanale = idCanale;
	}

	public TipoVersamento getTipoVersamento() {
		return this.tipoVersamento;
	}

	public void setTipoVersamento(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	public String getIdPsp() {
		return this.idPsp;
	}

	public void setIdPsp(String idPsp) {
		this.idPsp = idPsp;
	}

	private String idCanale;
	private String idPsp;
	private TipoVersamento tipoVersamento;
	
}
