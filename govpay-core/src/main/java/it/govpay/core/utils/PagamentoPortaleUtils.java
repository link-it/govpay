package it.govpay.core.utils;

import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoRpt;

public class PagamentoPortaleUtils {

	public static void aggiornaPagamentoPortale(Long idPagamentoPortale, BasicBD bd) throws ServiceException {
		PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(bd);
		try {
			PagamentoPortale pagamentoPortale = pagamentiPortaleBD.getPagamento(idPagamentoPortale);
			// disabilito la select for update
			bd.disableSelectForUpdate();

			RptBD rptBD = new RptBD(bd);
			RptFilter filter = rptBD.newFilter();
			filter.setIdPagamentoPortale(idPagamentoPortale);
			
			List<Rpt> findAll = rptBD.findAll(filter);
			boolean updateStato = true;
			int numeroEseguiti = 0;
			int numeroNonEseguiti = 0;
			//int numeroResidui = 0;
			for (int i = 0; i <findAll.size(); i++) {
				Rpt rpt  = findAll.get(i);
				if(rpt.getEsitoPagamento() == null) {
					updateStato = false;
					break;
				}
				StatoRpt stato = rpt.getStato();
				
				if(rpt.getEsitoPagamento().equals(EsitoPagamento.PAGAMENTO_ESEGUITO)) {
					numeroEseguiti ++;
				} else if(rpt.getEsitoPagamento().equals(EsitoPagamento.PAGAMENTO_NON_ESEGUITO) || rpt.getEsitoPagamento().equals(EsitoPagamento.DECORRENZA_TERMINI) || !stato.equals(StatoRpt.RT_ACCETTATA_PA)) {
					numeroNonEseguiti ++;
				} else {
					//numeroResidui ++;
				}
			}
			
			if(updateStato) {
				if(numeroEseguiti == findAll.size()) {
					pagamentoPortale.setStato(STATO.PAGAMENTO_ESEGUITO);
				} else if(numeroNonEseguiti == findAll.size()) {
					pagamentoPortale.setStato(STATO.PAGAMENTO_NON_ESEGUITO);
				} else{
					pagamentoPortale.setStato(STATO.PAGAMENTO_PARZIALMENTE_ESEGUITO); 
				}
				
				 
			} else {
				pagamentoPortale.setStato(STATO.PAGAMENTO_IN_ATTESA_DI_ESITO);
			}
			pagamentiPortaleBD.updatePagamento(pagamentoPortale);
		} catch (NotFoundException e) {
		}
	}
}
