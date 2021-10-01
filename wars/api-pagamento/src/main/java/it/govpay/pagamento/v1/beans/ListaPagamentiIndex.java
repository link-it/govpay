package it.govpay.pagamento.v1.beans;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaPagamentiIndex extends Lista<it.govpay.pagamento.v1.beans.PagamentoIndex> {
	
	public ListaPagamentiIndex(List<it.govpay.pagamento.v1.beans.PagamentoIndex> pagamentiPortale, URI requestUri, Long count, Integer pagina, Integer limit, BigDecimal maxRisultati) {
		super(pagamentiPortale, requestUri, count, pagina, limit, maxRisultati);
	}
}
