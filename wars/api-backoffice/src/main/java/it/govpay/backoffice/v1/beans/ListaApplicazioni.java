package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaApplicazioni extends Lista<ApplicazioneIndex> {
	
	public ListaApplicazioni() {
		super();
	}
	
	public ListaApplicazioni(List<ApplicazioneIndex> flussiRendicontazione, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(flussiRendicontazione, requestUri, count, pagina, limit);
	}
	
}
