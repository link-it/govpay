/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;


import it.govpay.bd.model.Fr;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiRendicontazioneDTOResponse {

	private Fr fr;

	public Fr getFr() {
		return this.fr;
	}

	public void setFr(Fr fr) {
		this.fr = fr;
	}
}