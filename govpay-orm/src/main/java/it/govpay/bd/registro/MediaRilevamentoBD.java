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
import it.govpay.bd.model.MediaRilevamento;
import it.govpay.bd.model.converter.MediaRilevamentoConverter;
import it.govpay.orm.dao.jdbc.JDBCMediaRilevamentoServiceSearch;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class MediaRilevamentoBD extends BasicBD {

	public MediaRilevamentoBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera lo MediaRilevamento identificato dalla chiave fisica
	 * 
	 * @param idMediaRilevamento
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public MediaRilevamento getMediaRilevamento(long idMediaRilevamento) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			return MediaRilevamentoConverter.toDTO(((JDBCMediaRilevamentoServiceSearch)this.getServiceManager().getMediaRilevamentoServiceSearch()).get(idMediaRilevamento));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Inserisce un nuovo MediaRilevamento
	 * 
	 * @param mediaRilevamento
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void insertMediaRilevamento(MediaRilevamento mediaRilevamento) throws ServiceException, NotFoundException {
		try {
			
			it.govpay.orm.MediaRilevamento vo = MediaRilevamentoConverter.toVO(mediaRilevamento);
			
			this.getServiceManager().getMediaRilevamentoService().create(vo);
			mediaRilevamento.setId(vo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
}
