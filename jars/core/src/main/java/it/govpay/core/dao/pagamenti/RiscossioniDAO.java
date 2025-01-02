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
package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RiscossioneNonTrovataException;

public class RiscossioniDAO extends BaseDAO{

	public RiscossioniDAO() {
		super();
	}

	public ListaRiscossioniDTOResponse listaRiscossioni(ListaRiscossioniDTO listaRiscossioniDTO) throws ServiceException {
		it.govpay.bd.viste.PagamentiBD pagamentiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			pagamentiBD = new it.govpay.bd.viste.PagamentiBD(configWrapper);
			
			pagamentiBD.setupConnection(configWrapper.getTransactionID());
			
			pagamentiBD.setAtomica(false); 
			
			it.govpay.bd.viste.filters.PagamentoFilter filter = pagamentiBD.newFilter();
			
			filter.setEseguiCountConLimit(listaRiscossioniDTO.isEseguiCountConLimit());
			filter.setOffset(listaRiscossioniDTO.getOffset());
			filter.setLimit(listaRiscossioniDTO.getLimit());
			if(listaRiscossioniDTO.getIdDominio() != null) {
				filter.setCodDominio(listaRiscossioniDTO.getIdDominio());
			}
			filter.setIdDomini(listaRiscossioniDTO.getCodDomini());
			filter.setDataInizio(listaRiscossioniDTO.getDataRiscossioneDa());
			filter.setDataFine(listaRiscossioniDTO.getDataRiscossioneA());
			filter.setTipo(listaRiscossioniDTO.getTipo()); 
			filter.setIdA2A(listaRiscossioniDTO.getIdA2A());
			filter.setCodVersamentoEnte(listaRiscossioniDTO.getIdPendenza());
			if(listaRiscossioniDTO.getStato() != null)
				filter.setStati(Arrays.asList(listaRiscossioniDTO.getStato())); 
			filter.setIuv(listaRiscossioniDTO.getIuv());
			filter.setFilterSortList(listaRiscossioniDTO.getFieldSortList());
			filter.setDivisione(listaRiscossioniDTO.getDivisione());
			filter.setDirezione(listaRiscossioniDTO.getDirezione());
			filter.setTassonomia(listaRiscossioniDTO.getTassonomia());
			filter.setIdTipoPendenza(listaRiscossioniDTO.getIdTipoPendenza());
			filter.setIdUnita(listaRiscossioniDTO.getIdUnita());
			filter.setIur(listaRiscossioniDTO.getIur());

			Long count = null;
			
			if(listaRiscossioniDTO.isEseguiCount()) {
				 count = pagamentiBD.count(filter);
			}

			List<it.govpay.bd.viste.model.Pagamento> resList = new ArrayList<>();
			if(listaRiscossioniDTO.isEseguiFindAll()) {
				if(listaRiscossioniDTO.isDeep()) {
					List<it.govpay.bd.viste.model.Pagamento> findAll = pagamentiBD.findAll(filter);
					for (it.govpay.bd.viste.model.Pagamento pagamento: findAll) {
						this.populatePagamentoRagioneria(pagamento, pagamentiBD, configWrapper);
						resList.add(pagamento);
					}
				} else {
					resList = pagamentiBD.findAll(filter);
				}
			} 

			return new ListaRiscossioniDTOResponse(count, resList);
		}finally {
			pagamentiBD.closeConnection();
		}
	}

	public LeggiRiscossioneDTOResponse leggiRiscossione(LeggiRiscossioneDTO leggiRiscossioniDTO) throws ServiceException,RiscossioneNonTrovataException {
		LeggiRiscossioneDTOResponse response = new LeggiRiscossioneDTOResponse();
		PagamentiBD pagamentiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		
		try {
			pagamentiBD = new PagamentiBD(configWrapper);
			
			pagamentiBD.setupConnection(configWrapper.getTransactionID());
			
			pagamentiBD.setAtomica(false); 
			
			Pagamento flussoPagamento = pagamentiBD.getPagamento(leggiRiscossioniDTO.getIdDominio(), leggiRiscossioniDTO.getIuv(), leggiRiscossioniDTO.getIur(), leggiRiscossioniDTO.getIndice());

			this.populatePagamento(flussoPagamento, pagamentiBD, configWrapper);
			response.setPagamento(flussoPagamento);
			response.setDominio(flussoPagamento.getDominio(configWrapper));

		} catch (NotFoundException e) {
			throw new RiscossioneNonTrovataException(e.getMessage(), e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e.getMessage(), e);
		} finally {
			pagamentiBD.closeConnection();
		}
		return response;
	}

	private void populatePagamento(Pagamento pagamento, BasicBD bd, BDConfigWrapper configWrapper)
			throws ServiceException {
		
		SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(bd);
		Versamento versamento = singoloVersamento.getVersamentoBD(bd);
		versamento.getApplicazione(configWrapper);
		versamento.getUo(configWrapper);
		versamento.getDominio(configWrapper);
		versamento.getTipoVersamento(configWrapper);
		versamento.getTipoVersamentoDominio(configWrapper); 
		singoloVersamento.getTributo(configWrapper);
		singoloVersamento.getCodContabilita(configWrapper);
		singoloVersamento.getIbanAccredito(configWrapper);
		singoloVersamento.getIbanAppoggio(configWrapper);
		singoloVersamento.getTipoContabilita(configWrapper);
		pagamento.getRpt(bd);
		pagamento.getDominio(configWrapper);
		pagamento.getRendicontazioni(bd);
		pagamento.getIncasso(bd);
	}

	private void populatePagamentoRagioneria(it.govpay.bd.viste.model.Pagamento dto, BasicBD bd, BDConfigWrapper configWrapper)
			throws ServiceException {
		
		Pagamento pagamento = dto.getPagamento();
			
		SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(bd);
		Versamento versamento = singoloVersamento.getVersamentoBD(bd);
		versamento.getApplicazione(configWrapper);
		versamento.getUo(configWrapper);
		versamento.getDominio(configWrapper);
		versamento.getTipoVersamento(configWrapper);
		versamento.getTipoVersamentoDominio(configWrapper); 
		singoloVersamento.getTributo(configWrapper);
		singoloVersamento.getCodContabilita(configWrapper);
		singoloVersamento.getIbanAccredito(configWrapper);
		singoloVersamento.getIbanAppoggio(configWrapper);
		singoloVersamento.getTipoContabilita(configWrapper);
		
		dto.setSingoloVersamento(singoloVersamento);
		dto.setVersamento(versamento);
	}

}
