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
package it.govpay.core.utils.trasformazioni;

public class Costanti {
	
	private Costanti() {}

	public static final String MAP_RESPONSE = "responseMap";
    public static final String MAP_DATE_OBJECT = "date";
    public static final String MAP_TRANSACTION_ID_OBJECT = "transactionId";
    public static final String MAP_TRANSACTION_ID = "{transaction:id}";
    public static final String MAP_CTX_OBJECT = "context";
    public static final String MAP_HEADER = "header";
    public static final String MAP_QUERY_PARAMETER = "queryParams";
    public static final String MAP_PATH_PARAMETER  = "pathParams";
    public static final String MAP_ELEMENT_URL_REGEXP = "urlRegExp";
    public static final String MAP_ELEMENT_URL_REGEXP_PREFIX = "{"+MAP_ELEMENT_URL_REGEXP+":";
    public static final String MAP_ELEMENT_XML_XPATH = "xPath";
    public static final String MAP_ELEMENT_XML_XPATH_PREFIX = "{"+MAP_ELEMENT_XML_XPATH+":";
    public static final String MAP_ELEMENT_JSON_PATH = "jsonPath";
    public static final String MAP_ELEMENT_JSON_PATH_PREFIX = "{"+MAP_ELEMENT_JSON_PATH+":";
	
    public static final String MAP_CLASS_LOAD_STATIC = "class";
    public static final String MAP_CLASS_NEW_INSTANCE = "new";
    
    public static final String MAP_SUFFIX_RESPONSE = "Response";
    
    public static final String MAP_VERSAMENTO = "versamento";
    public static final String MAP_DOCUMENTO = "documento";
    public static final String MAP_DOMINIO = "dominio";
    public static final String MAP_APPLICAZIONE = "applicazione";
    public static final String MAP_RPT = "rpt";
    public static final String MAP_ID_TIPO_VERSAMENTO = "idTipoVersamento";
    public static final String MAP_ID_UNITA_OPERATIVA = "idUnitaOperativa";
    public static final String MAP_ID_DOMINIO = "idDominio";
    
    public static final String MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA = "contentTypePromemoria";
    public static final String MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA_DEFAULT_VALUE = "text/html";
    
    public static final String MAP_LINEA_CSV_RICHIESTA = "lineaCsvRichiesta";
    
    public static final String MAP_CSV_HEADER_RISPOSTA = "headerRisposta";
    
    public static final String MAP_CSV_TIPO_OPERAZIONE = "tipoOperazione";
    public static final String MAP_CSV_ESITO_OPERAZIONE = "esitoOperazione";
    public static final String MAP_CSV_DESCRIZIONE_ESITO_OPERAZIONE = "descrizioneEsitoOperazione";
    
}
