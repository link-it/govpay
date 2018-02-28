package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaTipiEntrata extends Lista<Tipoentrata> {
	
	public ListaTipiEntrata(List<Tipoentrata> tipiEntrata, URI requestUri, long count, long pagina, long limit) {
		super(tipiEntrata, requestUri, count, pagina, limit);
	}
	
}
