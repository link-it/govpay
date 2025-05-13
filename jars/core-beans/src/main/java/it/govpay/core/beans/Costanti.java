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
	public static final String PREFIX_CONTENT_DISPOSITION_ATTACHMENT_FILENAME = "attachment; filename=\"";
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
	public static final String PARAM_DATA_A = "dataA";
	public static final String PARAM_DATA_DA = "dataDa";
	public static final String PARAM_ID_PENDENZA = "idPendenza";
	public static final String PARAM_ID_A2A = "idA2A";
	public static final String PARAM_ID_DOMINIO = "idDominio";
	public static final String PARAM_BOUNDARY = "boundary";
	public static final String PARAM_FLUSSO_RENDICONTAZIONE_DATA_FLUSSO_A = "flussoRendicontazione.dataFlussoA";
	public static final String PARAM_FLUSSO_RENDICONTAZIONE_DATA_FLUSSO_DA = "flussoRendicontazione.dataFlussoDa";
	public static final String PARAM_ID_UNITA_OPERATIVA = "idUnitaOperativa";
	public static final String PARAM_ID_TIPO_PENDENZA = "idTipoPendenza";
	public static final String PARAM_PRINCIPAL = "principal";
    public static final String PARAM_ID_STAZIONE = "idStazione";
	public static final String PARAM_ID_INTERMEDIARIO = "idIntermediario";
	public static final String PARAM_DATA_ORA_FLUSSO = "dataOraFlusso";
	public static final String PARAM_SEVERITA_DA = "severitaDa";
	public static final String PARAM_SEVERITA_A = "severitaA";
    public static final String PARAM_NUMERO_DOCUMENTO = "numeroDocumento";
	public static final String PARAM_ID_ENTRATA = "idEntrata";
	public static final String PARAM_IBAN_ACCREDITO = "ibanAccredito";
	
	/** NOMI FIELD */
	public static final String FIELD_ID_DOMINIO = "idDominio";
	
	
	/** NOMI HEADERS */
	public static final String HEADER_NAME_CONTENT_DISPOSITION = "content-disposition";
	public static final String HEADER_NAME_ACCEPT = "Accept";
	public static final String HEADER_NAME_X_GOVPAY_FILENAME = "X-GOVPAY-FILENAME";
	public static final String HEADER_NAME_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_NAME_CACHE_CONTROL = "CacheControl";
	public static final String HEADER_NAME_X_HTTP_METHOD_OVERRIDE = "X-HTTP-Method-Override";
	
	/** ALTRE COSTANTI */
	public static final String MEDIA_TYPE_APPLICATION_PDF = "application/pdf";
	public static final String MEDIA_TYPE_TEXT_CSV = "text/csv";
	public static final String MSG_FORMATO_NON_DISPONIBILE = "Formato non disponibile";
	public static final String ESTENSIONE_PUNTO_JSON = ".json";
	public static final String ESTENSIONE_PUNTO_CSV = ".csv";

	public static final String CHARSET_UTF_8 = "UTF-8";
	public static final String MULTIPART_PREFIX = "multipart";
	public static final String VALUE_MAX_AGE = "max-age: ";
	
	/** COSTANTI MESSAGGI */
	public static final String MSG_L_ENTE_CREDITORE_NON_E_TRA_QUELLI_ASSOCIATI_ALL_UTENZA = "l'ente creditore non e' tra quelli associati all'utenza";
	public static final String MSG_L_UTENZA_NON_E_ASSOCIATA_A_TUTTI_GLI_ENTI_CREDITORI_NON_PUO_DUNQUE_AUTORIZZARE_L_APPLICAZIONE_A_TUTTI_GLI_ENTI_CREDITORI = "l'utenza non e' associata a tutti gli enti creditori, non puo' dunque autorizzare l'applicazione a tutti gli enti creditori";
	public static final String MSG_L_UTENZA_NON_E_ASSOCIATA_A_TUTTI_I_TIPI_PENDENZA_NON_PUO_DUNQUE_AUTORIZZARE_L_APPLICAZIONE_A_TUTTI_I_TIPI_PENDENZA_O_ABILITARE_L_AUTODETERMINAZIONE_DEI_TIPI_PENDENZA = "l'utenza non e' associata a tutti i tipi pendenza, non puo' dunque autorizzare l'applicazione a tutti i tipi pendenza o abilitare l'autodeterminazione dei tipi pendenza";
	public static final String MSG_L_UTENZA_NON_E_ASSOCIATA_A_TUTTI_GLI_ENTI_CREDITORI_NON_PUO_DUNQUE_AUTORIZZARE_L_OPERATORE_A_TUTTI_GLI_ENTI_CREDITORI = "l'utenza non e' associata a tutti gli enti creditori, non puo' dunque autorizzare l'operatore a tutti gli enti creditori";
	public static final String MSG_ERRORE_DURANTE_IL_LOG_DELL_OPERAZIONE = "Errore durante il log dell'operazione: ";
	
	/** COSTANTI PATH */
	public static final String DETTAGLIO_PATH_PATTERN = "/allegati/"+"{0}";
	
	/** COSTANTI VALIDAZIONE */
	public static final String MSG_VALIDATION_EXCEPTION_CODIFICA_INESISTENTE_PER ="Codifica inesistente per ";
	public static final String MSG_VALIDATION_EXCEPTION_VALORE_FORNITO = ". Valore fornito [";
	public static final String MSG_VALIDATION_EXCEPTION_VALORI_POSSIBILI = "] valori possibili " ;
	public static final String MSG_VALIDATION_EXCEPTION_SUFFIX = "" ;
	public static final String LABEL_TIPO_TRASFORMAZIONE = "tipo trasformazione";
	
}
