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
package it.govpay.web.ws.v3;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.Versamento;
import it.govpay.core.business.model.AvviaRichiestaStornoDTO;
import it.govpay.core.business.model.AvviaRichiestaStornoDTOResponse;
import it.govpay.core.business.model.AvviaTransazioneDTO;
import it.govpay.core.business.model.AvviaTransazioneDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.CredentialUtils;
import it.govpay.core.utils.Gp21Utils;
import it.govpay.core.utils.Gp25Utils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.RptUtils;
import it.govpay.model.Iuv;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.commons.MetaInfo;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.v2_3.commons.Mittente;
import it.govpay.servizi.v2_5.PagamentiTelematiciGPPrt;
import it.govpay.servizi.v2_5.gpprt.GpAvviaRichiestaStorno;
import it.govpay.servizi.v2_5.gpprt.GpAvviaRichiestaStornoResponse;
import it.govpay.servizi.v2_5.gpprt.GpAvviaTransazionePagamento;
import it.govpay.servizi.v2_5.gpprt.GpAvviaTransazionePagamentoResponse;
import it.govpay.servizi.v2_5.gpprt.GpChiediListaPsp;
import it.govpay.servizi.v2_5.gpprt.GpChiediListaPspResponse;
import it.govpay.servizi.v2_5.gpprt.GpChiediListaVersamenti;
import it.govpay.servizi.v2_5.gpprt.GpChiediListaVersamentiResponse;
import it.govpay.servizi.v2_5.gpprt.GpChiediStatoRichiestaStorno;
import it.govpay.servizi.v2_5.gpprt.GpChiediStatoRichiestaStornoResponse;
import it.govpay.servizi.v2_5.gpprt.GpChiediStatoTransazione;
import it.govpay.servizi.v2_5.gpprt.GpChiediStatoTransazioneResponse;
import it.govpay.servizi.v2_5.gpprt.GpChiediStatoVersamento;
import it.govpay.servizi.v2_5.gpprt.GpChiediStatoVersamentoResponse;
import it.govpay.servizi.v2_5.gpprt.GpChiediStatoVersamentoResponse.SpezzoneCausaleStrutturata;
import it.govpay.web.ws.Utils;

@WebService(serviceName = "PagamentiTelematiciGPPrtService",
endpointInterface = "it.govpay.servizi.v2_5.PagamentiTelematiciGPPrt",
targetNamespace = "http://www.govpay.it/servizi/v2_5",
portName = "GPPrtPort",
wsdlLocation="/wsdl/GpPrt_2.5.wsdl",
name="PagamentiTelematiciGPPrtService")

@HandlerChain(file="../../../../../handler-chains/handler-chain-gpws.xml")

@org.apache.cxf.annotations.SchemaValidation

public class PagamentiTelematiciGPPrtImpl implements PagamentiTelematiciGPPrt {
	
	@Resource
	WebServiceContext wsCtxt;
	
	private static Logger log = LoggerWrapperFactory.getLogger(PagamentiTelematiciGPPrtImpl.class);
	
