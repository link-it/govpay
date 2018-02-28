/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.thread;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NotificaClient;
import it.govpay.model.Notifica.StatoSpedizione;
import it.govpay.model.Notifica.TipoNotifica;

public class InviaNotificaThread implements Runnable {

	private static Logger log = LoggerWrapperFactory.getLogger(InviaNotificaThread.class);
	private Notifica notifica;
	private boolean completed = false;

	public InviaNotificaThread(Notifica notifica, BasicBD bd) throws ServiceException {
		// Verifico che tutti i campi siano valorizzati
		this.notifica = notifica;
		this.notifica.getApplicazione(bd);
		if(this.notifica.getIdRpt() != null) {
			this.notifica.getRpt(bd).getVersamento(bd);
			this.notifica.getRpt(bd).getCanale(bd);
			this.notifica.getRpt(bd).getPsp(bd);
			List<Pagamento> pagamenti = this.notifica.getRpt(bd).getPagamenti(bd);
			if(pagamenti != null) {
				for(Pagamento pagamento : pagamenti)
					pagamento.getSingoloVersamento(bd);
			}
		} else {
			this.notifica.getRr(bd);
			this.notifica.getRr(bd).getRpt(bd);
			this.notifica.getRr(bd).getRpt(bd).getVersamento(bd);
			this.notifica.getRr(bd).getRpt(bd).getCanale(bd);
			this.notifica.getRr(bd).getRpt(bd).getPsp(bd);
			List<Pagamento> pagamenti = this.notifica.getRr(bd).getRpt(bd).getPagamenti(bd);
			if(pagamenti != null) {
				for(Pagamento pagamento : pagamenti)
					pagamento.getSingoloVersamento(bd);
			}
		}
	}

