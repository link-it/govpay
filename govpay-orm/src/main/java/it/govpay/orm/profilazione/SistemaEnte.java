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

import it.govpay.orm.BaseEntity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sil")
public class SistemaEnte extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/*** PK Reference ***/
	private SistemaEnteId sisEntId;

	/*** Persistent Properties ***/
	private String deSystem;
	private String stato;
	private int prVersione;
	
	private Long idConnettoreNotifica;
	private Long idConnettorePagAttesa;
	
	private StazioneIntermediario stazioneIntermediario;

	public SistemaEnte() {
		this.sisEntId = new SistemaEnteId();
	}

	public SistemaEnte(SistemaEnteId id) {
		this.sisEntId = id;
	}

	public SistemaEnte(String idEnte, String idSystem, int prVersione, String opInserimento, Timestamp tsInserimento) {
		this.sisEntId.setIdEnte(idEnte);
		this.sisEntId.setIdSystem(idSystem);
		this.prVersione = prVersione;
		super.setOpInserimento(opInserimento);
		super.setTsInserimento(tsInserimento);

	}

	public SistemaEnte(String idEnte, String idSystem, String deSystem, String stato, int prVersione, String opInserimento, Timestamp tsInserimento,
			String opAggiornamento, Timestamp tsAggiornamento) {
		this.sisEntId.setIdEnte(idEnte);
		this.sisEntId.setIdSystem(idSystem);
		this.deSystem = deSystem;
		this.stato = stato;
		this.prVersione = prVersione;
		super.setOpInserimento(opInserimento);
		super.setTsInserimento(tsInserimento);
		super.setOpAggiornamento(opAggiornamento);
		super.setTsAggiornamento(tsAggiornamento);
	}

	@Id
	public SistemaEnteId getSisEntId() {
		return sisEntId;
	}

	public void setSisEntId(SistemaEnteId sisEntId) {
		this.sisEntId = sisEntId;
	}

	@Column(name = "DE_SYSTEM")
	public String getDeSystem() {
		return this.deSystem;
	}

	public void setDeSystem(String deSystem) {
		this.deSystem = deSystem;
	}

	@Column(name = "STATO")
	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	@Column(name = "PR_VERSIONE")
	public int getPrVersione() {
		return this.prVersione;
	}

	public void setPrVersione(int prVersione) {
		this.prVersione = prVersione;
	}

	@Column(name = "ID_CONNETTORE_NOTIFICA")
	public Long getIdConnettoreNotifica() {
		return idConnettoreNotifica;
	}

	public void setIdConnettoreNotifica(Long idConnettoreNotifica) {
		this.idConnettoreNotifica = idConnettoreNotifica;
	}

	@Column(name = "ID_CONNETTORE_PAGATTESA")
	public Long getIdConnettorePagAttesa() {
		return idConnettorePagAttesa;
	}

	public void setIdConnettorePagAttesa(Long idConnettorePagAttesa) {
		this.idConnettorePagAttesa = idConnettorePagAttesa;
	}
	
	@ManyToOne(targetEntity = StazioneIntermediario.class)
	@JoinColumn(name = "ID_STAZIONE") //, insertable = false, updatable = false)
	public StazioneIntermediario getStazioneIntermediario() {
		return stazioneIntermediario;
	}

	public void setStazioneIntermediario(StazioneIntermediario stazioneIntermediario) {
		this.stazioneIntermediario = stazioneIntermediario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sisEntId == null) ? 0 : sisEntId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SistemaEnte other = (SistemaEnte) obj;
		if (sisEntId == null) {
			if (other.sisEntId != null)
				return false;
		} else if (!sisEntId.equals(other.sisEntId))
			return false;
		return true;
	}

}
