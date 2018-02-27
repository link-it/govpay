package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.utils.SimpleDateFormatUtils;

public class ListaPagamentiPortale extends Lista<PagamentoPortale> {
	
	public ListaPagamentiPortale(List<PagamentoPortale> pagamentiPortale, URI requestUri, long count, long pagina, long limit) {
		super(pagamentiPortale, requestUri, count, pagina, limit);
	}
	
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}
}
