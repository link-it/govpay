package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaTracciatiPendenza extends Lista<TracciatoPendenzeIndex> {
	
	public ListaTracciatiPendenza() {
		super();
	}
	
	public ListaTracciatiPendenza(List<TracciatoPendenzeIndex> flussiRendicontazione, URI requestUri, long count, long pagina, long limit) {
		super(flussiRendicontazione, requestUri, count, pagina, limit);
	}
	
}
