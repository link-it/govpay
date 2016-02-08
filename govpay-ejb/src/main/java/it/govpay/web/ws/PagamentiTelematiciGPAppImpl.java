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
package it.govpay.web.ws;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Iuv;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RtBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.VersamentiBD.TipoIUV;
import it.govpay.business.Autorizzazione;
import it.govpay.business.Pagamenti;
import it.govpay.business.Rendicontazioni;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.servizi.pa.CodErrore;
import it.govpay.servizi.pa.CodEsito;
import it.govpay.servizi.pa.EsitoOperazione;
import it.govpay.servizi.pa.GpCancellaPagamentoInAttesa;
import it.govpay.servizi.pa.GpCercaVersamentiRequest;
import it.govpay.servizi.pa.GpCercaVersamentiResponse;
import it.govpay.servizi.pa.GpChiediFlussoRendicontazione;
import it.govpay.servizi.pa.GpChiediFlussoRendicontazioneResponse;
import it.govpay.servizi.pa.GpChiediListaFlussiRendicontazione;
import it.govpay.servizi.pa.GpChiediListaFlussiRendicontazioneResponse;
import it.govpay.servizi.pa.GpChiediStatoPagamento;
import it.govpay.servizi.pa.GpChiediStatoPagamentoResponse;
import it.govpay.servizi.pa.GpGeneraCodiciAvviso;
import it.govpay.servizi.pa.GpGeneraCodiciAvvisoResponse;
import it.govpay.servizi.pa.GpGeneraIUV;
import it.govpay.servizi.pa.IdPagamento;
import it.govpay.servizi.pa.PagamentiTelematiciGPApp;
import it.govpay.servizi.pa.Pagamento;
import it.govpay.web.adapter.PagamentiTelematiciGPUtil;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

@WebService(serviceName = "PagamentiTelematiciGPAppservice",
endpointInterface = "it.govpay.servizi.pa.PagamentiTelematiciGPApp",
targetNamespace = "http://www.govpay.it/servizi/PagamentiTelematiciGP",
portName = "GPAppPort")

@HandlerChain(file="../../../../handler-chains/handler-chain.xml")
public class PagamentiTelematiciGPAppImpl implements PagamentiTelematiciGPApp {
	
	@Resource
	WebServiceContext wsCtxt;
	
	private static Logger log = LogManager.getLogger();
	
