package it.govpay.core.rs.v1.beans.pendenze;

import java.net.URI;
import java.util.List;

import it.govpay.core.rs.v1.beans.Lista;


public class ListaAcl extends Lista<AclPost> {
	
	public ListaAcl() {
	}
	public ListaAcl(List<AclPost> acl, URI requestUri, long count, long pagina, long limit) {
		super(acl, requestUri, count, pagina, limit);
	}
	
}
