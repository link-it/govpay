package it.govpay.pagamento.v2.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaRppIndex extends Lista<RppIndex> {
	
	public ListaRppIndex() {
		super();
	}
	
	public ListaRppIndex(List<RppIndex> rpt, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(rpt, requestUri, count, pagina, limit);
	}
	
}
