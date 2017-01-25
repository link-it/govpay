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

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiMarcaBolloDigitale;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDominio;
import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaFG;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaG;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.gov.digitpa.schemas._2011.pagamenti.StAutenticazioneSoggetto;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersFG;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersG;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoVersamento;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediCopiaRT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediCopiaRTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediStatoRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediStatoRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaCarrelloRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRPT;
import it.gov.digitpa.schemas._2011.ws.paa.TipoElementoListaRPT;
import it.gov.digitpa.schemas._2011.ws.paa.TipoListaRPT;
import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.util.IuvUtils;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.business.model.Risposta;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.thread.InviaRptThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Anagrafica;
import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Dominio;
import it.govpay.model.Evento;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Intermediario;
import it.govpay.model.Iuv;
import it.govpay.model.Portale;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Evento.TipoEvento;
import it.govpay.model.Rpt.FirmaRichiesta;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.servizi.commons.EsitoOperazione;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.Property;

public class RptUtils {

	private static Logger log = LogManager.getLogger();

	public static String buildUUID35() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static Rpt buildRpt(
			Intermediario intermediario, Stazione stazione, String codCarrello, 
			Versamento versamento, Iuv iuv, String ccp, Portale portale, Psp psp, 
			Canale canale, Anagrafica versante, String autenticazione, 
			String ibanAddebito, String redirect, BasicBD bd) throws ServiceException {

		Rpt rpt = new Rpt();
		rpt.setCallbackURL(redirect);
		rpt.setCcp(ccp);
		rpt.setCodCarrello(codCarrello);
		rpt.setCodDominio(versamento.getUo(bd).getDominio(bd).getCodDominio());
		rpt.setCodMsgRichiesta(buildUUID35());
		rpt.setCodSessione(null);
		rpt.setCodStazione(stazione.getCodStazione());
		rpt.setDataAggiornamento(new Date());
		rpt.setDataMsgRichiesta(new Date());
		rpt.setDescrizioneStato(null);
		rpt.setFirmaRichiesta(versamento.getApplicazione(bd).getFirmaRichiesta());
		rpt.setId(null);
		rpt.setIdCanale(canale.getId());
		if(portale != null)
			rpt.setIdPortale(portale.getId());
		rpt.setIdVersamento(versamento.getId());
		rpt.setIuv(iuv.getIuv());
		rpt.setModelloPagamento(canale.getModelloPagamento());
		rpt.setPspRedirectURL(null);
		rpt.setStato(StatoRpt.RPT_ATTIVATA);
		rpt.setIdTransazioneRpt(GpThreadLocal.get().getTransactionId());
		rpt.setVersamento(versamento);
		rpt.setCanale(canale);
		rpt.setPsp(psp);
		rpt.setStazione(stazione);
		rpt.setIntermediario(intermediario);

		CtRichiestaPagamentoTelematico richiestaRPT = buildRPT(rpt, versamento, portale, versante, canale, autenticazione, ibanAddebito, bd);
		byte[] rptXml;
		try {
			rptXml = JaxbUtils.toByte(richiestaRPT);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		rpt.setXmlRpt(rptXml);
		return rpt;
	}

	private static CtRichiestaPagamentoTelematico buildRPT(Rpt rpt, Versamento versamento, Portale portale, Anagrafica versante, Canale canale, String autenticazione, String ibanAddebito, BasicBD bd) throws ServiceException {
		CtRichiestaPagamentoTelematico ctRpt = new CtRichiestaPagamentoTelematico();
		ctRpt.setVersioneOggetto(Rpt.VERSIONE);
		CtDominio ctDominio = new CtDominio();
		ctDominio.setIdentificativoDominio(rpt.getCodDominio());
		ctDominio.setIdentificativoStazioneRichiedente(versamento.getApplicazione(bd).getCodApplicazione());
		ctRpt.setDominio(ctDominio);
		ctRpt.setIdentificativoMessaggioRichiesta(rpt.getCodMsgRichiesta());
		ctRpt.setDataOraMessaggioRichiesta(rpt.getDataMsgRichiesta());
		ctRpt.setAutenticazioneSoggetto(StAutenticazioneSoggetto.fromValue(autenticazione.toString()));
		ctRpt.setSoggettoVersante(buildSoggettoVersante(versante));
		ctRpt.setSoggettoPagatore(buildSoggettoPagatore(versamento.getAnagraficaDebitore()));
		ctRpt.setEnteBeneficiario(buildEnteBeneficiario(versamento.getUo(bd).getDominio(bd), versamento.getUo(bd), bd));
		ctRpt.setDatiVersamento(buildDatiVersamento(rpt, versamento, canale, ibanAddebito, bd));
		return ctRpt;
	}

	private static CtSoggettoVersante buildSoggettoVersante(Anagrafica versante) {
		if(versante == null) return null;
		CtSoggettoVersante soggettoVersante = new CtSoggettoVersante();
		CtIdentificativoUnivocoPersonaFG idUnivocoVersante = new CtIdentificativoUnivocoPersonaFG();
		String cFiscale = versante.getCodUnivoco();
		idUnivocoVersante.setCodiceIdentificativoUnivoco(cFiscale);
		idUnivocoVersante.setTipoIdentificativoUnivoco((cFiscale.length() == 16) ? StTipoIdentificativoUnivocoPersFG.F : StTipoIdentificativoUnivocoPersFG.G);
		soggettoVersante.setAnagraficaVersante(getNotEmpty(versante.getRagioneSociale()));
		soggettoVersante.setCapVersante(getNotEmpty(versante.getCap()));
		soggettoVersante.setCivicoVersante(getNotEmpty(versante.getCivico()));
		soggettoVersante.setEMailVersante(getNotEmpty(versante.getEmail()));
		soggettoVersante.setIdentificativoUnivocoVersante(idUnivocoVersante);
		soggettoVersante.setIndirizzoVersante(getNotEmpty(versante.getIndirizzo()));
		soggettoVersante.setLocalitaVersante(getNotEmpty(versante.getLocalita()));
		soggettoVersante.setNazioneVersante(getNotEmpty(versante.getNazione()));
		soggettoVersante.setProvinciaVersante(getNotEmpty(versante.getProvincia()));
		return soggettoVersante;
	}

	private static CtSoggettoPagatore buildSoggettoPagatore(Anagrafica debitore) {
		CtSoggettoPagatore soggettoDebitore = new CtSoggettoPagatore();
		CtIdentificativoUnivocoPersonaFG idUnivocoDebitore = new CtIdentificativoUnivocoPersonaFG();
		String cFiscale = debitore.getCodUnivoco();
		idUnivocoDebitore.setCodiceIdentificativoUnivoco(cFiscale);
		idUnivocoDebitore.setTipoIdentificativoUnivoco((cFiscale.length() == 16) ? StTipoIdentificativoUnivocoPersFG.F : StTipoIdentificativoUnivocoPersFG.G);
		soggettoDebitore.setAnagraficaPagatore(debitore.getRagioneSociale());
		soggettoDebitore.setCapPagatore(getNotEmpty(debitore.getCap()));
		soggettoDebitore.setCivicoPagatore(getNotEmpty(debitore.getCivico()));
		soggettoDebitore.setEMailPagatore(getNotEmpty(debitore.getEmail()));
		soggettoDebitore.setIdentificativoUnivocoPagatore(idUnivocoDebitore);
		soggettoDebitore.setIndirizzoPagatore(getNotEmpty(debitore.getIndirizzo()));
		soggettoDebitore.setLocalitaPagatore(getNotEmpty(debitore.getLocalita()));
		soggettoDebitore.setNazionePagatore(getNotEmpty(debitore.getNazione()));
		soggettoDebitore.setProvinciaPagatore(getNotEmpty(debitore.getProvincia()));
		return soggettoDebitore;
	}

	public static CtEnteBeneficiario buildEnteBeneficiario(Dominio dominio, UnitaOperativa uo, BasicBD bd) throws ServiceException {

		CtEnteBeneficiario enteBeneficiario = new CtEnteBeneficiario();
		CtIdentificativoUnivocoPersonaG idUnivocoBeneficiario = new CtIdentificativoUnivocoPersonaG();
		idUnivocoBeneficiario.setCodiceIdentificativoUnivoco(dominio.getCodDominio());
		idUnivocoBeneficiario.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersG.G);
		enteBeneficiario.setIdentificativoUnivocoBeneficiario(idUnivocoBeneficiario);
		enteBeneficiario.setDenominazioneBeneficiario(dominio.getRagioneSociale());

		try {
			Anagrafica anagrafica = dominio.getAnagrafica(bd);
			enteBeneficiario.setCapBeneficiario(getNotEmpty(anagrafica.getCap()));
			enteBeneficiario.setCivicoBeneficiario(getNotEmpty(anagrafica.getCivico()));
			enteBeneficiario.setIndirizzoBeneficiario(getNotEmpty(anagrafica.getIndirizzo()));
			enteBeneficiario.setLocalitaBeneficiario(getNotEmpty(anagrafica.getLocalita()));
			enteBeneficiario.setNazioneBeneficiario(getNotEmpty(anagrafica.getNazione()));
			enteBeneficiario.setProvinciaBeneficiario(getNotEmpty(anagrafica.getProvincia()));
		} catch (NotFoundException e) {
		}

		if(uo.getAnagrafica() != null) {
			if(uo.getAnagrafica().getCodUnivoco() != null && uo.getAnagrafica().getCodUnivoco().trim().length()>0)
				enteBeneficiario.setCodiceUnitOperBeneficiario(uo.getAnagrafica().getCodUnivoco());
			if(uo.getAnagrafica().getRagioneSociale() != null && uo.getAnagrafica().getRagioneSociale().trim().length()>0)
				enteBeneficiario.setDenomUnitOperBeneficiario(uo.getAnagrafica().getRagioneSociale());
			if(uo.getAnagrafica().getIndirizzo() != null && uo.getAnagrafica().getIndirizzo().trim().length()>0)
				enteBeneficiario.setIndirizzoBeneficiario(uo.getAnagrafica().getIndirizzo());
			if(uo.getAnagrafica().getCivico() != null && uo.getAnagrafica().getCivico().trim().length()>0)
				enteBeneficiario.setCivicoBeneficiario(uo.getAnagrafica().getCivico());
			if(uo.getAnagrafica().getCap() != null && uo.getAnagrafica().getCap().trim().length()>0)
				enteBeneficiario.setCapBeneficiario(uo.getAnagrafica().getCap());
			if(uo.getAnagrafica().getLocalita() != null && uo.getAnagrafica().getLocalita().trim().length()>0)
				enteBeneficiario.setLocalitaBeneficiario(uo.getAnagrafica().getLocalita());
			if(uo.getAnagrafica().getProvincia() != null && uo.getAnagrafica().getProvincia().trim().length()>0)
				enteBeneficiario.setProvinciaBeneficiario(uo.getAnagrafica().getProvincia());
			if(uo.getAnagrafica().getNazione() != null && uo.getAnagrafica().getNazione().trim().length()>0)
				enteBeneficiario.setNazioneBeneficiario(uo.getAnagrafica().getNazione());
		}
		return enteBeneficiario;
	}

