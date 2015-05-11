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
package it.govpay.orm.posizionedebitoria;

import it.govpay.orm.BaseEntity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the JLTDEPD database table.
 */
@Entity
@Table(name = "destinatari")
@NamedQueries({
//		@NamedQuery(name = "getDebitoriPendenzaByIdPend", query = "select coDestinatario from DestinatarioPendenza where pendenza.idPendenza=:idPendenza and tiDestinatario <> '"
//				+ DestinatarioPendenza.TIPO_DEST_DELEGATO + "' "),
//		@NamedQuery(name = "listByCodiceFiscale", query = "select dest from DestinatarioPendenza dest where coDestinatario=:coDestinatario "),
//		@NamedQuery(name = "listByCodiceFiscaleAndStatoPendenza", query = "select dest from DestinatarioPendenza dest where coDestinatario=:coDestinatario "
//				+ "and dest.pendenza.stPendenza = :statoPendenza                                  "),
//		@NamedQuery(name = "listByCodiceFiscaleStatoAndDate", query = "select dest from DestinatarioPendenza dest where coDestinatario=:coDestinatario "
//				+ "and (dest.pendenza.stPendenza = :statoPendenza    or :statoPendenza is null)   "
//				+ "and (dest.pendenza.tsDecorrenza = :dataIni        or dest.pendenza.tsDecorrenza is null)  "
//				+ "and (dest.pendenza.tsPrescrizione = :dataFin          or :dataFin is null)         ") 
})
public class DestinatarioPendenza extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String TIPO_DEST_DELEGATO = "DE";

	private String idDestinatario;
	private String coDestinatario;
	private String deDestinatario;
	private int prVersione;
	private String stRiga;
	private String tiDestinatario;
	private Timestamp tsDecorrenza;
	private Pendenza pendenza;
	
	private DatiAnagraficiDestinatario datiAnagraficiDestinatario;
	
	
	// bi-directional many-to-one association to Pendenza
	@ManyToOne
	@JoinColumn(name = "ID_PENDENZA", nullable = false)
	public Pendenza getPendenza() {
		return pendenza;
	}

	public void setPendenza(Pendenza pendenza) {
		this.pendenza = pendenza;
	}

	public DestinatarioPendenza() {
	}

	@Id
	@Column(name = "ID_DESTINATARIO", unique = true, nullable = false, length = 40)
	public String getIdDestinatario() {
		return this.idDestinatario;
	}

	public void setIdDestinatario(String idDestinatario) {
		this.idDestinatario = idDestinatario;
	}

	@Column(name = "CO_DESTINATARIO", nullable = false, length = 70)
	public String getCoDestinatario() {
		return this.coDestinatario;
	}

	public void setCoDestinatario(String coDestinatario) {
		this.coDestinatario = coDestinatario;
	}

	@Column(name = "DE_DESTINATARIO", length = 140)
	public String getDeDestinatario() {
		return this.deDestinatario;
	}

	public void setDeDestinatario(String deDestinatario) {
		this.deDestinatario = deDestinatario;
	}

	@Column(name = "PR_VERSIONE", nullable = false)
	public int getPrVersione() {
		return this.prVersione;
	}

	public void setPrVersione(int prVersione) {
		this.prVersione = prVersione;
	}

	@Column(name = "ST_RIGA", nullable = false, length = 2)
	public String getStRiga() {
		return this.stRiga;
	}

	public void setStRiga(String stRiga) {
		this.stRiga = stRiga;
	}

	@Column(name = "TI_DESTINATARIO", nullable = false, length = 4)
	public String getTiDestinatario() {
		return this.tiDestinatario;
	}

	public void setTiDestinatario(String tiDestinatario) {
		this.tiDestinatario = tiDestinatario;
	}

	@Column(name = "TS_DECORRENZA", nullable = false)
	public Timestamp getTsDecorrenza() {
		return this.tsDecorrenza;
	}

	public void setTsDecorrenza(Timestamp tsDecorrenza) {
		this.tsDecorrenza = tsDecorrenza;
	}

	@OneToOne(mappedBy = "destinatario", fetch = FetchType.LAZY)
	public DatiAnagraficiDestinatario getDatiAnagraficiDestinatario() {
		return datiAnagraficiDestinatario;
	}

	public void setDatiAnagraficiDestinatario(DatiAnagraficiDestinatario datiAnagraficiDestinatario) {
		this.datiAnagraficiDestinatario = datiAnagraficiDestinatario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idDestinatario == null) ? 0 : idDestinatario.hashCode());
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
		DestinatarioPendenza other = (DestinatarioPendenza) obj;
		if (idDestinatario == null) {
			if (other.idDestinatario != null)
				return false;
		} else if (!idDestinatario.equals(other.idDestinatario))
			return false;
		return true;
	}
}