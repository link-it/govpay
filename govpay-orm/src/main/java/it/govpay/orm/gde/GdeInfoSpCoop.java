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
package it.govpay.orm.gde;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "gde_infospcoop")
public class GdeInfoSpCoop {

	private String idEgov;
	private String tipoSoggettoErogatore;
	private String soggettoErogatore;
	private String tipoSoggettoFruitore;
	private String soggettoFruitore;
	private String tipoServizio;
	private String servizio;
	private String azione;
	
	@Id
	@Column(name = "id_egov")
	public String getIdEgov() {
		return idEgov;
	}

	public void setIdEgov(String idEgov) {
		this.idEgov = idEgov;
	}

	@Column(name = "tipo_soggetto_erogatore")
	public String getTipoSoggettoErogatore() {
		return tipoSoggettoErogatore;
	}

	public void setTipoSoggettoErogatore(String tipoSoggettoErogatore) {
		this.tipoSoggettoErogatore = tipoSoggettoErogatore;
	}

	@Column(name = "soggetto_erogatore")
	public String getSoggettoErogatore() {
		return soggettoErogatore;
	}

	public void setSoggettoErogatore(String soggettoErogatore) {
		this.soggettoErogatore = soggettoErogatore;
	}

	@Column(name = "tipo_soggetto_fruitore")
	public String getTipoSoggettoFruitore() {
		return tipoSoggettoFruitore;
	}

	public void setTipoSoggettoFruitore(String tipoSoggettoFruitore) {
		this.tipoSoggettoFruitore = tipoSoggettoFruitore;
	}

	@Column(name = "soggetto_fruitore")
	public String getSoggettoFruitore() {
		return soggettoFruitore;
	}

	public void setSoggettoFruitore(String soggettoFruitore) {
		this.soggettoFruitore = soggettoFruitore;
	}

	@Column(name = "tipo_servizio")
	public String getTipoServizio() {
		return tipoServizio;
	}

	public void setTipoServizio(String tipoServizio) {
		this.tipoServizio = tipoServizio;
	}

	@Column(name = "servizio")
	public String getServizio() {
		return servizio;
	}

	public void setServizio(String servizio) {
		this.servizio = servizio;
	}

	@Column(name = "azione")
	public String getAzione() {
		return azione;
	}

	public void setAzione(String azione) {
		this.azione = azione;
	}


}
