package it.govpay.pendenze.v2.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;


public class ListaAcl extends Lista<AclPost> {
	
	public ListaAcl() {
	}
	public ListaAcl(List<AclPost> acl, URI requestUri, long count, long pagina, long limit) {
		super(acl, requestUri, count, pagina, limit);
	}
	
}
