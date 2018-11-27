package it.govpay.pendenze.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaEntrate extends Lista<Entrata> {
	
	public ListaEntrate() {
		super();
	}
	
	public ListaEntrate(List<Entrata> flussiRendicontazione, URI requestUri, long count, long pagina, long limit) {
		super(flussiRendicontazione, requestUri, count, pagina, limit);
	}
	
}
