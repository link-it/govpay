package it.govpay.ragioneria.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaFlussiRendicontazione extends Lista<FlussoRendicontazioneIndex> {
	
	public ListaFlussiRendicontazione() {
		super();
	}
	
	public ListaFlussiRendicontazione(List<FlussoRendicontazioneIndex> flussiRendicontazione, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(flussiRendicontazione, requestUri, count, pagina, limit);
	}
	
}
