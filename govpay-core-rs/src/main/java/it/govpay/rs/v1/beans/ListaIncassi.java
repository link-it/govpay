package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaIncassi extends Lista<Incasso> {
	
	public ListaIncassi(List<Incasso> incassi, URI requestUri, long count, long offset, long limit) {
		super(incassi, requestUri, count, offset, limit);
	}
	
}
