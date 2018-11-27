package it.govpay.pagamento.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaRppIndex extends Lista<RppIndex> {
	
	public ListaRppIndex() {
		super();
	}
	
	public ListaRppIndex(List<RppIndex> rpt, URI requestUri, long count, long pagina, long limit) {
		super(rpt, requestUri, count, pagina, limit);
	}
	
}
