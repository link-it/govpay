package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaCanali extends Lista<Canale> {
	
	public ListaCanali(List<Canale> canale, URI requestUri, long count, long pagina, long limit) {
		super(canale, requestUri, count, pagina, limit);
	}
	
}
