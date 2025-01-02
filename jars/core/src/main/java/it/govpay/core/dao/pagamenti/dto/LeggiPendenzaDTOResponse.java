/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.bd.model.Allegato;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.model.TipoVersamento;

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
	private byte[] avvisoPdf;
	private List<PagamentoPortale> pagamenti;
	private List<Rpt> rpts;
	private TipoVersamento tipoVersamento;
	private List<Allegato> allegati;

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

	public List<SingoloVersamento> getLstSingoliVersamenti() {
		return this.lstSingoliVersamenti;
	}

	public void setLstSingoliVersamenti(List<SingoloVersamento> lstSingoliVersamenti) {
		this.lstSingoliVersamenti = lstSingoliVersamenti;
	}

	public byte[] getAvvisoPdf() {
		return this.avvisoPdf;
	}

	public void setAvvisoPdf(byte[] avvisoPdf) {
		this.avvisoPdf = avvisoPdf;
	}

	public Versamento getVersamento() {
		return versamento;
	}

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}

	public List<PagamentoPortale> getPagamenti() {
		return pagamenti;
	}

	public void setPagamenti(List<PagamentoPortale> pagamenti) {
		this.pagamenti = pagamenti;
	}

	public List<Rpt> getRpts() {
		return rpts;
	}

	public void setRpts(List<Rpt> rpts) {
		this.rpts = rpts;
	}

	public TipoVersamento getTipoVersamento() {
		return tipoVersamento;
	}

	public void setTipoVersamento(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	public List<Allegato> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<Allegato> allegati) {
		this.allegati = allegati;
	}
	
}
