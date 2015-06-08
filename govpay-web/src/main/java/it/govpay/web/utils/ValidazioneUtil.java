/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.web.utils;

import java.math.BigDecimal;

import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.core.model.GatewayPagamentoModel;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumFornitoreGateway;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumModalitaPagamento;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumModelloVersamento;
import it.govpay.rs.DatiSingoloVersamento;
import it.govpay.rs.Pagamento;
import it.govpay.rs.RichiestaPagamento;

public class ValidazioneUtil {

	public static void valida(RichiestaPagamento richiestaPagamento, GatewayPagamentoModel gw) throws GovPayException {
		try {
			for(Pagamento p : richiestaPagamento.getPagamentis()) {
				BigDecimal totaleAtteso = p.getDatiVersamento().getImportoTotaleDaVersare();
				BigDecimal totaleCalcolato = BigDecimal.ZERO;
				for(DatiSingoloVersamento dsv : p.getDatiVersamento().getDatiSingoloVersamentos()) {
					totaleCalcolato = totaleCalcolato.add(dsv.getImportoSingoloVersamento());
				}
				if(totaleAtteso.compareTo(totaleCalcolato) != 0) {
					throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE, "Il pagamento [Beneficiario: " + p.getIdentificativoBeneficiario() + "][IUV: " + p.getDatiVersamento().getIuv() + "] riporta un importo totale [" + totaleAtteso + "] diverso dalla somma degli importi dei singoli versamenti [" + totaleCalcolato + "].");
				}
				
				// In caso di AddebitoDiretto, deve essere specificato l'Iban del beneficiario
				if(gw.getModalitaPagamento().equals(EnumModalitaPagamento.ADDEBITO_DIRETTO) && p.getDatiVersamento().getIbanAddebito() == null) {
					throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE, "Il psp selezionato prevede una modalita di pagamento per cui e' obbligatorio specificare l'Iban di addebito.");
				}
			}
			
			if(!gw.getFornitoreGateway().equals(EnumFornitoreGateway.NODO_PAGAMENTI_SPC)) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE, "Il psp selezionato [Id: " + gw.getIdGateway() + "] non e' gestito da questa applicazione.");
			} 
			
			// Pagamento MyBank. Deve essere un pagamento singolo con una sola condizione
			if(gw.getModalitaPagamento().equals(EnumModalitaPagamento.MYBANK)) {
				if(richiestaPagamento.getPagamentis().size() != 1)
					throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE, "La modalita di pagamento MyBank non supporta pagamenti multipli");
				if(richiestaPagamento.getPagamentis().get(0).getDatiVersamento().getDatiSingoloVersamentos().size() != 1)
					throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE, "La modalita di pagamento MyBank non supporta pagamenti con versamenti multipli");
			}
			
			if(richiestaPagamento.getPagamentis().size() > 1){
				// Pagamento multiplo. Il psp deve avere la modalita multi-beneficiario
				if(!gw.getModelloVersamento().equals(EnumModelloVersamento.IMMEDIATO_MULTIBENEFICIARIO)) {
					throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE, "Il psp selezionato [Id: " + gw.getIdGateway() + "] non supporta pagamenti multibeneficiario.");
				}
			} else {
				// Pagamento singolo, non deve essere ATTIVATO_PRESSO_PSP o IMMEDIATO_MULTIBENEFICIARIO
				if(gw.getModelloVersamento().equals(EnumModelloVersamento.ATTIVATO_PRESSO_PSP) || gw.getModelloVersamento().equals(EnumModelloVersamento.IMMEDIATO_MULTIBENEFICIARIO)) {
					throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE, "Il psp selezionato [Id: " + gw.getIdGateway() + "] non supporta pagamenti monobeneficiario.");
				}
			}
		} catch (Exception e) {
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			else 
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}
}
