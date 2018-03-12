package it.govpay.core.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaPendenze extends Lista<Pendenza> {
	
	public ListaPendenze(List<Pendenza> pagamentiPortale, URI requestUri, long count, long pagina, long limit) {
		super(pagamentiPortale, requestUri, count, pagina, limit);
	}
	
}
