package it.govpay.core.rs.v1.beans;

public class Applicazione extends it.govpay.core.rs.v1.beans.base.Applicazione {

	@Override
	public String getJsonIdFilter() {
		return "applicazione";
	}
	
	public static Applicazione parse(String json) {
		return (Applicazione) parse(json, Applicazione.class);
	}
	
	public Applicazione() {
	}
}
