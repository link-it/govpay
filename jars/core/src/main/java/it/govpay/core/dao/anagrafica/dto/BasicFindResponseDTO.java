package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

public class BasicFindResponseDTO<T> {
	
	private long totalResults;
	private List<T> results;
	
	public BasicFindResponseDTO(long totalResults, List<T> results) {
		this.totalResults = totalResults;
		this.results = results;
	}

	public long getTotalResults() {
		return this.totalResults;
	}

	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}

	public List<T> getResults() {
		return this.results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}



}
