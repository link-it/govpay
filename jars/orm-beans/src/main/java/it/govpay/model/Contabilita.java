package it.govpay.model;

import java.util.List;

public class Contabilita {
	
	private List<QuotaContabilita> quote;
	private Object proprietaCustom;
	
	public List<QuotaContabilita> getQuote() {
		return quote;
	}
	public void setQuote(List<QuotaContabilita> quote) {
		this.quote = quote;
	}
	public Object getProprietaCustom() {
		return proprietaCustom;
	}
	public void setProprietaCustom(Object proprietaCustom) {
		this.proprietaCustom = proprietaCustom;
	} 
	
	
}