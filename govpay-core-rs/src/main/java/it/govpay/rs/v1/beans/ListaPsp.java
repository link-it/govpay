package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaPsp extends Lista<Psp> {
	
	public ListaPsp(List<Psp> psp, URI requestUri, long count, long pagina, long limit) {
		super(psp, requestUri, count, pagina, limit);
	}
	
}
