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

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.StazioneFilter;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.converter.StazioneConverter;
import it.govpay.bd.wrapper.StatoNdP;
import it.govpay.orm.IdStazione;
import it.govpay.orm.dao.jdbc.JDBCStazioneService;
import it.govpay.orm.dao.jdbc.JDBCStazioneServiceSearch;
import it.govpay.orm.dao.jdbc.converter.StazioneFieldConverter;

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
import org.openspcoop2.utils.UtilsException;

public class StazioniBD extends BasicBD {
	
	public StazioniBD(BasicBD basicBD) {
		super(basicBD);
		
	}

	public void insertStazione(Stazione stazione) throws ServiceException {
		try {

			it.govpay.orm.Stazione vo = StazioneConverter.toVO(stazione);

			this.getStazioneService().create(vo);
			stazione.setId(vo.getId());			
			emitAudit(stazione);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateStazione(Stazione stazione) throws ServiceException, NotFoundException {

		try {
			it.govpay.orm.Stazione vo = StazioneConverter.toVO(stazione);
			IdStazione idStazione = this.getStazioneService().convertToId(vo);
			
			if(!this.getStazioneService().exists(idStazione)) {
				throw new NotFoundException("IdStazione con id ["+idStazione.toJson()+"] non trovata");
			}
			
			this.getStazioneService().update(idStazione, vo);
			stazione.setId(vo.getId());
			emitAudit(stazione);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		}
	}

	
	public void setStatoNdp(long idStazione, StatoNdP statoNdp) throws NotFoundException, ServiceException{
		this.setStatoNdp(idStazione, statoNdp.getCodice(), statoNdp.getOperazione(), statoNdp.getDescrizione());
	}
	
	public void setStatoNdp(long idStazione, Integer codice, String operazione, String descrizione) throws NotFoundException, ServiceException{
		try {
			
			List<UpdateField> lst = new ArrayList<UpdateField>();
			lst.add(new UpdateField(it.govpay.orm.Stazione.model().NDP_STATO, codice));
			lst.add(new UpdateField(it.govpay.orm.Stazione.model().NDP_OPERAZIONE, operazione));
			lst.add(new UpdateField(it.govpay.orm.Stazione.model().NDP_DESCRIZIONE, descrizione));
			lst.add(new UpdateField(it.govpay.orm.Stazione.model().NDP_DATA, new Date()));
			
			((JDBCStazioneService)this.getStazioneService()).updateFields(idStazione, lst.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public StatoNdP getStatoNdp(long idStazione) throws NotFoundException, ServiceException {
		try {
			
			List<IField> lst = new ArrayList<IField>();
			lst.add(it.govpay.orm.Stazione.model().NDP_STATO);
			lst.add(it.govpay.orm.Stazione.model().NDP_OPERAZIONE);
			lst.add(it.govpay.orm.Stazione.model().NDP_DESCRIZIONE);
			lst.add(it.govpay.orm.Stazione.model().NDP_DATA);

			IPaginatedExpression expr = this.getStazioneService().newPaginatedExpression();
			StazioneFieldConverter converter = new StazioneFieldConverter(this.getJdbcProperties().getDatabase());
			expr.equals(new CustomField("id",  Long.class, "id", converter.toTable(it.govpay.orm.Stazione.model())), idStazione);
			List<Map<String,Object>> select = this.getStazioneService().select(expr, lst.toArray(new IField[]{}));
			if(select == null || select.size() <= 0) {
				throw new NotFoundException("Id Stazione ["+idStazione+"]");
			}
			
			if(select.size() > 1) {
				throw new MultipleResultException("Id Stazione ["+idStazione+"]");
			}
			
			StatoNdP stato = new StatoNdP();
			
			Object oStato = select.get(0).get(it.govpay.orm.Stazione.model().NDP_STATO.getFieldName());
			if(oStato instanceof Integer)
				stato.setCodice((Integer) oStato);
			
			Object oDescrizione = select.get(0).get(it.govpay.orm.Stazione.model().NDP_DESCRIZIONE.getFieldName());
			if(oDescrizione instanceof String)
				stato.setDescrizione((String) oDescrizione);
			
			Object oOperazione = select.get(0).get(it.govpay.orm.Stazione.model().NDP_OPERAZIONE.getFieldName());
			if(oOperazione instanceof String)
				stato.setOperazione((String) oOperazione);
			
			Object oData = select.get(0).get(it.govpay.orm.Stazione.model().NDP_DATA.getFieldName());
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

	public Stazione getStazione(Long idStazione) throws ServiceException, NotFoundException, MultipleResultException {
		
		if(idStazione== null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		long id = idStazione.longValue();
		try {
			it.govpay.orm.Stazione stazioneVO = ((JDBCStazioneServiceSearch)this.getStazioneService()).get(id);
			Stazione stazione = StazioneConverter.toDTO(stazioneVO);
			return stazione;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}

	public Stazione getStazione(String codStazione) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			IdStazione idStazione = new IdStazione();
			idStazione.setCodStazione(codStazione);

			it.govpay.orm.Stazione stazioneVO = this.getStazioneService().get(idStazione);
			Stazione stazione = StazioneConverter.toDTO(stazioneVO);
			return stazione;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
	public StazioneFilter newFilter() throws ServiceException {
		return new StazioneFilter(this.getStazioneService());
	}

	public long count(StazioneFilter filter) throws ServiceException {
		try {
			return this.getStazioneService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Stazione> findAll(StazioneFilter filter) throws ServiceException {
		try {
			return StazioneConverter.toDTOList(this.getStazioneService().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	// Ritorna tutte le stazioni afferenti all'intermediario indicato
	public List<Stazione> getStazioni(long idIntermediario) throws ServiceException {
		try {
			StazioneFieldConverter fieldConverter = new StazioneFieldConverter(this.getJdbcProperties().getDatabaseType());

			IPaginatedExpression exp = this.getStazioneService().newPaginatedExpression();
			exp.equals(new CustomField("id_intermediario", Long.class, "id_intermediario", fieldConverter.toTable(it.govpay.orm.Stazione.model())), idIntermediario);
			return StazioneConverter.toDTOList(this.getStazioneService().findAll(exp));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public List<Stazione> getStazioni() throws ServiceException {
		return this.findAll(this.newFilter());
	}

}
