package it.govpay.core.rs.v1.beans.base;

import java.net.URI;
import java.util.List;

import it.govpay.core.rs.v1.beans.Lista;

public class ListaTracciatiPendenza extends Lista<TracciatoPendenze> {
	
	public ListaTracciatiPendenza() {
		super();
	}
	
	public ListaTracciatiPendenza(List<TracciatoPendenze> flussiRendicontazione, URI requestUri, long count, long pagina, long limit) {
		super(flussiRendicontazione, requestUri, count, pagina, limit);
	}
	
}
