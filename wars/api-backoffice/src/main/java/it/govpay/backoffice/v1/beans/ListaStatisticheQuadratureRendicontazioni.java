package it.govpay.backoffice.v1.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class ListaStatisticheQuadratureRendicontazioni extends Lista<StatisticaQuadraturaRendicontazione> {
	
	public ListaStatisticheQuadratureRendicontazioni(List<StatisticaQuadraturaRendicontazione> risultati, URI requestUri, long count, long pagina, long limit) {
		super(risultati, requestUri, count, pagina, limit);
	}
	
}
