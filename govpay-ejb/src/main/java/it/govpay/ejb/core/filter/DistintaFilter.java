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
package it.govpay.ejb.core.filter;

import it.govpay.ejb.core.model.DistintaModel.EnumStatoDistinta;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DistintaFilter extends PagingFilter {

	private static final long serialVersionUID = 1L;

	private String iuv;
	private BigDecimal importoDa;
	private BigDecimal importoA;
	
	// Intervallo Date
	private Date dataInizio = null;
	private Date dataFine = null;
	
	// Codice Fiscale Ente
	private String cfEnteCreditore = null;
	
	// Stato del pagamento
	private EnumStatoDistinta stato = null;
	
	// CF del versante e del debitore da utilizzare in OR
	private String cfVersanteODebitore = null;
	
	// Lista degli id degli enti gestiti dall'operatore.
	private List<String> identificativiEnteCreditore= null;

	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public BigDecimal getImportoDa() {
		return this.importoDa;
	}

	public void setImportoDa(BigDecimal importoDa) {
		this.importoDa = importoDa;
	}

	public BigDecimal getImportoA() {
		return this.importoA;
	}

	public void setImportoA(BigDecimal importoA) {
		this.importoA = importoA;
	}

	public Date getDataInizio() {
		return this.dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return this.dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public String getCfEnteCreditore() {
		return this.cfEnteCreditore;
	}

	public void setCfEnteCreditore(String cfEnteCreditore) {
		this.cfEnteCreditore = cfEnteCreditore;
	}

	public EnumStatoDistinta getStato() {
		return this.stato;
	}

	public void setStato(EnumStatoDistinta stato) {
		this.stato = stato;
	}

	public String getCfVersanteODebitore() {
		return cfVersanteODebitore;
	}

	public void setCfVersanteODebitore(String cfVersanteODebitore) {
		this.cfVersanteODebitore = cfVersanteODebitore;
	}

	public List<String> getIdentificativiEnteCreditore() {
		return this.identificativiEnteCreditore;
	}

	public void setIdentificativiEnteCreditore(
			List<String> identificativiEnteCreditore) {
		this.identificativiEnteCreditore = identificativiEnteCreditore;
	}

}
