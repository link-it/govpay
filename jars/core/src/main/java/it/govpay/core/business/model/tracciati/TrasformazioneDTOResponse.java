package it.govpay.core.business.model.tracciati;

import java.util.Map;

public class TrasformazioneDTOResponse {
	private String output;
	private Map<String, Object> dynamicMap;

	public TrasformazioneDTOResponse(String output, Map<String, Object> dynamicMap) {
		this.dynamicMap = dynamicMap;
		this.output = output;
	}

	public String getOutput() {
		return output;
	}

	public Map<String, Object> getDynamicMap() {
		return dynamicMap;
	}
}
