package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaIntermediari extends Lista<IntermediarioIndex> {
	
	public ListaIntermediari(List<IntermediarioIndex> incassi, URI requestUri, Long count, Integer offset, Integer limit) {
		super(incassi, requestUri, count, offset, limit);
	}
	
}
