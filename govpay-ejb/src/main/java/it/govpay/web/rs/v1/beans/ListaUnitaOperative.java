package it.govpay.web.rs.v1.beans;

import java.net.URI;
import java.util.List;

public class ListaUnitaOperative extends Lista {
	
	public ListaUnitaOperative(List<UnitaOperativa> unitaOperative, URI requestUri, long totalCount, long offset, long limit) {
		super(requestUri, unitaOperative.size(), totalCount, offset, limit);
		this.setUnitaOperative(unitaOperative);
	}
	
	private List<UnitaOperativa> unitaOperative;
	
	public List<UnitaOperativa> getUnitaOperative() {
		return unitaOperative;
	}

	public void setUnitaOperative(List<UnitaOperativa> unitaOperative) {
		this.unitaOperative = unitaOperative;
	}
	
}
