/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
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
	
	private PagamentoPortaleUtils() {}
	
	private static Logger log = LoggerWrapperFactory.getLogger(PagamentoPortaleUtils.class);

	public static void aggiornaPagamentoPortale(Long idPagamentoPortale, Rpt rptCorrente, BasicBD bd) throws ServiceException {
		IContext ctx = ContextThreadLocal.get();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		PagamentiPortaleBD pagamentiPortaleBD = null;
		try {
			if(bd != null) {
				pagamentiPortaleBD = new PagamentiPortaleBD(bd);
				
				pagamentiPortaleBD.setAtomica(false);
				
			} else {
				pagamentiPortaleBD = new PagamentiPortaleBD(configWrapper);
				
				pagamentiPortaleBD.setupConnection(configWrapper.getTransactionID());
				
				pagamentiPortaleBD.setAtomica(false);
				
				pagamentiPortaleBD.enableSelectForUpdate();
			}
			
			log.debug("Leggo pagamento portale id [{}]", idPagamentoPortale); 
			PagamentoPortale pagamentoPortale = pagamentiPortaleBD.getPagamento(idPagamentoPortale);
			
			List<Rpt> findAll = null;

			// Pagamento Modello 1 controllo le altre RPT altrimenti evito una ricerca sul DB e utilizzo la sola RPT passata come parametro
			if(pagamentoPortale.getTipo() == 1) {
				RptBD rptBD = new RptBD(bd);
				rptBD.setAtomica(false); // connessione condivisa
				RptFilter filter = rptBD.newFilter();
				filter.setIdPagamentoPortale(idPagamentoPortale);
				
				findAll = rptBD.findAll(filter);
				
				log.debug("Trovate  [{}] RPT associate",findAll.size()); 
			} else {
				findAll = new ArrayList<>();
				findAll.add(rptCorrente);
				log.debug("Il nuovo stato del pagamento portale verra' deciso utilizzando [{}] RPT associata.",findAll.size()); 
			}
			
			boolean updateStato = true;
			int numeroEseguiti = 0;
			int numeroNonEseguiti = 0;
			int numeroFalliti = 0;
			String descrizioneStato = null;
			for (int i = 0; i <findAll.size(); i++) {
				Rpt rpt  = findAll.get(i);
				log.debug("RPT corrente [{}] Stato [{}] EsitoPagamento [{}]", rpt.getId(), rpt.getStato(), rpt.getEsitoPagamento());
				StatoRpt stato = rpt.getStato();
				if(it.govpay.model.Rpt.getStatiPendenti().contains(stato)) {
					log.debug("RPT corrente [{}] e' in uno stato pendente [{}], il nuovo stato del pagamento sara' 'IN_CORSO'.", rpt.getId(), rpt.getStato());
					updateStato = false;
					break;
				}
				
				// controllo che l'rpt non si trovi in uno stato di fallimento
				if(stato.equals(StatoRpt.RPT_ERRORE_INVIO_A_PSP) 
					|| stato.equals(StatoRpt.RPT_RIFIUTATA_NODO) 
					|| stato.equals(StatoRpt.RPT_RIFIUTATA_PSP)) {
					numeroFalliti ++;
				}
				else if(stato.equals(StatoRpt.RPT_ERRORE_INVIO_A_NODO)) {
					descrizioneStato = "Errore nella spedizione della richiesta di pagamento a pagoPA";
					numeroFalliti ++;
				} else {
					if(rpt.getEsitoPagamento() != null) {
						if(rpt.getEsitoPagamento().equals(EsitoPagamento.PAGAMENTO_ESEGUITO)) {
							numeroEseguiti ++;
						} else if(rpt.getEsitoPagamento().equals(EsitoPagamento.PAGAMENTO_NON_ESEGUITO) 
								|| rpt.getEsitoPagamento().equals(EsitoPagamento.DECORRENZA_TERMINI) || !stato.equals(StatoRpt.RT_ACCETTATA_PA)) {
							numeroNonEseguiti ++;
						}  
					} else {
						log.debug("RPT corrente [{}] in stato non pendente [{}] ma esito pagamento null, il nuovo stato del pagamento sara' 'IN_CORSO'.", rpt.getId(), rpt.getStato());
						 // in corso aspetto che terminino tutte
						updateStato = false;
						break;
					}
				}
			}
			
			log.debug("Esito analisi rpt Update [{}] #OK [{}], #KO [{}], #Fallite [{}]", updateStato, numeroEseguiti, numeroNonEseguiti, numeroFalliti); 
			
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
				pagamentoPortale.setDescrizioneStato(descrizioneStato);
				 
			} else {
				pagamentoPortale.setStato(STATO.IN_CORSO);
				pagamentoPortale.setCodiceStato(CODICE_STATO.PAGAMENTO_IN_ATTESA_DI_ESITO);
			}
			
			log.debug("Update pagamento portale id [{}] nuovo Stato [{}], nuovo CodiceStato [{}]",idPagamentoPortale, pagamentoPortale.getStato(), pagamentoPortale.getCodiceStato()); 
			
			pagamentiPortaleBD.updatePagamento(pagamentoPortale, false, true);
			
			// disabilito la select for update
			if(bd == null) {
				pagamentiPortaleBD.disableSelectForUpdate();
			}
			log.debug("Update pagamento portale id [{}] completato ", idPagamentoPortale); 
		} catch (NotFoundException e) {
			//donothing
		} finally {
			if(pagamentiPortaleBD != null && bd == null) {
				pagamentiPortaleBD.closeConnection();
			}
		}
	}
}
