/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Applicazione;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiPendenzaDTOResponse {

	private Versamento versamento;
	private List<SingoloVersamento> lstSingoliVersamenti;
	private UnitaOperativa unitaOperativa;
	private Dominio dominio;
	private Applicazione applicazione;

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

	public Dominio getDominio() {
		return dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public UnitaOperativa getUnitaOperativa() {
		return unitaOperativa;
	}

	public void setUnitaOperativa(UnitaOperativa unitaOperativa) {
		this.unitaOperativa = unitaOperativa;
	}

	public List<SingoloVersamento> getLstSingoliVersamenti() {
		return lstSingoliVersamenti;
	}

	public void setLstSingoliVersamenti(List<SingoloVersamento> lstSingoliVersamenti) {
		this.lstSingoliVersamenti = lstSingoliVersamenti;
	}
}
