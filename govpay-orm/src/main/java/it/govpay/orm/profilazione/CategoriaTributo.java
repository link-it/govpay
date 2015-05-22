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
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "categorie_tributi")
public class CategoriaTributo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/*** Persistent Properties ***/
	private String idTributo;
	private String deTrb;
	private String cdAde;
	private String tpEntrata;
	private String flPredeterm;
	private String flIniziativa;
	private String stato;
	private String soggEsclusi;
	private int prVersione;
	private String cdPagamentoSpontaneo;

	/*** Persistent Collections ***/
	private Set<TributoEnte> entiTributi;

	public CategoriaTributo() {
	}

	public CategoriaTributo(String idTributo, int prVersione, String opInserimento, Timestamp tsInserimento) {
		this.idTributo = idTributo;
		this.prVersione = prVersione;
		super.setOpInserimento(opInserimento);
		super.setTsInserimento(tsInserimento);
	}

	public CategoriaTributo(String idTributo, String deTrb, String cdAde, String tpEntrata, String flPredeterm,
			String flIniziativa, String stato, String soggEsclusi, int prVersione, String opInserimento,
			Timestamp tsInserimento, String opAggiornamento, Timestamp tsAggiornamento) {
		this.idTributo = idTributo;
		this.deTrb = deTrb;
		this.cdAde = cdAde;
		this.tpEntrata = tpEntrata;
		this.flPredeterm = flPredeterm;
		this.flIniziativa = flIniziativa;
		this.stato = stato;
		this.soggEsclusi = soggEsclusi;
		this.prVersione = prVersione;
		super.setOpInserimento(opInserimento);
		super.setTsInserimento(tsInserimento);
		super.setOpAggiornamento(opAggiornamento);
		super.setTsAggiornamento(tsAggiornamento);
	}

	@Id
	@Column(name = "ID_TRIBUTO")
	public String getIdTributo() {
		return this.idTributo;
	}

	public void setIdTributo(String idTributo) {
		this.idTributo = idTributo;
	}

	@Column(name = "DE_TRB")
	public String getDeTrb() {
		return this.deTrb;
	}

	public void setDeTrb(String deTrb) {
		this.deTrb = deTrb;
	}

	@Column(name = "CD_ADE")
	public String getCdAde() {
		return this.cdAde;
	}

	public void setCdAde(String cdAde) {
		this.cdAde = cdAde;
	}

	@Column(name = "TP_ENTRATA")
	public String getTpEntrata() {
		return this.tpEntrata;
	}

	public void setTpEntrata(String tpEntrata) {
		this.tpEntrata = tpEntrata;
	}

	@Column(name = "FL_PREDETERM")
	public String getFlPredeterm() {
		return this.flPredeterm;
	}

	public void setFlPredeterm(String flPredeterm) {
		this.flPredeterm = flPredeterm;
	}

	@Column(name = "FL_INIZIATIVA")
	public String getFlIniziativa() {
		return this.flIniziativa;
	}

	public void setFlIniziativa(String flIniziativa) {
		this.flIniziativa = flIniziativa;
	}

	@Column(name = "STATO")
	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	@Column(name = "SOGG_ESCLUSI")
	public String getSoggEsclusi() {
		return this.soggEsclusi;
	}

	public void setSoggEsclusi(String soggEsclusi) {
		this.soggEsclusi = soggEsclusi;
	}

	@Column(name = "PR_VERSIONE")
	public int getPrVersione() {
		return this.prVersione;
	}

	public void setPrVersione(int prVersione) {
		this.prVersione = prVersione;
	}

	@Column(name = "CDPAGAMENTOSPONTANEO")
	public String getCdPagamentoSpontaneo() {
		return cdPagamentoSpontaneo;
	}

	public void setCdPagamentoSpontaneo(String cdPagamentoSpontaneo) {
		this.cdPagamentoSpontaneo = cdPagamentoSpontaneo;
	}

	@OneToMany(targetEntity = TributoEnte.class, mappedBy = "categoria", fetch = FetchType.LAZY)
	public Set<TributoEnte> getEntiTributi() {
		return entiTributi;
	}

	public void setEntiTributi(Set<TributoEnte> entiTributi) {
		this.entiTributi = entiTributi;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idTributo == null) ? 0 : idTributo.hashCode());
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
		CategoriaTributo other = (CategoriaTributo) obj;
		if (idTributo == null) {
			if (other.idTributo != null)
				return false;
		} else if (!idTributo.equals(other.idTributo))
			return false;
		return true;
	}

}