	private static String getNotEmpty(String text) {
		if(text == null || text.trim().isEmpty())
			return null;
		else
			return text;
	}

	private static CtDatiVersamentoRPT buildDatiVersamento(Rpt rpt, Versamento versamento, Canale canale, String ibanAddebito, BasicBD bd) throws ServiceException {
		CtDatiVersamentoRPT datiVersamento = new CtDatiVersamentoRPT();
		datiVersamento.setDataEsecuzionePagamento(rpt.getDataMsgRichiesta());
		datiVersamento.setImportoTotaleDaVersare(versamento.getImportoTotale());
		datiVersamento.setTipoVersamento(StTipoVersamento.fromValue(canale.getTipoVersamento().getCodifica()));
		datiVersamento.setIdentificativoUnivocoVersamento(rpt.getIuv());
		datiVersamento.setCodiceContestoPagamento(rpt.getCcp());
		datiVersamento.setFirmaRicevuta(rpt.getFirmaRichiesta().getCodifica());
		datiVersamento.setIbanAddebito(ibanAddebito != null ? ibanAddebito : null);

		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(bd);
		for (SingoloVersamento singoloVersamento : singoliVersamenti) {
			datiVersamento.getDatiSingoloVersamento().add(buildDatiSingoloVersamento(rpt, singoloVersamento, bd));
		}

		return datiVersamento;
	}

