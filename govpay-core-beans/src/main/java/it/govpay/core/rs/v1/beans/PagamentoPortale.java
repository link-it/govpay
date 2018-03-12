
package it.govpay.core.rs.v1.beans;

import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.utils.SimpleDateFormatUtils;

public class PagamentoPortale extends it.govpay.core.rs.v1.beans.base.Pagamento {

	public PagamentoPortale() {}
	
	@Override
	public String getJsonIdFilter() {
		return "pagamentiPortale";
	}
	
	public static PagamentoPortale parse(String json) {
		return (PagamentoPortale) parse(json, PagamentoPortale.class);
	}

	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}
}
