package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaTipiPendenzaDominio extends Lista<TipoPendenzaDominio> {
	
	public ListaTipiPendenzaDominio(List<TipoPendenzaDominio> tipiEntrata, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(tipiEntrata, requestUri, count, pagina, limit);
	}
	
}
