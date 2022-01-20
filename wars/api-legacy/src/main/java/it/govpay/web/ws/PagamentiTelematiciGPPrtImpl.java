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

import java.net.URLDecoder;
import java.util.List;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.business.model.AvviaRichiestaStornoDTO;
import it.govpay.core.business.model.AvviaRichiestaStornoDTOResponse;
import it.govpay.core.business.model.AvviaTransazioneDTO;
import it.govpay.core.business.model.AvviaTransazioneDTOResponse;
import it.govpay.core.business.model.SceltaWISP;
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.RptDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.RptUtils;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.model.Iuv;
import it.govpay.model.Versionabile.Versione;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Versamento;
import it.govpay.servizi.PagamentiTelematiciGPPrt;
import it.govpay.servizi.commons.Canale;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.commons.MetaInfo;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.commons.TipoVersamento;
import it.govpay.servizi.commons.TipoSceltaWisp;
import it.govpay.servizi.gpprt.GpAvviaRichiestaStorno;
import it.govpay.servizi.gpprt.GpAvviaRichiestaStornoResponse;
import it.govpay.servizi.gpprt.GpAvviaTransazionePagamento;
import it.govpay.servizi.gpprt.GpAvviaTransazionePagamentoResponse;
import it.govpay.servizi.gpprt.GpChiediListaPsp;
import it.govpay.servizi.gpprt.GpChiediListaPspResponse;
import it.govpay.servizi.gpprt.GpChiediListaVersamenti;
import it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse;
import it.govpay.servizi.gpprt.GpChiediSceltaWisp;
import it.govpay.servizi.gpprt.GpChiediSceltaWispResponse;
import it.govpay.servizi.gpprt.GpChiediStatoRichiestaStorno;
import it.govpay.servizi.gpprt.GpChiediStatoRichiestaStornoResponse;
import it.govpay.servizi.gpprt.GpChiediStatoTransazione;
import it.govpay.servizi.gpprt.GpChiediStatoTransazioneResponse;
import it.govpay.servizi.gpprt.GpChiediStatoVersamento;
import it.govpay.servizi.gpprt.GpChiediStatoVersamentoResponse;
import it.govpay.servizi.gpprt.GpChiediStatoVersamentoResponse.SpezzoneCausaleStrutturata;
import it.govpay.web.utils.Gp21Utils;
import it.govpay.web.utils.Utils;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.context.core.Actor;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@WebService(serviceName = "PagamentiTelematiciGPPrtService",
endpointInterface = "it.govpay.servizi.PagamentiTelematiciGPPrt",
targetNamespace = "http://www.govpay.it/servizi/",
portName = "GPPrtPort",
wsdlLocation = "classpath:wsdl/GpPrt.wsdl")

@HandlerChain(file="../../../../handler-chains/handler-chain-gpws.xml")

@org.apache.cxf.annotations.SchemaValidation

public class PagamentiTelematiciGPPrtImpl implements PagamentiTelematiciGPPrt {

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LoggerWrapperFactory.getLogger(PagamentiTelematiciGPPrtImpl.class);

	@Override
	public GpChiediListaPspResponse gpChiediListaPsp(GpChiediListaPsp bodyrichiesta) {
		log.warn("Operazione gpChiediListaPsp non disponibile");
		throw new RuntimeException(new NotImplementedException("Operazione non disponibile"));
	}

