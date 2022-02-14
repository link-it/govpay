/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;


import java.util.List;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.viste.model.Rendicontazione;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiFrDTOResponse {

	private Fr fr;
	private Dominio dominio;
	private boolean authorized;
	private List<Rendicontazione> rendicontazioni;

	public Fr getFr() {
		return this.fr;
	}

	public void setFr(Fr fr) {
		this.fr = fr;
	}

	public Dominio getDominio() {
		return dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public boolean isAuthorized() {
		return authorized;
	}

	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}

	public List<Rendicontazione> getRendicontazioni() {
		return rendicontazioni;
	}

	public void setRendicontazioni(List<Rendicontazione> rendicontazioni) {
		this.rendicontazioni = rendicontazioni;
	}
}