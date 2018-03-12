package it.govpay.core.rs.v1.beans;

import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.utils.SimpleDateFormatUtils;

public class Rpt  extends it.govpay.core.rs.v1.beans.base.Rpt{

	public Rpt() {}
	
	@Override
	public String getJsonIdFilter() {
		return "rpt";
	}
	
	public static Rpt parse(String json) {
		return (Rpt) parse(json, Rpt.class);
	}
	
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}
}
