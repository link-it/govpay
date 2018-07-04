package it.govpay.core.rs.v1.beans.base;

import java.net.URI;
import java.util.List;

import it.govpay.core.rs.v1.beans.Lista;

public class ListaFlussiRendicontazione extends Lista<FlussoRendicontazioneIndex> {
	
	public ListaFlussiRendicontazione() {
		super();
	}
	
	public ListaFlussiRendicontazione(List<FlussoRendicontazioneIndex> flussiRendicontazione, URI requestUri, long count, long pagina, long limit) {
		super(flussiRendicontazione, requestUri, count, pagina, limit);
	}
	
}
