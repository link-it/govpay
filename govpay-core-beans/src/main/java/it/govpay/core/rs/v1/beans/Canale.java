package it.govpay.core.rs.v1.beans;

public class Canale extends it.govpay.core.rs.v1.beans.base.Canale {

	@Override
	public String getJsonIdFilter() {
		return "canale";
	}
	
	public static Canale parse(String json) {
		return (Canale) parse(json, Canale.class);
	}
	
	public Canale() {
	}
	
}
