package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaOperazioniPendenza extends Lista<OperazionePendenza> {
	
	public ListaOperazioniPendenza() {
		super();
	}
	
	public ListaOperazioniPendenza(List<OperazionePendenza> flussiRendicontazione, URI requestUri, long count, long pagina, long limit) {
		super(flussiRendicontazione, requestUri, count, pagina, limit);
	}
	
}
