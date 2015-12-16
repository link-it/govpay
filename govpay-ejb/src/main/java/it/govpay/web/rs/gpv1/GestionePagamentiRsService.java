/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.web.rs.gpv1;

import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.EntiBD;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.Autenticazione;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RtBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.business.Pagamenti;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.rs.Pagamento;
import it.govpay.rs.RichiestaPagamento;
import it.govpay.rs.RichiestaPagamentoResponse;
import it.govpay.rs.VerificaPagamento;
import it.govpay.utils.JaxbUtils;
import it.govpay.web.adapter.Converter;
import it.govpay.web.rs.BaseRsService;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/pagamenti")
public class GestionePagamentiRsService extends BaseRsService {

	private static Logger log = LogManager.getLogger();
	
	@POST
	@Path("/nuovoPagamento")
	public Response richiediPagamento(RichiestaPagamento richiestaPagamento, @DefaultValue("true") @QueryParam(value = "redirect") Boolean redirect) throws GovPayException {
		initLogger("richiediPagamento");
		log.info("Ricevuta richiesta di Esecuzione Pagamento.");

		if(log.getLevel().equals(Level.TRACE)) {
			try {
				log.trace(new String(JaxbUtils.toByte(richiestaPagamento)));
			} catch (Exception e) {
				log.warn("Errore nella serializzazione della richiesta di pagamento: " + e);
			}
		}
		Principal principal = request.getUserPrincipal();
		
		if(principal == null) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTENTICAZIONE);
		}
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			Applicazione applicazione = null;

			try {
				applicazione = AnagraficaManager.getApplicazioneByPrincipal(bd, principal.getName());
			} catch (Exception e){
				log.error("Applicazione [principal: " + principal.getName() + "] non censita in Anagrafica Applicazioni.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 
			
			log.info("Identificazione Applicazione avvenuta con successo [CodApplicazione: " + applicazione.getCodApplicazione() + "]");

			Portale portale = null;

			try {
				portale = AnagraficaManager.getPortaleByPrincipal(bd, principal.getName());
			} catch (Exception e){
				log.error("Portale [principal: " + principal.getName() + "] non censito in Anagrafica Portali.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 

			log.info("Identificazione Portale avvenuta con successo [CodPortale: " + portale.getCodPortale() + "]");
			
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			EntiBD entiBD = new EntiBD(bd);
			List<Long> versamenti = new ArrayList<Long>();
			String ibanAddebito = null; 
			String ibanAccredito = null; 
			FirmaRichiesta firma = null;
			Autenticazione autenticazione = Converter.toOrm(richiestaPagamento.getAutenticazioneSoggetto());
			Anagrafica versante = Converter.toOrm(richiestaPagamento.getSoggettoVersante());
			String callbackUrl = richiestaPagamento.getRedirectUrl();

			for(Pagamento pagamento : richiestaPagamento.getPagamentis()) {
				Dominio dominio = null;
				try {
					dominio = AnagraficaManager.getDominio(bd, pagamento.getIdentificativoBeneficiario());
				} catch (Exception e){
					log.error("Dominio [codDominio: " + pagamento.getIdentificativoBeneficiario() + "] non censito in Anagrafica Domini.");
					throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
				} 

				Ente ente = null;

				List<Ente> enti = entiBD.getEntiByDominio(dominio.getId());
				if(enti.size() != 1) {
					throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "L'uso dell'interfaccia GovPay v1 non consente la gestione di piu' enti nello stesso dominio.");
				}
				ente = enti.get(0);

				Versamento versamento = Converter.toVersamento(dominio, ente.getId(), applicazione, pagamento, richiestaPagamento.getSoggettoVersante(), bd);
				log.info("Inserito nuovo versamento [idVersamento: " + versamento.getId() + "]");
				versamentiBD.insertVersamento(versamento);
				versamenti.add(versamento.getId());
				firma = Converter.toOrm(pagamento.getFirma());
				if(ibanAddebito == null)
					ibanAddebito = pagamento.getDatiVersamento().getIbanAddebito();
				else {
					if(!ibanAddebito.equals(pagamento.getDatiVersamento().getIbanAddebito())) {
						throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Iban di addebito diversi per lo stesso pagamento non supportato.");
					}
				}
				
				if(ibanAccredito == null)
					ibanAddebito = pagamento.getDatiVersamento().getIbanAddebito();
				else {
					if(!ibanAddebito.equals(pagamento.getDatiVersamento().getIbanAddebito())) {
						throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Iban di addebito diversi per lo stesso pagamento non supportato.");
					}
				}
			}

			log.info("Esecuzione del pagamento in corso...");
			String pspUrl;
			Pagamenti pagamenti = new Pagamenti(bd);
			if(versamenti.size() == 1) {
				pspUrl = pagamenti.eseguiPagamentoEnte(portale.getId(), versamenti.get(0), richiestaPagamento.getIdentificativoPsp(), ibanAddebito, ibanAccredito, firma, autenticazione, versante, callbackUrl);
			} else {
				pspUrl = pagamenti.eseguiPagamentoCarrelloEnte(portale.getId(), versamenti, richiestaPagamento.getIdentificativoPsp(), ibanAddebito, firma, autenticazione, versante, callbackUrl);
			}
			log.info("Esecuzione del pagamento completata. [PspBackURL: " + pspUrl + "]");
			
			RichiestaPagamentoResponse richiestaPagamentoResponse = new RichiestaPagamentoResponse();

			if(pspUrl==null) {
				if(redirect) {
					return Response.seeOther(new URL(callbackUrl).toURI()).build();
				} else {
					richiestaPagamentoResponse.setPspRedirect(false);
					richiestaPagamentoResponse.setRedirectUrl(callbackUrl);
					return Response.ok(richiestaPagamentoResponse).build();
				}
			} else {
				if(redirect) {
					return Response.seeOther(new URL(pspUrl).toURI()).build();
				} else {
					richiestaPagamentoResponse.setPspRedirect(true);
					richiestaPagamentoResponse.setRedirectUrl(pspUrl);
					return Response.ok(new URL(pspUrl).toURI()).build();
				}
			}
		} catch (Exception e) {
			if(bd != null) bd.rollback();
			if(e instanceof GovPayException) {
				GovPayException gpe = (GovPayException) e;
				log.error("Riscontrato errore durante la verifica del pagamento [Tipo: " + gpe.getTipoException() + "][Descrizione: " + gpe.getDescrizione() + "]" );
				throw (GovPayException) gpe;
			}
			log.error("Riscontrato errore durante il pagamento." , e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
		}finally {
			if(bd != null) bd.closeConnection();
		}
	}

	@GET
	@Path("/verificaPagamento")
	@Produces(MediaType.APPLICATION_XML)
	public VerificaPagamento verificaPagamento(@QueryParam(value = "identificativoBeneficiario") String identificativoBeneficiario, @QueryParam(value = "iuv") String iuv) throws GovPayException {
		initLogger("verificaPagamento");
		log.info("Ricevuta richiesta di Verifica Pagamento [codDominio: " + identificativoBeneficiario + "][Iuv: " + iuv + "].");

		Principal principal = request.getUserPrincipal();

		if(principal == null) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTENTICAZIONE);
		}
		
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			try {
				AnagraficaManager.getPortaleByPrincipal(bd, principal.getName());
			} catch (Exception e){
				log.error("Portale [principal: " + principal.getName() + "] non censito in Anagrafica Portali.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			Versamento versamento = null;
			try {
				versamento = versamentiBD.getVersamento(identificativoBeneficiario, iuv);
			} catch (Exception e){
				throw new GovPayException(GovPayExceptionEnum.IUV_NON_TROVATO);
			} 
			
			RptBD rptBD = new RptBD(bd);
			Rpt rpt = null;
			try {
				rpt = rptBD.getLastRpt(versamento.getId());
				//Verifico lo stato RPT
				if(new Pagamenti(bd).aggiornaRptDaNpD(rpt)) {
					versamento = versamentiBD.getVersamento(identificativoBeneficiario, iuv);
				}
			} catch (Exception e){
				return Converter.toVerificaPagamento(versamento, null, null);
			} 
			
			RtBD rtBD = new RtBD(bd);
			Rt rt = null;
			byte[] rtByte = null;
			try {
				rt = rtBD.getLastRt(rpt.getId());
				TracciatiBD tracciatiBD = new TracciatiBD(bd);
				rtByte = tracciatiBD.getTracciato(rt.getIdTracciatoXML());
			} catch (Exception e){
				
			} 
			log.info("Verifica effettuata. Pagamento in stato: " + versamento.getStato());
			return Converter.toVerificaPagamento(versamento, rpt, rtByte);
		} catch (Exception e) {
			if(bd != null) bd.rollback();
			if(e instanceof GovPayException) {
				GovPayException gpe = (GovPayException) e;
				log.error("Riscontrato errore durante la verifica del pagamento [Tipo: " + gpe.getTipoException() + "][Descrizione: " + gpe.getDescrizione() + "]" );
				throw gpe;
			}
			log.error("Riscontrato errore durante la verifica del pagamento", e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}finally {
			if(bd != null) bd.closeConnection();
		}
	}
}

