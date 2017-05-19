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
package it.govpay.core.business.model;

import java.math.BigDecimal;
import java.util.Date;

public class RichiestaIncassoDTO {
	
	private String principal;
	private String codDominio;
	private String trn;
	private String causale;
	private BigDecimal importo;
	private Date dataValuta;
	private Date dataContabile;
	private String dispositivo;
	
	public String getTrn() {
		return trn;
	}
	public void setTrn(String trn) {
		this.trn = trn;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public String getDispositivo() {
		return dispositivo;
	}
	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public Date getDataContabile() {
		return dataContabile;
	}
	public void setDataContabile(Date dataContabile) {
		this.dataContabile = dataContabile;
	}
	public Date getDataValuta() {
		return dataValuta;
	}
	public void setDataValuta(Date dataValuta) {
		this.dataValuta = dataValuta;
	}
}
