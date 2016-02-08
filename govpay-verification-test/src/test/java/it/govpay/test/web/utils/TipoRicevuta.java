/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.govpay.it
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
package it.govpay.test.web.utils;

public enum TipoRicevuta {
	ESEGUITO ("R01"), 
	ESEGUITO_ImportoTotalePagatoErrato ("R07"), 
	ESEGUITO_ConRitardoRT("R09"),
	NON_ESEGUITO ("R02"), 
	NON_ESEGUITO_NoDatiSingoloPagamento ("R08"),
	ESEGUITO_PARZIALE ("R03"), 
	DECORRENZA ("R04"), 
	DECORRENZA_PARZIALE ("R05"), 
	SINTASSI_ERRATA ("R06"); 
	
	String codice;
	private TipoRicevuta(String codice) {
		this.codice = codice;
	}
	
	public static TipoRicevuta getTipoRicevuta(String codice) {
		if(codice == null) return ESEGUITO;
		if(codice.endsWith("R01")) return ESEGUITO;
		if(codice.endsWith("R02")) return NON_ESEGUITO;
		if(codice.endsWith("R03")) return ESEGUITO_PARZIALE;
		if(codice.endsWith("R04")) return DECORRENZA;
		if(codice.endsWith("R05")) return DECORRENZA_PARZIALE;
		if(codice.endsWith("R06")) return SINTASSI_ERRATA;
		if(codice.endsWith("R07")) return ESEGUITO_ImportoTotalePagatoErrato;
		if(codice.endsWith("R08")) return NON_ESEGUITO_NoDatiSingoloPagamento;
		if(codice.endsWith("R09")) return ESEGUITO_ConRitardoRT;
		return ESEGUITO;
	}
	
	public String getCodice() {
		return codice;
	}
}
