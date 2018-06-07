package it.govpay.core.rs.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.rs.v1.beans.base.DominioIndex;

public class ListaDomini extends Lista<DominioIndex> {
	
	public ListaDomini(List<DominioIndex> domini, URI requestUri, long count, long pagina, long limit) {
		super(domini, requestUri, count, pagina, limit);
	}
	
}
