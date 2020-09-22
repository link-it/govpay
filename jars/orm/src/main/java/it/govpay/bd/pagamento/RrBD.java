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

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.converter.RrConverter;
import it.govpay.bd.pagamento.filters.RrFilter;
import it.govpay.orm.IdRr;
import it.govpay.orm.RR;
import it.govpay.orm.dao.jdbc.JDBCRRServiceSearch;

public class RrBD extends BasicBD {

	public RrBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RrBD(String idTransaction) {
		super(idTransaction);
	}
	
	public RrBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public RrBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	/**
	 * Recupera l'RR identificato dalla chiave fisica
	 * 
	 * @param idRr
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Rr getRr(long idRr) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			RR rptVO = ((JDBCRRServiceSearch)this.getRrService()).get(idRr);
			return RrConverter.toDTO(rptVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public Rr getRr(String codMsgRevoca) throws NotFoundException, ServiceException {
		return this.getRr(codMsgRevoca, false);
	}

	/**
	 * Recupera l'RR identificato dal msg id
	 * 
	 * @param codMsgRevoca
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Rr getRr(String codMsgRevoca, boolean deep) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getRrService().newExpression();
			exp.equals(RR.model().COD_MSG_REVOCA, codMsgRevoca);
			
			RR rptVO = this.getRrService().find(exp);
			Rr dto = RrConverter.toDTO(rptVO);
			
			if(deep) {
				RptBD rptBD = new RptBD(this);
				rptBD.setAtomica(false); // connessione condivisa
				dto.setRpt(rptBD.getRpt(dto.getIdRpt(), true));
				
				PagamentiBD pagamentiBD = new PagamentiBD(this);
				pagamentiBD.setAtomica(false); // connessione condivisa
				dto.setPagamenti(pagamentiBD.getPagamentiByRr(dto.getId()));
				
			}
			return dto;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	/**
	 * Inserisce l'RR.
	 * 
	 * @param rr
	 * @param documentoXml
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void insertRr(Rr rr) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			RR rrVo = RrConverter.toVO(rr);
			this.getRrService().create(rrVo);
			rr.setId(rrVo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void updateRr(long id, Rr.StatoRr stato, String descrizione) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdRr idRr = new IdRr();
			idRr.setId(id);
			List<UpdateField> lstUpdateFields = new ArrayList<>();
			lstUpdateFields.add(new UpdateField(RR.model().STATO, stato.toString()));
			lstUpdateFields.add(new UpdateField(RR.model().DESCRIZIONE_STATO, descrizione));
			this.getRrService().updateFields(idRr, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void updateRr(Long id, Rr rpt) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.RR vo = RrConverter.toVO(rpt);
			IdRr idRr = this.getRrService().convertToId(vo);
			this.getRrService().update(idRr, vo);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<Rr> getRrPendenti(String codDominio) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IPaginatedExpression exp = this.getRrService().newPaginatedExpression();
			
			exp.equals(RR.model().COD_DOMINIO, codDominio);
			exp.notEquals(RR.model().STATO, Rr.StatoRr.RR_ERRORE_INVIO_A_NODO.toString());
			exp.notEquals(RR.model().STATO, Rr.StatoRr.RR_RIFIUTATA_NODO.toString());
			exp.notEquals(RR.model().STATO, Rr.StatoRr.ER_ACCETTATA_PA.toString());
			
			List<RR> findAll = this.getRrService().findAll(exp);
			return RrConverter.toDTOList(findAll);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public RrFilter newFilter() throws ServiceException {
		return new RrFilter(this.getRrService());
	}
	
	public long count(RrFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			return this.getRrService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<Rr> findAll(RrFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getRrService());
			}
			
			List<Rr> rrLst = new ArrayList<>();
			List<it.govpay.orm.RR> rrVOLst = this.getRrService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.RR rrVO: rrVOLst) {
				rrLst.add(RrConverter.toDTO(rrVO));
			}
			return rrLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
}
