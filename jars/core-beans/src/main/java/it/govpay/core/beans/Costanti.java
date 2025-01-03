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
package it.govpay.core.beans;

public class Costanti {

	public static final String PARAMETRO_PAGINA = "pagina";
	public static final String PARAMETRO_RISULTATI_PER_PAGINA = "risultatiPerPagina";
	
	
	public static final Integer NUMERO_MASSIMO_RISULTATI_PER_PAGINA = 500;
	
	
	public static final String HEADER_NAME_OUTPUT_TRANSACTION_ID = "X-Govpay-IdTransazione";
	
	public static final String GOVPAY = "GovPay";
	
	
	/** COSTANTI OPERAZIONI VERIFICA PENDENZA*/
	
	public static final String VERIFICA_PENDENZE_GET_AVVISO_OPERATION_ID = "verificaAvviso";
	public static final String VERIFICA_PENDENZE_VERIFY_PENDENZA_OPERATION_ID = "verificaPendenza";
	
	/** GESTIONE AVVISI CON DEBITORE ANONIMO */
	public static final String IDENTIFICATIVO_DEBITORE_ANONIMO = "ANONIMO";
}
