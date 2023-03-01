package it.govpay.core.utils;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDominio;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivoco;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaFG;
import it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.StAutenticazioneSoggetto;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivoco;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersFG;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoVersamento;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtEntityUniqueIdentifier;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPAV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceipt;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtSubject;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPAReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Response;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.gov.pagopa.pagopa_api.pa.pafornode.StEntityUniqueIdentifierType;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.model.Rpt.FirmaRichiesta;

public class MessaggiPagoPAUtils {

	
	public static CtRichiestaPagamentoTelematico toCtRichiestaPagamentoTelematico(PaGetPaymentRes paGetPaymentRes, Rpt rpt) throws ServiceException {
		CtRichiestaPagamentoTelematico ctRpt = new CtRichiestaPagamentoTelematico();
		ctRpt.setVersioneOggetto(it.govpay.model.Rpt.VERSIONE_620);
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		Versamento versamento = rpt.getVersamento(configWrapper);
		Dominio dominio = versamento.getDominio(configWrapper); 
		UnitaOperativa uo = versamento.getUo(configWrapper);

		CtPaymentPA data = paGetPaymentRes.getData();
		
		CtDominio ctDominio = new CtDominio();
		ctDominio.setIdentificativoDominio(rpt.getCodDominio());
		ctDominio.setIdentificativoStazioneRichiedente(dominio.getStazione().getCodStazione());
		ctRpt.setDominio(ctDominio);
		ctRpt.setIdentificativoMessaggioRichiesta(rpt.getCodMsgRichiesta());
		ctRpt.setDataOraMessaggioRichiesta(rpt.getDataMsgRichiesta());
		ctRpt.setAutenticazioneSoggetto(StAutenticazioneSoggetto.N_A); // Informazione non presente nella V2 ma campo obbligatorio nella V1
		// ctRpt.setSoggettoVersante(RptBuilder.buildSoggettoVersante(versante)) Versante??
		ctRpt.setSoggettoPagatore(toCtSoggettoPagatore(data.getDebtor()));
		ctRpt.setEnteBeneficiario(RptBuilder.buildEnteBeneficiario(dominio, uo));
		
		CtDatiVersamentoRPT datiVersamento = new CtDatiVersamentoRPT();
		datiVersamento.setDataEsecuzionePagamento(rpt.getDataMsgRichiesta());
		datiVersamento.setImportoTotaleDaVersare(versamento.getImportoTotale());
		datiVersamento.setTipoVersamento(StTipoVersamento.fromValue(rpt.getTipoVersamento().getCodifica()));
		datiVersamento.setIdentificativoUnivocoVersamento(rpt.getIuv());
		//datiVersamento.setCodiceContestoPagamento(rpt.getCcp());
		datiVersamento.setFirmaRicevuta(FirmaRichiesta.NESSUNA.getCodifica());
//		datiVersamento.setIbanAddebito(ibanAddebito != null ? ibanAddebito : null);
//		datiVersamento.setBicAddebito(bicAddebito != null ? bicAddebito : null);
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
		for (SingoloVersamento singoloVersamento : singoliVersamenti) {
			datiVersamento.getDatiSingoloVersamento().add(RptBuilder.buildDatiSingoloVersamento(rpt, singoloVersamento, configWrapper));
		}
		ctRpt.setDatiVersamento(datiVersamento);
		
		return ctRpt;
	}
	
