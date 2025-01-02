/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.RptBuilder;
import it.govpay.model.Anagrafica;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.VersioneRPT;
import it.govpay.stampe.model.QuietanzaPagamentoInput;
import it.govpay.stampe.model.QuietanzaPagamentoInput.ElencoVoci;
import it.govpay.stampe.model.VoceRicevutaTelematicaInput;
import it.govpay.stampe.pdf.quietanzaPagamento.QuietanzaPagamentoPdf;
import it.govpay.stampe.pdf.quietanzaPagamento.utils.QuietanzaPagamentoProperties;
import it.govpay.stampe.pdf.rt.RicevutaTelematicaCostanti;

public class QuietanzaPagamento {

	private static Logger log = LoggerWrapperFactory.getLogger(QuietanzaPagamento.class);
	private SimpleDateFormat sdfSoloData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfDataOraMinuti = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public QuietanzaPagamento() {
		//donothing
	}

	public byte[] creaPdfQuietanzaPagamento(Rendicontazione rendicontazione, Pagamento pagamento, SingoloVersamento singoloVersamento, Versamento versamento, Fr fr) throws ServiceException {
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
			QuietanzaPagamentoProperties quietanzaPagamentoProperties = QuietanzaPagamentoProperties.getInstance();
			Dominio dominio = versamento.getDominio(configWrapper);
			String codDominio = dominio.getCodDominio();
			QuietanzaPagamentoInput input = fromRendicontazione(dominio, rendicontazione, pagamento, singoloVersamento, versamento, fr, configWrapper);
			
			File jasperFile = null; 
			if(GovpayConfig.getInstance().getTemplateQuietanzaPagamento() != null) {
				jasperFile = new File(GovpayConfig.escape(GovpayConfig.getInstance().getTemplateQuietanzaPagamento()));
			}
			
			return QuietanzaPagamentoPdf.getInstance().creaQuietanzaPagamento(log, input, codDominio, quietanzaPagamentoProperties, jasperFile);
		}catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	private QuietanzaPagamentoInput fromRendicontazione(Dominio dominio, Rendicontazione rendicontazione, Pagamento pagamento, SingoloVersamento singoloVersamento, Versamento versamento, Fr fr, BDConfigWrapper configWrapper) throws Exception{
		QuietanzaPagamentoInput input = new QuietanzaPagamentoInput();

		this.impostaAnagraficaEnteCreditore(dominio, input);

		input.setIUV(pagamento.getIuv());

		StringBuilder sbIstitutoAttestante = new StringBuilder();
		if(fr.getRagioneSocialePsp() != null){
			sbIstitutoAttestante.append(fr.getRagioneSocialePsp());
			sbIstitutoAttestante.append(", ");
			sbIstitutoAttestante.append(fr.getCodPsp());
		} else {
			sbIstitutoAttestante.append(fr.getCodPsp());
		}
		input.setIstituto(sbIstitutoAttestante.toString());

		input.setElencoVoci(this.getElencoVoci(rendicontazione, pagamento, singoloVersamento, input));
		input.setImporto(pagamento.getImportoPagato().doubleValue());

		input.setOggettoDelPagamento(versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().getSimple() : "");
		input.setStato(RicevutaTelematicaCostanti.PAGAMENTO_ESEGUITO);

		Anagrafica soggettoPagatore = versamento.getAnagraficaDebitore(); 
		if(soggettoPagatore != null) {
			this.impostaIndirizzoSoggettoPagatore(input, soggettoPagatore);
		}

		return input;
	}


