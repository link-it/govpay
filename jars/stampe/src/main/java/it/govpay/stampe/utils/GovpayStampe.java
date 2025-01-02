/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.stampe.utils;

import org.slf4j.Logger;

import it.govpay.core.exceptions.ConfigException;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;
import it.govpay.stampe.pdf.prospettoRiscossioni.utils.ProspettoRiscossioniProperties;
import it.govpay.stampe.pdf.quietanzaPagamento.utils.QuietanzaPagamentoProperties;
import it.govpay.stampe.pdf.rt.utils.RicevutaTelematicaProperties;

public class GovpayStampe {
	
	private GovpayStampe() {}

	/**
	 * Inizializza l'intero modulo delle stampe
	 * @param govpayResourceDir
	 * @throws Exception
	 */
	public static synchronized void init(Logger log, String govpayResourceDir) throws ConfigException {
		AvvisoPagamentoProperties.newInstance(govpayResourceDir);
		RicevutaTelematicaProperties.newInstance(govpayResourceDir);
		ProspettoRiscossioniProperties.newInstance(govpayResourceDir);
		QuietanzaPagamentoProperties.newInstance(govpayResourceDir);
	}
}
