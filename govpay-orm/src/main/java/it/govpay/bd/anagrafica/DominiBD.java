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
package it.govpay.bd.anagrafica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

import it.govpay.bd.BasicBD;
import it.govpay.bd.IFilter;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.converter.DominioConverter;
import it.govpay.bd.model.converter.IbanAccreditoConverter;
import it.govpay.bd.wrapper.StatoNdP;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.orm.IdDominio;
import it.govpay.orm.dao.jdbc.JDBCDominioService;
import it.govpay.orm.dao.jdbc.JDBCDominioServiceSearch;
import it.govpay.orm.dao.jdbc.converter.DominioFieldConverter;
import it.govpay.orm.dao.jdbc.converter.IbanAccreditoFieldConverter;

public class DominiBD extends BasicBD {
	
	public static final String tipoElemento = "DOMINIO";

	public DominiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera il Dominio tramite il codDominio
	 * 
	 * @param codDominio
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Dominio getDominio(String codDominio) throws NotFoundException, ServiceException {
		try {
			IdDominio id = new IdDominio();
			id.setCodDominio(codDominio);
			it.govpay.orm.Dominio dominioVO = this.getDominioService().get(id);
			Dominio dominio = DominioConverter.toDTO(dominioVO, this);
			return dominio;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera il Dominio tramite id
	 * 
	 * @param idDominio
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Dominio getDominio(Long idDominio) throws NotFoundException, MultipleResultException, ServiceException {
		if(idDominio == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		
		long id = idDominio.longValue();
		
		try {
			it.govpay.orm.Dominio dominioVO = ((JDBCDominioServiceSearch)this.getDominioService()).get(id);
			Dominio dominio = DominioConverter.toDTO(dominioVO, this);
			return dominio;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Inserisce il dominio con i dati forniti
	 * @param dominio
	 * @throws NotPermittedException se si inserisce un Dominio gia' censito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertDominio(Dominio dominio) throws ServiceException{
		try {
			it.govpay.orm.Dominio vo = DominioConverter.toVO(dominio);
			this.getDominioService().create(vo);
			dominio.setId(vo.getId());
			this.emitAudit(dominio);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Aggiorna il dominio con i dati forniti
	 * @param dominio
	 * @throws NotPermittedException se si inserisce un Dominio gia' censito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateDominio(Dominio dominio) throws NotFoundException, ServiceException{
		try {
			it.govpay.orm.Dominio vo = DominioConverter.toVO(dominio);
			IdDominio id = this.getDominioService().convertToId(vo);
			
			if(!this.getDominioService().exists(id)) {
				throw new NotFoundException("Dominio con id ["+id+"] non esiste.");
			}
			this.getDominioService().update(id, vo);
			dominio.setId(vo.getId());
			this.emitAudit(dominio);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}
	
	public void setStatoNdp(long idDominio, StatoNdP statoNdp) throws ServiceException{
		this.setStatoNdp(idDominio, statoNdp.getCodice(), statoNdp.getOperazione(), statoNdp.getDescrizione());
	}
	
	public void setStatoNdp(long idDominio, Integer codice, String operazione, String descrizione) throws ServiceException{
		try {
			List<UpdateField> lst = new ArrayList<>();
			lst.add(new UpdateField(it.govpay.orm.Dominio.model().NDP_STATO, codice));
			lst.add(new UpdateField(it.govpay.orm.Dominio.model().NDP_OPERAZIONE, operazione));
			lst.add(new UpdateField(it.govpay.orm.Dominio.model().NDP_DESCRIZIONE, descrizione));
			lst.add(new UpdateField(it.govpay.orm.Dominio.model().NDP_DATA, new Date()));
			
			((JDBCDominioService)this.getDominioService()).updateFields(idDominio, lst.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	public StatoNdP getStatoNdp(long idDominio) throws NotFoundException, ServiceException {
		try {
			
			List<IField> lst = new ArrayList<>();
			lst.add(it.govpay.orm.Dominio.model().NDP_STATO);
			lst.add(it.govpay.orm.Dominio.model().NDP_OPERAZIONE);
			lst.add(it.govpay.orm.Dominio.model().NDP_DESCRIZIONE);
			lst.add(it.govpay.orm.Dominio.model().NDP_DATA);
			
			IPaginatedExpression expr = this.getDominioService().newPaginatedExpression();
			DominioFieldConverter converter = new DominioFieldConverter(this.getJdbcProperties().getDatabase());
			expr.equals(new CustomField("id",  Long.class, "id", converter.toTable(it.govpay.orm.Dominio.model())), idDominio);
			List<Map<String,Object>> select = this.getDominioService().select(expr, lst.toArray(new IField[]{}));
			if(select == null || select.size() <= 0) {
				throw new NotFoundException("Id dominio ["+idDominio+"]");
			}
			
			if(select.size() > 1) {
				throw new MultipleResultException("Id dominio ["+idDominio+"]");
			}
			
			StatoNdP stato = new StatoNdP();
			
			Object oStato = select.get(0).get(it.govpay.orm.Dominio.model().NDP_STATO.getFieldName());
			if(oStato instanceof Integer)
				stato.setCodice((Integer) oStato);
			
			Object oDescrizione = select.get(0).get(it.govpay.orm.Dominio.model().NDP_DESCRIZIONE.getFieldName());
			if(oDescrizione instanceof String)
				stato.setDescrizione((String) oDescrizione);
			
			Object oOperazione = select.get(0).get(it.govpay.orm.Dominio.model().NDP_OPERAZIONE.getFieldName());
			if(oOperazione instanceof String)
				stato.setOperazione((String) oOperazione);
			
			Object oData = select.get(0).get(it.govpay.orm.Dominio.model().NDP_DATA.getFieldName());
			if(oData instanceof Date)
				stato.setData((Date) oData);
			
			return stato;
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	
	/**
	 * Recupera gli ibanAccredito per un idDominio (join tra dominio, ente, tributo, ibanAccredito)
	 * 
	 * @param idIbanAccredito
	 * @return
	 * @throws NotFoundException se non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public List<IbanAccredito> getIbanAccreditoByIdDominio(long idDominio) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getIbanAccreditoService().newPaginatedExpression();
			IbanAccreditoFieldConverter converter = new IbanAccreditoFieldConverter(this.getJdbcProperties().getDatabase());
			exp.equals(new CustomField("id_dominio",  Long.class, "id_dominio", converter.toTable(it.govpay.orm.IbanAccredito.model())), idDominio);
			List<it.govpay.orm.IbanAccredito> lstIban = this.getIbanAccreditoService().findAll(exp);
			
			return IbanAccreditoConverter.toDTOList(lstIban);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}

	}

	
	public DominioFilter newFilter() throws ServiceException {
		return new DominioFilter(this.getDominioService());
	}
	
	public DominioFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new DominioFilter(this.getDominioService(),simpleSearch);
	}

	public long count(IFilter filter) throws ServiceException {
		try {
			return this.getDominioService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Dominio> findAll(IFilter filter) throws ServiceException {
		try {
			return DominioConverter.toDTOList(this.getDominioService().findAll(filter.toPaginatedExpression()), this);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Ritorna la lista di tutti i domini censiti
	 * @return
	 * @throws ServiceException 
	 */
	public List<Dominio> getDomini() throws ServiceException {
		return this.findAll(this.newFilter());
	}

}
