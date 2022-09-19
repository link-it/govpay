package it.govpay.netpay.v1.beans.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.core.beans.commons.Anagrafica;
import it.govpay.core.beans.commons.Versamento;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.utils.IuvNavUtils;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.netpay.v1.beans.ActivePaymentItem;
import it.govpay.netpay.v1.beans.ActivePaymentRequest;

public class PagamentiConverter {

	public static PagamentiPortaleDTO getPagamentiPortaleDTO(ActivePaymentRequest activePaymentRequest, Applicazione applicazione, Dominio dominio, Authentication user, String idSessione, Logger log) {
		
		PagamentiPortaleDTO pagamentiPortaleDTO = new PagamentiPortaleDTO(user);
		
		pagamentiPortaleDTO.setIdSessione(idSessione);
		pagamentiPortaleDTO.setAutenticazioneSoggetto("N/A");
		
		pagamentiPortaleDTO.setUrlRitorno(activePaymentRequest.getCallbackURL());
		
		List<Object> listRefs = new ArrayList<>();
		
		for (ActivePaymentItem activePaymentItem  : activePaymentRequest.getActivePaymentList()) {
			it.govpay.core.beans.commons.Versamento versamento = new Versamento();
			
			versamento.setCausale(activePaymentItem.getInvoiceType());
			versamento.setCodApplicazione(applicazione.getCodApplicazione());
			
			versamento.setCodDominio(activePaymentRequest.getDomainId());
			versamento.setCodVersamentoEnte(activePaymentItem.getInvoiceID());
			versamento.setDataScadenza(activePaymentItem.getDueDate()); 
			versamento.setDataValidita(new Date()); // Validita' entro oggi cosi da scatenare il processo di aggiornamento in caso di richieste successive
			Anagrafica debitore = new Anagrafica();
			debitore.setCodUnivoco(activePaymentItem.getClientFiscalID());
			debitore.setRagioneSociale(activePaymentItem.getClientDescription());
			debitore.setTipo(activePaymentItem.getClientType().toString().toUpperCase());
			versamento.setDebitore(debitore );
			versamento.setImportoTotale(activePaymentItem.getTotAmount());
			
			versamento.setStatoVersamento(StatoVersamento.NON_ESEGUITO);
			int auxDigit = dominio.getAuxDigit();
			int applicationCode = dominio.getStazione().getApplicationCode();
			versamento.setNumeroAvviso(IuvNavUtils.toNumeroAvviso(activePaymentRequest.getCreditorTxId(), auxDigit, applicationCode));
			
			// voci pagamento
			it.govpay.core.beans.commons.Versamento.SingoloVersamento sv = new it.govpay.core.beans.commons.Versamento.SingoloVersamento();
			sv.setCodSingoloVersamentoEnte(activePaymentItem.getPaymentId().toString());
			sv.setDescrizione(activePaymentItem.getInvoiceType());
			sv.setDescrizioneCausaleRPT(activePaymentItem.getInvoiceType());
			
			it.govpay.core.beans.commons.Versamento.SingoloVersamento.Tributo tributo = new it.govpay.core.beans.commons.Versamento.SingoloVersamento.Tributo();
			String additionalRemittanceInfo = activePaymentItem.getAdditionalRemittanceInfo();
			String[] split = additionalRemittanceInfo.split("/");
			
			tributo.setCodContabilita(split[1]);
			tributo.setIbanAccredito(activePaymentItem.getCreditorIBAN());
			tributo.setTipoContabilita(it.govpay.core.beans.commons.Versamento.SingoloVersamento.TipoContabilita.valueOf(split[0]));
			sv.setTributo(tributo);
			
			versamento.getSingoloVersamento().add(sv);
			
			listRefs.add(versamento);
		}
		
		return pagamentiPortaleDTO;
	}

}
