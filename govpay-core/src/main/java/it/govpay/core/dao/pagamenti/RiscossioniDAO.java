package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.filters.PagamentoFilter;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RiscossioneNonTrovataException;
import it.govpay.core.utils.GpThreadLocal;

public class RiscossioniDAO{

	public RiscossioniDAO() {
	}

	public ListaRiscossioniDTOResponse listaRiscossioni(ListaRiscossioniDTO listaRiscossioniDTO) throws ServiceException{
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

		try {
			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			PagamentoFilter filter = pagamentiBD.newFilter();

			filter.setOffset(listaRiscossioniDTO.getOffset());
			filter.setLimit(listaRiscossioniDTO.getLimit());
			filter.setIdDomini(Arrays.asList(listaRiscossioniDTO.getIdDominio()));

			filter.setDataInizio(listaRiscossioniDTO.getDataRiscossioneDa());
			filter.setDataFine(listaRiscossioniDTO.getDataRiscossioneA());
			filter.setTipo(listaRiscossioniDTO.getTipo());
			filter.setIdA2A(listaRiscossioniDTO.getIdA2A());
			filter.setCodSingoloVersamentoEnte(listaRiscossioniDTO.getIdPendenza());
			filter.setStati(Arrays.asList(listaRiscossioniDTO.getStato().toString()));
			
			filter.setFilterSortList(listaRiscossioniDTO.getFieldSortList());

			long count = pagamentiBD.count(filter);

			List<LeggiRiscossioneDTOResponse> resList = new ArrayList<LeggiRiscossioneDTOResponse>();
			if(count > 0) {
				List<Pagamento> findAll = pagamentiBD.findAll(filter);

				for (Pagamento pagamento: findAll) {
					LeggiRiscossioneDTOResponse elem = new LeggiRiscossioneDTOResponse();
					populatePagamento(pagamento, bd);
					elem.setPagamento(pagamento);
					resList.add(elem);
				}
			} 

			return new ListaRiscossioniDTOResponse(count, resList);
		}finally {
			bd.closeConnection();
		}
	}

	public LeggiRiscossioneDTOResponse leggiRiscossione(LeggiRiscossioneDTO leggiRiscossioniDTO) throws ServiceException,RiscossioneNonTrovataException{
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		LeggiRiscossioneDTOResponse response = new LeggiRiscossioneDTOResponse();

		PagamentiBD pagamentiBD = new PagamentiBD(bd);
		try {

			Pagamento flussoPagamento = pagamentiBD.getPagamento(leggiRiscossioniDTO.getIdDominio(), leggiRiscossioniDTO.getIuv(), leggiRiscossioniDTO.getIur(), leggiRiscossioniDTO.getIndice());
			populatePagamento(flussoPagamento, bd);
			response.setPagamento(flussoPagamento);

		} catch (NotFoundException e) {
			throw new RiscossioneNonTrovataException(e.getMessage(), e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e.getMessage(), e);
		} finally {
			bd.closeConnection();
		}
		return response;
	}

	private void populatePagamento(Pagamento pagamento, BasicBD bd)
			throws ServiceException {
			pagamento.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd);
			pagamento.getRpt(bd);
	}
	

}
