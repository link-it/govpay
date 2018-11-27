package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaOperatori extends Lista<Operatore> {
	
	public ListaOperatori(List<Operatore> operatori, URI requestUri, long count, long pagina, long limit) {
		super(operatori, requestUri, count, pagina, limit);
	}
	
}