	@Override
	public void run() {
		
		GpContext ctx = null;
		BasicBD bd = null;
		try {
			if(notifica.getIdRpt() != null) {
				if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
					ctx = new GpContext(notifica.getRpt(bd).getIdTransazioneRpt());
				} else {
					ctx = new GpContext(notifica.getRpt(bd).getIdTransazioneRt());
				}
				
				if(notifica.getRpt(bd).getCodCarrello() != null) {
					ctx.getContext().getRequest().addGenericProperty(new Property("codCarrello", notifica.getRpt(bd).getCodCarrello()));
				} 
				ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", notifica.getRpt(null).getCodDominio()));
				ctx.getContext().getRequest().addGenericProperty(new Property("iuv", notifica.getRpt(null).getIuv()));
				ctx.getContext().getRequest().addGenericProperty(new Property("ccp", notifica.getRpt(null).getCcp()));
				
				if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) 
					ctx.log("notifica.rpt");
				else
					ctx.log("notifica.rt");
			} else {
				if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
					ctx = new GpContext(notifica.getRr(bd).getIdTransazioneRr());
				} else {
					ctx = new GpContext(notifica.getRr(bd).getIdTransazioneEr());
				}
				ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", notifica.getRr(null).getCodDominio()));
				ctx.getContext().getRequest().addGenericProperty(new Property("iuv", notifica.getRr(null).getIuv()));
				ctx.getContext().getRequest().addGenericProperty(new Property("ccp", notifica.getRr(null).getCcp()));
				if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) 
					ctx.log("notifica.rr");
				else
					ctx.log("notifica.er");
			}
			GpThreadLocal.set(ctx);
			
			ctx.setupPaClient(notifica.getApplicazione(null).getCodApplicazione(), notifica.getIdRpt() != null ? "paNotificaTransazione" : "paNotificaStorno", notifica.getApplicazione(bd).getConnettoreNotifica() == null ? null : notifica.getApplicazione(bd).getConnettoreNotifica().getUrl(), notifica.getApplicazione(null).getConnettoreNotifica().getVersione());
					
			MDC.put("op", ctx.getTransactionId());
			
			log.info("Spedizione della notifica [idNotifica: " + notifica.getId() +"] all'applicazione [CodApplicazione: " + notifica.getApplicazione(null).getCodApplicazione() + "]");
			if(notifica.getApplicazione(bd).getConnettoreNotifica() == null || notifica.getApplicazione(bd).getConnettoreNotifica().getUrl() == null) {
				ctx.log("notifica.annullata");
				log.info("Connettore Notifica non configurato per l'applicazione [CodApplicazione: " + notifica.getApplicazione(null).getCodApplicazione() + "]. Spedizione inibita.");
				if(bd == null)
					bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
				NotificheBD notificheBD = new NotificheBD(bd);
				long tentativi = notifica.getTentativiSpedizione() + 1;
				Date prossima = new GregorianCalendar(9999,1,1).getTime();
				notificheBD.updateDaSpedire(notifica.getId(), "Connettore Notifica non configurato.", tentativi, prossima);
				return;
			}
			
			ctx.log("notifica.spedizione");
			
			NotificaClient client = new NotificaClient(notifica.getApplicazione(bd));
			client.invoke(notifica);
			notifica.setStato(StatoSpedizione.SPEDITO);
			notifica.setDescrizioneStato(null);
			notifica.setDataAggiornamento(new Date());
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			NotificheBD notificheBD = new NotificheBD(bd);
			notificheBD.updateSpedito(notifica.getId());
			
			if(notifica.getIdRpt() != null) {
				if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
					if(notifica.getRpt(bd).getCodCarrello() != null) {
						ctx.log("notifica.carrellook");
					} else {
						ctx.log("notifica.rptok");
					}
				} else {
					ctx.log("notifica.rtok");
				}
			} else {
				if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
					ctx.log("notifica.rrok");
				} else {
					ctx.log("notifica.erok");
				}
			}
			log.info("Notifica consegnata con successo");
		} catch(Exception e) {
			if(e instanceof GovPayException || e instanceof ClientException)
				log.warn("Errore nella consegna della notifica: " + e);
			else
				log.error("Errore nella consegna della notifica", e);
			try {
				if(bd == null)
					bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
				long tentativi = notifica.getTentativiSpedizione() + 1;
				NotificheBD notificheBD = new NotificheBD(bd);
				
				Date today = new Date();
				Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
				Date prossima = new Date(today.getTime() + (tentativi * tentativi * 60 * 1000));
				
				// Limito la rispedizione al giorno dopo.
				if(prossima.after(tomorrow)) prossima = tomorrow;
				
				if(tentativi == 1 || !e.getMessage().equals(notifica.getDescrizioneStato())) {
					if(notifica.getIdRpt() != null) {
						if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
							if(notifica.getRpt(bd).getCodCarrello() != null) {
								ctx.log("notifica.carrelloko", e.getMessage());
							} else {
								ctx.log("notifica.rptko", e.getMessage());
							}
						} else {
							ctx.log("notifica.rtko", e.getMessage());
						}
					} else {
						if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
							ctx.log("notifica.rrko", e.getMessage());
						} else {
							ctx.log("notifica.erko", e.getMessage());
						}
					}
				} else {
					if(notifica.getIdRpt() != null) {
						if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
							if(notifica.getRpt(bd).getCodCarrello() != null) {
								ctx.log("notifica.carrelloRetryko", e.getMessage(), prossima.toString());
							} else {
								ctx.log("notifica.rptRetryko", e.getMessage(), prossima.toString());
							}
						} else {
							ctx.log("notifica.rtRetryko", e.getMessage(), prossima.toString());
						}
					} else {
						if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
							ctx.log("notifica.rrRetryko", e.getMessage(), prossima.toString());
						} else {
							ctx.log("notifica.erRetryko", e.getMessage(), prossima.toString());
						}
					}
				}
				
				notificheBD.updateDaSpedire(notifica.getId(), e.getMessage(), tentativi, prossima);
			} catch (Exception ee) {
				// Andato male l'aggiornamento. Non importa, verra' rispedito.
			}
		} finally {
			completed = true;
			if(bd != null) bd.closeConnection(); 
			if(ctx != null) ctx.log();
		}
	}

	public boolean isCompleted() {
		return completed;
	}
}
