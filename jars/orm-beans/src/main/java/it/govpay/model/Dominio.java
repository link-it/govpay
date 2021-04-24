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

public class Dominio extends BasicModel {
	private static final long serialVersionUID = 1L;

	public static final String EC = "EC"; 

	private Long id; 
	private long idStazione; 
	private Long idApplicazioneDefault; 
	private String codDominio;
	private String ragioneSociale;
	private String gln;
	private boolean abilitato;
	private int auxDigit;
	private Integer segregationCode;

	private String iuvPrefix;
	private byte[] logo;
	private String cbill;
	private String autStampaPoste;
	private ConnettoreNotificaPagamenti connettoreMyPivot;
	private ConnettoreNotificaPagamenti connettoreSecim;
	private ConnettoreNotificaPagamenti connettoreGovPay;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getGln() {
		return this.gln;
	}

	public void setGln(String gln) {
		this.gln = gln;
	}

	public String getRagioneSociale() {
		return this.ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public boolean isAbilitato() {
		return this.abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	public long getIdStazione() {
		return this.idStazione;
	}

	public void setIdStazione(long idStazione) {
		this.idStazione = idStazione;
	}

	public Long getIdApplicazioneDefault() {
		return this.idApplicazioneDefault;
	}

	public void setIdApplicazioneDefault(Long idApplicazioneDefault) {
		this.idApplicazioneDefault = idApplicazioneDefault;
	}

	public int getAuxDigit() {
		return this.auxDigit;
	}

	public void setAuxDigit(int auxDigit) {
		this.auxDigit = auxDigit;
	}

	public String getIuvPrefix() {
		return this.iuvPrefix;
	}

	public void setIuvPrefix(String iuvPrefix) {
		this.iuvPrefix = iuvPrefix;
	}

	public Integer getSegregationCode() {
		return this.segregationCode;
	}

	public void setSegregationCode(Integer segregationCode) {
		this.segregationCode = segregationCode;
	}

	public byte[] getLogo() {
		return this.logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public String getCbill() {
		return this.cbill;
	}

	public void setCbill(String cbill) {
		this.cbill = cbill;
	}

	public String getAutStampaPoste() {
		return autStampaPoste;
	}

	public void setAutStampaPoste(String autStampaPoste) {
		this.autStampaPoste = autStampaPoste;
	}

	public ConnettoreNotificaPagamenti getConnettoreMyPivot() {
		return connettoreMyPivot;
	}

	public void setConnettoreMyPivot(ConnettoreNotificaPagamenti connettoreMyPivot) {
		this.connettoreMyPivot = connettoreMyPivot;
	}

	public ConnettoreNotificaPagamenti getConnettoreSecim() {
		return connettoreSecim;
	}

	public void setConnettoreSecim(ConnettoreNotificaPagamenti connettoreSecim) {
		this.connettoreSecim = connettoreSecim;
	}

	public ConnettoreNotificaPagamenti getConnettoreGovPay() {
		return connettoreGovPay;
	}

	public void setConnettoreGovPay(ConnettoreNotificaPagamenti connettoreGovPay) {
		this.connettoreGovPay = connettoreGovPay;
	}
	
}

