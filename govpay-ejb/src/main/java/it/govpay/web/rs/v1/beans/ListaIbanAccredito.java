package it.govpay.web.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaIbanAccredito extends Lista {
	
	public ListaIbanAccredito(List<Iban> iban, URI requestUri, long totalCount, long offset, long limit) {
		super(requestUri, iban.size(), totalCount, offset, limit);
		this.setIban(iban);
	}
	
	public List<Iban> getIban() {
		return iban;
	}

	public void setIban(List<Iban> iban) {
		this.iban = iban;
	}

	private List<Iban> iban;
	
}
