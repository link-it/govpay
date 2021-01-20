package it.govpay.pendenze.v2.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;


public class ListaAcl extends Lista<Acl> {
	
	public ListaAcl() {
	}
	public ListaAcl(List<Acl> acl, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(acl, requestUri, count, pagina, limit);
	}
	
}
