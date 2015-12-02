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
package it.govpay.bd.anagrafica;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.EnteFilter;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.converter.AnagraficaConverter;
import it.govpay.bd.model.converter.EnteConverter;
import it.govpay.orm.IdAnagrafica;
import it.govpay.orm.IdEnte;
import it.govpay.orm.dao.jdbc.JDBCEnteServiceSearch;
import it.govpay.orm.dao.jdbc.converter.EnteFieldConverter;
import it.govpay.orm.dao.jdbc.converter.OperatoreFieldConverter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.utils.UtilsException;

public class EntiBD extends BasicBD {

	public EntiBD(BasicBD basicBD) {
		super(basicBD);
	}

	
	/**
	 * Recupera l'ente tramite l'id fisico
	 * 
	 * @param idEnte
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Ente getEnte(Long idEnte) throws NotFoundException, MultipleResultException, ServiceException {
		if(idEnte == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		
		long id = idEnte.longValue();

		try {
			it.govpay.orm.Ente enteVO = ((JDBCEnteServiceSearch)this.getServiceManager().getEnteServiceSearch()).get(id);
			Ente ente = EnteConverter.toDTO(enteVO);
			ente.setAnagraficaEnte(AnagraficaConverter.toDTO(this.getServiceManager().getAnagraficaServiceSearch().get(enteVO.getIdAnagraficaEnte())));
			return ente;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera l'ente tramite codEnte
	 */
	public Ente getEnte(String codEnte) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IdEnte id = new IdEnte();
			id.setCodEnte(codEnte);
			it.govpay.orm.Ente enteVO = this.getServiceManager().getEnteServiceSearch().get(id);
			return getEnte(enteVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public Ente getEnte(it.govpay.orm.Ente enteVO) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException {
		Ente ente = EnteConverter.toDTO(enteVO);
		ente.setAnagraficaEnte(AnagraficaConverter.toDTO(this.getServiceManager().getAnagraficaServiceSearch().get(enteVO.getIdAnagraficaEnte())));
		return ente;
	}
	/**
	 * Recupera la lista degli enti censiti sul sistema
	 * 
	 * @return
	 * @throws ServiceException in caso di errore DB.
	 */
	public List<Ente> getEnti() throws ServiceException{
		return this.findAll(this.newFilter());
	}
	
	/**
	 * Recupera la lista degli enti gestiti da un operatore
	 * 
	 * @return
	 * @throws ServiceException in caso di errore DB.
	 */
	public List<Ente> getEnti(long idOperatore) throws ServiceException{
		try {
			List<Ente> lst = new ArrayList<Ente>();
			IPaginatedExpression exp = this.getServiceManager().getEnteServiceSearch().newPaginatedExpression();
			OperatoreFieldConverter fieldConverter = new OperatoreFieldConverter(this.getServiceManager().getJdbcProperties().getDatabaseType());

			exp.equals(new CustomField("id_operatore", Long.class, "id_operatore", fieldConverter.toTable(it.govpay.orm.Operatore.model().OPERATORE_ENTE)), idOperatore);
			List<it.govpay.orm.Ente> lstenteVO = this.getServiceManager().getEnteServiceSearch().findAll(exp);
			for(it.govpay.orm.Ente enteVO: lstenteVO) {
				lst.add(getEnte(enteVO));
			}
			return lst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Aggiorna l'ente con i dati forniti
	 * @param ente
	 * @throws NotFoundException se l'ente non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateEnte(Ente ente) throws NotFoundException, ServiceException {
		try {
			
			it.govpay.orm.Ente vo = EnteConverter.toVO(ente);
			IdEnte idEnte = this.getServiceManager().getEnteServiceSearch().convertToId(vo);

			if(!this.getServiceManager().getEnteServiceSearch().exists(idEnte)) {
				throw new NotFoundException("Ente con id ["+idEnte.toJson()+"] non trovato");
			}

			it.govpay.orm.Ente enteDB = this.getServiceManager().getEnteServiceSearch().get(idEnte);

			ente.getAnagraficaEnte().setId(enteDB.getIdAnagraficaEnte().getId());
			it.govpay.orm.Anagrafica voAnagrafica = AnagraficaConverter.toVO(ente.getAnagraficaEnte());
			
			IdAnagrafica idAnagrafica = this.getServiceManager().getAnagraficaServiceSearch().convertToId(voAnagrafica);

			
			if(!this.getServiceManager().getAnagraficaServiceSearch().exists(idAnagrafica)) {
				throw new NotFoundException("Anagrafica con id ["+idAnagrafica.toJson()+"] non trovata");
			}

			this.getServiceManager().getAnagraficaService().update(idAnagrafica, voAnagrafica);
			
			idAnagrafica.setId(voAnagrafica.getId());
			vo.setIdAnagraficaEnte(idAnagrafica);
			
			this.getServiceManager().getEnteService().update(idEnte, vo);
			ente.setId(vo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Aggiorna l'ente con i dati forniti
	 * @param ente
	 * @throws NotPermittedException se si inserisce un Ente gia' censito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertEnte(Ente ente) throws ServiceException{
		try {
			it.govpay.orm.Anagrafica voAnagrafica = AnagraficaConverter.toVO(ente.getAnagraficaEnte());
			IdAnagrafica idAnagrafica = this.getServiceManager().getAnagraficaServiceSearch().convertToId(voAnagrafica);
			this.getServiceManager().getAnagraficaService().create(voAnagrafica);
			
			it.govpay.orm.Ente vo = EnteConverter.toVO(ente);
			
			idAnagrafica.setId(voAnagrafica.getId());
			vo.setIdAnagraficaEnte(idAnagrafica);

			this.getServiceManager().getEnteService().create(vo);
			ente.setId(vo.getId());

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public EnteFilter newFilter() throws ServiceException {
		try {
			return new EnteFilter(this.getServiceManager().getEnteServiceSearch());
		} catch (ServiceException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public long count(EnteFilter filter) throws ServiceException {
		try {
			return this.getServiceManager().getEnteServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Ente> findAll(EnteFilter filter) throws ServiceException {
		try {
			List<Ente> lst = new ArrayList<Ente>();
			List<it.govpay.orm.Ente> lstenteVO = this.getServiceManager().getEnteServiceSearch().findAll(filter.toPaginatedExpression());
			for(it.govpay.orm.Ente enteVO: lstenteVO) {
				lst.add(getEnte(enteVO));
			}
			return lst;

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}


	public List<Ente> getEntiByDominio(Long idDominio) throws ServiceException {
		try {
			List<Ente> lst = new ArrayList<Ente>();
			IPaginatedExpression exp = this.getServiceManager().getEnteServiceSearch().newPaginatedExpression();
			EnteFieldConverter fieldConverter = new EnteFieldConverter(this.getServiceManager().getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_dominio", Long.class, "id_dominio", fieldConverter.toTable(it.govpay.orm.Ente.model())), idDominio);
			List<it.govpay.orm.Ente> lstenteVO = this.getServiceManager().getEnteServiceSearch().findAll(exp);
			for(it.govpay.orm.Ente enteVO: lstenteVO) {
				lst.add(getEnte(enteVO));
			}
			return lst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

}