	private static CtDatiSingoloVersamentoRPT buildDatiSingoloVersamento(Rpt rpt, SingoloVersamento singoloVersamento, BasicBD bd) throws ServiceException  {
		CtDatiSingoloVersamentoRPT datiSingoloVersamento = new CtDatiSingoloVersamentoRPT();
		datiSingoloVersamento.setImportoSingoloVersamento(singoloVersamento.getImportoSingoloVersamento());
		
		if(singoloVersamento.getIbanAccredito(bd) != null) {
			IbanAccredito ibanAccredito = singoloVersamento.getIbanAccredito(bd);
			datiSingoloVersamento.setBicAccredito(getNotEmpty(ibanAccredito.getCodBicAccredito()));
			datiSingoloVersamento.setBicAppoggio(getNotEmpty(ibanAccredito.getCodBicAppoggio()));
			datiSingoloVersamento.setIbanAppoggio(getNotEmpty(ibanAccredito.getCodIbanAppoggio()));
			datiSingoloVersamento.setIbanAccredito(getNotEmpty(ibanAccredito.getCodIban()));
		} else {
			CtDatiMarcaBolloDigitale marcaBollo = new CtDatiMarcaBolloDigitale();
			marcaBollo.setHashDocumento(singoloVersamento.getHashDocumento());
			marcaBollo.setProvinciaResidenza(singoloVersamento.getProvinciaResidenza());
			marcaBollo.setTipoBollo(singoloVersamento.getTipoBollo().getCodifica());
			datiSingoloVersamento.setDatiMarcaBolloDigitale(marcaBollo);
			
		}
		datiSingoloVersamento.setDatiSpecificiRiscossione(singoloVersamento.getTipoContabilita(bd).getCodifica() + "/" + singoloVersamento.getCodContabilita(bd));
		datiSingoloVersamento.setCausaleVersamento(buildCausaleSingoloVersamento(rpt.getIuv(), singoloVersamento.getImportoSingoloVersamento()));
		return datiSingoloVersamento;
	}

