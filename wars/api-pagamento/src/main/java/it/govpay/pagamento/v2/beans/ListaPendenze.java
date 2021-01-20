package it.govpay.pagamento.v2.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaPendenze extends Lista<Pendenza> {
	
	public ListaPendenze(List<Pendenza> pagamentiPortale, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(pagamentiPortale, requestUri, count, pagina, limit);
	}
	
}
