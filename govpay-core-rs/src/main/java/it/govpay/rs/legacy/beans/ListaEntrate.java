package it.govpay.rs.legacy.beans;

import java.net.URI;
import java.util.List;

import it.govpay.rs.v1.beans.Lista;

public class ListaEntrate extends Lista<Entrata> {

	public ListaEntrate(List<Entrata> risultati, URI requestUri, long count, long pagina, long limit) {
		super(risultati, requestUri, count, pagina, limit);
	}
	
}
