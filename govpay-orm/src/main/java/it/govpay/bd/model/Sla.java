/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.bd.model;


/**
 * Modello di uno SLA
 */
public class Sla extends BasicModel {	
	
	private static final long serialVersionUID = 1L;
	private Long id; 	

	private String descrizione;
	private String tipoEventoIniziale;
	private String sottotipoEventoIniziale;
	private String tipoEventoFinale;
	private String sottotipoEventoFinale;
	private long tempoA;
	private long tempoB;
	private double tolleranzaA;
	private double tolleranzaB;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getTipoEventoIniziale() {
		return tipoEventoIniziale;
	}

	public void setTipoEventoIniziale(String tipoEventoIniziale) {
		this.tipoEventoIniziale = tipoEventoIniziale;
	}

	public String getSottotipoEventoIniziale() {
		return sottotipoEventoIniziale;
	}

	public void setSottotipoEventoIniziale(String sottotipoEventoIniziale) {
		this.sottotipoEventoIniziale = sottotipoEventoIniziale;
	}

	public String getTipoEventoFinale() {
		return tipoEventoFinale;
	}

	public void setTipoEventoFinale(String tipoEventoFinale) {
		this.tipoEventoFinale = tipoEventoFinale;
	}

	public String getSottotipoEventoFinale() {
		return sottotipoEventoFinale;
	}

	public void setSottotipoEventoFinale(String sottotipoEventoFinale) {
		this.sottotipoEventoFinale = sottotipoEventoFinale;
	}

	public long getTempoA() {
		return tempoA;
	}

	public void setTempoA(long tempoA) {
		this.tempoA = tempoA;
	}

	public long getTempoB() {
		return tempoB;
	}

	public void setTempoB(long tempoB) {
		this.tempoB = tempoB;
	}

	public double getTolleranzaA() {
		return tolleranzaA;
	}

	public void setTolleranzaA(double tolleranzaA) {
		this.tolleranzaA = tolleranzaA;
	}

	public double getTolleranzaB() {
		return tolleranzaB;
	}

	public void setTolleranzaB(double tolleranzaB) {
		this.tolleranzaB = tolleranzaB;
	}

    /**
     * Verifica l'equivalenza tra due PSP
     * Nel controllo non verifica la chiave fisica!!
     */
	@Override
	public boolean equals(Object obj) {
		Sla psp = null;
		if(obj instanceof Sla) {
			psp = (Sla) obj;
		} else {
			return false;
		}

		boolean equal =
			equals(descrizione, psp.getDescrizione()) &&
			equals(tipoEventoIniziale, psp.getTipoEventoIniziale()) &&
			equals(sottotipoEventoIniziale, psp.getSottotipoEventoIniziale()) &&
			equals(tipoEventoFinale, psp.getTipoEventoFinale()) &&
			equals(sottotipoEventoFinale, psp.getSottotipoEventoFinale()) &&
			equals(tempoA, psp.getTempoA()) &&
			equals(tempoB, psp.getTempoB()) &&
			equals(tolleranzaA, psp.getTolleranzaA()) &&
			equals(tolleranzaB, psp.getTolleranzaB());
		
		return equal;
	}
	
	@Override
	public String toString() {
		String txt =  "\n[Id: " + id + "]"
			 + "\n[descrizione: " + descrizione + "]"
		     + "\n[tipoEventoIniziale: " + tipoEventoIniziale + "]"
			 + "\n[sottotipoEventoIniziale: " + sottotipoEventoIniziale + "]"
			 + "\n[tipoEventoFinale: " + tipoEventoFinale + "]"
			 + "\n[sottotipoEventoFinale: " + sottotipoEventoFinale + "]"
			 + "\n[tempoA: " + tempoA + "]"
			 + "\n[tempoB: " + tempoB + "]"
			 + "\n[tolleranzaA: " + tolleranzaA + "]"
			 + "\n[tolleranzaB: " + tolleranzaB + "]";
		
		return txt;
	}
	
}
