package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaTipiPendenza extends Lista<TipoPendenza> {
	
	public ListaTipiPendenza(List<TipoPendenza> tipiEntrata, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(tipiEntrata, requestUri, count, pagina, limit);
	}
	
}
