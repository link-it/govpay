/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 31 gen 2018 $
 * 
 */
public class LeggiRicevutaDTOResponse {

	private Rpt rpt;
	private Dominio dominio;
	private Versamento versamento;

	public Dominio getDominio() {
		return this.dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public Versamento getVersamento() {
		return this.versamento;
	}

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}

	public Rpt getRpt() {
		return this.rpt;
	}

	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
	}
}
