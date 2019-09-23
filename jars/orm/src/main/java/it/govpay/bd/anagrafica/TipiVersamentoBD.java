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

import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.TipoVersamentoFilter;
import it.govpay.bd.model.converter.TipoVersamentoConverter;
import it.govpay.model.TipoVersamento;
import it.govpay.model.TipoVersamento.Tipo;
import it.govpay.orm.IdTipoVersamento;
import it.govpay.orm.dao.jdbc.JDBCTipoVersamentoServiceSearch;

public class TipiVersamentoBD extends BasicBD {

	public TipiVersamentoBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera il tipoVersamento identificato dalla chiave fisica
	 * 
	 * @param idTipoVersamento
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public TipoVersamento getTipoVersamento(Long idTipoVersamento) throws NotFoundException, MultipleResultException, ServiceException {
		if(idTipoVersamento == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idTipoVersamento.longValue();

		try {
			return TipoVersamentoConverter.toDTO(((JDBCTipoVersamentoServiceSearch)this.getTipoVersamentoService()).get(id));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}


	/**
	 * Recupera il tipoVersamento identificato dal codice
	 * 
	 * @param codTipoVersamento
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public TipoVersamento getTipoVersamento(String codTipoVersamento) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IdTipoVersamento idTipoVersamento = new IdTipoVersamento();
			idTipoVersamento.setCodTipoVersamento(codTipoVersamento);
			return TipoVersamentoConverter.toDTO( this.getTipoVersamentoService().get(idTipoVersamento));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Aggiorna il tipoVersamento
	 * 
	 * @param tipoVersamento
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void updateTipoVersamento(TipoVersamento tipoVersamento) throws NotFoundException, ServiceException {
		try {
			it.govpay.orm.TipoVersamento vo = TipoVersamentoConverter.toVO(tipoVersamento);
			IdTipoVersamento idVO = this.getTipoVersamentoService().convertToId(vo);
			if(!this.getTipoVersamentoService().exists(idVO)) {
				throw new NotFoundException("TipoVersamento con id ["+idVO.toJson()+"] non trovato.");
			}
			this.getTipoVersamentoService().update(idVO, vo);
			tipoVersamento.setId(vo.getId());
			this.emitAudit(tipoVersamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * Crea un nuovo tipoVersamento
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public TipoVersamento autoCensimentoTipoVersamento(String codTipoVersamento) throws ServiceException {
		TipoVersamento tipoVersamento = new TipoVersamento();
		tipoVersamento.setCodTipoVersamento(codTipoVersamento);
		tipoVersamento.setDescrizione(codTipoVersamento);
		tipoVersamento.setAbilitatoDefault(true);
		tipoVersamento.setCodificaIuvDefault(null);
		tipoVersamento.setPagaTerziDefault(false);
		tipoVersamento.setTipoDefault(Tipo.DOVUTO);
		this.insertTipoVersamento(tipoVersamento);
		return tipoVersamento;
	}

	/**
	 * Crea un nuovo tipoVersamento
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void insertTipoVersamento(TipoVersamento tipoVersamento) throws ServiceException {
		try {
			it.govpay.orm.TipoVersamento vo = TipoVersamentoConverter.toVO(tipoVersamento);
			this.getTipoVersamentoService().create(vo);
			tipoVersamento.setId(vo.getId());
			this.emitAudit(tipoVersamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public TipoVersamentoFilter newFilter() throws ServiceException {
		return new TipoVersamentoFilter(this.getTipoVersamentoService());
	}

	public TipoVersamentoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new TipoVersamentoFilter(this.getTipoVersamentoService(),simpleSearch);
	}

	public long count(TipoVersamentoFilter filter) throws ServiceException {
		try {
			return this.getTipoVersamentoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<TipoVersamento> findAll(TipoVersamentoFilter filter) throws ServiceException {
		try {
			return TipoVersamentoConverter.toDTOList(this.getTipoVersamentoService().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
}
