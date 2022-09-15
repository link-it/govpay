package it.govpay.netpay.v1.verifica.converter;

import java.util.Date;
import java.util.List;

import it.govpay.core.beans.commons.Anagrafica;
import it.govpay.core.beans.commons.Versamento;
import it.govpay.core.utils.IuvNavUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.netpay.v1.model.CheckPaymentItem;
import it.govpay.netpay.v1.model.CheckPaymentResponse;

public class VersamentoConverter {

	/***
	 * Converte la risposta della chiamata CheckPayment in un oggetto di tipo Versamento interno a GovPay
	 * 
	 * Ogni elemento della lista checkPaymentList ha la seguente forma:
	 * {
		"paymentId":<integer>,
		"totAmount":<integer>,
		"dueDate":<datetime format 'dd/MM/yyyy HH:mm:ss'>,
		"invoiceID":<string maxlen 50>,
		"invoiceType":<string maxlen 50>,
		"additionalRemittanceInfo":<string maxlen 140>,
		"creditorIBAN":<string maxlen 35>,
		"clientType":<string maxlen 1>,
		"clientDescription":<string maxlen 70>,
		"clientFiscalID":<string maxlen 35>,
		}
	 * @param response
	 * @param applicationCode 
	 * @param auxDigit 
	 * @return
	 */
	public static Versamento getVersamentoFromCheckPaymentResponse(CheckPaymentResponse response, String codApplicazione, String codDominio, String iuv, int auxDigit, int applicationCode) {
		it.govpay.core.beans.commons.Versamento versamento = new it.govpay.core.beans.commons.Versamento();
		
		List<CheckPaymentItem> checkPaymentList = response.getCheckPaymentList();
		CheckPaymentItem checkPaymentItem = checkPaymentList.get(0);
		
		versamento.setCausale(checkPaymentItem.getInvoiceType());
		versamento.setCodApplicazione(codApplicazione);
		
		versamento.setCodDominio(codDominio);
		versamento.setCodVersamentoEnte(checkPaymentItem.getInvoiceID());
		versamento.setDataScadenza(SimpleDateFormatUtils.toDate(checkPaymentItem.getDueDate())); 
		versamento.setDataValidita(new Date()); // Validita' entro oggi cosi da scatenare il processo di aggiornamento in caso di richieste successive
		Anagrafica debitore = new Anagrafica();
		debitore.setCodUnivoco(checkPaymentItem.getClientFiscalID());
		debitore.setRagioneSociale(checkPaymentItem.getClientDescription());
		debitore.setTipo(checkPaymentItem.getClientType().toString().toUpperCase());
		versamento.setDebitore(debitore );
		versamento.setImportoTotale(checkPaymentItem.getTotAmount());
		
		versamento.setStatoVersamento(StatoVersamento.NON_ESEGUITO);
		versamento.setNumeroAvviso(IuvNavUtils.toNumeroAvviso(iuv, auxDigit, applicationCode));
		
		// voci pagamento
		it.govpay.core.beans.commons.Versamento.SingoloVersamento sv = new it.govpay.core.beans.commons.Versamento.SingoloVersamento();
		sv.setCodSingoloVersamentoEnte(checkPaymentItem.getPaymentId().toString());
		sv.setDescrizione(checkPaymentItem.getInvoiceType());
		sv.setDescrizioneCausaleRPT(checkPaymentItem.getInvoiceType());
		
		it.govpay.core.beans.commons.Versamento.SingoloVersamento.Tributo tributo = new it.govpay.core.beans.commons.Versamento.SingoloVersamento.Tributo();
		String additionalRemittanceInfo = checkPaymentItem.getAdditionalRemittanceInfo();
		String[] split = additionalRemittanceInfo.split("/");
		
		tributo.setCodContabilita(split[1]);
		tributo.setIbanAccredito(checkPaymentItem.getCreditorIBAN());
		tributo.setTipoContabilita(it.govpay.core.beans.commons.Versamento.SingoloVersamento.TipoContabilita.valueOf(split[0]));
		sv.setTributo(tributo);
		
		versamento.getSingoloVersamento().add(sv);
		
		return versamento;
	}
}
