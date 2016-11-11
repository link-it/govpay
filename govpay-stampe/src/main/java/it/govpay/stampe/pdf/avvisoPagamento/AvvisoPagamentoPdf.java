package it.govpay.stampe.pdf.avvisoPagamento;

import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import it.govpay.model.AvvisoPagamento;
import it.govpay.stampe.pdf.TemplateBase;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;

public class AvvisoPagamentoPdf implements IAvvisoPagamento{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	public String getPdfAvvisoPagamento(String pathLoghi, AvvisoPagamento avviso, Properties properties, OutputStream os, Logger log) throws Exception {
		String msg = null;
		List<String> errList = new ArrayList<String>();
		try{

			JasperPdfExporterBuilder pdfExporter = export.pdfExporter(os);
			JasperReportBuilder report = report();

			List<ComponentBuilder<?, ?>> cl = new ArrayList<ComponentBuilder<?,?>>();


			ComponentBuilder<?, ?> createTitleComponent = TemplateAvvisoPagamento.createTitleComponent(pathLoghi,avviso,errList,log);
			if(createTitleComponent != null) {
				cl.add(createTitleComponent);
				ComponentBuilder<?, ?> createSezioneDovuto = TemplateAvvisoPagamento.createSezioneDovuto(avviso,errList,log);
				if(createSezioneDovuto != null){
					cl.add(createSezioneDovuto);
					ComponentBuilder<?, ?> createSezioneDisponibilita = TemplateAvvisoPagamento.createSezioneDisponibilita(avviso,errList,log);
					if(createSezioneDisponibilita!= null){
						cl.add(createSezioneDisponibilita);
					}
				}
			}

			// se e solo se ho generato correttamente tutte le sezioni dell'estratto conto allor ascirvo il pdf.
			if(cl.size() == 3){

				ComponentBuilder<?, ?>[] ca = new ComponentBuilder<?, ?>[cl.size()];

				//configure report
				ComponentBuilder<?, ?> createSezioneCodici = TemplateAvvisoPagamento.createSezioneCodici(avviso,errList,log);
				if(createSezioneCodici!= null){
					report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT)
					.setTemplate(TemplateBase.reportTemplate)
					.title(cl.toArray(ca))
					.pageFooter(createSezioneCodici)
					.toPdf(pdfExporter); 
				}
			}

		}catch(Exception e){
			log.error("Errore durante la generazione dell'avviso ["+avviso.getCodiceAvviso()+"]: "+ e.getMessage()); 
			errList.add(0,"Errore durante la generazione dell'avviso ["+avviso.getCodiceAvviso()+"]: "+e.getMessage());
		}

		// colleziono eventuali errori durante la generazione del pdf
		if(errList.size() > 0){
			StringBuilder sb = new StringBuilder();

			for (String errore : errList) {
				if(sb.length() > 0)
					sb.append(", ");

				sb.append(errore);
			}
			msg = sb.toString();
		}

		return msg;
	}
}
