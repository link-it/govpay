package it.govpay.stampe.pdf.estrattoConto;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.grid;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;

import it.govpay.model.Anagrafica;
import it.govpay.model.Dominio;
import it.govpay.stampe.pdf.Costanti;
import it.govpay.stampe.pdf.TemplateBase;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.ComponentColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.grid.ColumnTitleGroupBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.datasource.DRDataSource;

public class EstrattoContoPdf {
	
	public static String getPdfEstrattoConto(String pathLoghi, Dominio dominio, Anagrafica anagraficaDominio, Date dataInizio, Date dataFine, String ibanAccredito, List<it.govpay.model.EstrattoConto> estrattoContoList, OutputStream os ,Logger log) throws Exception {
		String msg = null;
		JasperPdfExporterBuilder pdfExporter = export.pdfExporter(os);
		JasperReportBuilder report = report();

		List<ComponentBuilder<?, ?>> cl = new ArrayList<ComponentBuilder<?,?>>();
		
		String dataInizioS = dataInizio != null ? TemplateBase.sdf_ddMMyyyy.format(dataInizio) : null;
		String dataFineS = dataFine != null ? TemplateBase.sdf_ddMMyyyy.format(dataFine) : null;
 
		List<String> errList = new ArrayList<String>();
		ComponentBuilder<?, ?> titleComponent = TemplateEstrattoContoPagamenti.createTitleComponent(dominio,dataInizioS,dataFineS,log,errList, pathLoghi);
		cl.add(titleComponent);
		
		if(errList.size() > 0)
			msg = errList.get(0);
		
		List<Double> totale = new ArrayList<Double>();
		
		// Scittura Intestazione
		List<ColumnBuilder<?, ?>> colonne = new ArrayList<ColumnBuilder<?, ?>>();

		TextColumnBuilder<String> idFlussoColumn = col.column(Costanti.LABEL_ID_FLUSSO_RENDICONTAZIONE, Costanti.CODICE_RENDICONTAZIONE_COL, type.stringType()).setStyle(TemplateBase.fontStyle9)
							.setWidth(20).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		TextColumnBuilder<String> iuvColumn  = col.column(Costanti.LABEL_IUV, Costanti.IUV_COL, type.stringType()).setStyle(TemplateBase.fontStyle9)
							.setWidth(15).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		ComponentColumnBuilder componentColumnSx = col.componentColumn( TemplateEstrattoContoPagamenti.getContenutoCellaSx()).setWidth(30);
		ComponentColumnBuilder componentColumnDx = col.componentColumn( TemplateEstrattoContoPagamenti.getContenutoCellaDx()).setWidth(35);
		
		 ColumnTitleGroupBuilder titleGroup1 = grid.titleGroup(Costanti.LABEL_ULTERIORI_INFORMAZIONI, componentColumnSx,   componentColumnDx).setTitleWidth(65); 

		colonne.add(idFlussoColumn);
		colonne.add(iuvColumn);
		colonne.add(componentColumnSx);
		colonne.add(componentColumnDx);

		List<FieldBuilder<String>> fields = new ArrayList<FieldBuilder<String>>();

		fields.add(field(Costanti.IMPORTO_PAGATO_COL, String.class));
		fields.add(field(Costanti.DATA_PAGAMENTO_COL, String.class));
		fields.add(field(Costanti.CODICE_RIVERSAMENTO_COL, String.class));
		fields.add(field(Costanti.ID_REGOLAMENTO_COL, String.class));
		fields.add(field(Costanti.BIC_RIVERSAMENTO_COL, String.class));
		fields.add(field(Costanti.IDENTIFICATIVO_VERSAMENTO_COL, String.class));

		DRDataSource dataSource = TemplateEstrattoContoPagamenti.createDataSource(estrattoContoList,totale);
		
	//	SubreportBuilder tabellaDettaglioPagamenti = TemplateEstrattoContoPagamenti.getTabellaDettaglioPagamenti(bd, estrattoContoList, totale, log);

		cl.add(TemplateEstrattoContoPagamenti.createRiepilogoComponent(dominio,anagraficaDominio,ibanAccredito,estrattoContoList, totale.get(0),log));
		cl.add(cmp.verticalGap(20));
		String titoloTabella = Costanti.LABEL_DETTAGLIO_PAGAMENTI;
		cl.add(cmp.text(titoloTabella).setStyle(TemplateBase.bold18CenteredStyle.italic()));
		cl.add(cmp.verticalGap(20));
		
		
		//cl.add(tabellaDettaglioPagamenti); 
		

		ComponentBuilder<?, ?>[] ca = new ComponentBuilder<?, ?>[cl.size()];

		//configure report
		report.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
		.setTemplate(TemplateBase.reportTemplate)
		.title(cl.toArray(ca))
		.fields(fields.toArray(new FieldBuilder[fields.size()])) 
		//.title(cmp.text(titoloTabella).setStyle(TemplateEstrattoContoPagamenti.bold18CenteredStyle.italic()),cmp.verticalGap(20))
		.columnGrid(idFlussoColumn,iuvColumn,titleGroup1)
		.columns(colonne.toArray(new ColumnBuilder[colonne.size()]))
		.setDataSource(dataSource)
		//.summary(cl.toArray(ca))
		.pageFooter(TemplateBase.footerComponent)
		.toPdf(pdfExporter); 
		
		return msg;

	}

}
