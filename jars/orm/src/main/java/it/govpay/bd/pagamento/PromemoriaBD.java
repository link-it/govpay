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
package it.govpay.bd.pagamento;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Promemoria;
import it.govpay.bd.model.converter.PromemoriaConverter;
import it.govpay.model.Promemoria.StatoSpedizione;
import it.govpay.orm.dao.jdbc.JDBCPromemoriaService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

public class PromemoriaBD extends BasicBD {

	public PromemoriaBD(BasicBD basicBD) {
		super(basicBD);
	}

	public Promemoria insertPromemoria(Promemoria dto) throws ServiceException {
		it.govpay.orm.Promemoria vo = PromemoriaConverter.toVO(dto);
		try {
			this.getPromemoriaService().create(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
		dto.setId(vo.getId());
		return dto;
	}

	public List<Promemoria> findPromemoriaDaSpedire() throws ServiceException {
		try {
			IPaginatedExpression exp = this.getPromemoriaService().newPaginatedExpression();
			exp.lessThan(it.govpay.orm.Promemoria.model().DATA_PROSSIMA_SPEDIZIONE, new Date());
			exp.equals(it.govpay.orm.Promemoria.model().STATO, Promemoria.StatoSpedizione.DA_SPEDIRE.toString());
			List<it.govpay.orm.Promemoria> findAll = this.getPromemoriaService().findAll(exp);
			return PromemoriaConverter.toDTOList(findAll);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public long countPromemoriaDaSpedire() throws ServiceException {
		try {
			IExpression exp = this.getPromemoriaService().newExpression();
			exp.lessThan(it.govpay.orm.Promemoria.model().DATA_PROSSIMA_SPEDIZIONE, new Date());
			exp.equals(it.govpay.orm.Promemoria.model().STATO, Promemoria.StatoSpedizione.DA_SPEDIRE.toString());
			return this.getPromemoriaService().count(exp).longValue();
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	public long countPromemoriaInAttesa() throws ServiceException {
		try {
			IExpression exp = this.getPromemoriaService().newExpression();
			exp.equals(it.govpay.orm.Promemoria.model().STATO, Promemoria.StatoSpedizione.DA_SPEDIRE.toString());
			return this.getPromemoriaService().count(exp).longValue();
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public void updateSpedito(long id) throws ServiceException {
		this.update(id,  StatoSpedizione.SPEDITO, null, null, null);
	}

	public void updateDaSpedire(Long id, String message, long tentativi, Date prossima) throws ServiceException {
		// Non aggiorno il campo a DA_SPEDIRE. Se lo e' gia' tutto bene, se per concorrenza e' a spedito, non voglio sovrascriverlo. 
		this.update(id, null, message, tentativi, prossima);
	}
	
	public void updateAnnullata(Long id, String message, long tentativi, Date prossima) throws ServiceException {
		this.update(id, StatoSpedizione.ANNULLATO, message, tentativi, prossima);
	}

	private void update(long id, StatoSpedizione stato, String descrizione, Long tentativi, Date prossimaSpedizione) throws ServiceException {
		try {
//			IdPromemoria idVO = ((JDBCPromemoriaServiceSearch)this.getPromemoriaService()).findId(id, true);
			List<UpdateField> lstUpdateFields = new ArrayList<>();
			if(stato != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().STATO, stato.toString()));
//			if(descrizione != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().DESCRIZIONE_STATO, descrizione));
			if(tentativi != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().TENTATIVI_SPEDIZIONE, tentativi));
			if(prossimaSpedizione != null) 
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().DATA_PROSSIMA_SPEDIZIONE, prossimaSpedizione));
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Promemoria.model().DATA_AGGIORNAMENTO_STATO, new Date()));

			((JDBCPromemoriaService)this.getPromemoriaService()).updateFields(id, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
}
