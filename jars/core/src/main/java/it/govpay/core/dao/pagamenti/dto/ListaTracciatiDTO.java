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
package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.StatoTracciatoPendenza;
import it.govpay.model.Tracciato.FORMATO_TRACCIATO;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;
import it.govpay.orm.Tracciato;
import it.govpay.orm.constants.StatoTracciatoType;

public class ListaTracciatiDTO extends BasicFindRequestDTO{

	
	private List<TIPO_TRACCIATO> tipoTracciato = null;
	private String operatore;
	private STATO_ELABORAZIONE statoTracciato = null;
	private StatoTracciatoPendenza statoTracciatoPendenza = null;
	private String idDominio;
	private String dettaglioStato = null;
	private FORMATO_TRACCIATO formatoTracciato = null;
	private String idTipoPendenza = null;
	
	public ListaTracciatiDTO(Authentication user) {
		super(user);
		this.addDefaultSort(Tracciato.model().DATA_CARICAMENTO,SortOrder.DESC);
	}

	public List<TIPO_TRACCIATO> getTipoTracciato() {
		return this.tipoTracciato;
	}

	public void setTipoTracciato(List<TIPO_TRACCIATO> tipoTracciato) {
		this.tipoTracciato = tipoTracciato;
	}

	public String getOperatore() {
		return this.operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public STATO_ELABORAZIONE getStatoTracciato() {
		if(this.statoTracciatoPendenza != null) {
			switch (this.statoTracciatoPendenza) {
			case ESEGUITO:
			case ESEGUITO_CON_ERRORI:
				this.statoTracciato = STATO_ELABORAZIONE.COMPLETATO;
				break;
			case IN_ATTESA:
			case IN_ELABORAZIONE:
				this.statoTracciato = STATO_ELABORAZIONE.ELABORAZIONE;
				break;
			case SCARTATO:
				this.statoTracciato = STATO_ELABORAZIONE.SCARTATO;
				break;
			case ELABORAZIONE_STAMPA:
				this.statoTracciato = STATO_ELABORAZIONE.IN_STAMPA;
				break;
			}
		}
		
		return this.statoTracciato;
	}
	
	public String getDettaglioStato() {
		if(this.statoTracciatoPendenza != null) {
			switch (this.statoTracciatoPendenza) {
			case ESEGUITO:
				this.dettaglioStato = StatoTracciatoType.CARICAMENTO_OK.getValue();
				break;
			case ESEGUITO_CON_ERRORI:
				this.dettaglioStato =  StatoTracciatoType.CARICAMENTO_KO.getValue();
				break;
			case IN_ATTESA:
				this.dettaglioStato = StatoTracciatoType.NUOVO.getValue();
				break;
			case IN_ELABORAZIONE:
				this.dettaglioStato = StatoTracciatoType.IN_CARICAMENTO.getValue();
				break;
			case SCARTATO:
			default:
				this.dettaglioStato = null;
				break;
			}
		}
		
		return dettaglioStato;
	}

	public StatoTracciatoPendenza getStatoTracciatoPendenza() {
		return this.statoTracciatoPendenza;
	}

	public void setStatoTracciatoPendenza(StatoTracciatoPendenza statoTracciatoPendenza) {
		this.statoTracciatoPendenza = statoTracciatoPendenza;
	}

	public String getIdDominio() {
		return this.idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

	public FORMATO_TRACCIATO getFormatoTracciato() {
		return formatoTracciato;
	}

	public void setFormatoTracciato(FORMATO_TRACCIATO formatoTracciato) {
		this.formatoTracciato = formatoTracciato;
	}

	public String getIdTipoPendenza() {
		return idTipoPendenza;
	}

	public void setIdTipoPendenza(String idTipoPendenza) {
		this.idTipoPendenza = idTipoPendenza;
	}
	
	
}
