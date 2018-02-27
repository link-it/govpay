package it.govpay.rs.v1.beans;

import java.net.URI;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.utils.SimpleDateFormatUtils;

public class ListaRpt extends Lista<Rpt> {
	
	public ListaRpt() {
		super();
	}
	
	public ListaRpt(List<Rpt> rpt, URI requestUri, long count, long pagina, long limit) {
		super(rpt, requestUri, count, pagina, limit);
	}
	
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}
}
