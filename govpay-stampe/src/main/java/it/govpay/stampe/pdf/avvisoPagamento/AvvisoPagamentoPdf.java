package it.govpay.stampe.pdf.avvisoPagamento;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.utils.beans.WriteToSerializerType;

import it.govpay.model.avvisi.AvvisoPagamento;
import it.govpay.model.avvisi.AvvisoPagamentoInput;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class AvvisoPagamentoPdf {

	private static AvvisoPagamentoPdf _instance = null;

	public static AvvisoPagamentoPdf getInstance() {
		if(_instance == null)
			init();

		return _instance;
	}

	public static synchronized void init() {
		if(_instance == null)
			_instance = new AvvisoPagamentoPdf();
	}

	public AvvisoPagamentoPdf() {

	}

	public AvvisoPagamento creaAvviso(Logger log, AvvisoPagamentoInput input, AvvisoPagamento avvisoPagamento, AvvisoPagamentoProperties avProperties) throws Exception {

		String codDominio = avvisoPagamento.getCodDominio();
		Properties propertiesAvvisoPerDominio = avProperties.getPropertiesPerDominio(codDominio, log);

		// leggo il file jasper da inizializzare
		String jasperTemplateFilename = propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_TEMPLATE_JASPER);

		if(!jasperTemplateFilename.startsWith("/"))
			jasperTemplateFilename = "/" + jasperTemplateFilename; 



		//		InputStream is = AvvisoPagamentoPdf.class.getResourceAsStream(jasperTemplateFilename);
		InputStream is = new FileInputStream("/home/pintori/Downloads/Jasper/AvvisoPagamento.jasper");
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ente_denominazione","Comune di San Valentino in Abruzzo Citeriore");
		parameters.put("ente_logo",propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.LOGO_ENTE));


		// valorizzare la sezione loghi
		input.setEnteLogo(propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.LOGO_ENTE).getBytes());
		input.setAgidLogo(propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.LOGO_AGID).getBytes());
		input.setPagopaLogo(propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.LOGO_PAGOPA).getBytes());
		input.setAppLogo(propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.LOGO_APP).getBytes());
		input.setPlaceLogo(propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.LOGO_PLACE).getBytes());
		input.setImportoLogo(propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.LOGO_IMPORTO).getBytes());
		input.setScadenzaLogo(propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.LOGO_SCADENZA).getBytes());

		WriteToSerializerType serType = WriteToSerializerType.JAXB;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// [TODO] convertire l'input in datasource xml
			input.writeTo(os, serType);
			JRDataSource dataSource = new JRXmlDataSource(new ByteArrayInputStream(os.toByteArray()),"/input");
