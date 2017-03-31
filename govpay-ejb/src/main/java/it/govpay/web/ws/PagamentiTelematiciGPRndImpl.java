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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.proxy.Actor;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.RendicontazionePagamento;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.wrapper.RendicontazionePagamentoBD;
import it.govpay.bd.wrapper.filters.RendicontazionePagamentoFilter;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.Gp21Utils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Applicazione;
import it.govpay.bd.model.Fr;
import it.govpay.model.Acl.Servizio;
import it.govpay.servizi.PagamentiTelematiciGPRnd;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.gprnd.GpChiediFlussoRendicontazione;
import it.govpay.servizi.gprnd.GpChiediFlussoRendicontazioneResponse;
import it.govpay.servizi.gprnd.GpChiediListaFlussiRendicontazione;
import it.govpay.servizi.gprnd.GpChiediListaFlussiRendicontazioneResponse;

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
	
	private static Logger log = LogManager.getLogger();
	
	@Override
	public GpChiediListaFlussiRendicontazioneResponse gpChiediListaFlussiRendicontazione(GpChiediListaFlussiRendicontazione bodyrichiesta) {
		log.info("Richiesta operazione gpChiediListaFlussiRendicontazione");
		GpChiediListaFlussiRendicontazioneResponse response = new GpChiediListaFlussiRendicontazioneResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			Applicazione applicazione = getApplicazioneAutenticata(bd);
			ctx.log("gprnd.ricevutaRichiesta");
			it.govpay.core.business.Rendicontazioni rendicontazioneBusiness = new it.govpay.core.business.Rendicontazioni(bd);
			
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
			List<Fr> rendicontazioni = rendicontazioneBusiness.chiediListaRendicontazioni(applicazione, bodyrichiesta.getCodDominio(), bodyrichiesta.getCodApplicazione(), da, a);
			for(Fr frModel : rendicontazioni) {
				response.getFlussoRendicontazione().add(Gp21Utils.toFr(frModel, bd));
			}
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.log("gprnd.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			response.getFlussoRendicontazione().clear();
			e.log(log);
			ctx.log("gprnd.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			response.getFlussoRendicontazione().clear();
			new GovPayException(e).log(log);
			ctx.log("gprnd.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
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
			
			if(!AclEngine.isAuthorized(applicazione, Servizio.RENDICONTAZIONE, frModel.getDominio(bd).getCodDominio(), null)) {
				throw new GovPayException(EsitoOperazione.RND_001);
			}
			
			RendicontazionePagamentoBD rendicontazionePagamentoBD = new RendicontazionePagamentoBD(bd); 
			RendicontazionePagamentoFilter filter = rendicontazionePagamentoBD.newFilter();
			filter.setCodFlusso(bodyrichiesta.getCodFlusso());
			
			if(bodyrichiesta.getCodApplicazione() != null) {
				try {
					Long idApplicazione = AnagraficaManager.getApplicazione(bd, bodyrichiesta.getCodApplicazione()).getId();
					filter.setCodApplicazione(idApplicazione);
				} catch (NotFoundException e) {
					throw new GovPayException(EsitoOperazione.APP_000, bodyrichiesta.getCodApplicazione());
				}
			}
			List<RendicontazionePagamento> rends = rendicontazionePagamentoBD.findAll(filter);
			response.setFlussoRendicontazione(Gp21Utils.toFr(frModel, rends, applicazione.getVersione(), bd));
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.log("gprnd.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			response.setFlussoRendicontazione(null);
			e.log(log);
			ctx.log("gprnd.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			response.setFlussoRendicontazione(null);
			new GovPayException(e).log(log);
			ctx.log("gprnd.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
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
	
}
