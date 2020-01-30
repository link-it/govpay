package it.govpay.core.utils;

import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.CODICE_STATO;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoRpt;

public class PagamentoPortaleUtils {
	
	private static Logger log = LoggerWrapperFactory.getLogger(PagamentoPortaleUtils.class);

	public static void aggiornaPagamentoPortale(Long idPagamentoPortale, BasicBD bd) throws ServiceException {
		
		try {
			PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(bd);
			pagamentiPortaleBD.enableSelectForUpdate();
			log.debug("Leggo pagamento portale id ["+idPagamentoPortale+"]"); 
			PagamentoPortale pagamentoPortale = pagamentiPortaleBD.getPagamento(idPagamentoPortale);
			// disabilito la select for update
			bd.disableSelectForUpdate();

			RptBD rptBD = new RptBD(bd);
			RptFilter filter = rptBD.newFilter();
			filter.setIdPagamentoPortale(idPagamentoPortale);
			
			List<Rpt> findAll = rptBD.findAll(filter);
			
			log.debug("Trovate  ["+findAll.size()+"] RPT associate"); 
			boolean updateStato = true;
			int numeroEseguiti = 0;
			int numeroNonEseguiti = 0;
			int numeroFalliti = 0;
			//int numeroResidui = 0;
			for (int i = 0; i <findAll.size(); i++) {
				Rpt rpt  = findAll.get(i);
				log.debug("RPT corrente ["+rpt.getId()+"] Stato ["+rpt.getStato()+ "] EsitoPagamento ["+rpt.getEsitoPagamento()+"]");
				StatoRpt stato = rpt.getStato();
				if(it.govpay.model.Rpt.stati_pendenti.contains(stato)) {
//						rpt.getEsitoPagamento() == null) {
					updateStato = false;
					break;
				}
				
				// controllo che l'rpt non si trovi in uno stato di fallimento
				if(stato.equals(StatoRpt.RPT_ERRORE_INVIO_A_PSP) 
					|| stato.equals(StatoRpt.RPT_RIFIUTATA_NODO) 
					|| stato.equals(StatoRpt.RPT_RIFIUTATA_PSP)) {
					numeroFalliti ++;
				}
				else {
					if(rpt.getEsitoPagamento() != null) {
						if(rpt.getEsitoPagamento().equals(EsitoPagamento.PAGAMENTO_ESEGUITO)) {
							numeroEseguiti ++;
						} else if(rpt.getEsitoPagamento().equals(EsitoPagamento.PAGAMENTO_NON_ESEGUITO) 
								|| rpt.getEsitoPagamento().equals(EsitoPagamento.DECORRENZA_TERMINI) || !stato.equals(StatoRpt.RT_ACCETTATA_PA)) {
							numeroNonEseguiti ++;
						}  
					} else {
						 // in corso aspetto che terminino tutte
						updateStato = false;
						break;
					}
				}
			}
			
			log.debug("Esito analisi rpt Update ["+updateStato+"] #OK ["+numeroEseguiti+"], #KO ["+numeroNonEseguiti+"], #Fallite ["+numeroFalliti+"]"); 
			
			if(updateStato) {
				if(numeroFalliti == findAll.size()) {
					pagamentoPortale.setStato(STATO.FALLITO);
					pagamentoPortale.setCodiceStato(CODICE_STATO.PAGAMENTO_FALLITO);
				} else if(numeroEseguiti == findAll.size()) {
					pagamentoPortale.setStato(STATO.ESEGUITO);
					pagamentoPortale.setCodiceStato(CODICE_STATO.PAGAMENTO_ESEGUITO);
				} else if(numeroNonEseguiti == findAll.size()) {
					pagamentoPortale.setStato(STATO.NON_ESEGUITO);
					pagamentoPortale.setCodiceStato(CODICE_STATO.PAGAMENTO_NON_ESEGUITO);
				} else{
					pagamentoPortale.setStato(STATO.ESEGUITO_PARZIALE);
					pagamentoPortale.setCodiceStato(CODICE_STATO.PAGAMENTO_PARZIALMENTE_ESEGUITO); 
				}
				
				 
			} else {
				pagamentoPortale.setStato(STATO.IN_CORSO);
				pagamentoPortale.setCodiceStato(CODICE_STATO.PAGAMENTO_IN_ATTESA_DI_ESITO);
			}
			
			log.debug("Nuovo Stato ["+pagamentoPortale.getStato()+"]"); 
			
			pagamentiPortaleBD.updatePagamento(pagamentoPortale);
			log.debug("Update pagamento portale id ["+idPagamentoPortale+"] completato "); 
		} catch (NotFoundException e) {
		}
	}
}
