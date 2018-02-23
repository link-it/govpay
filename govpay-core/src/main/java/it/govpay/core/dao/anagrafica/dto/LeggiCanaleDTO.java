/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.IAutorizzato;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiCanaleDTO extends BasicCreateRequestDTO {


	public LeggiCanaleDTO(IAutorizzato user) {
		super(user);
	}
	
	public String getIdCanale() {
		return idCanale;
	}

	public void setIdCanale(String idCanale) {
		this.idCanale = idCanale;
	}

	public TipoVersamento getTipoVersamento() {
		return tipoVersamento;
	}

	public void setTipoVersamento(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	public String getIdPsp() {
		return idPsp;
	}

	public void setIdPsp(String idPsp) {
		this.idPsp = idPsp;
	}

	private String idCanale;
	private String idPsp;
	private TipoVersamento tipoVersamento;
	
}
