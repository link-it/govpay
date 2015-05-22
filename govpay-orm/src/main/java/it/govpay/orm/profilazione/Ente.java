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

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "enti")
@NamedQueries({
	@NamedQuery(name = "getEnteFromLapl", query = "select e from Ente e where e.intestatario.lapl = :lapl")
})
public class Ente extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/*** Persistent Properties ***/
	private String idEnte;
	private String codiceEnte;
	private String denominazione;
	private Intestatario intestatario;
	private int prVersione;
	private String provincia;
	private String stato;
	private Integer maxNumTributi;

	/*** Persistent Reference ***/
	private CategoriaEnte tipoEnte;
	private Dominio dominio;

	private Set<TributoEnte> tributiEnte;

	@Id
	@Column(name = "ID_ENTE")
	public String getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(String idEnte) {
		this.idEnte = idEnte;
	}

	@Column(name = "MAX_NUM_TRIBUTI")
	public Integer getMaxNumTributi() {
		return maxNumTributi;
	}

	public void setMaxNumTributi(Integer maxNumTributi) {
		this.maxNumTributi = maxNumTributi;
	}

	@Column(name = "CD_ENTE")
	public String getCodiceEnte() {
		return codiceEnte;
	}

	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}

	@Column(name = "DENOM")
	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	@Column(name = "PR_VERSIONE")
	public int getPrVersione() {
		return prVersione;
	}

	public void setPrVersione(int prVersione) {
		this.prVersione = prVersione;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	@ManyToOne(targetEntity = CategoriaEnte.class)
	@JoinColumn(name = "TP_ENTE")
	public CategoriaEnte getTipoEnte() {
		return tipoEnte;
	}

	public void setTipoEnte(CategoriaEnte tipoEnte) {
		this.tipoEnte = (CategoriaEnte) tipoEnte;
	}

	@OneToOne(targetEntity = Intestatario.class, optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "INTESTATARIO")
	public Intestatario getIntestatario() {
		return intestatario;
	}

	public void setIntestatario(Intestatario intestatario) {
		this.intestatario = intestatario;
	}
	
	
	@OneToOne(targetEntity = Dominio.class, mappedBy = "ente", cascade = CascadeType.REMOVE)
	public Dominio getDominio() {
		return dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	@OneToMany(targetEntity = TributoEnte.class, mappedBy = "ente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	// TODO: ,orphanRemoval=true)
	public Set<TributoEnte> getTributiEnte() {
		return tributiEnte;
	}

	public void setTributiEnte(Set<TributoEnte> tributiEnte) {
		this.tributiEnte = tributiEnte;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Ente [getIdEnte()=");
		builder.append(getIdEnte());
		builder.append(", getMaxNumTributi()=");
		builder.append(getMaxNumTributi());
		builder.append(", getCodiceEnte()=");
		builder.append(getCodiceEnte());
		builder.append(", getDenominazione()=");
		builder.append(getDenominazione());
		builder.append(", getPrVersione()=");
		builder.append(getPrVersione());
		builder.append(", getProvincia()=");
		builder.append(getProvincia());
		builder.append(", getStato()=");
		builder.append(getStato());
		builder.append(", getTipoEnte()=");
		builder.append(getTipoEnte());
//		builder.append(", getIntestatario()=");
//		builder.append(getIntestatario());
//		builder.append(", getTributiEnte()=");
//		builder.append(getTributiEnte());
		builder.append(", getVersion()=");
		builder.append(getVersion());
		builder.append(", getOpInserimento()=");
		builder.append(getOpInserimento());
		builder.append(", getOpAggiornamento()=");
		builder.append(getOpAggiornamento());
		builder.append(", getTsInserimento()=");
		builder.append(getTsInserimento());
		builder.append(", getTsAggiornamento()=");
		builder.append(getTsAggiornamento());
		builder.append("]");
		return builder.toString();
	}

	
	
	
}
