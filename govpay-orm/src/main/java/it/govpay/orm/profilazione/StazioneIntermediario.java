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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "stazioni_intermediario")
public class StazioneIntermediario extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String idStazione;
	private String idIntermediario;
	private String password;
	
	private Intermediario intermediario;
	private Set<SistemaEnte> sistemiEnte;

	@Id
	@Column(name = "ID_STAZIONE")
	public String getIdStazione() {
		return idStazione;
	}

	public void setIdStazione(String idStazione) {
		this.idStazione = idStazione;
	}

	@Column(name = "ID_INTERMEDIARIO")
	public String getIdIntermediario() {
		return idIntermediario;
	}

	public void setIdIntermediario(String idIntermediario) {
		this.idIntermediario = idIntermediario;
	}

	@ManyToOne(targetEntity = Intermediario.class)
	@JoinColumn(name = "ID_INTERMEDIARIO", insertable = false, updatable = false)
	public Intermediario getIntermediario() {
		return intermediario;
	}

	public void setIntermediario(Intermediario intermediario) {
		this.intermediario = intermediario;
	}
	
	@OneToMany(mappedBy = "stazioneIntermediario", fetch = FetchType.LAZY)
	public Set<SistemaEnte> getSistemiEnte() {
		return sistemiEnte;
	}

	public void setSistemiEnte(Set<SistemaEnte> sistemiEnte) {
		this.sistemiEnte = sistemiEnte;
	}
	
	public SistemaEnte getSistemaEnte(Dominio dominio) {
		for(SistemaEnte sistema : getSistemiEnte()) {
			if(dominio.getEnte().getIdEnte().equals(dominio.getEnte().getIdEnte())) 
				return sistema;
		}
		return null;
	}

	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