	@Override
	public GpChiediListaPspResponse gpChiediListaPsp(GpChiediListaPsp bodyrichiesta) {
		log.debug("Richiesta operazione gpChiediListaPsp");
		GpChiediListaPspResponse response = new GpChiediListaPspResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			it.govpay.core.business.Applicazione applicazioneBusiness = new it.govpay.core.business.Applicazione(bd);
			applicazioneBusiness.getApplicazioneAutenticata(getUtenzaAutenticata(bd)); 
			
			ctx.log("ws.ricevutaRichiesta");
			
			it.govpay.core.business.Psp pspBusiness = new it.govpay.core.business.Psp(bd);
			List<Psp> psps = pspBusiness.chiediListaPsp();
			response.getPsp().addAll(Gp25Utils.toPsp(psps));
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpChiediListaPspResponse) gpe.getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpChiediListaPspResponse) new GovPayException(e).getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(MDC.get("op"));
		return response;
	}
	
	@Override
	public GpAvviaTransazionePagamentoResponse gpAvviaTransazionePagamento(GpAvviaTransazionePagamento bodyrichiesta, MetaInfo metaInfo) {
		GpAvviaTransazionePagamentoResponse response = new GpAvviaTransazionePagamentoResponse();
		GpContext ctx = GpThreadLocal.get();
		Utils.loadMetaInfo(ctx, metaInfo);
		BasicBD bd = null;
		try {
			log.info("Richiesta operazione gpAvviaTransazionePagamento");
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			// Aggiungo il codSessionePortale al PaymentContext
			ctx.getPagamentoCtx().setCodSessionePortale(bodyrichiesta.getCodSessionePortale());
			ctx.getContext().getRequest().addGenericProperty(new Property("codSessionePortale", bodyrichiesta.getCodSessionePortale() != null ? bodyrichiesta.getCodSessionePortale() : "--Non fornito--"));
			
			it.govpay.core.business.Applicazione applicazioneBusiness = new it.govpay.core.business.Applicazione(bd);
			Applicazione applicazioneAutenticata = 	applicazioneBusiness.getApplicazioneAutenticata(getUtenzaAutenticata(bd)); 
			ctx.log("ws.ricevutaRichiesta");
			
			applicazioneBusiness.autorizzaApplicazione(bodyrichiesta.getCodPortale(), applicazioneAutenticata, bd);
			ctx.log("ws.autorizzazione");
			
			ctx.getPagamentoCtx().setCarrello(true);
			String codCarrello = RptUtils.buildUUID35();
			ctx.getPagamentoCtx().setCodCarrello(codCarrello);
			ctx.getContext().getRequest().addGenericProperty(new Property("codCarrello", codCarrello));
			ctx.setCorrelationId(codCarrello);
			ctx.log("pagamento.avviaTransazioneCarrelloWISP20");
			
			it.govpay.core.business.Pagamento pagamentoBusiness = new it.govpay.core.business.Pagamento(bd);
			
			AvviaTransazioneDTO dto = new AvviaTransazioneDTO();
			dto.setAggiornaSeEsisteB(bodyrichiesta.isAggiornaSeEsiste());
			dto.setAutenticazione(bodyrichiesta.getAutenticazione().value());
			dto.setIbanAddebito(bodyrichiesta.getIbanAddebito());
			dto.setApplicazione(applicazioneAutenticata);
			dto.setUrlRitorno(bodyrichiesta.getUrlRitorno());
			dto.setVersamentoOrVersamentoRef(bodyrichiesta.getVersamentoOrVersamentoRef());
			dto.setVersante(bodyrichiesta.getVersante());
			
			AvviaTransazioneDTOResponse dtoResponse = pagamentoBusiness.avviaTransazione(dto);
			response.getRifTransazione().addAll(Gp25Utils.toRifTransazione(dtoResponse.getRifTransazioni()));
			response.setPspSessionId(dtoResponse.getCodSessione());
			response.setUrlRedirect(dtoResponse.getPspRedirectURL());
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			if(response.getPspSessionId() != null) {
				ctx.getContext().getResponse().addGenericProperty(new Property("codPspSession", response.getPspSessionId()));
			}
			
			if(response.getPspSessionId() != null) {
				ctx.log("pagamento.invioCarrelloRpt");
			} else {
				ctx.log("pagamento.invioCarrelloRptNoRedirect");
			}
			
		} catch (GovPayException gpe) {
			response = (GpAvviaTransazionePagamentoResponse) gpe.getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpAvviaTransazionePagamentoResponse) new GovPayException(e).getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(MDC.get("op"));
		return response;
	}

	@Override
	public GpChiediStatoTransazioneResponse gpChiediStatoTransazione(GpChiediStatoTransazione bodyrichiesta) {
		log.info("Richiesta operazione gpChiediStatoTransazione per la transazione con dominio (" + bodyrichiesta.getCodDominio() + ") iuv (" +  bodyrichiesta.getIuv()+") e ccp (" + bodyrichiesta.getCcp() + ")");
		GpContext ctx = GpThreadLocal.get();
		GpChiediStatoTransazioneResponse response = new GpChiediStatoTransazioneResponse();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			it.govpay.core.business.Applicazione applicazioneBusiness = new it.govpay.core.business.Applicazione(bd);
			Applicazione applicazioneAutenticata = applicazioneBusiness.getApplicazioneAutenticata(getUtenzaAutenticata(bd));
			
			ctx.log("ws.ricevutaRichiesta");
			
			applicazioneBusiness.autorizzaApplicazione(bodyrichiesta.getCodPortale(), applicazioneAutenticata, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Rpt rptBusiness = new it.govpay.core.business.Rpt(bd);
			Rpt rpt = rptBusiness.chiediTransazione(applicazioneAutenticata, bodyrichiesta.getCodDominio(), bodyrichiesta.getIuv(), bodyrichiesta.getCcp());
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			response.setTransazione(Gp25Utils.toTransazione(rpt, bd));
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpChiediStatoTransazioneResponse) gpe.getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpChiediStatoTransazioneResponse) new GovPayException(e).getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(MDC.get("op"));
		return response;
	}

	@Override
	public GpChiediListaVersamentiResponse gpChiediListaVersamenti(GpChiediListaVersamenti bodyrichiesta) {
		log.info("Richiesta operazione gpChiediListaVersamenti dal portale (" +  bodyrichiesta.getCodPortale() +") per il debitore (" + bodyrichiesta.getCodUnivocoDebitore() + ")");
		GpContext ctx = GpThreadLocal.get();
		GpChiediListaVersamentiResponse response = new GpChiediListaVersamentiResponse();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			it.govpay.core.business.Applicazione applicazioneBusiness = new it.govpay.core.business.Applicazione(bd);
			Applicazione applicazioneAutenticata = applicazioneBusiness.getApplicazioneAutenticata(getUtenzaAutenticata(bd));
			
			ctx.log("ws.ricevutaRichiesta");
			
			applicazioneBusiness.autorizzaApplicazione(bodyrichiesta.getCodPortale(), applicazioneAutenticata, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			List<Versamento> versamenti = versamentoBusiness.chiediVersamenti(applicazioneAutenticata, bodyrichiesta.getCodPortale(), bodyrichiesta.getCodUnivocoDebitore(), Gp21Utils.toStatiVersamento(bodyrichiesta.getStato()), Gp21Utils.toFilterSort(bodyrichiesta.getOrdinamento()));
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			for(Versamento versamento : versamenti) {
				response.getVersamento().add(Gp25Utils.toVersamento(versamento, bd));
			}
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpChiediListaVersamentiResponse) gpe.getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpChiediListaVersamentiResponse) new GovPayException(e).getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(MDC.get("op"));
		return response;
	}
	
	@Override
	public GpAvviaRichiestaStornoResponse gpAvviaRichiestaStorno(GpAvviaRichiestaStorno bodyrichiesta) {
		log.info("Richiesta operazione gpAvviaTransazionePagamento dal portale (" +  bodyrichiesta.getCodPortale() +")");
		GpContext ctx = GpThreadLocal.get();
		GpAvviaRichiestaStornoResponse response = new GpAvviaRichiestaStornoResponse();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", bodyrichiesta.getCodDominio()));
			ctx.getContext().getRequest().addGenericProperty(new Property("iuv", bodyrichiesta.getIuv()));
			ctx.getContext().getRequest().addGenericProperty(new Property("ccp", bodyrichiesta.getCcp()));
			
			it.govpay.core.business.Applicazione applicazioneBusiness = new it.govpay.core.business.Applicazione(bd);
			Applicazione applicazioneAutenticata = applicazioneBusiness.getApplicazioneAutenticata(getUtenzaAutenticata(bd));
			
			ctx.log("ws.ricevutaRichiesta");
			
			applicazioneBusiness.autorizzaApplicazione(bodyrichiesta.getCodPortale(), applicazioneAutenticata, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Pagamento pagamentoBusiness = new it.govpay.core.business.Pagamento(bd);
			
			AvviaRichiestaStornoDTO dto = new AvviaRichiestaStornoDTO();
			dto.setCausaleRevoca(bodyrichiesta.getCausaleRevoca());
			dto.setCcp(bodyrichiesta.getCcp());
			dto.setCodDominio(bodyrichiesta.getCodDominio());
			dto.setDatiAggiuntivi(bodyrichiesta.getDatiAggiuntivi());
			dto.setIuv(bodyrichiesta.getIuv());
			dto.setApplicazione(applicazioneAutenticata);
			AvviaRichiestaStornoDTOResponse dtoResponse = pagamentoBusiness.avviaStorno(dto);
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			response.setCodRichiestaStorno(dtoResponse.getCodRichiestaStorno());
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpAvviaRichiestaStornoResponse) gpe.getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpAvviaRichiestaStornoResponse) new GovPayException(e).getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(MDC.get("op"));
		return response;
	}

	@Override
	public GpChiediStatoRichiestaStornoResponse gpChiediStatoRichiestaStorno(GpChiediStatoRichiestaStorno bodyrichiesta) {
		log.info("Richiesta operazione gpChiediStatoRichiestaStorno per la richiesta (" + bodyrichiesta.getCodRichiestaStorno() + ")");
		GpContext ctx = GpThreadLocal.get();
		GpChiediStatoRichiestaStornoResponse response = new GpChiediStatoRichiestaStornoResponse();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			it.govpay.core.business.Applicazione applicazioneBusiness = new it.govpay.core.business.Applicazione(bd);
			Applicazione applicazioneAutenticata = applicazioneBusiness.getApplicazioneAutenticata(getUtenzaAutenticata(bd));
			
			ctx.log("ws.ricevutaRichiesta");
			
			applicazioneBusiness.autorizzaApplicazione(bodyrichiesta.getCodPortale(), applicazioneAutenticata, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Pagamento pagamentoBusiness = new it.govpay.core.business.Pagamento(bd);
			Rr rr = pagamentoBusiness.chiediStorno(applicazioneAutenticata, bodyrichiesta.getCodRichiestaStorno());
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			response.setStorno(Gp25Utils.toStorno(rr, bd));
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpChiediStatoRichiestaStornoResponse) gpe.getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpChiediStatoRichiestaStornoResponse) new GovPayException(e).getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(MDC.get("op"));
		return response;
	}
	
	@Override
	public GpChiediStatoVersamentoResponse gpChiediStatoVersamento(GpChiediStatoVersamento bodyrichiesta) {
		log.info("Richiesta operazione gpChiediStatoVersamento");
		GpContext ctx = GpThreadLocal.get();
		GpChiediStatoVersamentoResponse response = new GpChiediStatoVersamentoResponse();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			it.govpay.core.business.Applicazione applicazioneBusiness = new it.govpay.core.business.Applicazione(bd);
			Applicazione applicazioneAutenticata = applicazioneBusiness.getApplicazioneAutenticata(getUtenzaAutenticata(bd));
			
			ctx.log("ws.ricevutaRichiesta");
			
			applicazioneBusiness.autorizzaApplicazione(bodyrichiesta.getCodPortale(), applicazioneAutenticata, bd);
			ctx.log("ws.autorizzazione");
		
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			
			Versamento versamento = versamentoBusiness.chiediVersamento(applicazioneAutenticata, bodyrichiesta.getCodApplicazione(), bodyrichiesta.getCodVersamentoEnte(), bodyrichiesta.getBundleKey(), bodyrichiesta.getCodUnivocoDebitore(), bodyrichiesta.getCodDominio(), bodyrichiesta.getIuv());
			
			if(bodyrichiesta.getCodUnivocoDebitore() != null && !bodyrichiesta.getCodUnivocoDebitore().equalsIgnoreCase(versamento.getAnagraficaDebitore().getCodUnivoco())) {
				throw new GovPayException(EsitoOperazione.PRT_005);
			}
			
			response.setCodApplicazione(versamento.getApplicazione(bd).getCodApplicazione());
			response.setCodDominio(versamento.getUo(bd).getDominio(bd).getCodDominio());
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
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
			
			Iuv iuv = versamento.getIuv(bd);
			if(iuv != null) {
				it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento.getApplicazione(bd), versamento.getUo(bd).getDominio(bd), iuv, versamento.getImportoTotale());
				response.setIuv(iuv.getIuv());
				response.setNumeroAvviso(iuvGenerato.getNumeroAvviso());
				response.setBarCode(iuvGenerato.getBarCode());
				response.setQrCode(iuvGenerato.getQrCode());
			}
			
			List<Rpt> rpts = versamento.getRpt(bd);
			for(Rpt rpt : rpts) {
				response.getTransazione().add(Gp25Utils.toTransazione(rpt, bd));
			}
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpChiediStatoVersamentoResponse) gpe.getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpChiediStatoVersamentoResponse) new GovPayException(e).getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(MDC.get("op"));
		return response;
	}
	
	private Utenza getUtenzaAutenticata(BasicBD bd) throws GovPayException, ServiceException {
		Utenza user = null;
		try {
			HttpServletRequest request = (HttpServletRequest) wsCtxt.getMessageContext().get(MessageContext.SERVLET_REQUEST);  
			user = CredentialUtils.getUser(request, log);
		} catch (Exception e) {
			throw new GovPayException(EsitoOperazione.AUT_001, wsCtxt.getUserPrincipal().getName());
		}
		return user;
	}
}
