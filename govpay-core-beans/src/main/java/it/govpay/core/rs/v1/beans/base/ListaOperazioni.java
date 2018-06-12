package it.govpay.core.rs.v1.beans.base;

import java.net.URI;
import java.util.List;

public class ListaOperazioni extends Lista<OperazioneIndex> {
	
	public ListaOperazioni(List<OperazioneIndex> tipiEntrata, URI requestUri, long count, long pagina, long limit) {
		super(tipiEntrata, requestUri, count, pagina, limit);
	}
	
}
