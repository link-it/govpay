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
	public static final String PATTERN_ID_ENTRATA = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_ID_TIPO_VERSAMENTO = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_ID_RUOLO = "(^[a-zA-Z0-9\\-_]{1,255}$)";
	public static final String PATTERN_PRINCIPAL = "(^.{1,4000}$)";
	public static final String PATTERN_ID_PENDENZA = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_PROVINCIA = "[A-Z]{2,2}";
	public static final String PATTERN_COD_CONTABILITA = "\\S{3,138}";
	public static final String PATTERN_ID_DIREZIONE = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_ID_DIVISIONE = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	
	public static final String PATTERN_NAZIONE = "[A-Z]{2,2}";
	public static final String PATTERN_EMAIL = "(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)";
	public static final String PATTERN_CELLULARE = "\\+[0-9]{2,2}\\s[0-9]{3,3}\\-[0-9]{7,7}";
	
	public static final String PATTERN_G_RECAPTCHA_RESPONSE = "(^[a-zA-Z0-9\\-_]+$)";
	
	public static final String PATTERN_HOST_SERVER_SMTP = "(^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9-]*[a-zA-Z0-9]).)*([A-Za-z]|[A-Za-z][A-Za-z0-9-]*[A-Za-z0-9])$)";
	public static final String PATTERN_PORT_SERVER_SMTP = "(^[0-9]+$)";
	public static final String PATTERN_USERNAME = "(^[a-zA-Z0-9\\-_]+$)";
	
	public static final String PATTERN_NO_WHITE_SPACES = "(^\\S+$)";
}


 
