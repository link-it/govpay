package it.govpay.core.dao.reportistica.dto;

import java.util.List;

import it.govpay.bd.viste.model.EntrataPrevista;
import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaEntratePrevisteDTOResponse extends BasicFindResponseDTO<EntrataPrevista> {
	
	private byte[] pdf;

	public ListaEntratePrevisteDTOResponse(long totalResults, List<EntrataPrevista> results) {
		super(totalResults, results);
	}

	public byte[] getPdf() {
		return pdf;
	}

	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}

}
