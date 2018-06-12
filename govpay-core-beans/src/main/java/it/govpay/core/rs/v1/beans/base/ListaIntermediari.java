package it.govpay.core.rs.v1.beans.base;

import java.net.URI;
import java.util.List;

public class ListaIntermediari extends Lista<Intermediario> {
	
	public ListaIntermediari(List<Intermediario> incassi, URI requestUri, long count, long offset, long limit) {
		super(incassi, requestUri, count, offset, limit);
	}
	
}
