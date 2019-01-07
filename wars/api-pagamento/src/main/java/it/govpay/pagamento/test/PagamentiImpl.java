package it.govpay.pagamento.test;

import javax.ws.rs.WebApplicationException;

public class PagamentiImpl implements PagamentiApi {

	@Override
	public PagamentoCreato addPagamento(NuovoPagamento body, String idSessionePortale) {
		throw new WebApplicationException("Trovato " + body.getPendenze().size() + " elementi di tipo " + body.getPendenze().get(0).getClass());
	}

}
