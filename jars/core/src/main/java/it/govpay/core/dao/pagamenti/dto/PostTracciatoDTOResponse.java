package it.govpay.core.dao.pagamenti.dto;

import it.govpay.bd.model.Tracciato;
import it.govpay.core.dao.anagrafica.dto.BasicCreateResponseDTO;

public class PostTracciatoDTOResponse extends BasicCreateResponseDTO {

	private Tracciato tracciato = null;

	public Tracciato getTracciato() {
		return tracciato;
	}

	public void setTracciato(Tracciato tracciato) {
		this.tracciato = tracciato;
	}
}
