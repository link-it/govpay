/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.report;

import it.govpay.report.exporter.ReportExporter;
import it.govpay.report.exporter.ReportExporterFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;



public class ReportManager {

	private static Logger logger = LogManager.getLogger(ReportManager.class);  

	protected Map<String, Object> reportParams = new HashMap<String, Object>();

	protected String reportFileName = null;

	protected String reportExtension = null;

	protected String compiledReportFileName = null;

	protected String moduleName = null;

	private ReportExporter reportExporter = null;

	public ReportManager(String reportFileName, String reportExtension, Map<String, Object> reportParams, String moduleName) {
		this.reportFileName = reportFileName;
		this.reportExtension = reportExtension;
		this.reportParams = reportParams;
		this.compiledReportFileName = reportFileName + reportExtension.toUpperCase() + ".jasper";
		this.moduleName = moduleName;
		this.reportExporter = ReportExporterFactory.getReportExporter(reportExtension);
		logger.debug("ReportManager - costruttore:" + toString());
	}

	public String toString() {

		StringBuffer stringBuf = new StringBuffer("\nReportManager:\n");
		stringBuf.append("\treportFileName: " + reportFileName + "\n");
		stringBuf.append("\treportExtension: " + reportExtension + "\n");
		stringBuf.append("\treportParams: " + reportParams + "\n");
		stringBuf.append("\tcompiledReportFileName: " + compiledReportFileName + "\n");
		stringBuf.append("\tserviceName: " + moduleName + "\n");
		stringBuf.append("\treportExporter: " + reportExporter + "\n");
		return stringBuf.toString();
	}

	private JasperPrint generateAndFillJasperPrint(JRBeanCollectionDataSource jrDataSource) throws JRException, FileNotFoundException {

		logger.debug("generateAndFillJasperPrint[0]: jrDataSource=" + jrDataSource);

		InputStream fi = ReportManager.class.getClassLoader().getResourceAsStream("compiled/" + moduleName + "/" + compiledReportFileName);

		if (reportParams == null) 
			reportParams = new HashMap<String, Object>();
		
		
		
		logger.debug("generateAndFillJasperPrint[2]: FileInputStream=" + fi + "\nreportParams " + reportParams + "\njrDataSource=" + jrDataSource);

//		ResourceBundle configurationBundle = ResourceBundle.getBundle("JasperReports/resources/reportMessages");
//		reportParams.put(JRParameter.REPORT_RESOURCE_BUNDLE, configurationBundle);
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(fi, reportParams, jrDataSource);

		logger.debug("generateAndFillJasperPrint[3]: jasperPrint=" + jasperPrint);

		return jasperPrint;
	}

	public byte[] generateReport(Object dataObj) {

		byte[] flusso = null;

		try {
			Collection<?> dataCollection =(Collection<?>) dataObj;

			logger.debug("generateReport[0]: dataCollection = " + dataCollection);

			JRBeanCollectionDataSource jrDataSource = new JRBeanCollectionDataSource(dataCollection);

			logger.debug("generateReport[1]: jrDataSource = " + jrDataSource);

			JasperPrint jasperPrint = generateAndFillJasperPrint(jrDataSource);

			logger.debug("generateReport[2]: jasperPrint = " + jasperPrint);

			flusso = reportExporter.exportReport(jasperPrint);

		} catch (JRException e) {

			logger.error("generateReport[3]:", e);

		}

		catch (FileNotFoundException e1) {

			logger.error("generateReport[4]:", e1);

		}

		logger.debug("generateReport[5]: FINE");

		return flusso;

	}

	public Map<String, Object> getReportParams() {
		return reportParams;
	}

	public void setReportParams(Map<String, Object> map) {
		reportParams = map;
	}

}