	private ElencoVoci getElencoVoci(Rendicontazione rendicontazione, Pagamento pagamento, SingoloVersamento singoloVersamento, QuietanzaPagamentoInput input) {
		ElencoVoci elencoVoci = new ElencoVoci();

		VoceRicevutaTelematicaInput voce = new VoceRicevutaTelematicaInput();

		String descrizione = RptBuilder.buildCausaleSingoloVersamento(pagamento.getIuv(), singoloVersamento.getImportoSingoloVersamento(), singoloVersamento.getDescrizione(), singoloVersamento.getDescrizioneCausaleRPT());
		
		voce.setDescrizione(descrizione);
		voce.setIdRiscossione(rendicontazione.getIur());
		voce.setImporto(pagamento.getImportoPagato().doubleValue());
		voce.setStato(pagamento.getImportoPagato().compareTo(BigDecimal.ZERO) == 0 ? RicevutaTelematicaCostanti.PAGAMENTO_NON_ESEGUITO : RicevutaTelematicaCostanti.PAGAMENTO_ESEGUITO);

		elencoVoci.getVoce().add(voce);

		setDataOperazione(input, pagamento.getDataPagamento());

		return elencoVoci;
	}

	private void impostaAnagraficaEnteCreditore(it.govpay.bd.model.Dominio dominio, QuietanzaPagamentoInput input) throws ServiceException {
		String codDominio = dominio.getCodDominio();

		input.setEnteDenominazione(dominio.getRagioneSociale());
		input.setCfEnte(codDominio);
		// se e' presente un logo lo inserisco altrimemti verra' caricato il logo di default.
		if(dominio.getLogo() != null && dominio.getLogo().length > 0)
			input.setLogoEnte(new String(dominio.getLogo()));

		this.impostaIndirizzoEnteCreditore(dominio, input);
	}

	private void impostaIndirizzoEnteCreditore(it.govpay.bd.model.Dominio dominio, QuietanzaPagamentoInput input) throws ServiceException {
		Anagrafica anagraficaEnteCreditore = dominio.getAnagrafica();
		if(anagraficaEnteCreditore != null) {
			String indirizzo = StringUtils.isNotEmpty(anagraficaEnteCreditore.getIndirizzo()) ? anagraficaEnteCreditore.getIndirizzo() : "";
			String civico = StringUtils.isNotEmpty(anagraficaEnteCreditore.getCivico()) ? anagraficaEnteCreditore.getCivico() : "";
			String cap = StringUtils.isNotEmpty(anagraficaEnteCreditore.getCap()) ? anagraficaEnteCreditore.getCap() : "";
			String localita = StringUtils.isNotEmpty(anagraficaEnteCreditore.getLocalita()) ? anagraficaEnteCreditore.getLocalita() : "";
			String provincia = StringUtils.isNotEmpty(anagraficaEnteCreditore.getProvincia()) ? (" (" +anagraficaEnteCreditore.getProvincia() +")" ) : "";
			// Indirizzo piu' civico impostati se non e' vuoto l'indirizzo
			String indirizzoCivico = StringUtils.isNotEmpty(indirizzo) ? indirizzo + " " + civico : "";
			// capCittaProv impostati se e' valorizzata la localita'
			String capCitta = StringUtils.isNotEmpty(localita) ? (cap + " " + localita + provincia) : "";

			// Inserisco la virgola se la prima riga non e' vuota
			String indirizzoEnte = StringUtils.isNotEmpty(indirizzoCivico) ? indirizzoCivico + "," : "";

			input.setIndirizzoEnte(indirizzoEnte);

			input.setLuogoEnte(capCitta);
		}
	}

