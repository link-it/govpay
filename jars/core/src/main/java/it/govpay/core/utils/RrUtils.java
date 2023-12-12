/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.context.application.ApplicationContext;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import gov.telematici.pagamenti.ws.rpt.NodoInviaRichiestaStorno;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtDatiEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtDatiRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtDatiSingolaRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtDatiSingoloEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtDominio;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtSoggettoVersante;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.ER;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.RR;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.StTipoIdentificativoUnivoco;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.StTipoIdentificativoUnivocoPersFG;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.business.model.Risposta;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.exceptions.NotificaException;
import it.govpay.core.utils.RtUtils.EsitoValidazione;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Intermediario;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Rr.StatoRr;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.eventi.DatiPagoPA;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class RrUtils extends NdpValidationUtils {

	private static Logger log = LoggerWrapperFactory.getLogger(RrUtils.class);

	public static String buildUUID35() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static Rr buildRr(Rpt rpt, List<Pagamento> pagamenti) throws ServiceException {

		CtRicevutaTelematica ctRt = null;
		
		try {
			ctRt = JaxbUtils.toRT(rpt.getXmlRt(), true);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		RR ctRr = buildCtRr(ctRt, pagamenti);
		
		byte[] xmlRr;
		try {
			xmlRr = JaxbUtils.toByte(ctRr);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		Rr rr = new Rr();
		rr.setCcp(rpt.getCcp());
		rr.setCodDominio(rpt.getCodDominio());
		rr.setCodMsgEsito(null);
		rr.setCodMsgRevoca(ctRr.getIdentificativoMessaggioRevoca());
		rr.setDataMsgRevoca(ctRr.getDataOraMessaggioRevoca());
		rr.setDescrizioneStato(null);
		rr.setIdRpt(rpt.getId());
		rr.setImportoTotaleRevocato(null);
		rr.setImportoTotaleRichiesto(ctRr.getDatiRevoca().getImportoTotaleRevocato());
		rr.setIuv(rpt.getIuv());
		rr.setPagamenti(pagamenti);
		rr.setRpt(rpt);
		rr.setStato(StatoRr.RR_ATTIVATA);
		rr.setXmlEr(null);
		rr.setXmlRr(xmlRr);
		return rr;
	}

	private static RR buildCtRr(CtRicevutaTelematica ctRt, List<Pagamento> pagamenti) throws ServiceException {
		RR ctRr = new RR();
		ctRr.setVersioneOggetto(ctRt.getVersioneOggetto());
		ctRr.setDominio(toDominio(ctRt.getDominio()));
		ctRr.setIdentificativoMessaggioRevoca(buildUUID35());
		ctRr.setDataOraMessaggioRevoca(new Date());
		ctRr.setIstitutoAttestante(toIstitutoAttestante(ctRt.getIstitutoAttestante()));
		ctRr.setSoggettoVersante(toSoggettoVersante(ctRt.getSoggettoVersante()));
		ctRr.setSoggettoPagatore(toSoggettoPagatore(ctRt.getSoggettoPagatore()));
		ctRr.setDatiRevoca(buildDatiRevoca(ctRt, pagamenti));
		return ctRr;
	}

	private static CtSoggettoPagatore toSoggettoPagatore(it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore soggettoPagatore) {
		if(soggettoPagatore == null)
			return null;
		
		CtSoggettoPagatore p = new CtSoggettoPagatore();
		p.setAnagraficaPagatore(soggettoPagatore.getAnagraficaPagatore());
		p.setCapPagatore(soggettoPagatore.getCapPagatore());
		p.setCivicoPagatore(soggettoPagatore.getCivicoPagatore());
		p.setEMailPagatore(soggettoPagatore.getEMailPagatore());
		if(soggettoPagatore.getIdentificativoUnivocoPagatore() != null) {
			it.gov.digitpa.schemas._2011.pagamenti.revoche.CtIdentificativoUnivocoPersonaFG id = new it.gov.digitpa.schemas._2011.pagamenti.revoche.CtIdentificativoUnivocoPersonaFG();
			id.setCodiceIdentificativoUnivoco(soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco());
			id.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.valueOf(soggettoPagatore.getIdentificativoUnivocoPagatore().getTipoIdentificativoUnivoco().toString()));
			p.setIdentificativoUnivocoPagatore(id);
		}
		p.setIndirizzoPagatore(soggettoPagatore.getIndirizzoPagatore());
		p.setLocalitaPagatore(soggettoPagatore.getLocalitaPagatore());
		p.setNazionePagatore(soggettoPagatore.getNazionePagatore());
		p.setProvinciaPagatore(soggettoPagatore.getProvinciaPagatore());
		return p;
	}

	private static CtSoggettoVersante toSoggettoVersante(it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante soggettoVersante) {
		if(soggettoVersante == null)
			return null;
		
		CtSoggettoVersante v = new CtSoggettoVersante();
		v.setAnagraficaVersante(soggettoVersante.getAnagraficaVersante());
		v.setCapVersante(soggettoVersante.getCapVersante());
		v.setCivicoVersante(soggettoVersante.getCivicoVersante());
		v.setEMailVersante(soggettoVersante.getEMailVersante());
		if(soggettoVersante.getIdentificativoUnivocoVersante() != null) {
			it.gov.digitpa.schemas._2011.pagamenti.revoche.CtIdentificativoUnivocoPersonaFG id = new it.gov.digitpa.schemas._2011.pagamenti.revoche.CtIdentificativoUnivocoPersonaFG();
			id.setCodiceIdentificativoUnivoco(soggettoVersante.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco());
			id.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.valueOf(soggettoVersante.getIdentificativoUnivocoVersante().getTipoIdentificativoUnivoco().toString()));
			v.setIdentificativoUnivocoVersante(id);
		}
		v.setIndirizzoVersante(soggettoVersante.getIndirizzoVersante());
		v.setLocalitaVersante(soggettoVersante.getLocalitaVersante());
		v.setNazioneVersante(soggettoVersante.getNazioneVersante());
		v.setProvinciaVersante(soggettoVersante.getProvinciaVersante());
		return v;
	}

	private static CtIstitutoAttestante toIstitutoAttestante(it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante istitutoAttestante) {
		if(istitutoAttestante == null)
			return null;
		
		CtIstitutoAttestante i = new CtIstitutoAttestante();
		i.setCapMittente(istitutoAttestante.getCapAttestante());
		i.setCivicoMittente(istitutoAttestante.getCivicoAttestante());
		i.setCodiceUnitOperMittente(istitutoAttestante.getCodiceUnitOperAttestante());
		i.setDenominazioneMittente(istitutoAttestante.getDenominazioneAttestante());
		i.setDenomUnitOperMittente(istitutoAttestante.getDenomUnitOperAttestante());
		if(istitutoAttestante.getIdentificativoUnivocoAttestante() != null) {
			it.gov.digitpa.schemas._2011.pagamenti.revoche.CtIdentificativoUnivoco id = new it.gov.digitpa.schemas._2011.pagamenti.revoche.CtIdentificativoUnivoco();
			id.setCodiceIdentificativoUnivoco(istitutoAttestante.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco());
			id.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.valueOf(istitutoAttestante.getIdentificativoUnivocoAttestante().getTipoIdentificativoUnivoco().toString()));
			i.setIdentificativoUnivocoMittente(id);
		}
		i.setIndirizzoMittente(istitutoAttestante.getIndirizzoAttestante());
		i.setLocalitaMittente(istitutoAttestante.getLocalitaAttestante());
		i.setNazioneMittente(istitutoAttestante.getNazioneAttestante());
		i.setProvinciaMittente(istitutoAttestante.getProvinciaAttestante());
		return i;
	}

	private static CtDominio toDominio(it.gov.digitpa.schemas._2011.pagamenti.CtDominio dominio) {
		if(dominio == null)
			return null;
		
		CtDominio d = new CtDominio();
		d.setIdentificativoDominio(dominio.getIdentificativoDominio());
		d.setIdentificativoStazioneRichiedente(dominio.getIdentificativoStazioneRichiedente());
		return d;
	}

	private static CtDatiRevoca buildDatiRevoca(CtRicevutaTelematica ctRt, List<Pagamento> pagamenti) throws ServiceException {
		CtDatiRevoca datiRevoca = new CtDatiRevoca();
		datiRevoca.setIdentificativoUnivocoVersamento(ctRt.getDatiPagamento().getIdentificativoUnivocoVersamento());
		datiRevoca.setCodiceContestoPagamento(ctRt.getDatiPagamento().getCodiceContestoPagamento());

		BigDecimal importoTotaleRevocato = BigDecimal.ZERO;
		for(Pagamento pagamento : pagamenti) {
			CtDatiSingolaRevoca singolaRevoca = new CtDatiSingolaRevoca();
			singolaRevoca.setCausaleRevoca(pagamento.getCausaleRevoca());
			singolaRevoca.setDatiAggiuntiviRevoca(pagamento.getDatiRevoca());
			singolaRevoca.setIdentificativoUnivocoRiscossione(pagamento.getIur());
			singolaRevoca.setSingoloImportoRevocato(pagamento.getImportoPagato());
			importoTotaleRevocato = importoTotaleRevocato.add(pagamento.getImportoPagato());
			datiRevoca.getDatiSingolaRevocas().add(singolaRevoca);
		}
		
		datiRevoca.setImportoTotaleRevocato(importoTotaleRevocato);
		return datiRevoca;
	}
	
	public static it.govpay.core.business.model.Risposta inviaRr(NodoClient client, Rr rr, Rpt rpt, String operationId) throws GovPayException, ClientException, ServiceException, UtilsException {
		// Chiamate per acquisire dati prima di chiudere la connessione
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		Intermediario intermediario = rpt.getIntermediario(configWrapper);
		Stazione stazione = rpt.getStazione(configWrapper);
		
		it.govpay.core.business.model.Risposta risposta = null;
		try {
			NodoInviaRichiestaStorno inviaRR = new NodoInviaRichiestaStorno();
			inviaRR.setCodiceContestoPagamento(rr.getCcp());
			inviaRR.setIdentificativoDominio(rr.getCodDominio());
			inviaRR.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
			inviaRR.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
			inviaRR.setIdentificativoUnivocoVersamento(rr.getIuv());
			inviaRR.setPassword(stazione.getPassword());
			inviaRR.setRr(rr.getXmlRr());
			risposta = new it.govpay.core.business.model.Risposta(client.nodoInviaRichiestaStorno(inviaRR)); 
			return risposta;
		} finally {
			// Se mi chiama InviaRptThread, BD e' null
//			if(bd != null) 
//				bd.setupConnection(ContextThreadLocal.get().getTransactionId());
//			else
//				bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			popolaEventoCooperazione(client, rpt, risposta, intermediario, stazione);
		}
	}
	
	private static void popolaEventoCooperazione(NodoClient client, Rpt rpt, Risposta risposta, Intermediario intermediario, Stazione stazione) throws ServiceException {
		
		DatiPagoPA datiPagoPA = new DatiPagoPA();
		datiPagoPA.setCodCanale(rpt.getCodCanale());
		datiPagoPA.setCodPsp(rpt.getCodPsp());
		datiPagoPA.setCodStazione(stazione.getCodStazione());
		datiPagoPA.setErogatore(it.govpay.model.Evento.NDP);
		datiPagoPA.setFruitore(intermediario.getCodIntermediario());
		datiPagoPA.setTipoVersamento(rpt.getTipoVersamento());
		datiPagoPA.setTipoVersamento(rpt.getTipoVersamento());
		datiPagoPA.setModelloPagamento(rpt.getModelloPagamento());
		datiPagoPA.setCodIntermediarioPsp(rpt.getCodIntermediarioPsp());
		datiPagoPA.setCodDominio(rpt.getCodDominio());
		client.getEventoCtx().setDatiPagoPA(datiPagoPA);
	}

	public static Rr acquisisciEr(String identificativoDominio, String identificativoUnivocoVersamento, String codiceContestoPagamento, byte[] er) throws NdpException, ServiceException, UtilsException {
		
		
		IContext ctx = ContextThreadLocal.get();
		ApplicationContext appContext = (ApplicationContext) ctx.getApplicationContext();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		RrBD rrBD = null;
		ER ctEr = null;
		// Validazione Sintattica
		try {
			ctEr = JaxbUtils.toER(er);
		} catch (Exception e) {
			log.error("Errore durante la validazione sintattica della Ricevuta Telematica.", e);
			throw new NdpException(FaultPa.PAA_SINTASSI_XSD, e.getCause().getMessage(), identificativoDominio);
		}
		
		appContext.getRequest().addGenericProperty(new Property("codMessaggioEsito", ctEr.getIdentificativoMessaggioEsito()));
		appContext.getRequest().addGenericProperty(new Property("importo", ctEr.getDatiRevoca().getImportoTotaleRevocato().toString()));
		ctx.getApplicationLogger().log("er.acquisizione");
		
		try {
			rrBD = new RrBD(configWrapper);
			
			rrBD.setupConnection(configWrapper.getTransactionID());
			
			rrBD.setAutoCommit(false);
			
			rrBD.enableSelectForUpdate();
			
			rrBD.setAtomica(false);
			
			Rr rr = null;
			try {
				rr = rrBD.getRr(ctEr.getRiferimentoMessaggioRevoca());
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, "RR con identificativo " + ctEr.getRiferimentoMessaggioRevoca() + " sconosciuta", identificativoDominio);
			}
			
			if(rr.getStato().equals(StatoRr.ER_ACCETTATA_PA)) {
				throw new NdpException(FaultPa.PAA_ER_DUPLICATA, "Esito già acquisito in data " + rr.getDataMsgRevoca(), identificativoDominio);
			}
			
			RtUtils.EsitoValidazione esito = null;
				try {
					esito = RrUtils.validaSemantica(rr, JaxbUtils.toRR(rr.getXmlRr()), ctEr);
				} catch (JAXBException e) {
					throw new ServiceException(e);
				} catch (SAXException e) {
					throw new ServiceException(e);
				}
				
			
			if(esito.validato && esito.errori.size() > 0) {
				ctx.getApplicationLogger().log("er.validazioneWarn", esito.getDiagnostico());
			} 
			
			if (!esito.validato) {
				ctx.getApplicationLogger().log("er.validazioneFail", esito.getDiagnostico());
				rr.setStato(StatoRr.ER_RIFIUTATA_PA);
				rr.setDescrizioneStato(esito.getFatal());
				rr.setXmlEr(er);
				rrBD.updateRr(rr.getId(), rr);
				throw new NdpException(FaultPa.PAA_SEMANTICA, esito.getFatal(), identificativoDominio);
			}
			
			// Rileggo per avere la lettura dello stato rpt in transazione
			rr.setCodMsgEsito(ctEr.getIdentificativoMessaggioEsito());
			rr.setDataMsgEsito(ctEr.getDataOraMessaggioEsito());
			rr.setImportoTotaleRevocato(ctEr.getDatiRevoca().getImportoTotaleRevocato());
			rr.setStato(StatoRr.ER_ACCETTATA_PA);
			rr.setXmlEr(er);
			
			// Aggiorno l'RR con i dati dell'ER
			rrBD.updateRr(rr.getId(), rr);
			
			List<Pagamento> pagamenti = rr.getPagamenti();
			
			PagamentiBD pagamentiBD = new PagamentiBD(rrBD);
			VersamentiBD versamentiBD = new VersamentiBD(rrBD);
			
			Versamento v = pagamenti.get(0).getSingoloVersamento(rrBD).getVersamentoBD(rrBD);
			if(rr.getImportoTotaleRevocato().compareTo(BigDecimal.ZERO) == 0) {
				
			} else {
				SingoloVersamento sv = null;
				for(Pagamento pagamento : pagamenti) {
					
					sv = pagamento.getSingoloVersamento(rrBD);
					if(pagamento.getImportoRevocato().compareTo(BigDecimal.ZERO) == 0){ 
						
						ctx.getApplicationLogger().log("er.acquisizioneRevoca", pagamento.getIur(), pagamento.getImportoRevocato().toString(), sv.getCodSingoloVersamentoEnte(), sv.getStatoSingoloVersamento().toString());
						continue;
					}
						
					pagamentiBD.updatePagamento(pagamento);
					
//					sv = pagamento.getSingoloVersamento(bd);
					
					// TODO gestire lo storno di un pagamento doppio o storno parziale
					
					versamentiBD.updateStatoSingoloVersamento(sv.getId(), StatoSingoloVersamento.NON_ESEGUITO);
					ctx.getApplicationLogger().log("er.acquisizioneRevoca", pagamento.getIur(), pagamento.getImportoRevocato().toString(), sv.getCodSingoloVersamentoEnte(), StatoSingoloVersamento.NON_ESEGUITO.toString());
				}
				versamentiBD.updateStatoVersamento(v.getId(), StatoVersamento.NON_ESEGUITO, "Pagamenti stornati");
				v.setStatoVersamento(StatoVersamento.NON_ESEGUITO);
			}
			
			Notifica notifica = new Notifica(rr, TipoNotifica.RICEVUTA, configWrapper);
			it.govpay.core.business.Notifica notificaBD = new it.govpay.core.business.Notifica();
			boolean schedulaThreadInvio = notificaBD.inserisciNotifica(notifica, rrBD);
			
			rrBD.commit();
			
			rrBD.disableSelectForUpdate();
			
			if(schedulaThreadInvio)
				ThreadExecutorManager.getClientPoolExecutorNotifica().execute(new InviaNotificaThread(notifica, ctx));
			
			ctx.getApplicationLogger().log("er.acquisizioneOk", v.getCodVersamentoEnte(), v.getStatoVersamento().toString());
			log.info("ER acquisita con successo.");
			
			return rr;
		} catch (NotificaException | IOException e) {
			if(rrBD != null)
				rrBD.rollback();
			throw new ServiceException(e);
		}catch (NdpException | ServiceException | UtilsException e) {
			if(rrBD != null)
				rrBD.rollback();
			throw e;
		} finally {
			if(rrBD != null)
				rrBD.closeConnection();
		}
	}

	private static EsitoValidazione validaSemantica(Rr rr, RR ctRr, ER ctEr) throws ServiceException {
		EsitoValidazione esito = new RtUtils().new EsitoValidazione();
		validaSemantica(ctRr.getIstitutoAttestante(),ctEr.getIstitutoAttestante(), esito); 
		validaSemantica(ctRr.getSoggettoPagatore(),ctEr.getSoggettoPagatore(), esito);
		validaSemantica(ctRr.getSoggettoVersante(),ctEr.getSoggettoVersante(), esito);
		validaSemantica(ctRr.getDominio(),ctEr.getDominio(), esito);
		
		CtDatiRevoca datiRr = ctRr.getDatiRevoca();
		CtDatiEsitoRevoca datiEr = ctEr.getDatiRevoca();
		valida(datiRr.getCodiceContestoPagamento(), datiEr.getCodiceContestoPagamento(), esito, "CodiceContestoPagamento non corrisponde", true);
		valida(datiRr.getIdentificativoUnivocoVersamento(), datiEr.getIdentificativoUnivocoVersamento(), esito, "IdentificativoUnivocoVersamento non corrisponde", true);
		
		int indiceDati = 1;
		for(CtDatiSingoloEsitoRevoca singolaRevoca : datiEr.getDatiSingolaRevocas()) {
			
			if(singolaRevoca.getSingoloImportoRevocato().compareTo(BigDecimal.ZERO) == 0) {
				//Importo non revocato. Non faccio niente
				continue;
			}

			Pagamento pagamento = null;
			try {
				pagamento = rr.getPagamento(singolaRevoca.getIdentificativoUnivocoRiscossione(),indiceDati);
			} catch (NotFoundException e) {
				esito.addErrore("Ricevuto esito di revoca non richiesta", true);
			}
			
			if(pagamento != null) {
				pagamento.setDatiEsitoRevoca(singolaRevoca.getDatiAggiuntiviEsito());
				pagamento.setEsitoRevoca(singolaRevoca.getCausaleEsito());
				pagamento.setImportoRevocato(singolaRevoca.getSingoloImportoRevocato());
			}
			indiceDati++;
		}
		
		return esito;
	}
}
