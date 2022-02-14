package it.govpay.core.business;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceipt;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtSubject;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferListPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.StOutcome;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Versamento;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.RicevutaPagamento;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.stampe.model.RicevutaTelematicaInput;
import it.govpay.stampe.model.RicevutaTelematicaInput.ElencoVoci;
import it.govpay.stampe.model.VoceRicevutaTelematicaInput;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoCostanti;
import it.govpay.stampe.pdf.rt.RicevutaTelematicaCostanti;
import it.govpay.stampe.pdf.rt.RicevutaTelematicaPdf;
import it.govpay.stampe.pdf.rt.utils.RicevutaTelematicaProperties;

public class RicevutaTelematica {

	private static Logger log = LoggerWrapperFactory.getLogger(RicevutaTelematica.class);
	private SimpleDateFormat sdfDataPagamento = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public RicevutaTelematica() {
	}


	public LeggiRicevutaDTOResponse creaPdfRicevuta(LeggiRicevutaDTO leggiRicevutaDTO,it.govpay.bd.model.Rpt rpt) throws ServiceException{
		LeggiRicevutaDTOResponse response = new LeggiRicevutaDTOResponse();

		try {
			RicevutaPagamento ricevuta = new RicevutaPagamento();
			ricevuta.setCodDominio(leggiRicevutaDTO.getIdDominio());
			ricevuta.setIuv(leggiRicevutaDTO.getIuv());
			ricevuta.setCcp(leggiRicevutaDTO.getCcp());

			RicevutaTelematicaProperties rtProperties = RicevutaTelematicaProperties.getInstance();
			RicevutaTelematicaInput input = this.fromRpt(rpt, leggiRicevutaDTO.isVisualizzaSoggettoDebitore());
			ricevuta = RicevutaTelematicaPdf.getInstance().creaRicevuta(log, input, ricevuta, rtProperties );
			response.setPdf(ricevuta.getPdf()); 
		}catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException(e);
		}
		return response;
	}

	public RicevutaTelematicaInput fromRpt(it.govpay.bd.model.Rpt rpt, boolean visualizzaSoggettoDebitore) throws Exception{
		switch (rpt.getVersione()) {
		case SANP_230:
			return this._fromRpt(rpt, visualizzaSoggettoDebitore);
		case SANP_240:
			return this._fromRptVersione240(rpt, visualizzaSoggettoDebitore);
		}
		
		return this._fromRpt(rpt, visualizzaSoggettoDebitore);
	}
	
	public RicevutaTelematicaInput _fromRpt(it.govpay.bd.model.Rpt rpt, boolean visualizzaSoggettoDebitore) throws Exception{
		RicevutaTelematicaInput input = new RicevutaTelematicaInput();

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


		input.setElencoVoci(this.getElencoVoci(rt,datiSingoloPagamento,input,rpt.getDataMsgRichiesta()));
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
		if(visualizzaSoggettoDebitore && soggettoPagatore != null) {
			this.impostaIndirizzoSoggettoPagatore(input, soggettoPagatore);
		}

		return input;
	}

	private ElencoVoci getElencoVoci(CtRicevutaTelematica rt, List<CtDatiSingoloPagamentoRT> datiSingoloPagamento, RicevutaTelematicaInput input, Date dataRpt) {
		ElencoVoci elencoVoci = new ElencoVoci();

		for (CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT : datiSingoloPagamento) {
			VoceRicevutaTelematicaInput voce = new VoceRicevutaTelematicaInput();

			voce.setDescrizione(ctDatiSingoloPagamentoRT.getCausaleVersamento());
			voce.setIdRiscossione(ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione());
			voce.setImporto(ctDatiSingoloPagamentoRT.getSingoloImportoPagato().doubleValue());
			voce.setStato(ctDatiSingoloPagamentoRT.getSingoloImportoPagato().compareTo(BigDecimal.ZERO) == 0 ? RicevutaTelematicaCostanti.PAGAMENTO_NON_ESEGUITO : RicevutaTelematicaCostanti.PAGAMENTO_ESEGUITO);

			elencoVoci.getVoce().add(voce);
			
		}
		input.setData( this.sdfDataPagamento.format(dataRpt));

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

	private void impostaIndirizzoEnteCreditore(it.govpay.bd.model.Dominio dominio, RicevutaTelematicaInput input) throws ServiceException {
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

			if(indirizzoEnte.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setIndirizzoEnte(indirizzoEnte);
			}else {
				input.setIndirizzoEnte(indirizzoEnte);
			}

			if(capCitta.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setLuogoEnte(capCitta);
			}else {
				input.setLuogoEnte(capCitta);
			}
		}
	}

	private void impostaIndirizzoSoggettoPagatore(RicevutaTelematicaInput input, CtSoggettoPagatore soggettoPagatore) throws ServiceException {
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


			if(indirizzoEnte.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setIndirizzoSoggetto(indirizzoEnte);
			}else {
				input.setIndirizzoSoggetto(indirizzoEnte);
			}

			if(capCitta.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setLuogoSoggetto(capCitta);
			}else {
				input.setLuogoSoggetto(capCitta);
			}

			input.setSoggetto(StringUtils.isNotEmpty(soggettoPagatore.getAnagraficaPagatore()) ? soggettoPagatore.getAnagraficaPagatore() : "");
			if(soggettoPagatore.getIdentificativoUnivocoPagatore() != null)
				input.setCfSoggetto(StringUtils.isNotEmpty(soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco()) ? soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco() : "");
		}
	}
	
	public RicevutaTelematicaInput _fromRptVersione240(it.govpay.bd.model.Rpt rpt, boolean visualizzaSoggettoDebitore) throws Exception{
		RicevutaTelematicaInput input = new RicevutaTelematicaInput();

		this.impostaAnagraficaEnteCreditore(rpt, input);
		Versamento versamento = rpt.getVersamento();

		PaSendRTReq rt = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);

		CtReceipt datiPagamento = rt.getReceipt();

		CtTransferListPA transferList = datiPagamento.getTransferList();
		List<CtTransferPA> datiSingoloPagamento = transferList.getTransfer();

		// input.setCCP(datiPagamento.getCodiceContestoPagamento()); non esiste
		input.setIUV(datiPagamento.getCreditorReferenceId());

		StringBuilder sbIstitutoAttestante = new StringBuilder();
		if(datiPagamento.getPspFiscalCode() != null){
			if(datiPagamento.getPSPCompanyName() != null){
				sbIstitutoAttestante.append(datiPagamento.getPSPCompanyName());
				sbIstitutoAttestante.append(", ");
			}
			sbIstitutoAttestante.append(datiPagamento.getPspFiscalCode());
		}
		input.setIstituto(sbIstitutoAttestante.toString());


		input.setElencoVoci(this.getElencoVoci(datiPagamento,datiSingoloPagamento,input,rpt.getDataMsgRichiesta()));
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
		if(visualizzaSoggettoDebitore && soggettoPagatore != null) {
			this.impostaIndirizzoSoggettoPagatore(input, soggettoPagatore);
		}

		return input;
	}
	
	private ElencoVoci getElencoVoci(CtReceipt rt, List<CtTransferPA> datiSingoloPagamento, RicevutaTelematicaInput input, Date dataRpt) {
		ElencoVoci elencoVoci = new ElencoVoci();

		for (CtTransferPA ctDatiSingoloPagamentoRT : datiSingoloPagamento) {
			VoceRicevutaTelematicaInput voce = new VoceRicevutaTelematicaInput();

			voce.setDescrizione(ctDatiSingoloPagamentoRT.getRemittanceInformation());
			voce.setIdRiscossione(rt.getReceiptId() + ctDatiSingoloPagamentoRT.getIdTransfer());
			voce.setImporto(ctDatiSingoloPagamentoRT.getTransferAmount().doubleValue());
			voce.setStato(ctDatiSingoloPagamentoRT.getTransferAmount().compareTo(BigDecimal.ZERO) == 0 ? RicevutaTelematicaCostanti.PAGAMENTO_NON_ESEGUITO : RicevutaTelematicaCostanti.PAGAMENTO_ESEGUITO);

			elencoVoci.getVoce().add(voce);
			
		}
		input.setData( this.sdfDataPagamento.format(dataRpt));

		return elencoVoci;
	}
	
	private void impostaIndirizzoSoggettoPagatore(RicevutaTelematicaInput input, CtSubject soggettoPagatore) throws ServiceException {
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


			if(indirizzoEnte.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setIndirizzoSoggetto(indirizzoEnte);
			}else {
				input.setIndirizzoSoggetto(indirizzoEnte);
			}

			if(capCitta.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setLuogoSoggetto(capCitta);
			}else {
				input.setLuogoSoggetto(capCitta);
			}

			input.setSoggetto(StringUtils.isNotEmpty(soggettoPagatore.getFullName()) ? soggettoPagatore.getFullName() : "");
			if(soggettoPagatore.getUniqueIdentifier() != null)
				input.setCfSoggetto(StringUtils.isNotEmpty(soggettoPagatore.getUniqueIdentifier().getEntityUniqueIdentifierValue()) ? soggettoPagatore.getUniqueIdentifier().getEntityUniqueIdentifierValue() : "");
		}
	}
}
