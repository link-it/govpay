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
import it.govpay.bd.model.converter.BatchConverter;
import it.govpay.model.Batch;
import it.govpay.orm.IdBatch;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class BatchBD extends BasicBD {

	public BatchBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public void insert(Batch batch) throws ServiceException {
		try{
			it.govpay.orm.Batch batchVO = BatchConverter.toVO(batch);
			this.getBatchService().create(batchVO);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public void update(Batch batch) throws ServiceException, NotFoundException {
		try{
			it.govpay.orm.Batch batchVO = BatchConverter.toVO(batch);
			IdBatch id = this.getBatchService().convertToId(batchVO);
			this.getBatchService().update(id, batchVO);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} 
	}
	
	public Batch get(String codBatch) throws ServiceException, NotFoundException {
		try{
			IdBatch id = new IdBatch();
			id.setCodBatch(codBatch);
			return BatchConverter.toDTO(this.getBatchService().get(id));
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
}
