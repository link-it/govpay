package it.govpay.ragioneria.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaDominiIndex extends Lista<DominioIndex> {
	
	public ListaDominiIndex(List<DominioIndex> domini, URI requestUri, long count, long pagina, long limit) {
		super(domini, requestUri, count, pagina, limit);
	}
	
}
