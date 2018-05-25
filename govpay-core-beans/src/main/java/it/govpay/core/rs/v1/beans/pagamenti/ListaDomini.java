package it.govpay.core.rs.v1.beans.pagamenti;

import java.net.URI;
import java.util.List;

public class ListaDomini extends Lista<Dominio> {
	
	public ListaDomini(List<Dominio> domini, URI requestUri, long count, long pagina, long limit) {
		super(domini, requestUri, count, pagina, limit);
	}
	
}
