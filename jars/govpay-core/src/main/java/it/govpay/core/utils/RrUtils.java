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
package it.govpay.core.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtDatiEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtDatiRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtDatiSingolaRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtDatiSingoloEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtDominio;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.ER;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.RR;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtSoggettoVersante;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.StTipoIdentificativoUnivoco;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.StTipoIdentificativoUnivocoPersFG;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRichiestaStorno;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.eventi.EventoCooperazione;
import it.govpay.bd.model.eventi.EventoCooperazione.TipoEvento;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.business.model.Risposta;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.RtUtils.EsitoValidazione;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Rr.StatoRr;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento.StatoVersamento;

public class RrUtils extends NdpValidationUtils {

	private static Logger log = LoggerWrapperFactory.getLogger(RrUtils.class);

	public static String buildUUID35() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static Rr buildRr(Rpt rpt, List<Pagamento> pagamenti, BasicBD bd) throws ServiceException {

		CtRicevutaTelematica ctRt = null;
		
		try {
			ctRt = JaxbUtils.toRT(rpt.getXmlRt());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		RR ctRr = buildCtRr(ctRt, pagamenti, bd);
		
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

	private static RR buildCtRr(CtRicevutaTelematica ctRt, List<Pagamento> pagamenti, BasicBD bd) throws ServiceException {
		RR ctRr = new RR();
		ctRr.setVersioneOggetto(ctRt.getVersioneOggetto());
		ctRr.setDominio(toDominio(ctRt.getDominio()));
		ctRr.setIdentificativoMessaggioRevoca(buildUUID35());
		ctRr.setDataOraMessaggioRevoca(new Date());
		ctRr.setIstitutoAttestante(toIstitutoAttestante(ctRt.getIstitutoAttestante()));
		ctRr.setSoggettoVersante(toSoggettoVersante(ctRt.getSoggettoVersante()));
		ctRr.setSoggettoPagatore(toSoggettoPagatore(ctRt.getSoggettoPagatore()));
		ctRr.setDatiRevoca(buildDatiRevoca(ctRt, pagamenti, bd));
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

	private static CtDatiRevoca buildDatiRevoca(CtRicevutaTelematica ctRt, List<Pagamento> pagamenti, BasicBD bd) throws ServiceException {
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
	
	public static it.govpay.core.business.model.Risposta inviaRr(Rr rr, Rpt rpt, BasicBD bd) throws GovPayException, ClientException, ServiceException {
		// Chiamate per acquisire dati prima di chiudere la connessione
		rpt.getIntermediario(bd);
		rpt.getStazione(bd);
		
		if(bd != null) bd.closeConnection();
		EventoCooperazione evento = new EventoCooperazione();
		it.govpay.core.business.model.Risposta risposta = null;
		try {
			NodoClient client = new it.govpay.core.utils.client.NodoClient(rpt.getIntermediario(bd), bd);
			NodoInviaRichiestaStorno inviaRR = new NodoInviaRichiestaStorno();
			inviaRR.setCodiceContestoPagamento(rr.getCcp());
			inviaRR.setIdentificativoDominio(rr.getCodDominio());
			inviaRR.setIdentificativoIntermediarioPA(rpt.getIntermediario(bd).getCodIntermediario());
			inviaRR.setIdentificativoStazioneIntermediarioPA(rpt.getStazione(bd).getCodStazione());
			inviaRR.setIdentificativoUnivocoVersamento(rr.getIuv());
			inviaRR.setPassword(rpt.getStazione(bd).getPassword());
			inviaRR.setRr(rr.getXmlRr());
			risposta = new it.govpay.core.business.model.Risposta(client.nodoInviaRichiestaStorno(inviaRR)); 
			return risposta;
		} finally {
			// Se mi chiama InviaRptThread, BD e' null
			if(bd != null) 
				bd.setupConnection(GpThreadLocal.get().getTransactionId());
			else
				bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			GiornaleEventi giornale = new GiornaleEventi(bd);
			buildEventoCooperazione(evento, rpt, risposta, TipoEvento.nodoInviaRichiestaStorno, bd);
			giornale.registraEventoCooperazione(evento);
		}
	}
	
	private static void buildEventoCooperazione(EventoCooperazione evento, Rpt rpt, Risposta risposta, TipoEvento tipoEvento, BasicBD bd) throws ServiceException {
		evento.setAltriParametriRichiesta(null);
		evento.setAltriParametriRisposta(null);
		evento.setCategoriaEvento(CategoriaEvento.INTERFACCIA_COOPERAZIONE);
		evento.setCcp(rpt.getCcp());
		evento.setCodCanale(rpt.getCodCanale());
		evento.setCodDominio(rpt.getCodDominio());
		evento.setCodPsp(rpt.getCodPsp());
		evento.setCodStazione(rpt.getStazione(bd).getCodStazione());
		evento.setComponente(EventoCooperazione.COMPONENTE);
		evento.setDataRisposta(new Date());
		evento.setErogatore(EventoCooperazione.NDP);
		if(risposta != null)
			evento.setEsito(risposta.getEsito());
		else
			evento.setEsito("Errore di trasmissione al Nodo");
		evento.setFruitore(rpt.getIntermediario(bd).getDenominazione());
		evento.setIuv(rpt.getIuv());
		evento.setSottotipoEvento(null);
		evento.setTipoEvento(tipoEvento);
		evento.setTipoVersamento(rpt.getTipoVersamento());
		
		if(rpt.getVersamento(bd) != null) {
			evento.setIdVersamento(rpt.getVersamento(bd).getId());
		}
		
		try {
			if(rpt.getPagamentoPortale(bd) != null) {
				evento.setIdPagamentoPortale(rpt.getPagamentoPortale(bd).getId());
			}
		} catch (NotFoundException e) {
		}
	}

	public static Rr acquisisciEr(String identificativoDominio, String identificativoUnivocoVersamento, String codiceContestoPagamento, byte[] er, BasicBD bd) throws NdpException, ServiceException {
		bd.setAutoCommit(false);
		bd.enableSelectForUpdate();
		
		GpContext ctx = GpThreadLocal.get();
		
		ER ctEr = null;
		// Validazione Sintattica
		try {
			ctEr = JaxbUtils.toER(er);
		} catch (Exception e) {
			log.error("Errore durante la validazione sintattica della Ricevuta Telematica.", e);
			throw new NdpException(FaultPa.PAA_SINTASSI_XSD, identificativoDominio, e.getCause().getMessage());
		}
		
		ctx.getContext().getRequest().addGenericProperty(new Property("codMessaggioEsito", ctEr.getIdentificativoMessaggioEsito()));
		ctx.getContext().getRequest().addGenericProperty(new Property("importo", ctEr.getDatiRevoca().getImportoTotaleRevocato().toString()));
		ctx.log("er.acquisizione");
		
		RrBD rrBD = new RrBD(bd);
		Rr rr = null;
		try {
			rr = rrBD.getRr(ctEr.getRiferimentoMessaggioRevoca());
		} catch (NotFoundException e) {
			throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, identificativoDominio, "RR con identificativo " + ctEr.getRiferimentoMessaggioRevoca() + " sconosciuta");
		}
		
		if(rr.getStato().equals(StatoRr.ER_ACCETTATA_PA)) {
			throw new NdpException(FaultPa.PAA_ER_DUPLICATA, "Esito già acquisito in data " + rr.getDataMsgRevoca(), identificativoDominio);
		}
		
		RtUtils.EsitoValidazione esito = null;
			try {
				esito = RrUtils.validaSemantica(rr, JaxbUtils.toRR(rr.getXmlRr()), ctEr, bd);
			} catch (JAXBException e) {
				throw new ServiceException(e);
			} catch (SAXException e) {
				throw new ServiceException(e);
			}
			
		
		if(esito.validato && esito.errori.size() > 0) {
			ctx.log("er.validazioneWarn", esito.getDiagnostico());
		} 
		
		if (!esito.validato) {
			ctx.log("er.validazioneFail", esito.getDiagnostico());
			rr.setStato(StatoRr.ER_RIFIUTATA_PA);
			rr.setDescrizioneStato(esito.getFatal());
			rr.setXmlEr(er);
			rrBD.updateRr(rr.getId(), rr);
			throw new NdpException(FaultPa.PAA_SEMANTICA, identificativoDominio, esito.getFatal());
		}
		
		// Rileggo per avere la lettura dello stato rpt in transazione
		rr.setCodMsgEsito(ctEr.getIdentificativoMessaggioEsito());
		rr.setDataMsgEsito(ctEr.getDataOraMessaggioEsito());
		rr.setImportoTotaleRevocato(ctEr.getDatiRevoca().getImportoTotaleRevocato());
		rr.setStato(StatoRr.ER_ACCETTATA_PA);
		rr.setXmlEr(er);
		
		// Aggiorno l'RR con i dati dell'ER
		rrBD.updateRr(rr.getId(), rr);
		
		List<Pagamento> pagamenti = rr.getPagamenti(bd);
		
		PagamentiBD pagamentiBD = new PagamentiBD(bd);
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		
		Versamento v = rr.getPagamenti(versamentiBD).get(0).getSingoloVersamento(bd).getVersamento(bd);
		if(rr.getImportoTotaleRevocato().compareTo(BigDecimal.ZERO) == 0) {
			
		} else {
			SingoloVersamento sv = null;
			for(Pagamento pagamento : pagamenti) {
				
				if(pagamento.getImportoRevocato().compareTo(BigDecimal.ZERO) == 0){ 
					ctx.log("er.acquisizioneRevoca", pagamento.getIur(), pagamento.getImportoRevocato().toString(), pagamento.getSingoloVersamento(bd).getCodSingoloVersamentoEnte(), pagamento.getSingoloVersamento(bd).getStatoSingoloVersamento().toString());
					continue;
				}
					
				pagamentiBD.updatePagamento(pagamento);
				
				sv = pagamento.getSingoloVersamento(bd);
				
				// TODO gestire lo storno di un pagamento doppio o storno parziale
				
				versamentiBD.updateStatoSingoloVersamento(sv.getId(), StatoSingoloVersamento.NON_ESEGUITO);
				ctx.log("er.acquisizioneRevoca", pagamento.getIur(), pagamento.getImportoRevocato().toString(), pagamento.getSingoloVersamento(bd).getCodSingoloVersamentoEnte(), StatoSingoloVersamento.NON_ESEGUITO.toString());
			}
			versamentiBD.updateStatoVersamento(v.getId(), StatoVersamento.NON_ESEGUITO, "Pagamenti stornati");
			v.setStatoVersamento(StatoVersamento.NON_ESEGUITO);
		}
		
		Notifica notifica = new Notifica(rr, TipoNotifica.RICEVUTA, bd);
		NotificheBD notificheBD = new NotificheBD(bd);
		notificheBD.insertNotifica(notifica);
		
		bd.commit();
		bd.disableSelectForUpdate();
		
		ThreadExecutorManager.getClientPoolExecutor().execute(new InviaNotificaThread(notifica, bd));
		
		ctx.log("er.acquisizioneOk", v.getCodVersamentoEnte(), v.getStatoVersamento().toString());
		log.info("ER acquisita con successo.");
		
		return rr;
	}

	private static EsitoValidazione validaSemantica(Rr rr, RR ctRr, ER ctEr, BasicBD bd) throws ServiceException {
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
				pagamento = rr.getPagamento(singolaRevoca.getIdentificativoUnivocoRiscossione(),indiceDati, bd);
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
