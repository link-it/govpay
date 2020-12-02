package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaRiscossioni extends Lista<Riscossione> {
	
	public ListaRiscossioni(List<Riscossione> pagamentiPortale, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(pagamentiPortale, requestUri, count, pagina, limit);
	}
	
}
