package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaStazioni extends Lista<StazioneIndex> {
	
	public ListaStazioni(List<StazioneIndex> incassi, URI requestUri, Long count, Integer offset, Integer limit) {
		super(incassi, requestUri, count, offset, limit);
	}
	
}
