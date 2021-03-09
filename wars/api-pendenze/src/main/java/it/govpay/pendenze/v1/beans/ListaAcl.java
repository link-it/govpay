package it.govpay.pendenze.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;


public class ListaAcl extends Lista<AclPost> {
	
	public ListaAcl() {
	}
	public ListaAcl(List<AclPost> acl, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(acl, requestUri, count, pagina, limit);
	}
	
}
