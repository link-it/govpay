package it.govpay.core.rs.v1.beans;

import java.net.URI;
import java.util.List;


public class ListaIntermediari extends Lista<Intermediario> {
	
	public ListaIntermediari(List<Intermediario> intermediari, URI requestUri, long count, long pagina, long limit) {
		super(intermediari, requestUri, count, pagina, limit);
	}
	
}
