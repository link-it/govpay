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
package it.govpay.core.utils.validator;

public class CostantiValidazione {

	public static final String PATTERN_IBAN_ACCREDITO_2 = "^(?:(?:IT|SM)\\d{2}[A-Z]\\d{22}|CY\\d{2}[A-Z]\\d{23}|NL\\d{2}[A-Z]{4}\\d{10}|LV\\d{2}[A-Z]{4}\\d{13}|(?:BG|BH|GB|IE)\\d{2}[A-Z]{4}\\d{14}|GI\\d{2}[A-Z]{4}\\d{15}|RO\\d{2}[A-Z]{4}\\d{16}|KW\\d{2}[A-Z]{4}\\d{22}|MT\\d{2}[A-Z]{4}\\d{23}|NO\\d{13}|(?:DK|FI|GL|FO)\\d{16}|MK\\d{17}|(?:AT|EE|KZ|LU|XK)\\d{18}|(?:BA|HR|LI|CH|CR)\\d{19}|(?:GE|DE|LT|ME|RS)\\d{20}|IL\\d{21}|(?:AD|CZ|ES|MD|SA)\\d{22}|PT\\d{23}|(?:BE|IS)\\d{24}|(?:FR|MR|MC)\\d{25}|(?:AL|DO|LB|PL)\\d{26}|(?:AZ|HU)\\d{27}|(?:GR|MU)\\d{28})$";
	public static final String PATTERN_IBAN_ACCREDITO = "[a-zA-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}";
	public static final String PATTERN_BIC_1 = "[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3,3}){0,1}";
	public static final String PATTERN_BIC = "([a-zA-Z]{4})([a-zA-Z]{2})(([2-9a-zA-Z]{1})([0-9a-np-zA-NP-Z]{1}))((([0-9a-wy-zA-WY-Z]{1})([0-9a-zA-Z]{2}))|([xX]{3})|)";
	public static final String PATTERN_ID_INTERMEDIARIO = "(^([0-9]){11}$)";
	public static final String PATTERN_ID_STAZIONE = "(^([0-9]){11}_([0-9]){2}$)";
	public static final String PATTERN_ID_DOMINIO = "(^([0-9]){11}$)";
	public static final String PATTERN_ID_UO = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_ID_APPLICAZIONE = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_ID_ENTRATA = "(^[a-zA-Z0-9\\-_\\.]{1,35}$)";
	public static final String PATTERN_ID_TIPO_VERSAMENTO = "(^[a-zA-Z0-9\\-_\\.]{1,35}$)";
	public static final String PATTERN_ID_RUOLO = "(^[a-zA-Z0-9\\-_]{1,255}$)";
	public static final String PATTERN_PRINCIPAL = "(^.{1,4000}$)";
	public static final String PATTERN_ID_PENDENZA = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_ID_VOCE_PENDENZA = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_PROVINCIA = "(^[A-Z]{2,2}$)";
	public static final String PATTERN_COD_CONTABILITA = "(^\\S{3,138}$)";
	public static final String PATTERN_CODICE_TASSONOMICO_PAGOPA = "(^[0129]\\/\\S{3,138}$)";
	public static final String PATTERN_ID_DIREZIONE = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_ID_DIVISIONE = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_ANNO_RIFERIMENTO = "(^[0-9]{4}$)";
	public static final String PATTERN_NUMERO_AVVISO = "(^[0-9]{18}$)";
	public static final String PATTERN_ID_DOCUMENTO = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_ID_RICONCILIAZIONE = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	
	public static final String PATTERN_NAZIONE = "(^[A-Z]{2,2}$)";
	public static final String PATTERN_EMAIL = "(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)";
	public static final String PATTERN_CELLULARE = "\\+[0-9]{2,2}\\s[0-9]{3,3}\\-[0-9]{7,7}";
	
	public static final String PATTERN_G_RECAPTCHA_RESPONSE = "(^[a-zA-Z0-9\\-_]+$)";
	
