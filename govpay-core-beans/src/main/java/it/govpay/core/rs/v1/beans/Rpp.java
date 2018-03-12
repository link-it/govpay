package it.govpay.core.rs.v1.beans;

import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.utils.SimpleDateFormatUtils;

public class Rpp  extends it.govpay.core.rs.v1.beans.base.Rpp{

	public Rpp() {}
	
	@Override
	public String getJsonIdFilter() {
		return "rpp";
	}
	
	public static Rpp parse(String json) {
		return (Rpp) parse(json, Rpp.class);
	}
	
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}
}
