package it.govpay.core.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaIncassiIndex extends Lista<IncassoIndex> {
	
	public ListaIncassiIndex(List<IncassoIndex> incassi, URI requestUri, long count, long offset, long limit) {
		super(incassi, requestUri, count, offset, limit);
	}
	
}
