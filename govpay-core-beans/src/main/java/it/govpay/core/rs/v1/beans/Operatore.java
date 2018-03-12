package it.govpay.core.rs.v1.beans;

public class Operatore  extends it.govpay.core.rs.v1.beans.base.Operatore{

	public Operatore() {}

	@Override
	public String getJsonIdFilter() {
		return "operatore";
	}

	public static Operatore parse(String json) {
		return (Operatore) parse(json, Operatore.class);
	}
}
