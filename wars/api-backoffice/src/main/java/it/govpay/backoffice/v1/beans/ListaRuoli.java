package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaRuoli extends Lista<RuoloIndex> {
	
	public ListaRuoli() {
		super();
	}
	
	public ListaRuoli(List<RuoloIndex> rpt, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(rpt, requestUri, count, pagina, limit);
	}
	
}
