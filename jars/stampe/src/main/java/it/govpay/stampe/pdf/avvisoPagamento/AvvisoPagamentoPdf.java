package it.govpay.stampe.pdf.avvisoPagamento;

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
import org.openspcoop2.utils.UtilsException;
import org.slf4j.Logger;

import it.govpay.stampe.model.AvvisoPagamentoInput;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class AvvisoPagamentoPdf {

	private static AvvisoPagamentoPdf _instance = null;
	private static JAXBContext jaxbContext = null;

	public static AvvisoPagamentoPdf getInstance() {
		if(_instance == null)
			init();

		return _instance;
	}

	public static synchronized void init() {
		if(_instance == null)
			_instance = new AvvisoPagamentoPdf();
		
		if(jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(AvvisoPagamentoInput.class);
			} catch (JAXBException e) {
				LoggerWrapperFactory.getLogger(AvvisoPagamentoPdf.class).error("Errore durtante l'inizializzazione JAXB", e); 
			}
		}
	}

	public AvvisoPagamentoPdf() {
	}


	public JasperPrint creaJasperPrintAvviso(Logger log, AvvisoPagamentoInput input, Properties propertiesAvvisoPerDominio, InputStream jasperTemplateInputStream,JRDataSource dataSource,Map<String, Object> parameters) throws Exception {
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperTemplateInputStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, dataSource);
		return jasperPrint;
	}

	public byte[] creaAvviso(Logger log, AvvisoPagamentoInput input, String codDominio, AvvisoPagamentoProperties avProperties) throws Exception {
		// cerco file di properties esterni per configurazioni specifiche per dominio
		Properties propertiesAvvisoPerDominio = avProperties.getPropertiesPerDominio(codDominio, log);

		this.caricaLoghiAvviso(input, propertiesAvvisoPerDominio);

		// leggo il template file jasper da inizializzare
		String jasperTemplateFilename = propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_TEMPLATE_JASPER);

		if(!jasperTemplateFilename.startsWith("/"))
			jasperTemplateFilename = "/" + jasperTemplateFilename; 

		InputStream is = AvvisoPagamentoPdf.class.getResourceAsStream(jasperTemplateFilename);
		Map<String, Object> parameters = new HashMap<>();
		JRDataSource dataSource = this.creaXmlDataSource(log,input);
		JasperPrint jasperPrint = this.creaJasperPrintAvviso(log, input, propertiesAvvisoPerDominio, is, dataSource,parameters);

		byte[] reportToPdf = JasperExportManager.exportReportToPdf(jasperPrint);
		return reportToPdf;
	}

	public JRDataSource creaXmlDataSource(Logger log,AvvisoPagamentoInput input) throws UtilsException, JRException, JAXBException {
//		WriteToSerializerType serType = WriteToSerializerType.XML_JAXB;
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JAXBElement<AvvisoPagamentoInput> jaxbElement = new JAXBElement<AvvisoPagamentoInput>(new QName("", "AvvisoPagamentoInput"), AvvisoPagamentoInput.class, null, input);
		jaxbMarshaller.marshal(jaxbElement, baos);
		System.out.println(new String(baos.toByteArray()));
		JRDataSource dataSource = new JRXmlDataSource(new ByteArrayInputStream(baos.toByteArray()),AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_ROOT_ELEMENT_NAME);
		return dataSource;
	}

	public void caricaLoghiAvviso(AvvisoPagamentoInput input, Properties propertiesAvvisoPerDominio) {
		// valorizzo la sezione loghi
		if(input.getLogoEnte() == null)
			input.setLogoEnte(propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.LOGO_ENTE));
	}


}
