/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.model.Applicazione;

public class AvviaRichiestaStornoDTO {

	private String codApplicazione;
	private String codDominio;
	private String iuv;
	private String ccp;
	private String causaleRevoca;
	private String datiAggiuntivi;
	private List<Pagamento> pagamento;
	private Applicazione applicazione;

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String value) {
		this.codDominio = value;
	}

	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String value) {
		this.iuv = value;
	}

	public String getCcp() {
		return this.ccp;
	}

	public void setCcp(String value) {
		this.ccp = value;
	}

	public String getCausaleRevoca() {
		return this.causaleRevoca;
	}

	public void setCausaleRevoca(String value) {
		this.causaleRevoca = value;
	}

	public String getDatiAggiuntivi() {
		return this.datiAggiuntivi;
	}

	public void setDatiAggiuntivi(String value) {
		this.datiAggiuntivi = value;
	}

	public List<Pagamento> getPagamento() {
		if (this.pagamento == null) {
			this.pagamento = new ArrayList<>();
		}
		return this.pagamento;
	}

	public String getCodApplicazione() {
		return this.codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public Applicazione getApplicazione() {
		return this.applicazione;
	}

	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}



	public class Pagamento {

		private String iur;
		private String causaleRevoca;
		private String datiAggiuntivi;

		public String getIur() {
			return this.iur;
		}

		public void setIur(String value) {
			this.iur = value;
		}

		public String getCausaleRevoca() {
			return this.causaleRevoca;
		}

		public void setCausaleRevoca(String value) {
			this.causaleRevoca = value;
		}

		public String getDatiAggiuntivi() {
			return this.datiAggiuntivi;
		}

		public void setDatiAggiuntivi(String value) {
			this.datiAggiuntivi = value;
		}

	}

}

