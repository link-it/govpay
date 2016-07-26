package it.govpay.web.rs.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.rest.Pagamento;
import it.govpay.core.exceptions.GovPayException;

public class PagamentoConverter {

	public static it.govpay.web.rs.model.Pagamento toPagamento(Pagamento pagamentoModel, BasicBD bd) throws ServiceException, GovPayException {
		it.govpay.web.rs.model.Pagamento pagamento = new it.govpay.web.rs.model.Pagamento();
		
		pagamento.setIdentificativoVersamento(pagamentoModel.getCodSingoloVersamentoEnte());
		pagamento.setDataPagamento(pagamentoModel.getDataPagamento());
		pagamento.setImportoPagato(pagamentoModel.getImportoPagato().doubleValue());
		pagamento.setCodiceRendicontazione(pagamentoModel.getCodFlussoRendicontazione());
		pagamento.setCodiceRiversamento(pagamentoModel.getIur());
		pagamento.setNote(pagamentoModel.getNote());
		
		return pagamento;
	}
}
