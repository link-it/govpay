package it.govpay.web.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaEntrate extends Lista {
	
	public ListaEntrate(List<Entrata> entrate, URI requestUri, long totalCount, long offset, long limit) {
		super(requestUri, entrate.size(), totalCount, offset, limit);
		this.setEntrate(entrate);
	}
	
	private List<Entrata> entrate;
	
	public List<Entrata> getEntrate() {
		return entrate;
	}

	public void setEntrate(List<Entrata> entrate) {
		this.entrate = entrate;
	}


	
	
}
