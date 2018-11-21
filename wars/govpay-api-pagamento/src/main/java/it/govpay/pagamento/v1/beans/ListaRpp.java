package it.govpay.pagamento.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaRpp extends Lista<Rpp> {
	
	public ListaRpp() {
		super();
	}
	
	public ListaRpp(List<Rpp> rpt, URI requestUri, long count, long pagina, long limit) {
		super(rpt, requestUri, count, pagina, limit);
	}
}
