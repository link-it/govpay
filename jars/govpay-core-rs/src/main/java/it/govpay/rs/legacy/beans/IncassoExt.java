/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.rs.legacy.beans;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.core.utils.IncassoUtils;

public class IncassoExt extends Incasso{
	
	private String riferimento_rendicontazione;
	private List<Pagamento> pagamenti;
	
	public IncassoExt() {
		super();
	}
	
	public IncassoExt(it.govpay.bd.model.Incasso i, BasicBD bd) throws ServiceException {
		super(i);
		this.pagamenti = new ArrayList<>();
		for(it.govpay.bd.model.Pagamento p : i.getPagamenti(bd)) {
			this.pagamenti.add(toRsModel(p, bd));
		}
		this.riferimento_rendicontazione = IncassoUtils.getRiferimentoIncasso(this.getCausale());
	}
	
	
	private static Pagamento toRsModel(it.govpay.bd.model.Pagamento p, BasicBD bd) throws ServiceException {
		Pagamento rsModel = new Pagamento();
		rsModel.setDominio(p.getCodDominio());
		rsModel.setIuv(p.getIuv());
		rsModel.setIur(p.getIur());
		rsModel.setImporto(p.getImportoPagato());
		rsModel.setData_pagamento(p.getDataPagamento());
		rsModel.setId_applicazione(p.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd).getCodApplicazione());
		rsModel.setId_versamento_ente(p.getSingoloVersamento(bd).getVersamento(bd).getCodVersamentoEnte());
		rsModel.setId_singolo_versamento_ente(p.getSingoloVersamento(bd).getCodSingoloVersamentoEnte());
		
		return rsModel;
	}

	public List<Pagamento> getPagamenti() {
		return this.pagamenti;
	}
	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}

	public String getRiferimento_rendicontazione() {
		return this.riferimento_rendicontazione;
	}

	public void setRiferimento_rendicontazione(String riferimento_rendicontazione) {
		this.riferimento_rendicontazione = riferimento_rendicontazione;
	}
}
