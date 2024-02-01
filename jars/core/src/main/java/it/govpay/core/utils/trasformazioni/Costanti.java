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
package it.govpay.core.utils.trasformazioni;

public class Costanti {

	public final static String MAP_RESPONSE = "responseMap";
    public final static String MAP_DATE_OBJECT = "date";
    public final static String MAP_TRANSACTION_ID_OBJECT = "transactionId";
    public final static String MAP_TRANSACTION_ID = "{transaction:id}";
    public final static String MAP_CTX_OBJECT = "context";
    public final static String MAP_HEADER = "header";
    public final static String MAP_QUERY_PARAMETER = "queryParams";
    public final static String MAP_PATH_PARAMETER  = "pathParams";
    public final static String MAP_ELEMENT_URL_REGEXP = "urlRegExp";
    public final static String MAP_ELEMENT_URL_REGEXP_PREFIX = "{"+MAP_ELEMENT_URL_REGEXP+":";
    public final static String MAP_ELEMENT_XML_XPATH = "xPath";
    public final static String MAP_ELEMENT_XML_XPATH_PREFIX = "{"+MAP_ELEMENT_XML_XPATH+":";
    public final static String MAP_ELEMENT_JSON_PATH = "jsonPath";
    public final static String MAP_ELEMENT_JSON_PATH_PREFIX = "{"+MAP_ELEMENT_JSON_PATH+":";
	
    public final static String MAP_CLASS_LOAD_STATIC = "class";
    public final static String MAP_CLASS_NEW_INSTANCE = "new";
    
    public final static String MAP_SUFFIX_RESPONSE = "Response";
    
    public final static String MAP_VERSAMENTO = "versamento";
    public final static String MAP_DOCUMENTO = "documento";
    public final static String MAP_DOMINIO = "dominio";
    public final static String MAP_APPLICAZIONE = "applicazione";
    public final static String MAP_RPT = "rpt";
    public final static String MAP_ID_TIPO_VERSAMENTO = "idTipoVersamento";
    public final static String MAP_ID_UNITA_OPERATIVA = "idUnitaOperativa";
    public final static String MAP_ID_DOMINIO = "idDominio";
    
    public final static String MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA = "contentTypePromemoria";
    public final static String MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA_DEFAULT_VALUE = "text/html";
    
    public final static String MAP_LINEA_CSV_RICHIESTA = "lineaCsvRichiesta";
    
    public final static String MAP_CSV_HEADER_RISPOSTA = "headerRisposta";
    
    public final static String MAP_CSV_TIPO_OPERAZIONE = "tipoOperazione";
    public final static String MAP_CSV_ESITO_OPERAZIONE = "esitoOperazione";
    public final static String MAP_CSV_DESCRIZIONE_ESITO_OPERAZIONE = "descrizioneEsitoOperazione";
    
}
