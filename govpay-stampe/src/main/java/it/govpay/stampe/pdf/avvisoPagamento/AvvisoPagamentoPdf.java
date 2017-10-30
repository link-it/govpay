package it.govpay.stampe.pdf.avvisoPagamento;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import it.govpay.model.AvvisoPagamento;
import it.govpay.model.AvvisoPagamentoInput;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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
	
	public AvvisoPagamento creaAvviso(Logger log, AvvisoPagamentoInput input, AvvisoPagamentoProperties avProperties) throws Exception {
		String codDominio = input.getDominioCreditore().getCodDominio();
		Properties propertiesAvvisoPerDominio = avProperties.getPropertiesPerDominio(codDominio, log);
		
		// leggo il file jasper da inizializzare
		String jasperTemplateFilename = propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_TEMPLATE_JASPER);
		
		if(!jasperTemplateFilename.startsWith("/"))
			jasperTemplateFilename = "/" + jasperTemplateFilename; 
			
		InputStream is = AvvisoPagamentoPdf.class.getResourceAsStream(jasperTemplateFilename);
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);
		Map<String, Object> parameters = new HashMap<String, Object>();
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters );
		
		byte[] reportToPdf = JasperExportManager.exportReportToPdf(jasperPrint);
		
		AvvisoPagamento avvisoPagamento = new AvvisoPagamento();
		avvisoPagamento.setBarCode(reportToPdf);
		
		return avvisoPagamento;
	}
}
