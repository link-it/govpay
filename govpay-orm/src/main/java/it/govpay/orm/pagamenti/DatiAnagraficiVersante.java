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
package it.govpay.orm.pagamenti;

import it.govpay.orm.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dati_anagrafici_versante")
public class DatiAnagraficiVersante extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long id;
	private DistintaPagamento distintaPagamento;
	private char tipoSoggetto;
	private String anagrafica;
	private String indirizzo;
	private String numeroCivico;
	private String cap;
	private String localita;
	private String provincia;
	private String nazione;
	private String email;

	public DatiAnagraficiVersante() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DISTINTE_PAGAMENTO", nullable = false)
	public DistintaPagamento getDistintaPagamento() {
		return this.distintaPagamento;
	}

	public void setDistintaPagamento(DistintaPagamento distintePagamento) {
		this.distintaPagamento = distintePagamento;
	}

	@Column(name = "TIPO_SOGGETTO", nullable = false, length = 1)
	public char getTipoSoggetto() {
		return this.tipoSoggetto;
	}

	public void setTipoSoggetto(char tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}

	@Column(name = "ANAGRAFICA", length = 70)
	public String getAnagrafica() {
		return this.anagrafica;
	}

	public void setAnagrafica(String anagrafica) {
		this.anagrafica = anagrafica;
	}

	@Column(name = "INDIRIZZO", length = 70)
	public String getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	@Column(name = "NUMERO_CIVICO", length = 16)
	public String getNumeroCivico() {
		return this.numeroCivico;
	}

	public void setNumeroCivico(String numeroCivico) {
		this.numeroCivico = numeroCivico;
	}

	@Column(name = "CAP", length = 16)
	public String getCap() {
		return this.cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	@Column(name = "LOCALITA", length = 35)
	public String getLocalita() {
		return this.localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	@Column(name = "PROVINCIA", length = 35)
	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	@Column(name = "NAZIONE", length = 2)
	public String getNazione() {
		return this.nazione;
	}

	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	@Column(name = "EMAIL", length = 256)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
