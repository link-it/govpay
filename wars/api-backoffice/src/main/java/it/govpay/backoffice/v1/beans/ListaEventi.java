package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaEventi extends Lista<Evento> {
	
	public ListaEventi() {
		super();
	}
	
	public ListaEventi(List<Evento> eventi, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(eventi, requestUri, count, pagina, limit);
	}
}
