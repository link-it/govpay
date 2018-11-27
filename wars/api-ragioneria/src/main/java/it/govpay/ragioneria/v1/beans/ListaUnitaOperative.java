package it.govpay.ragioneria.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaUnitaOperative extends Lista<UnitaOperativa> {

	public ListaUnitaOperative(List<UnitaOperativa> risultati, URI requestUri, long count, long pagina, long limit) {
		super(risultati, requestUri, count, pagina, limit);
	}
	
}
