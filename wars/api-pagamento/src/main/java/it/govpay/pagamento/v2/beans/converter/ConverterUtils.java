package it.govpay.pagamento.v2.beans.converter;

import javax.ws.rs.core.UriBuilder;

import it.govpay.pagamento.v2.beans.Lista;


public class ConverterUtils {

	public static void popolaLista(Lista lista, UriBuilder requestURI, int items, int offset, int limit, long total) {
		if(offset > 0)
			lista.setFirst(requestURI.queryParam("offset", 0).build().toString());
		
		if(offset + items < total) 
			lista.setLast(requestURI.queryParam("offset", (total % limit) * limit).build().toString());
		
		if(offset + limit < total)
			lista.setNext(requestURI.queryParam("offset", offset + limit).build().toString());
		
		if(offset - limit > 0)
			lista.setPrev(requestURI.queryParam("offset",offset - limit).build().toString());
		
		lista.setOffset(offset);
		lista.setLimit(limit);
		lista.setTotal(total);
	}
}
