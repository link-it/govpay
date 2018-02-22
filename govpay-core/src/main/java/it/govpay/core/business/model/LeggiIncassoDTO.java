package it.govpay.core.business.model;

public class LeggiIncassoDTO {
	
	private String principal;
	private String trn;
	private Long id;
	
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getTrn() {
		return trn;
	}
	public void setTrn(String trn) {
		this.trn = trn;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id= id;
	}

}
