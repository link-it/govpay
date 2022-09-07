/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import it.govpay.bd.model.Allegato;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.model.TipoVersamento;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiAllegatoDTOResponse {

	private Versamento versamento;
	private UnitaOperativa unitaOperativa;
	private Dominio dominio;
	private Applicazione applicazione;
	private Allegato allegato;
	private TipoVersamento tipoVersamento;

	public Applicazione getApplicazione() {
		return this.applicazione;
	}

	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}

	public Dominio getDominio() {
		return this.dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public UnitaOperativa getUnitaOperativa() {
		return this.unitaOperativa;
	}

	public void setUnitaOperativa(UnitaOperativa unitaOperativa) {
		this.unitaOperativa = unitaOperativa;
	}

	public Versamento getVersamento() {
		return versamento;
	}

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}

	public TipoVersamento getTipoVersamento() {
		return tipoVersamento;
	}

	public void setTipoVersamento(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	public Allegato getAllegato() {
		return allegato;
	}

	public void setAllegato(Allegato allegato) {
		this.allegato = allegato;
	}
	
}
