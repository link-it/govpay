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
package it.govpay.model;

import java.math.BigDecimal;

public class QuotaContabilita {

	private String capitolo;
	private int annoEsercizio;
	private String accertamento;
	private BigDecimal importo;
	private Object proprietaCustom;
	private String titolo;
	private String tipologia;
	private String categoria;
	private String articolo;

	public String getCapitolo() {
		return capitolo;
	}
	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}
	public int getAnnoEsercizio() {
		return annoEsercizio;
	}
	public void setAnnoEsercizio(int annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}
	public String getAccertamento() {
		return accertamento;
	}
	public void setAccertamento(String accertamento) {
		this.accertamento = accertamento;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public Object getProprietaCustom() {
		return proprietaCustom;
	}
	public void setProprietaCustom(Object proprietaCustom) {
		this.proprietaCustom = proprietaCustom;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getTipologia() {
		return tipologia;
	}
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getArticolo() {
		return articolo;
	}
	public void setArticolo(String articolo) {
		this.articolo = articolo;
	} 


}