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
package it.govpay.web.rs.v1.beans;

import java.math.BigDecimal;
import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;

public class Pagamento {
	
	private String dominio;
	private String iuv;
	private String iur;
	private BigDecimal importo;
	private Date data_pagamento;
	private String id_applicazione;
	private String id_versamento_ente;
	private String id_singolo_versamento_ente;
	
	public Pagamento(it.govpay.bd.model.Pagamento p, BasicBD bd) throws ServiceException {
		this.setDominio(p.getCodDominio());
		this.setIuv(p.getIuv());
		this.setIur(p.getIur());
		this.setImporto(p.getImportoPagato());
		this.setData_pagamento(p.getDataPagamento());
		this.setId_applicazione(p.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd).getCodApplicazione());
		this.setId_versamento_ente(p.getSingoloVersamento(bd).getVersamento(bd).getCodVersamentoEnte());
		this.setId_singolo_versamento_ente(p.getSingoloVersamento(bd).getCodSingoloVersamentoEnte());
	}
	
	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getIur() {
		return iur;
	}
	public void setIur(String iur) {
		this.iur = iur;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public Date getData_pagamento() {
		return data_pagamento;
	}
	public void setData_pagamento(Date data_pagamento) {
		this.data_pagamento = data_pagamento;
	}
	public String getId_applicazione() {
		return id_applicazione;
	}
	public void setId_applicazione(String id_applicazione) {
		this.id_applicazione = id_applicazione;
	}
	public String getId_versamento_ente() {
		return id_versamento_ente;
	}
	public void setId_versamento_ente(String id_versamento_ente) {
		this.id_versamento_ente = id_versamento_ente;
	}
	public String getId_singolo_versamento_ente() {
		return id_singolo_versamento_ente;
	}
	public void setId_singolo_versamento_ente(String id_singolo_versamento_ente) {
		this.id_singolo_versamento_ente = id_singolo_versamento_ente;
	}

}
