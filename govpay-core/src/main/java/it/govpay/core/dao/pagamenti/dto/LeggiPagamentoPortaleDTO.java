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
	private String idSessione;
	private boolean risolviLink = false;
	
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIdSessione() {
		return this.idSessione;
	}
	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}
	public boolean isRisolviLink() {
		return this.risolviLink;
	}
	public void setRisolviLink(boolean risolviLink) {
		this.risolviLink = risolviLink;
	}
}
