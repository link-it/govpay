package it.govpay.stampe.pdf.incasso;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import it.govpay.model.Applicazione;
import it.govpay.model.Incasso;
import it.govpay.model.Pagamento;
import it.govpay.stampe.pdf.Costanti;
import it.govpay.stampe.pdf.TemplateBase;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.datasource.DRDataSource;

public class IncassoPdf {


	public static String getPdfIncasso(String pathLoghi, Incasso incasso, List<Pagamento> pagamentiList, Applicazione applicazione, OutputStream os, Logger log) throws Exception {
		String msg = null;
		JasperPdfExporterBuilder pdfExporter = export.pdfExporter(os);
		JasperReportBuilder report = report();

		List<ComponentBuilder<?, ?>> cl = new ArrayList<>();

		List<String> errList = new ArrayList<>();
		ComponentBuilder<?, ?> titleComponent = TemplateIncasso.createTitleComponent(incasso,log,errList, pathLoghi);
		cl.add(titleComponent);

		if(errList.size() > 0)
			msg = errList.get(0);

		List<Double> totale = new ArrayList<>();

		// Scittura Intestazione
		List<ColumnBuilder<?, ?>> colonne = new ArrayList<>();

		TextColumnBuilder<String> creditoreColumn = col.column(Costanti.LABEL_CREDITORE, Costanti.CREDITORE_COL, type.stringType()).setStyle(TemplateBase.fontStyle9)
				.setWidth(20).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		TextColumnBuilder<String> iuvColumn  = col.column(Costanti.LABEL_IUV, Costanti.IUV_COL, type.stringType()).setStyle(TemplateBase.fontStyle9)
				.setWidth(20).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		TextColumnBuilder<String> iurColumn  = col.column(Costanti.LABEL_IUR2, Costanti.IUR_COL, type.stringType()).setStyle(TemplateBase.fontStyle9)
				.setWidth(20).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		TextColumnBuilder<String> importoColumn  = col.column(Costanti.LABEL_IMPORTO, Costanti.IMPORTO_COL, type.stringType()).setStyle(TemplateBase.fontStyle9)
				.setWidth(15).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		TextColumnBuilder<String> dataColumn  = col.column(Costanti.LABEL_DATA_PAGAMENTO, Costanti.DATA_PAGAMENTO_COL, type.stringType()).setStyle(TemplateBase.fontStyle9)
				.setWidth(15).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

		colonne.add(creditoreColumn);
		colonne.add(iuvColumn);
		colonne.add(iurColumn);
		colonne.add(importoColumn);
		colonne.add(dataColumn);

		DRDataSource dataSource = TemplateIncasso.createDataSource(pagamentiList,totale);

		//	SubreportBuilder tabellaDettaglioPagamenti = TemplateEstrattoContoPagamenti.getTabellaDettaglioPagamenti(bd, estrattoContoList, totale, log);

		cl.add(TemplateIncasso.createRiepilogoComponent(incasso, pagamentiList , applicazione, totale.get(0),log));
		cl.add(cmp.verticalGap(20));
		String titoloTabella = Costanti.LABEL_DETTAGLIO_PAGAMENTI;
		cl.add(cmp.text(titoloTabella).setStyle(TemplateBase.bold18CenteredStyle.italic()));
		cl.add(cmp.verticalGap(20));

		//cl.add(tabellaDettaglioPagamenti); 

		ComponentBuilder<?, ?>[] ca = new ComponentBuilder<?, ?>[cl.size()];

		//configure report
		report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT)
		.setTemplate(TemplateBase.reportTemplate)
		.title(cl.toArray(ca))
		//.title(cmp.text(titoloTabella).setStyle(TemplateEstrattoContoPagamenti.bold18CenteredStyle.italic()),cmp.verticalGap(20))
		.columns(colonne.toArray(new ColumnBuilder[colonne.size()]))
		.setDataSource(dataSource)
		//.summary(cl.toArray(ca))
		.pageFooter(TemplateBase.footerComponent)
		.toPdf(pdfExporter); 

		return msg;
	}
}
