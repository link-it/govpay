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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "operatori")
public class Operatore implements Serializable {

	private static final long serialVersionUID = 1L;

	/*** Persistent Properties ***/
	private String operatore;
	private String username;
	private Integer locked;
	private String mobile;
	private String signerCode;
	private String description;
	// private String email;
	private String name;
	private Integer numFailedlogon;
	private String password;
	private Timestamp lastLogon;
	private Date expirationDate;
	private Timestamp lockDate;
	private Timestamp failedLogonDate;
	private String codiceFiscale;
	private String surname;


	/**
	 * Riferimento all'intestatario che contiene i dati anagrfici di questo operatore.
	 */
	private Intestatario intestatario;

	/**
	 * Elenco di associazioni tra questo operatore e gli intestatari che pu� gestire.
	 */
	private Set<IntestatarioOperatore> intestatarioOperatore = new LinkedHashSet<IntestatarioOperatore>();

	@Id
	public String getOperatore() {
		return this.operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "BLOCCATO")
	public Integer getLocked() {
		return this.locked;
	}

	public void setLocked(Integer locked) {
		this.locked = locked;
	}

	@Column(name = "CELLULARE")
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "CODICEFIRMATARIO")
	public String getSignerCode() {
		return this.signerCode;
	}

	public void setSignerCode(String signerCode) {
		this.signerCode = signerCode;
	}

	@Column(name = "DESCRIZIONE")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// public String getEmail() {
	// return this.email;
	// }
	//
	// public void setEmail(String email) {
	// this.email = email;
	// }

	@Column(name = "NOME")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "NULL_COLL_FALL")
	public Integer getNumFailedlogon() {
		return this.numFailedlogon;
	}

	public void setNumFailedlogon(Integer numFailedlogon) {
		this.numFailedlogon = numFailedlogon;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "ULTIMOCOLLEGAMENTO")
	public Timestamp getLastLogon() {
		return this.lastLogon;
	}

	public void setLastLogon(Timestamp lastLogon) {
		this.lastLogon = lastLogon;
	}

	@Column(name = "DATASCADENZA")
	public Date getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Column(name = "DATABLOCCO")
	public Timestamp getLockDate() {
		return this.lockDate;
	}

	public void setLockDate(Timestamp lockDate) {
		this.lockDate = lockDate;
	}

	@Column(name = "DATA_COLL_FALL")
	public Timestamp getFailedLogonDate() {
		return this.failedLogonDate;
	}

	public void setFailedLogonDate(Timestamp failedLogonDate) {
		this.failedLogonDate = failedLogonDate;
	}

	public String getCodiceFiscale() {
		return this.codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	@Column(name = "COGNOME")
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@OneToOne(targetEntity = Intestatario.class, optional = false)
	@JoinColumn(name = "INTESTATARIO")
	public Intestatario getIntestatario() {
		return this.intestatario;
	}

	public void setIntestatario(Intestatario intestatario) {
		this.intestatario = intestatario;
	}

	// @ManyToMany(mappedBy="operatori",targetEntity=Intestatario.class)
	// public Set<IntestatariCommon> getIntestatari() {
	// return this.intestatari;
	// }
	//
	// public void setIntestatari(Set<IntestatariCommon> intestatari) {
	// this.intestatari = intestatari;
	// }



	@OneToMany(targetEntity = IntestatarioOperatore.class, fetch = FetchType.EAGER, mappedBy = "operatore", cascade = CascadeType.ALL)
	//TODO: , orphanRemoval = true)
	public Set<IntestatarioOperatore> getIntestatarioperatori() {
		return this.intestatarioOperatore;
	}

	public void setIntestatarioperatori(Set<IntestatarioOperatore> intestatarioOperatore) {
		this.intestatarioOperatore = intestatarioOperatore;
	}

}
