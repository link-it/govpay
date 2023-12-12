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
package it.govpay.core.utils;

import it.govpay.model.Tributo.TipoContabilita;

public class VersamentoContext {
	
	private String codUoBeneficiaria;
	private String codUnivocoDebitore;
	
	//Valorizzati solo se il versamento ha un solo singolo pagamento
	private TipoContabilita tipoContabilita;
	private String codContabilita;
	
	//Valorizzato con la codifica IUV prevista per la tipologia di versamento
	private String codificaIuv;

	public String getCodUoBeneficiaria() {
		return this.codUoBeneficiaria;
	}

	public void setCodUoBeneficiaria(String codUoBeneficiaria) {
		this.codUoBeneficiaria = codUoBeneficiaria;
	}

	public TipoContabilita getTipoContabilita() {
		return this.tipoContabilita;
	}

	public void setTipoContabilita(TipoContabilita tipoContabilita) {
		this.tipoContabilita = tipoContabilita;
	}

	public String getCodContabilita() {
		return this.codContabilita;
	}

	public void setCodContabilita(String codContabilita) {
		this.codContabilita = codContabilita;
	}

	public String getCodificaIuv() {
		return this.codificaIuv;
	}

	public void setCodificaIuv(String codificaIuv) {
		this.codificaIuv = codificaIuv;
	}

	public String getCodUnivocoDebitore() {
		return this.codUnivocoDebitore;
	}

	public void setCodUnivocoDebitore(String codUnivocoDebitore) {
		this.codUnivocoDebitore = codUnivocoDebitore;
	}
}