	public static CtRichiestaPagamentoTelematico toCtRichiestaPagamentoTelematico(PaGetPaymentV2Response paGetPaymentV2Response, Rpt rpt) throws ServiceException {
		CtRichiestaPagamentoTelematico ctRpt = new CtRichiestaPagamentoTelematico();
		ctRpt.setVersioneOggetto(it.govpay.model.Rpt.VERSIONE_620);
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		Versamento versamento = rpt.getVersamento(configWrapper);
		Dominio dominio = versamento.getDominio(configWrapper); 
		UnitaOperativa uo = versamento.getUo(configWrapper);

		CtPaymentPAV2 data = paGetPaymentV2Response.getData();
		
		CtDominio ctDominio = new CtDominio();
		ctDominio.setIdentificativoDominio(rpt.getCodDominio());
		ctDominio.setIdentificativoStazioneRichiedente(dominio.getStazione().getCodStazione());
		ctRpt.setDominio(ctDominio);
		ctRpt.setIdentificativoMessaggioRichiesta(rpt.getCodMsgRichiesta());
		ctRpt.setDataOraMessaggioRichiesta(rpt.getDataMsgRichiesta());
		ctRpt.setAutenticazioneSoggetto(StAutenticazioneSoggetto.N_A); // Informazione non presente nella V2 ma campo obbligatorio nella V1
		// ctRpt.setSoggettoVersante(RptBuilder.buildSoggettoVersante(versante)) Versante??
		ctRpt.setSoggettoPagatore(toCtSoggettoPagatore(data.getDebtor()));
		ctRpt.setEnteBeneficiario(RptBuilder.buildEnteBeneficiario(dominio, uo));
		
		CtDatiVersamentoRPT datiVersamento = new CtDatiVersamentoRPT();
		datiVersamento.setDataEsecuzionePagamento(rpt.getDataMsgRichiesta());
		datiVersamento.setImportoTotaleDaVersare(versamento.getImportoTotale());
		datiVersamento.setTipoVersamento(StTipoVersamento.fromValue(rpt.getTipoVersamento().getCodifica()));
		datiVersamento.setIdentificativoUnivocoVersamento(rpt.getIuv());
		//datiVersamento.setCodiceContestoPagamento(rpt.getCcp());
		datiVersamento.setFirmaRicevuta(FirmaRichiesta.NESSUNA.getCodifica());
//		datiVersamento.setIbanAddebito(ibanAddebito != null ? ibanAddebito : null);
//		datiVersamento.setBicAddebito(bicAddebito != null ? bicAddebito : null);
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
		for (SingoloVersamento singoloVersamento : singoliVersamenti) {
			datiVersamento.getDatiSingoloVersamento().add(RptBuilder.buildDatiSingoloVersamento(rpt, singoloVersamento, configWrapper));
		}
		ctRpt.setDatiVersamento(datiVersamento);
		
		return ctRpt;
	}


