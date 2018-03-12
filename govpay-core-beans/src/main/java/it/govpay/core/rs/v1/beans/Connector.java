package it.govpay.core.rs.v1.beans;

public class Connector extends it.govpay.core.rs.v1.beans.base.Connector {

	@Override
	public String getJsonIdFilter() {
		return "connector";
	}
	
	public static Connector parse(String json) {
		return (Connector) parse(json, Connector.class);
	}
	public Connector() {
	}
}
