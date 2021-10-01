package it.govpay.pendenze.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaPendenze extends Lista<PendenzaIndex> {
	
	public ListaPendenze(List<PendenzaIndex> pagamentiPortale, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(pagamentiPortale, requestUri, count, pagina, limit);
	}
	
}
