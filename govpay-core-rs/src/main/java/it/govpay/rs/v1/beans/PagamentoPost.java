
package it.govpay.rs.v1.beans;

public class PagamentoPost extends it.govpay.rs.v1.beans.base.PagamentoPost {

	public PagamentoPost() {}
	
	@Override
	public String getJsonIdFilter() {
		return "pagamentiPost";
	}
	
}
