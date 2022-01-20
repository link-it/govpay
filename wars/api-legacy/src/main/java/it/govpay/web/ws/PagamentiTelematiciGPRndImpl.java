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
package it.govpay.web.ws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.WebServiceContext;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.context.core.Actor;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.pagamenti.RendicontazioniDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiFrDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiFrDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.model.Applicazione;
import it.govpay.model.Versamento.TipologiaTipoVersamento;
import it.govpay.servizi.PagamentiTelematiciGPRnd;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.gprnd.GpChiediFlussoRendicontazione;
import it.govpay.servizi.gprnd.GpChiediFlussoRendicontazioneResponse;
import it.govpay.servizi.gprnd.GpChiediListaFlussiRendicontazione;
import it.govpay.servizi.gprnd.GpChiediListaFlussiRendicontazioneResponse;
import it.govpay.web.utils.Gp21Utils;
import it.govpay.web.utils.Utils;

@WebService(serviceName = "PagamentiTelematiciGPRndService",
endpointInterface = "it.govpay.servizi.PagamentiTelematiciGPRnd",
targetNamespace = "http://www.govpay.it/servizi/",
portName = "GPRndPort",
wsdlLocation = "classpath:wsdl/GpRnd.wsdl")

@HandlerChain(file="../../../../handler-chains/handler-chain-gpws.xml")

@org.apache.cxf.annotations.SchemaValidation

public class PagamentiTelematiciGPRndImpl implements PagamentiTelematiciGPRnd {

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LoggerWrapperFactory.getLogger(PagamentiTelematiciGPRndImpl.class);