	private static final DecimalFormat nFormatter = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));

	public static String buildCausaleSingoloVersamento(String iuv, BigDecimal importoTotale) {
		//Controllo se lo IUV che mi e' stato passato e' ISO11640:2011
		if(IuvUtils.checkISO11640(iuv))
			return "/RFS/" + iuv + "/" + nFormatter.format(importoTotale);
		else 
			return "/RFB/" + iuv + "/" + nFormatter.format(importoTotale);
	}



	public static it.govpay.core.business.model.Risposta inviaRPT(Rpt rpt, BasicBD bd) throws GovPayException, ClientException, ServiceException {
		if(bd != null) bd.closeConnection();
		Evento evento = new Evento();
		it.govpay.core.business.model.Risposta risposta = null;
		try {
			NodoClient client = new it.govpay.core.utils.client.NodoClient(rpt.getIntermediario(bd));
			NodoInviaRPT inviaRPT = new NodoInviaRPT();
			inviaRPT.setIdentificativoCanale(rpt.getCanale(bd).getCodCanale());
			inviaRPT.setIdentificativoIntermediarioPSP(rpt.getCanale(bd).getCodIntermediario());
			inviaRPT.setIdentificativoPSP(rpt.getPsp(bd).getCodPsp());
			inviaRPT.setPassword(rpt.getStazione(bd).getPassword());
			inviaRPT.setRpt(rpt.getXmlRpt());
			
			// FIX Bug Nodo che richiede firma vuota in caso di NESSUNA
			if(rpt.getFirmaRichiesta() == FirmaRichiesta.NESSUNA)
				inviaRPT.setTipoFirma("");
			else
				inviaRPT.setTipoFirma(rpt.getFirmaRichiesta().getCodifica());
			risposta = new it.govpay.core.business.model.Risposta(client.nodoInviaRPT(rpt.getIntermediario(bd), rpt.getStazione(bd), rpt, inviaRPT)); 
			return risposta;
		} finally {
			// Se mi chiama InviaRptThread, BD e' null
			boolean newCon = bd == null;
			if(!newCon) 
				bd.setupConnection(GpThreadLocal.get().getTransactionId());
			else {
				bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			}
			
			try {
				GiornaleEventi giornale = new GiornaleEventi(bd);
				buildEvento(evento, rpt, risposta, TipoEvento.nodoInviaRPT, bd);
				giornale.registraEvento(evento);
			} finally {
				if(newCon) bd.closeConnection();
			}
		}
	}

	public static it.govpay.core.business.model.Risposta inviaRPT(Intermediario intermediario, Stazione stazione, List<Rpt> rpts, BasicBD bd) throws GovPayException, ClientException, ServiceException {
		if(rpts.size() == 1) {
			return inviaRPT(rpts.get(0), bd);
		} else {
			bd.closeConnection();
			Evento evento = new Evento();
			it.govpay.core.business.model.Risposta risposta = null;
			try {
				NodoClient client = new it.govpay.core.utils.client.NodoClient(intermediario);
				NodoInviaCarrelloRPT inviaCarrelloRpt = new NodoInviaCarrelloRPT();
				inviaCarrelloRpt.setIdentificativoCanale(rpts.get(0).getCanale(bd).getCodCanale());
				inviaCarrelloRpt.setIdentificativoIntermediarioPSP(rpts.get(0).getCanale(bd).getCodIntermediario());
				inviaCarrelloRpt.setIdentificativoPSP(rpts.get(0).getPsp(bd).getCodPsp());
				inviaCarrelloRpt.setPassword(stazione.getPassword());
				TipoListaRPT listaRpt = new TipoListaRPT();
				for(Rpt rpt : rpts) {
					TipoElementoListaRPT elementoListaRpt = new TipoElementoListaRPT();
					elementoListaRpt.setCodiceContestoPagamento(rpt.getCcp());
					elementoListaRpt.setIdentificativoDominio(rpt.getCodDominio());
					elementoListaRpt.setIdentificativoUnivocoVersamento(rpt.getIuv());
					elementoListaRpt.setRpt(rpt.getXmlRpt());
					if(rpt.getFirmaRichiesta() == FirmaRichiesta.NESSUNA)
						elementoListaRpt.setTipoFirma("");
					else
						elementoListaRpt.setTipoFirma(rpt.getFirmaRichiesta().getCodifica());
					listaRpt.getElementoListaRPT().add(elementoListaRpt);
				}
				inviaCarrelloRpt.setListaRPT(listaRpt);
				risposta = new it.govpay.core.business.model.Risposta(client.nodoInviaCarrelloRPT(intermediario, stazione, inviaCarrelloRpt, rpts.get(0).getCodCarrello())); 
				return risposta;
			} finally {
				bd.setupConnection(GpThreadLocal.get().getTransactionId());

				GiornaleEventi giornale = new GiornaleEventi(bd);
				for(Rpt rpt : rpts) {
					buildEvento(evento, rpt, risposta, TipoEvento.nodoInviaCarrelloRPT, bd);
					giornale.registraEvento(evento);
				}
			}
		}
	}

	private static void buildEvento(Evento evento, Rpt rpt, Risposta risposta, TipoEvento tipoEvento, BasicBD bd) throws ServiceException {
		evento.setAltriParametriRichiesta(null);
		evento.setAltriParametriRisposta(null);
		evento.setCategoriaEvento(CategoriaEvento.INTERFACCIA);
		evento.setCcp(rpt.getCcp());
		evento.setCodCanale(rpt.getCanale(bd).getCodCanale());
		evento.setCodDominio(rpt.getCodDominio());
		evento.setCodPsp(rpt.getPsp(bd).getCodPsp());
		evento.setCodStazione(rpt.getCodStazione());
		evento.setComponente(Evento.COMPONENTE);
		evento.setDataRisposta(new Date());
		evento.setErogatore(Evento.NDP);
		if(risposta != null)
			evento.setEsito(risposta.getEsito());
		else
			evento.setEsito("Errore di trasmissione al Nodo");
		evento.setFruitore(rpt.getIntermediario(bd).getDenominazione());
		evento.setIuv(rpt.getIuv());
		evento.setSottotipoEvento(null);
		evento.setTipoEvento(tipoEvento);
		evento.setTipoVersamento(rpt.getCanale(bd).getTipoVersamento());

	}

	public static void inviaRPTAsync(Rpt rpt, BasicBD bd) throws ServiceException {
		InviaRptThread t = new InviaRptThread(rpt, bd);
		ThreadExecutorManager.getClientPoolExecutor().execute(t);
	}

	public static NodoChiediStatoRPTRisposta chiediStatoRPT(Intermediario intermediario, Stazione stazione, Rpt rpt, BasicBD bd) throws GovPayException, ClientException {

		NodoClient client = new it.govpay.core.utils.client.NodoClient(intermediario);

		NodoChiediStatoRPT nodoChiediStatoRPT = new NodoChiediStatoRPT();
		nodoChiediStatoRPT.setCodiceContestoPagamento(rpt.getCcp());
		nodoChiediStatoRPT.setIdentificativoDominio(rpt.getCodDominio());
		nodoChiediStatoRPT.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
		nodoChiediStatoRPT.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
		nodoChiediStatoRPT.setIdentificativoUnivocoVersamento(rpt.getIuv());
		nodoChiediStatoRPT.setPassword(stazione.getPassword());
		return client.nodoChiediStatoRpt(nodoChiediStatoRPT, rpt.getCodDominio());
	}


	public static boolean aggiornaRptDaNpD(NodoClient client, Rpt rpt, BasicBD bd) throws GovPayException, ServiceException, ClientException, NdpException {
		try {
			switch (rpt.getStato()) {
			case RPT_RIFIUTATA_NODO:
			case RPT_RIFIUTATA_PSP:
			case RPT_ERRORE_INVIO_A_PSP:
			case RT_ACCETTATA_PA:
				log.info("Rpt in stato terminale [" + rpt.getStato()+ "]. Aggiornamento non necessario.");
				return false;
				
			case RPT_ATTIVATA:
				log.info("Rpt in stato non terminale [" + rpt.getStato()+ "]. Eseguo una rispedizione della RPT.");
				inviaRPTAsync(rpt, bd);
				return false;
				
			default:
				log.info("Rpt in stato non terminale [" + rpt.getStato()+ "]. Eseguo un aggiornamento dello stato.");
	
				bd.closeConnection();
				String transactionId = null;
				NodoChiediStatoRPTRisposta risposta = null;
				try {
					transactionId = GpThreadLocal.get().openTransaction();
					GpThreadLocal.get().setupNodoClient(rpt.getStazione(bd).getCodStazione(), rpt.getCodDominio(), Azione.nodoChiediStatoRPT);
					NodoChiediStatoRPT richiesta = new NodoChiediStatoRPT();
					richiesta.setIdentificativoDominio(rpt.getCodDominio());
					richiesta.setIdentificativoIntermediarioPA(rpt.getStazione(bd).getIntermediario(bd).getCodIntermediario());
					richiesta.setIdentificativoStazioneIntermediarioPA(rpt.getStazione(bd).getCodStazione());
					richiesta.setPassword(rpt.getStazione(bd).getPassword());
					richiesta.setIdentificativoUnivocoVersamento(rpt.getIuv());
					richiesta.setCodiceContestoPagamento(rpt.getCcp());
	
					risposta = client.nodoChiediStatoRpt(richiesta, rpt.getStazione(bd).getIntermediario(bd).getDenominazione());
				} finally {
					bd.setupConnection(GpThreadLocal.get().getTransactionId());
					GpThreadLocal.get().closeTransaction(transactionId);
				}
				if(risposta.getFault() != null) {
					if(it.govpay.core.exceptions.NdpException.FaultNodo.valueOf(risposta.getFault().getFaultCode()).equals(it.govpay.core.exceptions.NdpException.FaultNodo.PPT_RPT_SCONOSCIUTA)) {
						log.info("RPT inesistente. Aggiorno lo stato in " + StatoRpt.RPT_ERRORE_INVIO_A_NODO + ".");
						rpt.setStato(StatoRpt.RPT_ERRORE_INVIO_A_NODO);
						rpt.setDescrizioneStato("Stato sul nodo: PPT_RPT_SCONOSCIUTA");
	
						RptBD rptBD = new RptBD(bd);
						rptBD.updateRpt(rpt.getId(), StatoRpt.RPT_ERRORE_INVIO_A_NODO, null, null, null);
						return true;
					}
					throw new GovPayException(EsitoOperazione.NDP_001, risposta.getFault().getFaultCode() + ": " + risposta.getFault().getFaultString() != null ? risposta.getFault().getFaultString() : "");
				} else {
					StatoRpt nuovoStato = Rpt.StatoRpt.toEnum(risposta.getEsito().getStato());
					log.info("Acquisito stato della RPT: " + nuovoStato + ".");
	
					RptBD rptBD = null;
					
					switch (nuovoStato) {
					case RT_ACCETTATA_NODO:
					case RT_ESITO_SCONOSCIUTO_PA:
					case RT_RIFIUTATA_PA:
					case RT_ACCETTATA_PA:
						log.info("Richiesta dell'RT al Nodo dei Pagamenti [CodDominio: " + rpt.getCodDominio() + "][IUV: " + rpt.getIuv() + "][CCP: " + rpt.getCcp() + "].");
	
						bd.closeConnection();
						transactionId = null;
						NodoChiediCopiaRTRisposta nodoChiediCopiaRTRisposta = null;
						try {
							transactionId = GpThreadLocal.get().openTransaction();
							GpThreadLocal.get().setupNodoClient(rpt.getStazione(bd).getCodStazione(), rpt.getCodDominio(), Azione.nodoChiediCopiaRT);
							NodoChiediCopiaRT nodoChiediCopiaRT = new NodoChiediCopiaRT();
							nodoChiediCopiaRT.setIdentificativoDominio(rpt.getCodDominio());
							nodoChiediCopiaRT.setIdentificativoIntermediarioPA(rpt.getIntermediario(bd).getCodIntermediario());
							nodoChiediCopiaRT.setIdentificativoStazioneIntermediarioPA(rpt.getStazione(bd).getCodStazione());
							nodoChiediCopiaRT.setPassword(rpt.getStazione(bd).getPassword());
							nodoChiediCopiaRT.setIdentificativoUnivocoVersamento(rpt.getIuv());
							nodoChiediCopiaRT.setCodiceContestoPagamento(rpt.getCcp());
							nodoChiediCopiaRTRisposta = client.nodoChiediCopiaRT(nodoChiediCopiaRT, rpt.getIntermediario(bd).getDenominazione());
						} finally {
							bd.setupConnection(GpThreadLocal.get().getTransactionId());
							GpThreadLocal.get().closeTransaction(transactionId);
						}
	
						rptBD = new RptBD(bd);
						
						byte[] rtByte = null;
						try {
							ByteArrayOutputStream output = new ByteArrayOutputStream();
							DataHandler dh = nodoChiediCopiaRTRisposta.getRt();
							dh.writeTo(output);
							rtByte = output.toByteArray();
						} catch (IOException e) {
							log.error("Errore durante la lettura dell'RT: " + e);
							rptBD.updateRpt(rpt.getId(), nuovoStato, null, null, null);
							rpt.setStato(nuovoStato);
							rpt.setDescrizioneStato(null);
							throw new GovPayException(EsitoOperazione.INTERNAL, e);
						}
	
						if(nodoChiediCopiaRTRisposta.getFault() != null) {
							log.info("Fault nell'acquisizione dell'RT: [" + risposta.getFault().getFaultCode() + "] " + risposta.getFault().getFaultString());
							log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in " + nuovoStato + ".");
							rptBD.updateRpt(rpt.getId(), nuovoStato, null, null, null);
							rpt.setStato(nuovoStato);
							rpt.setDescrizioneStato(null);
							return true;
						}
						
						GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("ccp", rpt.getCcp()));
						GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("codDominio", rpt.getCodDominio()));
						GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("iuv", rpt.getIuv()));
						GpThreadLocal.get().log("pagamento.recuperoRt");
						rpt = RtUtils.acquisisciRT(rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp(), nodoChiediCopiaRTRisposta.getTipoFirma(), rtByte, bd);
						GpThreadLocal.get().getContext().getResponse().addGenericProperty(new Property("esitoPagamento", rpt.getEsitoPagamento().toString()));
						GpThreadLocal.get().log("pagamento.acquisizioneRtOk");
						return true;
					default:
						log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in " + nuovoStato + ".");
						rptBD = new RptBD(bd);
						rptBD.updateRpt(rpt.getId(), nuovoStato, null, null, null);
						rpt.setStato(nuovoStato);
						rpt.setDescrizioneStato(null);
						return false;
					}
				}
			}
		} catch(NotFoundException e) {
			throw new ServiceException(e);
		}
	}
}
