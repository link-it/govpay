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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.proxy.Actor;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.core.business.Versamento;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IncassiException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.Gp23Utils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.bd.model.Applicazione;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.v2_5.PagamentiTelematiciGPRnd;
import it.govpay.servizi.v2_3.commons.Incasso;
import it.govpay.servizi.v2_3.commons.Mittente;
import it.govpay.servizi.v2_3.gprnd.GpChiediFlussoRendicontazione;
import it.govpay.servizi.v2_3.gprnd.GpChiediFlussoRendicontazioneResponse;
import it.govpay.servizi.v2_3.gprnd.GpChiediListaFlussiRendicontazione;
import it.govpay.servizi.v2_3.gprnd.GpChiediListaFlussiRendicontazioneResponse;
import it.govpay.servizi.v2_3.gprnd.GpRegistraIncasso;
import it.govpay.servizi.v2_3.gprnd.GpRegistraIncassoResponse;

@WebService(serviceName = "PagamentiTelematiciGPRndService",
endpointInterface = "it.govpay.servizi.v2_5.PagamentiTelematiciGPRnd",
targetNamespace = "http://www.govpay.it/servizi/v2_5",
portName = "GPRndPort",
wsdlLocation="/wsdl/GpRnd_2.5.wsdl",
name="PagamentiTelematiciGPRndService")

@HandlerChain(file="../../../../../handler-chains/handler-chain-gpws.xml")

@org.apache.cxf.annotations.SchemaValidation

public class PagamentiTelematiciGPRndImpl implements PagamentiTelematiciGPRnd {

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LoggerWrapperFactory.getLogger(PagamentiTelematiciGPRndImpl.class);

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
				response.getFlussoRendicontazione().add(Gp23Utils.toFr(frModel, bd));
			}
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			ctx.log("gprnd.ricevutaRichiestaOk");
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
		response.setCodOperazione(MDC.get("op"));
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
			
			List<Diritti> diritti = new ArrayList<Diritti>(); // TODO controllare quale diritto serve in questa fase
			diritti.add(Diritti.LETTURA);
			if(!AclEngine.isAuthorized(applicazione.getUtenza(), Servizio.RENDICONTAZIONI_E_INCASSI, frModel.getDominio(bd).getCodDominio(), null,diritti)) {
				throw new GovPayException(EsitoOperazione.RND_001);
			}

			List<Rendicontazione> rends = frModel.getRendicontazioni(bd);
			for(Rendicontazione rend : rends) {
				if(rend.getPagamento(bd) == null) {
					try {
						it.govpay.bd.model.Versamento versamento = new Versamento(bd).chiediVersamento(null, null, null, null,	frModel.getDominio(bd).getCodDominio(), rend.getIuv());
						rend.setVersamento(versamento);
					}catch (Exception e) {
						continue;
					}
				}
			}
			
			if(bodyrichiesta.getCodApplicazione() != null) {
				Long idApplicazione = AnagraficaManager.getApplicazione(bd, bodyrichiesta.getCodApplicazione()).getId();
				List<Rendicontazione> rendsFiltrato = new ArrayList<Rendicontazione>();
				
				for(Rendicontazione rend : rends) {
					if(rend.getVersamento(bd) ==  null || rend.getVersamento(bd).getIdApplicazione() != idApplicazione.longValue()) {
						continue;
					}
					rendsFiltrato.add(rend);
				}
				
				rends = rendsFiltrato;
			}


			response.setFlussoRendicontazione(Gp23Utils.toFr(frModel, rends, bd));
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
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
		response.setCodOperazione(MDC.get("op"));
		return response;
	}
	
	
	@Override
	public GpRegistraIncassoResponse gpRegistraIncasso(GpRegistraIncasso bodyrichiesta) {
		log.info("Richiesta operazione gpRegistraIncasso");
		GpRegistraIncassoResponse response = new GpRegistraIncassoResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			Applicazione applicazione = getApplicazioneAutenticata(bd);
			ctx.log("gprnd.ricevutaRichiesta");

			RichiestaIncassoDTO richiestaIncassoDTO = new RichiestaIncassoDTO(null);
			richiestaIncassoDTO.setApplicazione(applicazione);
			richiestaIncassoDTO.setCausale(bodyrichiesta.getCausale());
			richiestaIncassoDTO.setCodDominio(bodyrichiesta.getCodDominio());
			richiestaIncassoDTO.setDataContabile(bodyrichiesta.getDataContabile());
			richiestaIncassoDTO.setDataValuta(bodyrichiesta.getDataValuta());
			richiestaIncassoDTO.setImporto(bodyrichiesta.getImporto());
			richiestaIncassoDTO.setTrn(bodyrichiesta.getTrn());
			it.govpay.core.business.Incassi incassi = new it.govpay.core.business.Incassi(bd);
			RichiestaIncassoDTOResponse richiestaIncassoDTOResponse = incassi.richiestaIncasso(richiestaIncassoDTO);
			
			it.govpay.bd.model.Incasso i = richiestaIncassoDTOResponse.getIncasso();
			Incasso incasso = new Incasso();
			incasso.setCausale(i.getCausale());
			incasso.setCodDominio(i.getCodDominio());
			incasso.setDataContabile(i.getDataContabile());
			incasso.setDataValuta(i.getDataValuta());
			incasso.setDispositivo(i.getDispositivo());
			incasso.setImporto(i.getImporto());
			incasso.setTrn(i.getTrn());
			for(Pagamento p : i.getPagamenti(bd)) {
				Incasso.Pagamento pagamento = new Incasso.Pagamento();
				pagamento.setCodApplicazione(p.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd).getCodApplicazione());
				pagamento.setCodSingoloVersamentoEnte(p.getSingoloVersamento(bd).getCodSingoloVersamentoEnte());
				pagamento.setCodVersamentoEnte(p.getSingoloVersamento(bd).getVersamento(bd).getCodVersamentoEnte());
				pagamento.setDataPagamento(p.getDataPagamento());
				pagamento.setImportoPagato(p.getImportoPagato());
				pagamento.setIur(p.getIur());
				pagamento.setIuv(p.getIuv());
				incasso.getPagamento().add(pagamento);
			}
			response.setIncasso(incasso);
			response.setCodEsito(EsitoOperazione.OK.toString());
			response.setDescrizioneEsito("Operazione completata con successo");
			response.setMittente(Mittente.GOV_PAY);
			ctx.log("gprnd.ricevutaRichiestaOk");
		} catch (GovPayException gpe) {
			response = (GpRegistraIncassoResponse) gpe.getWsResponse(response, "gprnd.ricevutaRichiestaKo", log);
		} catch (IncassiException ie) {
			response.setMittente(Mittente.GOV_PAY);
			response.setCodEsito(ie.getCode());
			response.setDescrizioneEsito(ie.getMessage());
			response.setDettaglioEsito(ie.getDetails());
			GpThreadLocal.get().log("gprnd.ricevutaRichiestaKo", response.getCodEsito().toString(), response.getDescrizioneEsito(), response.getDettaglioEsito());
		} catch (Exception e) {
			response = (GpRegistraIncassoResponse) new GovPayException(e).getWsResponse(response, "gprnd.ricevutaRichiestaKo", log);
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