	@Override
	public GpChiediListaFlussiRendicontazioneResponse gpChiediListaFlussiRendicontazione(GpChiediListaFlussiRendicontazione bodyrichiesta) {
		log.info("Richiesta operazione gpChiediListaFlussiRendicontazione");
		GpChiediListaFlussiRendicontazioneResponse response = new GpChiediListaFlussiRendicontazioneResponse();
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		response.setCodOperazione(ctx.getTransactionId());
		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(user));
		//		BasicBD bd = null;
		try {
			//			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.getApplicazioneAutenticata(appContext, user);
			ctx.getApplicationLogger().log("gprnd.ricevutaRichiesta");

			Date da = null, a=null;

			if(bodyrichiesta.getDataInizio() != null) {
				Calendar inizio = Calendar.getInstance();
				inizio.setTime(bodyrichiesta.getDataInizio());
				inizio.set(Calendar.HOUR_OF_DAY, 0);
				inizio.set(Calendar.MINUTE, 0);
				inizio.set(Calendar.SECOND, 0);
				inizio.set(Calendar.MILLISECOND, 0);
				da = inizio.getTime();
			}

			if(bodyrichiesta.getDataFine() != null) {
				Calendar fine = Calendar.getInstance();
				fine.setTime(bodyrichiesta.getDataFine());
				fine.set(Calendar.HOUR_OF_DAY, 23);
				fine.set(Calendar.MINUTE, 59);
				fine.set(Calendar.SECOND, 59);
				fine.set(Calendar.MILLISECOND, 999);
				a = fine.getTime();
			}

			ListaRendicontazioniDTO findRendicontazioniDTO = new ListaRendicontazioniDTO(user);
			findRendicontazioniDTO.setIdDominio(bodyrichiesta.getCodDominio());
			findRendicontazioniDTO.setCodApplicazione(bodyrichiesta.getCodApplicazione());

			findRendicontazioniDTO.setDataAcquisizioneFlussoDa(da);
			findRendicontazioniDTO.setDataAcquisizioneFlussoA(a);

			// Autorizzazione sulle uo
			List<IdUnitaOperativa> uo = AuthorizationManager.getUoAutorizzate(user);
			findRendicontazioniDTO.setUnitaOperative(uo);
			//findRendicontazioniDTO.setObsoleto(false);
			findRendicontazioniDTO.setRicercaFR(true);

			RendicontazioniDAO rendicontazioniDAO = new RendicontazioniDAO();

			ListaRendicontazioniDTOResponse findRendicontazioniDTOResponse = uo != null ? rendicontazioniDAO.listaRendicontazioni(findRendicontazioniDTO)
					: new ListaRendicontazioniDTOResponse(0L, new ArrayList<>());

			for (it.govpay.bd.viste.model.Rendicontazione frModel : findRendicontazioniDTOResponse.getResults()) {
				response.getFlussoRendicontazione().add(Gp21Utils.toFr(frModel.getFr()));
			}

			//			List<Fr> rendicontazioni = rendicontazioneBusiness.chiediListaRendicontazioni(applicazione, bodyrichiesta.getCodDominio(), bodyrichiesta.getCodApplicazione(), da, a);
			//			for(Fr frModel : rendicontazioni) {
			//				response.getFlussoRendicontazione().add(Gp21Utils.toFr(frModel, bd));
			//			}
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.getApplicationLogger().log("gprnd.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(Utils.fromEsitoOperazioneGovpay(e.getCodEsito()));
			response.setDescrizioneEsitoOperazione(e.getMessage());
			response.getFlussoRendicontazione().clear();
			e.log(log);
			try {
				ctx.getApplicationLogger().log("gprnd.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			response.getFlussoRendicontazione().clear();
			new GovPayException(e).log(log);
			try {
				ctx.getApplicationLogger().log("gprnd.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
		} finally {
			if(ctx != null) {
				PagamentiTelematiciGPAppImpl.setResult(appContext,response);
				//				ctx.getApplicationLogger().log();
			}
			//			if(bd != null) bd.closeConnection();
		}
		return response;
	}


	@Override
	public GpChiediFlussoRendicontazioneResponse gpChiediFlussoRendicontazione(GpChiediFlussoRendicontazione bodyrichiesta) {

		log.info("Richiesta operazione gpChiediFlussoRendicontazione");
		GpChiediFlussoRendicontazioneResponse response = new GpChiediFlussoRendicontazioneResponse();
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		response.setCodOperazione(ctx.getTransactionId());
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		//		BasicBD bd = null;

		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(user));
		try {
			//			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.getApplicazioneAutenticata(appContext, user);
			ctx.getApplicationLogger().log("gprnd.ricevutaRichiesta");

			//Autorizzazione alla richiesta: controllo che il dominio sia tra quelli abilitati per l'applicazione
			RendicontazioniDAO rendicontazioniDAO = new RendicontazioniDAO();

			String accept = MediaType.APPLICATION_JSON;
			LeggiFrDTO leggiRendicontazioneDTO = new LeggiFrDTO(user, bodyrichiesta.getCodFlusso());
			leggiRendicontazioneDTO.setAccept(accept);
			leggiRendicontazioneDTO.setObsoleto(false);	

			LeggiFrDTOResponse leggiRendicontazioneDTOResponse = rendicontazioniDAO.leggiFlussoRendicontazione(leggiRendicontazioneDTO);
			Fr frModel = leggiRendicontazioneDTOResponse.getFr();

			// controllo che il dominio sia autorizzato
			if(leggiRendicontazioneDTOResponse.getDominio() != null && !AuthorizationManager.isDominioAuthorized(user, leggiRendicontazioneDTOResponse.getDominio().getCodDominio())) {
				throw new GovPayException(it.govpay.core.beans.EsitoOperazione.RND_001);
				//				throw AuthorizationManager.toNotAuthorizedException(user,leggiRendicontazioneDTOResponse.getDominio().getCodDominio(), null);
			}

			// controllo uo
			List<IdUnitaOperativa> uo = AuthorizationManager.getUoAutorizzate(user);
			leggiRendicontazioneDTO = new LeggiFrDTO(user, bodyrichiesta.getCodFlusso());
			leggiRendicontazioneDTO.setUnitaOperative(uo);

			LeggiFrDTOResponse checkAutorizzazioneRendicontazioneDTOResponse = rendicontazioniDAO.checkAutorizzazioneFlussoRendicontazione(leggiRendicontazioneDTO);

			// controllo che il dominio sia autorizzato
			if(!checkAutorizzazioneRendicontazioneDTOResponse.isAuthorized()) {
				throw AuthorizationManager.toNotAuthorizedException(user,"Il flusso non contiente dei pagamenti associati a Unita' Operative autorizzate.");
			}

			//			if(!AclEngine.isAuthorized(applicazione, Servizio.RENDICONTAZIONE, frModel.getDominio(bd).getCodDominio(), null)) {
			//				throw new GovPayException(EsitoOperazione.RND_001);
			//			}


			List<Rendicontazione> rends = frModel.getRendicontazioni();
			for(Rendicontazione rend : rends) {
				if(rend.getPagamento(null) == null) {
					try {
						it.govpay.bd.model.Versamento versamento = new it.govpay.core.business.Versamento().chiediVersamento(null, null, null, null, frModel.getDominio(configWrapper).getCodDominio(), rend.getIuv(), TipologiaTipoVersamento.DOVUTO);
						rend.setVersamento(versamento);
					}catch (Exception e) {
						continue;
					}
				}
			}

			if(bodyrichiesta.getCodApplicazione() != null) {
				Long idApplicazione = AnagraficaManager.getApplicazione(configWrapper, bodyrichiesta.getCodApplicazione()).getId();
				List<Rendicontazione> rendsFiltrato = new ArrayList<Rendicontazione>();

				for(Rendicontazione rend : rends) {
					if(rend.getVersamento(null) !=  null && rend.getVersamento(null).getIdApplicazione() != idApplicazione.longValue()) {
						continue;
					}
					rendsFiltrato.add(rend);
				}

				rends = rendsFiltrato;
			}
			response.setFlussoRendicontazione(Gp21Utils.toFr(frModel, rends, configWrapper));
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.getApplicationLogger().log("gprnd.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(Utils.fromEsitoOperazioneGovpay(e.getCodEsito()));
			response.setDescrizioneEsitoOperazione(e.getMessage());
			response.setFlussoRendicontazione(null);
			e.log(log);
			try {
				ctx.getApplicationLogger().log("gprnd.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			response.setFlussoRendicontazione(null);
			new GovPayException(e).log(log);
			try {
				ctx.getApplicationLogger().log("gprnd.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
		} finally {
			if(ctx != null) {
				PagamentiTelematiciGPAppImpl.setResult(appContext,response);
//				ctx.getApplicationLogger().log();
			}
			//			if(bd != null) bd.closeConnection();
		}
		return response;
	}

	private Applicazione getApplicazioneAutenticata(GpContext appContext, Authentication authentication) throws GovPayException, ServiceException {

		if(AutorizzazioneUtils.getPrincipal(authentication) == null) {
			throw new GovPayException(it.govpay.core.beans.EsitoOperazione.AUT_000);
		}

		//		if(wsCtxt.getUserPrincipal() == null) {
		//			throw new GovPayException(EsitoOperazione.AUT_000);
		//		}

		GovpayLdapUserDetails authenticationDetails = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		Applicazione app = authenticationDetails.getApplicazione();

		if(app == null) {
			throw new GovPayException(it.govpay.core.beans.EsitoOperazione.AUT_001, AutorizzazioneUtils.getPrincipal(authentication));
		}

		//		Applicazione app = null;
		//		try {
		//			app = AnagraficaManager.getApplicazioneByPrincipal(bd, wsCtxt.getUserPrincipal().getName());
		//		} catch (NotFoundException e) {
		//			throw new GovPayException(EsitoOperazione.AUT_001, wsCtxt.getUserPrincipal().getName());
		//		}

		if(app != null) {
			Actor from = new Actor();
			from.setName(app.getCodApplicazione());
			from.setType(GpContext.TIPO_SOGGETTO_APP);
			appContext.getTransaction().setFrom(from);
			appContext.getTransaction().getClient().setName(app.getCodApplicazione());
		}
		return app;
	}

}
