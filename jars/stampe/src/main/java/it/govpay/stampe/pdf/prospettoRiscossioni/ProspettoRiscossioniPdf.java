package it.govpay.stampe.pdf.prospettoRiscossioni;

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

import it.govpay.stampe.pdf.prospettoRiscossioni.utils.ProspettoRiscossioniProperties;
import it.govpay.stampe.model.ProspettoRiscossioneDominioInput;
import it.govpay.stampe.model.ProspettoRiscossioniInput;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class ProspettoRiscossioniPdf {

	private static ProspettoRiscossioniPdf _instance = null;
	private static JAXBContext jaxbContext = null;

	public static ProspettoRiscossioniPdf getInstance() {
		if(_instance == null)
			init();

		return _instance;
	}

	public static synchronized void init() {
		if(_instance == null)
			_instance = new ProspettoRiscossioniPdf();
		

		if(jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(ProspettoRiscossioniInput.class);
			} catch (JAXBException e) {
				LoggerWrapperFactory.getLogger(ProspettoRiscossioniPdf.class).error("Errore durtante l'inizializzazione JAXB", e); 
			}
		}
	}

	public ProspettoRiscossioniPdf() {

	}
	
	public byte[] creaProspettoRiscossioni(Logger log, ProspettoRiscossioniInput input, ProspettoRiscossioniProperties prospettoRiscossioniProperties) throws Exception {
		
		// utilizzo le properties di default per caricare i loghi pagopa e dominio di default
		Properties propertiesProspettoRiscossioniDefault = prospettoRiscossioniProperties.getPropertiesPerDominio(ProspettoRiscossioniProperties.DEFAULT_PROPS, log);
		
		for (ProspettoRiscossioneDominioInput prospettoDominio : input.getElencoProspettiDominio().getProspettoDominio()) { 
			// cerco file di properties esterni per configurazioni specifiche per dominio
			this.caricaLoghiProspettoRiscossioni(prospettoDominio, propertiesProspettoRiscossioniDefault);
		}

		// leggo il template file jasper da inizializzare 
		String jasperTemplateFilename = propertiesProspettoRiscossioniDefault.getProperty(ProspettoRiscossioniCostanti.PROSPETTO_RISCOSSIONI_TEMPLATE_JASPER);

		if(!jasperTemplateFilename.startsWith("/"))
			jasperTemplateFilename = "/" + jasperTemplateFilename; 

		InputStream is = ProspettoRiscossioniPdf.class.getResourceAsStream(jasperTemplateFilename);
		Map<String, Object> parameters = new HashMap<>();
		JRDataSource dataSource = this.creaXmlDataSource(log,input);
		JasperPrint jasperPrint = this.creaJasperPrintProspettoRiscossioni(log, input, is, dataSource,parameters);

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
	public JasperPrint creaJasperPrintProspettoRiscossioni(Logger log, ProspettoRiscossioniInput input,	 InputStream jasperTemplateInputStream,JRDataSource dataSource,Map<String, Object> parameters) throws Exception {
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperTemplateInputStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, dataSource);
		return jasperPrint;
	}
	
	public JRDataSource creaXmlDataSource(Logger log,ProspettoRiscossioniInput input) throws UtilsException, JRException, JAXBException {
//		WriteToSerializerType serType = WriteToSerializerType.XML_JAXB;
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JAXBElement<ProspettoRiscossioniInput> jaxbElement = new JAXBElement<ProspettoRiscossioniInput>(new QName("", "input"), ProspettoRiscossioniInput.class, null, input);
		jaxbMarshaller.marshal(jaxbElement, baos);

		//log.debug(baos.toString());
		
		JRDataSource dataSource = new JRXmlDataSource(new ByteArrayInputStream(baos.toByteArray()),ProspettoRiscossioniCostanti.PROSPETTO_RISCOSSIONI_ROOT_ELEMENT_NAME);
		return dataSource;
	}
	
	public void caricaLoghiProspettoRiscossioni(ProspettoRiscossioneDominioInput input, Properties propertiesProspettoRiscossioniPerDominio) {
		// valorizzo la sezione loghi
		if(input.getLogoEnte() == null)
			input.setLogoEnte(propertiesProspettoRiscossioniPerDominio.getProperty(ProspettoRiscossioniCostanti.LOGO_ENTE));
		
		input.setLogoPagopa(propertiesProspettoRiscossioniPerDominio.getProperty(ProspettoRiscossioniCostanti.LOGO_PAGOPA));
	}
}