	public static final String PATTERN_HOST_SERVER_SMTP = "(^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9-]*[a-zA-Z0-9]).)*([A-Za-z]|[A-Za-z][A-Za-z0-9-]*[A-Za-z0-9])$)";
	public static final String PATTERN_PORT_SERVER_SMTP = "(^[0-9]+$)";
	public static final String PATTERN_USERNAME = "(^[a-zA-Z0-9\\-_]+$)";
	
	public static final String PATTERN_NO_WHITE_SPACES = "(^\\S+$)";
	
	public static final String PATTERN_PSSWRD_HTTP_BASIC_DEFAULT = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?!.*[\\s]).{8,}$";
	public static final String PATTERN_PSSWRD_HTTP_BASIC_WITH_SPECIAL_CHAR = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=(?:.*[!@#$%^&*-]){1})(?!.*[\\s]).{8,}$";
	public static final String PATTERN_PSSWRD_DEFAULT = "^.{1,255}$"; 
	
	
	public static final String PATTERN_CF = "^[A-Z]{6}[0-9LMNPQRSTUV]{2}[ABCDEHLMPRST][0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{3}[A-Z]$";
	
	public static final String MASSIMALE_IMPORTO_PENDENZA_SANP_3 = "999999999.99";
	
	// Messaggi Errore BigDecimal Validator 
	
	public static final String BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_SUPERARE_LE_1_CIFRE = "Il campo {0} non deve superare le {1} cifre.";
	public static final String BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_SUPERIORE_O_UGUALE_A_1 = "Il campo {0} deve essere superiore o uguale a {1}.";
	public static final String BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_INFERIORE_O_UGUALE_A_1 = "Il campo {0} deve essere inferiore o uguale a {1}.";
	public static final String BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_SUPERIORE_A_1 = "Il campo {0} deve essere superiore a {1}.";
	public static final String BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_INFERIORE_A_1 = "Il campo {0} deve essere inferiore a {1}.";
	public static final String BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO = "Il campo {0} deve essere vuoto.";
	public static final String BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO = "Il campo {0} non deve essere vuoto.";
	public static final String BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_SUPERARE_LE_2_CIFRE_DECIMALI = "Il campo {0} non deve superare le 2 cifre decimali.";
	
	// Messaggi Errore BigInteger Validator
	
	public static final String BIG_INTEGER_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_SUPERIORE_O_UGUALE_A_1 = "Il campo {0} deve essere superiore o uguale a {1}.";
	public static final String BIG_INTEGER_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_INFERIORE_O_UGUALE_A_1 = "Il campo {0} deve essere inferiore o uguale a {1}.";
	public static final String BIG_INTEGER_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_SUPERIORE_A_1 = "Il campo {0} deve essere superiore a {1}.";
	public static final String BIG_INTEGER_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_INFERIORE_A_1 = "Il campo {0} deve essere inferiore a {1}.";
	public static final String BIG_INTEGER_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO = "Il campo {0} deve essere vuoto.";
	public static final String BIG_INTEGER_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO = "Il campo {0} non deve essere vuoto.";

	// Messaggi Errore Boolean Validator
	
	public static final String BOOLEAN_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO = "Il campo {0} deve essere vuoto.";
	public static final String BOOLEAN_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO = "Il campo {0} non deve essere vuoto.";
	
	// Messaggi Errore Date Validator
	
	public static final String DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_UNA_DATA_OLTRE_1 = "Il campo {0} deve avere una data oltre {1}.";
	public static final String DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_UNA_DATA_ENTRO_1_GIORNI = "Il campo {0} deve avere una data entro {1} giorni .";
	public static final String DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_UNA_DATA_ENTRO_1 = "Il campo {0} deve avere una data entro {1}.";
	public static final String DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_UNA_DATA_PRECEDENTE_A_1 = "Il campo {0} deve avere una data precedente a {1}.";
	public static final String DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_UNA_DATA_SUCCESSIVA_A_1 = "Il campo {0} deve avere una data successiva a {1}.";
	public static final String DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO = "Il campo {0} deve essere vuoto.";
	public static final String DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO = "Il campo {0} non deve essere vuoto.";
	public static final String DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_CONTIENE_UNA_DATA_VALIDA = "Il campo {0} non contiene una data valida.";
	
	// Messaggi Errore Double Validator
	
	public static final String DOUBLE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_SUPERIORE_O_UGUALE_A_1 = "Il campo {0} deve essere superiore o uguale a {1}.";
	public static final String DOUBLE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_INFERIORE_O_UGUALE_A_1 = "Il campo {0} deve essere inferiore o uguale a {1}.";
	public static final String DOUBLE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_SUPERIORE_A_1 = "Il campo {0} deve essere superiore a {1}.";
	public static final String DOUBLE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_INFERIORE_A_1 = "Il campo {0} deve essere inferiore a {1}.";
	public static final String DOUBLE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO = "Il campo {0} deve essere vuoto.";
	public static final String DOUBLE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO = "Il campo {0} non deve essere vuoto.";
	public static final String DOUBLE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_CONTIENE_UN_VALORE_NON_VALIDO = "Il campo {0} contiene un valore non valido.";
	
	// Messaggi Errore Enum Validator
	
	public static final String ENUM_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO = "Il campo {0} deve essere vuoto.";
	public static final String ENUM_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO = "Il campo {0} non deve essere vuoto.";
	
	// Messaggi Errore ObjectList Validator
	
	public static final String OBJECT_LIST_VALIDATOR_ERROR_MSG_L_ELEMENTO_IN_POSIZIONE_0_DEL_CAMPO_1_E_VUOTO = "L''elemento in posizione {0} del campo {1} e'' vuoto.";
	public static final String OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_MASSIMO_1_ELEMENTI = "Il campo {0} deve avere massimo {1} elementi.";
	public static final String OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_ALMENO_1_ELEMENTI = "Il campo {0} deve avere almeno {1} elementi.";
	public static final String OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO = "Il campo {0} deve essere vuoto.";
	public static final String OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO = "Il campo {0} non deve essere vuoto.";
	
	// Messaggi Errore Object Validator
	
	public static final String OBJECT_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO = "Il campo {0} deve essere vuoto.";
	public static final String OBJECT_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO = "Il campo {0} non deve essere vuoto.";
	
	// Messaggi Errore String Validator
	
	public static final String STRING_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_CONTIENE_UNA_URL_VALIDA = "Il campo {0} non contiene una URL valida.";
	public static final String STRING_VALIDATOR_ERROR_MSG_IL_VALORE_0_DEL_CAMPO_1_NON_RISPETTA_IL_PATTERN_RICHIESTO_2 = "Il valore [{0}] del campo {1} non rispetta il pattern richiesto: {2}";
	public static final String STRING_VALIDATOR_ERROR_MSG_IL_VALORE_0_DEL_CAMPO_1_NON_RISPETTA_LA_LUNGHEZZA_DI_2_CARATTERI = "Il valore [{0}] del campo {1} non rispetta la lunghezza di {2} caratteri.";
	public static final String STRING_VALIDATOR_ERROR_MSG_IL_VALORE_0_DEL_CAMPO_1_NON_RISPETTA_LA_LUNGHEZZA_MASSIMA_DI_2_CARATTERI = "Il valore [{0}] del campo {1} non rispetta la lunghezza massima di {2} caratteri.";
	public static final String STRING_VALIDATOR_ERROR_MSG_IL_VALORE_0_DEL_CAMPO_1_NON_RISPETTA_LA_LUNGHEZZA_MINIMA_DI_2_CARATTERI = "Il valore [{0}] del campo {1} non rispetta la lunghezza minima di {2} caratteri.";
	public static final String STRING_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO = "Il campo {0} deve essere vuoto.";
	public static final String STRING_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO = "Il campo {0} non deve essere vuoto.";

	
}
