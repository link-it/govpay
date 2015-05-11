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
package it.govpay.orm.profilazione;

import java.util.Set;

import it.govpay.orm.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "intermediari")
public class Intermediario extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String idIntermediario;
	private String nomeSoggettoSPC;

	private Long idConnettorePDD;

	private Set<StazioneIntermediario> stazioni;

	@Id
	@Column(name = "ID_INTERMEDIARIO")
	public String getIdIntermediario() {
		return idIntermediario;
	}

	public void setIdIntermediario(String idIntermediario) {
		this.idIntermediario = idIntermediario;
	}

	@Column(name = "ID_CONNETTORE_PDD")
	public Long getIdConnettorePDD() {
		return idConnettorePDD;
	}

	public void setIdConnettorePDD(Long idConnettorePDD) {
		this.idConnettorePDD = idConnettorePDD;
	}

	@Column(name = "NOME_SOGGETTO_SPC")
	public String getNomeSoggettoSPC() {
		return nomeSoggettoSPC;
	}

	public void setNomeSoggettoSPC(String nomeSoggettoSPC) {
		this.nomeSoggettoSPC = nomeSoggettoSPC;
	}

	@OneToMany(mappedBy = "intermediario", fetch = FetchType.LAZY)
	public Set<StazioneIntermediario> getStazioni() {
		return stazioni;
	}

	public void setStazioni(Set<StazioneIntermediario> stazioni) {
		this.stazioni = stazioni;
	}

}
