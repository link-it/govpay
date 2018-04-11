package it.govpay.core.rs.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.rs.v1.beans.base.OperazioneIndex;

public class ListaOperazioni extends Lista<OperazioneIndex> {
	
	public ListaOperazioni(List<OperazioneIndex> operatori, URI requestUri, long count, long pagina, long limit) {
		super(operatori, requestUri, count, pagina, limit);
	}
	
}
