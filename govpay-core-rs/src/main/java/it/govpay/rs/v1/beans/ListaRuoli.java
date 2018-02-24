package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaRuoli extends Lista<Ruolo> {
	
	public ListaRuoli(List<Ruolo> domini, URI requestUri, long count, long pagina, long limit) {
		super(domini, requestUri, count, pagina, limit);
	}
	
}
