package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;


public class ListaApplicazioni extends Lista<Applicazione> {
	
	public ListaApplicazioni(List<Applicazione> applicazioni, URI requestUri, long count, long pagina, long limit) {
		super(applicazioni, requestUri, count, pagina, limit);
	}
	
}
