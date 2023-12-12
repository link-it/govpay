/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.model.reportistica.statistiche;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import it.govpay.model.Pagamento.TipoPagamento;

public class FiltroRiscossioni implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date dataDa;
	private Date dataA;
	private Long idDominio;
	private List<String> codDominio;
	private Long idUo;
	private List<String> codUo;
	private Long idTipoVersamento;
	private List<String> codTipoVersamento;
	private List<String> direzione;
	private List<String> divisione;
	private List<String> tassonomia;
	private Long idApplicazione;
	private List<String> codApplicazione;
	private List<TipoPagamento> tipo;
	
	public Date getDataDa() {
		return dataDa;
	}
	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}
	public Date getDataA() {
		return dataA;
	}
	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	public Long getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(Long idDominio) {
		this.idDominio = idDominio;
	}
	public Long getIdUo() {
		return idUo;
	}
	public void setIdUo(Long idUo) {
		this.idUo = idUo;
	}
	public Long getIdTipoVersamento() {
		return idTipoVersamento;
	}
	public void setIdTipoVersamento(Long idTipoVersamento) {
		this.idTipoVersamento = idTipoVersamento;
	}
	public Long getIdApplicazione() {
		return idApplicazione;
	}
	public void setIdApplicazione(Long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	public List<String> getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(List<String> codDominio) {
		this.codDominio = codDominio;
	}
	public List<String> getCodUo() {
		return codUo;
	}
	public void setCodUo(List<String> codUo) {
		this.codUo = codUo;
	}
	public List<String> getCodTipoVersamento() {
		return codTipoVersamento;
	}
	public void setCodTipoVersamento(List<String> codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}
	public List<String> getDirezione() {
		return direzione;
	}
	public void setDirezione(List<String> direzione) {
		this.direzione = direzione;
	}
	public List<String> getDivisione() {
		return divisione;
	}
	public void setDivisione(List<String> divisione) {
		this.divisione = divisione;
	}
	public List<String> getTassonomia() {
		return tassonomia;
	}
	public void setTassonomia(List<String> tassonomia) {
		this.tassonomia = tassonomia;
	}
	public List<String> getCodApplicazione() {
		return codApplicazione;
	}
	public void setCodApplicazione(List<String> codApplicazione) {
		this.codApplicazione = codApplicazione;
	}
	public List<TipoPagamento> getTipo() {
		return tipo;
	}
	public void setTipo(List<TipoPagamento> tipo) {
		this.tipo = tipo;
	}
}
