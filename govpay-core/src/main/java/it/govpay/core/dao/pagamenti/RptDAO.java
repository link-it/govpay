package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.RicevutaNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;

public class RptDAO extends BaseDAO{

	public RptDAO() {
	}

	public LeggiRptDTOResponse leggiRpt(LeggiRptDTO leggiRptDTO) throws ServiceException,RicevutaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiRptDTOResponse response = new LeggiRptDTOResponse();

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(leggiRptDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, leggiRptDTO.getIdDominio(), null, bd);

			RptBD rptBD = new RptBD(bd);
			Rpt	rpt = rptBD.getRpt(leggiRptDTO.getIdDominio(), leggiRptDTO.getIuv(), leggiRptDTO.getCcp());

			if(rpt.getXmlRt() == null)
				throw new RicevutaNonTrovataException(null);

			response.setRpt(rpt);
			response.setVersamento(rpt.getVersamento(bd));
			response.setApplicazione(rpt.getVersamento(bd).getApplicazione(bd)); 
			response.setCanale(rpt.getCanale(bd));
			response.setPsp(rpt.getPsp(bd));
		} catch (NotFoundException e) {
			throw new RicevutaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

	public LeggiRicevutaDTOResponse leggiRt(LeggiRicevutaDTO leggiRicevutaDTO) throws ServiceException,RicevutaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiRicevutaDTOResponse response = new LeggiRicevutaDTOResponse();

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(leggiRicevutaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, leggiRicevutaDTO.getIdDominio(), null, bd);

			RptBD rptBD = new RptBD(bd);
			Rpt rpt = rptBD.getRpt(leggiRicevutaDTO.getIdDominio(), leggiRicevutaDTO.getIuv(), leggiRicevutaDTO.getCcp());

			if(rpt.getXmlRt() == null)
				throw new RicevutaNonTrovataException(null);

			response.setRpt(rpt);
			response.setDominio(rpt.getDominio(bd));
			response.setVersamento(rpt.getVersamento(bd));
		} catch (NotFoundException e) {
			throw new RicevutaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

	public ListaRptDTOResponse listaRpt(ListaRptDTO listaRptDTO) throws ServiceException,PagamentoPortaleNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		List<String> listaDominiFiltro = null;
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			this.autorizzaRichiesta(listaRptDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

			// Autorizzazione sui domini
			listaDominiFiltro = AclEngine.getDominiAutorizzati((Utenza) listaRptDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
			if(listaDominiFiltro == null) {
				throw new NotAuthorizedException("L'utenza autenticata ["+listaRptDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
			}

			RptBD rptBD = new RptBD(bd);
			RptFilter filter = rptBD.newFilter();

			filter.setOffset(listaRptDTO.getOffset());
			filter.setLimit(listaRptDTO.getLimit());
			filter.setDataInizio(listaRptDTO.getDataDa());
			filter.setDataFine(listaRptDTO.getDataA());
			filter.setStato(listaRptDTO.getStato());
			filter.setCcp(listaRptDTO.getCcp());
			filter.setIuv(listaRptDTO.getIuv());
			if(listaRptDTO.getIdDominio() != null) {
				listaDominiFiltro.add(listaRptDTO.getIdDominio());
			}

			if(listaDominiFiltro != null && listaDominiFiltro.size() > 0) {
				filter.setIdDomini(listaDominiFiltro);
			}

			filter.setCodPagamentoPortale(listaRptDTO.getIdPagamento());
			filter.setIdPendenza(listaRptDTO.getIdPendenza());
			filter.setCodApplicazione(listaRptDTO.getIdA2A());
			filter.setFilterSortList(listaRptDTO.getFieldSortList());

			long count = rptBD.count(filter);

			List<LeggiRptDTOResponse> resList = new ArrayList<LeggiRptDTOResponse>();
			if(count > 0) {
				List<Rpt> findAll = rptBD.findAll(filter);

				for (Rpt rpt : findAll) {
					LeggiRptDTOResponse elem = new LeggiRptDTOResponse();
					elem.setRpt(rpt);
					elem.setVersamento(rpt.getVersamento(bd));
					elem.setApplicazione(rpt.getVersamento(bd).getApplicazione(bd)); 
					elem.setCanale(rpt.getCanale(bd));
					elem.setPsp(rpt.getPsp(bd));

					resList.add(elem);
				}
			} 

			return new ListaRptDTOResponse(count, resList);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
}