	private void impostaIndirizzoSoggettoPagatore(QuietanzaPagamentoInput input, Anagrafica soggettoPagatore) {
		if(soggettoPagatore != null) {
			String indirizzo = StringUtils.isNotEmpty(soggettoPagatore.getIndirizzo()) ? soggettoPagatore.getIndirizzo() : "";
			String civico = StringUtils.isNotEmpty(soggettoPagatore.getCivico()) ? soggettoPagatore.getCivico() : "";
			String cap = StringUtils.isNotEmpty(soggettoPagatore.getCap()) ? soggettoPagatore.getCap() : "";
			String localita = StringUtils.isNotEmpty(soggettoPagatore.getLocalita()) ? soggettoPagatore.getLocalita() : "";
			String provincia = StringUtils.isNotEmpty(soggettoPagatore.getProvincia()) ? (" (" +soggettoPagatore.getProvincia() +")" ) : "";
			// Indirizzo piu' civico impostati se non e' vuoto l'indirizzo
			String indirizzoCivico = StringUtils.isNotEmpty(indirizzo) ? indirizzo + " " + civico : "";
			// capCittaProv impostati se e' valorizzata la localita'
			String capCitta = StringUtils.isNotEmpty(localita) ? (cap + " " + localita + provincia) : "";

			// Inserisco la virgola se la prima riga non e' vuota
			String indirizzoEnte = StringUtils.isNotEmpty(indirizzoCivico) ? indirizzoCivico + "," : ""; 

			input.setIndirizzoSoggetto(indirizzoEnte);

			input.setLuogoSoggetto(capCitta);

			input.setSoggetto(StringUtils.isNotEmpty(soggettoPagatore.getRagioneSociale()) ? soggettoPagatore.getRagioneSociale() : "");
			input.setCfSoggetto(StringUtils.isNotEmpty(soggettoPagatore.getCodUnivoco()) ? soggettoPagatore.getCodUnivoco() : "");
		}
	}

	private void setDataOperazione(QuietanzaPagamentoInput input, Date dataRpt) {
		Calendar cRpt = Calendar.getInstance();
		cRpt.setTime(dataRpt);
		if((cRpt.get(Calendar.HOUR_OF_DAY) + cRpt.get(Calendar.MINUTE) + cRpt.get(Calendar.SECOND)) == 0) {
			input.setDataOperazione( this.sdfSoloData.format(dataRpt));
		} else {
			input.setDataOperazione( this.sdfDataOraMinuti.format(dataRpt));
		}
	}

	public it.govpay.bd.model.Rpt creaRPTFromRendicontazione(Rendicontazione rendicontazione, Pagamento pagamento, SingoloVersamento singoloVersamento, Versamento versamento, Fr fr, BDConfigWrapper configWrapper) {
		it.govpay.bd.model.Rpt input = new it.govpay.bd.model.Rpt();

		input.setCodDominio(pagamento.getCodDominio());
		input.setIuv(pagamento.getIuv());
		input.setCcp(pagamento.getIur());
		input.setDataMsgRicevuta(pagamento.getDataPagamento());
		input.setDataMsgRichiesta(pagamento.getDataPagamento());
		input.setCodPsp(fr.getCodPsp());
		input.setDenominazioneAttestante(fr.getRagioneSocialePsp());
		input.setIdentificativoAttestante(fr.getCodPsp());
		input.setImportoTotalePagato(pagamento.getImportoPagato());
		input.setVersione(VersioneRPT.SANP_240);
		input.setEsitoPagamento(EsitoPagamento.PAGAMENTO_ESEGUITO);

		return input;
	}
	
	public it.govpay.bd.model.Rpt creaRPTFromVersamento(Versamento versamento, BDConfigWrapper configWrapper) throws ServiceException{
		VersamentiBD versamentiBD = null;
		SingoloVersamento singoloVersamento = null;
		Pagamento pagamento = null;
		Rendicontazione rendicontazione = null;
		Fr fr = null;
		try {
			versamentiBD = new VersamentiBD(configWrapper);
			
			versamentiBD.setupConnection(configWrapper.getTransactionID());
			
			versamentiBD.setAtomica(false);
			
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(versamentiBD);
			// in questo caso avremmo solo 1 singolo versamento
			singoloVersamento = singoliVersamenti.get(0);
			List<Pagamento> pagamenti = singoloVersamento.getPagamenti(versamentiBD);
			pagamento = pagamenti.get(0);
			List<Rendicontazione> rendicontazioni = pagamento.getRendicontazioni(versamentiBD);
			rendicontazione = rendicontazioni.get(0);
			fr = rendicontazione.getFr(versamentiBD);
			
			return this.creaRPTFromRendicontazione(rendicontazione, pagamento, singoloVersamento, versamento, fr, configWrapper);
		} finally {
			versamentiBD.closeConnection();
		}
	}
}
