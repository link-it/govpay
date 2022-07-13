package it.govpay.stampe.pdf.quietanzaPagamento;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.stampe.model.QuietanzaPagamentoInput;
import it.govpay.stampe.pdf.quietanzaPagamento.utils.QuietanzaPagamentoProperties;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;

public class QuietanzaPagamentoPdf {

	private static QuietanzaPagamentoPdf _instance = null;
	private static JAXBContext jaxbContext = null;

	public static QuietanzaPagamentoPdf getInstance() {
		if(_instance == null)
			init();

		return _instance;
	}
	
	public static JAXBContext getJAXBContextInstance() {
		if(jaxbContext == null)
			init();

		return jaxbContext;
	}

	public static synchronized void init() {
		if(_instance == null)
			_instance = new QuietanzaPagamentoPdf();
		

		if(jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(QuietanzaPagamentoInput.class);
			} catch (JAXBException e) {
				LoggerWrapperFactory.getLogger(QuietanzaPagamentoPdf.class).error("Errore durtante l'inizializzazione JAXB", e); 
			}
		}
	}

	public QuietanzaPagamentoPdf() {

	}
	
//	public JasperPrint creaJasperPrintQuietanzaPagamento(Logger log, QuietanzaPagamentoInput input,
//			Properties propertiesRicevutaPerDominio, InputStream jasperTemplateInputStream,JRDataSource dataSource,Map<String, Object> parameters) throws Exception {
//		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperTemplateInputStream);
//		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, dataSource);
//		return jasperPrint;
//	}
	
	public byte[] creaQuietanzaPagamento(Logger log, QuietanzaPagamentoInput input, String codDominio, QuietanzaPagamentoProperties quietanzaPagamentoProperties) throws Exception {
	
		// cerco file di properties esterni per configurazioni specifiche per dominio
		Properties propertiesRicevutaPerDominio = quietanzaPagamentoProperties.getPropertiesPerDominio(codDominio, log);

		this.caricaLoghiQuietanzaPagamento(input, propertiesRicevutaPerDominio);

		// leggo il template file jasper da inizializzare
		String jasperTemplateFilename = propertiesRicevutaPerDominio.getProperty(QuietanzaPagamentoCostanti.QUIETANZA_PAGAMENTO_TEMPLATE_JASPER);

		if(!jasperTemplateFilename.startsWith("/"))
			jasperTemplateFilename = "/" + jasperTemplateFilename; 
		
		Map<String, Object> parameters = new HashMap<>();
		
		JRGzipVirtualizer virtualizer = new JRGzipVirtualizer(50);
		parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		
		try (InputStream is = QuietanzaPagamentoPdf.class.getResourceAsStream(jasperTemplateFilename);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			
			DefaultJasperReportsContext defaultJasperReportsContext = DefaultJasperReportsContext.getInstance();
			
			JRPropertiesUtil.getInstance(defaultJasperReportsContext).setProperty("net.sf.jasperreports.xpath.executer.factory",
                    "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
			
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			
			JAXBElement<QuietanzaPagamentoInput> jaxbElement = new JAXBElement<QuietanzaPagamentoInput>(new QName("", "root"), QuietanzaPagamentoInput.class, null, input);
			jaxbMarshaller.marshal(jaxbElement, baos);
			byte[] byteArray = baos.toByteArray();
			log.trace("QuietanzaPagamentoInput: " + new String(byteArray));
			try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);){
				
				JRDataSource dataSource = new JRXmlDataSource(defaultJasperReportsContext, byteArrayInputStream, QuietanzaPagamentoCostanti.QUIETANZA_PAGAMENTO_ROOT_ELEMENT_NAME);
	
				JasperReport jasperReport = (JasperReport) JRLoader.loadObject(defaultJasperReportsContext,is);
				JasperPrint jasperPrint = JasperFillManager.getInstance(defaultJasperReportsContext).fill(jasperReport, parameters, dataSource);
				
				return JasperExportManager.getInstance(defaultJasperReportsContext).exportToPdf(jasperPrint);
			}finally {
				
			}
		}finally {
			
		}
	}
	
//	public JRDataSource creaXmlDataSource(Logger log,QuietanzaPagamentoInput input) throws UtilsException, JRException, JAXBException {
////		WriteToSerializerType serType = WriteToSerializerType.XML_JAXB;
//		Marshaller jaxbMarshaller = getJAXBContextInstance().createMarshaller();
//		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		JAXBElement<QuietanzaPagamentoInput> jaxbElement = new JAXBElement<QuietanzaPagamentoInput>(new QName("", "root"), QuietanzaPagamentoInput.class, null, input);
//		jaxbMarshaller.marshal(jaxbElement, baos);
//		JRDataSource dataSource = new JRXmlDataSource(new ByteArrayInputStream(baos.toByteArray()),QuietanzaPagamentoCostanti.QUIETANZA_PAGAMENTO_ROOT_ELEMENT_NAME);
//		return dataSource;
//	}
	
	public void caricaLoghiQuietanzaPagamento(QuietanzaPagamentoInput input, Properties propertiesQuietanzaPagamentoPerDominio) {
		// valorizzo la sezione loghi
		if(input.getLogoEnte() == null)
			input.setLogoEnte(propertiesQuietanzaPagamentoPerDominio.getProperty(QuietanzaPagamentoCostanti.LOGO_ENTE));
		
		input.setLogoPagopa(propertiesQuietanzaPagamentoPerDominio.getProperty(QuietanzaPagamentoCostanti.LOGO_PAGOPA));
	}
}
