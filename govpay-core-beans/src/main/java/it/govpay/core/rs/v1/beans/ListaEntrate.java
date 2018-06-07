package it.govpay.core.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaEntrate extends Lista<it.govpay.core.rs.v1.beans.base.Entrata> {

	public ListaEntrate(List<it.govpay.core.rs.v1.beans.base.Entrata> risultati, URI requestUri, long count, long pagina, long limit) {
		super(risultati, requestUri, count, pagina, limit);
	}
	
}
