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

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.EnteFilter;
import it.govpay.bd.anagrafica.filters.TributoFilter;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Tributo;
import it.govpay.dars.model.ListaDominiEntry;
import it.govpay.dars.model.ListaTributiEntry;
import it.govpay.dars.model.TributoExt;

public class TributiBD extends BasicBD{

	private it.govpay.bd.anagrafica.TributiBD tributiBD = null;
	private it.govpay.bd.anagrafica.EntiBD entiBD = null;
	private it.govpay.dars.bd.DominiBD dominiBD = null;

	public TributiBD(BasicBD basicBD) {
		super(basicBD);
		this.tributiBD = new it.govpay.bd.anagrafica.TributiBD(basicBD);
		this.entiBD = new it.govpay.bd.anagrafica.EntiBD(basicBD);
		this.dominiBD = new it.govpay.dars.bd.DominiBD(basicBD);
	}

	public List<ListaTributiEntry> findAll(TributoFilter filter) throws ServiceException {
		try {
			List<ListaTributiEntry> tributiLst = new ArrayList<ListaTributiEntry>();
			List<it.govpay.bd.model.Tributo> lsttributoDTO = this.tributiBD.findAll(filter);
								
			for(it.govpay.bd.model.Tributo tributoDTO: lsttributoDTO) {
				ListaTributiEntry entry = new ListaTributiEntry();
				entry.setId(tributoDTO.getId());
				entry.setAbilitato(tributoDTO.isAbilitato());
				entry.setCodTributo(tributoDTO.getCodTributo());
				entry.setDescrizione(tributoDTO.getDescrizione());
				
				Ente ente = AnagraficaManager.getEnte(this,tributoDTO.getIdEnte());
				
				entry.setCodEnte(ente.getCodEnte());
				
				IbanAccredito iban = AnagraficaManager.getIbanAccredito(this, tributoDTO.getIbanAccredito());
				
				entry.setCodIban(iban.getCodIban());

				tributiLst.add(entry);
			}
			return tributiLst;
		}  catch (MultipleResultException e) {
			throw new ServiceException(e);
		}  catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	public TributoExt getTributo(long idTributo) throws NotFoundException, MultipleResultException, ServiceException {
		Tributo tributo =  this.tributiBD.getTributo(idTributo);

		TributoExt tributoExt = null;

		if(tributo!= null){
			Ente ente = AnagraficaManager.getEnte(this,tributo.getIdEnte());
			IbanAccredito iban = AnagraficaManager.getIbanAccredito(this, tributo.getIbanAccredito());

			tributoExt = new TributoExt(tributo, ente,iban);
		}

		return tributoExt;
	}

	public ListaTributiEntry getListaTributiEntry(long idTributo) throws NotFoundException, MultipleResultException, ServiceException {
		Tributo tributo =  this.tributiBD.getTributo(idTributo);

		ListaTributiEntry entry = null;

		if(tributo!= null){
			entry = new ListaTributiEntry();
			entry.setId(tributo.getId());
			entry.setAbilitato(tributo.isAbilitato());
			entry.setCodTributo(tributo.getCodTributo());
			entry.setDescrizione(tributo.getDescrizione());
			Ente ente = AnagraficaManager.getEnte(this,tributo.getIdEnte());
			entry.setCodEnte(ente.getCodEnte());
			IbanAccredito iban = AnagraficaManager.getIbanAccredito(this, tributo.getIbanAccredito());
			entry.setCodIban(iban.getCodIban());
		}

		return entry;
	}

	public void updateTributo(Tributo tributo) throws NotFoundException, ServiceException {
		this.tributiBD.updateTributo(tributo);
	}
	
	public void updateTributoExt(TributoExt tributo) throws NotFoundException, ServiceException {
		this.tributiBD.updateTributo(tributo.getTributo());
	}

	public void insertTributo(Tributo tributo) throws ServiceException{
		this.tributiBD.insertTributo(tributo); 
	}
	
	public void insertTributoExt(TributoExt tributo) throws ServiceException{
		this.tributiBD.insertTributo(tributo.getTributo()); 
	}

	public TributoFilter newFilter() throws ServiceException {
		return this.tributiBD.newFilter();
	}

	public  List<ListaTributiEntry> findAllTributiByIdStazione(TributoFilter filter,List<Long> idTributi, long idStazione)  throws ServiceException{
		List<ListaTributiEntry> tributiLst = new ArrayList<ListaTributiEntry>();


		Stazione stazione = null;
		try {
			stazione = AnagraficaManager.getStazione(this.tributiBD, idStazione);
		} catch (NotFoundException e1) {
			return tributiLst;
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

		List<ListaDominiEntry> listaDomini = new ArrayList<ListaDominiEntry>();
		try{
			DominioFilter dominiFilter = dominiBD.newFilter();
			dominiFilter.setCodStazione(stazione.getCodStazione());

			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
			fsw.setSortOrder(SortOrder.ASC);
			dominiFilter.getFilterSortList().add(fsw);

			listaDomini = dominiBD.findAllListaEntries(dominiFilter);

			if(listaDomini == null || listaDomini.size() == 0)
				return tributiLst;
		}catch(ServiceException e){
			throw e;
		}

		List<it.govpay.orm.Ente> listaEnti = new ArrayList<it.govpay.orm.Ente>();

		EnteFilter enteFilter = this.entiBD.newFilter();

		IPaginatedExpression pagExpr = enteFilter.toPaginatedExpression();


		try {
			List<String> codiciDominio = new ArrayList<String>();
			for (ListaDominiEntry listaDominiEntry : listaDomini) {
				codiciDominio.add(listaDominiEntry.getCodDominio());
			}

			pagExpr.in(it.govpay.orm.Ente.model().ID_DOMINIO.COD_DOMINIO, codiciDominio);

			listaEnti = this.getServiceManager().getEnteServiceSearch().findAll(pagExpr);

			if(listaEnti == null || listaEnti.size() == 0)
				return tributiLst;
		}catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}


		try {
			IPaginatedExpression paginatedExpression = filter.toPaginatedExpression();

			List<String> codiciEnte = new ArrayList<String>();
			for (it.govpay.orm.Ente  entry : listaEnti) {
				codiciEnte.add(entry.getCodEnte());
			}

			pagExpr.in(it.govpay.orm.Tributo.model().ID_ENTE.COD_ENTE, codiciEnte);


			List<it.govpay.orm.Tributo> lsttributoVO = 
					this.getServiceManager().getTributoServiceSearch().findAll(paginatedExpression);


			for(it.govpay.orm.Tributo tributoVO: lsttributoVO) {
				// aggiungo solo i tributi non presenti.
				if(!idTributi.contains(tributoVO.getId().longValue())){
					ListaTributiEntry entry = new ListaTributiEntry();
					entry.setId(tributoVO.getId());
					entry.setAbilitato(tributoVO.isAbilitato());
					entry.setCodTributo(tributoVO.getCodTributo());
					entry.setDescrizione(tributoVO.getDescrizione());
					entry.setCodEnte(tributoVO.getIdEnte().getCodEnte());
					entry.setCodIban(tributoVO.getIbanAccredito().getCodIban());

					tributiLst.add(entry);
				}
			}
			return tributiLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}

	}
}
