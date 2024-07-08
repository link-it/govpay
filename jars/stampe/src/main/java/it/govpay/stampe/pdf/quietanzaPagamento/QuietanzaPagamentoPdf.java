/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.stampe.pdf.quietanzaPagamento;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
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
	
	public byte[] creaQuietanzaPagamento(Logger log, QuietanzaPagamentoInput input, String codDominio, QuietanzaPagamentoProperties quietanzaPagamentoProperties, File jasperFile) throws Exception {
	
		// cerco file di properties esterni per configurazioni specifiche per dominio
		Properties propertiesRicevutaPerDominio = quietanzaPagamentoProperties.getPropertiesPerDominio(codDominio, log);

		this.caricaLoghiQuietanzaPagamento(input, propertiesRicevutaPerDominio);

		// leggo il template file jasper da inizializzare
		String jasperTemplateFilename = propertiesRicevutaPerDominio.getProperty(QuietanzaPagamentoCostanti.QUIETANZA_PAGAMENTO_TEMPLATE_JASPER);

		if(!jasperTemplateFilename.startsWith("/"))
			jasperTemplateFilename = "/" + jasperTemplateFilename; 
		
		InputStream isTemplate = null;
		Map<String, Object> parameters = new HashMap<>();
		JRGzipVirtualizer virtualizer = new JRGzipVirtualizer(50);
		parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			
			// leggo il template file jasper da inizializzare 
			if(jasperFile != null && jasperFile.exists()) { // se non l'ho ricevuto dall'esterno carico quello di default
				LoggerWrapperFactory.getLogger(QuietanzaPagamentoPdf.class).debug("Utilizzo il template esterno: ["+jasperFile.getAbsolutePath()+"].");
				isTemplate = new FileInputStream(jasperFile);
				parameters.put("SUBREPORT_DIR", jasperFile.getParent() + File.separatorChar);
				parameters.put("report_base_path", jasperFile.getParent() + File.separatorChar);
			} else {
				
				if(jasperFile != null) 
					LoggerWrapperFactory.getLogger(QuietanzaPagamentoPdf.class).error("Errore di configurazione: il template configurato " + jasperFile.getAbsolutePath() + " non esiste. Verra utilizzato il template di default.");
				
				if(!jasperTemplateFilename.startsWith("/"))
					jasperTemplateFilename = "/" + jasperTemplateFilename; 
				
				isTemplate = QuietanzaPagamentoPdf.class.getResourceAsStream(jasperTemplateFilename);
			}
			
			DefaultJasperReportsContext defaultJasperReportsContext = DefaultJasperReportsContext.getInstance();
			
			JRPropertiesUtil.getInstance(defaultJasperReportsContext).setProperty("net.sf.jasperreports.xpath.executer.factory",
                    "net.sf.jasperreports.jaxen.util.xml.JaxenXPathExecuterFactory");
			
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			
			JAXBElement<QuietanzaPagamentoInput> jaxbElement = new JAXBElement<QuietanzaPagamentoInput>(new QName("", QuietanzaPagamentoCostanti.QUIETANZA_PAGAMENTO_ROOT_ELEMENT_NAME), QuietanzaPagamentoInput.class, null, input);
			jaxbMarshaller.marshal(jaxbElement, baos);
			byte[] byteArray = baos.toByteArray();
			log.trace("QuietanzaPagamentoInput: " + new String(byteArray));
			try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);){
				
				JRDataSource dataSource = new JRXmlDataSource(defaultJasperReportsContext, byteArrayInputStream, QuietanzaPagamentoCostanti.QUIETANZA_PAGAMENTO_ROOT_ELEMENT_NAME);
	
				JasperReport jasperReport = (JasperReport) JRLoader.loadObject(defaultJasperReportsContext,isTemplate);
				JasperPrint jasperPrint = JasperFillManager.getInstance(defaultJasperReportsContext).fill(jasperReport, parameters, dataSource);
				
				return JasperExportManager.getInstance(defaultJasperReportsContext).exportToPdf(jasperPrint);
			}finally {
				
			}
		}finally {
			if(isTemplate != null)
				isTemplate.close();
		}
	}
	
//	public JRDataSource creaXmlDataSource(Logger log,QuietanzaPagamentoInput input) throws UtilsException, JRException, JAXBException {
////		WriteToSerializerType serType = WriteToSerializerType.XML_JAXB;
//		Marshaller jaxbMarshaller = getJAXBContextInstance().createMarshaller();
//		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
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
