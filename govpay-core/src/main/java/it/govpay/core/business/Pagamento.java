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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.slf4j.Logger;

import it.gov.digitpa.schemas._2011.ws.paa.FaultBean;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediListaPendentiRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediListaPendentiRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.TipoRPTPendente;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.core.business.model.AvviaRichiestaStornoDTO;
import it.govpay.core.business.model.AvviaRichiestaStornoDTOResponse;
import it.govpay.core.business.model.AvviaTransazioneDTO;
import it.govpay.core.business.model.AvviaTransazioneDTOResponse;
import it.govpay.core.business.model.AvviaTransazioneDTOResponse.RifTransazione;
import it.govpay.core.business.model.Risposta;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.rs.v1.costanti.EsitoOperazione;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.RrUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Anagrafica;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Intermediario;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.bd.model.Applicazione;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Rr.StatoRr;

public class Pagamento extends BasicBD {

	private static Logger log = LoggerWrapperFactory.getLogger(Pagamento.class);

	public Pagamento(BasicBD bd) {
		super(bd);
	}

	public AvviaTransazioneDTOResponse avviaTransazione(AvviaTransazioneDTO dto) throws GovPayException, ServiceException {

		GpContext ctx = GpThreadLocal.get();
		List<Versamento> versamenti = new ArrayList<>();

		for(Object v : dto.getVersamentoOrVersamentoRef()) {
			Versamento versamentoModel = null;

			if(v instanceof it.govpay.servizi.commons.Versamento) {
				it.govpay.servizi.commons.Versamento versamento = (it.govpay.servizi.commons.Versamento) v;
				ctx.log("rpt.acquisizioneVersamento", versamento.getCodApplicazione(), versamento.getCodVersamentoEnte());
				versamentoModel = VersamentoUtils.toVersamentoModel(versamento, this);
				versamentoModel.setIuvProposto(versamento.getIuv());
			} else {
				it.govpay.servizi.commons.VersamentoKey versamento = (it.govpay.servizi.commons.VersamentoKey) v;

				String codDominio = null, codApplicazione = null, codVersamentoEnte = null, iuv = null, bundlekey = null, codUnivocoDebitore = null;

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

				it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(this);
				versamentoModel = versamentoBusiness.chiediVersamento(codApplicazione, codVersamentoEnte, bundlekey, codUnivocoDebitore, codDominio, iuv);
			}
			
			if(!versamentoModel.getUo(this).isAbilitato()) {
				throw new GovPayException("Il pagamento non puo' essere avviato poiche' uno dei versamenti risulta associato ad una unita' operativa disabilitata [Uo:"+versamentoModel.getUo(this).getCodUo()+"].", EsitoOperazione.UOP_001, versamentoModel.getUo(this).getCodUo());
			}
			
			if(!versamentoModel.getUo(this).getDominio(this).isAbilitato()) {
				throw new GovPayException("Il pagamento non puo' essere avviato poiche' uno dei versamenti risulta associato ad un dominio disabilitato [Dominio:"+versamentoModel.getUo(this).getDominio(this).getCodDominio()+"].", EsitoOperazione.DOM_001, versamentoModel.getUo(this).getDominio(this).getCodDominio());
			}
			
			versamenti.add(versamentoModel);
		}

		Anagrafica versanteModel = VersamentoUtils.toAnagraficaModel(dto.getVersante());
		boolean aggiornaSeEsiste = dto.getAggiornaSeEsisteB() != null ? dto.getAggiornaSeEsisteB() : true;
		it.govpay.core.business.Rpt rptBD = new it.govpay.core.business.Rpt(this);
		List<Rpt> rpts = rptBD.avviaTransazione(versamenti, dto.getApplicazione(), dto.getCanale(), dto.getIbanAddebito(), versanteModel, dto.getAutenticazione(), dto.getUrlRitorno(), aggiornaSeEsiste);

		AvviaTransazioneDTOResponse response = new AvviaTransazioneDTOResponse();

		response.setCodSessione(rpts.get(0).getCodSessione());
		response.setPspRedirectURL(rpts.get(0).getPspRedirectURL());

		for(Rpt rpt : rpts) {
			RifTransazione rifTransazione = response.new RifTransazione();
			rifTransazione.setCcp(rpt.getCcp());
			rifTransazione.setCodApplicazione(rpt.getVersamento(this).getApplicazione(this).getCodApplicazione());
			rifTransazione.setCodDominio(rpt.getCodDominio());
			rifTransazione.setCodVersamentoEnte(rpt.getVersamento(this).getCodVersamentoEnte());
			rifTransazione.setIuv(rpt.getIuv());
			response.getRifTransazioni().add(rifTransazione);
		}

		return response;
	}

