package it.govpay.stampe.pdf.avvisoPagamento;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
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

import it.govpay.model.avvisi.AvvisoPagamento;
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


	public JasperPrint creaJasperPrintAvviso(Logger log, AvvisoPagamentoInput input, AvvisoPagamento avvisoPagamento, 
			Properties propertiesAvvisoPerDominio, InputStream jasperTemplateInputStream,JRDataSource dataSource,Map<String, Object> parameters) throws Exception {
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperTemplateInputStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, dataSource);
		return jasperPrint;
	}

	public AvvisoPagamento creaAvviso(Logger log, AvvisoPagamentoInput input, AvvisoPagamento avvisoPagamento, AvvisoPagamentoProperties avProperties) throws Exception {
		// cerco file di properties esterni per configurazioni specifiche per dominio
		String codDominio = avvisoPagamento.getCodDominio();
		Properties propertiesAvvisoPerDominio = avProperties.getPropertiesPerDominio(codDominio, log);

		this.caricaLoghiAvviso(input, propertiesAvvisoPerDominio);

		// leggo il template file jasper da inizializzare
		String jasperTemplateFilename = propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_TEMPLATE_JASPER);

		if(!jasperTemplateFilename.startsWith("/"))
			jasperTemplateFilename = "/" + jasperTemplateFilename; 

		InputStream is = AvvisoPagamentoPdf.class.getResourceAsStream(jasperTemplateFilename);
		Map<String, Object> parameters = new HashMap<>();
		JRDataSource dataSource = this.creaXmlDataSource(log,input);
		JasperPrint jasperPrint = this.creaJasperPrintAvviso(log, input, avvisoPagamento, propertiesAvvisoPerDominio, is, dataSource,parameters);

		byte[] reportToPdf = JasperExportManager.exportReportToPdf(jasperPrint);
		avvisoPagamento.setPdf(reportToPdf);
		return avvisoPagamento;
	}

	public JRDataSource creaXmlDataSource(Logger log,AvvisoPagamentoInput input) throws UtilsException, JRException, JAXBException {
//		WriteToSerializerType serType = WriteToSerializerType.XML_JAXB;
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JAXBElement<AvvisoPagamentoInput> jaxbElement = new JAXBElement<AvvisoPagamentoInput>(new QName("", "input"), AvvisoPagamentoInput.class, null, input);
		jaxbMarshaller.marshal(jaxbElement, baos);
		JRDataSource dataSource = new JRXmlDataSource(new ByteArrayInputStream(baos.toByteArray()),AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_ROOT_ELEMENT_NAME);
		return dataSource;
	}

	public void caricaLoghiAvviso(AvvisoPagamentoInput input, Properties propertiesAvvisoPerDominio) {
		// valorizzo la sezione loghi
		if(input.getLogoEnte() == null)
			input.setLogoEnte(propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.LOGO_ENTE));
	}


	public static void main(String[] args) throws Exception {
		try (InputStream jasperTemplateInputStream = new FileInputStream("FILE_PATH/AvvisoPagamento.jasper");) {
			Logger log = LoggerWrapperFactory.getLogger(AvvisoPagamentoPdf.class);

			AvvisoPagamentoProperties.newInstance("/var/govpay");

			AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();
			AvvisoPagamento av = new AvvisoPagamento();
			av.setCodDominio("83000390019");

			String codDominio = av.getCodDominio();
			Properties propertiesAvvisoPerDominio = avProperties.getPropertiesPerDominio(codDominio, log);

			Map<String, Object> parameters = new HashMap<>();
			AvvisoPagamentoInput input = new AvvisoPagamentoInput();
			AvvisoPagamentoPdf.getInstance().caricaLoghiAvviso(input, propertiesAvvisoPerDominio);

			input.setEnteCreditore("Comune di San Valentino in Abruzzo Citeriore"); 
			input.setSettoreEnte("Area di sviluppo per le politiche agricole e forestali");
			input.setCfEnte("83000390019");
			input.setCbill("AAAAAAA");
			input.setInfoEnte("www.comune.sanciprianopicentino.sa.it/ <br/> info@comune.sancipriano.sa.it <br/> protocollo@pec.comune.sanciprianopicentino.sa.it");
			input.setNomeCognomeDestinatario("Lorenzo Nardi");
			input.setCfDestinatario("NRDLNA80P19D612M");
			input.setIndirizzoDestinatario1("Via di Corniola 119A,");
			input.setIndirizzoDestinatario2(" 50053 Empoli (FI)");
			input.setOggettoDelPagamento("Pagamento diritti di segreteria per il rilascio in duplice copia della documentazione richiesta.");
			input.setImporto(9999999.99);
			input.setData("31/12/2020");
			input.setCodiceAvviso("399000012345678900");
			input.setDataMatrix("01034531200000111719112510ABCD1234");
			input.setQrCode("PAGOPA|002|399000012345678900|83000390019|222250");
			input.setDelTuoEnte(AvvisoPagamentoCostanti.DEL_TUO_ENTE_CREDITORE);

			JRDataSource dataSource = AvvisoPagamentoPdf.getInstance().creaXmlDataSource(log,input);
			JasperPrint jasperPrint = AvvisoPagamentoPdf.getInstance().creaJasperPrintAvviso(log, input, av, propertiesAvvisoPerDominio, jasperTemplateInputStream, dataSource,parameters);

			JasperExportManager.exportReportToPdfFile(jasperPrint,"/tmp/tmp.pdf");

			//System.out.println(input.toXml_Jaxb()); 
			System.out.println("FINE");
		}catch(Exception e ) {
			throw e;
		}  
	}
}
