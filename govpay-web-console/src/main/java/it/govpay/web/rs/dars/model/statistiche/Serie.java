package it.govpay.web.rs.dars.model.statistiche;

import java.util.ArrayList;
import java.util.List;

public class Serie<T> {

	private List<T> dati;
	private List<String> tooltip;

	public Serie() {
		this.dati = new ArrayList<T>();
		this.tooltip = new ArrayList<String>();
	}

	public List<T> getDati() {
		return dati;
	}

	public List<String> getTooltip() {
		return tooltip;
	}
}
