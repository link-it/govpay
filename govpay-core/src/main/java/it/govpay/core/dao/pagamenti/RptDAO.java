package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.RicevutaNonTrovataException;
import it.govpay.core.utils.GpThreadLocal;

public class RptDAO{

	public RptDAO() {
	}

	public LeggiRptDTOResponse leggiRpt(LeggiRptDTO leggiRptDTO) throws ServiceException,RicevutaNonTrovataException{
		LeggiRptDTOResponse response = new LeggiRptDTOResponse();

		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

		try {
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
			bd.closeConnection();
		}
		return response;
	}

	public LeggiRicevutaDTOResponse leggiRpt(LeggiRicevutaDTO leggiRicevutaDTO) throws ServiceException,RicevutaNonTrovataException{
		LeggiRicevutaDTOResponse response = new LeggiRicevutaDTOResponse();

		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

		try {
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
			bd.closeConnection();
		}
		return response;
	}

	public ListaRptDTOResponse listaRpt(ListaRptDTO listaPagamentiPortaleDTO) throws ServiceException,PagamentoPortaleNonTrovatoException{
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

		try {

			RptBD rptBD = new RptBD(bd);
			RptFilter filter = rptBD.newFilter();

			filter.setOffset(listaPagamentiPortaleDTO.getOffset());
			filter.setLimit(listaPagamentiPortaleDTO.getLimit());
			filter.setDataInizio(listaPagamentiPortaleDTO.getDataDa());
			filter.setDataFine(listaPagamentiPortaleDTO.getDataA());
			filter.setStato(listaPagamentiPortaleDTO.getStato());
			filter.setCcp(listaPagamentiPortaleDTO.getCcp());
			filter.setIuv(listaPagamentiPortaleDTO.getIuv());
			if(listaPagamentiPortaleDTO.getIdDominio() != null) {
				List<String> idDomini = new ArrayList<String>();
				idDomini.add(listaPagamentiPortaleDTO.getIdDominio());
				filter.setIdDomini(idDomini );
			}
			filter.setCodPagamentoPortale(listaPagamentiPortaleDTO.getIdPagamento());
			filter.setIdPendenza(listaPagamentiPortaleDTO.getIdPendenza());
			filter.setCodApplicazione(listaPagamentiPortaleDTO.getIdA2A());
			filter.setFilterSortList(listaPagamentiPortaleDTO.getFieldSortList());

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
			bd.closeConnection();
		}
	}
}
