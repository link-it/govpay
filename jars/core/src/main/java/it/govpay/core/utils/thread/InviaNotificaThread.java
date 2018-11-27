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
import it.govpay.bd.model.Applicazione;
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
			List<Pagamento> pagamenti = this.notifica.getRpt(bd).getPagamenti(bd);
			if(pagamenti != null) {
				for(Pagamento pagamento : pagamenti)
					pagamento.getSingoloVersamento(bd);
			}
		} else {
			this.notifica.getRr(bd);
			this.notifica.getRr(bd).getRpt(bd);
			this.notifica.getRr(bd).getRpt(bd).getVersamento(bd);
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
			
			if(this.notifica.getIdRpt() != null) {
				if(this.notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
					ctx = new GpContext(this.notifica.getRpt(bd).getIdTransazioneRpt());
				} else {
					ctx = new GpContext(this.notifica.getRpt(bd).getIdTransazioneRt());
				}
				
				if(this.notifica.getRpt(bd).getCodCarrello() != null) {
					ctx.getContext().getRequest().addGenericProperty(new Property("codCarrello", this.notifica.getRpt(bd).getCodCarrello()));
				} 
				ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", this.notifica.getRpt(null).getCodDominio()));
				ctx.getContext().getRequest().addGenericProperty(new Property("iuv", this.notifica.getRpt(null).getIuv()));
				ctx.getContext().getRequest().addGenericProperty(new Property("ccp", this.notifica.getRpt(null).getCcp()));
				
				if(this.notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) 
					ctx.log("notifica.rpt");
				else
					ctx.log("notifica.rt");
			} else {
				if(this.notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
					ctx = new GpContext(this.notifica.getRr(bd).getIdTransazioneRr());
				} else {
					ctx = new GpContext(this.notifica.getRr(bd).getIdTransazioneEr());
				}
				ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", this.notifica.getRr(null).getCodDominio()));
				ctx.getContext().getRequest().addGenericProperty(new Property("iuv", this.notifica.getRr(null).getIuv()));
				ctx.getContext().getRequest().addGenericProperty(new Property("ccp", this.notifica.getRr(null).getCcp()));
				if(this.notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) 
					ctx.log("notifica.rr");
				else
					ctx.log("notifica.er");
			}
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			Applicazione applicazione = this.notifica.getApplicazione(bd);
					
			MDC.put("op", ctx.getTransactionId());
			
			log.info("Spedizione della notifica [idNotifica: " + this.notifica.getId() +"] all'applicazione [CodApplicazione: " + this.notifica.getApplicazione(null).getCodApplicazione() + "]");
			if(applicazione.getConnettoreNotifica() == null || applicazione.getConnettoreNotifica().getUrl() == null) {
				ctx.log("notifica.annullata");
				log.info("Connettore Notifica non configurato per l'applicazione [CodApplicazione: " + applicazione.getCodApplicazione() + "]. Spedizione inibita.");
				NotificheBD notificheBD = new NotificheBD(bd);
				long tentativi = this.notifica.getTentativiSpedizione() + 1;
				Date prossima = new GregorianCalendar(9999,1,1).getTime();
				notificheBD.updateDaSpedire(this.notifica.getId(), "Connettore Notifica non configurato.", tentativi, prossima);
				return;
			}
			
			ctx.setupPaClient(applicazione.getCodApplicazione(), this.notifica.getIdRpt() != null ? "paNotificaTransazione" : "paNotificaStorno", applicazione.getConnettoreNotifica().getUrl(), applicazione.getConnettoreNotifica().getVersione());
			ctx.log("notifica.spedizione");
			
			NotificaClient client = new NotificaClient(applicazione);
			client.invoke(this.notifica,bd);
			this.notifica.setStato(StatoSpedizione.SPEDITO);
			this.notifica.setDescrizioneStato(null);
			this.notifica.setDataAggiornamento(new Date());
			if(bd == null)
				bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			NotificheBD notificheBD = new NotificheBD(bd);
			notificheBD.updateSpedito(this.notifica.getId());
			
			if(this.notifica.getIdRpt() != null) {
				if(this.notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
					if(this.notifica.getRpt(bd).getCodCarrello() != null) {
						ctx.log("notifica.carrellook");
					} else {
						ctx.log("notifica.rptok");
					}
				} else {
					ctx.log("notifica.rtok");
				}
			} else {
				if(this.notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
					ctx.log("notifica.rrok");
				} else {
					ctx.log("notifica.erok");
				}
			}
			log.info("Notifica consegnata con successo");
		} catch(Exception e) {
			if(e instanceof GovPayException || e instanceof ClientException)
				log.warn("Errore nella consegna della notifica: " + e.getMessage());
			else
				log.error("Errore nella consegna della notifica", e);
			try {
				if(bd == null)
					bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
				long tentativi = this.notifica.getTentativiSpedizione() + 1;
				NotificheBD notificheBD = new NotificheBD(bd);
				
				Date today = new Date();
				Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
				Date prossima = new Date(today.getTime() + (tentativi * tentativi * 60 * 1000));
				
				// Limito la rispedizione al giorno dopo.
				if(prossima.after(tomorrow)) prossima = tomorrow;
				
				if(tentativi == 1 || !e.getMessage().equals(this.notifica.getDescrizioneStato())) {
					if(this.notifica.getIdRpt() != null) {
						if(this.notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
							if(this.notifica.getRpt(bd).getCodCarrello() != null) {
								ctx.log("notifica.carrelloko", e.getMessage());
							} else {
								ctx.log("notifica.rptko", e.getMessage());
							}
						} else {
							ctx.log("notifica.rtko", e.getMessage());
						}
					} else {
						if(this.notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
							ctx.log("notifica.rrko", e.getMessage());
						} else {
							ctx.log("notifica.erko", e.getMessage());
						}
					}
				} else {
					if(this.notifica.getIdRpt() != null) {
						if(this.notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
							if(this.notifica.getRpt(bd).getCodCarrello() != null) {
								ctx.log("notifica.carrelloRetryko", e.getMessage(), prossima.toString());
							} else {
								ctx.log("notifica.rptRetryko", e.getMessage(), prossima.toString());
							}
						} else {
							ctx.log("notifica.rtRetryko", e.getMessage(), prossima.toString());
						}
					} else {
						if(this.notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
							ctx.log("notifica.rrRetryko", e.getMessage(), prossima.toString());
						} else {
							ctx.log("notifica.erRetryko", e.getMessage(), prossima.toString());
						}
					}
				}
				
				notificheBD.updateDaSpedire(this.notifica.getId(), e.getMessage(), tentativi, prossima);
			} catch (Exception ee) {
				// Andato male l'aggiornamento. Non importa, verra' rispedito.
			}
		} finally {
			this.completed = true;
			if(bd != null) bd.closeConnection(); 
			if(ctx != null) ctx.log();
		}
	}

	public boolean isCompleted() {
		return this.completed;
	}
}
