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

import java.io.UnsupportedEncodingException;
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
import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceipt;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtSubject;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferListPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferListPAReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPAReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.gov.pagopa.pagopa_api.xsd.common_types.v1_0.StOutcome;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Versamento;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.utils.DateUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.RicevutaPagamento;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.pagopa.beans.utils.JaxbUtils;
import it.govpay.stampe.model.RicevutaTelematicaInput;
import it.govpay.stampe.model.RicevutaTelematicaInput.ElencoVoci;
import it.govpay.stampe.model.VoceRicevutaTelematicaInput;
import it.govpay.stampe.pdf.rt.RicevutaTelematicaCostanti;
import it.govpay.stampe.pdf.rt.RicevutaTelematicaPdf;
import it.govpay.stampe.pdf.rt.utils.RicevutaTelematicaProperties;
import jakarta.xml.bind.JAXBException;

public class RicevutaTelematica {

	private static Logger log = LoggerWrapperFactory.getLogger(RicevutaTelematica.class);
	private SimpleDateFormat sdfSoloData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfDataOraMinuti = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public RicevutaTelematica() {
		//donothing
	}


	public LeggiRicevutaDTOResponse creaPdfRicevuta(LeggiRicevutaDTO leggiRicevutaDTO,it.govpay.bd.model.Rpt rpt) throws ServiceException{
		LeggiRicevutaDTOResponse response = new LeggiRicevutaDTOResponse();

		try {
			RicevutaPagamento ricevuta = new RicevutaPagamento();
			ricevuta.setCodDominio(leggiRicevutaDTO.getIdDominio());
			ricevuta.setIuv(leggiRicevutaDTO.getIuv());
			ricevuta.setCcp(leggiRicevutaDTO.getCcp());

			RicevutaTelematicaProperties rtProperties = RicevutaTelematicaProperties.getInstance();
			RicevutaTelematicaInput input = this.fromRpt(rpt);
			ricevuta = RicevutaTelematicaPdf.getInstance().creaRicevuta(log, input, ricevuta, rtProperties );
			response.setPdf(ricevuta.getPdf()); 
		}catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException(e);
		}
		return response;
	}

	public RicevutaTelematicaInput fromRpt(it.govpay.bd.model.Rpt rpt) throws JAXBException, SAXException, ServiceException, UnsupportedEncodingException, CodificaInesistenteException {
		switch (rpt.getVersione()) {
		case SANP_230:
			return this._fromRpt(rpt);
		case SANP_240:
		case RPTV2_RTV1:
			return this._fromRptVersione240(rpt);
		case SANP_321_V2:
		case RPTV1_RTV2:
		case RPTSANP230_RTV2:
			return this._fromRptVersione321_V2(rpt);
		}
		
		return this._fromRpt(rpt);
	}
	
	public RicevutaTelematicaInput _fromRpt(it.govpay.bd.model.Rpt rpt) throws JAXBException, SAXException, ServiceException, UnsupportedEncodingException, CodificaInesistenteException {
		RicevutaTelematicaInput input = new RicevutaTelematicaInput();
		input.setVersioneOggetto(rpt.getVersione().name());

		this.impostaAnagraficaEnteCreditore(rpt, input);
		Versamento versamento = rpt.getVersamento();

		CtRicevutaTelematica rt = JaxbUtils.toRT(rpt.getXmlRt(), false);

		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();

		List<CtDatiSingoloPagamentoRT> datiSingoloPagamento = datiPagamento.getDatiSingoloPagamento();

		input.setCCP(datiPagamento.getCodiceContestoPagamento());
		input.setIUV(datiPagamento.getIdentificativoUnivocoVersamento());

		StringBuilder sbIstitutoAttestante = new StringBuilder();
		if(rt.getIstitutoAttestante() != null){
			CtIstitutoAttestante istitutoAttestante = rt.getIstitutoAttestante();

			sbIstitutoAttestante.append(istitutoAttestante.getDenominazioneAttestante());
			sbIstitutoAttestante.append(", ");
			sbIstitutoAttestante.append(istitutoAttestante.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco());
		}
		input.setIstituto(sbIstitutoAttestante.toString());

		CtRichiestaPagamentoTelematico ctRichiestaPagamentoTelematico = JaxbUtils.toRPT(rpt.getXmlRpt(), false);

		input.setElencoVoci(this.getElencoVoci(rt,datiSingoloPagamento,input, DateUtils.toJavaDate(ctRichiestaPagamentoTelematico.getDataOraMessaggioRichiesta()), DateUtils.toJavaDate(rt.getDataOraMessaggioRicevuta())));
		input.setImporto(datiPagamento.getImportoTotalePagato().doubleValue());
		input.setOggettoDelPagamento(versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().getSimple() : "");

		EsitoPagamento esitoPagamento = EsitoPagamento.toEnum(datiPagamento.getCodiceEsitoPagamento());

		String stato = "";
		switch(esitoPagamento) {
		case DECORRENZA_TERMINI:
			stato = RicevutaTelematicaCostanti.DECORRENZA_TERMINI;
			break;
		case DECORRENZA_TERMINI_PARZIALE:
			stato = RicevutaTelematicaCostanti.DECORRENZA_TERMINI_PARZIALE;
			break;
		case PAGAMENTO_ESEGUITO:
			stato = RicevutaTelematicaCostanti.PAGAMENTO_ESEGUITO;
			break;
		case PAGAMENTO_NON_ESEGUITO:
			stato = RicevutaTelematicaCostanti.PAGAMENTO_NON_ESEGUITO;
			break;
		case PAGAMENTO_PARZIALMENTE_ESEGUITO:
			stato = RicevutaTelematicaCostanti.PAGAMENTO_PARZIALMENTE_ESEGUITO;
			break;
		default:
			break;

		}

		input.setStato(stato);


		CtSoggettoPagatore soggettoPagatore = rt.getSoggettoPagatore();
		if(soggettoPagatore != null) {
			this.impostaIndirizzoSoggettoPagatore(input, soggettoPagatore);
		}

		return input;
	}

	private ElencoVoci getElencoVoci(CtRicevutaTelematica rt, List<CtDatiSingoloPagamentoRT> datiSingoloPagamento, RicevutaTelematicaInput input, Date dataRpt, Date dataRt) {
		ElencoVoci elencoVoci = new ElencoVoci();

		for (CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT : datiSingoloPagamento) {
			VoceRicevutaTelematicaInput voce = new VoceRicevutaTelematicaInput();

			voce.setDescrizione(ctDatiSingoloPagamentoRT.getCausaleVersamento());
			voce.setIdRiscossione(ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione());
			voce.setImporto(ctDatiSingoloPagamentoRT.getSingoloImportoPagato().doubleValue());
			voce.setStato(ctDatiSingoloPagamentoRT.getSingoloImportoPagato().compareTo(BigDecimal.ZERO) == 0 ? RicevutaTelematicaCostanti.PAGAMENTO_NON_ESEGUITO : RicevutaTelematicaCostanti.PAGAMENTO_ESEGUITO);

			elencoVoci.getVoce().add(voce);
			
		}
		
		setDataOperazione(input, dataRpt);
		setDataApplicativa(input, dataRt);
		
		return elencoVoci;
	}


	private it.govpay.bd.model.Dominio impostaAnagraficaEnteCreditore(it.govpay.bd.model.Rpt rpt, RicevutaTelematicaInput input) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		it.govpay.bd.model.Dominio dominio = rpt.getDominio(configWrapper);
		String codDominio = dominio.getCodDominio();

		input.setEnteDenominazione(dominio.getRagioneSociale());
		input.setCfEnte(codDominio);
		// se e' presente un logo lo inserisco altrimemti verra' caricato il logo di default.
		if(dominio.getLogo() != null && dominio.getLogo().length > 0)
			input.setLogoEnte(new String(dominio.getLogo()));

		this.impostaIndirizzoEnteCreditore(dominio, input);
		return dominio;
	}

	private void impostaIndirizzoEnteCreditore(it.govpay.bd.model.Dominio dominio, RicevutaTelematicaInput input) {
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

	private void impostaIndirizzoSoggettoPagatore(RicevutaTelematicaInput input, CtSoggettoPagatore soggettoPagatore) {
		if(soggettoPagatore != null) {
			String indirizzo = StringUtils.isNotEmpty(soggettoPagatore.getIndirizzoPagatore()) ? soggettoPagatore.getIndirizzoPagatore() : "";
			String civico = StringUtils.isNotEmpty(soggettoPagatore.getCivicoPagatore()) ? soggettoPagatore.getCivicoPagatore() : "";
			String cap = StringUtils.isNotEmpty(soggettoPagatore.getCapPagatore()) ? soggettoPagatore.getCapPagatore() : "";
			String localita = StringUtils.isNotEmpty(soggettoPagatore.getLocalitaPagatore()) ? soggettoPagatore.getLocalitaPagatore() : "";
			String provincia = StringUtils.isNotEmpty(soggettoPagatore.getProvinciaPagatore()) ? (" (" +soggettoPagatore.getProvinciaPagatore() +")" ) : "";
			// Indirizzo piu' civico impostati se non e' vuoto l'indirizzo
			String indirizzoCivico = StringUtils.isNotEmpty(indirizzo) ? indirizzo + " " + civico : "";
			// capCittaProv impostati se e' valorizzata la localita'
			String capCitta = StringUtils.isNotEmpty(localita) ? (cap + " " + localita + provincia) : "";

			// Inserisco la virgola se la prima riga non e' vuota
			String indirizzoEnte = StringUtils.isNotEmpty(indirizzoCivico) ? indirizzoCivico + "," : "";

			input.setIndirizzoSoggetto(indirizzoEnte);

			input.setLuogoSoggetto(capCitta);

			input.setSoggetto(StringUtils.isNotEmpty(soggettoPagatore.getAnagraficaPagatore()) ? soggettoPagatore.getAnagraficaPagatore() : "");
			if(soggettoPagatore.getIdentificativoUnivocoPagatore() != null)
				input.setCfSoggetto(StringUtils.isNotEmpty(soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco()) ? soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco() : "");
		}
	}
	
	public RicevutaTelematicaInput _fromRptVersione240(it.govpay.bd.model.Rpt rpt) throws JAXBException, SAXException, ServiceException, UnsupportedEncodingException{
		RicevutaTelematicaInput input = new RicevutaTelematicaInput();
		input.setVersioneOggetto(rpt.getVersione().name());

		this.impostaAnagraficaEnteCreditore(rpt, input);
		Versamento versamento = rpt.getVersamento();

		PaSendRTReq rt = JaxbUtils.toPaSendRTReqRT(rpt.getXmlRt(), false);

		CtReceipt datiPagamento = rt.getReceipt();

		CtTransferListPA transferList = datiPagamento.getTransferList();
		List<CtTransferPA> datiSingoloPagamento = transferList.getTransfer();

		input.setCCP(datiPagamento.getReceiptId()); 
		input.setIUV(datiPagamento.getCreditorReferenceId());

		StringBuilder sbIstitutoAttestante = new StringBuilder();
		if(datiPagamento.getPSPCompanyName() != null){
			sbIstitutoAttestante.append(datiPagamento.getPSPCompanyName());
			
		}
		
		if(datiPagamento.getPspFiscalCode() != null){
			if(!sbIstitutoAttestante.isEmpty()) {
				sbIstitutoAttestante.append(", ");
			}
			sbIstitutoAttestante.append(datiPagamento.getPspFiscalCode());
		}
		
		input.setIstituto(sbIstitutoAttestante.toString());


		input.setElencoVoci(this.getElencoVoci(datiPagamento,datiSingoloPagamento,input,DateUtils.toJavaDate(datiPagamento.getPaymentDateTime()), DateUtils.toJavaDate(datiPagamento.getApplicationDate())));
		input.setImporto(datiPagamento.getPaymentAmount().doubleValue());
		input.setOggettoDelPagamento(versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().getSimple() : "");

		StOutcome esitoPagamento = datiPagamento.getOutcome();

		String stato = "";
		switch(esitoPagamento) {
		case KO:
			stato = RicevutaTelematicaCostanti.PAGAMENTO_NON_ESEGUITO;
			break;
		case OK:
			stato = RicevutaTelematicaCostanti.PAGAMENTO_ESEGUITO;
			break;

		}

		input.setStato(stato);


		CtSubject soggettoPagatore = datiPagamento.getDebtor();
		if(soggettoPagatore != null) {
			this.impostaIndirizzoSoggettoPagatore(input, soggettoPagatore);
		}

		return input;
	}
	
	private ElencoVoci getElencoVoci(CtReceipt rt, List<CtTransferPA> datiSingoloPagamento, RicevutaTelematicaInput input, Date dataRpt, Date dataRt) {
		ElencoVoci elencoVoci = new ElencoVoci();

		for (CtTransferPA ctDatiSingoloPagamentoRT : datiSingoloPagamento) {
			VoceRicevutaTelematicaInput voce = new VoceRicevutaTelematicaInput();

			voce.setDescrizione(ctDatiSingoloPagamentoRT.getRemittanceInformation());
			voce.setIdRiscossione(rt.getReceiptId());
			voce.setImporto(ctDatiSingoloPagamentoRT.getTransferAmount().doubleValue());
			voce.setStato(ctDatiSingoloPagamentoRT.getTransferAmount().compareTo(BigDecimal.ZERO) == 0 ? RicevutaTelematicaCostanti.PAGAMENTO_NON_ESEGUITO : RicevutaTelematicaCostanti.PAGAMENTO_ESEGUITO);

			elencoVoci.getVoce().add(voce);
			
		}
		
		setDataOperazione(input, dataRpt);
		setDataApplicativa(input, dataRt);

		return elencoVoci;
	}


	private void setDataApplicativa(RicevutaTelematicaInput input, Date dataRt) {
		if(dataRt != null) {
			input.setDataApplicativa( this.sdfSoloData.format(dataRt));
		} else {
			input.setDataApplicativa("--");
		}
	}


	private void setDataOperazione(RicevutaTelematicaInput input, Date dataRpt) {
		if(dataRpt != null) {
			Calendar cRpt = Calendar.getInstance();
			cRpt.setTime(dataRpt);
			if((cRpt.get(Calendar.HOUR_OF_DAY) + cRpt.get(Calendar.MINUTE) + cRpt.get(Calendar.SECOND)) == 0) {
				input.setDataOperazione( this.sdfSoloData.format(dataRpt));
			} else {
				input.setDataOperazione( this.sdfDataOraMinuti.format(dataRpt));
			}
		} else {
			input.setDataOperazione("--");
		}
	}
	
	private void impostaIndirizzoSoggettoPagatore(RicevutaTelematicaInput input, CtSubject soggettoPagatore) {
		if(soggettoPagatore != null) {
			String indirizzo = StringUtils.isNotEmpty(soggettoPagatore.getStreetName()) ? soggettoPagatore.getStreetName() : "";
			String civico = StringUtils.isNotEmpty(soggettoPagatore.getCivicNumber()) ? soggettoPagatore.getCivicNumber() : "";
			String cap = StringUtils.isNotEmpty(soggettoPagatore.getPostalCode()) ? soggettoPagatore.getPostalCode() : "";
			String localita = StringUtils.isNotEmpty(soggettoPagatore.getCity()) ? soggettoPagatore.getCity() : "";
			String provincia = StringUtils.isNotEmpty(soggettoPagatore.getStateProvinceRegion()) ? (" (" +soggettoPagatore.getStateProvinceRegion() +")" ) : "";
			// Indirizzo piu' civico impostati se non e' vuoto l'indirizzo
			String indirizzoCivico = StringUtils.isNotEmpty(indirizzo) ? indirizzo + " " + civico : "";
			// capCittaProv impostati se e' valorizzata la localita'
			String capCitta = StringUtils.isNotEmpty(localita) ? (cap + " " + localita + provincia) : "";

			// Inserisco la virgola se la prima riga non e' vuota
			String indirizzoEnte = StringUtils.isNotEmpty(indirizzoCivico) ? indirizzoCivico + "," : "";

			input.setIndirizzoSoggetto(indirizzoEnte);

			input.setLuogoSoggetto(capCitta);

			input.setSoggetto(StringUtils.isNotEmpty(soggettoPagatore.getFullName()) ? soggettoPagatore.getFullName() : "");
			if(soggettoPagatore.getUniqueIdentifier() != null)
				input.setCfSoggetto(StringUtils.isNotEmpty(soggettoPagatore.getUniqueIdentifier().getEntityUniqueIdentifierValue()) ? soggettoPagatore.getUniqueIdentifier().getEntityUniqueIdentifierValue() : "");
		}
	}
	
	public RicevutaTelematicaInput _fromRptVersione321_V2(it.govpay.bd.model.Rpt rpt) throws JAXBException, SAXException, ServiceException, UnsupportedEncodingException {
		RicevutaTelematicaInput input = new RicevutaTelematicaInput();
		input.setVersioneOggetto(rpt.getVersione().name());

		this.impostaAnagraficaEnteCreditore(rpt, input);
		Versamento versamento = rpt.getVersamento();

		PaSendRTV2Request rt = JaxbUtils.toPaSendRTV2RequestRT(rpt.getXmlRt(), false);

		CtReceiptV2 datiPagamento = rt.getReceipt();

		CtTransferListPAReceiptV2 transferList = datiPagamento.getTransferList();
		List<CtTransferPAReceiptV2> datiSingoloPagamento = transferList.getTransfer();

		input.setCCP(datiPagamento.getReceiptId()); 
		input.setIUV(datiPagamento.getCreditorReferenceId());

		StringBuilder sbIstitutoAttestante = new StringBuilder();
		if(datiPagamento.getPSPCompanyName() != null){
			sbIstitutoAttestante.append(datiPagamento.getPSPCompanyName());
			
		}
		
		if(datiPagamento.getPspFiscalCode() != null){
			if(!sbIstitutoAttestante.isEmpty()) {
				sbIstitutoAttestante.append(", ");
			}
			sbIstitutoAttestante.append(datiPagamento.getPspFiscalCode());
		}
		
		input.setIstituto(sbIstitutoAttestante.toString());


		input.setElencoVoci(this.getElencoVoci(datiPagamento,datiSingoloPagamento,input,DateUtils.toJavaDate(datiPagamento.getPaymentDateTime()), DateUtils.toJavaDate(datiPagamento.getApplicationDate())));
		input.setImporto(datiPagamento.getPaymentAmount().doubleValue());
		input.setOggettoDelPagamento(versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().getSimple() : "");

		StOutcome esitoPagamento = datiPagamento.getOutcome();

		String stato = "";
		switch(esitoPagamento) {
		case KO:
			stato = RicevutaTelematicaCostanti.PAGAMENTO_NON_ESEGUITO;
			break;
		case OK:
			stato = RicevutaTelematicaCostanti.PAGAMENTO_ESEGUITO;
			break;

		}

		input.setStato(stato);


		CtSubject soggettoPagatore = datiPagamento.getDebtor();
		if(soggettoPagatore != null) {
			this.impostaIndirizzoSoggettoPagatore(input, soggettoPagatore);
		}

		return input;
	}
	
	private ElencoVoci getElencoVoci(CtReceiptV2 rt, List<CtTransferPAReceiptV2> datiSingoloPagamento, RicevutaTelematicaInput input, Date dataRpt, Date dataRt) {
		ElencoVoci elencoVoci = new ElencoVoci();

		for (CtTransferPAReceiptV2 ctDatiSingoloPagamentoRT : datiSingoloPagamento) {
			VoceRicevutaTelematicaInput voce = new VoceRicevutaTelematicaInput();

			voce.setDescrizione(ctDatiSingoloPagamentoRT.getRemittanceInformation());
			voce.setIdRiscossione(rt.getReceiptId());
			voce.setImporto(ctDatiSingoloPagamentoRT.getTransferAmount().doubleValue());
			voce.setStato(ctDatiSingoloPagamentoRT.getTransferAmount().compareTo(BigDecimal.ZERO) == 0 ? RicevutaTelematicaCostanti.PAGAMENTO_NON_ESEGUITO : RicevutaTelematicaCostanti.PAGAMENTO_ESEGUITO);

			elencoVoci.getVoce().add(voce);
			
		}
		
		setDataOperazione(input, dataRpt);
		setDataApplicativa(input, dataRt);

		return elencoVoci;
	}
}
