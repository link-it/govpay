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
package it.govpay.core.business;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.rpt.FaultBean;
import gov.telematici.pagamenti.ws.rpt.NodoChiediListaPendentiRPT;
import gov.telematici.pagamenti.ws.rpt.NodoChiediListaPendentiRPTRisposta;
import gov.telematici.pagamenti.ws.rpt.TipoRPTPendente;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.business.model.AvviaRichiestaStornoDTO;
import it.govpay.core.business.model.AvviaRichiestaStornoDTOResponse;
import it.govpay.core.business.model.Risposta;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.RrUtils;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Intermediario;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Rr.StatoRr;

public class Pagamento   {

	private static Logger log = LoggerWrapperFactory.getLogger(Pagamento.class);

	public Pagamento() {
	}

	public String verificaTransazioniPendenti() throws GovPayException {

		IContext ctx = ContextThreadLocal.get();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		List<String> response = new ArrayList<>();
		try {
			ctx.getApplicationLogger().log("pendenti.avvio");
			StazioniBD stazioniBD = new StazioniBD(configWrapper);
			List<Stazione> lstStazioni = stazioniBD.getStazioni();
			DominiBD dominiBD = new DominiBD(ctx.getTransactionId());
			Giornale giornale = new it.govpay.core.business.Configurazione().getConfigurazione().getGiornale();

			for(Stazione stazione : lstStazioni) {

				DominioFilter filter = dominiBD.newFilter();
				filter.setCodStazione(stazione.getCodStazione());
				List<Dominio> lstDomini = dominiBD.findAll(filter);

				Intermediario intermediario = stazione.getIntermediario(configWrapper);

				//this.closeConnection();
				ctx.getApplicationLogger().log("pendenti.acquisizionelistaPendenti", stazione.getCodStazione());
				log.debug("Recupero i pendenti [CodStazione: " + stazione.getCodStazione() + "]");

				if(lstDomini.isEmpty()) {
					log.debug("Recupero i pendenti per la stazione [CodStazione: " + stazione.getCodStazione() + "], non eseguita: la stazione non e' associata ad alcun dominio.");
					continue;
				}

				// Costruisco una mappa di tutti i pagamenti pendenti sul nodo
				// La chiave di lettura e' iuv@ccp
				// Le pendenze per specifica durano 60 giorni.
				int finestra = 60;
				Calendar fineFinestra = Calendar.getInstance();
				Calendar inizioFinestra = (Calendar) fineFinestra.clone();
				inizioFinestra.add(Calendar.DATE, -finestra);

				Map<String, String> statiRptPendenti = this.acquisisciPendenti(giornale,intermediario, stazione, lstDomini, false, inizioFinestra, fineFinestra, 500);

				log.info("Identificate sul NodoSPC " + statiRptPendenti.size() + " RPT pendenti");
				ctx.getApplicationLogger().log("pendenti.listaPendentiOk", stazione.getCodStazione(), statiRptPendenti.size() + "");

				// Ho acquisito tutti gli stati pendenti. 
				// Tutte quelle in stato terminale, 
				//	this.setupConnection(ContextThreadLocal.get().getTransactionId());

				RptBD rptBD = new RptBD(configWrapper);

				List<String> codDomini = new ArrayList<>();
				for(Dominio d : lstDomini) {
					codDomini.add(d.getCodDominio());
				}
				List<Rpt> rpts = rptBD.getRptPendenti(codDomini);

				log.info("Identificate su GovPay " + rpts.size() + " transazioni pendenti");
				ctx.getApplicationLogger().log("pendenti.listaPendentiGovPayOk", rpts.size() + "");

				// Scorro le transazioni. Se non risulta pendente sul nodo (quindi non e' pendente) la mando in aggiornamento.

				Integer minutiDallaCreazioneRPT = GovpayConfig.getInstance().getIntervalloControlloRptPendenti();
				Date dataCreazioneRPT = new Date(new Date().getTime() - (minutiDallaCreazioneRPT * 60 * 1000));

				for(Rpt rpt : rpts) {

					// WORKAROUND CONCORRENZA CON INVIO RT DAL NODO SKIPPO LE RPT PENDENTI CREATE DA MENO DI X MINUTI (INDICATI NELLE PROPERTIES)

					if(rpt.getDataMsgRichiesta().after(dataCreazioneRPT)) {
						log.info("Rpt recente [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] aggiornamento non necessario");
						continue;
					} else {
						log.info("Rpt pendente su GovPay [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "]: stato " + rpt.getStato().name());
					}

					// Aggiorno il batch
					BatchManager.aggiornaEsecuzione(configWrapper, Operazioni.PND);

					String stato = statiRptPendenti.get(rpt.getCodDominio() + "@" + rpt.getIuv() + "@" + rpt.getCcp());
					if(stato != null && !stato.equals(StatoRpt.RPT_ANNULLATA.name())) {
						log.info("Rpt confermata pendente dal nodo [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "]: stato " + stato);
						ctx.getApplicationLogger().log("pendenti.confermaPendente", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp(), stato);
						StatoRpt statoRpt = StatoRpt.toEnum(stato);
						if(!rpt.getStato().equals(statoRpt)) {
							response.add("[" + rpt.getCodDominio() + " " + rpt.getIuv() + " " + rpt.getCcp() + "]# Aggiornamento in stato " + stato.toString());
							rptBD.updateRpt(rpt.getId(), statoRpt, null, null, null,null);
						}
					} else {
						log.info("Rpt non pendente o sconosciuta sul nodo [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "]");
						ctx.getApplicationLogger().log("pendenti.confermaNonPendente", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp());
						// Accedo alle entita che serviranno in seguito prima di chiudere la connessione;
						rpt.getStazione(configWrapper).getIntermediario(configWrapper);
						try {
							RptUtils.aggiornaRptDaNpD(intermediario, rpt);
						} catch (NdpException e) {
							ctx.getApplicationLogger().log("pendenti.rptAggiornataKo", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp(), e.getFaultString());
							log.warn("Errore durante l'aggiornamento della RPT: " + e.getFaultString());
							continue;
						} catch (Exception e) {
							ctx.getApplicationLogger().log("pendenti.rptAggiornataFail", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp(), e.getMessage());
							log.warn("Errore durante l'aggiornamento della RPT", e);
							continue;
						}

						if(rpt.getModelloPagamento().equals(ModelloPagamento.ATTIVATO_PRESSO_PSP) && (rpt.getStato().equals(StatoRpt.RPT_ATTIVATA) || rpt.getStato().equals(StatoRpt.RPT_ERRORE_INVIO_A_NODO))) {
							ctx.getApplicationLogger().log("pendenti.rptAttivata", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp());
							log.info("Rpt attivata ma non consegnata [" + rpt.getCodDominio() + "][" + rpt.getIuv() + "][" + rpt.getCcp() + "]: avviata rispedizione al Nodo.");
						} else {
							ctx.getApplicationLogger().log("pendenti.rptAggiornata", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp(), rpt.getStato().toString());
							log.info("Processo di aggiornamento completato [" + rpt.getCodDominio() + "][" + rpt.getIuv() + "][" + rpt.getCcp() + "]: nuovo stato " + rpt.getStato().toString());
							response.add("[" + rpt.getCodDominio() + " " + rpt.getIuv() + " " + rpt.getCcp() + "]# Aggiornamento in stato " + rpt.getStato().toString());
						}
					}
				}
			}
		} catch (Exception e) {
			log.warn("Fallito aggiornamento pendenti", e);
			throw new GovPayException(EsitoOperazione.INTERNAL, e);
		}

		if(response.isEmpty()) {
			return "Acquisizione completata#Nessun pagamento pendente.";
		} else {
			return StringUtils.join(response,"|");
		}

	}

