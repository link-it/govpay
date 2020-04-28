package it.govpay.stampe.pdf.prospettoRiscossioni;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
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

import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoPdf;
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
	
	public byte[] creaProspettoRiscossioni(Logger log, ProspettoRiscossioniInput input, ProspettoRiscossioniProperties prospettoRiscossioniProperties, File jasperFile) throws Exception {
		
		// utilizzo le properties di default per caricare i loghi pagopa e dominio di default
		Properties propertiesProspettoRiscossioniDefault = prospettoRiscossioniProperties.getPropertiesPerDominio(ProspettoRiscossioniProperties.DEFAULT_PROPS, log);
		
		for (ProspettoRiscossioneDominioInput prospettoDominio : input.getElencoProspettiDominio().getProspettoDominio()) { 
			// cerco file di properties esterni per configurazioni specifiche per dominio
			this.caricaLoghiProspettoRiscossioni(prospettoDominio, propertiesProspettoRiscossioniDefault);
		}

		InputStream isTemplate = null;
		Map<String, Object> parameters = new HashMap<>();
		
		// leggo il template file jasper da inizializzare 
		if(jasperFile != null && jasperFile.exists()) { // se non l'ho ricevuto dall'esterno carico quello di default
			isTemplate = new FileInputStream(jasperFile);
			parameters.put("SUBREPORT_DIR", jasperFile.getParent() + File.separatorChar);
			parameters.put("report_base_path", jasperFile.getParent() + File.separatorChar);
		} else {
			
			if(jasperFile != null) 
				LoggerWrapperFactory.getLogger(ProspettoRiscossioniPdf.class).error("Errore di configurazione: il template configurato " + jasperFile.getAbsolutePath() + " non esiste. Verra utilizzato il template di default.");
			
			String jasperTemplateFilename = propertiesProspettoRiscossioniDefault.getProperty(ProspettoRiscossioniCostanti.PROSPETTO_RISCOSSIONI_TEMPLATE_JASPER);
			if(!jasperTemplateFilename.startsWith("/"))
				jasperTemplateFilename = "/" + jasperTemplateFilename; 
			
			isTemplate = ProspettoRiscossioniPdf.class.getResourceAsStream(jasperTemplateFilename);
		}
		
		JRDataSource dataSource = this.creaXmlDataSource(log,input);
		JasperPrint jasperPrint = this.creaJasperPrintProspettoRiscossioni(log, input, isTemplate, dataSource, parameters);

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
	public JasperPrint creaJasperPrintProspettoRiscossioni(Logger log, ProspettoRiscossioniInput input,	 InputStream jasperTemplateInputStream,JRDataSource dataSource,Map<String, Object> parameters) throws Exception {
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperTemplateInputStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, dataSource);
		return jasperPrint;
	}
	
	public JRDataSource creaXmlDataSource(Logger log,ProspettoRiscossioniInput input) throws UtilsException, JRException, JAXBException {
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JAXBElement<ProspettoRiscossioniInput> jaxbElement = new JAXBElement<ProspettoRiscossioniInput>(new QName("", ProspettoRiscossioniCostanti.PROSPETTO_RISCOSSIONI_ROOT_ELEMENT_NAME), ProspettoRiscossioniInput.class, null, input);
		jaxbMarshaller.marshal(jaxbElement, baos);

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
