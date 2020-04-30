package it.govpay.stampe.pdf.avvisoPagamento;

public class AvvisoPagamentoCostanti {
	
	// root element elemento di input
	public static final String AVVISO_PAGAMENTO_ROOT_ELEMENT_NAME= "AvvisoPagamentoInput";
	
	// nomi properties loghi
	public static final String LOGO_ENTE = "avvisoPagamento.logo.ente";
	public static final String LOGO_PAGOPA = "avvisoPagamento.logo.pagopa";
	public static final String LOGO_APP = "avvisoPagamento.logo.app";
	public static final String LOGO_PLACE = "avvisoPagamento.logo.place";
	public static final String LOGO_SCISSORS = "avvisoPagamento.logo.scissors";
	public static final String LOGO_POSTA = "avvisoPagamento.logo.posta";
	public static final String LOGO_EURO = "avvisoPagamento.logo.euro";
	
	public static final String AVVISO_PAGAMENTO_TEMPLATE_JASPER = "/AvvisoPagamento.jasper";

	public static final String DEL_TUO_ENTE_CREDITORE = "del tuo Ente Creditore";
	public static final String DI_POSTE = "di Poste Italiane";
	
	public static final String FILLER_DATAMATRIX = "            ";
	public static final String PATTERN_DATAMATRIX = "codfase=NBPA;{0}1P1{1}{2}{3}{4}{5}A";
	public static final String PATTERN_CODELINE = "18{0}12{1}10{2}3896";
	
	public static final Integer DATAMATRIX_LUNGHEZZA_CAMPO_IMPORTO = 10;
	public static final Integer DATAMATRIX_LUNGHEZZA_CAMPO_CAUSALE = 110;
	public static final Integer DATAMATRIX_LUNGHEZZA_CAMPO_ANAGRAFICA_DEBITORE = 40;
	public static final Integer DATAMATRIX_LUNGHEZZA_CAMPO_CF_DEBITORE = 16;
	
	public static final Integer AVVISO_LUNGHEZZA_CAMPO_CAUSALE = 60;
	public static final Integer AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO = 40;
}
