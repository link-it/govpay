package it.govpay.core.rs.v1.beans;

import java.net.URI;
import java.util.List;


public class ListaStazioni extends Lista<Stazione> {
	
	public ListaStazioni(List<Stazione> stazioni, URI requestUri, long count, long pagina, long limit) {
		super(stazioni, requestUri, count, pagina, limit);
	}
	
}
