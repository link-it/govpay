package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Utenza;
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
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;

public class RiscossioniDAO extends BaseDAO{

	public RiscossioniDAO() {
	}

	public ListaRiscossioniDTOResponse listaRiscossioni(ListaRiscossioniDTO listaRiscossioniDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException{
		List<String> listaDominiFiltro = null;
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(listaRiscossioniDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

			// Autorizzazione sui domini
			listaDominiFiltro = AclEngine.getDominiAutorizzati((Utenza) listaRiscossioniDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
			if(listaDominiFiltro == null) {
				throw new NotAuthorizedException("L'utenza autenticata ["+listaRiscossioniDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
			}

			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			PagamentoFilter filter = pagamentiBD.newFilter();

			filter.setOffset(listaRiscossioniDTO.getOffset());
			filter.setLimit(listaRiscossioniDTO.getLimit());
			if(listaRiscossioniDTO.getIdDominio() != null) {
				listaDominiFiltro.add(listaRiscossioniDTO.getIdDominio());
			}
			if(listaDominiFiltro != null && listaDominiFiltro.size() > 0) {
				filter.setIdDomini(listaDominiFiltro);
			}

			filter.setDataInizio(listaRiscossioniDTO.getDataRiscossioneDa());
			filter.setDataFine(listaRiscossioniDTO.getDataRiscossioneA());
			filter.setTipo(listaRiscossioniDTO.getTipo());
			filter.setIdA2A(listaRiscossioniDTO.getIdA2A());
			filter.setCodSingoloVersamentoEnte(listaRiscossioniDTO.getIdPendenza());
			if(listaRiscossioniDTO.getStato() != null)
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
			if(bd != null)
				bd.closeConnection();
		}
	}

	public LeggiRiscossioneDTOResponse leggiRiscossione(LeggiRiscossioneDTO leggiRiscossioniDTO) throws ServiceException,RiscossioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiRiscossioneDTOResponse response = new LeggiRiscossioneDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(leggiRiscossioniDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			Pagamento flussoPagamento = pagamentiBD.getPagamento(leggiRiscossioniDTO.getIdDominio(), leggiRiscossioniDTO.getIuv(), leggiRiscossioniDTO.getIur(), leggiRiscossioniDTO.getIndice());

			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(leggiRiscossioniDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, flussoPagamento.getDominio(bd).getCodDominio(), null, bd);

			populatePagamento(flussoPagamento, bd);
			response.setPagamento(flussoPagamento);

		} catch (NotFoundException e) {
			throw new RiscossioneNonTrovataException(e.getMessage(), e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e.getMessage(), e);
		} finally {
			if(bd != null)
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
