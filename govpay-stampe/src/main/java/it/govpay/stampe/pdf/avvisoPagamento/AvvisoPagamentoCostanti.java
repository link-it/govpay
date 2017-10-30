package it.govpay.stampe.pdf.avvisoPagamento;

public class AvvisoPagamentoCostanti {
	
	// root element elemento di input
	public static final String AVVISO_PAGAMENTO_ROOT_ELEMENT_NAME= "input";
	
	// nomi properties loghi
	public static final String LOGO_ENTE = "avvisoPagamento.logo.ente";
	public static final String LOGO_AGID = "avvisoPagamento.logo.agid";
	public static final String LOGO_PAGOPA = "avvisoPagamento.logo.pagopa";
	public static final String LOGO_PAGOPA_90 = "avvisoPagamento.logo.pagopa90";
	public static final String LOGO_APP = "avvisoPagamento.logo.app";
	public static final String LOGO_PLACE = "avvisoPagamento.logo.place";
	public static final String LOGO_IMPORTO = "avvisoPagamento.logo.importo";
	public static final String LOGO_SCADENZA = "avvisoPagamento.logo.scadenza";
	
	public static final String PREFIX_LOGO = "avvisoPagamento.logo";
	
	public static final String AVVISO_PAGAMENTO_TEMPLATE_JASPER = "avvisoPagamento.risorse.template";
	
	// chiavi contenuto statico
	public static final String SEZIONE_DOVUTO_KEY = "sezioneDovuto";
	public static final String SEZIONE_DISPONIBILITA_KEY = "sezioneDisponibilita";
	public static final String SEZIONE_COMUNICAZIONI_KEY = "sezioneComunicazioni";
	public static final String SEZIONE_PAGOPA_KEY = "sezionePagoPA";
	
	public static final String[] SEZIONE_STATICA_KEYS  = {
			SEZIONE_DOVUTO_KEY,	SEZIONE_DISPONIBILITA_KEY,SEZIONE_COMUNICAZIONI_KEY,SEZIONE_PAGOPA_KEY
	};
	
	public static final String ENTE_CREDITORE_KEY = "$ENTE_CREDITORE$";
	public static final String URL_ENTE_CREDITORE_KEY = "$URL_ENTE_CREDITORE$";
}
