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
package it.govpay.core.business;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.logger.MessaggioDiagnosticoCostanti;
import it.govpay.core.utils.logger.MessaggioDiagnosticoUtils;
import it.govpay.core.utils.thread.RecuperaRTThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Intermediario;

public class Ricevute {

	private static Logger log = LoggerWrapperFactory.getLogger(Ricevute.class);

	public Ricevute() {
		// donothing
	}

	public String recuperoRT() throws ServiceException, IOException {
		IContext ctx = ContextThreadLocal.get();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		List<String> response = new ArrayList<>();
		try {
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_AVVIO_KEY);
			StazioniBD stazioniBD = new StazioniBD(configWrapper);
			List<Stazione> lstStazioni = stazioniBD.getStazioni();
			DominiBD dominiBD = new DominiBD(configWrapper);
			RendicontazioniBD rendicontazioniBD = new RendicontazioniBD(configWrapper);
			Integer numeroGiorniSoglia = GovpayConfig.getInstance().getNumeroGiorniRendicontazioniSenzaPagamento();

			for(Stazione stazione : lstStazioni) {
				// controllare se e' abilitato il recupero
				Intermediario intermediario = stazione.getIntermediario(configWrapper);
				
				if(intermediario.getConnettorePddRecuperoRT() != null) {
					DominioFilter filter = dominiBD.newFilter();
					filter.setCodStazione(stazione.getCodStazione());
					List<Dominio> lstDomini = dominiBD.findAll(filter);

					MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_ACQUISIZIONE_LISTA_RENDICONTAZIONI_KEY, stazione.getCodStazione());
					log.debug("Recupero RT [CodStazione: {}]", stazione.getCodStazione());

					if(lstDomini.isEmpty()) {
						log.debug("Recupero RT per la stazione [CodStazione: {}] non eseguito: la stazione non e' associata ad alcun dominio.", stazione.getCodStazione());
						continue;
					}
					
					for (Dominio dominio : lstDomini) {
						String codDominio = dominio.getCodDominio();
						
						List<Rendicontazione> rendicontazioniSenzaPagamento = rendicontazioniBD.getRendicontazioniSenzaPagamento(codDominio, numeroGiorniSoglia);
						
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_LISTA_RENDICONTAZIONI_GOVPAY_OK_KEY, codDominio, rendicontazioniSenzaPagamento.size() + "");
						
						if(rendicontazioniSenzaPagamento.isEmpty()) {
							log.debug("Recupero RT per il Dominio [{}] completato, non sono state trovate RT da recuperare.", codDominio);
						} else {
							log.info("Recupero RT per il Dominio [{}] trovate [{}] RT da recuperare.", codDominio, rendicontazioniSenzaPagamento.size());
							
							List<RecuperaRTThread> threads = new ArrayList<>();
							
							for (Rendicontazione rendicontazione : rendicontazioniSenzaPagamento) {
								RecuperaRTThread sender = new RecuperaRTThread(dominio, rendicontazione, ctx);
								ThreadExecutorManager.getExecutorRecuperaRT().execute(sender);
								threads.add(sender);
							}
							
							log.info("Processi di recupero RT per il Dominio [{}] avviati.", codDominio);
							Operazioni.aggiornaSondaOK(configWrapper, Operazioni.BATCH_RECUPERO_RT);

							attendiTerminazioneThreads(response, threads);

							// Hanno finito tutti, aggiorno stato esecuzione
							BatchManager.aggiornaEsecuzione(configWrapper, Operazioni.BATCH_RECUPERO_RT);
							log.info("Processi di recupero RT per il Dominio [{}] terminati.", codDominio);
							
							MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_DOMINIO_OK_KEY, codDominio);
						}
					}
					
				} else {
					// servizion non configurato
					log.warn("Recupero RT per la stazione [{}] dell'Intermediario [{}] non configurato.", stazione.getCodStazione(), intermediario.getCodIntermediario());
					MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_NON_CONFIGURATO_KEY, intermediario.getCodIntermediario(), stazione.getCodStazione());
				}
			}
			
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_CONCLUSIONE_KEY);
		} catch (ServiceException | IOException e) {
			logError(e);
			throw e;
		}

		if(response.isEmpty()) {
			return "Recupero RT completato#Nessuna ricevuta acquisita.";
		} else {
			return StringUtils.join(response,"|");
		}
	}
	
	private static void logError(Exception e) {
		log.error("Recupero RT fallito", e);
	}

	private void attendiTerminazioneThreads(List<String> response, List<RecuperaRTThread> threads) {
		// Aspetto che abbiano finito tutti
		int numeroErrori = 0;
		while(true){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				log.warn(MessageFormat.format(Operazioni.ERROR_MSG_INTERRUPTED_0, e.getMessage()), e);
			    // Restore interrupted state...
			    Thread.currentThread().interrupt();
			}
			boolean completed = true;
			for(RecuperaRTThread sender : threads) {
				if(!sender.isCompleted()) 
					completed = false;
			}

			if(completed) { 
				for(RecuperaRTThread sender : threads) {
					if(sender.isErrore()) 
						numeroErrori ++;
					
					response.add(sender.getEsitoOperazione());
				}
				int numOk = threads.size() - numeroErrori;
				log.debug(Operazioni.DEBUG_MSG_COMPLETATA_ESECUZIONE_DEI_0_THREADS_OK_1_ERRORE_2, threads.size(), numOk, numeroErrori);
				break; // esco
			}
		}
	}
}
