/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiMarcaBolloDigitale;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloVersamentoRPT;
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
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferListPAV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPAReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPAV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Response;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.gov.pagopa.pagopa_api.pa.pafornode.StEntityUniqueIdentifierType;
import it.gov.pagopa.pagopa_api.xsd.common_types.v1_0.CtRichiestaMarcaDaBollo;
import it.gov.pagopa.pagopa_api.xsd.common_types.v1_0.StOutcome;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.model.Rpt.FirmaRichiesta;
import it.govpay.model.SingoloVersamento.TipoBollo;

public class MessaggiPagoPAUtils {

	private MessaggiPagoPAUtils() {}
	
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
		datiVersamento.setImportoTotaleDaVersare(data.getPaymentAmount());
		datiVersamento.setTipoVersamento(StTipoVersamento.fromValue(rpt.getTipoVersamento().getCodifica()));
		datiVersamento.setIdentificativoUnivocoVersamento(rpt.getIuv());
		//datiVersamento.setCodiceContestoPagamento(rpt.getCcp());
		datiVersamento.setFirmaRicevuta(FirmaRichiesta.NESSUNA.getCodifica());
//		datiVersamento.setIbanAddebito(ibanAddebito != null ? ibanAddebito : null);
//		datiVersamento.setBicAddebito(bicAddebito != null ? bicAddebito : null);
		toDatiSingoloVersamento(data, datiVersamento);
		
		ctRpt.setDatiVersamento(datiVersamento);
		
