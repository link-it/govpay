/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.core.business.model.tracciati.operazioni;

import java.math.BigDecimal;

import it.govpay.core.beans.tracciati.EsitoOperazionePendenza;
import it.govpay.core.beans.tracciati.FaultBean;
import it.govpay.core.beans.tracciati.StatoOperazionePendenza;
import it.govpay.core.beans.tracciati.TipoOperazionePendenza;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;


public abstract class AbstractOperazioneResponse {
	
	public static final String ESITO_ADD_OK = "ADD_OK";
	public static final String ESITO_ADD_KO = "ADD_KO";
	public static final String ESITO_DEL_OK = "DEL_OK";
	public static final String ESITO_DEL_KO = "DEL_KO";

	private StatoOperazioneType stato;
	private TipoOperazioneType tipo;
	private String esito;
	private String descrizioneEsito;
	private String idA2A;
	private String idPendenza;
	private long numero;
	private EsitoOperazionePendenza esitoOperazionePendenza;
	private FaultBean faultBean;
	private String jsonRichiesta;

	public StatoOperazioneType getStato() {
		return this.stato;
	}
	public void setStato(StatoOperazioneType stato) {
		this.stato = stato;
	}

	public String getDescrizioneEsito() {
		return this.descrizioneEsito;
	}
	public void setDescrizioneEsito(String descrizioneEsito) {
		this.descrizioneEsito = descrizioneEsito;
	}
	public String getEsito() {
		return this.esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public TipoOperazioneType getTipo() {
		return this.tipo;
	}
	public void setTipo(TipoOperazioneType tipo) {
		this.tipo = tipo;
	}
	public String getIdA2A() {
		return this.idA2A;
	}
	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}
	public String getIdPendenza() {
		return this.idPendenza;
	}
	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}
	public long getNumero() {
		return this.numero;
	}
	public void setNumero(long numero) {
		this.numero = numero;
	}
	public EsitoOperazionePendenza getEsitoOperazionePendenza() {
		this.esitoOperazionePendenza = new EsitoOperazionePendenza();
		this.esitoOperazionePendenza.setDescrizioneEsito(this.getDescrizioneEsito());
		this.esitoOperazionePendenza.setEsito(this.getEsito());
		this.esitoOperazionePendenza.setIdA2A(this.getIdA2A());
		this.esitoOperazionePendenza.setIdPendenza(this.getIdPendenza());
		this.esitoOperazionePendenza.setNumero(BigDecimal.valueOf(this.getNumero()));
		StatoOperazioneType stato = this.getStato();
		StatoOperazionePendenza statoEsitoOperazione = null;

		switch (stato) {
		case ESEGUITO_OK:
			statoEsitoOperazione = StatoOperazionePendenza.ESEGUITO;
			break;
		case ESEGUITO_KO:
			statoEsitoOperazione = StatoOperazionePendenza.SCARTATO;
			break;
		case NON_VALIDO:
		default:
			statoEsitoOperazione = StatoOperazionePendenza.NON_VALIDO;
			break;
		}
		this.esitoOperazionePendenza.setStato(statoEsitoOperazione);

		TipoOperazioneType tipo = this.getTipo();
		TipoOperazionePendenza tipoEsitoOperazione;

		switch(tipo) {
		case ADD:
			tipoEsitoOperazione = TipoOperazionePendenza.ADD;
			break;
		case DEL:
			tipoEsitoOperazione = TipoOperazionePendenza.DEL;
			break;
		case INC:
		case N_V:
		default:		
			tipoEsitoOperazione = TipoOperazionePendenza.NON_VALIDA;
			break;

		}

		this.esitoOperazionePendenza.setTipoOperazione(tipoEsitoOperazione);
		this.esitoOperazionePendenza.setDati(this.getDati());
		return this.esitoOperazionePendenza;
	}

	public void setEsitoOperazionePendenza(EsitoOperazionePendenza esitoOperazionePendenza) {
		this.esitoOperazionePendenza = esitoOperazionePendenza;
	}
	public FaultBean getFaultBean() {
		return this.faultBean;
	}
	public void setFaultBean(FaultBean faultBean) {
		this.faultBean = faultBean;
	}
	public String getJsonRichiesta() {
		return jsonRichiesta;
	}
	public void setJsonRichiesta(String jsonRichiesta) {
		this.jsonRichiesta = jsonRichiesta;
	}

	public abstract Object getDati();
}
