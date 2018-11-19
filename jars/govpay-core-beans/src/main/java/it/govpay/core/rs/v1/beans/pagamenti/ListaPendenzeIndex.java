package it.govpay.core.rs.v1.beans.pagamenti;

import java.net.URI;
import java.util.List;

import it.govpay.core.rs.v1.beans.Lista;

public class ListaPendenzeIndex extends Lista<PendenzaIndex> {
	
	public ListaPendenzeIndex(List<PendenzaIndex> pagamentiPortale, URI requestUri, long count, long pagina, long limit) {
		super(pagamentiPortale, requestUri, count, pagina, limit);
	}
	
}
