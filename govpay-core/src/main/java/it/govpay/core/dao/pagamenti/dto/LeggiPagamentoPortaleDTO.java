package it.govpay.core.dao.pagamenti.dto;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.model.IAutorizzato;

public class LeggiPagamentoPortaleDTO extends BasicCreateRequestDTO {

	/**
	 * @param user
	 */
	public LeggiPagamentoPortaleDTO(IAutorizzato user) {
		super(user);
	}
	
	private Long id;
	private String principal;
	private String idSessione;
	
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