	public static CtRicevutaTelematica toCtRicevutaTelematica(PaSendRTV2Request paSendRTReq, Rpt rpt) throws ServiceException {
		CtRicevutaTelematica ctRt = new CtRicevutaTelematica();
		
		CtReceiptV2 receipt = paSendRTReq.getReceipt();
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		Versamento versamento = rpt.getVersamento(configWrapper);
		Dominio dominio = versamento.getDominio(configWrapper); 
		UnitaOperativa uo = versamento.getUo(configWrapper);
		
		ctRt.setDataOraMessaggioRicevuta(rpt.getDataMsgRichiesta());
		CtDatiVersamentoRT ctDatiPagamento = new CtDatiVersamentoRT();
		ctDatiPagamento.setCodiceContestoPagamento(receipt.getReceiptId());
		if(rpt.getEsitoPagamento() != null) {
			ctDatiPagamento.setCodiceEsitoPagamento(rpt.getEsitoPagamento().getCodifica() + "");
		}
		ctDatiPagamento.setIdentificativoUnivocoVersamento(receipt.getCreditorReferenceId());
		ctDatiPagamento.setImportoTotalePagato(rpt.getImportoTotalePagato());
		
		List<CtTransferPAReceiptV2> datiSingoliPagamenti = receipt.getTransferList().getTransfer();
		
		// singole voci
		for(int indice = 0; indice < datiSingoliPagamenti.size(); indice++) {
			CtTransferPAReceiptV2 ctTransferPA = datiSingoliPagamenti.get(indice);
			
			CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = new CtDatiSingoloPagamentoRT();
//			ctDatiSingoloPagamentoRT.setAllegatoRicevuta(null);  // non presente
			ctDatiSingoloPagamentoRT.setCausaleVersamento(ctTransferPA.getRemittanceInformation());
//			ctDatiSingoloPagamentoRT.setCommissioniApplicatePA(null);
			ctDatiSingoloPagamentoRT.setCommissioniApplicatePSP(receipt.getFee());
			ctDatiSingoloPagamentoRT.setDataEsitoSingoloPagamento(receipt.getPaymentDateTime());
			ctDatiSingoloPagamentoRT.setDatiSpecificiRiscossione(ctTransferPA.getTransferCategory());
			ctDatiSingoloPagamentoRT.setEsitoSingoloPagamento(receipt.getOutcome().toString()); 
			ctDatiSingoloPagamentoRT.setIdentificativoUnivocoRiscossione(receipt.getReceiptId());
			ctDatiSingoloPagamentoRT.setSingoloImportoPagato(ctTransferPA.getTransferAmount());
			
			ctDatiPagamento.getDatiSingoloPagamento().add(ctDatiSingoloPagamentoRT );
		}
		
		ctRt.setDatiPagamento(ctDatiPagamento );
		CtDominio ctDominio = new CtDominio();
		ctDominio.setIdentificativoDominio(rpt.getCodDominio());
		ctDominio.setIdentificativoStazioneRichiedente(dominio.getStazione().getCodStazione());
		ctRt.setDominio(ctDominio);
		ctRt.setEnteBeneficiario(RptBuilder.buildEnteBeneficiario(dominio, uo));
		ctRt.setIdentificativoMessaggioRicevuta(rpt.getCodMsgRicevuta());
		CtIstitutoAttestante ctIstitutoAttestante = new CtIstitutoAttestante();
		CtIdentificativoUnivoco ctIdentificativoUnivocoIstitutoAttestante = new CtIdentificativoUnivoco();
		ctIdentificativoUnivocoIstitutoAttestante.setCodiceIdentificativoUnivoco(rpt.getIdentificativoAttestante());
		if(rpt.getTipoIdentificativoAttestante() != null) {
			switch (rpt.getTipoIdentificativoAttestante()) {
			case A:
				ctIdentificativoUnivocoIstitutoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.A);
				break;
			case B:
				ctIdentificativoUnivocoIstitutoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.B);
				break;
			case G:
			default:
				ctIdentificativoUnivocoIstitutoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.G);	
				break;
			}
		}
		ctIstitutoAttestante.setIdentificativoUnivocoAttestante(ctIdentificativoUnivocoIstitutoAttestante );
		ctIstitutoAttestante.setDenominazioneAttestante(rpt.getDenominazioneAttestante());
		
		ctRt.setIstitutoAttestante(ctIstitutoAttestante );
		ctRt.setRiferimentoDataRichiesta(rpt.getDataMsgRicevuta());
		ctRt.setRiferimentoMessaggioRichiesta(rpt.getCodMsgRichiesta());
		ctRt.setSoggettoPagatore(toCtSoggettoPagatore(receipt.getDebtor()));
		ctRt.setSoggettoVersante(null); // informazione non presente
		ctRt.setVersioneOggetto(it.govpay.model.Rpt.VERSIONE_620);
		
		return ctRt;
	}
	
	public static CtRicevutaTelematica toCtRicevutaTelematica(PaSendRTReq paSendRTReq, Rpt rpt) throws ServiceException {
		CtRicevutaTelematica ctRt = new CtRicevutaTelematica();
		
		CtReceipt receipt = paSendRTReq.getReceipt();
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		Versamento versamento = rpt.getVersamento(configWrapper);
		Dominio dominio = versamento.getDominio(configWrapper); 
		UnitaOperativa uo = versamento.getUo(configWrapper);
		
		ctRt.setDataOraMessaggioRicevuta(rpt.getDataMsgRichiesta());
		CtDatiVersamentoRT ctDatiPagamento = new CtDatiVersamentoRT();
		ctDatiPagamento.setCodiceContestoPagamento(receipt.getReceiptId());
		if(rpt.getEsitoPagamento() != null) {
			ctDatiPagamento.setCodiceEsitoPagamento(rpt.getEsitoPagamento().getCodifica() + "");
		}
		ctDatiPagamento.setIdentificativoUnivocoVersamento(receipt.getCreditorReferenceId());
		ctDatiPagamento.setImportoTotalePagato(rpt.getImportoTotalePagato());
		
		List<CtTransferPA> datiSingoliPagamenti = receipt.getTransferList().getTransfer();
		
		// singole voci
		for(int indice = 0; indice < datiSingoliPagamenti.size(); indice++) {
			CtTransferPA ctTransferPA = datiSingoliPagamenti.get(indice);
			
			CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = new CtDatiSingoloPagamentoRT();
//			ctDatiSingoloPagamentoRT.setAllegatoRicevuta(null);  // non presente
			ctDatiSingoloPagamentoRT.setCausaleVersamento(ctTransferPA.getRemittanceInformation());
//			ctDatiSingoloPagamentoRT.setCommissioniApplicatePA(null);
			ctDatiSingoloPagamentoRT.setCommissioniApplicatePSP(receipt.getFee());
			ctDatiSingoloPagamentoRT.setDataEsitoSingoloPagamento(receipt.getPaymentDateTime());
			ctDatiSingoloPagamentoRT.setDatiSpecificiRiscossione(ctTransferPA.getTransferCategory());
			ctDatiSingoloPagamentoRT.setEsitoSingoloPagamento(receipt.getOutcome().toString()); 
			ctDatiSingoloPagamentoRT.setIdentificativoUnivocoRiscossione(receipt.getReceiptId());
			ctDatiSingoloPagamentoRT.setSingoloImportoPagato(ctTransferPA.getTransferAmount());
			
			ctDatiPagamento.getDatiSingoloPagamento().add(ctDatiSingoloPagamentoRT );
		}
		
		ctRt.setDatiPagamento(ctDatiPagamento );
		CtDominio ctDominio = new CtDominio();
		ctDominio.setIdentificativoDominio(rpt.getCodDominio());
		ctDominio.setIdentificativoStazioneRichiedente(dominio.getStazione().getCodStazione());
		ctRt.setDominio(ctDominio);
		ctRt.setEnteBeneficiario(RptBuilder.buildEnteBeneficiario(dominio, uo));
		ctRt.setIdentificativoMessaggioRicevuta(rpt.getCodMsgRicevuta());
		CtIstitutoAttestante ctIstitutoAttestante = new CtIstitutoAttestante();
		CtIdentificativoUnivoco ctIdentificativoUnivocoIstitutoAttestante = new CtIdentificativoUnivoco();
		ctIdentificativoUnivocoIstitutoAttestante.setCodiceIdentificativoUnivoco(rpt.getIdentificativoAttestante());
		if(rpt.getTipoIdentificativoAttestante() != null) {
			switch (rpt.getTipoIdentificativoAttestante()) {
			case A:
				ctIdentificativoUnivocoIstitutoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.A);
				break;
			case B:
				ctIdentificativoUnivocoIstitutoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.B);
				break;
			case G:
			default:
				ctIdentificativoUnivocoIstitutoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.G);	
				break;
			}
		}
		ctIstitutoAttestante.setIdentificativoUnivocoAttestante(ctIdentificativoUnivocoIstitutoAttestante );
		ctIstitutoAttestante.setDenominazioneAttestante(rpt.getDenominazioneAttestante());
		
		ctRt.setIstitutoAttestante(ctIstitutoAttestante );
		ctRt.setRiferimentoDataRichiesta(rpt.getDataMsgRicevuta());
		ctRt.setRiferimentoMessaggioRichiesta(rpt.getCodMsgRichiesta());
		ctRt.setSoggettoPagatore(toCtSoggettoPagatore(receipt.getDebtor()));
		ctRt.setSoggettoVersante(null); // informazione non presente
		ctRt.setVersioneOggetto(it.govpay.model.Rpt.VERSIONE_620);
		
		return ctRt;
	}


	private static CtSoggettoPagatore toCtSoggettoPagatore(CtSubject debitore) {
		if(debitore == null) return null;
		
		CtSoggettoPagatore soggettoDebitore = new CtSoggettoPagatore();
		CtIdentificativoUnivocoPersonaFG idUnivocoDebitore = new CtIdentificativoUnivocoPersonaFG();
		CtEntityUniqueIdentifier ctEntityUniqueIdentifier = debitore.getUniqueIdentifier();
		String cFiscale = ctEntityUniqueIdentifier.getEntityUniqueIdentifierValue();
		idUnivocoDebitore.setCodiceIdentificativoUnivoco(cFiscale);
		idUnivocoDebitore.setTipoIdentificativoUnivoco((ctEntityUniqueIdentifier.getEntityUniqueIdentifierType().compareTo(StEntityUniqueIdentifierType.F) == 0) ? StTipoIdentificativoUnivocoPersFG.F : StTipoIdentificativoUnivocoPersFG.G);
		soggettoDebitore.setAnagraficaPagatore(debitore.getFullName());
		soggettoDebitore.setCapPagatore(RptBuilder.getNotEmpty(debitore.getPostalCode()));
		soggettoDebitore.setCivicoPagatore(RptBuilder.getNotEmpty(debitore.getCivicNumber()));
		soggettoDebitore.setEMailPagatore(RptBuilder.getNotEmpty(debitore.getEMail()));
		soggettoDebitore.setIdentificativoUnivocoPagatore(idUnivocoDebitore);
		soggettoDebitore.setIndirizzoPagatore(RptBuilder.getNotEmpty(debitore.getStreetName()));
		soggettoDebitore.setLocalitaPagatore(RptBuilder.getNotEmpty(debitore.getCity()));
		soggettoDebitore.setNazionePagatore(RptBuilder.getNotEmpty(debitore.getCountry()));
		soggettoDebitore.setProvinciaPagatore(RptBuilder.getNotEmpty(debitore.getStateProvinceRegion()));
		return soggettoDebitore;
	}
}
