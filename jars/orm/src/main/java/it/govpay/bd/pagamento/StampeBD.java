/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.model.converter.StampaConverter;
import it.govpay.model.Stampa;
import it.govpay.orm.IdDocumento;
import it.govpay.orm.IdStampa;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.dao.jdbc.JDBCStampaServiceSearch;
import it.govpay.orm.dao.jdbc.converter.StampaFieldConverter;

public class StampeBD extends BasicBD{

	public StampeBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public StampeBD(String idTransaction) {
		super(idTransaction);
	}
	
	public StampeBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public StampeBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	
	public Stampa getStampa(long id) throws ServiceException , NotFoundException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Stampa stampaVO = ((JDBCStampaServiceSearch)this.getStampaService()).get(id);
			return StampaConverter.toDTO(stampaVO);
		} catch (NotImplementedException | MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public Stampa getAvvisoVersamento(long idVersamento) throws ServiceException, NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			StampaFieldConverter converter = new StampaFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			
			IExpression exp = this.getStampaService().newExpression();
			exp.equals(it.govpay.orm.Stampa.model().TIPO, Stampa.TIPO.AVVISO.toString());
			exp.and();
			exp.equals(new CustomField("id_versamento",  Long.class, "id_versamento",converter.toTable(it.govpay.orm.Stampa.model())), idVersamento);
			
			it.govpay.orm.Stampa stampaVO = this.getStampaService().find(exp);
			return StampaConverter.toDTO(stampaVO);
		} catch (NotImplementedException | MultipleResultException |ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public Stampa getAvvisoDocumento(long idDocumento) throws ServiceException, NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			StampaFieldConverter converter = new StampaFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			
			IExpression exp = this.getStampaService().newExpression();
			exp.equals(it.govpay.orm.Stampa.model().TIPO, Stampa.TIPO.AVVISO.toString());
			exp.and();
			exp.equals(new CustomField("id_documento",  Long.class, "id_documento",converter.toTable(it.govpay.orm.Stampa.model())), idDocumento);
			
			it.govpay.orm.Stampa stampaVO = this.getStampaService().find(exp);
			return StampaConverter.toDTO(stampaVO);
		} catch (NotImplementedException | MultipleResultException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void cancellaAvviso(long idVersamento) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdStampa idStampa = new IdStampa();
			idStampa.setTipo(Stampa.TIPO.AVVISO.toString());
			IdVersamento idVersamentoObj = new IdVersamento();
			idVersamentoObj.setId(idVersamento);
			idStampa.setIdVersamento(idVersamentoObj);
			
			this.getStampaService().deleteById(idStampa);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void cancellaAvvisoDocumento(long idDocumento) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdStampa idStampa = new IdStampa();
			idStampa.setTipo(Stampa.TIPO.AVVISO.toString());
			IdDocumento idDocumentoObj = new IdDocumento();
			idDocumentoObj.setId(idDocumento);
			idStampa.setIdDocumento(idDocumentoObj);
			
			this.getStampaService().deleteById(idStampa);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void insertStampa(Stampa stampa) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Stampa vo = StampaConverter.toVO(stampa);
			this.getStampaService().create(vo);
			stampa.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void updatePdfStampa(Stampa stampa) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Stampa vo = StampaConverter.toVO(stampa);
			IdStampa idStampa = this.getStampaService().convertToId(vo);
			List<UpdateField> lstUpdateFields = new ArrayList<>();
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Stampa.model().DATA_CREAZIONE, stampa.getDataCreazione()));
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Stampa.model().PDF, stampa.getPdf()));
			
			this.getStampaService().updateFields(idStampa, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException | NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		} 
	}
}
