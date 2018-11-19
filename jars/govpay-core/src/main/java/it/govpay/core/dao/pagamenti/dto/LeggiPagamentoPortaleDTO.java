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
	
	private String id;
	private String idSessionePsp;
	private boolean risolviLink = false;
	
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdSessionePsp() {
		return this.idSessionePsp;
	}
	public void setIdSessionePsp(String idSessionePsp) {
		this.idSessionePsp = idSessionePsp;
	}
	public boolean isRisolviLink() {
		return this.risolviLink;
	}
	public void setRisolviLink(boolean risolviLink) {
		this.risolviLink = risolviLink;
	}
}
