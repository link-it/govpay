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
package it.govpay.rs.v1.beans;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.model.Applicazione;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;

public class Operazione {
	
	private long linea_elaborazione;
	private StatoOperazioneType stato;
	private String dettaglio_esito;
	private TipoOperazioneType tipo_operazione;
	private String cod_applicazione;
	private String cod_versamento_ente;

	public Operazione() {
	}
	
	public Operazione(it.govpay.bd.model.Operazione o, BasicBD bd) throws ServiceException {
		this.linea_elaborazione = o.getLineaElaborazione();
		this.stato = o.getStato();
		this.dettaglio_esito = o.getDettaglioEsito();
		this.tipo_operazione = o.getTipoOperazione();
		
		Applicazione applicazione = o.getApplicazione(bd);
		if(applicazione != null)
			this.cod_applicazione = applicazione.getCodApplicazione();
		
		this.cod_versamento_ente = o.getCodVersamentoEnte();
	}

	public long getLinea_elaborazione() {
		return linea_elaborazione;
	}

	public StatoOperazioneType getStato() {
		return stato;
	}

	public String getDettaglio_esito() {
		return dettaglio_esito;
	}

	public TipoOperazioneType getTipo_operazione() {
		return tipo_operazione;
	}

	public String getCod_applicazione() {
		return cod_applicazione;
	}

	public String getCod_versamento_ente() {
		return cod_versamento_ente;
	}
	
}
