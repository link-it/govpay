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
import java.math.BigDecimal;
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
@Table(name = "intestatari")
public class Intestatario implements Serializable {

	private static final long serialVersionUID = 1L;

	/*** Persistent Properties ***/
	private String corporate;
	private String abi;
	private String abiaccentratore;
	private String cab;
	private String chiavessb;
	private String codicepostel;
	private String codicesoftware;
	private String denominazione;
	private String gruppo;
	private String lapl;
	private String ragionesociale;
	private String rapl;
	private String rcz;
	private String sia;
	private Integer stato;
	private String tipointestatario;
	private String tiposicurezza;
	private String uffpostale;
	private String codiceCuc;
	private String nonresidente;
	private String categoria;
	private String sottoCategoria;
	private BigDecimal importoMaxFlusso;
	private String flagComunicazioni;

	/*** Persistent References ***/
	private IndirizzoPostale indirizzoPostale;
	private Ente ente;

	/*** Persistent Collections ***/
	private Set<IntestatarioOperatore> intestatarioOperatore;

	@Id
	@Column(name = "INTESTATARIO")
	public String getCorporate() {
		return this.corporate;
	}

	public void setCorporate(String corporate) {
		this.corporate = corporate;
	}

	public String getAbi() {
		return this.abi;
	}

	public void setAbi(String abi) {
		this.abi = abi;
	}

	public String getAbiaccentratore() {
		return this.abiaccentratore;
	}

	public void setAbiaccentratore(String abiaccentratore) {
		this.abiaccentratore = abiaccentratore;
	}

	public String getCab() {
		return this.cab;
	}

	public void setCab(String cab) {
		this.cab = cab;
	}

	public String getChiavessb() {
		return this.chiavessb;
	}

	public void setChiavessb(String chiavessb) {
		this.chiavessb = chiavessb;
	}

	public String getCodicepostel() {
		return this.codicepostel;
	}

	public void setCodicepostel(String codicepostel) {
		this.codicepostel = codicepostel;
	}

	public String getCodicesoftware() {
		return this.codicesoftware;
	}

	public void setCodicesoftware(String codicesoftware) {
		this.codicesoftware = codicesoftware;
	}

	public String getDenominazione() {
		return this.denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getGruppo() {
		return this.gruppo;
	}

	public void setGruppo(String gruppo) {
		this.gruppo = gruppo;
	}

	public String getLapl() {
		return this.lapl;
	}

	public void setLapl(String lapl) {
		this.lapl = lapl;
	}

	public String getRagionesociale() {
		return this.ragionesociale;
	}

	public void setRagionesociale(String ragionesociale) {
		this.ragionesociale = ragionesociale;
	}

	public String getRapl() {
		return this.rapl;
	}

	public void setRapl(String rapl) {
		this.rapl = rapl;
	}

	public String getRcz() {
		return this.rcz;
	}

	public void setRcz(String rcz) {
		this.rcz = rcz;
	}

	public String getSia() {
		return this.sia;
	}

	public void setSia(String sia) {
		this.sia = sia;
	}

	public Integer getStato() {
		return this.stato;
	}

	public void setStato(Integer stato) {
		this.stato = stato;
	}

	public String getTipointestatario() {
		return this.tipointestatario;
	}

	public void setTipointestatario(String tipointestatario) {
		this.tipointestatario = tipointestatario;
	}

	public String getTiposicurezza() {
		return this.tiposicurezza;
	}

	public void setTiposicurezza(String tiposicurezza) {
		this.tiposicurezza = tiposicurezza;
	}

	public String getUffpostale() {
		return this.uffpostale;
	}

	public void setUffpostale(String uffpostale) {
		this.uffpostale = uffpostale;
	}

	public String getCodiceCuc() {
		return this.codiceCuc;
	}

	public void setCodiceCuc(String codiceCuc) {
		this.codiceCuc = codiceCuc;
	}

	public String getNonresidente() {
		return this.nonresidente;
	}

	public void setNonresidente(String nonresidente) {
		this.nonresidente = nonresidente;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getSottoCategoria() {
		return sottoCategoria;
	}

	public void setSottoCategoria(String sottoCategoria) {
		this.sottoCategoria = sottoCategoria;
	}

	public BigDecimal getImportoMaxFlusso() {
		return importoMaxFlusso;
	}

	public void setImportoMaxFlusso(BigDecimal importoMaxFlusso) {
		this.importoMaxFlusso = importoMaxFlusso;
	}

	@OneToOne(targetEntity = IndirizzoPostale.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_INDIRIZZIPOSTALI")
	public IndirizzoPostale getIndirizzipostali() {
		return (IndirizzoPostale) this.indirizzoPostale;
	}

	public void setIndirizzipostali(IndirizzoPostale indirizzoPostale) {
		this.indirizzoPostale = indirizzoPostale;
	}

	@OneToOne(targetEntity = Ente.class, fetch = FetchType.LAZY, mappedBy = "intestatario", cascade = CascadeType.ALL)
	public Ente getEnte() {
		return ente;
	}

	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	@OneToMany(targetEntity = IntestatarioOperatore.class, mappedBy = "intestatario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public Set<IntestatarioOperatore> getIntestatariOperatori() {
		return this.intestatarioOperatore;
	}

	public void setIntestatariOperatori(Set<IntestatarioOperatore> intestatariOperatori) {
		this.intestatarioOperatore = intestatariOperatori;
	}

	@Column(name = "FLAG_COMUNICAZIONI")
	public String getFlagComunicazioni() {
		return flagComunicazioni;
	}

	public void setFlagComunicazioni(String flagComunicazioni) {
		this.flagComunicazioni = flagComunicazioni;
	}

}
