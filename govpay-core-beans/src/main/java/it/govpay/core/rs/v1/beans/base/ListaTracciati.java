package it.govpay.core.rs.v1.beans.base;

import java.net.URI;
import java.util.List;

import it.govpay.core.rs.v1.beans.Lista;

public class ListaTracciati extends Lista<Tracciato> {
	
	public ListaTracciati() {
		super();
	}
	
	public ListaTracciati(List<Tracciato> rpt, URI requestUri, long count, long pagina, long limit) {
		super(rpt, requestUri, count, pagina, limit);
	}
	
}
