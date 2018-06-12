package it.govpay.core.rs.v1.beans.base;

import java.net.URI;
import java.util.List;

public class ListaTipiEntrata extends Lista<TipoEntrata> {
	
	public ListaTipiEntrata(List<TipoEntrata> tipiEntrata, URI requestUri, long count, long pagina, long limit) {
		super(tipiEntrata, requestUri, count, pagina, limit);
	}
	
}