		return ctRpt;
	}

	private static void toDatiSingoloVersamento(CtPaymentPA data, CtDatiVersamentoRPT datiVersamento) {
		for (CtTransferPA ctTransferPA : data.getTransferList().getTransfer()) {
			CtDatiSingoloVersamentoRPT ctDatiSingoloVersamentoRPT = new CtDatiSingoloVersamentoRPT();
			
			ctDatiSingoloVersamentoRPT.setCausaleVersamento(ctTransferPA.getRemittanceInformation());
			ctDatiSingoloVersamentoRPT.setDatiSpecificiRiscossione(ctTransferPA.getTransferCategory());
			ctDatiSingoloVersamentoRPT.setIbanAccredito(ctTransferPA.getIBAN());
			ctDatiSingoloVersamentoRPT.setImportoSingoloVersamento(ctTransferPA.getTransferAmount());
			ctDatiSingoloVersamentoRPT.setIbanAppoggio(null);
			ctDatiSingoloVersamentoRPT.setCommissioneCaricoPA(null);
			ctDatiSingoloVersamentoRPT.setCredenzialiPagatore(null);
			ctDatiSingoloVersamentoRPT.setDatiMarcaBolloDigitale(null);
			ctDatiSingoloVersamentoRPT.setBicAccredito(null);
			ctDatiSingoloVersamentoRPT.setBicAppoggio(null);
			
			datiVersamento.getDatiSingoloVersamento().add(ctDatiSingoloVersamentoRPT);
		}
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
		datiVersamento.setImportoTotaleDaVersare(data.getPaymentAmount());
		datiVersamento.setTipoVersamento(StTipoVersamento.fromValue(rpt.getTipoVersamento().getCodifica()));
		datiVersamento.setIdentificativoUnivocoVersamento(rpt.getIuv());
		//datiVersamento.setCodiceContestoPagamento(rpt.getCcp());
		datiVersamento.setFirmaRicevuta(FirmaRichiesta.NESSUNA.getCodifica());
//		datiVersamento.setIbanAddebito(ibanAddebito != null ? ibanAddebito : null);
//		datiVersamento.setBicAddebito(bicAddebito != null ? bicAddebito : null);
		toDatiSingoloVersamentoV2(data, datiVersamento);
		ctRpt.setDatiVersamento(datiVersamento);
		
		return ctRpt;
	}
	
	private static void toDatiSingoloVersamentoV2(CtPaymentPAV2 data, CtDatiVersamentoRPT datiVersamento) {
		for (CtTransferPAV2 ctTransferPA : data.getTransferList().getTransfer()) {
			CtDatiSingoloVersamentoRPT ctDatiSingoloVersamentoRPT = new CtDatiSingoloVersamentoRPT();
			
			ctDatiSingoloVersamentoRPT.setCausaleVersamento(ctTransferPA.getRemittanceInformation());
			ctDatiSingoloVersamentoRPT.setDatiSpecificiRiscossione(ctTransferPA.getTransferCategory());
			ctDatiSingoloVersamentoRPT.setIbanAccredito(ctTransferPA.getIBAN());
			ctDatiSingoloVersamentoRPT.setImportoSingoloVersamento(ctTransferPA.getTransferAmount());
			
			if(ctTransferPA.getRichiestaMarcaDaBollo() != null) {
				CtDatiMarcaBolloDigitale datiMarcaBolloDigitale = new CtDatiMarcaBolloDigitale();
				
				datiMarcaBolloDigitale.setHashDocumento(new String(ctTransferPA.getRichiestaMarcaDaBollo().getHashDocumento()));
				datiMarcaBolloDigitale.setProvinciaResidenza(ctTransferPA.getRichiestaMarcaDaBollo().getProvinciaResidenza());
				datiMarcaBolloDigitale.setTipoBollo(ctTransferPA.getRichiestaMarcaDaBollo().getTipoBollo());
				
				ctDatiSingoloVersamentoRPT.setDatiMarcaBolloDigitale(datiMarcaBolloDigitale );
			}
			
			ctDatiSingoloVersamentoRPT.setIbanAppoggio(null);
			ctDatiSingoloVersamentoRPT.setCommissioneCaricoPA(null);
			ctDatiSingoloVersamentoRPT.setCredenzialiPagatore(null);
			ctDatiSingoloVersamentoRPT.setBicAccredito(null);
			ctDatiSingoloVersamentoRPT.setBicAppoggio(null);
			
			datiVersamento.getDatiSingoloVersamento().add(ctDatiSingoloVersamentoRPT);
		}
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
		StOutcome ctReceiptOutcome = receipt.getOutcome();
		it.govpay.model.Rpt.EsitoPagamento rptEsito = ctReceiptOutcome.equals(StOutcome.OK) ? it.govpay.model.Rpt.EsitoPagamento.PAGAMENTO_ESEGUITO : it.govpay.model.Rpt.EsitoPagamento.PAGAMENTO_NON_ESEGUITO; 
		ctDatiPagamento.setCodiceEsitoPagamento(rptEsito.getCodifica() + "");
		ctDatiPagamento.setIdentificativoUnivocoVersamento(receipt.getCreditorReferenceId());
		ctDatiPagamento.setImportoTotalePagato(receipt.getPaymentAmount());
		
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
		ctDominio.setIdentificativoDominio(receipt.getFiscalCode());
		ctDominio.setIdentificativoStazioneRichiedente(dominio.getStazione().getCodStazione());
		ctRt.setDominio(ctDominio);
		ctRt.setEnteBeneficiario(RptBuilder.buildEnteBeneficiario(dominio, uo));
		ctRt.setIdentificativoMessaggioRicevuta(receipt.getReceiptId());
		CtIstitutoAttestante ctIstitutoAttestante = new CtIstitutoAttestante();
		CtIdentificativoUnivoco ctIdentificativoUnivocoIstitutoAttestante = new CtIdentificativoUnivoco();
		ctIdentificativoUnivocoIstitutoAttestante.setCodiceIdentificativoUnivoco(receipt.getIdPSP());
		ctIdentificativoUnivocoIstitutoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.A);
		ctIstitutoAttestante.setIdentificativoUnivocoAttestante(ctIdentificativoUnivocoIstitutoAttestante );
		ctIstitutoAttestante.setDenominazioneAttestante(receipt.getPSPCompanyName());
		
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
		StOutcome ctReceiptOutcome = receipt.getOutcome();
		it.govpay.model.Rpt.EsitoPagamento rptEsito = ctReceiptOutcome.equals(StOutcome.OK) ? it.govpay.model.Rpt.EsitoPagamento.PAGAMENTO_ESEGUITO : it.govpay.model.Rpt.EsitoPagamento.PAGAMENTO_NON_ESEGUITO; 
		ctDatiPagamento.setCodiceEsitoPagamento(rptEsito.getCodifica() + "");
		ctDatiPagamento.setIdentificativoUnivocoVersamento(receipt.getCreditorReferenceId());
		ctDatiPagamento.setImportoTotalePagato(receipt.getPaymentAmount());
		
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
		ctDominio.setIdentificativoDominio(receipt.getFiscalCode());
		ctDominio.setIdentificativoStazioneRichiedente(dominio.getStazione().getCodStazione());
		ctRt.setDominio(ctDominio);
		ctRt.setEnteBeneficiario(RptBuilder.buildEnteBeneficiario(dominio, uo));
		ctRt.setIdentificativoMessaggioRicevuta(receipt.getReceiptId());
		
		CtIstitutoAttestante ctIstitutoAttestante = new CtIstitutoAttestante();
		CtIdentificativoUnivoco ctIdentificativoUnivocoIstitutoAttestante = new CtIdentificativoUnivoco();
		ctIdentificativoUnivocoIstitutoAttestante.setCodiceIdentificativoUnivoco(receipt.getIdPSP());
		ctIdentificativoUnivocoIstitutoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.A);
		ctIstitutoAttestante.setIdentificativoUnivocoAttestante(ctIdentificativoUnivocoIstitutoAttestante );
		ctIstitutoAttestante.setDenominazioneAttestante(receipt.getPSPCompanyName());
		
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
	
	public static PaGetPaymentRes ricostruisciPaGetPaymentRes(PaSendRTReq paSendRTReq, Rpt rpt, Versamento versamento) {
		if(paSendRTReq == null) return null;
		
		CtReceipt receipt = paSendRTReq.getReceipt();
		
		PaGetPaymentRes paGetPaymentRes = new PaGetPaymentRes();
		
		CtPaymentPA ctPaymentPA = new CtPaymentPA();
		
		ctPaymentPA.setCompanyName(receipt.getCompanyName());
		ctPaymentPA.setCreditorReferenceId(receipt.getCreditorReferenceId());
		ctPaymentPA.setDebtor(receipt.getDebtor());
		ctPaymentPA.setDescription(receipt.getDescription());
		ctPaymentPA.setDueDate(CtPaymentPABuilder.calcolaDueDate(versamento));
		ctPaymentPA.setLastPayment(null);
		ctPaymentPA.setMetadata(receipt.getMetadata());
		ctPaymentPA.setOfficeName(receipt.getOfficeName());
		ctPaymentPA.setPaymentAmount(receipt.getPaymentAmount());
		ctPaymentPA.setRetentionDate(null);
		ctPaymentPA.setTransferList(receipt.getTransferList());
		
		paGetPaymentRes.setData(ctPaymentPA );
		paGetPaymentRes.setOutcome(StOutcome.OK); 
		
		return paGetPaymentRes;
	}
	
	public static PaGetPaymentV2Response ricostruisciPaGetPaymentV2Response(PaSendRTV2Request paSendRTV2Request, Rpt rpt, Versamento versamento) throws ServiceException {
		if(paSendRTV2Request == null) return null;
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		CtReceiptV2 receipt = paSendRTV2Request.getReceipt();
		
		PaGetPaymentV2Response paGetPaymentV2Response = new PaGetPaymentV2Response();
		
		CtPaymentPAV2 ctPaymentPA = new CtPaymentPAV2();
		
		ctPaymentPA.setCompanyName(receipt.getCompanyName());
		ctPaymentPA.setCreditorReferenceId(receipt.getCreditorReferenceId());
		ctPaymentPA.setDebtor(receipt.getDebtor());
		ctPaymentPA.setDescription(receipt.getDescription());
		ctPaymentPA.setDueDate(CtPaymentPABuilder.calcolaDueDate(versamento));
		ctPaymentPA.setLastPayment(null);
		ctPaymentPA.setMetadata(receipt.getMetadata());
		ctPaymentPA.setOfficeName(receipt.getOfficeName());
		ctPaymentPA.setPaymentAmount(receipt.getPaymentAmount());
		ctPaymentPA.setRetentionDate(null);
		
		if(receipt.getTransferList() != null) {
			CtTransferListPAV2 ctTransferListPAV2 = new CtTransferListPAV2();
			
			List<CtTransferPAReceiptV2> datiSingoliPagamenti = receipt.getTransferList().getTransfer();
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
			
			for(int indice = 0; indice < datiSingoliPagamenti.size(); indice++) {
				CtTransferPAReceiptV2 ctTransferPAReceiptV2 = datiSingoliPagamenti.get(indice);
				CtTransferPAV2 ctTransferPAV2 = new CtTransferPAV2();
				
				ctTransferPAV2.setCompanyName(ctTransferPAReceiptV2.getCompanyName());
				ctTransferPAV2.setFiscalCodePA(ctTransferPAReceiptV2.getFiscalCodePA());
				ctTransferPAV2.setIBAN(ctTransferPAReceiptV2.getIBAN());
				ctTransferPAV2.setIdTransfer(ctTransferPAReceiptV2.getIdTransfer());
				ctTransferPAV2.setMetadata(ctTransferPAReceiptV2.getMetadata());
				ctTransferPAV2.setRemittanceInformation(ctTransferPAReceiptV2.getRemittanceInformation());
				if(ctTransferPAReceiptV2.getMBDAttachment() != null) {
					SingoloVersamento singoloVersamento = singoliVersamenti.get(indice);
					
					CtRichiestaMarcaDaBollo marcaBollo = new CtRichiestaMarcaDaBollo();
					if(singoloVersamento.getHashDocumento() != null)
						marcaBollo.setHashDocumento(singoloVersamento.getHashDocumento().getBytes());
					marcaBollo.setProvinciaResidenza(singoloVersamento.getProvinciaResidenza());
					if(singoloVersamento.getTipoBollo() != null)
						marcaBollo.setTipoBollo(singoloVersamento.getTipoBollo().getCodifica());
					else
						marcaBollo.setTipoBollo(TipoBollo.IMPOSTA_BOLLO.getCodifica());
					ctTransferPAV2.setRichiestaMarcaDaBollo(marcaBollo);
				}
				ctTransferPAV2.setTransferAmount(ctTransferPAReceiptV2.getTransferAmount());
				ctTransferPAV2.setTransferCategory(ctTransferPAReceiptV2.getTransferCategory());
				
				ctTransferListPAV2.getTransfer().add(ctTransferPAV2 );
			}
			
			
			ctPaymentPA.setTransferList(ctTransferListPAV2 );
		}
		
		
		paGetPaymentV2Response.setData(ctPaymentPA  );
		paGetPaymentV2Response.setOutcome(StOutcome.OK); 
		
		return paGetPaymentV2Response;
	}
}
