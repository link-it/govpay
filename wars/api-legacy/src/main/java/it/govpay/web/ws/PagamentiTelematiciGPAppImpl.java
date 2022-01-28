/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2022 Link.it srl (http://www.link.it).
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
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.context.core.Actor;
import org.openspcoop2.utils.logger.constants.context.Result;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.business.model.AnnullaVersamentoDTO;
import it.govpay.core.business.model.Iuv;
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PatchPendenzaDTO;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.model.PatchOp;
import it.govpay.model.StatoPendenza;
import it.govpay.model.PatchOp.OpEnum;
import it.govpay.servizi.PagamentiTelematiciGPApp;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.commons.GpResponse;
import it.govpay.servizi.commons.MetaInfo;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.gpapp.GpAnnullaVersamento;
import it.govpay.servizi.gpapp.GpCaricaIuv;
import it.govpay.servizi.gpapp.GpCaricaIuvResponse;
import it.govpay.servizi.gpapp.GpCaricaVersamento;
import it.govpay.servizi.gpapp.GpCaricaVersamentoResponse;
import it.govpay.servizi.gpapp.GpChiediFlussoRendicontazione;
import it.govpay.servizi.gpapp.GpChiediFlussoRendicontazioneResponse;
import it.govpay.servizi.gpapp.GpChiediListaFlussiRendicontazione;
import it.govpay.servizi.gpapp.GpChiediListaFlussiRendicontazioneResponse;
import it.govpay.servizi.gpapp.GpChiediStatoVersamento;
import it.govpay.servizi.gpapp.GpChiediStatoVersamentoResponse;
import it.govpay.servizi.gpapp.GpGeneraIuv;
import it.govpay.servizi.gpapp.GpGeneraIuvResponse;
import it.govpay.servizi.gpapp.GpNotificaPagamento;
import it.govpay.web.utils.Gp21Utils;
import it.govpay.web.utils.Utils;

@WebService(serviceName = "PagamentiTelematiciGPAppService",
endpointInterface = "it.govpay.servizi.PagamentiTelematiciGPApp",
targetNamespace = "http://www.govpay.it/servizi/",
portName = "GPAppPort",
wsdlLocation="classpath:wsdl/GpApp.wsdl")

//@HandlerChain(file="../../../../handler-chains/handler-chain-gpws.xml")

@org.apache.cxf.annotations.SchemaValidation
public class PagamentiTelematiciGPAppImpl implements PagamentiTelematiciGPApp {

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LoggerWrapperFactory.getLogger(PagamentiTelematiciGPAppImpl.class);

	@Override
	public GpGeneraIuvResponse gpGeneraIuv(GpGeneraIuv bodyrichiesta, MetaInfo metaInfo) {
		log.warn("Operazione gpGeneraIuv non disponibile");
		throw new RuntimeException(new NotImplementedException("Operazione non disponibile"));
	}

	@Override
	public GpCaricaIuvResponse gpCaricaIuv(GpCaricaIuv bodyrichiesta) {
		log.info("Richiesta operazione gpCaricaIuv di " + bodyrichiesta.getIuvGenerato().size() + " Iuv per (" + bodyrichiesta.getCodApplicazione() + ")");
		GpCaricaIuvResponse response = new GpCaricaIuvResponse();
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		response.setCodOperazione(ctx.getTransactionId());

		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(user));

		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		try {
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(appContext, user);
			appContext.getEventoCtx().setIdA2A(applicazioneAutenticata.getCodApplicazione());
			ctx.getApplicationLogger().log("ws.ricevutaRichiesta");
			verificaApplicazione(applicazioneAutenticata, bodyrichiesta.getCodApplicazione());
			response.getIuvCaricato().addAll(Gp21Utils.toIuvCaricato(configWrapper, bodyrichiesta, applicazioneAutenticata));
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.getApplicationLogger().log("ws.ricevutaRichiestaOk");
			appContext.getEventoCtx().setEsito(Esito.OK);
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(Utils.fromEsitoOperazioneGovpay(e.getCodEsito()));
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			try {
				ctx.getApplicationLogger().log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(e.getMessage());
			appContext.getEventoCtx().setSottotipoEsito(response.getCodEsitoOperazione().name());
			if(response.getCodEsitoOperazione().equals(EsitoOperazione.INTERNAL))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else 
				appContext.getEventoCtx().setEsito(Esito.KO);
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			try {
				ctx.getApplicationLogger().log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(e.getMessage());
			appContext.getEventoCtx().setSottotipoEsito(response.getCodEsitoOperazione().name());
			appContext.getEventoCtx().setEsito(Esito.FAIL);
		} finally {
			if(ctx != null) {
				PagamentiTelematiciGPAppImpl.setResult(appContext,response);
			}
		}

		return response;
	}

	@Override
	public GpCaricaVersamentoResponse gpCaricaVersamento(GpCaricaVersamento bodyrichiesta, MetaInfo metaInfo) {
		log.info("Richiesta operazione gpCaricaVersamento per il versamento (" + bodyrichiesta.getVersamento().getCodVersamentoEnte() + ") dell'applicazione (" +  bodyrichiesta.getVersamento().getCodApplicazione()+") con generazione IUV (" + bodyrichiesta.isGeneraIuv() + ")");
		GpCaricaVersamentoResponse response = new GpCaricaVersamentoResponse();
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		Utils.loadMetaInfo(appContext, metaInfo);
		response.setCodOperazione(ctx.getTransactionId());

		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(user));
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		try {
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(appContext, user);
			ctx.getApplicationLogger().log("ws.ricevutaRichiesta");

			appContext.getEventoCtx().setIdA2A(bodyrichiesta.getVersamento().getCodApplicazione());
			appContext.getEventoCtx().setIdPendenza(bodyrichiesta.getVersamento().getCodVersamentoEnte());
			appContext.getRequest().addGenericProperty(new Property("codApplicazione", bodyrichiesta.getVersamento().getCodApplicazione()));
			appContext.getRequest().addGenericProperty(new Property("codVersamentoEnte", bodyrichiesta.getVersamento().getCodVersamentoEnte()));
			appContext.setCorrelationId(bodyrichiesta.getVersamento().getCodApplicazione() + bodyrichiesta.getVersamento().getCodVersamentoEnte());
			ctx.getApplicationLogger().log("versamento.carica");

			verificaApplicazione(applicazioneAutenticata, bodyrichiesta.getVersamento().getCodApplicazione());
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento();
			it.govpay.core.dao.commons.Versamento versamento = Gp21Utils.toVersamentoCommons(bodyrichiesta.getVersamento());
			it.govpay.bd.model.Versamento versamentoModel = VersamentoUtils.toVersamentoModel(versamento);
			boolean aggiornaSeEsiste = true;
			if(bodyrichiesta.isAggiornaSeEsiste() != null) {
				aggiornaSeEsiste = bodyrichiesta.isAggiornaSeEsiste();
			}
			versamentoModel = versamentoBusiness.caricaVersamento(versamentoModel, bodyrichiesta.isGeneraIuv(), aggiornaSeEsiste, null, null, null);
			Iuv iuv = IuvUtils.toIuv(versamentoModel, versamentoModel.getApplicazione(configWrapper), versamentoModel.getDominio(configWrapper));

			if(iuv != null) {
				response.setIuvGenerato(Gp21Utils.toIuvGenerato(iuv, applicazioneAutenticata));
				appContext.getResponse().addGenericProperty(new Property("codDominio", iuv.getCodDominio()));
				appContext.getResponse().addGenericProperty(new Property("iuv", iuv.getIuv()));
				ctx.getApplicationLogger().log("versamento.caricaOkIuv");
			} else {
				ctx.getApplicationLogger().log("versamento.caricaOk");
			}
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.getApplicationLogger().log("ws.ricevutaRichiestaOk");
			appContext.getEventoCtx().setEsito(Esito.OK);
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(Utils.fromEsitoOperazioneGovpay(e.getCodEsito()));
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			try {
				ctx.getApplicationLogger().log("versamento.caricaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione(), e.getCausa() != null ? e.getCausa() : "- Non specificata -");
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(e.getMessage());
			appContext.getEventoCtx().setSottotipoEsito(response.getCodEsitoOperazione().name());
			if(response.getCodEsitoOperazione().equals(EsitoOperazione.INTERNAL))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else 
				appContext.getEventoCtx().setEsito(Esito.KO);
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			try {
				ctx.getApplicationLogger().log("versamento.caricaFail", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(e.getMessage());
			appContext.getEventoCtx().setSottotipoEsito(response.getCodEsitoOperazione().name());
			appContext.getEventoCtx().setEsito(Esito.FAIL);
		} finally {
			if(ctx != null) {
				PagamentiTelematiciGPAppImpl.setResult(appContext,response);
			}
		}
		return response;
	}

	@Override
	public GpResponse gpAnnullaVersamento(GpAnnullaVersamento bodyrichiesta) {
		log.info("Richiesta operazione gpChiediAnnullaVersamento per il versamento (" + bodyrichiesta.getCodVersamentoEnte() + ") dell'applicazione (" +  bodyrichiesta.getCodApplicazione()+")");
		GpResponse response = new GpResponse();
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		response.setCodOperazione(ctx.getTransactionId());

		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(user));
		try {
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(appContext, user);
			ctx.getApplicationLogger().log("ws.ricevutaRichiesta");

			appContext.getEventoCtx().setIdA2A(bodyrichiesta.getCodApplicazione());
			appContext.getEventoCtx().setIdPendenza(bodyrichiesta.getCodVersamentoEnte());
			appContext.getRequest().addGenericProperty(new Property("codApplicazione", bodyrichiesta.getCodApplicazione()));
			appContext.getRequest().addGenericProperty(new Property("codVersamentoEnte", bodyrichiesta.getCodVersamentoEnte()));
			appContext.setCorrelationId(bodyrichiesta.getCodApplicazione() + bodyrichiesta.getCodVersamentoEnte());
			ctx.getApplicationLogger().log("versamento.annulla");

			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento();
			AnnullaVersamentoDTO annullaVersamentoDTO = new AnnullaVersamentoDTO(applicazioneAutenticata, bodyrichiesta.getCodApplicazione(), bodyrichiesta.getCodVersamentoEnte());
			versamentoBusiness.annullaVersamento(annullaVersamentoDTO);
			ctx.getApplicationLogger().log("versamento.annullaOk");

			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.getApplicationLogger().log("ws.ricevutaRichiestaOk");
			appContext.getEventoCtx().setEsito(Esito.OK);
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(Utils.fromEsitoOperazioneGovpay(e.getCodEsito()));
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			try {
				ctx.getApplicationLogger().log("versamento.annullaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione(), e.getCausa() != null ? e.getCausa() : "- Non specificata -");
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(e.getMessage());
			appContext.getEventoCtx().setSottotipoEsito(response.getCodEsitoOperazione().name());
			if(response.getCodEsitoOperazione().equals(EsitoOperazione.INTERNAL))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else 
				appContext.getEventoCtx().setEsito(Esito.KO);
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			try {
				ctx.getApplicationLogger().log("versamento.annullaFail", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(e.getMessage());
			appContext.getEventoCtx().setSottotipoEsito(response.getCodEsitoOperazione().name());
			appContext.getEventoCtx().setEsito(Esito.FAIL);
		} finally {
			if(ctx != null) {
				PagamentiTelematiciGPAppImpl.setResult(appContext,response);
			}
		}
		return response;
	}

	@Override
	public GpResponse gpNotificaPagamento(GpNotificaPagamento bodyrichiesta) {
		log.info("Richiesta operazione gpNotificaPagamento per il versamento (" + bodyrichiesta.getCodVersamentoEnte() + ") dell'applicazione (" +  bodyrichiesta.getCodApplicazione()+")");
		GpResponse response = new GpResponse();
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		response.setCodOperazione(ctx.getTransactionId());

		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(user));
		try {
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(appContext, user);
			ctx.getApplicationLogger().log("ws.ricevutaRichiesta");
			verificaApplicazione(applicazioneAutenticata, bodyrichiesta.getCodApplicazione());

			appContext.getEventoCtx().setIdA2A(bodyrichiesta.getCodApplicazione());
			appContext.getEventoCtx().setIdPendenza(bodyrichiesta.getCodVersamentoEnte());
			appContext.getRequest().addGenericProperty(new Property("codApplicazione", bodyrichiesta.getCodApplicazione()));
			appContext.getRequest().addGenericProperty(new Property("codVersamentoEnte", bodyrichiesta.getCodVersamentoEnte()));
			appContext.setCorrelationId(bodyrichiesta.getCodApplicazione() + bodyrichiesta.getCodVersamentoEnte());
			
			PendenzeDAO pendenzeDAO = new PendenzeDAO();
			PatchPendenzaDTO patchPendenzaDTO = new PatchPendenzaDTO(user);
			
			patchPendenzaDTO.setIdA2a(bodyrichiesta.getCodApplicazione());
			patchPendenzaDTO.setIdPendenza(bodyrichiesta.getCodVersamentoEnte());
			
			
			List<PatchOp> lstOp = new ArrayList<>();
			
			PatchOp patchStato = new PatchOp();
			patchStato.setOp(OpEnum.REPLACE);
			patchStato.setPath(PendenzeDAO.PATH_STATO);
			patchStato.setValue(StatoPendenza.ANNULLATA.toString());
			lstOp.add(patchStato);
			
			PatchOp patchDescrizioneStato = new PatchOp();
			patchDescrizioneStato.setOp(OpEnum.REPLACE);
			patchDescrizioneStato.setPath(PendenzeDAO.PATH_DESCRIZIONE_STATO);
			patchDescrizioneStato.setValue("Avviso pagato tramite canali alternativi a pagoPA");
			lstOp.add(patchDescrizioneStato);
			
			pendenzeDAO.patch(patchPendenzaDTO);
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.getApplicationLogger().log("ws.ricevutaRichiestaOk");
			appContext.getEventoCtx().setEsito(Esito.OK);
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(Utils.fromEsitoOperazioneGovpay(e.getCodEsito()));
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			try{
				ctx.getApplicationLogger().log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(e.getMessage());
			appContext.getEventoCtx().setSottotipoEsito(response.getCodEsitoOperazione().name());
			if(response.getCodEsitoOperazione().equals(EsitoOperazione.INTERNAL))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else 
				appContext.getEventoCtx().setEsito(Esito.KO);
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			try {
				ctx.getApplicationLogger().log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(e.getMessage());
			appContext.getEventoCtx().setSottotipoEsito(response.getCodEsitoOperazione().name());
			appContext.getEventoCtx().setEsito(Esito.FAIL);
		} finally {
			if(ctx != null) {
				PagamentiTelematiciGPAppImpl.setResult(appContext,response);
			}
		}
		return response;
	}

	@Override
	public GpChiediStatoVersamentoResponse gpChiediStatoVersamento(GpChiediStatoVersamento bodyrichiesta) {
		log.info("Richiesta operazione gpChiediStatoVersamento per il versamento (" + bodyrichiesta.getCodVersamentoEnte() + ") dell'applicazione (" +  bodyrichiesta.getCodApplicazione()+")");
		GpChiediStatoVersamentoResponse response = new GpChiediStatoVersamentoResponse();
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		response.setCodOperazione(ctx.getTransactionId());

		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(user));
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		try {
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(appContext, user);
			ctx.getApplicationLogger().log("ws.ricevutaRichiesta");
			verificaApplicazione(applicazioneAutenticata, bodyrichiesta.getCodApplicazione());

			appContext.getEventoCtx().setIdA2A(bodyrichiesta.getCodApplicazione());
			appContext.getEventoCtx().setIdPendenza(bodyrichiesta.getCodVersamentoEnte());
			appContext.getRequest().addGenericProperty(new Property("codApplicazione", bodyrichiesta.getCodApplicazione()));
			appContext.getRequest().addGenericProperty(new Property("codVersamentoEnte", bodyrichiesta.getCodVersamentoEnte()));
			appContext.setCorrelationId(bodyrichiesta.getCodApplicazione() + bodyrichiesta.getCodVersamentoEnte());

			PendenzeDAO pendenzeDAO = new PendenzeDAO();
			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(user);
			leggiPendenzaDTO.setCodA2A(bodyrichiesta.getCodApplicazione());
			leggiPendenzaDTO.setCodPendenza(bodyrichiesta.getCodVersamentoEnte());
			LeggiPendenzaDTOResponse leggiPendenza = pendenzeDAO.leggiPendenza(leggiPendenzaDTO ); 

			Versamento versamento = leggiPendenza.getVersamento();

			response.setCodApplicazione(versamento.getApplicazione(configWrapper).getCodApplicazione());
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			response.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
			response.setStato(StatoVersamento.valueOf(versamento.getStatoVersamento().toString()));
			List<Rpt> rpts = leggiPendenza.getRpts();
			for(Rpt rpt : rpts) {
				response.getTransazione().add(Gp21Utils.toTransazione(rpt, configWrapper));
			}
			ctx.getApplicationLogger().log("ws.ricevutaRichiestaOk");
			appContext.getEventoCtx().setEsito(Esito.OK);
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(Utils.fromEsitoOperazioneGovpay(e.getCodEsito()));
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			try {
				ctx.getApplicationLogger().log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(e.getMessage());
			appContext.getEventoCtx().setSottotipoEsito(response.getCodEsitoOperazione().name());
			if(response.getCodEsitoOperazione().equals(EsitoOperazione.INTERNAL))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else 
				appContext.getEventoCtx().setEsito(Esito.KO);
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			try {
				ctx.getApplicationLogger().log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(e.getMessage());
			appContext.getEventoCtx().setSottotipoEsito(response.getCodEsitoOperazione().name());
			appContext.getEventoCtx().setEsito(Esito.FAIL);
		} finally {
			if(ctx != null) {
				PagamentiTelematiciGPAppImpl.setResult(appContext,response);
			}
		}
		return response;
	}

	@Override
	public GpChiediListaFlussiRendicontazioneResponse gpChiediListaFlussiRendicontazione(GpChiediListaFlussiRendicontazione bodyrichiesta) {
		log.warn("Operazione gpChiediListaFlussiRendicontazione non disponibile");
		throw new RuntimeException(new NotImplementedException("Operazione non disponibile"));
	}

	@Override
	public GpChiediFlussoRendicontazioneResponse gpChiediFlussoRendicontazione(GpChiediFlussoRendicontazione bodyrichiesta) {
		log.warn("Operazione gpChiediFlussoRendicontazione non disponibile");
		throw new RuntimeException(new NotImplementedException("Operazione non disponibile"));
	}

	private Applicazione getApplicazioneAutenticata(GpContext appContext, Authentication authentication) throws GovPayException, ServiceException {
		if(AutorizzazioneUtils.getPrincipal(authentication) == null) {
			throw new GovPayException(it.govpay.core.beans.EsitoOperazione.AUT_000);
		}

		GovpayLdapUserDetails authenticationDetails = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		Applicazione app = authenticationDetails.getApplicazione();

		if(app == null) {
			throw new GovPayException(it.govpay.core.beans.EsitoOperazione.AUT_001, AutorizzazioneUtils.getPrincipal(authentication));
		}
		
		if(!app.getUtenza().isAbilitato())
			throw new GovPayException(it.govpay.core.beans.EsitoOperazione.APP_001, app.getCodApplicazione());

		Actor from = new Actor();
		from.setName(app.getCodApplicazione());
		from.setType(GpContext.TIPO_SOGGETTO_APP);
		appContext.getTransaction().setFrom(from);
		appContext.getTransaction().getClient().setName(app.getCodApplicazione());
		
		return app;
	}

	private void verificaApplicazione(Applicazione applicazioneAutenticata, String codApplicazione) throws GovPayException {
		if(!applicazioneAutenticata.getCodApplicazione().equals(codApplicazione))
			throw new GovPayException(it.govpay.core.beans.EsitoOperazione.APP_002, applicazioneAutenticata.getCodApplicazione(), codApplicazione);
	}

	public static void setResult(GpContext appContext, GpResponse response) {
		if(response == null || response.getCodEsitoOperazione() == null) {
			appContext.getTransaction().setResult(Result.INTERNAL_ERROR);
			return;
		}
		switch (response.getCodEsitoOperazione()) {
		case OK:
			appContext.getTransaction().setResult(Result.SUCCESS);
			break;
		case INTERNAL:
			appContext.getTransaction().setResult(Result.INTERNAL_ERROR);
			break;
		default:
			appContext.getTransaction().setResult(Result.PROCESSING_ERROR);
			break;
		}
	}

}
