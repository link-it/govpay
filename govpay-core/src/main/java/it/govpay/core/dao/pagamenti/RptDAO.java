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

public class RptDAO extends BasicBD{
	
	public RptDAO(BasicBD basicBD) {
		super(basicBD);
	}
	
	public LeggiRptDTOResponse leggiRpt(LeggiRptDTO leggiRptDTO) throws ServiceException,RicevutaNonTrovataException{
		
		LeggiRptDTOResponse response = new LeggiRptDTOResponse();
		
		RptBD rptBD = new RptBD(this);
		Rpt rpt;
		try {
			rpt = rptBD.getRpt(leggiRptDTO.getIdDominio(), leggiRptDTO.getIuv(), leggiRptDTO.getCcp());
			
			if(rpt.getXmlRt() == null)
				throw new RicevutaNonTrovataException();
			
			response.setRpt(rpt);
			response.setRpt(rpt);
			response.setVersamento(rpt.getVersamento(this));
			response.setApplicazione(rpt.getVersamento(this).getApplicazione(this)); 
			response.setCanale(rpt.getCanale(this));
			response.setPsp(rpt.getPsp(this));
		} catch (NotFoundException e) {
			throw new RicevutaNonTrovataException(e.getMessage(), e);
		}
		return response;
	}

	public LeggiRicevutaDTOResponse leggiRpt(LeggiRicevutaDTO leggiRicevutaDTO) throws ServiceException,RicevutaNonTrovataException{
		
		LeggiRicevutaDTOResponse response = new LeggiRicevutaDTOResponse();
		
		RptBD rptBD = new RptBD(this);
		Rpt rpt;
		try {
			rpt = rptBD.getRpt(leggiRicevutaDTO.getIdDominio(), leggiRicevutaDTO.getIuv(), leggiRicevutaDTO.getCcp());
			
			if(rpt.getXmlRt() == null)
				throw new RicevutaNonTrovataException();
			
			response.setRpt(rpt);
			response.setDominio(rpt.getDominio(this));
			response.setVersamento(rpt.getVersamento(this));
		} catch (NotFoundException e) {
			throw new RicevutaNonTrovataException(e.getMessage(), e);
		}
		return response;
	}
	
	public ListaRptDTOResponse listaRpt(ListaRptDTO listaPagamentiPortaleDTO) throws ServiceException,PagamentoPortaleNonTrovatoException{
		RptBD rptBD = new RptBD(this);
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
				elem.setVersamento(rpt.getVersamento(this));
				elem.setApplicazione(rpt.getVersamento(this).getApplicazione(this)); 
				elem.setCanale(rpt.getCanale(this));
				elem.setPsp(rpt.getPsp(this));
				
				resList.add(elem);
			}
		} 

		return new ListaRptDTOResponse(count, resList);
	}
}
