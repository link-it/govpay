package it.govpay.core.rs.v1.beans.pagamenti;

import java.net.URI;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.utils.SimpleDateFormatUtils;

public class ListaRppIndex extends Lista<RppIndex> {
	
	public ListaRppIndex() {
		super();
	}
	
	public ListaRppIndex(List<RppIndex> rpt, URI requestUri, long count, long pagina, long limit) {
		super(rpt, requestUri, count, pagina, limit);
	}
	
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}
}