	@Override
	public EsitoOperazione gpGeneraIUV(GpGeneraIUV bodyrichiesta) {
		log.error("Ricevuta richiesta di generazione iuv");
		
		EsitoOperazione esitoOperazione = new EsitoOperazione();
		esitoOperazione.setCodApplicazione(bodyrichiesta.getCodApplicazione());
		esitoOperazione.setCodOperazione(ThreadContext.get("op"));
		
		BasicBD bd = null;
		try {
			
			try {
				bd = BasicBD.newInstance();
			} catch (ServiceException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			Applicazione applicazione = new Autorizzazione(bd).authApplicazione(wsCtxt.getUserPrincipal(), bodyrichiesta.getCodApplicazione());
			
			Ente ente = null;
			try {
				ente = AnagraficaManager.getEnte(bd, bodyrichiesta.getCodEnte());
			} catch (NotFoundException e){
				log.error("Ente [codEnte: " + bodyrichiesta.getCodEnte() + "] non censito in Anagrafica Enti.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 
			
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, ente.getIdDominio());
			} catch (NotFoundException e){
				log.error("Dominio [idDominio: " + ente.getIdDominio() + "] associato all'Ente [codEnte: " + ente.getCodEnte() + " non censito in Anagrafica Domini.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 
			
			Stazione stazione = null;
			try {
				stazione = AnagraficaManager.getStazione(bd, dominio.getIdStazione());
			} catch (NotFoundException e){
				log.error("Stazione [idStazione: " + dominio.getIdStazione() + "] associato al dominio [codDominio: " + dominio.getCodDominio() + " non censito in Anagrafica Stazioni.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 
			
			String iuv = null;
			Pagamenti pagamenti = new Pagamenti(bd);
			switch (bodyrichiesta.getTipoIuv()) {
			case IUV_INIZIATIVA_ENTE:
				iuv = pagamenti.generaIuv(applicazione.getId(), stazione.getApplicationCode(), dominio.getCodDominio(), TipoIUV.ISO11694, Iuv.AUX_DIGIT);
				break;

			case IUV_INIZIATIVA_PSP:
				iuv = pagamenti.generaIuv(applicazione.getId(), stazione.getApplicationCode(), dominio.getCodDominio(), TipoIUV.NUMERICO, Iuv.AUX_DIGIT);
				break;
			}
			
			esitoOperazione.setCodEsito(CodEsito.OK);
			IdPagamento idPagamento = new IdPagamento();
			idPagamento.setCodEnte(bodyrichiesta.getCodEnte());
			idPagamento.setIuv(iuv);
			esitoOperazione.getIdPagamento().add(idPagamento);
			log.error("Generato iuv [" + iuv + "]");
			return esitoOperazione;
		} catch (GovPayException e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodEnte(bodyrichiesta.getCodEnte());
			esitoOperazione.setCodErrore(PagamentiTelematiciGPUtil.toDescrizioneEsito(e.getTipoException()));
			if(e.getTipoException().equals(GovPayExceptionEnum.ERRORE_INTERNO))
				log.error("Errore durante la generazione dello iuv. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			else
				log.error("Errore durante la generazione dello iuv. Ritorno esito [" + esitoOperazione.getCodErrore() + "]");
			return esitoOperazione;
		} catch (Exception e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodEnte(bodyrichiesta.getCodEnte());
			esitoOperazione.setCodErrore(CodErrore.ERRORE_INTERNO);
			log.error("Errore durante il caricamento del versamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			return esitoOperazione;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}

	@Override
	public EsitoOperazione gpCaricaPagamentoInAttesa(Pagamento bodyrichiesta) {
		log.error("Ricevuta richiesta di CaricaPagamentoInAttesa");
		
		EsitoOperazione esitoOperazione = new EsitoOperazione();
		esitoOperazione.setCodApplicazione(bodyrichiesta.getCodApplicazione());
		esitoOperazione.setCodOperazione(ThreadContext.get("op"));
		
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (ServiceException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			Applicazione applicazione = new Autorizzazione(bd).authApplicazione(wsCtxt.getUserPrincipal(), bodyrichiesta.getCodApplicazione());
			
			Ente ente = null;
			try {
				ente = AnagraficaManager.getEnte(bd, bodyrichiesta.getCodEnte());
			} catch (NotFoundException e){
				log.error("Ente [codEnte: " + bodyrichiesta.getCodEnte() + "] non censito in Anagrafica Enti.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 
			
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, ente.getIdDominio());
			} catch (NotFoundException e){
				log.error("Dominio [idDominio: " + ente.getIdDominio() + "] associato all'Ente [codEnte: " + ente.getCodEnte() + " non censito in Anagrafica Domini.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 
			
			Stazione stazione = null;
			try {
				stazione = AnagraficaManager.getStazione(bd, dominio.getIdStazione());
			} catch (NotFoundException e){
				log.error("Stazione [idStazione: " + dominio.getIdStazione() + "] associato al dominio [codDominio: " + dominio.getCodDominio() + " non censito in Anagrafica Stazioni.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 
			
			Pagamenti pagamenti = new Pagamenti(bd);
		
			String iuv = bodyrichiesta.getIuv();
			if(iuv == null) {
				switch (bodyrichiesta.getTipoIuv()) {
				case IUV_INIZIATIVA_ENTE:
					iuv = pagamenti.generaIuv(applicazione.getId(), stazione.getApplicationCode(), dominio.getCodDominio(), TipoIUV.ISO11694, Iuv.AUX_DIGIT);
					break;

				case IUV_INIZIATIVA_PSP:
					iuv = pagamenti.generaIuv(applicazione.getId(), stazione.getApplicationCode(), dominio.getCodDominio(), TipoIUV.NUMERICO, Iuv.AUX_DIGIT);
					break;
				}
				log.info("Assegnato al Pagamento in Attesa lo IUV [" + iuv + "]");
			} 
			
			Versamento versamento = PagamentiTelematiciGPUtil.toVersamento(bodyrichiesta, ente, dominio, applicazione, iuv, bd);
			
			pagamenti.caricaPagamento(versamento);
			
			esitoOperazione.setCodEsito(CodEsito.OK);
			IdPagamento idPagamento = new IdPagamento();
			idPagamento.setCodEnte(bodyrichiesta.getCodEnte());
			idPagamento.setIuv(iuv);
			esitoOperazione.getIdPagamento().add(idPagamento);
			log.error("Generato iuv [" + iuv + "]");
			return esitoOperazione;
		} catch (GovPayException e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodEnte(bodyrichiesta.getCodEnte());
			esitoOperazione.setCodErrore(PagamentiTelematiciGPUtil.toDescrizioneEsito(e.getTipoException()));
			if(e.getTipoException().equals(GovPayExceptionEnum.ERRORE_INTERNO))
				log.error("Errore durante il caricamento del versamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			else
				log.error("Errore durante il caricamento del versamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]");
			return esitoOperazione;
		} catch (Exception e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodEnte(bodyrichiesta.getCodEnte());
			esitoOperazione.setCodErrore(CodErrore.ERRORE_INTERNO);
			log.error("Errore durante il caricamento del versamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			return esitoOperazione;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
	
	@Override
	public EsitoOperazione gpCancellaPagamentoInAttesa(GpCancellaPagamentoInAttesa bodyrichiesta) {
		log.error("Ricevuta richiesta di CancellaPagamentoInAttesa");
		
		EsitoOperazione esitoOperazione = new EsitoOperazione();
		esitoOperazione.setCodApplicazione(bodyrichiesta.getCodApplicazione());
		esitoOperazione.setCodOperazione(ThreadContext.get("op"));
		esitoOperazione.getIdPagamento().add(bodyrichiesta.getIdPagamento());
		
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (ServiceException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			Applicazione applicazione = new Autorizzazione(bd).authApplicazione(wsCtxt.getUserPrincipal(), bodyrichiesta.getCodApplicazione());
			
			Ente ente = null;
			try {
				ente = AnagraficaManager.getEnte(bd, bodyrichiesta.getIdPagamento().getCodEnte());
			} catch (NotFoundException e){
				log.error("Ente [codEnte: " + bodyrichiesta.getIdPagamento().getCodEnte() + "] non censito in Anagrafica Enti.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 
			
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, ente.getIdDominio());
			} catch (NotFoundException e){
				log.error("Dominio [idDominio: " + ente.getIdDominio() + "] associato all'Ente [codEnte: " + ente.getCodEnte() + " non censito in Anagrafica Domini.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 
			
			Pagamenti pagamentiInAttesa = new Pagamenti(bd);
			pagamentiInAttesa.annullaPagamento(applicazione.getId(), dominio.getCodDominio(), bodyrichiesta.getIdPagamento().getIuv());
			esitoOperazione.setCodEsito(CodEsito.OK);
			return esitoOperazione;
		} catch (GovPayException e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(PagamentiTelematiciGPUtil.toDescrizioneEsito(e.getTipoException()));
			if(e.getTipoException().equals(GovPayExceptionEnum.ERRORE_INTERNO))
				log.error("Errore durante la cancellazione del versamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			else
				log.error("Errore durante la cancellazione del versamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]");
			return esitoOperazione;
		} catch (Exception e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(CodErrore.ERRORE_INTERNO);
			log.error("Errore durante la cancellazione del versamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			return esitoOperazione;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}

	@Override
	public GpChiediStatoPagamentoResponse gpChiediStatoPagamento(GpChiediStatoPagamento bodyrichiesta) {
		log.info("Ricevuta richiesta di gpChiediStatoPagamento.");
		
		GpChiediStatoPagamentoResponse esitoOperazione = new GpChiediStatoPagamentoResponse();
		esitoOperazione.setCodApplicazione(bodyrichiesta.getCodApplicazione());
		esitoOperazione.setCodOperazione(ThreadContext.get("op"));
		
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			Applicazione applicazione = new Autorizzazione(bd).authApplicazione(wsCtxt.getUserPrincipal(), bodyrichiesta.getCodApplicazione());
			log.info("Identificazione Applicazione avvenuta con successo [CodApplicazione: " + applicazione.getCodApplicazione() + "]");
			
			Ente ente = null;
			try {
				ente = AnagraficaManager.getEnte(bd, bodyrichiesta.getIdPagamento().getCodEnte());
			} catch (NotFoundException e){
				log.error("Ente [codEnte: " + bodyrichiesta.getIdPagamento().getCodEnte() + "] non censito in Anagrafica Enti.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 
			
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, ente.getIdDominio());
			} catch (NotFoundException e){
				log.error("Dominio [idDominio: " + ente.getIdDominio() + "] associato all'Ente [codEnte: " + ente.getCodEnte() + " non censito in Anagrafica Domini.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			Versamento versamento = null;
			try {
				versamento = versamentiBD.getVersamento(dominio.getCodDominio(), bodyrichiesta.getIdPagamento().getIuv());
			} catch (Exception e){
				throw new GovPayException(GovPayExceptionEnum.IUV_NON_TROVATO);
			} 
			
			RptBD rptBD = new RptBD(bd);
			Rpt rpt = null;
			try {
				rpt = rptBD.getLastRpt(versamento.getId());
				//Verifico lo stato RPT
				if(new Pagamenti(bd).aggiornaRptDaNpD(rpt)) {
					versamento = versamentiBD.getVersamento(versamento.getId());
				}
			} catch (Exception e){
				return PagamentiTelematiciGPUtil.toGpChiediStatoPagamentoResponse(bd, esitoOperazione, applicazione, ente, dominio, versamento, null, null);
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
			return PagamentiTelematiciGPUtil.toGpChiediStatoPagamentoResponse(bd, esitoOperazione, applicazione, ente, dominio,versamento, rpt, rtByte);
		} catch (GovPayException e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(PagamentiTelematiciGPUtil.toDescrizioneEsito(e.getTipoException()));
			if(e.getTipoException().equals(GovPayExceptionEnum.ERRORE_INTERNO))
				log.error("Errore durante il pagamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			else
				log.error("Errore durante il pagamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]");
			return esitoOperazione;
		} catch (Exception e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(CodErrore.ERRORE_INTERNO);
			log.error("Errore durante il pagamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			return esitoOperazione;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}

	@Override
	public GpCercaVersamentiResponse gpCercaVersamenti(
			GpCercaVersamentiRequest bodyrichiesta) {
		log.info("Ricevuta richiesta di CercaVersamenti");
		
		GpCercaVersamentiResponse esitoOperazione = new GpCercaVersamentiResponse();
		esitoOperazione.setCodApplicazione(bodyrichiesta.getCodApplicazione());
		esitoOperazione.setCodOperazione(ThreadContext.get("op"));
		
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (ServiceException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			if(bodyrichiesta.getCodApplicazione() == null) {
				throw new GovPayException(GovPayExceptionEnum.APPLICAZIONE_NON_TROVATA);
			}
			

			Applicazione applicazione = new Autorizzazione(bd).authApplicazione(wsCtxt.getUserPrincipal(), bodyrichiesta.getCodApplicazione());

			log.info("Autorizzata applicazione ["+applicazione.getCodApplicazione()+"]");
			
			esitoOperazione.getVersamento().addAll(PagamentiTelematiciGPUtil.findVersamentiByFilter(bodyrichiesta, bd));
			esitoOperazione.setCodEsito(CodEsito.OK);
			return esitoOperazione;
		} catch (GovPayException e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(PagamentiTelematiciGPUtil.toDescrizioneEsito(e.getTipoException()));
			if(e.getTipoException().equals(GovPayExceptionEnum.ERRORE_INTERNO))
				log.error("Errore durante la ricerca dei versamenti. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			else
				log.error("Errore durante la ricerca dei versamenti. Ritorno esito [" + esitoOperazione.getCodErrore() + "]");
			return esitoOperazione;
		} catch (Exception e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(CodErrore.ERRORE_INTERNO);
			log.error("Errore durante la ricerca dei versamenti. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			return esitoOperazione;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}

	@Override
	public GpChiediListaFlussiRendicontazioneResponse gpChiediListaFlussiRendicontazione(GpChiediListaFlussiRendicontazione bodyrichiesta) {
		log.info("Ricevuta richiesta di gpChiediFlussiRendicontazione");
		
		GpChiediListaFlussiRendicontazioneResponse esitoOperazione = new GpChiediListaFlussiRendicontazioneResponse();
		esitoOperazione.setCodApplicazione(bodyrichiesta.getCodApplicazione());
		esitoOperazione.setCodOperazione(ThreadContext.get("op"));
		
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (ServiceException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			if(bodyrichiesta.getCodApplicazione() == null) {
				throw new GovPayException(GovPayExceptionEnum.APPLICAZIONE_NON_TROVATA);
			}
			
			Applicazione applicazione = new Autorizzazione(bd).authApplicazione(wsCtxt.getUserPrincipal(), bodyrichiesta.getCodApplicazione());
			List<Long> idDomini = new ArrayList<Long>();
			List<Long> tributi = applicazione.getIdTributi();
			
			for(Long idTributo : tributi) {
				Tributo tributo = AnagraficaManager.getTributo(bd, idTributo);
				Ente ente = AnagraficaManager.getEnte(bd, tributo.getIdEnte());
				if(!idDomini.contains(ente.getIdDominio()))
				       idDomini.add(ente.getIdDominio());
			}
			
			Rendicontazioni rendicontazioniBD = new Rendicontazioni(bd);
			List<Fr> frs= rendicontazioniBD.getFlussi(idDomini);
			
			esitoOperazione.setCodEsito(CodEsito.OK);
			esitoOperazione.setCodApplicazione(bodyrichiesta.getCodApplicazione());
			for(Fr fr : frs) {
				GpChiediListaFlussiRendicontazioneResponse.Fr f = new GpChiediListaFlussiRendicontazioneResponse.Fr();
				f.setIdFr(fr.getId());
				f.setDataOraFlusso(fr.getDataOraFlusso());
				f.setDataRegolamento(fr.getDataRegolamento());
				f.setImportoTotale(new BigDecimal(fr.getImportoTotalePagamenti()));
				f.setIur(fr.getIur());
				f.setNumeroPagamenti(fr.getNumeroPagamenti());
				f.setStato(fr.getStato().toString());
				f.setDescrizioneStato(fr.getDescrizioneStato());
				esitoOperazione.getFr().add(f);
			}
			return esitoOperazione;
		} catch (Exception e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(CodErrore.ERRORE_INTERNO);
			log.error("Errore durante la ricerca dei versamenti. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			return esitoOperazione;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
	
	@Override
	public GpChiediFlussoRendicontazioneResponse gpChiediFlussoRendicontazione(GpChiediFlussoRendicontazione bodyrichiesta) {
		log.info("Ricevuta richiesta di gpChiediFlussiRendicontazione");
		
		GpChiediFlussoRendicontazioneResponse esitoOperazione = new GpChiediFlussoRendicontazioneResponse();
		esitoOperazione.setCodApplicazione(bodyrichiesta.getCodApplicazione());
		esitoOperazione.setCodOperazione(ThreadContext.get("op"));
		
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (ServiceException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			if(bodyrichiesta.getCodApplicazione() == null) {
				throw new GovPayException(GovPayExceptionEnum.APPLICAZIONE_NON_TROVATA);
			}
			
			Rendicontazioni rendicontazioniBD = new Rendicontazioni(bd);
			Fr fr= rendicontazioniBD.getFlusso(bodyrichiesta.getIdFr());
			
			esitoOperazione.setCodEsito(CodEsito.OK);
			esitoOperazione.setCodApplicazione(bodyrichiesta.getCodApplicazione());
			esitoOperazione.setFr(PagamentiTelematiciGPUtil.toFr(fr, bd));
			return esitoOperazione;
		} catch (Exception e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(CodErrore.ERRORE_INTERNO);
			log.error("Errore durante la ricerca dei versamenti. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			return esitoOperazione;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
	
	@Override
	public GpGeneraCodiciAvvisoResponse gpGeneraCodiciAvviso(GpGeneraCodiciAvviso bodyrichiesta) {
		log.info("Ricevuta richiesta di gpGeneraCodiciAvvisoResponse.");
		
		GpGeneraCodiciAvvisoResponse esitoOperazione = new GpGeneraCodiciAvvisoResponse();
		esitoOperazione.setCodApplicazione(bodyrichiesta.getCodApplicazione());
		esitoOperazione.setCodOperazione(ThreadContext.get("op"));
		
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			Applicazione applicazione = new Autorizzazione(bd).authApplicazione(wsCtxt.getUserPrincipal(), bodyrichiesta.getCodApplicazione());
			log.info("Identificazione Applicazione avvenuta con successo [CodApplicazione: " + applicazione.getCodApplicazione() + "]");
			
			Ente ente = null;
			try {
				ente = AnagraficaManager.getEnte(bd, bodyrichiesta.getIdPagamento().getCodEnte());
			} catch (NotFoundException e){
				log.error("Ente [codEnte: " + bodyrichiesta.getIdPagamento().getCodEnte() + "] non censito in Anagrafica Enti.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 
			
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, ente.getIdDominio());
			} catch (NotFoundException e){
				log.error("Dominio [idDominio: " + ente.getIdDominio() + "] associato all'Ente [codEnte: " + ente.getCodEnte() + " non censito in Anagrafica Domini.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 
			
			Stazione stazione = null;
			try {
				stazione = AnagraficaManager.getStazione(bd, dominio.getIdStazione());
			} catch (NotFoundException e){
				log.error("Stazione [idStazione: " + dominio.getIdStazione() + "] associato al Dominio [codDominio: " + dominio.getCodDominio() + " non censito in Anagrafica Domini.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			Versamento versamento = null;
			try {
				versamento = versamentiBD.getVersamento(dominio.getCodDominio(), bodyrichiesta.getIdPagamento().getIuv());
			} catch (Exception e){
				throw new GovPayException(GovPayExceptionEnum.IUV_NON_TROVATO);
			} 
			
			
			ByteArrayOutputStream barcodeos = new ByteArrayOutputStream();
			String barcodetxt = PagamentiTelematiciGPUtil.buildBarCodeTxt(dominio.getGln(), stazione.getApplicationCode(), versamento.getIuv(), versamento.getImportoTotale());
			log.debug("Produzione barcode: " + barcodetxt);
			BufferedImage barcode = com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage(new com.google.zxing.oned.Code128Writer().encode(barcodetxt, com.google.zxing.BarcodeFormat.CODE_128, 700, 200));
			ImageIO.write(barcode, "GIF", barcodeos);
			esitoOperazione.setBarcode(barcodeos.toByteArray());
			
			ByteArrayOutputStream qrcodeos = new ByteArrayOutputStream();
			String qrcodetxt = PagamentiTelematiciGPUtil.buildQrCodeTxt(dominio.getCodDominio(), stazione.getApplicationCode(), versamento.getIuv(), versamento.getImportoTotale()); 
			log.debug("Produzione qrcode: " + qrcodetxt);
			BufferedImage qrcode = com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage(new com.google.zxing.qrcode.QRCodeWriter().encode(qrcodetxt, com.google.zxing.BarcodeFormat.QR_CODE, 500, 500));
			ImageIO.write(qrcode, "GIF", qrcodeos);
			esitoOperazione.setQrcode(qrcodeos.toByteArray());
			
			esitoOperazione.setCodEsito(CodEsito.OK);
			log.info("Generazione eseguita.");
			return esitoOperazione;
		} catch (GovPayException e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(PagamentiTelematiciGPUtil.toDescrizioneEsito(e.getTipoException()));
			if(e.getTipoException().equals(GovPayExceptionEnum.ERRORE_INTERNO))
				log.error("Errore durante la generazione dei codici grafici. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			else
				log.error("Errore durante la generazione dei codici grafici. Ritorno esito [" + esitoOperazione.getCodErrore() + "]");
			return esitoOperazione;
		} catch (Exception e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(CodErrore.ERRORE_INTERNO);
			log.error("Errore durante la generazione dei codici grafici.. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			return esitoOperazione;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
}
