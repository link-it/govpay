package it.govpay.core.rs.v1.beans.base;

import java.net.URI;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.utils.SimpleDateFormatUtils;

public class ListaRpp extends Lista<RppIndex> {
	
	public ListaRpp() {
		super();
	}
	
	public ListaRpp(List<RppIndex> rpt, URI requestUri, long count, long pagina, long limit) {
		super(rpt, requestUri, count, pagina, limit);
	}
	
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}
}
