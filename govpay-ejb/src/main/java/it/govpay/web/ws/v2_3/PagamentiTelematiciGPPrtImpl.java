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
package it.govpay.web.ws.v2_3;

import java.util.Iterator;
import java.util.List;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.business.model.AvviaRichiestaStornoDTO;
import it.govpay.core.business.model.AvviaRichiestaStornoDTOResponse;
import it.govpay.core.business.model.AvviaTransazioneDTO;
import it.govpay.core.business.model.AvviaTransazioneDTOResponse;
import it.govpay.core.business.model.SceltaWISP;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.Gp21Utils;
import it.govpay.core.utils.Gp23Utils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Psp;
import it.govpay.model.Iuv;
import it.govpay.model.Portale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Versamento;
import it.govpay.servizi.v2_3.PagamentiTelematiciGPPrt;
import it.govpay.servizi.v2_3.commons.Mittente;
import it.govpay.servizi.commons.Canale;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.commons.MetaInfo;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.commons.TipoVersamento;
import it.govpay.servizi.commons.TipoSceltaWisp;
import it.govpay.servizi.v2_3.gpprt.GpAvviaRichiestaStorno;
import it.govpay.servizi.v2_3.gpprt.GpAvviaRichiestaStornoResponse;
import it.govpay.servizi.v2_3.gpprt.GpAvviaTransazionePagamento;
import it.govpay.servizi.v2_3.gpprt.GpAvviaTransazionePagamentoResponse;
import it.govpay.servizi.v2_3.gpprt.GpChiediListaPsp;
import it.govpay.servizi.v2_3.gpprt.GpChiediListaPspResponse;
import it.govpay.servizi.v2_3.gpprt.GpChiediListaVersamenti;
import it.govpay.servizi.v2_3.gpprt.GpChiediListaVersamentiResponse;
import it.govpay.servizi.v2_3.gpprt.GpChiediSceltaWisp;
import it.govpay.servizi.v2_3.gpprt.GpChiediSceltaWispResponse;
import it.govpay.servizi.v2_3.gpprt.GpChiediStatoRichiestaStorno;
import it.govpay.servizi.v2_3.gpprt.GpChiediStatoRichiestaStornoResponse;
import it.govpay.servizi.v2_3.gpprt.GpChiediStatoTransazione;
import it.govpay.servizi.v2_3.gpprt.GpChiediStatoTransazioneResponse;
import it.govpay.servizi.v2_3.gpprt.GpChiediStatoVersamento;
import it.govpay.servizi.v2_3.gpprt.GpChiediStatoVersamentoResponse;
import it.govpay.servizi.v2_3.gpprt.GpChiediStatoVersamentoResponse.SpezzoneCausaleStrutturata;
import it.govpay.web.ws.Utils;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.WebServiceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.proxy.Actor;

@WebService(serviceName = "PagamentiTelematiciGPPrtService",
endpointInterface = "it.govpay.servizi.v2_3.PagamentiTelematiciGPPrt",
targetNamespace = "http://www.govpay.it/servizi/v2_3",
portName = "GPPrtPort",
wsdlLocation="classpath:wsdl/GpPrt_2.3.wsdl",
name="PagamentiTelematiciGPPrtService")

@HandlerChain(file="../../../../../handler-chains/handler-chain-gpws.xml")

@org.apache.cxf.annotations.SchemaValidation

public class PagamentiTelematiciGPPrtImpl implements PagamentiTelematiciGPPrt {
	
	@Resource
	WebServiceContext wsCtxt;
	
	private static Logger log = LogManager.getLogger();
	
	@Override
	public GpChiediListaPspResponse gpChiediListaPsp(GpChiediListaPsp bodyrichiesta) {
		log.debug("Richiesta operazione gpChiediListaPsp");
		GpChiediListaPspResponse response = new GpChiediListaPspResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			getPortaleAutenticato(bd);
			
			ctx.log("ws.ricevutaRichiesta");
			
			it.govpay.core.business.Psp pspBusiness = new it.govpay.core.business.Psp(bd);
			List<Psp> psps = pspBusiness.chiediListaPsp();
			response.getPsp().addAll(Gp23Utils.toPsp(psps));
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
		response.setCodOperazione(ThreadContext.get("op"));
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
			
			Portale portaleAutenticato = getPortaleAutenticato(bd);
			ctx.log("ws.ricevutaRichiesta");
			
			autorizzaPortale(bodyrichiesta.getCodPortale(), portaleAutenticato, bd);
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
					scelta = wisp.chiediScelta(portaleAutenticato, dominio, bodyrichiesta.getSceltaWisp().getCodKeyPA(), bodyrichiesta.getSceltaWisp().getCodKeyWISP());
				} catch (Exception e) {
					ctx.log("pagamento.risoluzioneWispKo", e.getMessage());
					throw e;
				}
				
