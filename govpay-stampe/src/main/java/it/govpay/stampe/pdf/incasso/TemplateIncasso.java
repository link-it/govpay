package it.govpay.stampe.pdf.incasso;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import it.govpay.model.Applicazione;
import it.govpay.model.Incasso;
import it.govpay.model.Pagamento;
import it.govpay.stampe.pdf.Costanti;
import it.govpay.stampe.pdf.TemplateBase;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;

public class TemplateIncasso {
	
	
	
	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createTitleComponent(Incasso incasso, Logger log,List<String> errList,String pathLoghi) {
		try{

			StringBuilder errMsg = new StringBuilder();
			List<ComponentBuilder<?, ?>> lst = new ArrayList<ComponentBuilder<?,?>>();

			String titoloReport =Costanti.TITOLO_REPORT_INCASSO;
			
			lst.add(cmp.text(titoloReport).setStyle(TemplateBase.bold18LeftStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

			if(errMsg.length() >0){
				errList.add(errMsg.toString());
			}

			return cmp.horizontalList(lst.toArray(new ComponentBuilder[lst.size()])).newRow().add(cmp.verticalGap(20)).newRow();
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
	
	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createRiepilogoComponent(Incasso incasso, List<Pagamento> pagamentiList,Applicazione applicazione, Double totale, Logger log) {
		try{
			return cmp.horizontalList(
					createRiepilogoGeneraleSx(incasso, pagamentiList, totale ,log),createRiepilogoGeneraleDx(incasso, pagamentiList,applicazione, totale, log)
					).newRow();
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createRiepilogoGeneraleSx(Incasso incasso, List<Pagamento> pagamentiList,Double totale,Logger log) {
		try{
			HorizontalListBuilder listRiepilogo = cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.fontStyle12).setLeftPadding(10).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)); 

			TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_TRN, incasso.getTrn(), true, false, true);
			TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_CAUSALE, incasso.getCausale(), true, false, true);
			String tot = Costanti.LABEL_EURO + " " + String.format("%.2f", (double)totale.doubleValue());
			TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_IMPORTO, tot, true, false, true);
			if(incasso.getDataContabile() != null)
				TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_DATA_CONTABILE, TemplateBase.sdf_ddMMyyyy.format(incasso.getDataContabile()), true, false, true);
			if(incasso.getDataValuta() != null)
				TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_DATA_VALUTA,  TemplateBase.sdf_ddMMyyyy.format(incasso.getDataValuta()), true, false, true);

			return listRiepilogo;
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createRiepilogoGeneraleDx(Incasso incasso, List<Pagamento> pagamentiList, Applicazione applicazione, Double totale, Logger log) {
		try{
			HorizontalListBuilder listRiepilogo = cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.fontStyle12).setLeftPadding(10).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)); 

			TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_DISPOSITIVO, incasso.getDispositivo(), true, false, true);
			TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_APPLICAZIONE, applicazione.getCodApplicazione(), true, false, true);
			if(incasso.getDataIncasso() != null)
				TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_DATA_INCASSO, TemplateBase.sdf_ddMMyyyy.format(incasso.getDataIncasso()), true, false, true);
			TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_NUMERO_PAGAMENTI, pagamentiList.size() + "", true, false, true);

			return listRiepilogo;
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return null;
	}

	public static SubreportBuilder getTabellaDettaglioPagamenti(List<Pagamento> pagamentiList, List<Double> totale, Logger log) throws Exception{

		// Scittura Intestazione
		List<ColumnBuilder<?, ?>> colonne = new ArrayList<ColumnBuilder<?, ?>>();

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

		DRDataSource dataSource = createDataSource(pagamentiList,totale);

		String titoloTabella = Costanti.LABEL_DETTAGLIO_PAGAMENTI;
		return cmp.subreport(
				report()
				.setTemplate(TemplateBase.reportTemplate)
				.title(cmp.text(titoloTabella).setStyle(TemplateBase.bold18CenteredStyle.italic()),cmp.verticalGap(20))
				.columns(colonne.toArray(new ColumnBuilder[colonne.size()]))
				.setDataSource(dataSource)
				.pageFooter(TemplateBase.footerComponent)
				//.setTableOfContents(true)
				);
	}
	
	public static DRDataSource createDataSource(List<Pagamento> list, List<Double> totale) {
		List<String> header = new ArrayList<String>();

		Double tot = 0D;
		
		header.add(Costanti.CREDITORE_COL);
		header.add(Costanti.IUV_COL);
		header.add(Costanti.IUR_COL);
		header.add(Costanti.IMPORTO_COL);
		header.add(Costanti.DATA_PAGAMENTO_COL);

		DRDataSource dataSource = new DRDataSource(header.toArray(new String[header.size()]));
		for (Pagamento pagamento : list) {

			List<String> oneLine = new ArrayList<String>();

			if(StringUtils.isNotEmpty(pagamento.getCodDominio()))
				oneLine.add(pagamento.getCodDominio());
			else 
				oneLine.add("");

			if(StringUtils.isNotEmpty(pagamento.getIuv()))
				oneLine.add(pagamento.getIuv());
			else 
				oneLine.add("");
			
			if(StringUtils.isNotEmpty(pagamento.getIur()))
				oneLine.add(pagamento.getIur());
			else 
				oneLine.add("");

			if(pagamento.getImportoPagato() != null){
				tot = tot.doubleValue() + pagamento.getImportoPagato().doubleValue();
				oneLine.add(Costanti.LABEL_EURO +" "+pagamento.getImportoPagato());
			}else 
				oneLine.add("");

			if(pagamento.getDataPagamento() != null)
				oneLine.add(TemplateBase.sdf_ddMMyyyy.format(pagamento.getDataPagamento()));
			else 
				oneLine.add("");

			dataSource.add(oneLine.toArray(new Object[oneLine.size()]));
		}

		totale.add(tot);
		return dataSource;
	}
}
