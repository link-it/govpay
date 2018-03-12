
package it.govpay.core.rs.v1.beans;

public class PagamentoPost extends it.govpay.core.rs.v1.beans.base.PagamentoPost {

	public PagamentoPost() {}
	
	@Override
	public String getJsonIdFilter() {
		return "pagamentiPost";
	}
	
}
