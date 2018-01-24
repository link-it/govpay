package it.govpay.core.dao.pagamenti.dto;

public class LeggiPagamentoPortaleDTO {

	private Long id= null;
	private String principal = null;
	private String idSessione = null;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getIdSessione() {
		return idSessione;
	}
	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}
}
