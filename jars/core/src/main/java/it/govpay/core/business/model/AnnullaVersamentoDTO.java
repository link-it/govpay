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

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;

public class AnnullaVersamentoDTO {
	
	private Operatore operatore;
	private Applicazione applicazione;
	private String codApplicazione;
	private String codVersamentoEnte;
	private String motivoAnnullamento;
	
	public AnnullaVersamentoDTO(Operatore operatore, String codApplicazione, String codVersamentoEnte) {
		this.operatore = operatore;
		this.codApplicazione = codApplicazione;
		this.codVersamentoEnte = codVersamentoEnte;
	}
	
	public AnnullaVersamentoDTO(Applicazione applicazione, String codApplicazione, String codVersamentoEnte) {
		this.applicazione = applicazione;
		this.codApplicazione = codApplicazione;
		this.codVersamentoEnte = codVersamentoEnte;
	}
	
	public Applicazione getApplicazione() {
		return this.applicazione;
	}
	
	public Operatore getOperatore() {
		return this.operatore;
	}

	public String getCodApplicazione() {
		return this.codApplicazione;
	}

	public String getCodVersamentoEnte() {
		return this.codVersamentoEnte;
	}

	public String getMotivoAnnullamento() {
		return this.motivoAnnullamento;
	}

	public void setMotivoAnnullamento(String motivoAnnullamento) {
		this.motivoAnnullamento = motivoAnnullamento;
	}
	
}
