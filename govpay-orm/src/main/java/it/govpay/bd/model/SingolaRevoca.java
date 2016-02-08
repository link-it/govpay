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


public class SingolaRevoca extends BasicModel{
	private static final long serialVersionUID = 1L;
	
	
	public enum Stato {
		SINGOLA_REVOCA_REVOCATA,
		SINGOLA_REVOCA_NON_REVOCATA
	}
	
	private Long id;
	private Long idSingoloVersamento;
	private long idEr;
	private String causaleRevoca;
	private String datiAggiuntiviRevoca;
	private double singoloImporto;
	private Double singoloImportoRevocato;
	private String causaleEsito;
	private String datiAggiuntiviEsito;
	private Stato stato;
	private String descrizioneStato;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Stato getStato() {
		return stato;
	}
	public void setStato(Stato stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	
	@Override
	public boolean equals(Object obj) {
		SingolaRevoca singolaRevoca = null;
		if(obj instanceof SingolaRevoca) {
			singolaRevoca = (SingolaRevoca) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(idEr, singolaRevoca.getIdEr()) &&
				equals(idSingoloVersamento, singolaRevoca.getIdSingoloVersamento()) &&
				equals(causaleRevoca, singolaRevoca.getCausaleRevoca()) &&
				equals(datiAggiuntiviRevoca, singolaRevoca.getDatiAggiuntiviRevoca()) &&
				equals(singoloImporto, singolaRevoca.getSingoloImporto()) &&
				equals(singoloImportoRevocato, singolaRevoca.getSingoloImportoRevocato()) &&
				equals(causaleEsito, singolaRevoca.getCausaleEsito()) &&
				equals(datiAggiuntiviEsito, singolaRevoca.getDatiAggiuntiviEsito()) &&
				equals(stato, singolaRevoca.getStato()) &&
				equals(descrizioneStato, singolaRevoca.getDescrizioneStato());
		
		return equal;
	}
	public String getCausaleRevoca() {
		return causaleRevoca;
	}
	public void setCausaleRevoca(String causaleRevoca) {
		this.causaleRevoca = causaleRevoca;
	}
	public String getDatiAggiuntiviRevoca() {
		return datiAggiuntiviRevoca;
	}
	public void setDatiAggiuntiviRevoca(String datiAggiuntiviRevoca) {
		this.datiAggiuntiviRevoca = datiAggiuntiviRevoca;
	}
	public double getSingoloImporto() {
		return singoloImporto;
	}
	public void setSingoloImporto(double singoloImporto) {
		this.singoloImporto = singoloImporto;
	}
	public Double getSingoloImportoRevocato() {
		return singoloImportoRevocato;
	}
	public void setSingoloImportoRevocato(Double singoloImportoRevocato) {
		this.singoloImportoRevocato = singoloImportoRevocato;
	}
	public String getCausaleEsito() {
		return causaleEsito;
	}
	public void setCausaleEsito(String causaleEsito) {
		this.causaleEsito = causaleEsito;
	}
	public String getDatiAggiuntiviEsito() {
		return datiAggiuntiviEsito;
	}
	public void setDatiAggiuntiviEsito(String datiAggiuntiviEsito) {
		this.datiAggiuntiviEsito = datiAggiuntiviEsito;
	}
	public long getIdEr() {
		return idEr;
	}
	public void setIdEr(long idEr) {
		this.idEr = idEr;
	}
	public Long getIdSingoloVersamento() {
		return idSingoloVersamento;
	}
	public void setIdSingoloVersamento(Long idSingoloVersamento) {
		this.idSingoloVersamento = idSingoloVersamento;
	}

	
}
