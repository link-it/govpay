
package it.govpay.core.rs.v1.beans;

import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.utils.SimpleDateFormatUtils;

public class Pendenza extends it.govpay.core.rs.v1.beans.base.Pendenza {

	public Pendenza() {}
	
	@Override
	public String getJsonIdFilter() {
		return "pendenze";
	}
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}

}
