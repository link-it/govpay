/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Rendicontazione;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiRendicontazioneDTOResponse {

	private Fr fr;
	private List<Rendicontazione> rendicontazioni;

	public Fr getFr() {
		return fr;
	}

	public void setFr(Fr fr) {
		this.fr = fr;
	}

	public List<Rendicontazione> getRendicontazioni() {
		return rendicontazioni;
	}

	public void setRendicontazioni(List<Rendicontazione> rendicontazioni) {
		this.rendicontazioni = rendicontazioni;
	}

}
