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

import java.util.Calendar;
import java.util.List;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.business.model.CaricaIuvDTO;
import it.govpay.core.business.model.CaricaIuvDTOResponse;
import it.govpay.core.business.model.GeneraIuvDTO;
import it.govpay.core.business.model.GeneraIuvDTOResponse;
import it.govpay.core.business.model.Iuv;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.Gp21Utils;
import it.govpay.core.utils.Gp23Utils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.model.Applicazione;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.RendicontazionePagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.wrapper.RendicontazionePagamentoBD;
import it.govpay.bd.wrapper.filters.RendicontazionePagamentoFilter;
import it.govpay.servizi.v2_3.PagamentiTelematiciGPApp;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.v2_3.commons.GpResponse;
import it.govpay.servizi.v2_3.commons.Mittente;
import it.govpay.servizi.commons.MetaInfo;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.v2_3.gpapp.GpAnnullaVersamento;
import it.govpay.servizi.v2_3.gpapp.GpCaricaIuv;
import it.govpay.servizi.v2_3.gpapp.GpCaricaIuvResponse;
import it.govpay.servizi.v2_3.gpapp.GpCaricaVersamento;
import it.govpay.servizi.v2_3.gpapp.GpCaricaVersamentoResponse;
import it.govpay.servizi.v2_3.gpapp.GpChiediFlussoRendicontazione;
import it.govpay.servizi.v2_3.gpapp.GpChiediFlussoRendicontazioneResponse;
import it.govpay.servizi.v2_3.gpapp.GpChiediListaFlussiRendicontazione;
import it.govpay.servizi.v2_3.gpapp.GpChiediListaFlussiRendicontazioneResponse;
import it.govpay.servizi.v2_3.gpapp.GpChiediStatoVersamento;
import it.govpay.servizi.v2_3.gpapp.GpChiediStatoVersamentoResponse;
import it.govpay.servizi.v2_3.gpapp.GpGeneraIuv;
import it.govpay.servizi.v2_3.gpapp.GpGeneraIuvResponse;
import it.govpay.servizi.v2_3.gpapp.GpNotificaPagamento;
import it.govpay.web.ws.Utils;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.proxy.Actor;

@WebService(serviceName = "PagamentiTelematiciGPAppService",
endpointInterface = "it.govpay.servizi.v2_3.PagamentiTelematiciGPApp",
targetNamespace = "http://www.govpay.it/servizi/v2_3",
portName = "GPAppPort",
wsdlLocation="classpath:wsdl/GpApp_2.3.wsdl",
name="PagamentiTelematiciGPAppService")

@HandlerChain(file="../../../../../handler-chains/handler-chain-gpws.xml")

@org.apache.cxf.annotations.SchemaValidation
public class PagamentiTelematiciGPAppImpl implements PagamentiTelematiciGPApp {

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LogManager.getLogger();

