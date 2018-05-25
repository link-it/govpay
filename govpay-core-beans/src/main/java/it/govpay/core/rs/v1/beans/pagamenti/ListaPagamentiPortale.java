package it.govpay.core.rs.v1.beans.pagamenti;

import java.net.URI;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.utils.SimpleDateFormatUtils;

public class ListaPagamentiPortale extends it.govpay.core.rs.v1.beans.pagamenti.Lista<it.govpay.core.rs.v1.beans.pagamenti.Pagamento> {
	
	public ListaPagamentiPortale(List<it.govpay.core.rs.v1.beans.pagamenti.Pagamento> pagamentiPortale, URI requestUri, long count, long pagina, long limit) {
		super(pagamentiPortale, requestUri, count, pagina, limit);
	}
	
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}
}
