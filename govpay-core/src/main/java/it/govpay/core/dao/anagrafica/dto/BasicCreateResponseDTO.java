package it.govpay.core.dao.anagrafica.dto;

public abstract class BasicCreateResponseDTO {
	
	private boolean created;

	public boolean isCreated() {
		return this.created;
	}

	public void setCreated(boolean created) {
		this.created = created;
	} 
}
