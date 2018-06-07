package it.govpay.core.rs.v1.beans.pendenze;

import java.net.URI;
import java.util.List;


public class ListaAcl extends Lista<Acl> {
	
	public ListaAcl() {
	}
	public ListaAcl(List<Acl> acl, URI requestUri, long count, long pagina, long limit) {
		super(acl, requestUri, count, pagina, limit);
	}
	
}
