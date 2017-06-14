/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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

package it.govpay.model;

import java.util.Date;

public class Dominio extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	public static final String EC = "EC"; 
	
	private Long id; 
	private long idStazione; 
	private Long idApplicazioneDefault; 
	private String codDominio;
	private String ragioneSociale;
	private String gln;
	private boolean riusoIuv;
	private boolean customIuv;
	private boolean abilitato;
	private transient byte[] contiAccredito;
	private transient byte[] tabellaControparti;
	private int auxDigit;
	private Integer segregationCode;
	
	private transient Integer ndpStato;
	private transient String ndpOperazione;
	private transient String ndpDescrizione;
	private transient Date ndpData;
	
	private String iuvPrefix;
	private boolean iuvPrefixStrict;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getGln() {
		return gln;
	}

	public void setGln(String gln) {
		this.gln = gln;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	public long getIdStazione() {
		return idStazione;
	}

	public void setIdStazione(long idStazione) {
		this.idStazione = idStazione;
	}

	public byte[] getContiAccredito() {
		return contiAccredito;
	}

	public void setContiAccredito(byte[] contiAccredito) {
		this.contiAccredito = contiAccredito;
	}

	public byte[] getTabellaControparti() {
		return tabellaControparti;
	}

	public void setTabellaControparti(byte[] tabellaControparti) {
		this.tabellaControparti = tabellaControparti;
	}
	
	public Long getIdApplicazioneDefault() {
		return idApplicazioneDefault;
	}

	public void setIdApplicazioneDefault(Long idApplicazioneDefault) {
		this.idApplicazioneDefault = idApplicazioneDefault;
	}

	public boolean isRiusoIuv() {
		return riusoIuv;
	}

	public void setRiusoIuv(boolean riusoIuv) {
		this.riusoIuv = riusoIuv;
	}

	public boolean isCustomIuv() {
		return customIuv;
	}

	public void setCustomIuv(boolean customIuv) {
		this.customIuv = customIuv;
	}

	public int getAuxDigit() {
		return auxDigit;
	}

	public void setAuxDigit(int auxDigit) {
		this.auxDigit = auxDigit;
	}

	public String getIuvPrefix() {
		return iuvPrefix;
	}

	public void setIuvPrefix(String iuvPrefix) {
		this.iuvPrefix = iuvPrefix;
	}

	public boolean isIuvPrefixStrict() {
		return iuvPrefixStrict;
	}

	public void setIuvPrefixStrict(boolean iuvPrefixStrict) {
		this.iuvPrefixStrict = iuvPrefixStrict;
	}

	public Integer getSegregationCode() {
		return segregationCode;
	}

	public void setSegregationCode(Integer segregationCode) {
		this.segregationCode = segregationCode;
	}
	public Integer getNdpStato() {
		return ndpStato;
	}

	public void setNdpStato(Integer ndpStato) {
		this.ndpStato = ndpStato;
	}

	public String getNdpOperazione() {
		return ndpOperazione;
	}

	public void setNdpOperazione(String ndpOperazione) {
		this.ndpOperazione = ndpOperazione;
	}

	public String getNdpDescrizione() {
		return ndpDescrizione;
	}

	public void setNdpDescrizione(String ndpDescrizione) {
		this.ndpDescrizione = ndpDescrizione;
	}

	public Date getNdpData() {
		return ndpData;
	}

	public void setNdpData(Date ndpData) {
		this.ndpData = ndpData;
	}
	
}

