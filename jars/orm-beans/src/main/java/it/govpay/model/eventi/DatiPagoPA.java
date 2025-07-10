/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.model.eventi;

import java.io.Serializable;

public class DatiPagoPA implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DatiPagoPA() {
		// donothing
	}

	private String codPsp;
	private String tipoVersamento;
	private String modelloPagamento;
	private String fruitore;
	private String erogatore;
	private String codStazione;
	private String codCanale;
	private String codIntermediario;
	private String codIntermediarioPsp;
	private String codDominio;
	private String codFlusso;
	private String trn;
	private String sct;
	private Long idTracciato;

	public String getCodPsp() {
		return codPsp;
	}
	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}
	public String getTipoVersamento() {
		return tipoVersamento;
	}
	public void setTipoVersamento(String tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}
	public String getFruitore() {
		return fruitore;
	}
	public void setFruitore(String fruitore) {
		this.fruitore = fruitore;
	}
	public String getErogatore() {
		return erogatore;
	}
	public void setErogatore(String erogatore) {
		this.erogatore = erogatore;
	}
	public String getCodStazione() {
		return codStazione;
	}
	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}
	public String getCodCanale() {
		return codCanale;
	}
	public void setCodCanale(String codCanale) {
		this.codCanale = codCanale;
	}
	public String getModelloPagamento() {
		return modelloPagamento;
	}
	public void setModelloPagamento(String modelloPagamento) {
		this.modelloPagamento = modelloPagamento;
	}
	public String getCodIntermediario() {
		return codIntermediario;
	}
	public void setCodIntermediario(String codIntermediario) {
		this.codIntermediario = codIntermediario;
	}
	public String getCodIntermediarioPsp() {
		return codIntermediarioPsp;
	}
	public void setCodIntermediarioPsp(String codIntermediarioPsp) {
		this.codIntermediarioPsp = codIntermediarioPsp;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getCodFlusso() {
		return codFlusso;
	}
	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}
	public String getTrn() {
		return trn;
	}
	public void setTrn(String trn) {
		this.trn = trn;
	}
	public Long getIdTracciato() {
		return idTracciato;
	}
	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}
	public String getSct() {
		return sct;
	}
	public void setSct(String sct) {
		this.sct = sct;
	}

}
