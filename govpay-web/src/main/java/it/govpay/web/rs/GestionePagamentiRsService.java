/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.web.rs;

import java.net.URL;
import java.util.List;
import java.util.UUID;

import it.govpay.ejb.controller.AnagraficaEJB;
import it.govpay.ejb.controller.DistintaEJB;
import it.govpay.ejb.exception.GovPayException;
import it.govpay.ejb.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.model.EnteCreditoreModel;
import it.govpay.ejb.model.ScadenzarioModel;
import it.govpay.ejb.utils.GovpayConfiguration;
import it.govpay.ejb.utils.RedirectCache;
import it.govpay.ejb.utils.rs.JaxbUtils;
import it.govpay.web.controller.PagamentiController;
import it.govpay.web.controller.GatewayController;
import it.govpay.ndp.controller.RptController;
import it.govpay.rs.Pagamento;
import it.govpay.rs.RichiestaPagamento;
import it.govpay.rs.VerificaPagamento;
import it.govpay.web.utils.UrlUtils;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

@Path("/pagamenti")
public class GestionePagamentiRsService {

	@Inject
	PagamentiController pagamentiCtrl;
	
	@Inject
	DistintaEJB distintaEjb;
	
	@Inject
	AnagraficaEJB anagraficaEjb;

	@Inject
	GatewayController pspCtrl;
	
	@Inject
	RptController rptCtrl;
	
	@Inject
	Logger log;

	@POST
	@Path("/nuovoPagamento")
	@Consumes(MediaType.APPLICATION_XML)
	public Response richiediPagamento(RichiestaPagamento richiestaPagamento) throws GovPayException {
		ThreadContext.put("proc", "NuovoPagamento");
		/**
		 * DA RIMUOVERE QUANDO LA VALIDAZIONE FUNZIONA CORRETTAMENTE
		 */
		try {
			JaxbUtils.toRichiestaPagamento(JaxbUtils.toString(richiestaPagamento));
		} catch (JAXBException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE, e);
		}
		/**
		 * FINE DA RIMUOVERE
		 */
		
		if(richiestaPagamento.getPagamentis().size() == 1) {
			String beneficiario = richiestaPagamento.getPagamentis().get(0).getIdentificativoBeneficiario();
			String iuv = richiestaPagamento.getPagamentis().get(0).getDatiVersamento().getIuv();
			ThreadContext.put("dom", beneficiario);
			ThreadContext.put("iuv", iuv);
		} else {
			String uuid = UUID.randomUUID().toString();
			ThreadContext.put("dom", "-");
			ThreadContext.put("iuv", uuid);
			for(Pagamento p : richiestaPagamento.getPagamentis()) {
				log.info("Richiesta di pagamento multipla: [" + p.getIdentificativoBeneficiario()+ "][" + p.getDatiVersamento().getIuv() + "]");
			}
		}
		
		try {
			log.debug("Ricevuta richiesta di pagamento: \n" + JaxbUtils.toString(richiestaPagamento));
		} catch (JAXBException e) {
			log.error("Impossibile serializzare la richiesta: " + e);
		}
		
