package it.govpay.model;

import java.util.List;

public class Contabilita {
	
	private List<QuotaContabilita> quote;
	private String proprietaCustom;
	
	public List<QuotaContabilita> getQuote() {
		return quote;
	}
	public void setQuote(List<QuotaContabilita> quote) {
		this.quote = quote;
	}
	public String getProprietaCustom() {
		return proprietaCustom;
	}
	public void setProprietaCustom(String proprietaCustom) {
		this.proprietaCustom = proprietaCustom;
	} 
	
	
}