				if(scelta.isSceltaEffettuata() && !scelta.isPagaDopo()) {
					if(scelta.getCodPsp().equals(Rpt.codPspWISP20))
						canale = null;
					else
						try {
							canale = AnagraficaManager.getCanale(bd,  scelta.getCodPsp(), scelta.getCodCanale(), it.govpay.model.Canale.TipoVersamento.toEnum(scelta.getTipoVersamento()));
						} catch (NotFoundException e) {
							throw new GovPayException(EsitoOperazione.WISP_002,  scelta.getCodPsp(), scelta.getCodCanale(), scelta.getTipoVersamento());
						}
				}
				if(!scelta.isSceltaEffettuata()) {
					ctx.log("pagamento.risoluzioneWispOkNoScelta");
					throw new GovPayException(EsitoOperazione.WISP_003);
				}
				if(scelta.isPagaDopo()) {
					ctx.log("pagamento.risoluzioneWispOkPagaDopo");
					throw new GovPayException(EsitoOperazione.WISP_004);
				}
				
				ctx.getContext().getRequest().addGenericProperty(new Property("codPsp", scelta.getCodPsp()));
				ctx.getContext().getRequest().addGenericProperty(new Property("codCanale", scelta.getCodCanale()));
				ctx.getContext().getRequest().addGenericProperty(new Property("tipoVersamento", scelta.getTipoVersamento()));
				
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
			dto.setPortale(portaleAutenticato);
			dto.setUrlRitorno(bodyrichiesta.getUrlRitorno());
			dto.setVersamentoOrVersamentoRef(bodyrichiesta.getVersamentoOrVersamentoRef());
			dto.setVersante(bodyrichiesta.getVersante());
			AvviaTransazioneDTOResponse dtoResponse = pagamentoBusiness.avviaTransazione(dto);
			response.getRifTransazione().addAll(Gp23Utils.toRifTransazione(dtoResponse.getRifTransazioni()));
			response.setPspSessionId(dtoResponse.getCodSessione());
			response.setUrlRedirect(dtoResponse.getPspRedirectURL());
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
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
		response.setCodOperazione(ThreadContext.get("op"));
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
			
			Portale portaleAutenticato = getPortaleAutenticato(bd);
			ctx.log("ws.ricevutaRichiesta");
			
