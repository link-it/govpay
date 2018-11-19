/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2018 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.tracciati.operazioni;

import it.govpay.bd.model.Operatore;
import it.govpay.model.Operazione.TipoOperazioneType;


public abstract class AbstractOperazioneRequest {

	private Long idTracciato;
	private Long linea;
	private byte[] dati;
	private TipoOperazioneType tipoOperazione;
	private Operatore operatore;
	private String codApplicazione;
	private String codVersamentoEnte;

	public AbstractOperazioneRequest(TipoOperazioneType tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public Long getLinea() {
		return this.linea;
	}
	public void setLinea(Long linea) {
		this.linea = linea;
	}
	public byte[] getDati() {
		return this.dati;
	}
	public void setDati(byte[] dati) {
		this.dati = dati;
	}
	public Long getIdTracciato() {
		return this.idTracciato;
	}
	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}

	public TipoOperazioneType getTipoOperazione() {
		return this.tipoOperazione;
	}

	public void setTipoOperazione(TipoOperazioneType tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getCodApplicazione() {
		return this.codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getCodVersamentoEnte() {
		return this.codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public Operatore getOperatore() {
		return this.operatore;
	}

	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}
}
