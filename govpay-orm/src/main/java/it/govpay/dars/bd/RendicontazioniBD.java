/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.dars.bd;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Psp;
import it.govpay.bd.rendicontazione.FrFilter;
import it.govpay.dars.model.FrExt;
import it.govpay.dars.model.ListaRendicontazioniEntry;

public class RendicontazioniBD extends it.govpay.bd.rendicontazione.FrBD {

	public RendicontazioniBD(BasicBD basicBD) {
		super(basicBD);
	}

	public List<ListaRendicontazioniEntry> findAll(FrFilter filter) throws ServiceException {
		try {
			List<it.govpay.orm.FR> lstFRVO = this.getFrService().findAll(filter.toPaginatedExpression());
			List<ListaRendicontazioniEntry> dominiLst = new ArrayList<ListaRendicontazioniEntry>();
			for(it.govpay.orm.FR frVO: lstFRVO) {
				ListaRendicontazioniEntry entry = new ListaRendicontazioniEntry();
				entry.setAnnoRiferimento(frVO.getAnnoRiferimento());
				entry.setCodFlusso(frVO.getCodFlusso());
				
				Psp psp = AnagraficaManager.getPsp(this, frVO.getIdPsp().getId());
				
				entry.setCodPsp(psp.getCodPsp());
				entry.setDataOraFlusso(frVO.getDataOraFlusso());
				entry.setDescrizioneStato(frVO.getDescrizioneStato());
				entry.setId(frVO.getId());
				entry.setImportoTotalePagamenti(frVO.getImportoTotalePagamenti());
				entry.setNumeroPagamenti(frVO.getNumeroPagamenti());
				entry.setStato(Fr.StatoFr.valueOf(frVO.getStato()));
				dominiLst.add(entry);
			}

			return dominiLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}  
	}

	public FrExt get(long idFr) throws NotFoundException, MultipleResultException, ServiceException{
		FrExt frExt  =null;

		Fr fr = this.getFr(idFr);

		if(fr!= null){
			long idPsp = fr.getIdPsp();

			Psp psp = AnagraficaManager.getPsp(this, idPsp);
			
			frExt = new FrExt();
			frExt.setFr(fr);
			frExt.setPsp(psp);
		}
		return frExt;
	}
}
