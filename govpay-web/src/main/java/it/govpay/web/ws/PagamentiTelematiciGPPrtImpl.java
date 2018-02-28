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

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.WebServiceContext;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Versamento;
import it.govpay.core.business.model.AvviaRichiestaStornoDTO;
import it.govpay.core.business.model.AvviaRichiestaStornoDTOResponse;
import it.govpay.core.business.model.AvviaTransazioneDTO;
import it.govpay.core.business.model.AvviaTransazioneDTOResponse;
import it.govpay.core.business.model.SceltaWISP;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.Gp21Utils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.model.Iuv;
import it.govpay.bd.model.Applicazione;
import it.govpay.model.Versionabile.Versione;
import it.govpay.servizi.PagamentiTelematiciGPPrt;
import it.govpay.servizi.commons.Canale;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.commons.MetaInfo;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.commons.TipoSceltaWisp;
import it.govpay.servizi.commons.TipoVersamento;
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

@WebService(serviceName = "PagamentiTelematiciGPPrtService",
endpointInterface = "it.govpay.servizi.PagamentiTelematiciGPPrt",
targetNamespace = "http://www.govpay.it/servizi/",
portName = "GPPrtPort",
wsdlLocation = "/wsdl/GpPrt.wsdl")

@HandlerChain(file="../../../../handler-chains/handler-chain-gpws.xml")

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
			applicazioneBusiness.getApplicazioneAutenticata(getPrincipal());
			ctx.log("ws.ricevutaRichiesta");
			
			it.govpay.core.business.Psp pspBusiness = new it.govpay.core.business.Psp(bd);
			List<Psp> psps = pspBusiness.chiediListaPsp();
			response.getPsp().addAll(Gp21Utils.toPsp(psps));
			
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			log.error("Richiesta lista psp fallita",e);
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
			Applicazione applicazioneAutenticata = applicazioneBusiness.getApplicazioneAutenticata(getPrincipal());
			ctx.log("ws.ricevutaRichiesta");
			
			applicazioneBusiness.autorizzaApplicazione(bodyrichiesta.getCodPortale(), applicazioneAutenticata, bd);
			ctx.log("ws.autorizzazione");
			
			if(bodyrichiesta.getCanale() != null) {
				ctx.getContext().getRequest().addGenericProperty(new Property("codPsp", bodyrichiesta.getCanale().getCodPsp()));
				ctx.getContext().getRequest().addGenericProperty(new Property("codCanale", bodyrichiesta.getCanale().getCodCanale()));
				ctx.getContext().getRequest().addGenericProperty(new Property("tipoVersamento", bodyrichiesta.getCanale().getTipoVersamento().name()));
			} else {
				ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", bodyrichiesta.getSceltaWisp().getCodDominio()));
				ctx.getContext().getRequest().addGenericProperty(new Property("codKeyPA", bodyrichiesta.getSceltaWisp().getCodKeyPA()));
				ctx.getContext().getRequest().addGenericProperty(new Property("codKeyWISP", bodyrichiesta.getSceltaWisp().getCodKeyWISP()));
			}
			
			if(bodyrichiesta.getVersamentoOrVersamentoRef().size() > 1) {
				ctx.getPagamentoCtx().setCarrello(true);
				String codCarrello = RptUtils.buildUUID35();
				ctx.getPagamentoCtx().setCodCarrello(codCarrello);
				ctx.getContext().getRequest().addGenericProperty(new Property("codCarrello", codCarrello));
				ctx.setCorrelationId(codCarrello);
				if(bodyrichiesta.getCanale() != null) {
					ctx.log("pagamento.avviaTransazioneCarrello");
				} else {
					ctx.log("pagamento.avviaTransazioneCarrelloWISP");
				}
					
			} else {
				
				Object v = bodyrichiesta.getVersamentoOrVersamentoRef().get(0);
				String codDominio = null, codApplicazione = null, codVersamentoEnte = null, iuv = null, bundlekey = null, codUnivocoDebitore = null;
				
				if(v instanceof it.govpay.servizi.commons.Versamento) {
					it.govpay.servizi.commons.Versamento versamento = (it.govpay.servizi.commons.Versamento) v;
					ctx.getContext().getRequest().addGenericProperty(new Property("codApplicazione", versamento.getCodApplicazione()));
					ctx.getContext().getRequest().addGenericProperty(new Property("codVersamentoEnte", versamento.getCodVersamentoEnte()));
					ctx.setCorrelationId(versamento.getCodApplicazione() + versamento.getCodVersamentoEnte());
					if(bodyrichiesta.getCanale() != null) {
						ctx.log("pagamento.avviaTransazione");
					} else {
						ctx.log("pagamento.avviaTransazioneWISP");
					}
				} else {
					it.govpay.servizi.commons.VersamentoKey versamento = (it.govpay.servizi.commons.VersamentoKey) v;
					Iterator<JAXBElement<String>> iterator = versamento.getContent().iterator();
					while(iterator.hasNext()){
						JAXBElement<String> element = iterator.next();
						
						if(element.getName().equals(VersamentoUtils._VersamentoKeyBundlekey_QNAME)) {
							bundlekey = element.getValue();
						}
						if(element.getName().equals(VersamentoUtils._VersamentoKeyCodUnivocoDebitore_QNAME)) {
							codUnivocoDebitore = element.getValue();
						}
						if(element.getName().equals(VersamentoUtils._VersamentoKeyCodApplicazione_QNAME)) {
							codApplicazione = element.getValue();
						}
						if(element.getName().equals(VersamentoUtils._VersamentoKeyCodDominio_QNAME)) {
							codDominio = element.getValue();
						}
						if(element.getName().equals(VersamentoUtils._VersamentoKeyCodVersamentoEnte_QNAME)) {
							codVersamentoEnte = element.getValue();
						}
						if(element.getName().equals(VersamentoUtils._VersamentoKeyIuv_QNAME)) {
							iuv = element.getValue();
						}
					}
					
					if(codApplicazione != null && codVersamentoEnte != null) {
						ctx.getContext().getRequest().addGenericProperty(new Property("codApplicazione", codApplicazione));
						ctx.getContext().getRequest().addGenericProperty(new Property("codVersamentoEnte", codVersamentoEnte));
						ctx.setCorrelationId(codApplicazione + codVersamentoEnte);
						if(bodyrichiesta.getCanale() != null) {
							ctx.log("pagamento.avviaTransazioneRef");
						} else {
							ctx.log("pagamento.avviaTransazioneRefWISP");
						}
					}
					
					if(codDominio != null && iuv != null) {
						ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", codDominio));
						ctx.getContext().getRequest().addGenericProperty(new Property("iuv", iuv));
						ctx.setCorrelationId(codDominio + iuv);
						if(bodyrichiesta.getCanale() != null) {
							ctx.log("pagamento.avviaTransazioneRefIuv");
						} else {
							ctx.log("pagamento.avviaTransazioneRefIuvWISP");
						}
					}
					
					if(codApplicazione != null && bundlekey != null) {
						ctx.getContext().getRequest().addGenericProperty(new Property("codApplicazione", codApplicazione));
						ctx.getContext().getRequest().addGenericProperty(new Property("bundleKey", bundlekey));
						ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", codDominio != null ? codDominio : GpContext.NOT_SET));
						ctx.getContext().getRequest().addGenericProperty(new Property("codUnivocoDebitore", codUnivocoDebitore != null ? codUnivocoDebitore : GpContext.NOT_SET));
						
						ctx.setCorrelationId(codApplicazione + bundlekey + (codUnivocoDebitore != null ? codUnivocoDebitore : "") + (codDominio != null ? codDominio : ""));
						if(bodyrichiesta.getCanale() != null) {
							ctx.log("pagamento.avviaTransazioneRefBundle");
						} else {
							ctx.log("pagamento.avviaTransazioneRefBundleWISP");
						}
					}
				}
			}
			
			
			it.govpay.bd.model.Canale canale = null;
			
			if(bodyrichiesta.getCanale() == null) {
				
				it.govpay.core.business.Wisp wisp = new it.govpay.core.business.Wisp(bd);
				
				Dominio dominio = null;
				try {
					dominio = AnagraficaManager.getDominio(bd, bodyrichiesta.getSceltaWisp().getCodDominio());
				} catch (NotFoundException e) {
					throw new GovPayException(EsitoOperazione.DOM_000, bodyrichiesta.getSceltaWisp().getCodDominio());
				}
				
				SceltaWISP scelta = null;
				
				try {
					scelta = wisp.chiediScelta(applicazioneAutenticata, dominio, bodyrichiesta.getSceltaWisp().getCodKeyPA(), bodyrichiesta.getSceltaWisp().getCodKeyWISP());
				} catch (Exception e) {
					ctx.log("pagamento.risoluzioneWispKo", e.getMessage());
					throw e;
				}
				
				if(scelta.isSceltaEffettuata() && !scelta.isPagaDopo()) {
					canale = scelta.getCanale();
				}
				if(!scelta.isSceltaEffettuata()) {
					ctx.log("pagamento.risoluzioneWispOkNoScelta");
					throw new GovPayException(EsitoOperazione.WISP_003);
				}
				if(scelta.isPagaDopo()) {
					ctx.log("pagamento.risoluzioneWispOkPagaDopo");
					throw new GovPayException(EsitoOperazione.WISP_004);
				}
				
				ctx.getContext().getRequest().addGenericProperty(new Property("codPsp", canale.getPsp(bd).getCodPsp()));
				ctx.getContext().getRequest().addGenericProperty(new Property("codCanale", canale.getCodCanale()));
				ctx.getContext().getRequest().addGenericProperty(new Property("tipoVersamento", canale.getTipoVersamento().getCodifica()));
				
				ctx.log("pagamento.risoluzioneWispOkCanale");
			} else {
				try {
					it.govpay.model.Canale.TipoVersamento tipoVersamento = it.govpay.model.Canale.TipoVersamento.toEnum(bodyrichiesta.getCanale().getTipoVersamento().toString());
					canale = AnagraficaManager.getCanale(bd, bodyrichiesta.getCanale().getCodPsp(), bodyrichiesta.getCanale().getCodCanale(), tipoVersamento);
				} catch (NotFoundException e) {
					throw new GovPayException(EsitoOperazione.PSP_000, bodyrichiesta.getCanale().getCodPsp(), bodyrichiesta.getCanale().getCodCanale(), bodyrichiesta.getCanale().getTipoVersamento().toString());
				}
			}
			
			ctx.log("pagamento.identificazioneCanale");
			it.govpay.core.business.Pagamento pagamentoBusiness = new it.govpay.core.business.Pagamento(bd);
			
			AvviaTransazioneDTO dto = new AvviaTransazioneDTO();
			dto.setAggiornaSeEsisteB(bodyrichiesta.isAggiornaSeEsiste());
			dto.setAutenticazione(bodyrichiesta.getAutenticazione().value());
			dto.setCanale(canale);
			dto.setIbanAddebito(bodyrichiesta.getIbanAddebito());
			dto.setApplicazione(applicazioneAutenticata);
			dto.setUrlRitorno(bodyrichiesta.getUrlRitorno());
			dto.setVersamentoOrVersamentoRef(bodyrichiesta.getVersamentoOrVersamentoRef());
			dto.setVersante(bodyrichiesta.getVersante());
			AvviaTransazioneDTOResponse dtoResponse = pagamentoBusiness.avviaTransazione(dto);
			response.getRifTransazione().addAll(Gp21Utils.toRifTransazione(dtoResponse.getRifTransazioni()));
			response.setPspSessionId(dtoResponse.getCodSessione());
			response.setUrlRedirect(dtoResponse.getPspRedirectURL());
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			
			if(response.getPspSessionId() != null) {
				ctx.getContext().getResponse().addGenericProperty(new Property("codPspSession", response.getPspSessionId()));
			}
			
			if(ctx.getPagamentoCtx().isCarrello()) {
				if(response.getPspSessionId() != null) {
					ctx.log("pagamento.invioCarrelloRpt");
				} else {
					ctx.log("pagamento.invioCarrelloRptNoRedirect");
				}
			} else {
				ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", response.getRifTransazione().get(0).getCodDominio()));
				ctx.getContext().getRequest().addGenericProperty(new Property("iuv", response.getRifTransazione().get(0).getIuv()));
				ctx.getContext().getRequest().addGenericProperty(new Property("ccp", response.getRifTransazione().get(0).getCcp()));
				if(response.getPspSessionId() != null) {
					ctx.log("pagamento.invioRpt");
				} else {
					ctx.log("pagamento.invioRptNoRedirect");
				}
			}
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("pagamento.avviaTransazioneKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione(), e.getCausa() != null ? e.getCausa() : "[--Non specificata--]");
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("pagamento.avviaTransazioneFail", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			log.error("Richiesta di pagamento fallita", e);
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
			Applicazione applicazioneAutenticata = applicazioneBusiness.getApplicazioneAutenticata(getPrincipal());
			ctx.log("ws.ricevutaRichiesta");
			
			applicazioneBusiness.autorizzaApplicazione(bodyrichiesta.getCodPortale(), applicazioneAutenticata, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Rpt rptBusiness = new it.govpay.core.business.Rpt(bd);
			Rpt rpt = rptBusiness.chiediTransazione(applicazioneAutenticata, bodyrichiesta.getCodDominio(), bodyrichiesta.getIuv(), bodyrichiesta.getCcp());
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			// TODO Nardi controllare versione 
			response.setTransazione(Gp21Utils.toTransazione(applicazioneAutenticata.getConnettoreVerifica().getVersione(), rpt, bd));
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			log.error("Richiesta di stato pagamento fallita", e);
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
	public GpChiediSceltaWispResponse gpChiediSceltaWisp(GpChiediSceltaWisp bodyrichiesta) {
		log.info("Richiesta operazione gpChiediSceltaWisp dal portale (" +  bodyrichiesta.getCodPortale() +")");
		GpContext ctx = GpThreadLocal.get();
		GpChiediSceltaWispResponse response = new GpChiediSceltaWispResponse();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			it.govpay.core.business.Applicazione applicazioneBusiness = new it.govpay.core.business.Applicazione(bd);
			Applicazione applicazioneAutenticata = applicazioneBusiness.getApplicazioneAutenticata(getPrincipal());
			ctx.log("ws.ricevutaRichiesta");
			
			applicazioneBusiness.autorizzaApplicazione(bodyrichiesta.getCodPortale(), applicazioneAutenticata, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Wisp wisp = new it.govpay.core.business.Wisp(bd);
			
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, bodyrichiesta.getCodDominio());
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.DOM_000, bodyrichiesta.getCodDominio());
			}
			SceltaWISP scelta = wisp.chiediScelta(applicazioneAutenticata, dominio, bodyrichiesta.getCodKeyPA(), bodyrichiesta.getCodKeyWISP());
			if(scelta.isSceltaEffettuata()) {
				if(scelta.isPagaDopo()) {
					response.setScelta(TipoSceltaWisp.PAGA_DOPO);
				} else {
					response.setScelta(TipoSceltaWisp.SI);
					Canale canale = new Canale();
					canale.setCodCanale(scelta.getCanale().getCodCanale());
					canale.setCodPsp(scelta.getCanale().getPsp(bd).getCodPsp());
					canale.setTipoVersamento(TipoVersamento.fromValue(scelta.getCanale().getTipoVersamento().getCodifica()));
					response.setCanale(canale);
				}
			} else {
				response.setScelta(TipoSceltaWisp.NO);
			}
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.log("ws.ricevutaRichiestaOk");
			log.info("SceltaWISP recuperata (" + response.getScelta() + ")");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			log.error("Richiesta di risoluzione wisp fallita", e);
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
			Applicazione applicazioneAutenticata = applicazioneBusiness.getApplicazioneAutenticata(getPrincipal());
			ctx.log("ws.ricevutaRichiesta");
			
			applicazioneBusiness.autorizzaApplicazione(bodyrichiesta.getCodPortale(), applicazioneAutenticata, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			List<Versamento> versamenti = versamentoBusiness.chiediVersamenti(applicazioneAutenticata, bodyrichiesta.getCodPortale(), bodyrichiesta.getCodUnivocoDebitore(), Gp21Utils.toStatiVersamento(bodyrichiesta.getStato()), Gp21Utils.toFilterSort(bodyrichiesta.getOrdinamento()));
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			for(Versamento versamento : versamenti) {
				// TODO Nardi controllare versione 
				response.getVersamento().add(Gp21Utils.toVersamento(applicazioneAutenticata.getConnettoreVerifica().getVersione(), versamento, bd));
			}
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			log.error("Richiesta di stato versamento fallita", e);
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
			Applicazione applicazioneAutenticata = applicazioneBusiness.getApplicazioneAutenticata(getPrincipal());
			ctx.log("ws.ricevutaRichiesta");
			
			applicazioneBusiness.autorizzaApplicazione(bodyrichiesta.getCodPortale(), applicazioneAutenticata, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Pagamento pagamentoBusiness = new it.govpay.core.business.Pagamento(bd);
			AvviaRichiestaStornoDTO dto = new AvviaRichiestaStornoDTO();
			dto.setCausaleRevoca(bodyrichiesta.getCausaleRevoca());
			dto.setCcp(bodyrichiesta.getCcp());
			dto.setCodDominio(bodyrichiesta.getCodDominio());
			dto.setCodApplicazione(bodyrichiesta.getCodPortale());
			dto.setDatiAggiuntivi(bodyrichiesta.getDatiAggiuntivi());
			dto.setIuv(bodyrichiesta.getIuv());
			dto.setApplicazione(applicazioneAutenticata);
			AvviaRichiestaStornoDTOResponse dtoResponse = pagamentoBusiness.avviaStorno(dto);
			response.setCodRichiestaStorno(dtoResponse.getCodRichiestaStorno());
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			log.error("Richiesta di storno fallita", e);
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
			Applicazione applicazioneAutenticata = applicazioneBusiness.getApplicazioneAutenticata(getPrincipal());
			ctx.log("ws.ricevutaRichiesta");
			
			applicazioneBusiness.autorizzaApplicazione(bodyrichiesta.getCodPortale(), applicazioneAutenticata, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Pagamento pagamentoBusiness = new it.govpay.core.business.Pagamento(bd);
			Rr rr = pagamentoBusiness.chiediStorno(applicazioneAutenticata, bodyrichiesta.getCodRichiestaStorno());
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			// TODO Nardi controllare versione 
			response.setStorno(Gp21Utils.toStorno(rr, applicazioneAutenticata.getConnettoreVerifica().getVersione(), bd));
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			log.error("Richiesta di stato storno fallita", e);
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
			Applicazione applicazioneAutenticata = applicazioneBusiness.getApplicazioneAutenticata(getPrincipal());
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
			
			Iuv iuv = versamento.getIuv(bd);
			if(iuv != null) {
				it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento.getApplicazione(bd), versamento.getUo(bd).getDominio(bd), iuv, versamento.getImportoTotale());
				response.setIuv(iuv.getIuv());
				// TODO Nardi controllare versione 
				if(applicazioneAutenticata.getConnettoreVerifica().getVersione().compareTo(Versione.GP_SOAP_02_03) >= 0)
					response.setNumeroAvviso(iuvGenerato.getNumeroAvviso());
				response.setBarCode(iuvGenerato.getBarCode());
				response.setQrCode(iuvGenerato.getQrCode());
			}
			
			List<Rpt> rpts = versamento.getRpt(bd);
			for(Rpt rpt : rpts) {
				// TODO Nardi controllare versione 
				response.getTransazione().add(Gp21Utils.toTransazione(applicazioneAutenticata.getConnettoreVerifica().getVersione(), rpt, bd));
			}
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("ws.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
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
	
	private String getPrincipal() {
		if(wsCtxt.getUserPrincipal() != null)
			return wsCtxt.getUserPrincipal().getName();
		return null;
	}
}
