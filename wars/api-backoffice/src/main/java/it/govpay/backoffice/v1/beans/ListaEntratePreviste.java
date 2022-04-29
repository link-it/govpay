package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaEntratePreviste extends Lista<EntrataPrevistaIndex> {
	
	public ListaEntratePreviste(List<EntrataPrevistaIndex> risultati, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(risultati, requestUri, count, pagina, limit);
	}
	
}
