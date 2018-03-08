/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Psp;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiCanaleDTOResponse {

	private Canale canale;
	private Psp psp;

	public Canale getCanale() {
		return canale;
	}

	public void setCanale(Canale canale) {
		this.canale = canale;
	}

	public Psp getPsp() {
		return psp;
	}

	public void setPsp(Psp psp) {
		this.psp = psp;
	}

}
