package it.govpay.pendenze.v2.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaRpp extends Lista<RppIndex> {
	
	public ListaRpp() {
		super();
	}
	
	public ListaRpp(List<RppIndex> rpt, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(rpt, requestUri, count, pagina, limit);
	}
	
}