		try {
			// Invio l'RPT del pagamento.
			// Se mi ritorna una URL allora e' un pagamento immediato
			// Altrimenti e' un pagamento differito
			URL pspUrl = pagamentiCtrl.richiediPagamento(richiestaPagamento, "n/a");	

			// Gestione della redirect
			URL backUrl = GovpayConfiguration.getDefaultBackUrl();
			if(richiestaPagamento.getRedirectUrl() != null) {
				try {
					backUrl = new URL(richiestaPagamento.getRedirectUrl());
				} catch (Exception e) {
					log.warn("La RedirectURL della richiesta non e' una URL valida. Verra' utilizzato il valore di default.");
				}
			} 

			if(pspUrl==null) {
				return Response.seeOther(backUrl.toURI()).build();
			} else {
				String idSession = UrlUtils.getParameterValue("idSession", pspUrl);
				RedirectCache.put(idSession, backUrl);
				return Response.seeOther(pspUrl.toURI()).build();
			}
		} catch (GovPayException e) {
			throw e;
		} catch (Exception e) {
			log.error("Errore non gestito", e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}
	
	@GET
	@Path("/verificaPagamento")
	@Produces(MediaType.APPLICATION_XML)
	public VerificaPagamento verificaPagamento(@QueryParam(value = "identificativoBeneficiario") String identificativoBeneficiario, @QueryParam(value = "iuv") String iuv) throws GovPayException {
		ThreadContext.put("proc", "VerificaPagamento");
		ThreadContext.put("dom", identificativoBeneficiario);
		ThreadContext.put("iuv", iuv);
		ThreadContext.put("ccp", null);
		log.info("Richiesta verifica del pagamento.");
		
		EnteCreditoreModel enteCreditore = anagraficaEjb.getCreditoreByIdLogico(identificativoBeneficiario);
		if(enteCreditore == null) {
			throw new GovPayException(GovPayExceptionEnum.BENEFICIARIO_NON_TROVATO, "Il beneficiario " + identificativoBeneficiario + " non risulta censito sul sistema.");
		}
		
		try { 
			VerificaPagamento verifica = pagamentiCtrl.verificaPagamento(enteCreditore, iuv);
			log.info("Verifica eseguita. Il pagamento risulta " + verifica.getStatoPagamento());
			log.debug("Messaggio di risposta:\n" + JaxbUtils.toString(verifica));
			return verifica;
		} catch (GovPayException e) {
			log.error("Errore durante la verifica: " + e.getTipoException() + " " + (e.getDescrizione() != null ? e.getDescrizione() : ""));
			throw e;
		} catch (Exception e) {
			log.error("Errore durante la verifica.",e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}
	
	@GET
	@Path("/notificaPagamenti")
	@Produces(MediaType.APPLICATION_XML)
	public void notificaPagamenti(@QueryParam(value = "identificativoBeneficiario") String identificativoBeneficiario) throws GovPayException {
		ThreadContext.put("proc", "NotificaPagamento");
		ThreadContext.put("dom", identificativoBeneficiario);
		ThreadContext.put("iuv", null);
		ThreadContext.put("ccp", null);
		log.info("Richiesta spedizione delle verifiche non consegnate.");
		try { 
			EnteCreditoreModel enteCreditore = anagraficaEjb.getCreditoreByIdLogico(identificativoBeneficiario);
			if(enteCreditore == null) {
				throw new GovPayException(GovPayExceptionEnum.BENEFICIARIO_NON_TROVATO, "Il beneficiario " + identificativoBeneficiario + " non risulta censito sul sistema.");
			}
			
			List<ScadenzarioModel> scadenzari = anagraficaEjb.getScadenzari(enteCreditore.getIdEnteCreditore());
			
			for(ScadenzarioModel scadenzario : scadenzari) {
				pagamentiCtrl.inviaNotifiche(enteCreditore.getIdEnteCreditore(), scadenzario);
			}
			return;
		} catch (GovPayException e) {
			log.error("Errore durante la spedizione delle notifiche.",e);
			throw e;
		} catch (Exception e) {
			log.error("Errore durante la spedizione delle notifiche",e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}
	
	
	@GET
	@Path("/stornaPagamento")
	public void stornaPagamento(@QueryParam(value = "identificativoBeneficiario") String identificativoBeneficiario, @QueryParam(value = "iuv") String iuv, @QueryParam(value = "causale") String causale) throws GovPayException {
		ThreadContext.put("proc", "StornaPagamento");
		ThreadContext.put("dom", identificativoBeneficiario);
		ThreadContext.put("iuv", iuv);
		ThreadContext.put("ccp", null);
		log.info("Richiesta storno del pagamento con causale: " + causale);
		try { 
			EnteCreditoreModel enteCreditore = anagraficaEjb.getCreditoreByIdLogico(identificativoBeneficiario);
			if(enteCreditore == null) {
				throw new GovPayException(GovPayExceptionEnum.BENEFICIARIO_NON_TROVATO, "Il beneficiario " + identificativoBeneficiario + " non risulta censito sul sistema.");
			}
			pagamentiCtrl.stornaPagamento(enteCreditore, iuv, causale);
			log.info("Richiesta di storno del pagamento eseguito.");
		} catch (GovPayException e) {
			log.error("Errore durante la verifica: " + e.getTipoException() + " " + (e.getDescrizione() != null ? e.getDescrizione() : ""));
			throw e;
		} catch (Exception e) {
			log.error("Errore durante la verifica.",e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}
	
	@GET
	@Path("/spedisciAttivati")
	public void spedisciAttivati(@QueryParam(value = "identificativoBeneficiario") String identificativoBeneficiario) throws GovPayException {
		ThreadContext.put("proc", "SpedizioneAttivati");
		ThreadContext.put("dom", identificativoBeneficiario);
		ThreadContext.put("iuv", null);
		ThreadContext.put("ccp", null);
		log.info("Richiesta spedizione delle richieste attivate.");
		try { 
			EnteCreditoreModel enteCreditore = anagraficaEjb.getCreditoreByIdLogico(identificativoBeneficiario);
			if(enteCreditore == null) {
				throw new GovPayException(GovPayExceptionEnum.BENEFICIARIO_NON_TROVATO, "Il beneficiario " + identificativoBeneficiario + " non risulta censito sul sistema.");
			}
			rptCtrl.spedizioneAttivati(enteCreditore);
			return;
		} catch (GovPayException e) {
			log.error("Errore durante la spedizione delle notifiche.",e);
			throw e;
		} catch (Exception e) {
			log.error("Errore durante la spedizione delle notifiche",e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}
	
	@GET
	@Path("/spedisciAttivato")
	public void spedisciAttivato(@QueryParam(value = "identificativoBeneficiario") String identificativoBeneficiario, @QueryParam(value = "iuv") String iuv) throws GovPayException {
		ThreadContext.put("proc", "SpedizioneAttivato");
		ThreadContext.put("dom", identificativoBeneficiario);
		ThreadContext.put("iuv", iuv);
		ThreadContext.put("ccp", null);
		log.info("Richiesta spedizione delle richieste attivate.");
		try { 
			EnteCreditoreModel enteCreditore = anagraficaEjb.getCreditoreByIdLogico(identificativoBeneficiario);
			if(enteCreditore == null) {
				throw new GovPayException(GovPayExceptionEnum.BENEFICIARIO_NON_TROVATO, "Il beneficiario " + identificativoBeneficiario + " non risulta censito sul sistema.");
			}
			rptCtrl.spedizioneAttivati(enteCreditore, iuv);
			return;
		} catch (GovPayException e) {
			log.error("Errore durante la spedizione delle notifiche.",e);
			throw e;
		} catch (Exception e) {
			log.error("Errore durante la spedizione delle notifiche",e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}
}

