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
package it.govpay.bd.registro;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Rilevamento;
import it.govpay.bd.model.converter.RilevamentoConverter;
import it.govpay.orm.dao.jdbc.JDBCRilevamentoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.RilevamentoFieldConverter;

import java.util.Date;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.Function;
import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

public class RilevamentiBD extends BasicBD {

	public RilevamentiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera l'Rilevamento identificato dalla chiave fisica
	 * 
	 * @param idRilevamento
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Rilevamento getRilevamento(long idRilevamento) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			return RilevamentoConverter.toDTO(((JDBCRilevamentoServiceSearch)this.getRilevamentoService()).get(idRilevamento));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public double getMedia(long idSla, Long idApplicazione, Date start, Date end) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IExpression exp = this.getRilevamentoService().newExpression();
			
			RilevamentoFieldConverter fieldConverter = new RilevamentoFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_sla",  Long.class, "id_sla", fieldConverter.toTable(it.govpay.orm.Rilevamento.model())), idSla);
			
			if(idApplicazione != null && idApplicazione.longValue() > 0) {
				exp.equals(it.govpay.orm.Rilevamento.model().ID_APPLICAZIONE, idApplicazione);
			}
			
			exp.greaterEquals(it.govpay.orm.Rilevamento.model().DATA_RILEVAMENTO, start);
			
			if(end != null) {
				exp.lessEquals(it.govpay.orm.Rilevamento.model().DATA_RILEVAMENTO, end);
			}
			
			Object avg = this.getRilevamentoService().aggregate(exp, new FunctionField(it.govpay.orm.Rilevamento.model().DURATA, Function.AVG_DOUBLE, "media"));
			
			return ((Double)avg).doubleValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public long countOverSoglia(long idSla, long soglia, Date start, Date end) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IExpression exp = this.getRilevamentoService().newExpression();
			
			RilevamentoFieldConverter fieldConverter = new RilevamentoFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_sla",  Long.class, "id_sla", fieldConverter.toTable(it.govpay.orm.Rilevamento.model())), idSla);
			
			exp.greaterEquals(it.govpay.orm.Rilevamento.model().DURATA, soglia);
			
			exp.greaterEquals(it.govpay.orm.Rilevamento.model().DATA_RILEVAMENTO, start);
			
			if(end != null) {
				exp.lessEquals(it.govpay.orm.Rilevamento.model().DATA_RILEVAMENTO, end);
			}
			
			
			return this.getRilevamentoService().count(exp).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Inserisce un nuovo evento
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void insertRilevamento(Rilevamento evento) throws ServiceException, NotFoundException {
		try {
			
			it.govpay.orm.Rilevamento vo = RilevamentoConverter.toVO(evento);
			
			this.getRilevamentoService().create(vo);
			evento.setId(vo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
}
