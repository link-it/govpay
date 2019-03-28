package it.govpay.core.utils.validator;

public class CostantiValidazione {

	public static final String PATTERN_IBAN_ACCREDITO = "^(?:(?:IT|SM)\\d{2}[A-Z]\\d{22}|CY\\d{2}[A-Z]\\d{23}|NL\\d{2}[A-Z]{4}\\d{10}|LV\\d{2}[A-Z]{4}\\d{13}|(?:BG|BH|GB|IE)\\d{2}[A-Z]{4}\\d{14}|GI\\d{2}[A-Z]{4}\\d{15}|RO\\d{2}[A-Z]{4}\\d{16}|KW\\d{2}[A-Z]{4}\\d{22}|MT\\d{2}[A-Z]{4}\\d{23}|NO\\d{13}|(?:DK|FI|GL|FO)\\d{16}|MK\\d{17}|(?:AT|EE|KZ|LU|XK)\\d{18}|(?:BA|HR|LI|CH|CR)\\d{19}|(?:GE|DE|LT|ME|RS)\\d{20}|IL\\d{21}|(?:AD|CZ|ES|MD|SA)\\d{22}|PT\\d{23}|(?:BE|IS)\\d{24}|(?:FR|MR|MC)\\d{25}|(?:AL|DO|LB|PL)\\d{26}|(?:AZ|HU)\\d{27}|(?:GR|MU)\\d{28})$";

	public static final String PATTERN_ID_INTERMEDIARIO = "(^([0-9]){11}$)";
	public static final String PATTERN_ID_STAZIONE = "(^([0-9]){11}_([0-9]){2}$)";
	public static final String PATTERN_ID_DOMINIO = "(^([0-9]){11}$)";
	public static final String PATTERN_ID_UO = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_ID_APPLICAZIONE = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_ID_ENTRATA = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_ID_TIPO_VERSAMENTO = "(^[a-zA-Z0-9\\-_]{1,35}$)";
	public static final String PATTERN_ID_RUOLO = "(^[a-zA-Z0-9\\-_]{1,255}$)";
	public static final String PATTERN_PRINCIPAL = "(^.{1,4000}$)";
}


 