//		JRDataSource dataSource = new AvvisoPagamentoDatasource(toMap(input),log); 
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, dataSource);

		//byte[] reportToPdf = JasperExportManager.exportReportToPdf(jasperPrint);

		//avvisoPagamento.setPdf(reportToPdf);

		JasperExportManager.exportReportToPdfFile(jasperPrint,"/tmp/tmp.pdf");

		return avvisoPagamento;
	}


	public static void main(String[] args) throws Exception {
		Logger log = LogManager.getLogger();

		AvvisoPagamentoProperties.newInstance("/var/govpay");


		AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();
		AvvisoPagamento av = new AvvisoPagamento();
		av.setCodDominio("83000390019");



		AvvisoPagamentoInput input = new AvvisoPagamentoInput();
		input.setEnteDenominazione("Comune di San Valentino in Abruzzo Citeriore");
		input.setEnteArea("Area di sviluppo per le politiche agricole e forestali");
		input.setEnteIdentificativo("83000390019");
		input.setEnteCbill("AAAAAAA");
		input.setEnteUrl("www.comune.sanciprianopicentino.sa.it/");
		input.setEntePeo("info@comune.sancipriano.sa.it");
		input.setEntePec("protocollo@pec.comune.sanciprianopicentino.sa.it");
		input.setEntePartner("Link.it Srl");
		input.setIntestatarioDenominazione("Lorenzo Nardi");
		input.setIntestatarioIdentificativo("NRDLNA80P19D612M");
		input.setIntestatarioIndirizzo1("Via di Corniola 119A");
		input.setIntestatarioIndirizzo2("50053 Empoli (FI");
		input.setAvvisoCausale("Pagamento diritti di segreteria per il rilascio in duplice copia della documentazione richiesta.");
		input.setAvvisoImporto(9999999.99);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/Mm/yyyy");
		Date scad = sdf.parse("31/12/2020");
		input.setAvvisoScadenza(scad);
		input.setAvvisoNumero("399000012345678900");
		input.setAvvisoIuv("99000012345678900");
		input.setAvvisoBarcode("415808888880094580203990000123456789003902222250");
		input.setAvvisoQrcode("PAGOPA|002|399000012345678900|83000390019|222250");


		av = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, av, avProperties);

		System.out.println("FINE");
		//System.out.println(input.toXml_Jaxb()); 

	}


	public static Map<String, Object> toMap(AvvisoPagamentoInput input){
		Map<String, Object> map = new HashMap<String, Object>();

		/*
     	<ente_denominazione>Comune di San Valentino in Abruzzo Citeriore</ente_denominazione>
        <ente_area>Area di sviluppo per le politiche agricole e forestali</ente_area>
        <ente_identificativo>83000390019</ente_identificativo>
        <ente_cbill>AAAAAAA</ente_cbill>
        <ente_url>www.comune.sanciprianopicentino.sa.it/</ente_url>
        <ente_peo>info@comune.sancipriano.sa.it</ente_peo>
        <ente_pec>protocollo@pec.comune.sanciprianopicentino.sa.it</ente_pec>
        <ente_partner>Link.it Srl</ente_partner>
        <intestatario_denominazione>Lorenzo Nardi</intestatario_denominazione>
        <intestatario_identificativo>NRDLNA80P19D612M</intestatario_identificativo>
        <intestatario_indirizzo_1>Via di Corniola 119A</intestatario_indirizzo_1>
        <intestatario_indirizzo_2>50053 Empoli (FI)</intestatario_indirizzo_2>
        <avviso_causale>Pagamento diritti di segreteria per il rilascio in duplice copia della documentazione richiesta.</avviso_causale>
        <avviso_importo>9999999.99</avviso_importo>
        <avviso_scadenza>31/12/2020</avviso_scadenza>
        <avviso_numero>399000012345678900</avviso_numero>
        <avviso_iuv>99000012345678900</avviso_iuv>
        <avviso_barcode>415808888880094580203990000123456789003902222250</avviso_barcode>
        <avviso_qrcode>PAGOPA|002|399000012345678900|83000390019|222250</avviso_qrcode>

		 * */

		if(input.getEnteLogo() != null)
			map.put("ente_logo", new String(input.getEnteLogo()));
		if(input.getAgidLogo() != null)
			map.put("agid_logo", new String(input.getAgidLogo()));
		if(input.getPagopaLogo() != null)
			map.put("pagopa_logo", new String(input.getPagopaLogo()));
		if(input.getAppLogo() != null)
			map.put("app_logo", new String(input.getAppLogo()));
		if(input.getPlaceLogo() != null)
			map.put("place_logo", new String(input.getPlaceLogo()));
		if(input.getImportoLogo() != null)
			map.put("importo_logo", new String(input.getImportoLogo()));
		if(input.getScadenzaLogo() != null)
			map.put("scadenza_logo", new String(input.getScadenzaLogo()));

		if(input.getEnteDenominazione() != null)
			map.put("ente_denominazione", input.getEnteDenominazione());
		if(input.getEnteArea() != null)
			map.put("ente_area", input.getEnteArea());
		if(input.getEnteIdentificativo() != null)
			map.put("ente_identificativo", input.getEnteIdentificativo());
		if(input.getEnteCbill() != null)
			map.put("ente_cbill", input.getEnteCbill());
		if(input.getEnteUrl() != null)
			map.put("ente_url", input.getEnteUrl());
		if(input.getEntePeo() != null)
			map.put("ente_peo", input.getEntePeo());
		if(input.getEntePec() != null)
			map.put("ente_pec", input.getEntePec());
		if(input.getEntePartner() != null)
			map.put("ente_partner", input.getEntePartner());
		if(input.getIntestatarioDenominazione() != null)
			map.put("intestatario_denominazione", input.getIntestatarioDenominazione());
		if(input.getIntestatarioIdentificativo() != null)
			map.put("intestatario_identificativo", input.getIntestatarioIdentificativo());
		if(input.getIntestatarioIndirizzo1() != null)
			map.put("intestatario_indirizzo_1", input.getIntestatarioIndirizzo1());
		if(input.getIntestatarioIndirizzo2() != null)
			map.put("intestatario_indirizzo_2", input.getIntestatarioIndirizzo2());
		if(input.getAvvisoCausale() != null)
			map.put("avviso_causale", input.getAvvisoCausale());
		//if(input.getAvvisoImporto() != null)
		map.put("avviso_importo", input.getAvvisoImporto());
		if(input.getAvvisoScadenza() != null)
			map.put("avviso_scadenza", input.getAvvisoScadenza());
		if(input.getAvvisoNumero() != null)
			map.put("avviso_numero", input.getAvvisoNumero());
		if(input.getAvvisoIuv() != null)
			map.put("avviso_iuv", input.getAvvisoIuv());
		if(input.getAvvisoBarcode() != null)
			map.put("avviso_barcode", input.getAvvisoBarcode());
		if(input.getAvvisoQrcode() != null)
			map.put("avviso_qrcode", input.getAvvisoQrcode());

		return map;
	}
}
