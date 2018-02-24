package it.govpay.rs.legacy.beans;

import java.net.URI;
import java.util.List;

import it.govpay.rs.v1.beans.Lista;

public class ListaIbanAccredito extends Lista<Iban> {

	public ListaIbanAccredito(List<Iban> risultati, URI requestUri, long count, long pagina, long limit) {
		super(risultati, requestUri, count, pagina, limit);
	}
	
}
