package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaContiAccredito extends Lista<ContiAccredito> {

	public ListaContiAccredito(List<ContiAccredito> risultati, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(risultati, requestUri, count, pagina, limit);
	}
	
}
