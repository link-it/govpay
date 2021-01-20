package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaOperatori extends Lista<OperatoreIndex> {
	
	public ListaOperatori(List<OperatoreIndex> operatori, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(operatori, requestUri, count, pagina, limit);
	}
	
}
