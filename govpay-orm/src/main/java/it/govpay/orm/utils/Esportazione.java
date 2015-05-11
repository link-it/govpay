/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.orm.utils;

import it.govpay.orm.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "esportazioni")
public class Esportazione extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Prenotazione prenotazione;
	private String nomeFile;
	private String formato;
	private String compressione;
	private Long lenDati;

	private byte[] dati;
	/*** Auto Generated Identity Property ***/
	private Long id;

	@ManyToOne(targetEntity = Prenotazione.class)
	@JoinColumn(name = "ID_PRENOTAZIONE")
	public Prenotazione getPrenotazione() {
		return this.prenotazione;
	}

	public void setPrenotazione(Prenotazione prenotazione) {
		this.prenotazione = prenotazione;
	}

	@Column(name = "NOME_FILE")
	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	@Column(name = "FORMATO")
	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	@Column(name = "COMPRESSIONE")
	public String getCompressione() {
		return compressione;
	}

	public void setCompressione(String compressione) {
		this.compressione = compressione;
	}

	@Column(name = "LEN_DATI")
	public Long getLenDati() {
		return lenDati;
	}

	public void setLenDati(Long lenDati) {
		this.lenDati = lenDati;
	}

	@Lob()
	public byte[] getDati() {
		return dati;
	}

	public void setDati(byte[] dati) {
		this.dati = dati;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}