	@Override
	public GpGeneraIuvResponse gpGeneraIuv(GpGeneraIuv bodyrichiesta, MetaInfo metaInfo) {
		log.info("Richiesta operazione gpGeneraIuv di " + bodyrichiesta.getIuvRichiesto().size() + " Iuv per (" + bodyrichiesta.getCodApplicazione() + ")");
		GpGeneraIuvResponse response = new GpGeneraIuvResponse();
		GpContext ctx = GpThreadLocal.get();
		Utils.loadMetaInfo(ctx, metaInfo);
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("ws.ricevutaRichiesta");
			verificaApplicazione(applicazioneAutenticata, bodyrichiesta.getCodApplicazione());
			it.govpay.core.business.Iuv iuvBusiness = new it.govpay.core.business.Iuv(bd);
			GeneraIuvDTO dto = new GeneraIuvDTO();
			dto.setApplicazioneAutenticata(applicazioneAutenticata);
			dto.setCodApplicazione(bodyrichiesta.getCodApplicazione());
			dto.setCodDominio(bodyrichiesta.getCodDominio());
			dto.getIuvRichiesto().addAll(Gp23Utils.toIuvRichiesto(bodyrichiesta.getIuvRichiesto()));
			GeneraIuvDTOResponse dtoResponse = iuvBusiness.generaIUV(dto);
			response.getIuvGenerato().addAll(Gp23Utils.toIuvGenerato(dtoResponse.getIuvGenerato()));
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpGeneraIuvResponse) gpe.getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpGeneraIuvResponse) new GovPayException(e).getWsResponse(response, "ws.ricevutaRichiestaKo", log);
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
	public GpCaricaIuvResponse gpCaricaIuv(GpCaricaIuv bodyrichiesta) {
		log.info("Richiesta operazione gpCaricaIuv di " + bodyrichiesta.getIuvGenerato().size() + " Iuv per (" + bodyrichiesta.getCodApplicazione() + ")");
		GpCaricaIuvResponse response = new GpCaricaIuvResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("ws.ricevutaRichiesta");
			verificaApplicazione(applicazioneAutenticata, bodyrichiesta.getCodApplicazione());
			it.govpay.core.business.Iuv iuvBusiness = new it.govpay.core.business.Iuv(bd);
			CaricaIuvDTO dto = new CaricaIuvDTO();
			dto.setApplicazioneAutenticata(applicazioneAutenticata);
			dto.setCodApplicazione(bodyrichiesta.getCodApplicazione());
			dto.setCodDominio(bodyrichiesta.getCodDominio());
			dto.getIuvDaCaricare().addAll(Gp23Utils.toIuvDaCaricare(bodyrichiesta.getIuvGenerato()));
			CaricaIuvDTOResponse dtoResponse = iuvBusiness.caricaIUV(dto);
			response.getIuvCaricato().addAll(Gp23Utils.toIuvCaricato(dtoResponse.getIuvCaricato()));			
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpCaricaIuvResponse) gpe.getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpCaricaIuvResponse) new GovPayException(e).getWsResponse(response, "ws.ricevutaRichiestaKo", log);
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
	public GpCaricaVersamentoResponse gpCaricaVersamento(GpCaricaVersamento bodyrichiesta, MetaInfo metaInfo) {
		log.info("Richiesta operazione gpCaricaVersamento per il versamento (" + bodyrichiesta.getVersamento().getCodVersamentoEnte() + ") dell'applicazione (" +  bodyrichiesta.getVersamento().getCodApplicazione()+") con generazione IUV (" + bodyrichiesta.isGeneraIuv() + ")");
		GpCaricaVersamentoResponse response = new GpCaricaVersamentoResponse();
		GpContext ctx = GpThreadLocal.get();
		Utils.loadMetaInfo(ctx, metaInfo);
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("ws.ricevutaRichiesta");
			
			ctx.getContext().getRequest().addGenericProperty(new Property("codApplicazione", bodyrichiesta.getVersamento().getCodApplicazione()));
			ctx.getContext().getRequest().addGenericProperty(new Property("codVersamentoEnte", bodyrichiesta.getVersamento().getCodVersamentoEnte()));
			ctx.setCorrelationId(bodyrichiesta.getVersamento().getCodApplicazione() + bodyrichiesta.getVersamento().getCodVersamentoEnte());
			ctx.log("versamento.carica");
			
			verificaApplicazione(applicazioneAutenticata, bodyrichiesta.getVersamento().getCodApplicazione());
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			it.govpay.servizi.commons.Versamento versamento = bodyrichiesta.getVersamento();
			it.govpay.bd.model.Versamento versamentoModel = VersamentoUtils.toVersamentoModel(versamento, bd);
			boolean aggiornaSeEsiste = true;
			if(bodyrichiesta.isAggiornaSeEsiste() != null) {
				aggiornaSeEsiste = bodyrichiesta.isAggiornaSeEsiste();
			}
			it.govpay.model.Iuv iuv = versamentoBusiness.caricaVersamento(applicazioneAutenticata, versamentoModel, bodyrichiesta.isGeneraIuv(), aggiornaSeEsiste);

			if(iuv != null) {
				Iuv iuvGenerato = IuvUtils.toIuv(versamentoModel.getApplicazione(bd), versamentoModel.getUo(bd).getDominio(bd), iuv, versamento.getImportoTotale());
				response.setIuvGenerato(Gp23Utils.toIuvGenerato(iuvGenerato));
				ctx.getContext().getResponse().addGenericProperty(new Property("codDominio", iuvGenerato.getCodDominio()));
				ctx.getContext().getResponse().addGenericProperty(new Property("iuv", iuvGenerato.getIuv()));
				ctx.log("versamento.caricaOkIuv");
			} else {
				ctx.log("versamento.caricaOk");
			}
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpCaricaVersamentoResponse) gpe.getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpCaricaVersamentoResponse) new GovPayException(e).getWsResponse(response, "ws.ricevutaRichiestaKo", log);
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
	public GpResponse gpAnnullaVersamento(GpAnnullaVersamento bodyrichiesta) {
		log.info("Richiesta operazione gpChiediAnnullaVersamento per il versamento (" + bodyrichiesta.getCodVersamentoEnte() + ") dell'applicazione (" +  bodyrichiesta.getCodApplicazione()+")");
		GpResponse response = new GpResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("ws.ricevutaRichiesta");
			
			ctx.getContext().getRequest().addGenericProperty(new Property("codApplicazione", bodyrichiesta.getCodApplicazione()));
			ctx.getContext().getRequest().addGenericProperty(new Property("codVersamentoEnte", bodyrichiesta.getCodVersamentoEnte()));
			ctx.setCorrelationId(bodyrichiesta.getCodApplicazione() + bodyrichiesta.getCodVersamentoEnte());
			ctx.log("versamento.annulla");
			
			verificaApplicazione(applicazioneAutenticata, bodyrichiesta.getCodApplicazione());
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			versamentoBusiness.annullaVersamento(applicazioneAutenticata, bodyrichiesta.getCodApplicazione(), bodyrichiesta.getCodVersamentoEnte());
			ctx.log("versamento.annullaOk");
			
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpResponse) gpe.getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpResponse) new GovPayException(e).getWsResponse(response, "ws.ricevutaRichiestaKo", log);
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
	public GpResponse gpNotificaPagamento(GpNotificaPagamento bodyrichiesta) {
		log.info("Richiesta operazione gpNotificaPagamento per il versamento (" + bodyrichiesta.getCodVersamentoEnte() + ") dell'applicazione (" +  bodyrichiesta.getCodApplicazione()+")");
		GpResponse response = new GpResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("ws.ricevutaRichiesta");
			verificaApplicazione(applicazioneAutenticata, bodyrichiesta.getCodApplicazione());
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			versamentoBusiness.notificaPagamento(applicazioneAutenticata, bodyrichiesta.getCodApplicazione(), bodyrichiesta.getCodVersamentoEnte());
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpResponse) gpe.getWsResponse(response, "ws.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpResponse) new GovPayException(e).getWsResponse(response, "ws.ricevutaRichiestaKo", log);
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
		log.info("Richiesta operazione gpChiediStatoVersamento per il versamento (" + bodyrichiesta.getCodVersamentoEnte() + ") dell'applicazione (" +  bodyrichiesta.getCodApplicazione()+")");
		GpChiediStatoVersamentoResponse response = new GpChiediStatoVersamentoResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("ws.ricevutaRichiesta");
			verificaApplicazione(applicazioneAutenticata, bodyrichiesta.getCodApplicazione());
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			Versamento versamento = versamentoBusiness.chiediVersamento(bodyrichiesta.getCodApplicazione(), bodyrichiesta.getCodVersamentoEnte(), null, null, null, null);
			response.setCodApplicazione(versamento.getApplicazione(bd).getCodApplicazione());
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			response.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
			response.setStato(StatoVersamento.valueOf(versamento.getStatoVersamento().toString()));
			List<Rpt> rpts = versamento.getRpt(bd);
			for(Rpt rpt : rpts) {
				response.getTransazione().add(Gp21Utils.toTransazione(applicazioneAutenticata.getVersione(), rpt, bd));
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

	@Override
	public GpChiediListaFlussiRendicontazioneResponse gpChiediListaFlussiRendicontazione(GpChiediListaFlussiRendicontazione bodyrichiesta) {
		log.info("Richiesta operazione gpChiediListaFlussiRendicontazione");
		GpChiediListaFlussiRendicontazioneResponse response = new GpChiediListaFlussiRendicontazioneResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			Applicazione applicazione = getApplicazioneAutenticata(bd);
			verificaApplicazione(applicazione, bodyrichiesta.getCodApplicazione());
			ctx.log("ws.ricevutaRichiesta");
			it.govpay.core.business.Rendicontazioni rendicontazioneBusiness = new it.govpay.core.business.Rendicontazioni(bd);
			Calendar fine = Calendar.getInstance();
			fine.setTime(bodyrichiesta.getDataFine());
			fine.set(Calendar.HOUR_OF_DAY, 23);
			fine.set(Calendar.MINUTE, 59);
			fine.set(Calendar.SECOND, 59);
			fine.set(Calendar.MILLISECOND, 999);
			List<Fr> rendicontazioni = rendicontazioneBusiness.chiediListaRendicontazioni(applicazione, bodyrichiesta.getCodDominio(), bodyrichiesta.getCodApplicazione(), bodyrichiesta.getDataInizio(), fine.getTime());
			for(Fr frModel : rendicontazioni) {
				response.getFlussoRendicontazione().add(Gp23Utils.toFr(frModel, bd));
			}
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setMittente(Mittente.GOV_PAY);
			ctx.log("ws.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpChiediListaFlussiRendicontazioneResponse) gpe.getWsResponse(response, "gprnd.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpChiediListaFlussiRendicontazioneResponse) new GovPayException(e).getWsResponse(response, "gprnd.ricevutaRichiestaKo", log);
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
	public GpChiediFlussoRendicontazioneResponse gpChiediFlussoRendicontazione(GpChiediFlussoRendicontazione bodyrichiesta) {
		
		log.info("Richiesta operazione gpChiediFlussoRendicontazione");
		GpChiediFlussoRendicontazioneResponse response = new GpChiediFlussoRendicontazioneResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			Applicazione applicazione = getApplicazioneAutenticata(bd);
			ctx.log("gprnd.ricevutaRichiesta");

			//Autorizzazione alla richiesta: controllo che il dominio sia tra quelli abilitati per l'applicazione
			Fr frModel = new FrBD(bd).getFr(bodyrichiesta.getCodFlusso());
			
			verificaApplicazione(applicazione, bodyrichiesta.getCodApplicazione());
			
			RendicontazionePagamentoBD rendicontazionePagamentoBD = new RendicontazionePagamentoBD(bd); 
			RendicontazionePagamentoFilter filter = rendicontazionePagamentoBD.newFilter();
			filter.setCodFlusso(bodyrichiesta.getCodFlusso());
			filter.setCodApplicazione(applicazione.getId());
			List<RendicontazionePagamento> rends = rendicontazionePagamentoBD.findAll(filter);
			response.setFlussoRendicontazione(Gp23Utils.toFr(frModel, rends, bd));
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setMittente(Mittente.GOV_PAY);
			ctx.log("gprnd.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpChiediFlussoRendicontazioneResponse) gpe.getWsResponse(response, "gprnd.ricevutaRichiestaKo", log);
		} catch (Exception e) {
			response = (GpChiediFlussoRendicontazioneResponse) new GovPayException(e).getWsResponse(response, "gprnd.ricevutaRichiestaKo", log);
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

	private Applicazione getApplicazioneAutenticata(BasicBD bd) throws GovPayException, ServiceException {
		if(wsCtxt.getUserPrincipal() == null) {
			throw new GovPayException(EsitoOperazione.AUT_000);
		}

		Applicazione app = null;
		try {
			app = AnagraficaManager.getApplicazioneByPrincipal(bd, wsCtxt.getUserPrincipal().getName());
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.AUT_001, wsCtxt.getUserPrincipal().getName());
		}
		
		if(app != null) {
			Actor from = new Actor();
			from.setName(app.getCodApplicazione());
			from.setType(GpContext.TIPO_SOGGETTO_APP);
			GpThreadLocal.get().getTransaction().setFrom(from);
			GpThreadLocal.get().getTransaction().getClient().setName(app.getCodApplicazione());
		}
		return app;
	}
	
	private void verificaApplicazione(Applicazione applicazioneAutenticata, String codApplicazione) throws GovPayException {
		if(!applicazioneAutenticata.getCodApplicazione().equals(codApplicazione))
			throw new GovPayException(EsitoOperazione.APP_002, applicazioneAutenticata.getCodApplicazione(), codApplicazione);
		
		if(!applicazioneAutenticata.isAbilitato())
			throw new GovPayException(EsitoOperazione.APP_001, applicazioneAutenticata.getCodApplicazione());
	}

}
