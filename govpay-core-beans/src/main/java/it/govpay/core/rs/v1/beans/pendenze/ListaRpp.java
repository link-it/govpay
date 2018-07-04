package it.govpay.core.rs.v1.beans.pendenze;

import java.net.URI;
import java.util.List;

import it.govpay.core.rs.v1.beans.Lista;

public class ListaRpp extends Lista<RppIndex> {
	
	public ListaRpp() {
		super();
	}
	
	public ListaRpp(List<RppIndex> rpt, URI requestUri, long count, long pagina, long limit) {
		super(rpt, requestUri, count, pagina, limit);
	}
	
}
