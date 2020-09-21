package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaRendicontazioni extends Lista<RendicontazioneConFlussoEVocePendenza> {
	
	public ListaRendicontazioni(List<RendicontazioneConFlussoEVocePendenza> rendicontazioni, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(rendicontazioni, requestUri, count, pagina, limit);
	}
	
}
