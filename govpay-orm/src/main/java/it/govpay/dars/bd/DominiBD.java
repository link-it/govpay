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

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.IbanAccreditoBD;
import it.govpay.bd.anagrafica.TabellaContropartiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Stazione;
import it.govpay.dars.model.DominioExt;
import it.govpay.dars.model.ListaDominiEntry;
import it.govpay.dars.model.converter.DominioConverter;
import it.govpay.orm.TabellaControparti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

public class DominiBD extends it.govpay.bd.anagrafica.DominiBD{

	private TabellaContropartiBD tabellaContropartiBD  = null;
	private IbanAccreditoBD ibanAccreditoBD  = null;

	public DominiBD(BasicBD basicBD) {
		super(basicBD);
		this.tabellaContropartiBD = new TabellaContropartiBD(this);
		this.ibanAccreditoBD = new IbanAccreditoBD(this);
	}

	public List<ListaDominiEntry> findAllListaEntries(DominioFilter filter) throws ServiceException {

		try {
			List<it.govpay.bd.model.Dominio> lstdominio = this.findAll(filter);
			List<ListaDominiEntry> dominiLst = new ArrayList<ListaDominiEntry>();

			for(it.govpay.bd.model.Dominio dominio: lstdominio) {
				ListaDominiEntry entry = new ListaDominiEntry();
				entry.setId(dominio.getId());
				entry.setAbilitato(dominio.isAbilitato());
				entry.setCodDominio(dominio.getCodDominio());
				entry.setRagioneSociale(dominio.getRagioneSociale());
				
				Stazione stazione = AnagraficaManager.getStazione(this, dominio.getIdStazione());
				entry.setCodStazione(stazione.getCodStazione());

				dominiLst.add(entry);
			}
			return dominiLst;
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}  
	}
	
	public ListaDominiEntry getListaEntryDominio(long idDominio) throws NotFoundException, MultipleResultException, ServiceException {
		
		ListaDominiEntry dominio = new ListaDominiEntry();
		
		Dominio dominioDTO = AnagraficaManager.getDominio(this, idDominio);
		
		//Stazione
		Stazione stazione = AnagraficaManager.getStazione(this, dominioDTO.getIdStazione());
		
		dominio.setId(dominioDTO.getId());
		dominio.setAbilitato(dominioDTO.isAbilitato());
		dominio.setCodDominio(dominioDTO.getCodDominio());
		dominio.setRagioneSociale(dominioDTO.getRagioneSociale());
		dominio.setCodStazione(stazione.getCodStazione());
		
		
		return dominio;
	}

	public DominioExt getDominio(long idDominio) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			Dominio dominioDTO = AnagraficaManager.getDominio(this, idDominio);
			
			//Intermediario
			Stazione stazione = AnagraficaManager.getStazione(this, dominioDTO.getIdStazione());
			
			//Tabelle Controparti
			final long id = dominioDTO.getId();
			AbstractFilter filter = new it.govpay.bd.AbstractFilter(this.getServiceManager().getTabellaContropartiServiceSearch()) {

				@Override
				public IExpression toExpression() throws ServiceException {
					try {
						IExpression exp = newExpression();
						exp.equals(new CustomField("id_dominio", Long.class, "id_dominio", this.getRootTable()), id);
						return exp;
					} catch (NotImplementedException e) {
						throw new ServiceException(e);
					} catch (ExpressionNotImplementedException e) {
						throw new ServiceException(e);
					} catch (ExpressionException e) {
						throw new ServiceException(e);
					}
				}
			};
			
			List<FilterSortWrapper> filterSortList = new ArrayList<FilterSortWrapper>();
			
			FilterSortWrapper e = new FilterSortWrapper();
			e.setField(TabellaControparti.model().DATA_ORA_PUBBLICAZIONE);
			e.setSortOrder(SortOrder.DESC);
			
			filterSortList.add(e);
			filter.setFilterSortList(filterSortList);
			List<it.govpay.bd.model.TabellaControparti> tabelleControparti = this.tabellaContropartiBD.findAll(filter);
			
			List<IbanAccredito> ibanAccredito = this.getIbanAccreditoByIdDominio(idDominio);
			
			return DominioConverter.toDominioExt(dominioDTO, stazione, tabelleControparti, ibanAccredito);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}


	}

	public void updateDominioExt(DominioExt dominio) throws NotFoundException, ServiceException {
		super.updateDominio(DominioConverter.toDTO(dominio));
		if(dominio.getIbanAccredito() != null) {
			for(IbanAccredito iban: dominio.getIbanAccredito()) {
				iban.setIdDominio(dominio.getId());
				try {
					this.ibanAccreditoBD.updateIbanAccredito(iban);
				} catch(NotFoundException e) {
					this.ibanAccreditoBD.insertIbanAccredito(iban);
				}
				
			}
		}
		
	}
	
	public void updateDominio(Dominio dominio) throws NotFoundException, ServiceException {
		super.updateDominio(dominio);
	}

	public void insertDominioExt(DominioExt dominio) throws ServiceException{
		
		Dominio dto = DominioConverter.toDTO(dominio);
		super.insertDominio(dto);
		dominio.setId(dto.getId()); 		
		if(dominio.getIbanAccredito() != null) {
			for(IbanAccredito iban: dominio.getIbanAccredito()) {
				iban.setIdDominio(dominio.getId());
				this.ibanAccreditoBD.insertIbanAccredito(iban);
			}
		}
		
	}
	
	public void insertDominio(Dominio dominio) throws ServiceException{
		super.insertDominio(dominio);
	}

	
}
