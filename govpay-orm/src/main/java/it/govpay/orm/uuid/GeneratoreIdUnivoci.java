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
package it.govpay.orm.uuid;

public class GeneratoreIdUnivoci {
	private long LastNumber;
	private static GeneratoreIdUnivoci Current;

	private GeneratoreIdUnivoci() {
		LastNumber = 0;
	}

	public static GeneratoreIdUnivoci getCurrent() {
		if (Current == null)
			Current = new GeneratoreIdUnivoci();
		return Current;
	}

	/**
	 * 
	 * @param nomeMacchina
	 * @return
	 */
	public synchronized String generaId(String nomeMacchina) {
		String mac = "DEF"; // default
		if (nomeMacchina != null)
			mac = nomeMacchina;
		if (LastNumber == 1000000)
			LastNumber = 0; // 36 raisedTo: 4 = 1679616
		else
			LastNumber++;
		String ms = new Long(System.currentTimeMillis()).toString();
		String inc = this.fillNumerico(Long.toString(LastNumber, 32).toUpperCase(), 4);
		return ms + inc + mac;
	}

	/**
	 * 
	 * @param num
	 * @param n
	 * @return
	 */
	private String fillNumerico(String num, int n) {
		String x = num;
		for (int i = x.length(); i < n; i++)
			x = "0" + x;
		return x;
	}

}
