/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Operatore;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiOperatoreDTOResponse {

	private Operatore operatore;

	public Operatore getOperatore() {
		return this.operatore;
	}

	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}
}