			autorizzaPortale(bodyrichiesta.getCodPortale(), portaleAutenticato, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Pagamento pagamentoBusiness = new it.govpay.core.business.Pagamento(bd);
			Rpt rpt = pagamentoBusiness.chiediTransazione(portaleAutenticato, bodyrichiesta.getCodDominio(), bodyrichiesta.getIuv(), bodyrichiesta.getCcp());
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			response.setTransazione(Gp21Utils.toTransazione(portaleAutenticato.getVersione(), rpt, bd));
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
		response.setCodOperazione(ThreadContext.get("op"));
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
			
			Portale portaleAutenticato = getPortaleAutenticato(bd);
			ctx.log("ws.ricevutaRichiesta");
			
			autorizzaPortale(bodyrichiesta.getCodPortale(), portaleAutenticato, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Wisp wisp = new it.govpay.core.business.Wisp(bd);
			
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, bodyrichiesta.getCodDominio());
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.DOM_000, bodyrichiesta.getCodDominio());
			}
			SceltaWISP scelta = wisp.chiediScelta(portaleAutenticato, dominio, bodyrichiesta.getCodKeyPA(), bodyrichiesta.getCodKeyWISP());
			if(scelta.isSceltaEffettuata()) {
				if(scelta.isPagaDopo()) {
					response.setScelta(TipoSceltaWisp.PAGA_DOPO);
				} else {
					response.setScelta(TipoSceltaWisp.SI);
					Canale canale = new Canale();
					canale.setCodCanale(scelta.getCodCanale());
					canale.setCodPsp(scelta.getCodPsp());
					canale.setTipoVersamento(TipoVersamento.fromValue(scelta.getTipoVersamento()));
					response.setCanale(canale);
				}
			} else {
				response.setScelta(TipoSceltaWisp.NO);
			}
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			ctx.log("ws.ricevutaRichiestaOk");
			log.info("SceltaWISP recuperata (" + response.getScelta() + ")");
		} catch (GovPayException gpe) {
			response = (GpChiediSceltaWispResponse) gpe.getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpChiediSceltaWispResponse) new GovPayException(e).getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(ThreadContext.get("op"));
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
			
			Portale portaleAutenticato = getPortaleAutenticato(bd);
			ctx.log("ws.ricevutaRichiesta");
			
			autorizzaPortale(bodyrichiesta.getCodPortale(), portaleAutenticato, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			List<Versamento> versamenti = versamentoBusiness.chiediVersamenti(portaleAutenticato, bodyrichiesta.getCodPortale(), bodyrichiesta.getCodUnivocoDebitore(), Gp21Utils.toStatiVersamento(bodyrichiesta.getStato()), Gp21Utils.toFilterSort(bodyrichiesta.getOrdinamento()));
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			for(Versamento versamento : versamenti) {
				response.getVersamento().add(Gp23Utils.toVersamento(versamento, bd));
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
		response.setCodOperazione(ThreadContext.get("op"));
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
			
			Portale portaleAutenticato = getPortaleAutenticato(bd);
			ctx.log("ws.ricevutaRichiesta");
			
			autorizzaPortale(bodyrichiesta.getCodPortale(), portaleAutenticato, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Pagamento pagamentoBusiness = new it.govpay.core.business.Pagamento(bd);
			
			AvviaRichiestaStornoDTO dto = new AvviaRichiestaStornoDTO();
			dto.setCausaleRevoca(bodyrichiesta.getCausaleRevoca());
			dto.setCcp(bodyrichiesta.getCcp());
			dto.setCodDominio(bodyrichiesta.getCodDominio());
			dto.setCodPortale(bodyrichiesta.getCodPortale());
			dto.setDatiAggiuntivi(bodyrichiesta.getDatiAggiuntivi());
			dto.setIuv(bodyrichiesta.getIuv());
			dto.setPortale(portaleAutenticato);
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
		response.setCodOperazione(ThreadContext.get("op"));
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
			
			Portale portaleAutenticato = getPortaleAutenticato(bd);
			ctx.log("ws.ricevutaRichiesta");
			
			autorizzaPortale(bodyrichiesta.getCodPortale(), portaleAutenticato, bd);
			ctx.log("ws.autorizzazione");
			
			it.govpay.core.business.Pagamento pagamentoBusiness = new it.govpay.core.business.Pagamento(bd);
			Rr rr = pagamentoBusiness.chiediStorno(portaleAutenticato, bodyrichiesta.getCodRichiestaStorno());
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			response.setStorno(Gp23Utils.toStorno(rr, bd));
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
		response.setCodOperazione(ThreadContext.get("op"));
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
			
			Portale portaleAutenticato = getPortaleAutenticato(bd);
			ctx.log("ws.ricevutaRichiesta");
			
			autorizzaPortale(bodyrichiesta.getCodPortale(), portaleAutenticato, bd);
			ctx.log("ws.autorizzazione");
		
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			
			Versamento versamento = versamentoBusiness.chiediVersamento(portaleAutenticato, bodyrichiesta.getCodApplicazione(), bodyrichiesta.getCodVersamentoEnte(), bodyrichiesta.getBundleKey(), bodyrichiesta.getCodUnivocoDebitore(), bodyrichiesta.getCodDominio(), bodyrichiesta.getIuv());
			
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
				response.getTransazione().add(Gp21Utils.toTransazione(portaleAutenticato.getVersione(), rpt, bd));
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
		response.setCodOperazione(ThreadContext.get("op"));
		return response;
	}
	
	private Portale getPortaleAutenticato(BasicBD bd) throws GovPayException, ServiceException {
		if(wsCtxt.getUserPrincipal() == null) {
			throw new GovPayException(EsitoOperazione.AUT_000);
		}
		
		Portale prt = null;
		try {
			prt =  AnagraficaManager.getPortaleByPrincipal(bd, wsCtxt.getUserPrincipal().getName());
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.AUT_002, wsCtxt.getUserPrincipal().getName());
		}
		
		if(prt != null) {
			Actor from = new Actor();
			from.setName(prt.getCodPortale());
			from.setType(GpContext.TIPO_SOGGETTO_PRT);
			GpThreadLocal.get().getTransaction().setFrom(from);
			GpThreadLocal.get().getTransaction().getClient().setName(prt.getCodPortale());
		}
		
		return prt;
	}
	
	private void autorizzaPortale(String codPortale, Portale portaleAutenticato, BasicBD bd) throws GovPayException, ServiceException {
		Portale portale = null;
		try {
			portale = AnagraficaManager.getPortale(bd, codPortale);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.PRT_000, codPortale);
		}

		if(!portale.isAbilitato())
			throw new GovPayException(EsitoOperazione.PRT_001, codPortale);

		if(!portale.getCodPortale().equals(portaleAutenticato.getCodPortale()))
			throw new GovPayException(EsitoOperazione.PRT_002, portaleAutenticato.getCodPortale(), codPortale);
	}
}
