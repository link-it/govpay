package it.govpay.stampe.pdf.avvisoPagamento;

public class AvvisoPagamentoCostanti {
	
	// root element elemento di input
	public static final String AVVISO_PAGAMENTO_ROOT_ELEMENT_NAME= "AvvisoPagamentoInput";
	public static final String VIOLAZIONE_CDS_ROOT_ELEMENT_NAME= "ViolazioneInput";
	
	// nomi properties loghi
	public static final String LOGO_ENTE = "avvisoPagamento.logo.ente";
	public static final String LOGO_PAGOPA = "avvisoPagamento.logo.pagopa";
	public static final String LOGO_APP = "avvisoPagamento.logo.app";
	public static final String LOGO_PLACE = "avvisoPagamento.logo.place";
	public static final String LOGO_SCISSORS = "avvisoPagamento.logo.scissors";
	public static final String LOGO_POSTA = "avvisoPagamento.logo.posta";
	public static final String LOGO_EURO = "avvisoPagamento.logo.euro";
	
	public static final String AVVISO_PAGAMENTO_TEMPLATE_JASPER = "/AvvisoPagamento.jasper";
	public static final String BOLLETTINORATA_TEMPLATE_JASPER = "/BollettinoRata.jasper";
	public static final String BOLLETTINOTRIRATA_TEMPLATE_JASPER = "/BollettinoTriRata.jasper";
	public static final String RATADOPPIA_TEMPLATE_JASPER = "/DoppiaRata.jasper";
	public static final String RATADOPPIAPOSTALE_TEMPLATE_JASPER = "/DoppiaRataPostale.jasper";
	public static final String DOPPIOFORMATO_TEMPLATE_JASPER = "/DoppioFormato.jasper";
	public static final String MONOBAND_TEMPLATE_JASPER = "/MonoBand.jasper";
	public static final String RATAUNICA_TEMPLATE_JASPER = "/RataUnica.jasper";
	public static final String TRIBAND_TEMPLATE_JASPER = "/TriBand.jasper";
	public static final String RATATRIPLA_TEMPLATE_JASPER = "/TriplaRata.jasper";
	public static final String RATATRIPLAPOSTALE_TEMPLATE_JASPER = "/TriplaRataPostale.jasper";
	public static final String TRIPLOFORMATO_TEMPLATE_JASPER = "/TriploFormato.jasper";
	
	public static final String VIOLAZIONE_CDS_DIR = "/violazioneCDS";
	
	public static final String VIOLAZIONE_CDS_TEMPLATE_JASPER = "/ViolazioneCDS.jasper";
	public static final String RIDOTTOSCONTATO_TEMPLATE_JASPER = "/RidottoScontato.jasper";
	public static final String SANZIONE_TEMPLATE_JASPER = "/Sanzione.jasper";
	public static final String FORMATO_TEMPLATE_JASPER = "/Formato.jasper";
	
	public static final String AVVISO_PAGAMENTO_TEMPLATE_JASPER_V2 = "/AvvisoPagamentoV2.jasper";
	public static final String MONOBAND_TEMPLATE_JASPER_V2 = "/MonoBandV2.jasper";
	public static final String TRIBAND_TEMPLATE_JASPER_V2 = "/TriBandV2.jasper";
	public static final String RATAUNICA_TEMPLATE_JASPER_V2 = "/RataUnicaV2.jasper";
	public static final String RATADOPPIA_TEMPLATE_JASPER_V2 = "/DoppiaRataV2.jasper";
	public static final String DOPPIOFORMATO_TEMPLATE_JASPER_V2 = "/DoppioFormatoV2.jasper";
	public static final String BOLLETTINORATA_TEMPLATE_JASPER_V2 = "/BollettinoRataV2.jasper";
	public static final String DUALBAND_TEMPLATE_JASPER_V2 = "/DualBandV2.jasper";
	
	public static final String AVVISO_PAGAMENTO_V2_DIR = "/avvisoPagamentoV2";
	
	public static final String AVVISO_PAGAMENTO_TEMPLATE_JASPER_V3 = "/AvvisoPagamentoV3.jasper";
	public static final String BOLLETTINORATA_TEMPLATE_JASPER_V3 = "/BollettinoRataV3.jasper";
	public static final String RATADOPPIAPOSTALE_TEMPLATE_JASPER_V3 = "/DoppiaRataPostaleV3.jasper";
	public static final String RATADOPPIA_TEMPLATE_JASPER_V3 = "/DoppiaRataV3.jasper";
	public static final String DOPPIOFORMATO_TEMPLATE_JASPER_V3 = "/DoppioFormatoV3.jasper";
	public static final String DUALBAND_TEMPLATE_JASPER_V3 = "/DualBandV3.jasper";
	public static final String MONOBAND_TEMPLATE_JASPER_V3 = "/MonoBandV3.jasper";
	public static final String RATAUNICA_TEMPLATE_JASPER_V3 = "/RataUnicaV3.jasper";
	
	public static final String AVVISO_PAGAMENTO_V3_DIR = "/avvisoPagamentoV3";

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
