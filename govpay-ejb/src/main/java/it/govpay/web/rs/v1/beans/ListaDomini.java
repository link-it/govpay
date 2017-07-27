package it.govpay.web.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaDomini extends Lista {
	
	public ListaDomini(List<Dominio> domini, URI requestUri, long totalCount, long offset, long limit) {
		super(requestUri, domini.size(), totalCount, offset, limit);
		this.domini = domini;
	}
	
	private List<Dominio> domini;
	
	public List<Dominio> getDomini() {
		return domini;
	}
	public void setDomini(List<Dominio> domini) {
		this.domini = domini;
	}
	
}
