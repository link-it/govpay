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
package it.govpay.stampe.pdf.avvisoPagamento;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.exceptions.PropertyNotFoundException;
import it.govpay.stampe.model.AvvisoPagamentoInput;
import it.govpay.stampe.model.PaginaAvvisoDoppia;
import it.govpay.stampe.model.PaginaAvvisoMultipla;
import it.govpay.stampe.model.PaginaAvvisoSingola;
import it.govpay.stampe.model.PaginaAvvisoTripla;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;

public class AvvisoPagamentoPdf {

	private static final String LOG_MSG_AVVISO_PAGAMENTO_INPUT = "AvvisoPagamentoInput: {}";
	private static final String PROPERTY_VALUE_NET_SF_JASPERREPORTS_ENGINE_UTIL_XML_JAXEN_X_PATH_EXECUTER_FACTORY = "net.sf.jasperreports.jaxen.util.xml.JaxenXPathExecuterFactory";
	private static final String PROPERTY_NAME_NET_SF_JASPERREPORTS_XPATH_EXECUTER_FACTORY = "net.sf.jasperreports.xpath.executer.factory";
	private static AvvisoPagamentoPdf instance = null;
	private static JAXBContext jaxbContext = null;
	
	private static byte[] templateAvviso = null;
	private static byte[] templateDoppiaRata = null;
	private static byte[] templateDoppioFormato = null;
	private static byte[] templateRataMultipla = null;
	private static byte[] templateRataUnica = null;
	private static byte[] templateTriplaRata = null;
	private static byte[] templateTriploFormato = null;
	
	private static byte[] templateAvvisoPostale = null;
	private static byte[] templateBollettinoRataPostale = null;
	private static byte[] templateBollettinoTriRataPostale = null;
	private static byte[] templateDoppiaRataPostale = null;
	private static byte[] templateDoppioFormatoPostale = null;
	private static byte[] templateRataUnicaPostale = null;
	private static byte[] templateTriplaRataPostale = null;
	private static byte[] templateTriploFormatoPostale = null;
	
	private static byte[] templateViolazioneCDS = null;
	private static byte[] templateRidottoScontato = null;
	private static byte[] templateSanzione = null;
	private static byte[] templateFormato = null;
	
	private static JAXBContext jaxbContextV2 = null;
	private static byte[] templateAvvisoV2 = null;
	private static byte[] templateMonoBandV2 = null;
	private static byte[] templateTriBandV2 = null;
	private static byte[] templateRataUnicaV2 = null;
	private static byte[] templateDoppiaRataV2 = null;
	private static byte[] templateDoppioFormatoV2 = null;
	private static byte[] templateBollettinoRataV2 = null;
	private static byte[] templateDualBandV2 = null;
	
