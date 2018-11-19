package it.govpay.core.rs.v2.beans.pagamenti;

import java.net.URI;
import java.util.List;

import it.govpay.core.rs.v1.beans.Lista;

public class ListaRpp extends Lista<Rpp> {
	
	public ListaRpp() {
		super();
	}
	
	public ListaRpp(List<Rpp> rpt, URI requestUri, long count, long pagina, long limit) {
		super(rpt, requestUri, count, pagina, limit);
	}
}
