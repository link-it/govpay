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
	
	private Costanti () {}

	public static final String PARAMETRO_PAGINA = "pagina";
	public static final String PARAMETRO_RISULTATI_PER_PAGINA = "risultatiPerPagina";
	
	
	public static final Integer NUMERO_MASSIMO_RISULTATI_PER_PAGINA = 500;
	
	public static final String PARAMETRO_CONTENT_DISPOSITION = "Content-Disposition";
	public static final String PREFIX_CONTENT_DISPOSITION = "form-data; name=\"";
	public static final String SUFFIX_CONTENT_DISPOSITION = "\"";
	public static final String PREFIX_FILENAME = "filename=\"";
	public static final String SUFFIX_FILENAME = "\"";

	public static final String ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN = "Errore durante la serializzazione del FaultBean"; 
	public static final String LOG_MSG_ESECUZIONE_METODO_COMPLETATA = "Esecuzione {} completata.";
	public static final String LOG_MSG_ESECUZIONE_METODO_IN_CORSO = "Esecuzione {} in corso...";
	public static final String LOG_MSG_ESECUZIONE_OPERAZIONE_COMPLETATA = "Esecuzione {} completata: Stato {}, Descrizione: {}";
	
	public static final String HEADER_NAME_OUTPUT_TRANSACTION_ID = "X-Govpay-IdTransazione";
	
	public static final String GOVPAY = "GovPay";
	
	
	/** COSTANTI OPERAZIONI VERIFICA PENDENZA*/
	
	public static final String VERIFICA_PENDENZE_GET_AVVISO_OPERATION_ID = "verificaAvviso";
	public static final String VERIFICA_PENDENZE_VERIFY_PENDENZA_OPERATION_ID = "verificaPendenza";
	
	/** GESTIONE AVVISI CON DEBITORE ANONIMO */
	public static final String IDENTIFICATIVO_DEBITORE_ANONIMO = "ANONIMO";
	
	
	/** NOMI PARAMETRI */
	public static final String PARAM_DATA_RT_A = "dataRtA";
	public static final String PARAM_DATA_RT_DA = "dataRtDa";
	public static final String PARAM_DATA_RPT_A = "dataRptA";
	public static final String PARAM_DATA_RPT_DA = "dataRptDa";
	public static final String PARAM_ID_PENDENZA = "idPendenza";
	public static final String PARAM_ID_A2A = "idA2A";
	public static final String PARAM_ID_DOMINIO = "idDominio";
	
	/** NOMI HEADERS */
	public static final String HEADER_NAME_CONTENT_DISPOSITION = "content-disposition";
	public static final String HEADER_NAME_ACCEPT = "Accept";
	
	/** ALTRE COSTANTI */
	public static final String MEDIA_TYPE_APPLICATION_PDF = "application/pdf";
	public static final String CHARSET_UTF_8 = "UTF-8";
}
