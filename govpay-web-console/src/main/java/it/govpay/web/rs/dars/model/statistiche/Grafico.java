package it.govpay.web.rs.dars.model.statistiche;

import java.util.ArrayList;
import java.util.List;

public class Grafico {
	
	public enum TipoGrafico {pie, bar, line} 

	private String titolo;
	private String sottotitolo;
	private String labelX;
	private String labelY;
	private TipoGrafico tipo;
	private List<Serie<?>> serie;
	private List<String> colori;
	private List<String> categorie;
	private List<String> valoriX;	
	
	public Grafico(TipoGrafico tipo) {
		this.serie = new ArrayList<Serie<?>>();
		this.colori = new ArrayList<String>();
		this.categorie = new ArrayList<String>();
		this.valoriX = new ArrayList<String>();
		this.tipo = tipo;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getSottotitolo() {
		return sottotitolo;
	}

	public void setSottotitolo(String sottotitolo) {
		this.sottotitolo = sottotitolo;
	}

	public String getLabelX() {
		return labelX;
	}

	public void setLabelX(String labelX) {
		this.labelX = labelX;
	}

	public String getLabelY() {
		return labelY;
	}

	public void setLabelY(String labelY) {
		this.labelY = labelY;
	}

	public TipoGrafico getTipo() {
		return tipo;
	}

	public void setTipo(TipoGrafico tipo) {
		this.tipo = tipo;
	}

	public List<Serie<?>> getSerie() {
		return serie;
	}

	public List<String> getColori() {
		return colori;
	}

	public void setColori(List<String> colori) {
		this.colori = colori;
	}

	public List<String> getCategorie() {
		return categorie;
	}

	public void setCategorie(List<String> categorie) {
		this.categorie = categorie;
	}

	public List<String> getValoriX() {
		return valoriX;
	}

	public void setValoriX(List<String> valoriX) {
		this.valoriX = valoriX;
	}
	
	
}
