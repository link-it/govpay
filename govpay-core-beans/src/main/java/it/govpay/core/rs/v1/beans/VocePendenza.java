
package it.govpay.core.rs.v1.beans;

public class VocePendenza extends it.govpay.core.rs.v1.beans.base.VocePendenza {

	public VocePendenza() {}
	
	@Override
	public String getJsonIdFilter() {
		return "vociPendenze";
	}


}
