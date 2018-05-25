package it.govpay.core.rs.v1.beans.pagamenti;

import java.net.URI;
import java.util.List;

public class ListaIbanAccredito extends Lista<ContiAccredito> {

	public ListaIbanAccredito(List<ContiAccredito> risultati, URI requestUri, long count, long pagina, long limit) {
		super(risultati, requestUri, count, pagina, limit);
	}
	
}