	@Override
	public GpAvviaTransazionePagamentoResponse gpAvviaTransazionePagamento(GpAvviaTransazionePagamento bodyrichiesta, MetaInfo metaInfo) {
		GpAvviaTransazionePagamentoResponse response = new GpAvviaTransazionePagamentoResponse();
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		Utils.loadMetaInfo(appContext, metaInfo);
		response.setCodOperazione(ctx.getTransactionId());

		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(user));
		//		BasicBD bd = null;
		try {
			log.info("Richiesta operazione gpAvviaTransazionePagamento");
			//			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			// Aggiungo il codSessionePortale al PaymentContext
			appContext.getPagamentoCtx().setCodSessionePortale(bodyrichiesta.getCodSessionePortale());
			appContext.getRequest().addGenericProperty(new Property("codSessionePortale", bodyrichiesta.getCodSessionePortale() != null ? bodyrichiesta.getCodSessionePortale() : "--Non fornito--"));

			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(appContext, user);
			ctx.getApplicationLogger().log("ws.ricevutaRichiesta");

			verificaApplicazione(applicazioneAutenticata, bodyrichiesta.getCodApplicazione());
			autorizzaPortale(bodyrichiesta.getCodPortale(), portaleAutenticato, bd);
			ctx.getApplicationLogger().log("ws.autorizzazione");

			appContext.getPagamentoCtx().setCarrello(true);
			String codCarrello = RptUtils.buildUUID35();
			appContext.getPagamentoCtx().setCodCarrello(codCarrello);
			appContext.getRequest().addGenericProperty(new Property("codCarrello", codCarrello));
			appContext.setCorrelationId(codCarrello);
			ctx.getApplicationLogger().log("pagamento.avviaTransazioneCarrelloWISP20");

			it.govpay.core.business.Pagamento pagamentoBusiness = new it.govpay.core.business.Pagamento();

			AvviaTransazioneDTO dto = new AvviaTransazioneDTO();
			dto.setAggiornaSeEsisteB(bodyrichiesta.isAggiornaSeEsiste());
			dto.setAutenticazione(bodyrichiesta.getAutenticazione().value());
			dto.setIbanAddebito(bodyrichiesta.getIbanAddebito());
			dto.setPortale(portaleAutenticato);
			dto.setUrlRitorno(bodyrichiesta.getUrlRitorno());
			dto.setVersamentoOrVersamentoRef(bodyrichiesta.getVersamentoOrVersamentoRef());
			dto.setVersante(bodyrichiesta.getVersante());
			dto.setCodiceConvenzione(bodyrichiesta.getCodConvenzione());
			if(bodyrichiesta.getCanale() != null) {
				dto.setCanale(new it.govpay.bd.model.Canale(bodyrichiesta.getCanale()));
			}


			AvviaTransazioneDTOResponse dtoResponse = pagamentoBusiness.avviaTransazione(dto);
			response.getRifTransazione().addAll(Gp21Utils.toRifTransazione(dtoResponse.getRifTransazioni()));
			response.setPspSessionId(dtoResponse.getCodSessione());
			response.setUrlRedirect(dtoResponse.getPspRedirectURL());
			response.setCodEsitoOperazione(EsitoOperazione.OK);

			if(response.getPspSessionId() != null) {
				appContext.getResponse().addGenericProperty(new Property("codPspSession", response.getPspSessionId()));
			}


			if(response.getPspSessionId() != null) {
				ctx.getApplicationLogger().log("pagamento.invioCarrelloRpt");
			} else {
				ctx.getApplicationLogger().log("pagamento.invioCarrelloRptNoRedirect");
			}


		} catch (GovPayException e) {
			response.setCodEsitoOperazione(Utils.fromEsitoOperazioneGovpay(e.getCodEsito()));
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			try {
				ctx.getApplicationLogger().log("pagamento.avviaTransazioneKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione(), e.getCausa() != null ? e.getCausa() : "[--Non specificata--]");
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
		} catch (Exception e) {
			log.error("Richiesta di pagamento fallita", e);
			GovPayException gpe = new GovPayException(e);
			response.setCodEsitoOperazione(Utils.fromEsitoOperazioneGovpay(gpe.getCodEsito()));
			response.setDescrizioneEsitoOperazione(gpe.getMessage());
			try {
				ctx.getApplicationLogger().log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
		} finally {
			if(ctx != null) {
				PagamentiTelematiciGPAppImpl.setResult(appContext,response);
				ctx.getApplicationLogger().log();
			}
			//			if(bd != null) bd.closeConnection();
		}
		return response;
	}

	@Override
	public GpChiediStatoTransazioneResponse gpChiediStatoTransazione(GpChiediStatoTransazione bodyrichiesta) {
		log.info("Richiesta operazione gpChiediStatoTransazione per la transazione con dominio (" + bodyrichiesta.getCodDominio() + ") iuv (" +  bodyrichiesta.getIuv()+") e ccp (" + bodyrichiesta.getCcp() + ")");
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		GpChiediStatoTransazioneResponse response = new GpChiediStatoTransazioneResponse();
		response.setCodOperazione(ctx.getTransactionId());

		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(user));
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		//		BasicBD bd = null;
		try {
			//			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(appContext, user);
			ctx.getApplicationLogger().log("ws.ricevutaRichiesta");

			verificaApplicazione(applicazioneAutenticata, bodyrichiesta.getCodApplicazione());
			autorizzaPortale(bodyrichiesta.getCodPortale(), portaleAutenticato, bd);
			ctx.getApplicationLogger().log("ws.autorizzazione");

			LeggiRptDTO leggiRptDTO = new LeggiRptDTO(user);
			leggiRptDTO.setIdDominio(bodyrichiesta.getCodDominio());
			leggiRptDTO.setIuv(bodyrichiesta.getIuv());
			String ccp = bodyrichiesta.getCcp();
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiRptDTO.setCcp(ccp);

			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(leggiRptDTO.getUser(), bodyrichiesta.getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiRptDTO.getUser(),bodyrichiesta.getCodDominio(), null);
			}

			RptDAO ricevuteDAO = new RptDAO(); 

			LeggiRptDTOResponse leggiRptDTOResponse = ricevuteDAO.leggiRpt(leggiRptDTO);
			Rpt rpt = leggiRptDTOResponse.getRpt();
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			response.setTransazione(Gp21Utils.toTransazione(rpt, configWrapper));
			ctx.getApplicationLogger().log("ws.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(Utils.fromEsitoOperazioneGovpay(e.getCodEsito()));
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			try {
				ctx.getApplicationLogger().log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
		} catch (Exception e) {
			log.error("Richiesta di stato pagamento fallita", e);
			GovPayException gpe = new GovPayException(e);
			response.setCodEsitoOperazione(Utils.fromEsitoOperazioneGovpay(gpe.getCodEsito()));
			response.setDescrizioneEsitoOperazione(gpe.getMessage());
			try {
				ctx.getApplicationLogger().log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
		} finally {
			if(ctx != null) {
				PagamentiTelematiciGPAppImpl.setResult(appContext,response);
				ctx.getApplicationLogger().log();
			}
			//			if(bd != null) bd.closeConnection();
		}
		return response;
	}

	@Override
	public GpChiediSceltaWispResponse gpChiediSceltaWisp(GpChiediSceltaWisp bodyrichiesta) {
		log.warn("Operazione gpChiediSceltaWisp non disponibile");
		throw new RuntimeException(new NotImplementedException("Operazione non disponibile"));
	}


	@Override
	public GpChiediListaVersamentiResponse gpChiediListaVersamenti(GpChiediListaVersamenti bodyrichiesta) {
		log.warn("Operazione gpChiediListaVersamenti non disponibile");
		throw new RuntimeException(new NotImplementedException("Operazione non disponibile"));
	}

	@Override
	public GpAvviaRichiestaStornoResponse gpAvviaRichiestaStorno(GpAvviaRichiestaStorno bodyrichiesta) {
		log.warn("Operazione gpAvviaRichiestaStorno non disponibile");
		throw new RuntimeException(new NotImplementedException("Operazione non disponibile"));
	}

	@Override
	public GpChiediStatoRichiestaStornoResponse gpChiediStatoRichiestaStorno(GpChiediStatoRichiestaStorno bodyrichiesta) {
		log.warn("Operazione gpChiediStatoRichiestaStorno non disponibile");
		throw new RuntimeException(new NotImplementedException("Operazione non disponibile"));
	}

	@Override
	public GpChiediStatoVersamentoResponse gpChiediStatoVersamento(GpChiediStatoVersamento bodyrichiesta) {
		log.info("Richiesta operazione gpChiediStatoVersamento");
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		GpChiediStatoVersamentoResponse response = new GpChiediStatoVersamentoResponse();
		response.setCodOperazione(ctx.getTransactionId());

		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(user));
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		try {
			//			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(appContext, user);
			ctx.getApplicationLogger().log("ws.ricevutaRichiesta");

			verificaApplicazione(applicazioneAutenticata, bodyrichiesta.getCodApplicazione());
			autorizzaPortale(bodyrichiesta.getCodPortale(), portaleAutenticato, bd);
			ctx.getApplicationLogger().log("ws.autorizzazione");

			PendenzeDAO pendenzeDAO = new PendenzeDAO();
			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(user);
			leggiPendenzaDTO.setCodA2A(bodyrichiesta.getCodApplicazione());
			leggiPendenzaDTO.setCodPendenza(bodyrichiesta.getCodVersamentoEnte());
			LeggiPendenzaDTOResponse leggiPendenza = pendenzeDAO.leggiPendenza(leggiPendenzaDTO ); 

			Versamento versamento = leggiPendenza.getVersamento();

			// TODO usare tutti i parametri
			//			Versamento versamento = versamentoBusiness.chiediVersamento(portaleAutenticato, bodyrichiesta.getCodApplicazione(), bodyrichiesta.getCodVersamentoEnte(), bodyrichiesta.getBundleKey(), bodyrichiesta.getCodUnivocoDebitore(), bodyrichiesta.getCodDominio(), bodyrichiesta.getIuv());

			if(bodyrichiesta.getCodUnivocoDebitore() != null && !bodyrichiesta.getCodUnivocoDebitore().equalsIgnoreCase(versamento.getAnagraficaDebitore().getCodUnivoco())) {
				throw new GovPayException(it.govpay.core.beans.EsitoOperazione.PRT_005);
			}

			response.setCodApplicazione(versamento.getApplicazione(configWrapper).getCodApplicazione());
			response.setCodDominio(versamento.getDominio(configWrapper).getCodDominio());
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			response.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
			response.setDataScadenza(versamento.getDataScadenza());
			response.setImportoTotale(versamento.getImportoTotale());
			response.setStato(StatoVersamento.valueOf(versamento.getStatoVersamento().toString()));


			if(versamento.getCausaleVersamento() instanceof Versamento.CausaleSemplice)
				response.setCausale(((Versamento.CausaleSemplice) versamento.getCausaleVersamento()).getCausale());
			if(versamento.getCausaleVersamento() instanceof Versamento.CausaleSpezzoni)
				response.getSpezzoneCausale().addAll(((Versamento.CausaleSpezzoni) versamento.getCausaleVersamento()).getSpezzoni());
			if(versamento.getCausaleVersamento() instanceof Versamento.CausaleSpezzoniStrutturati) {
				Versamento.CausaleSpezzoniStrutturati c = (Versamento.CausaleSpezzoniStrutturati) versamento.getCausaleVersamento();
				for(int i = 0; i<c.getImporti().size(); i++) {
					SpezzoneCausaleStrutturata s = new SpezzoneCausaleStrutturata();
					s.setCausale(c.getSpezzoni().get(i));
					s.setImporto(c.getImporti().get(i));
					response.getSpezzoneCausaleStrutturata().add(s);
				}
			}

			Iuv iuv = versamento.getIuv(configWrapper);
			if(iuv != null) {
				it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento.getApplicazione(configWrapper), versamento.getDominio(configWrapper), iuv, versamento.getImportoTotale());
				response.setIuv(iuv.getIuv());
				//				if(portaleAutenticato.getVersione().compareTo(Versione.GP_02_03_00) >= 0) // Versione 2.3
				//					response.setNumeroAvviso(iuvGenerato.getNumeroAvviso());
				response.setBarCode(iuvGenerato.getBarCode());
				response.setQrCode(iuvGenerato.getQrCode());
			}

			List<Rpt> rpts = leggiPendenza.getRpts();
			for(Rpt rpt : rpts) {
				response.getTransazione().add(Gp21Utils.toTransazione(rpt, configWrapper));
			}
			ctx.getApplicationLogger().log("ws.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(Utils.fromEsitoOperazioneGovpay(e.getCodEsito()));
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			try {
				ctx.getApplicationLogger().log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
		} catch (Exception e) {
			log.error("Richiesta di verifica stato versamento fallita", e);
			GovPayException gpe = new GovPayException(e);
			response.setCodEsitoOperazione(Utils.fromEsitoOperazioneGovpay(gpe.getCodEsito()));
			response.setDescrizioneEsitoOperazione(gpe.getMessage());
			try {
				ctx.getApplicationLogger().log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
		} finally {
			if(ctx != null) {
				PagamentiTelematiciGPAppImpl.setResult(appContext,response);
				ctx.getApplicationLogger().log();
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

	private void verificaApplicazione(Applicazione applicazioneAutenticata, String codApplicazione) throws GovPayException {
		if(!applicazioneAutenticata.getCodApplicazione().equals(codApplicazione))
			throw new GovPayException(it.govpay.core.beans.EsitoOperazione.APP_002, applicazioneAutenticata.getCodApplicazione(), codApplicazione);

		if(!applicazioneAutenticata.getUtenza().isAbilitato())
			throw new GovPayException(it.govpay.core.beans.EsitoOperazione.APP_001, applicazioneAutenticata.getCodApplicazione());
	}

//	private Portale getPortaleAutenticato(GpContext appContext, Authentication authentication) throws GovPayException, ServiceException {
//		if(wsCtxt.getUserPrincipal() == null) {
//			throw new GovPayException(it.govpay.core.beans.EsitoOperazione.AUT_000);
//		}
//
//		Portale prt = null;
//		try {
//			prt =  AnagraficaManager.getPortaleByPrincipal(bd, wsCtxt.getUserPrincipal().getName());
//		} catch (NotFoundException e) {
//			throw new GovPayException(it.govpay.core.beans.EsitoOperazione.AUT_002, wsCtxt.getUserPrincipal().getName());
//		}
//
//		if(prt != null) {
//			Actor from = new Actor();
//			from.setName(prt.getCodPortale());
//			from.setType(GpContext.TIPO_SOGGETTO_PRT);
//			appContext.getTransaction().setFrom(from);
//			appContext.getTransaction().getClient().setName(prt.getCodPortale());
//		}
//
//		return prt;
//	}
//
//	private void autorizzaPortale(String codPortale, Portale portaleAutenticato, BasicBD bd) throws GovPayException, ServiceException {
//		Portale portale = null;
//		try {
//			portale = AnagraficaManager.getPortale(bd, codPortale);
//		} catch (NotFoundException e) {
//			throw new GovPayException(it.govpay.core.beans.EsitoOperazione.PRT_000, codPortale);
//		}
//
//		if(!portale.isAbilitato())
//			throw new GovPayException(it.govpay.core.beans.EsitoOperazione.PRT_001, codPortale);
//
//		if(!portale.getCodPortale().equals(portaleAutenticato.getCodPortale()))
//			throw new GovPayException(it.govpay.core.beans.EsitoOperazione.PRT_002, portaleAutenticato.getCodPortale(), codPortale);
//	}
}
