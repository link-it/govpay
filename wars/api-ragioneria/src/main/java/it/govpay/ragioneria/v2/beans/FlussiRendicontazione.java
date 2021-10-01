package it.govpay.ragioneria.v2.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class FlussiRendicontazione extends Lista<FlussoRendicontazioneIndex> {
	
	public FlussiRendicontazione() {
		super();
	}
	
	public FlussiRendicontazione(List<FlussoRendicontazioneIndex> flussiRendicontazione, URI requestUri, Long count, Integer pagina, Integer limit) {
		super(flussiRendicontazione, requestUri, count, pagina, limit);
	}
	
}