	static {
		// inizializzazione delle risorse in un blocco static per evitare problemi di concorrenza tra i thread
		try {
			jaxbContext = JAXBContext.newInstance(AvvisoPagamentoInput.class);
		} catch (JAXBException e) {
			LoggerWrapperFactory.getLogger(AvvisoPagamentoPdf.class).error("Errore durtante l'inizializzazione JAXB", e); 
		}
		
		try {
			jaxbContextV2 = JAXBContext.newInstance(it.govpay.stampe.model.v2.AvvisoPagamentoInput.class);
		} catch (JAXBException e) {
			LoggerWrapperFactory.getLogger(AvvisoPagamentoPdf.class).error("Errore durtante l'inizializzazione JAXB", e); 
		}
		
		try {
			templateAvviso = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_TEMPLATE_JASPER));
			templateDoppiaRata = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.RATA_DOPPIA_TEMPLATE_JASPER));
			templateDoppioFormato = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.DOPPIO_FORMATO_TEMPLATE_JASPER));
			templateRataMultipla = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.RATA_MULTIPLA_TEMPLATE_JASPER));
			templateRataUnica = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.RATA_UNICA_TEMPLATE_JASPER));
			templateTriplaRata = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.RATA_TRIPLA_TEMPLATE_JASPER));
			templateTriploFormato = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.TRIPLO_FORMATO_TEMPLATE_JASPER));
			
			templateAvvisoPostale = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_POSTALE_TEMPLATE_JASPER)); 
			templateBollettinoRataPostale = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.BOLLETTINO_RATA_TEMPLATE_JASPER));
			templateBollettinoTriRataPostale = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.BOLLETTINO_TRIRATA_TEMPLATE_JASPER));
			templateDoppiaRataPostale = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.RATA_DOPPIA_POSTALE_TEMPLATE_JASPER));
			templateDoppioFormatoPostale = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.DOPPIO_FORMATO_POSTALE_TEMPLATE_JASPER));
			templateRataUnicaPostale = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.RATA_UNICA_POSTALE_TEMPLATE_JASPER));
			templateTriplaRataPostale = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.RATA_TRIPLA_POSTALE_TEMPLATE_JASPER));
			templateTriploFormatoPostale = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.TRIPLO_FORMATO_POSTALE_TEMPLATE_JASPER));
			
			templateViolazioneCDS = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.VIOLAZIONE_CDS_TEMPLATE_JASPER));
			templateRidottoScontato = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.RIDOTTOSCONTATO_TEMPLATE_JASPER));
			templateSanzione = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.SANZIONE_TEMPLATE_JASPER));
			templateFormato = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.FORMATO_TEMPLATE_JASPER));
		} catch (IOException e) {
			LoggerWrapperFactory.getLogger(AvvisoPagamentoPdf.class).error("Errore durante la lettura del template jasper dell'Avviso di Pagamento", e); 
		}
		
		try {
			templateAvvisoV2 = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_TEMPLATE_JASPER_V2));
			templateMonoBandV2 = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.MONOBAND_TEMPLATE_JASPER_V2));
			templateTriBandV2 = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.TRIBAND_TEMPLATE_JASPER_V2));
			templateRataUnicaV2 = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.RATAUNICA_TEMPLATE_JASPER_V2));
			templateDoppiaRataV2 = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.RATADOPPIA_TEMPLATE_JASPER_V2));
			templateDoppioFormatoV2 = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.DOPPIOFORMATO_TEMPLATE_JASPER_V2));
			templateBollettinoRataV2 = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.BOLLETTINORATA_TEMPLATE_JASPER_V2));
			templateDualBandV2 = IOUtils.toByteArray(AvvisoPagamentoPdf.class.getResourceAsStream(AvvisoPagamentoCostanti.DUALBAND_TEMPLATE_JASPER_V2));
		} catch (IOException e) {
			LoggerWrapperFactory.getLogger(AvvisoPagamentoPdf.class).error("Errore durante la lettura del template jasper dell'Avviso di Pagamento", e); 
		}
	}
	
	public static AvvisoPagamentoPdf getInstance() {
		if(instance == null)
			init();

		return instance;
	}

	public static synchronized void init() {
		if(instance == null)
			instance = new AvvisoPagamentoPdf();
	}

	public AvvisoPagamentoPdf() {
		//donothing
	}

	public byte[] creaAvviso(Logger log, AvvisoPagamentoInput input, String codDominio, AvvisoPagamentoProperties avProperties) throws JAXBException, IOException, JRException, PropertyNotFoundException {
		if(input.getScadenzaScontato() != null) {
			return creaAvvisoViolazioneCDSInner(log, input, codDominio, avProperties);
		} else if(input.getDiPoste() != null) {
			return creaAvvisoPostaleInner(log, input, codDominio, avProperties);
		} else {
			return creaAvvisoInner(log, input, codDominio, avProperties);
		}
	}
	
	public byte[] creaAvvisoInner(Logger log, AvvisoPagamentoInput input, String codDominio, AvvisoPagamentoProperties avProperties) throws JAXBException, IOException, JRException, PropertyNotFoundException {
		// cerco file di properties esterni per configurazioni specifiche per dominio
		Properties propertiesAvvisoPerDominio = avProperties.getPropertiesPerDominio(codDominio, log);

		this.caricaLoghiAvviso(input, propertiesAvvisoPerDominio);

		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("DoppiaRata", new ByteArrayInputStream(templateDoppiaRata));
		parameters.put("DoppioFormato", new ByteArrayInputStream(templateDoppioFormato));
		parameters.put("RataMultipla", new ByteArrayInputStream(templateRataMultipla));
		parameters.put("RataUnica", new ByteArrayInputStream(templateRataUnica));
		parameters.put("TriplaRata", new ByteArrayInputStream(templateTriplaRata));
		parameters.put("TriploFormato", new ByteArrayInputStream(templateTriploFormato));
		
		JRGzipVirtualizer virtualizer = new JRGzipVirtualizer(50);
		parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		
		try (ByteArrayInputStream templateIS = new ByteArrayInputStream(templateAvviso);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			
			DefaultJasperReportsContext defaultJasperReportsContext = DefaultJasperReportsContext.getInstance();
			
			JRPropertiesUtil.getInstance(defaultJasperReportsContext).setProperty(PROPERTY_NAME_NET_SF_JASPERREPORTS_XPATH_EXECUTER_FACTORY, PROPERTY_VALUE_NET_SF_JASPERREPORTS_ENGINE_UTIL_XML_JAXEN_X_PATH_EXECUTER_FACTORY);
			
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			
			JAXBElement<AvvisoPagamentoInput> jaxbElement = new JAXBElement<>(new QName("", AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_ROOT_ELEMENT_NAME), AvvisoPagamentoInput.class, null, input);
			jaxbMarshaller.marshal(jaxbElement, baos);
			byte[] byteArray = baos.toByteArray();
			String inputString = new String(byteArray);
			log.trace(LOG_MSG_AVVISO_PAGAMENTO_INPUT, inputString);
			try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);){

				JRDataSource dataSource = new JRXmlDataSource(defaultJasperReportsContext, byteArrayInputStream,AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_ROOT_ELEMENT_NAME);
				JasperReport jasperReport = (JasperReport) JRLoader.loadObject(defaultJasperReportsContext,templateIS);
				JasperPrint jasperPrint = JasperFillManager.getInstance(defaultJasperReportsContext).fill(jasperReport, parameters, dataSource);
				
				return JasperExportManager.getInstance(defaultJasperReportsContext).exportToPdf(jasperPrint);
			}finally {
				//donothing
			}
		}finally {
			//donothing
		}
	}
	
	public byte[] creaAvvisoPostaleInner(Logger log, AvvisoPagamentoInput input, String codDominio, AvvisoPagamentoProperties avProperties) throws JAXBException, IOException, JRException, PropertyNotFoundException {
		// cerco file di properties esterni per configurazioni specifiche per dominio
		Properties propertiesAvvisoPerDominio = avProperties.getPropertiesPerDominio(codDominio, log);

		this.caricaLoghiAvviso(input, propertiesAvvisoPerDominio);

		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("BollettinoRataPostale", new ByteArrayInputStream(templateBollettinoRataPostale));
		parameters.put("BollettinoTriRataPostale", new ByteArrayInputStream(templateBollettinoTriRataPostale));
		parameters.put("DoppiaRataPostalePostale", new ByteArrayInputStream(templateDoppiaRataPostale));
		parameters.put("DoppioFormatoPostale", new ByteArrayInputStream(templateDoppioFormatoPostale));
		parameters.put("RataUnicaPostalePostale", new ByteArrayInputStream(templateRataUnicaPostale));
		parameters.put("TriplaRataPostalePostale", new ByteArrayInputStream(templateTriplaRataPostale));
		parameters.put("TriploFormatoPostale", new ByteArrayInputStream(templateTriploFormatoPostale));
		
		JRGzipVirtualizer virtualizer = new JRGzipVirtualizer(50);
		parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		
		try (ByteArrayInputStream templateIS = new ByteArrayInputStream(templateAvvisoPostale);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			
			DefaultJasperReportsContext defaultJasperReportsContext = DefaultJasperReportsContext.getInstance();
			
			JRPropertiesUtil.getInstance(defaultJasperReportsContext).setProperty(PROPERTY_NAME_NET_SF_JASPERREPORTS_XPATH_EXECUTER_FACTORY, PROPERTY_VALUE_NET_SF_JASPERREPORTS_ENGINE_UTIL_XML_JAXEN_X_PATH_EXECUTER_FACTORY);
			
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			
			JAXBElement<AvvisoPagamentoInput> jaxbElement = new JAXBElement<>(new QName("", AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_ROOT_ELEMENT_NAME), AvvisoPagamentoInput.class, null, input);
			jaxbMarshaller.marshal(jaxbElement, baos);
			byte[] byteArray = baos.toByteArray();
			String inputString = new String(byteArray);
			log.trace(LOG_MSG_AVVISO_PAGAMENTO_INPUT, inputString);
			try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);){

				JRDataSource dataSource = new JRXmlDataSource(defaultJasperReportsContext, byteArrayInputStream,AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_ROOT_ELEMENT_NAME);
				JasperReport jasperReport = (JasperReport) JRLoader.loadObject(defaultJasperReportsContext,templateIS);
				JasperPrint jasperPrint = JasperFillManager.getInstance(defaultJasperReportsContext).fill(jasperReport, parameters, dataSource);
				
				return JasperExportManager.getInstance(defaultJasperReportsContext).exportToPdf(jasperPrint);
			}finally {
				//donothing
			}
		}finally {
			//donothing
		}
	}
	
	public byte[] creaAvvisoViolazioneCDSInner(Logger log, AvvisoPagamentoInput input, String codDominio, AvvisoPagamentoProperties avProperties) throws JAXBException, IOException, JRException, PropertyNotFoundException {
		// cerco file di properties esterni per configurazioni specifiche per dominio
		Properties propertiesAvvisoPerDominio = avProperties.getPropertiesPerDominio(codDominio, log);

		this.caricaLoghiAvviso(input, propertiesAvvisoPerDominio);

		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("RidottoScontato", new ByteArrayInputStream(templateRidottoScontato));
		parameters.put("Sanzione", new ByteArrayInputStream(templateSanzione));
		parameters.put("Formato", new ByteArrayInputStream(templateFormato));
		
		JRGzipVirtualizer virtualizer = new JRGzipVirtualizer(50);
		parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		
		try (ByteArrayInputStream templateIS = new ByteArrayInputStream(templateViolazioneCDS);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			
			DefaultJasperReportsContext defaultJasperReportsContext = DefaultJasperReportsContext.getInstance();
			
			JRPropertiesUtil.getInstance(defaultJasperReportsContext).setProperty(PROPERTY_NAME_NET_SF_JASPERREPORTS_XPATH_EXECUTER_FACTORY,
                    PROPERTY_VALUE_NET_SF_JASPERREPORTS_ENGINE_UTIL_XML_JAXEN_X_PATH_EXECUTER_FACTORY);
			
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			
			JAXBElement<AvvisoPagamentoInput> jaxbElement = new JAXBElement<>(new QName("", AvvisoPagamentoCostanti.VIOLAZIONE_CDS_ROOT_ELEMENT_NAME), AvvisoPagamentoInput.class, null, input);
			jaxbMarshaller.marshal(jaxbElement, baos);
			byte[] byteArray = baos.toByteArray();
			String inputString = new String(byteArray);
			log.trace(LOG_MSG_AVVISO_PAGAMENTO_INPUT, inputString);
			try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);){

				JRDataSource dataSource = new JRXmlDataSource(defaultJasperReportsContext, byteArrayInputStream,AvvisoPagamentoCostanti.VIOLAZIONE_CDS_ROOT_ELEMENT_NAME);
				JasperReport jasperReport = (JasperReport) JRLoader.loadObject(defaultJasperReportsContext,templateIS);
				JasperPrint jasperPrint = JasperFillManager.getInstance(defaultJasperReportsContext).fill(jasperReport, parameters, dataSource);
				
				return JasperExportManager.getInstance(defaultJasperReportsContext).exportToPdf(jasperPrint);
			}finally {
				//donothing
			}
		}finally {
			//donothing
		}
	}

	public void caricaLoghiAvviso(AvvisoPagamentoInput input, Properties propertiesAvvisoPerDominio) {
		// valorizzo la sezione loghi
		if(input.getLogoEnte() == null)
			input.setLogoEnte(propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.LOGO_ENTE));
	}
	
	public byte[] creaAvvisoV2(Logger log, it.govpay.stampe.model.v2.AvvisoPagamentoInput input, String codDominio, AvvisoPagamentoProperties avProperties) throws JAXBException, IOException, JRException, PropertyNotFoundException {
		// cerco file di properties esterni per configurazioni specifiche per dominio
		Properties propertiesAvvisoPerDominio = avProperties.getPropertiesPerDominio(codDominio, log);

		this.caricaLoghiAvvisoV2(input, propertiesAvvisoPerDominio);

		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("MonoBandV2", new ByteArrayInputStream(templateMonoBandV2));
		parameters.put("TriBandV2", new ByteArrayInputStream(templateTriBandV2));
		parameters.put("RataUnicaV2", new ByteArrayInputStream(templateRataUnicaV2));
		parameters.put("DoppiaRataV2", new ByteArrayInputStream(templateDoppiaRataV2));
		parameters.put("DoppioFormatoV2", new ByteArrayInputStream(templateDoppioFormatoV2));
		parameters.put("BollettinoRataV2", new ByteArrayInputStream(templateBollettinoRataV2));
		parameters.put("DualBandV2", new ByteArrayInputStream(templateDualBandV2));
		
		JRGzipVirtualizer virtualizer = new JRGzipVirtualizer(50);
		parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		
		try (ByteArrayInputStream templateIS = new ByteArrayInputStream(templateAvvisoV2);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			
			DefaultJasperReportsContext defaultJasperReportsContext = DefaultJasperReportsContext.getInstance();
			
			JRPropertiesUtil.getInstance(defaultJasperReportsContext).setProperty(PROPERTY_NAME_NET_SF_JASPERREPORTS_XPATH_EXECUTER_FACTORY,
                    PROPERTY_VALUE_NET_SF_JASPERREPORTS_ENGINE_UTIL_XML_JAXEN_X_PATH_EXECUTER_FACTORY);
			
			Marshaller jaxbMarshaller = jaxbContextV2.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			
			JAXBElement<it.govpay.stampe.model.v2.AvvisoPagamentoInput> jaxbElement = new JAXBElement<>(new QName("", AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_ROOT_ELEMENT_NAME), it.govpay.stampe.model.v2.AvvisoPagamentoInput.class, null, input);
			jaxbMarshaller.marshal(jaxbElement, baos);
			byte[] byteArray = baos.toByteArray();
			String inputString = new String(byteArray);
			log.trace(LOG_MSG_AVVISO_PAGAMENTO_INPUT, inputString);
			try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);){

				JRDataSource dataSource = new JRXmlDataSource(defaultJasperReportsContext, byteArrayInputStream,AvvisoPagamentoCostanti.AVVISO_PAGAMENTO_ROOT_ELEMENT_NAME);
				JasperReport jasperReport = (JasperReport) JRLoader.loadObject(defaultJasperReportsContext,templateIS);
				JasperPrint jasperPrint = JasperFillManager.getInstance(defaultJasperReportsContext).fill(jasperReport, parameters, dataSource);
				
				return JasperExportManager.getInstance(defaultJasperReportsContext).exportToPdf(jasperPrint);
			}finally {
				//donothing
			}
		}finally {
			//donothing
		}
	}
	
	public void caricaLoghiAvvisoV2(it.govpay.stampe.model.v2.AvvisoPagamentoInput input, Properties propertiesAvvisoPerDominio) {
		// valorizzo la sezione loghi
		if(input.getLogoEnte() == null)
			input.setLogoEnte(propertiesAvvisoPerDominio.getProperty(AvvisoPagamentoCostanti.LOGO_ENTE));
	}
	
	public static String getKeyAvviso(AvvisoPagamentoInput input) {
		List<Object> singolaOrDoppiaOrTripla = input.getPagine().getSingolaOrDoppiaOrTripla();
		
		if(!singolaOrDoppiaOrTripla.isEmpty()) {
			Object pag1Obj = singolaOrDoppiaOrTripla.get(0);
			
			if(pag1Obj instanceof PaginaAvvisoSingola) {
				return input.getCfEnte() + "," + ((PaginaAvvisoSingola) pag1Obj).getRata().getCodiceAvviso();
			}
			if(pag1Obj instanceof PaginaAvvisoDoppia) {
				return input.getCfEnte() + "," + ((PaginaAvvisoDoppia) pag1Obj).getRata().get(0).getCodiceAvviso();
			}
			if(pag1Obj instanceof PaginaAvvisoTripla) {
				return input.getCfEnte() + "," + ((PaginaAvvisoTripla) pag1Obj).getRata().get(0).getCodiceAvviso();
			}
			if(pag1Obj instanceof PaginaAvvisoMultipla) {
				return input.getCfEnte() + "," + ((PaginaAvvisoMultipla) pag1Obj).getRata().get(0).getCodiceAvviso();
			}
		}
		
		return input.getCfEnte() + "," + "Impossibile decodificare il tipo rata";
	}
}
