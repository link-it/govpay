package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaEntrate extends Lista<Entrata> {

	public ListaEntrate(List<Entrata> risultati, URI requestUri, long count, long pagina, long risultatiPerPagina) {
		super(risultati, requestUri, count, pagina, risultatiPerPagina);
	}
	
}
