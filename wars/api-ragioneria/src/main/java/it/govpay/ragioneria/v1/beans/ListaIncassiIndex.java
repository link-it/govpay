package it.govpay.ragioneria.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaIncassiIndex extends Lista<IncassoIndex> {
	
	public ListaIncassiIndex(List<IncassoIndex> incassi, URI requestUri, Long count, Integer offset, Integer limit) {
		super(incassi, requestUri, count, offset, limit);
	}
	
}
