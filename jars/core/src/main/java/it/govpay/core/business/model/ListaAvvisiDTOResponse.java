package it.govpay.core.business.model;

import java.util.ArrayList;
import java.util.List;

import it.govpay.model.avvisi.AvvisoPagamento;

public class ListaAvvisiDTOResponse {

	private List<AvvisoPagamento> avvisi;
	
	public ListaAvvisiDTOResponse() {
		this.avvisi = new ArrayList<>();
	}

	public List<AvvisoPagamento> getAvvisi() {
		return this.avvisi;
	}

	public void setAvvisi(List<AvvisoPagamento> avvisi) {
		this.avvisi = avvisi;
	}
}
