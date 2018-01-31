package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaIbanAccredito extends Lista<Iban> {

	public ListaIbanAccredito(List<Iban> risultati, URI requestUri, long count, long offset, long limit) {
		super(risultati, requestUri, count, offset, limit);
	}
	
}
