package it.govpay.core.business.anagrafica.dto;

public class BasicFindResponseDTO {
	
	private long totalResults;
	
	public BasicFindResponseDTO(long totalResults) {
		this.totalResults = totalResults;
	}

	public long getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}



}
