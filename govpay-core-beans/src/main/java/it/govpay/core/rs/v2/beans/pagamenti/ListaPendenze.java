package it.govpay.core.rs.v2.beans.pagamenti;

import java.net.URI;
import java.util.List;

import it.govpay.core.rs.v1.beans.Lista;

public class ListaPendenze extends Lista<Pendenza> {
	
	public ListaPendenze(List<Pendenza> pagamentiPortale, URI requestUri, long count, long pagina, long limit) {
		super(pagamentiPortale, requestUri, count, pagina, limit);
	}
	
}
