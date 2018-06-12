package it.govpay.core.rs.v1.beans.base;

import java.net.URI;
import java.util.List;

public class ListaContiAccredito extends Lista<ContiAccredito> {

	public ListaContiAccredito(List<ContiAccredito> risultati, URI requestUri, long count, long pagina, long limit) {
		super(risultati, requestUri, count, pagina, limit);
	}
	
}
