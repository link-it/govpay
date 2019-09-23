package it.govpay.pagamento.v2.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaDomini extends Lista<Dominio> {
	
	public ListaDomini(List<Dominio> domini, URI requestUri, long count, long pagina, long limit) {
		super(domini, requestUri, count, pagina, limit);
	}
	
}
