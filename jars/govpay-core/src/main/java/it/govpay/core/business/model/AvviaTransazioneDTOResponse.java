package it.govpay.core.business.model;

import java.util.ArrayList;
import java.util.List;

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
public class AvviaTransazioneDTOResponse {


	private String codSessione;
	private String pspRedirectURL;
	private List<RifTransazione> rifTransazioni;

	public class RifTransazione {

		protected String codApplicazione;
		protected String codVersamentoEnte;
		protected String codDominio;
		protected String iuv;
		protected String ccp;

		public String getCodApplicazione() {
			return this.codApplicazione;
		}

		public void setCodApplicazione(String value) {
			this.codApplicazione = value;
		}

		public String getCodVersamentoEnte() {
			return this.codVersamentoEnte;
		}

		public void setCodVersamentoEnte(String value) {
			this.codVersamentoEnte = value;
		}

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
	}

	public String getCodSessione() {
		return this.codSessione;
	}

	public void setCodSessione(String codSessione) {
		this.codSessione = codSessione;
	}

	public String getPspRedirectURL() {
		return this.pspRedirectURL;
	}

	public void setPspRedirectURL(String pspRedirectURL) {
		this.pspRedirectURL = pspRedirectURL;
	}

	public List<RifTransazione> getRifTransazioni() {
		if(this.rifTransazioni == null)
			this.rifTransazioni = new ArrayList<>();
		
		return this.rifTransazioni;
	}

	public void setRifTransazioni(List<RifTransazione> rifTransazioni) {
		this.rifTransazioni = rifTransazioni;
	}

}
