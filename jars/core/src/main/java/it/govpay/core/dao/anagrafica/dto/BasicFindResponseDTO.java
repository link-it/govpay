package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

public class BasicFindResponseDTO<T> {
	
	private Long totalResults;
	private List<T> results;
	
	public BasicFindResponseDTO(Long totalResults, List<T> results) {
		this.totalResults = totalResults;
		this.results = results;
	}

	public Long getTotalResults() {
		return this.totalResults;
	}

	public void setTotalResults(Long totalResults) {
		this.totalResults = totalResults;
	}

	public List<T> getResults() {
		return this.results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}



}
