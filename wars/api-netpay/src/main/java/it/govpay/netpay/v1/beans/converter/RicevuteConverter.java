package it.govpay.netpay.v1.beans.converter;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.netpay.v1.beans.GetRTReceipt;
import it.govpay.netpay.v1.beans.PaymentResultCode;
import it.govpay.netpay.v1.beans.PaymentType;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class RicevuteConverter {

	public static GetRTReceipt toRsModel(Rpt rpt) throws CodificaInesistenteException, ServiceException, UnsupportedEncodingException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		GetRTReceipt rsModel = new GetRTReceipt();
		
		rsModel.setPspId(rpt.getIdentificativoAttestante());
		rsModel.setChannelId(rpt.getCodCanale());

		switch (rpt.getTipoVersamento()) {
		case ADDEBITO_DIRETTO:
			rsModel.setPaymentType(PaymentType.AD);
			break;
		case ATTIVATO_PRESSO_PSP:
			rsModel.setPaymentType(PaymentType.PO);
			break;
		case BOLLETTINO_POSTALE:
			rsModel.setPaymentType(PaymentType.BP);
			break;
		case BONIFICO_BANCARIO_TESORERIA:
			rsModel.setPaymentType(PaymentType.BBT);
			break;
		case CARTA_PAGAMENTO:
			rsModel.setPaymentType(PaymentType.CP);
			break;
		case MYBANK:
			rsModel.setPaymentType(PaymentType.OBEP);
			break;
		case OTHER:
			throw new CodificaInesistenteException("RPT di tipo "+rpt.getTipoVersamento()+", non codificato nella enum PaymentType.");
		}

		Versamento versamento = rpt.getVersamento(configWrapper);
		
		if(versamento.getCausaleVersamento()!= null)
			rsModel.setPaymentCausal(versamento.getCausaleVersamento().getSimple());
		
		rsModel.idRT(rpt.getCodMsgRicevuta());
		rsModel.setPaymentContextCode(rpt.getCcp());
		
		if(rpt.getEsitoPagamento() != null) {
			switch (rpt.getEsitoPagamento()) {
			case DECORRENZA_TERMINI:
				rsModel.setPaymentResultCode(PaymentResultCode._3);
				break;
			case DECORRENZA_TERMINI_PARZIALE:
				rsModel.setPaymentResultCode(PaymentResultCode._4);
				break;
			case PAGAMENTO_ESEGUITO:
				rsModel.setPaymentResultCode(PaymentResultCode._0);
				break;
			case PAGAMENTO_NON_ESEGUITO:
				rsModel.setPaymentResultCode(PaymentResultCode._1);
				break;
			case PAGAMENTO_PARZIALMENTE_ESEGUITO:
				rsModel.setPaymentResultCode(PaymentResultCode._2);
				break;
			case RIFIUTATO:
			case IN_CORSO:
				throw new CodificaInesistenteException("RPT con codice esito pagamento: "+rpt.getEsitoPagamento()+", non codificato nella enum PaymentResultCode.");
			}
		}

		if(rpt.getXmlRt() != null) {
			rsModel.setReceiptXML(Base64.encodeBase64String(rpt.getXmlRt()));

			try {
				switch (rpt.getVersione()) {
				case SANP_240:
					PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);
					rsModel.setAmountPaid(paSendRTReq_RT.getReceipt().getPaymentAmount());
					rsModel.setDateRT(paSendRTReq_RT.getReceipt().getPaymentDateTime());
					break;
				case SANP_230:
					CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
					rsModel.setAmountPaid(ctRt.getDatiPagamento().getImportoTotalePagato());
					rsModel.setDateRT(ctRt.getDataOraMessaggioRicevuta());
					break;
				}
			} catch (Exception e) {
				throw new ServiceException(e);
			}

		}

		return rsModel;
	}

}
