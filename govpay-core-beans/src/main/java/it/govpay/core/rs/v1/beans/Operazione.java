package it.govpay.core.rs.v1.beans;

public class Operazione extends it.govpay.core.rs.v1.beans.base.Operazione{

	public Operazione() {}

	@Override
	public String getJsonIdFilter() {
		return "operazione";
	}

	public static Operazione parse(String json) {
		return (Operazione) parse(json, Operazione.class);
	}
}