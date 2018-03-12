
package it.govpay.core.rs.v1.beans;

public class Pendenza extends it.govpay.core.rs.v1.beans.base.Pendenza {

	public Pendenza() {}
	
	@Override
	public String getJsonIdFilter() {
		return "pendenze";
	}
}
