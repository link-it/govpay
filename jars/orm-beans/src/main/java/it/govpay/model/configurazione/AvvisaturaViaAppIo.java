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
package it.govpay.model.configurazione;

import java.io.Serializable;

public class AvvisaturaViaAppIo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private PromemoriaAvvisoBase promemoriaAvviso;
	private PromemoriaRicevutaBase promemoriaRicevuta;
	private PromemoriaScadenza promemoriaScadenza;
	
	public PromemoriaAvvisoBase getPromemoriaAvviso() {
		return promemoriaAvviso;
	}
	public void setPromemoriaAvviso(PromemoriaAvvisoBase promemoriaAvviso) {
		this.promemoriaAvviso = promemoriaAvviso;
	}
	public PromemoriaRicevutaBase getPromemoriaRicevuta() {
		return promemoriaRicevuta;
	}
	public void setPromemoriaRicevuta(PromemoriaRicevutaBase promemoriaRicevuta) {
		this.promemoriaRicevuta = promemoriaRicevuta;
	}
	public PromemoriaScadenza getPromemoriaScadenza() {
		return promemoriaScadenza;
	}
	public void setPromemoriaScadenza(PromemoriaScadenza promemoriaScadenza) {
		this.promemoriaScadenza = promemoriaScadenza;
	}
}