	public String verificaTransazioniPendenti() throws GovPayException {

		GpContext ctx = GpThreadLocal.get();
		ctx.log("pendenti.avvio");
		List<String> response = new ArrayList<>();
		try {
			StazioniBD stazioniBD = new StazioniBD(this);
			List<Stazione> lstStazioni = stazioniBD.getStazioni();
			DominiBD dominiBD = new DominiBD(this);
			
			for(Stazione stazione : lstStazioni) {
			
				DominioFilter filter = dominiBD.newFilter();
				filter.setCodStazione(stazione.getCodStazione());
				List<Dominio> lstDomini = dominiBD.findAll(filter);
				
				Intermediario intermediario = stazione.getIntermediario(this);

				this.closeConnection();
				ctx.log("pendenti.acquisizionelistaPendenti", stazione.getCodStazione());
				log.debug("Recupero i pendenti [CodStazione: " + stazione.getCodStazione() + "]");
				
				// Costruisco una mappa di tutti i pagamenti pendenti sul nodo
				// La chiave di lettura e' iuv@ccp

				NodoClient client = new NodoClient(intermediario, this);

				// Le pendenze per specifica durano 60 giorni.
				int finestra = 60;
				Calendar fineFinestra = Calendar.getInstance();
				Calendar inizioFinestra = (Calendar) fineFinestra.clone();
				inizioFinestra.add(Calendar.DATE, -finestra);

				Map<String, String> statiRptPendenti = this.acquisisciPendenti(client,intermediario, stazione, lstDomini, false, inizioFinestra, fineFinestra, 500);
				
				log.info("Identificate sul NodoSPC " + statiRptPendenti.size() + " RPT pendenti");
				ctx.log("pendenti.listaPendentiOk", stazione.getCodStazione(), statiRptPendenti.size() + "");

				// Ho acquisito tutti gli stati pendenti. 
				// Tutte quelle in stato terminale, 
				this.setupConnection(GpThreadLocal.get().getTransactionId());

				RptBD rptBD = new RptBD(this);

				List<String> codDomini = new ArrayList<>();
				for(Dominio d : lstDomini) {
					codDomini.add(d.getCodDominio());
				}
				List<Rpt> rpts = rptBD.getRptPendenti(codDomini);

				log.info("Identificate su GovPay " + rpts.size() + " transazioni pendenti");
				ctx.log("pendenti.listaPendentiGovPayOk", rpts.size() + "");

				// Scorro le transazioni. Se non risulta pendente sul nodo (quindi non e' pendente) la mando in aggiornamento.
				
				Date mezzorafa = new Date(new Date().getTime() - (30 * 60 * 1000));
				
				for(Rpt rpt : rpts) {
					
					// WORKAROUND CONCORRENZA CON INVIO RT DAL NODO
					// SKIPPO LE RPT PENDENTI CREATE MENO DI MEZZ'ORA FA
					
					if(rpt.getDataMsgRichiesta().after(mezzorafa)) {
						log.info("Rpt recente [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] aggiornamento non necessario");
						continue;
					} else {
						log.info("Rpt pendente su GovPay [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "]: stato " + rpt.getStato().name());
					}
					
					// Aggiorno il batch
					BatchManager.aggiornaEsecuzione(this, Operazioni.PND);
					
					String stato = statiRptPendenti.get(rpt.getCodDominio() + "@" + rpt.getIuv() + "@" + rpt.getCcp());
					if(stato != null) {
						log.info("Rpt confermata pendente dal nodo [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "]: stato " + stato);
						ctx.log("pendenti.confermaPendente", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp(), stato);
						StatoRpt statoRpt = StatoRpt.toEnum(stato);
						if(!rpt.getStato().equals(statoRpt)) {
							response.add("[" + rpt.getCodDominio() + " " + rpt.getIuv() + " " + rpt.getCcp() + "]# Aggiornamento in stato " + stato.toString());
							rptBD.updateRpt(rpt.getId(), statoRpt, null, null, null);
						}
					} else {
						log.info("Rpt non pendente sul nodo [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "]: stato " + stato);
						ctx.log("pendenti.confermaNonPendente", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp());
						// Accedo alle entita che serviranno in seguito prima di chiudere la connessione;
						rpt.getStazione(this).getIntermediario(this);
						try {
							RptUtils.aggiornaRptDaNpD(client, rpt, this);
						} catch (NdpException e) {
							ctx.log("pendenti.rptAggiornataKo", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp(), e.getFaultString());
							log.warn("Errore durante l'aggiornamento della RPT: " + e.getFaultString());
							continue;
						} catch (Exception e) {
							ctx.log("pendenti.rptAggiornataFail", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp(), e.getMessage());
							log.warn("Errore durante l'aggiornamento della RPT", e);
							continue;
						}

						if(rpt.getModelloPagamento().equals(ModelloPagamento.ATTIVATO_PRESSO_PSP) && (rpt.getStato().equals(StatoRpt.RPT_ATTIVATA) || rpt.getStato().equals(StatoRpt.RPT_ERRORE_INVIO_A_NODO))) {
							ctx.log("pendenti.rptAttivata", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp());
							log.info("Rpt attivata ma non consegnata [" + rpt.getCodDominio() + "][" + rpt.getIuv() + "][" + rpt.getCcp() + "]: avviata rispedizione al Nodo.");
						} else {
							ctx.log("pendenti.rptAggiornata", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp(), rpt.getStato().toString());
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
	 */
	private Map<String, String> acquisisciPendenti(NodoClient client, Intermediario intermediario, Stazione stazione, List<Dominio> lstDomini, boolean perDominio, Calendar da, Calendar a, long soglia) {
		GpContext ctx = GpThreadLocal.get();
		Map<String, String> statiRptPendenti = new HashMap<>();
		
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
				ctx.log("pendenti.listaPendenti", dominio.getCodDominio(), dateFormat.format(da.getTime()), dateFormat.format(a.getTime()));
			} else {
				log.debug("Richiedo la lista delle RPT pendenti (Stazione " + stazione.getCodStazione() + " dal " + dateFormat.format(da.getTime()) + " al " + dateFormat.format(a.getTime()) + ")");
				ctx.log("pendenti.listaPendenti", stazione.getCodStazione(), dateFormat.format(da.getTime()), dateFormat.format(a.getTime()));
			}
	
			NodoChiediListaPendentiRPTRisposta risposta = null;
			String transactionId = null;
			try {
				transactionId = GpThreadLocal.get().openTransaction();
				GpThreadLocal.get().setupNodoClient(stazione.getCodStazione(), null, Azione.nodoChiediListaPendentiRPT);
				risposta = client.nodoChiediListaPendentiRPT(richiesta, intermediario.getDenominazione());
			} catch (Exception e) {
				log.warn("Errore durante la richiesta di lista pendenti", e);
				// Esco da ciclo while e procedo con il prossimo dominio.
				if(perDominio) {
					ctx.log("pendenti.listaPendentiDominioFail", dominio.getCodDominio(), e.getMessage());
					continue;
				} else {
					ctx.log("pendenti.listaPendentiFail", stazione.getCodStazione(), e.getMessage());
					break;
				}
			} finally {
				if(transactionId != null) {
					GpThreadLocal.get().closeTransaction(transactionId);
				}
			}
	
			if(risposta != null) {
				if(risposta.getFault() != null) {
					log.warn("Ricevuto errore durante la richiesta di lista pendenti: " + risposta.getFault().getFaultCode() + ": " + risposta.getFault().getFaultString());
					
					String fc = risposta.getFault().getFaultCode() != null ? risposta.getFault().getFaultCode() : "-";
					String fs = risposta.getFault().getFaultString() != null ? risposta.getFault().getFaultString() : "-";
					String fd = risposta.getFault().getDescription() != null ? risposta.getFault().getDescription() : "-";
					if(perDominio) {
						ctx.log("pendenti.listaPendentiDominioKo", dominio.getCodDominio(), fc, fs, fd);
						continue;
					} else {
						ctx.log("pendenti.listaPendentiKo", stazione.getCodStazione(), fc, fs, fd);
						break;
					}
				}
		
				if(risposta.getListaRPTPendenti() == null || risposta.getListaRPTPendenti().getRptPendente().isEmpty()) {
					log.debug("Lista pendenti vuota.");
					if(perDominio) {
						ctx.log("pendenti.listaPendentiDominioVuota", dominio.getCodDominio());
						continue;
					} else {
						ctx.log("pendenti.listaPendentiVuota", stazione.getCodStazione());					
						break;
					}
				}
				
				
				
				if(risposta.getListaRPTPendenti().getTotRestituiti() >= soglia) {
					
					// Vedo quanto e' ampia la finestra per capire se dimezzarla o ciclare sui domini
					int finestra = (int) TimeUnit.DAYS.convert((a.getTimeInMillis() - da.getTimeInMillis()), TimeUnit.MILLISECONDS);
					
					if(finestra > 1) {
						ctx.log("pendenti.listaPendentiPiena", stazione.getCodStazione(), dateFormat.format(da.getTime()), dateFormat.format(a.getTime()));	
						finestra = finestra/2;
						Calendar mezzo = (Calendar) a.clone();
						mezzo.add(Calendar.DATE, -finestra);
						log.debug("Lista pendenti con troppi elementi. Ricalcolo la finestra: (dal " + dateFormat.format(da.getTime()) + " a " + dateFormat.format(a.getTime()) + ")");
						statiRptPendenti.putAll(this.acquisisciPendenti(client, intermediario, stazione, lstDomini, false, da, mezzo, soglia));
						mezzo.add(Calendar.DATE, 1);
						statiRptPendenti.putAll(this.acquisisciPendenti(client, intermediario, stazione, lstDomini, false, mezzo, a, soglia));
						return statiRptPendenti;
					} else {
						if(perDominio) {
							ctx.log("pendenti.listaPendentiDominioDailyPiena", dominio.getCodDominio(), dateFormat.format(a.getTime()));
							log.debug("Lista pendenti con troppi elementi, ma impossibile diminuire ulteriormente la finesta. Elenco accettato.");
						} else {
							ctx.log("pendenti.listaPendentiDailyPiena", stazione.getCodStazione(), dateFormat.format(a.getTime()));
							log.debug("Lista pendenti con troppi elementi, scalo a dominio.");
							return this.acquisisciPendenti(client, intermediario, stazione, lstDomini, true, da, a, soglia);
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
		}
		return statiRptPendenti;
	}

	public AvviaRichiestaStornoDTOResponse avviaStorno(AvviaRichiestaStornoDTO dto) throws ServiceException, GovPayException {
		GpContext ctx = GpThreadLocal.get();

		List<it.govpay.bd.model.Pagamento> pagamentiDaStornare = new ArrayList<>(); 
		Rpt rpt = null;
		try {
			RptBD rptBD = new RptBD(this);
			rpt = rptBD.getRpt(dto.getCodDominio(), dto.getIuv(), dto.getCcp());

			if(dto.getPagamento() == null || dto.getPagamento().isEmpty()) {
				for(it.govpay.bd.model.Pagamento pagamento : rpt.getPagamenti(this)) {
					if(pagamento.getImportoRevocato() != null) continue;
					pagamento.setCausaleRevoca(dto.getCausaleRevoca());
					pagamento.setDatiRevoca(dto.getDatiAggiuntivi());
					ctx.log("rr.stornoPagamentoRichiesto", pagamento.getIur(), pagamento.getImportoPagato().toString());
					pagamentiDaStornare.add(pagamento);
				}
			} else {
				for(AvviaRichiestaStornoDTO.Pagamento p : dto.getPagamento()) {
					it.govpay.bd.model.Pagamento pagamento = rpt.getPagamento(p.getIur(), this);
					if(pagamento.getImportoRevocato() != null) 
						throw new GovPayException(EsitoOperazione.PAG_009, p.getIur());
					pagamento.setCausaleRevoca(p.getCausaleRevoca());
					pagamento.setDatiRevoca(p.getDatiAggiuntivi());
					ctx.log("rr.stornoPagamentoTrovato", pagamento.getIur(), pagamento.getImportoPagato().toString());
					pagamentiDaStornare.add(pagamento);
				}
			}
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.PAG_008, dto.getCodApplicazione());
		}

		if(pagamentiDaStornare.isEmpty()) {
			throw new GovPayException(EsitoOperazione.PAG_011);
		}


		Rr rr = RrUtils.buildRr(rpt, pagamentiDaStornare, this);
		RrBD rrBD = new RrBD(this);

		Notifica notifica = new Notifica(rr, TipoNotifica.ATTIVAZIONE, this);
		NotificheBD notificheBD = new NotificheBD(this);
		PagamentiBD pagamentiBD = new PagamentiBD(this);

		ctx.log("rr.creazioneRr", rr.getCodDominio(), rr.getIuv(), rr.getCcp(), rr.getCodMsgRevoca());

		this.setAutoCommit(false);
		rrBD.insertRr(rr);
		notifica.setIdRr(rr.getId());
		notificheBD.insertNotifica(notifica);
		for(it.govpay.bd.model.Pagamento pagamento : pagamentiDaStornare) {
			pagamento.setIdRr(rr.getId());
			pagamentiBD.updatePagamento(pagamento);
		}
		this.commit();

		ThreadExecutorManager.getClientPoolExecutor().execute(new InviaNotificaThread(notifica, this));

		AvviaRichiestaStornoDTOResponse response = new AvviaRichiestaStornoDTOResponse();
		response.setCodRichiestaStorno(rr.getCodMsgRevoca());

		String idTransaction = null;
		try {

			idTransaction = ctx.openTransaction();
			ctx.setupNodoClient(rpt.getStazione(this).getCodStazione(), rr.getCodDominio(), Azione.nodoInviaRichiestaStorno);
			ctx.getContext().getRequest().addGenericProperty(new Property("codMessaggioRevoca", rr.getCodMsgRevoca()));
			ctx.log("rr.invioRr");

			Risposta risposta = RrUtils.inviaRr(rr, rpt, this);

			if(risposta.getEsito() == null || !risposta.getEsito().equals("OK")) {

				ctx.log("rr.invioRrKo");

				// RR rifiutata dal Nodo
				// Aggiorno lo stato e ritorno l'errore

				FaultBean fb = risposta.getFaultBean(0);
				String descrizione = null; 
				if(fb != null)
					descrizione = fb.getFaultCode() + ": " + fb.getFaultString();

				rrBD.updateRr(rr.getId(), StatoRr.RR_RIFIUTATA_NODO, descrizione);

				log.warn(risposta.getLog());
				throw new GovPayException(risposta.getFaultBean(0));
			} else {
				ctx.log("rr.invioRrOk");
				// RPT accettata dal Nodo
				// Aggiorno lo stato e ritorno
				rrBD.updateRr(rr.getId(), StatoRr.RR_ACCETTATA_NODO, null);
				return response;
			}
		} catch (ClientException e) {
			ctx.log("rr.invioRrKo");
			rrBD.updateRr(rr.getId(), StatoRr.RR_ERRORE_INVIO_A_NODO, e.getMessage());
			throw new GovPayException(EsitoOperazione.NDP_000, e);
		} finally {
			ctx.closeTransaction(idTransaction);
		}
	}

	public Rr chiediStorno(Applicazione applicazioneAutenticata, String codRichiestaStorno) throws ServiceException, GovPayException {
		if(!applicazioneAutenticata.getUtenza().isAbilitato())
			throw new GovPayException(EsitoOperazione.APP_001, applicazioneAutenticata.getCodApplicazione());

		RrBD rrBD = new RrBD(this);
		try {
			Rr rr = rrBD.getRr(codRichiestaStorno);
			if(rr.getRpt(this).getIdApplicazione() != null && !applicazioneAutenticata.getId().equals(rr.getRpt(this).getIdApplicazione())) {
				throw new GovPayException(EsitoOperazione.APP_004); 
			}
			return rr;
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.PAG_010);
		}
	}
}
