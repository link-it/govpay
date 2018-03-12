package it.govpay.core.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaPsp extends Lista<Psp> {
	
	public ListaPsp() {
		super();
	}
	
	public ListaPsp(List<Psp> psp, URI requestUri, long count, long pagina, long limit) {
		super(psp, requestUri, count, pagina, limit);
	}
	
}
