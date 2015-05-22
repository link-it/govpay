/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.report.dto;

import it.gov.spcoop.avvisopagamentopa.informazioniversamentoqr.InformazioniVersamento;
import it.govpay.ejb.model.EnteCreditoreModel;
import it.govpay.report.utils.JaxbUtils;
import it.govpay.rs.avviso.RichiestaAvviso;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

public class DocumentoDiPagamentoDTO implements Serializable, PrintableDocument {
	
	private static final long serialVersionUID = 1L;
	private EnteCreditoreModel ente;
	private RichiestaAvviso richiestaAvviso;
	private InformazioniVersamento informazioni;
	
	@Override
	public Boolean needWatermark() {
		return false;
	}

	@Override
	public String getWatermarkText(ResourceBundle bundle) {
		return "";
	}

	public EnteCreditoreModel getEnte() {
		return ente;
	}

	public void setEnte(EnteCreditoreModel ente) {
		this.ente = ente;
	}

	public String getQrCode() throws JAXBException {
		if(informazioni != null)
			return JaxbUtils.toString(informazioni);
		else 
			return null;
	}

	public RichiestaAvviso getRichiestaAvviso() {
		return richiestaAvviso;
	}

	public void setRichiestaAvviso(RichiestaAvviso richiestaAvviso) {
		this.richiestaAvviso = richiestaAvviso;
	}

	public InformazioniVersamento getInformazioni() {
		return informazioni;
	}

	public void setInformazioni(InformazioniVersamento informazioni) {
		this.informazioni = informazioni;
	}

}
