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
package it.govpay.rs.legacy.beans;

import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.map.annotate.JsonFilter;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.rs.v1.beans.JSONSerializable;


@JsonFilter(value="iban")
public class Iban extends JSONSerializable {

	private String href;
	private String iban;
	private String bic;
	private String ibanAppoggio;
	private String bicAppoggio;
	private boolean abilitato;

	public Iban(it.govpay.bd.model.IbanAccredito iban, String codDominio, UriBuilder uriBuilder) throws ServiceException {
		this.abilitato = iban.isAbilitato();
		this.iban = iban.getCodIban();
		this.bic = iban.getCodBicAccredito();
		this.ibanAppoggio = iban.getCodIbanAppoggio();
		this.bicAppoggio = iban.getCodBicAppoggio();    
		this.href = uriBuilder.clone().path("domini").path(codDominio).path("iban").path(iban.getCodIban()).build().toString();
	}
	

	  @Override
	  public String getJsonIdFilter() {
	          return "iban";
	  }

	  public String getHref() {
	          return href;
	  }

	  public void setHref(String href) {
	          this.href = href;
	  }

	  public String getIban() {
	          return iban;
	  }

	  public void setIban(String iban) {
	          this.iban = iban;
	  }

	  public String getBic() {
	          return bic;
	  }

	  public void setBic(String bic) {
	          this.bic = bic;
	  }

	  public String getIbanAppoggio() {
	          return ibanAppoggio;
	  }

	  public void setIbanAppoggio(String ibanAppoggio) {
	          this.ibanAppoggio = ibanAppoggio;
	  }

	  public String getBicAppoggio() {
	          return bicAppoggio;
	  }

	  public void setBicAppoggio(String bicAppoggio) {
	          this.bicAppoggio = bicAppoggio;
	  }

	  public boolean isAbilitato() {
	          return abilitato;
	  }

	  public void setAbilitato(boolean abilitato) {
	          this.abilitato = abilitato;
	  }

}
