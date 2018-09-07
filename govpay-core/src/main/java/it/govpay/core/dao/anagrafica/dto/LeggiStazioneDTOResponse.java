/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Canale;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiStazioneDTOResponse {

	private Canale canale;

	public Canale getCanale() {
		return this.canale;
	}

	public void setCanale(Canale canale) {
		this.canale = canale;
	}

}
