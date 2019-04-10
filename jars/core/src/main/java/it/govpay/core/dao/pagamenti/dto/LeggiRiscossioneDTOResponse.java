/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Pagamento;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiRiscossioneDTOResponse {

	private Pagamento pagamento;
	private Dominio dominio;

	public Pagamento getPagamento() {
		return this.pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public Dominio getDominio() {
		return dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}
}