package it.govpay.ragioneria.v2.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaIncassiIndex extends Lista<IncassoIndex> {
	
	public ListaIncassiIndex(List<IncassoIndex> incassi, URI requestUri, long count, long offset, long limit) {
		super(incassi, requestUri, count, offset, limit);
	}
	
}