	/**
	 * La logica prevede di cercare i pendenti per stazione nell'intervallo da >> a.
	 * Se nella risposta ci sono 500+ pendenti si dimezza l'intervallo.
	 * Se a forza di dimezzare l'intervallo diventa di 1 giorno ed ancora ci sono 500+ risultati, 
	 * si ripete la ricerca per quel giorno sulla lista di domini. Se anche in questo caso si hanno troppi risultati, 
	 * pace, non e' possibile filtrare ulteriormente. 
	 * 
	 * @param client
	 * @param intermediario
	 * @param stazione
	 * @param lstDomini
	 * @param perDominio
	 * @param da
	 * @param a
	 * @return
	 * @throws UtilsException 
	 * @throws ServiceException 
	 */
	private Map<String, String> acquisisciPendenti(Giornale giornale, Intermediario intermediario, Stazione stazione, List<Dominio> lstDomini, boolean perDominio, Calendar da, Calendar a, long soglia) throws UtilsException, ServiceException {
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		Map<String, String> statiRptPendenti = new HashMap<>();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		// Ciclo sui domini, ma ciclo veramente solo se perDominio == true,
		// Altrimenti ci giro una sola volta 

		for(Dominio dominio : lstDomini) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			NodoChiediListaPendentiRPT richiesta = new NodoChiediListaPendentiRPT();
			richiesta.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
			richiesta.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
			richiesta.setPassword(stazione.getPassword());
			richiesta.setDimensioneLista(BigInteger.valueOf(soglia));
			richiesta.setRangeA(a.getTime());
			richiesta.setRangeDa(da.getTime());

			if(perDominio) {
				richiesta.setIdentificativoDominio(dominio.getCodDominio());
				log.debug("Richiedo la lista delle RPT pendenti (Dominio " + dominio.getCodDominio() + " dal " + dateFormat.format(da.getTime()) + " al " + dateFormat.format(a.getTime()) + ")");
				ctx.getApplicationLogger().log("pendenti.listaPendenti", dominio.getCodDominio(), dateFormat.format(da.getTime()), dateFormat.format(a.getTime()));
			} else {
				log.debug("Richiedo la lista delle RPT pendenti (Stazione " + stazione.getCodStazione() + " dal " + dateFormat.format(da.getTime()) + " al " + dateFormat.format(a.getTime()) + ")");
				ctx.getApplicationLogger().log("pendenti.listaPendenti", stazione.getCodStazione(), dateFormat.format(da.getTime()), dateFormat.format(a.getTime()));
			}

			NodoChiediListaPendentiRPTRisposta risposta = null;
			NodoClient chiediListaPendentiClient = null;
			try {
				try {
					appContext.setupNodoClient(stazione.getCodStazione(), null, Azione.nodoChiediListaPendentiRPT);
					chiediListaPendentiClient = new NodoClient(intermediario, null, giornale);
					risposta = chiediListaPendentiClient.nodoChiediListaPendentiRPT(richiesta, intermediario.getDenominazione());
					chiediListaPendentiClient.getEventoCtx().setEsito(Esito.OK);
				} catch (Exception e) {
					log.warn("Errore durante la richiesta di lista pendenti", e);
					if(chiediListaPendentiClient != null) {
						if(e instanceof GovPayException) {
							chiediListaPendentiClient.getEventoCtx().setSottotipoEsito(((GovPayException)e).getCodEsito().toString());
						} else if(e instanceof ClientException) {
							chiediListaPendentiClient.getEventoCtx().setSottotipoEsito(((ClientException)e).getResponseCode() + "");
						} else {
							chiediListaPendentiClient.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
						}
						chiediListaPendentiClient.getEventoCtx().setEsito(Esito.FAIL);
						chiediListaPendentiClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
						chiediListaPendentiClient.getEventoCtx().setException(e);
					}
					// Esco da ciclo while e procedo con il prossimo dominio.
					if(perDominio) {
						ctx.getApplicationLogger().log("pendenti.listaPendentiDominioFail", dominio.getCodDominio(), e.getMessage());
						continue;
					} else {
						ctx.getApplicationLogger().log("pendenti.listaPendentiFail", stazione.getCodStazione(), e.getMessage());
						break;
					}
				} finally {
				}

				if(risposta != null) {
					if(risposta.getFault() != null) {
						log.warn("Ricevuto errore durante la richiesta di lista pendenti: " + risposta.getFault().getFaultCode() + ": " + risposta.getFault().getFaultString());

						String fc = risposta.getFault().getFaultCode() != null ? risposta.getFault().getFaultCode() : "-";
						String fs = risposta.getFault().getFaultString() != null ? risposta.getFault().getFaultString() : "-";
						String fd = risposta.getFault().getDescription() != null ? risposta.getFault().getDescription() : "-";
						if(chiediListaPendentiClient != null) {
							chiediListaPendentiClient.getEventoCtx().setSottotipoEsito(fc);
							chiediListaPendentiClient.getEventoCtx().setEsito(Esito.KO);
							chiediListaPendentiClient.getEventoCtx().setDescrizioneEsito(fd);
						}
						if(perDominio) {
							ctx.getApplicationLogger().log("pendenti.listaPendentiDominioKo", dominio.getCodDominio(), fc, fs, fd);
							continue;
						} else {
							ctx.getApplicationLogger().log("pendenti.listaPendentiKo", stazione.getCodStazione(), fc, fs, fd);
							break;
						}
					}

					if(risposta.getListaRPTPendenti() == null || risposta.getListaRPTPendenti().getRptPendente().isEmpty()) {
						log.debug("Lista pendenti vuota.");
						if(perDominio) {
							ctx.getApplicationLogger().log("pendenti.listaPendentiDominioVuota", dominio.getCodDominio());
							continue;
						} else {
							ctx.getApplicationLogger().log("pendenti.listaPendentiVuota", stazione.getCodStazione());					
							break;
						}
					}



					if(risposta.getListaRPTPendenti().getTotRestituiti() >= soglia) {

						// Vedo quanto e' ampia la finestra per capire se dimezzarla o ciclare sui domini
						int finestra = (int) TimeUnit.DAYS.convert((a.getTimeInMillis() - da.getTimeInMillis()), TimeUnit.MILLISECONDS);

						if(finestra > 1) {
							ctx.getApplicationLogger().log("pendenti.listaPendentiPiena", stazione.getCodStazione(), dateFormat.format(da.getTime()), dateFormat.format(a.getTime()));	
							finestra = finestra/2;
							Calendar mezzo = (Calendar) a.clone();
							mezzo.add(Calendar.DATE, -finestra);
							log.debug("Lista pendenti con troppi elementi. Ricalcolo la finestra: (dal " + dateFormat.format(da.getTime()) + " a " + dateFormat.format(a.getTime()) + ")");
							statiRptPendenti.putAll(this.acquisisciPendenti(giornale, intermediario, stazione, lstDomini, false, da, mezzo, soglia));
							mezzo.add(Calendar.DATE, 1);
							statiRptPendenti.putAll(this.acquisisciPendenti(giornale, intermediario, stazione, lstDomini, false, mezzo, a, soglia));
							return statiRptPendenti;
						} else {
							if(perDominio) {
								ctx.getApplicationLogger().log("pendenti.listaPendentiDominioDailyPiena", dominio.getCodDominio(), dateFormat.format(a.getTime()));
								log.debug("Lista pendenti con troppi elementi, ma impossibile diminuire ulteriormente la finesta. Elenco accettato.");
							} else {
								ctx.getApplicationLogger().log("pendenti.listaPendentiDailyPiena", stazione.getCodStazione(), dateFormat.format(a.getTime()));
								log.debug("Lista pendenti con troppi elementi, scalo a dominio.");
								return this.acquisisciPendenti(giornale, intermediario, stazione, lstDomini, true, da, a, soglia);
							}
						}
					}

					// Qui ci arrivo o se ho meno di 500 risultati oppure se sono in *giornaliero per dominio*
					for(TipoRPTPendente rptPendente : risposta.getListaRPTPendenti().getRptPendente()) {
						String rptKey = rptPendente.getIdentificativoDominio() + "@" + rptPendente.getIdentificativoUnivocoVersamento() + "@" + rptPendente.getCodiceContestoPagamento();
						statiRptPendenti.put(rptKey, rptPendente.getStato());
					}
				}

				// Se sto ricercando per stazione, esco.
				if(!perDominio) {
					return statiRptPendenti;
				}
			} finally {
				if(chiediListaPendentiClient != null && chiediListaPendentiClient.getEventoCtx().isRegistraEvento()) {
					EventiBD eventiBD = new EventiBD(configWrapper);
					eventiBD.insertEvento(chiediListaPendentiClient.getEventoCtx().toEventoDTO());
				}
			}
		}
		return statiRptPendenti;
	}

	public AvviaRichiestaStornoDTOResponse avviaStorno(AvviaRichiestaStornoDTO dto) throws ServiceException, GovPayException, UtilsException {
		IContext ctx = ContextThreadLocal.get();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		List<it.govpay.bd.model.Pagamento> pagamentiDaStornare = new ArrayList<>(); 
		Rpt rpt = null;
		Giornale giornale = new it.govpay.core.business.Configurazione().getConfigurazione().getGiornale();
		try {
			RptBD rptBD = new RptBD(configWrapper);
			rpt = rptBD.getRpt(dto.getCodDominio(), dto.getIuv(), dto.getCcp(), true);

			if(dto.getPagamento() == null || dto.getPagamento().isEmpty()) {
				for(it.govpay.bd.model.Pagamento pagamento : rpt.getPagamenti()) {
					if(pagamento.getImportoRevocato() != null) continue;
					pagamento.setCausaleRevoca(dto.getCausaleRevoca());
					pagamento.setDatiRevoca(dto.getDatiAggiuntivi());
					ctx.getApplicationLogger().log("rr.stornoPagamentoRichiesto", pagamento.getIur(), pagamento.getImportoPagato().toString());
					pagamentiDaStornare.add(pagamento);
				}
			} else {
				for(AvviaRichiestaStornoDTO.Pagamento p : dto.getPagamento()) {
					it.govpay.bd.model.Pagamento pagamento = rpt.getPagamento(p.getIur());
					if(pagamento.getImportoRevocato() != null) 
						throw new GovPayException(EsitoOperazione.PAG_009, p.getIur());
					pagamento.setCausaleRevoca(p.getCausaleRevoca());
					pagamento.setDatiRevoca(p.getDatiAggiuntivi());
					ctx.getApplicationLogger().log("rr.stornoPagamentoTrovato", pagamento.getIur(), pagamento.getImportoPagato().toString());
					pagamentiDaStornare.add(pagamento);
				}
			}
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.PAG_008, dto.getCodApplicazione());
		}

		if(pagamentiDaStornare.isEmpty()) {
			throw new GovPayException(EsitoOperazione.PAG_011);
		}


		Rr rr = RrUtils.buildRr(rpt, pagamentiDaStornare);
		RrBD rrBD = null;

		Notifica notifica = new Notifica(rr, TipoNotifica.ATTIVAZIONE, configWrapper);
		it.govpay.core.business.Notifica notificaBD = new it.govpay.core.business.Notifica();
		

		ctx.getApplicationLogger().log("rr.creazioneRr", rr.getCodDominio(), rr.getIuv(), rr.getCcp(), rr.getCodMsgRevoca());
		try {
			rrBD = new RrBD(configWrapper);
			
			rrBD.setupConnection(configWrapper.getTransactionID());
			
			rrBD.setAtomica(false);
			
			rrBD.setAutoCommit(false);
			
			PagamentiBD pagamentiBD = new PagamentiBD(rrBD);
			pagamentiBD.setAtomica(false);
			
			rrBD.insertRr(rr);
			notifica.setIdRr(rr.getId());
			boolean schedulaThreadInvio = notificaBD.inserisciNotifica(notifica, rrBD);
			for(it.govpay.bd.model.Pagamento pagamento : pagamentiDaStornare) {
				pagamento.setIdRr(rr.getId());
				pagamentiBD.updatePagamento(pagamento);
			}
			rrBD.commit();
			
			if(schedulaThreadInvio)
				ThreadExecutorManager.getClientPoolExecutorNotifica().execute(new InviaNotificaThread(notifica, ctx));
		} catch (ServiceException e) {
			if(rrBD != null && !rrBD.isAutoCommit() )
				rrBD.rollback();
			throw e;
		} 
		finally {
			if(rrBD != null)
				rrBD.closeConnection();
		}

		AvviaRichiestaStornoDTOResponse response = new AvviaRichiestaStornoDTOResponse();
		response.setCodRichiestaStorno(rr.getCodMsgRevoca());

		NodoClient nodoInviaRRClient = null;
		rrBD = null;
		try {

			String operationId = appContext.setupNodoClient(rpt.getStazione(configWrapper).getCodStazione(), rr.getCodDominio(), Azione.nodoInviaRichiestaStorno);
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codMessaggioRevoca", rr.getCodMsgRevoca()));
			ctx.getApplicationLogger().log("rr.invioRr");

			nodoInviaRRClient = new it.govpay.core.utils.client.NodoClient(rpt.getIntermediario(configWrapper), operationId, giornale);
			// salvataggio id Rpt/ versamento/ pagamento
			nodoInviaRRClient.getEventoCtx().setCodDominio(rpt.getCodDominio());
			nodoInviaRRClient.getEventoCtx().setIuv(rpt.getIuv());
			nodoInviaRRClient.getEventoCtx().setCcp(rpt.getCcp());
			nodoInviaRRClient.getEventoCtx().setIdA2A(rpt.getVersamento().getApplicazione(configWrapper).getCodApplicazione());
			nodoInviaRRClient.getEventoCtx().setIdPendenza(rpt.getVersamento().getCodVersamentoEnte());
			if(rpt.getPagamentoPortale() != null)
				nodoInviaRRClient.getEventoCtx().setIdPagamento(rpt.getPagamentoPortale().getIdSessione());
			
			Risposta risposta = RrUtils.inviaRr(nodoInviaRRClient, rr, rpt, operationId);
			nodoInviaRRClient.getEventoCtx().setEsito(Esito.OK);
			
			rrBD = new RrBD(configWrapper);
			
			rrBD.setupConnection(configWrapper.getTransactionID());

			if(risposta.getEsito() == null || !risposta.getEsito().equals("OK")) {

				ctx.getApplicationLogger().log("rr.invioRrKo");

				// RR rifiutata dal Nodo
				// Aggiorno lo stato e ritorno l'errore

				FaultBean fb = risposta.getFaultBean();
				String descrizione = null; 
				if(fb != null)
					descrizione = fb.getFaultCode() + ": " + fb.getFaultString();

				if(nodoInviaRRClient != null) {
					nodoInviaRRClient.getEventoCtx().setSottotipoEsito(fb.getFaultCode());
					nodoInviaRRClient.getEventoCtx().setEsito(Esito.KO);
					nodoInviaRRClient.getEventoCtx().setDescrizioneEsito(descrizione);
				}

				rrBD.updateRr(rr.getId(), StatoRr.RR_RIFIUTATA_NODO, descrizione);

				log.warn(risposta.getLog());
				throw new GovPayException(risposta.getFaultBean());
			} else {
				ctx.getApplicationLogger().log("rr.invioRrOk");
				// RPT accettata dal Nodo
				// Aggiorno lo stato e ritorno
				rrBD.updateRr(rr.getId(), StatoRr.RR_ACCETTATA_NODO, null);
				return response;
			}
		} catch (ClientException e) {
			if(nodoInviaRRClient != null) {
				nodoInviaRRClient.getEventoCtx().setSottotipoEsito(e.getResponseCode() + "");
				nodoInviaRRClient.getEventoCtx().setEsito(Esito.FAIL);
				nodoInviaRRClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				nodoInviaRRClient.getEventoCtx().setException(e);
			}	
			ctx.getApplicationLogger().log("rr.invioRrKo");
			if(rrBD == null) {
				rrBD = new RrBD(configWrapper);
				rrBD.setupConnection(configWrapper.getTransactionID());
			}
			rrBD.updateRr(rr.getId(), StatoRr.RR_ERRORE_INVIO_A_NODO, e.getMessage());
			throw new GovPayException(EsitoOperazione.NDP_000, e);
		} finally {
			if(nodoInviaRRClient != null && nodoInviaRRClient.getEventoCtx().isRegistraEvento()) {
				EventiBD eventiBD = new EventiBD(configWrapper);
				eventiBD.insertEvento(nodoInviaRRClient.getEventoCtx().toEventoDTO());
			}
		}
	}

	public Rr chiediStorno(Applicazione applicazioneAutenticata, String codRichiestaStorno) throws ServiceException, GovPayException {
		if(!applicazioneAutenticata.getUtenza().isAbilitato())
			throw new GovPayException(EsitoOperazione.APP_001, applicazioneAutenticata.getCodApplicazione());

		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RrBD rrBD = new RrBD(configWrapper);
		try {
			Rr rr = rrBD.getRr(codRichiestaStorno,true);
			if(rr.getRpt().getIdApplicazione() != null && !applicazioneAutenticata.getId().equals(rr.getRpt().getIdApplicazione())) {
				throw new GovPayException(EsitoOperazione.APP_004); 
			}
			return rr;
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.PAG_010);
		}
	}
}
