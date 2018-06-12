package it.govpay.core.rs.v1.beans.base;

import java.net.URI;
import java.util.List;

public class ListaOperatori extends Lista<Operatore> {
	
	public ListaOperatori(List<Operatore> operatori, URI requestUri, long count, long pagina, long limit) {
		super(operatori, requestUri, count, pagina, limit);
	}
	
}
