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
import it.govpay.bd.model.Sla;
import it.govpay.bd.model.converter.SlaConverter;
import it.govpay.orm.dao.jdbc.JDBCSLAServiceSearch;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class SlaBD extends BasicBD {

	public SlaBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera lo SLA identificato dalla chiave fisica
	 * 
	 * @param idSLA
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Sla getSLA(long idSLA) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			return SlaConverter.toDTO(((JDBCSLAServiceSearch)this.getServiceManager().getSLAServiceSearch()).get(idSLA));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Inserisce un nuovo sla
	 * 
	 * @param sla
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void insertSLA(Sla sla) throws ServiceException, NotFoundException {
		try {
			
			it.govpay.orm.SLA vo = SlaConverter.toVO(sla);
			
			this.getServiceManager().getSLAService().create(vo);
			sla.setId(vo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
}
