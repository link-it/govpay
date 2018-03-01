package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;


public class ListaAcl extends Lista<ACL> {
	
	public ListaAcl(List<ACL> acl, URI requestUri, long count, long pagina, long limit) {
		super(acl, requestUri, count, pagina, limit);
	}
	
}
