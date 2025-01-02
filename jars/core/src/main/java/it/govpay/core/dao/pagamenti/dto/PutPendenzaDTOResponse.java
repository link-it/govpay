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
package it.govpay.core.dao.pagamenti.dto;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.core.dao.anagrafica.dto.BasicCreateResponseDTO;

public class PutPendenzaDTOResponse extends BasicCreateResponseDTO {

	private Versamento versamento;
	private Dominio dominio;
	private String qrCode; 
	private String barCode;
	private String pdf;
	private UnitaOperativa uo;
	
	public Versamento getVersamento() {
		return this.versamento;
	}
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}
	public Dominio getDominio() {
		return this.dominio;
	}
	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}
	public String getQrCode() {
		return this.qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getBarCode() {
		return this.barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getPdf() {
		return this.pdf;
	}
	public void setPdf(String pdf) {
		this.pdf = pdf;
	}
	public UnitaOperativa getUo() {
		return uo;
	}
	public void setUo(UnitaOperativa uo) {
		this.uo = uo;
	}

}
