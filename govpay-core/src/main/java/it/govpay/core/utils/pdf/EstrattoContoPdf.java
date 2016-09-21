package it.govpay.core.utils.pdf;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Dominio;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;

public class EstrattoContoPdf {
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public static void getPdfEstrattoConto(BasicBD bd, Dominio dominio, Date dataInizio, Date dataFine, String ibanAccredito, List<it.govpay.bd.model.EstrattoConto> estrattoContoList, FileOutputStream fos ,Logger log) throws Exception {
		JasperPdfExporterBuilder pdfExporter = export.pdfExporter(fos);
		JasperReportBuilder report = report();

		List<ComponentBuilder<?, ?>> cl = new ArrayList<ComponentBuilder<?,?>>();
		
		String dataInizioS = sdf.format(dataInizio);
		String dataFineS = sdf.format(dataFine);
 
		cl.add(TemplateEstrattoContoPagamenti.createTitleComponent(dataInizioS,dataFineS,log));
		
		List<Double> totale = new ArrayList<Double>();
		
		SubreportBuilder tabellaDettaglioPagamenti = TemplateEstrattoContoPagamenti.getTabellaDettaglioPagamenti(bd, estrattoContoList, totale, log);

		cl.add(TemplateEstrattoContoPagamenti.createRiepilogoComponent(bd, dominio,ibanAccredito,estrattoContoList, totale.get(0),log));
//		cl.add(createRicevutaPagamentoList(PortaleCostanti.LABEL_RICEVUTA_PAGAMENTO, rt,causale)); 

//		cl.add(cmp.verticalGap(20));
		
		
		cl.add(tabellaDettaglioPagamenti); 
		

		ComponentBuilder<?, ?>[] ca = new ComponentBuilder<?, ?>[cl.size()];

		//configure report
		report.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
		.setTemplate(TemplateEstrattoContoPagamenti.reportTemplate)
		.title(cl.toArray(ca))
		.pageFooter(TemplateEstrattoContoPagamenti.footerComponent)
		//.pageFooter(cmp.pageXofY()) 
		.toPdf(pdfExporter); 

	}

}
