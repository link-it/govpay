package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaDomini extends Lista<DominioIndex> {
	
	public ListaDomini() {
		super();
	}
	
	public ListaDomini(List<DominioIndex> flussiRendicontazione, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(flussiRendicontazione, requestUri, count, pagina, limit);
	}
	
}
