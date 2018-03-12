package it.govpay.core.rs.v1.beans;

public class Psp extends it.govpay.core.rs.v1.beans.base.Psp {

	@Override
	public String getJsonIdFilter() {
		return "psp";
	}
	
	public static Psp parse(String json) {
		return (Psp) parse(json, Psp.class);
	}
	public Psp() {
	}
}

