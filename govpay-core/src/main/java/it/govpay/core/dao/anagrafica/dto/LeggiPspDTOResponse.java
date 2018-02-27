/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Applicazione;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiPspDTOResponse {

	private Rpt rpt;
	private Versamento versamento;
	private Applicazione applicazione;
	private Canale canale;
	private Psp psp;

	public Versamento getVersamento() {
		return versamento;
	}

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}

	public Applicazione getApplicazione() {
		return applicazione;
	}

	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}

	public Rpt getRpt() {
		return rpt;
	}

	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
	}

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
