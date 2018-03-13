
package it.govpay.core.rs.v1.beans;

import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.utils.SimpleDateFormatUtils;

public class Riscossione extends it.govpay.core.rs.v1.beans.base.Riscossione {

	public Riscossione() {}
	
	@Override
	public String getJsonIdFilter() {
		return "riscossioni";
	}
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}

}
