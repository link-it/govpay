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
package it.govpay.web.rs.v1.beans;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Fr;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.core.utils.IncassoUtils;

public class IncassoExt extends Incasso{
	
	private List<Pagamento> pagamenti;
	private String riferimento_rendicontazione;
	
	public IncassoExt() {
		super();
	}
	
	public IncassoExt(it.govpay.bd.model.Incasso i, BasicBD bd) throws ServiceException {
		super(i);
		this.pagamenti = new ArrayList<Pagamento>();
		for(it.govpay.bd.model.Pagamento p : i.getPagamenti(bd)) {
			pagamenti.add(new Pagamento(p, bd));
		}
		List<String> idfs = IncassoUtils.getRiferimentoIncassoCumulativo(this.getCausale());
		
		if(idfs.size()==1){
			this.setRiferimento_rendicontazione(idfs.get(0));
			return;
		}
			
		FrBD frBD = new FrBD(bd);
		for(String codFlusso : idfs) {
			FrFilter newFilter = frBD.newFilter();
			newFilter.setCodFlusso(codFlusso);
			List<Fr> frs = frBD.findAll(newFilter);
			if(frs.size() > 0) {
				this.setRiferimento_rendicontazione(codFlusso);
				return;
			}
		}
	}
	
	public List<Pagamento> getPagamenti() {
		return pagamenti;
	}
	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}

	public String getRiferimento_rendicontazione() {
		return riferimento_rendicontazione;
	}

	public void setRiferimento_rendicontazione(String riferimento_rendicontazione) {
		this.riferimento_rendicontazione = riferimento_rendicontazione;
	}

}
