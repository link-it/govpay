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
import it.govpay.bd.pagamento.filters.PagamentoFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RiscossioneNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;

public class RiscossioniDAO extends BaseDAO{

	public RiscossioniDAO() {
	}

	public ListaRiscossioniDTOResponse listaRiscossioni(ListaRiscossioniDTO listaRiscossioniDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException, NotFoundException{
		PagamentiBD pagamentiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			pagamentiBD = new PagamentiBD(configWrapper);
			
			pagamentiBD.setupConnection(configWrapper.getTransactionID());
			
			pagamentiBD.setAtomica(false); 
			
			PagamentoFilter filter = pagamentiBD.newFilter();
			
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

			Long count = null;
			
			if(listaRiscossioniDTO.isEseguiCount()) {
				 count = pagamentiBD.count(filter);
			}

			List<LeggiRiscossioneDTOResponse> resList = new ArrayList<>();
			if(listaRiscossioniDTO.isEseguiFindAll()) {
				List<Pagamento> findAll = pagamentiBD.findAll(filter);

				for (Pagamento pagamento: findAll) {
					LeggiRiscossioneDTOResponse elem = new LeggiRiscossioneDTOResponse();
					this.populatePagamento(pagamento, pagamentiBD, configWrapper);
					elem.setPagamento(pagamento);
					resList.add(elem);
				}
			} 

			return new ListaRiscossioniDTOResponse(count, resList);
		}finally {
			if(pagamentiBD != null)
				pagamentiBD.closeConnection();
		}
	}

	public LeggiRiscossioneDTOResponse leggiRiscossione(LeggiRiscossioneDTO leggiRiscossioniDTO) throws ServiceException,RiscossioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
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
			if(pagamentiBD != null)
				pagamentiBD.closeConnection();
		}
		return response;
	}

	private void populatePagamento(Pagamento pagamento, BasicBD bd, BDConfigWrapper configWrapper)
			throws ServiceException, NotFoundException {
		
		SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(bd);
		Versamento versamento = singoloVersamento.getVersamento(bd);
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


}
