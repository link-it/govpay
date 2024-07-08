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
package it.govpay.stampe.pdf.rt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

import it.govpay.model.RicevutaPagamento;
import it.govpay.stampe.model.RicevutaTelematicaInput;
import it.govpay.stampe.pdf.rt.utils.RicevutaTelematicaProperties;
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

public class RicevutaTelematicaPdf{
	
	
	private static RicevutaTelematicaPdf _instance = null;
	private static JAXBContext jaxbContext = null;

	public static RicevutaTelematicaPdf getInstance() {
		if(_instance == null)
			init();

		return _instance;
	}

	public static synchronized void init() {
		if(_instance == null)
			_instance = new RicevutaTelematicaPdf();
		

		if(jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(RicevutaTelematicaInput.class);
			} catch (JAXBException e) {
				LoggerWrapperFactory.getLogger(RicevutaTelematicaPdf.class).error("Errore durtante l'inizializzazione JAXB", e); 
			}
		}
	}

	public RicevutaTelematicaPdf() {
		// donothing
	}
	
	public JasperPrint creaJasperPrintRicevutaTelematica(Logger log, RicevutaTelematicaInput input,
			Properties propertiesRicevutaPerDominio, InputStream jasperTemplateInputStream,JRDataSource dataSource,Map<String, Object> parameters) throws Exception {
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperTemplateInputStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, dataSource);
		return jasperPrint;
	}
	
	public RicevutaPagamento creaRicevuta(Logger log, RicevutaTelematicaInput input, RicevutaPagamento ricevuta, RicevutaTelematicaProperties rtProperties) throws Exception {
	
		// cerco file di properties esterni per configurazioni specifiche per dominio
		String codDominio = ricevuta.getCodDominio();
		Properties propertiesRicevutaPerDominio = rtProperties.getPropertiesPerDominio(codDominio, log);

		this.caricaLoghiRicevuta(input, propertiesRicevutaPerDominio);

		// leggo il template file jasper da inizializzare
		String jasperTemplateFilename = propertiesRicevutaPerDominio.getProperty(RicevutaTelematicaCostanti.RICEVUTA_TELEMATICA_TEMPLATE_JASPER);

		if(!jasperTemplateFilename.startsWith("/"))
			jasperTemplateFilename = "/" + jasperTemplateFilename; 
		
		Map<String, Object> parameters = new HashMap<>();
		
		JRGzipVirtualizer virtualizer = new JRGzipVirtualizer(50);
		parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		
		try (InputStream is = RicevutaTelematicaPdf.class.getResourceAsStream(jasperTemplateFilename);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			
			DefaultJasperReportsContext defaultJasperReportsContext = DefaultJasperReportsContext.getInstance();
			
			JRPropertiesUtil.getInstance(defaultJasperReportsContext).setProperty("net.sf.jasperreports.xpath.executer.factory",
                    "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
			
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			
			JAXBElement<RicevutaTelematicaInput> jaxbElement = new JAXBElement<RicevutaTelematicaInput>(new QName("", "root"), RicevutaTelematicaInput.class, null, input);
			jaxbMarshaller.marshal(jaxbElement, baos);
			byte[] byteArray = baos.toByteArray();
			log.trace("RicevutaTelematicaInput: " + new String(byteArray));
			try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);){
				
				JRDataSource dataSource = new JRXmlDataSource(defaultJasperReportsContext, byteArrayInputStream, RicevutaTelematicaCostanti.RICEVUTA_TELEMATICA_ROOT_ELEMENT_NAME);
	
				JasperReport jasperReport = (JasperReport) JRLoader.loadObject(defaultJasperReportsContext,is);
				JasperPrint jasperPrint = JasperFillManager.getInstance(defaultJasperReportsContext).fill(jasperReport, parameters, dataSource);
				
				byte[] reportToPdf = JasperExportManager.getInstance(defaultJasperReportsContext).exportToPdf(jasperPrint);
				
				ricevuta.setPdf(reportToPdf);
				return ricevuta;
			}finally {
				
			}
		}finally {
			
		}
	}
	
	public void caricaLoghiRicevuta(RicevutaTelematicaInput input, Properties propertiesRicevutaTelematicaPerDominio) {
		// valorizzo la sezione loghi
		if(input.getLogoEnte() == null)
			input.setLogoEnte(propertiesRicevutaTelematicaPerDominio.getProperty(RicevutaTelematicaCostanti.LOGO_ENTE));
		
		input.setLogoPagopa(propertiesRicevutaTelematicaPerDominio.getProperty(RicevutaTelematicaCostanti.LOGO_PAGOPA));
	}
}
