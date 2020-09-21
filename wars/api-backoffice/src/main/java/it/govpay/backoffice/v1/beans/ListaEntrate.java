package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaEntrate extends Lista<Entrata> {
	
	public ListaEntrate() {
		super();
	}
	
	public ListaEntrate(List<Entrata> flussiRendicontazione, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(flussiRendicontazione, requestUri, count, pagina, limit);
	}
	
}
