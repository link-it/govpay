package it.govpay.stampe.pdf.estrattoConto;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.grid;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import it.govpay.model.Anagrafica;
import it.govpay.model.Dominio;
import it.govpay.model.EstrattoConto;
import it.govpay.stampe.pdf.Costanti;
import it.govpay.stampe.pdf.TemplateBase;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.ComponentColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.grid.ColumnTitleGroupBuilder;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;

public class TemplateEstrattoContoPagamenti {
	 
	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createTitleComponent(Dominio dominio, String dataInizio, String dataFine,Logger log,List<String> errList,String pathLoghi) {
		try{

			StringBuilder errMsg = new StringBuilder();
			List<ComponentBuilder<?, ?>> lst = new ArrayList<ComponentBuilder<?,?>>();
			// caricamento del logo PagoPA
			InputStream resourceLogoPagoPa = new ByteArrayInputStream(Base64.decodeBase64(Costanti.logoPagoPa));
					lst.add(cmp.image(resourceLogoPagoPa).setFixedDimension(90, 90));

			List<ComponentBuilder<?, ?>> lstTitolo = new ArrayList<ComponentBuilder<?,?>>();
			String titoloReport =Costanti.TITOLO_REPORT;
			
			lstTitolo.add(cmp.text(titoloReport).setStyle(TemplateBase.bold18LeftStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
			
			//Intervallo date Opzionale
			if(StringUtils.isNotEmpty(dataInizio) && StringUtils.isNotEmpty(dataFine)) {
				String periodoOsservazione = MessageFormat.format(Costanti.TITOLO_PERIODO, dataInizio,dataFine);
				lstTitolo.add(cmp.text(periodoOsservazione).setStyle(TemplateBase.fontStyle16).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
			}
			
			lst.add(cmp.verticalList(lstTitolo.toArray(new ComponentBuilder[lstTitolo.size()])));

			// caricamento del logo Dominio
			String logoDominio = dominio.getCodDominio() + ".png";

			File fLogoDominio = new File(pathLoghi+"/"+logoDominio);
			if(fLogoDominio.exists()){
				InputStream resourceLogoDominio = new FileInputStream(fLogoDominio);
				lst.add(cmp.image(resourceLogoDominio).setFixedDimension(80, 80).setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT));
			}else {
				if(errMsg.length() >0)
					errMsg.append(", ");

				errMsg.append(" l'estratto conto non contiene il logo del dominio poiche' il file ["+logoDominio+"] non e' stato trovato nella directory dei loghi");
			}

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
	public static ComponentBuilder<?, ?> createRiepilogoComponent(Dominio dominio, Anagrafica anagrafica, String ibanAccredito, List<it.govpay.model.EstrattoConto> estrattoContoList,Double totale,Logger log) {
		try{
			return cmp.horizontalList(
					createRiepilogoGenerale(dominio, ibanAccredito, estrattoContoList,totale,log),
					createDatiDominio(dominio,anagrafica, log)
					).newRow();
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createDatiDominio(Dominio dominio, Anagrafica anagrafica, Logger log) {
		try{
			VerticalListBuilder listDominio = cmp.verticalList().setStyle(stl.style(TemplateBase.fontStyle12).setLeftPadding(10)
					.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setHorizontalImageAlignment(HorizontalImageAlignment.CENTER)); 

			String denominazioneDominio = dominio.getRagioneSociale();
			String pIvaDominio = MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE, Costanti.LABEL_P_IVA, dominio.getCodDominio());

			String indirizzo = StringUtils.isNotEmpty(anagrafica.getIndirizzo()) ? anagrafica.getIndirizzo() : "";
			String civico = StringUtils.isNotEmpty(anagrafica.getCivico()) ? anagrafica.getCivico() : "";
			String cap = StringUtils.isNotEmpty(anagrafica.getCap()) ? anagrafica.getCap() : "";
			String localita = StringUtils.isNotEmpty(anagrafica.getLocalita()) ? anagrafica.getLocalita() : "";
			String provincia = StringUtils.isNotEmpty(anagrafica.getProvincia()) ? (" (" +anagrafica.getProvincia() +")" ) : "";


			String indirizzoCivico = indirizzo + " " + civico;
			String capCitta = cap + " " + localita + provincia      ;


			listDominio.add(cmp.text(denominazioneDominio).setStyle(TemplateBase.bold18LeftStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));//.newRow();
			listDominio.add(cmp.text(pIvaDominio).setStyle(TemplateBase.boldStyle12).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
			if(StringUtils.isNotEmpty(indirizzoCivico))
				listDominio.add(cmp.text(indirizzoCivico).setStyle(TemplateBase.fontStyle12).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
			if(StringUtils.isNotEmpty(capCitta))
				listDominio.add(cmp.text(capCitta).setStyle(TemplateBase.fontStyle12).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

			return listDominio;
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createRiepilogoGenerale(Dominio dominio,String ibanAccredito, List<it.govpay.model.EstrattoConto> estrattoContoList,Double totale,Logger log) {
		try{
			HorizontalListBuilder listRiepilogo = cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.fontStyle12).setLeftPadding(10).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)); 

			//			listRiepilogo.add(cmp.text(Costanti.LABEL_IBAN_ACCREDITO).setStyle(boldLeftStyle));
			//			listRiepilogo.add(splitIban( ibanAccredito)).newRow();

			String titoloRiepilogo = Costanti.TITOLO_RIEPILOGO;
			listRiepilogo.add(cmp.text(titoloRiepilogo).setStyle(TemplateBase.boldStyle16.italic()).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)).newRow();
			
			if(StringUtils.equals(ibanAccredito,Costanti.PAGAMENTI_SENZA_RPT_KEY))
				TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_PAGAMENTI_SENZA_RPT, "", true, false, false);
			else if(StringUtils.equals(ibanAccredito,Costanti.MARCA_DA_BOLLO_KEY))
					TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_MARCA_DA_BOLLO_TELEMATICA, "", true, false, false);
				else	
					TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_IBAN_ACCREDITO , ibanAccredito, true, false, false);
			
			TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_NUMERO_PAGAMENTI , "" + estrattoContoList.size(), true, false, false);
			String tot = Costanti.LABEL_EURO + " " + String.format("%.2f", (double)totale.doubleValue());
			TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_IMPORTO_TOTALE ,tot, true, false, false);

			return listRiepilogo;
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return null;
	}

	public static SubreportBuilder getTabellaDettaglioPagamenti(List<it.govpay.model.EstrattoConto> estrattoContoList,List<Double> totale ,Logger log) throws Exception{

		// Scittura Intestazione
		List<ColumnBuilder<?, ?>> colonne = new ArrayList<ColumnBuilder<?, ?>>();

		TextColumnBuilder<String> idFlussoColumn = col.column(Costanti.LABEL_ID_FLUSSO_RENDICONTAZIONE, Costanti.CODICE_RENDICONTAZIONE_COL, type.stringType()).setStyle(TemplateBase.fontStyle9)
				.setWidth(20).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		TextColumnBuilder<String> iuvColumn  = col.column(Costanti.LABEL_IUV, Costanti.IUV_COL, type.stringType()).setStyle(TemplateBase.fontStyle9)
				.setWidth(15).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		ComponentColumnBuilder componentColumnSx = col.componentColumn( getContenutoCellaSx()).setWidth(30);
		ComponentColumnBuilder componentColumnDx = col.componentColumn( getContenutoCellaDx()).setWidth(35);

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

		DRDataSource dataSource = createDataSource(estrattoContoList,totale);

		String titoloTabella = Costanti.LABEL_DETTAGLIO_PAGAMENTI;
		return cmp.subreport(
				report()
				.setTemplate(TemplateBase.reportTemplate)
				.fields(fields.toArray(new FieldBuilder[fields.size()])) 
				.title(cmp.text(titoloTabella).setStyle(TemplateBase.bold18CenteredStyle.italic()),cmp.verticalGap(20))
				.columnGrid(idFlussoColumn,iuvColumn,titleGroup1)
				.columns(colonne.toArray(new ColumnBuilder[colonne.size()]))
				.setDataSource(dataSource)
				.pageFooter(TemplateBase.footerComponent)
				//.setTableOfContents(true)
				);
	}

	public static HorizontalListBuilder getContenutoCellaDx() {
		HorizontalListBuilder itemComponent = cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.fontStyle9).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)); //.setLeftPadding(10));

		//		if(StringUtils.isNotEmpty(pag.getIur()))
		TemplateBase.addElementoLista(itemComponent, Costanti.LABEL_ID_RIVERSAMENTO,  new TemplateEstrattoContoPagamenti().new IdRiversamentoExpression(), true,false,true);
		//		if(StringUtils.isNotEmpty(pag.getCodBicRiversamento()))
		TemplateBase.addElementoLista(itemComponent, Costanti.LABEL_BIC_RIVERSAMENTO,  new TemplateEstrattoContoPagamenti().new BicRiversamentoExpression(), true,false,true);
		//		if(StringUtils.isNotEmpty(pag.getIdRegolamento()))
		TemplateBase.addElementoLista(itemComponent, Costanti.LABEL_ID_REGOLAMENTO,  new TemplateEstrattoContoPagamenti().new IdRegolamentoExpression(), true,false,true);

		return itemComponent;
	}

	public static HorizontalListBuilder getContenutoCellaSx() {
		HorizontalListBuilder itemComponent = cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.fontStyle9).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)); //.setLeftPadding(10)); 

		//		if(StringUtils.isNotEmpty(pag.getCodSingoloVersamentoEnte()))
		TemplateBase.addElementoLista(itemComponent, Costanti.LABEL_CODICE_VERSAMENTO_ENTE, new TemplateEstrattoContoPagamenti().new CodSingoloVersamentoEnteExpression(), true,false,true);
		//		if(pag.getImportoPagato() != null)
		TemplateBase.addElementoLista(itemComponent, Costanti.LABEL_IMPORTO,  new TemplateEstrattoContoPagamenti().new ImportoPagatoExpression(), true,false,true);
		//		if(pag.getDataPagamento() != null)
		TemplateBase.addElementoLista(itemComponent, Costanti.LABEL_DATA_PAGAMENTO,  new TemplateEstrattoContoPagamenti().new DataPagamentoExpression(), true,false,true);

		return itemComponent;
	}

	public class CodSingoloVersamentoEnteExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public String evaluate(ReportParameters reportParameters) {
			return "<b>" + Costanti.LABEL_CODICE_VERSAMENTO_ENTE + "</b>: " + reportParameters.getValue(Costanti.IDENTIFICATIVO_VERSAMENTO_COL);
		}
	}

	public class BicRiversamentoExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public String evaluate(ReportParameters reportParameters) {
			return "<b>" + Costanti.LABEL_BIC_RIVERSAMENTO + "</b>: " + reportParameters.getValue(Costanti.BIC_RIVERSAMENTO_COL);
		}
	}

	public class IdRegolamentoExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public String evaluate(ReportParameters reportParameters) {
			return "<b>" + Costanti.LABEL_ID_REGOLAMENTO + "</b>: " + reportParameters.getValue(Costanti.ID_REGOLAMENTO_COL);
		}
	}

	public class ImportoPagatoExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public String evaluate(ReportParameters reportParameters) {
			return "<b>" + Costanti.LABEL_IMPORTO + "</b>: " + Costanti.LABEL_EURO +" "+reportParameters.getValue(Costanti.IMPORTO_PAGATO_COL);
		}
	}

	public class IdRiversamentoExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public String evaluate(ReportParameters reportParameters) {
			return "<b>" + Costanti.LABEL_ID_RIVERSAMENTO + "</b>: " + reportParameters.getValue(Costanti.CODICE_RIVERSAMENTO_COL);
		}
	}

	public class DataPagamentoExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public String evaluate(ReportParameters reportParameters) {
			return "<b>" + Costanti.LABEL_DATA_PAGAMENTO + "</b>: " + reportParameters.getValue(Costanti.DATA_PAGAMENTO_COL);
		}
	}

	public static DRDataSource createDataSource(List<EstrattoConto> list,List<Double> totale ) {
		List<String> header = new ArrayList<String>();

		header.add(Costanti.IDENTIFICATIVO_VERSAMENTO_COL);
		header.add(Costanti.IUV_COL);
		header.add(Costanti.IMPORTO_PAGATO_COL);
		header.add(Costanti.DATA_PAGAMENTO_COL);
		header.add(Costanti.CODICE_RIVERSAMENTO_COL);
		header.add(Costanti.CODICE_RENDICONTAZIONE_COL);
		header.add(Costanti.BIC_RIVERSAMENTO_COL);
		header.add(Costanti.ID_REGOLAMENTO_COL);

		Double tot = 0D;

		DRDataSource dataSource = new DRDataSource(header.toArray(new String[header.size()]));
		for (EstrattoConto pagamento : list) {

			List<String> oneLine = new ArrayList<String>();

			if(StringUtils.isNotEmpty(pagamento.getCodSingoloVersamentoEnte()))
				oneLine.add(pagamento.getCodSingoloVersamentoEnte());
			else 
				oneLine.add("");

			if(StringUtils.isNotEmpty(pagamento.getIuv()))
				oneLine.add(pagamento.getIuv());
			else 
				oneLine.add("");

			if(pagamento.getImportoPagato() != null){
				tot = tot.doubleValue() + pagamento.getImportoPagato().doubleValue();
				oneLine.add(pagamento.getImportoPagato()+"");
			}else 
				oneLine.add("");

			if(pagamento.getDataPagamento() != null)
				oneLine.add(TemplateBase.sdf_ddMMyyyy.format(pagamento.getDataPagamento()));
			else 
				oneLine.add("");

			if(StringUtils.isNotEmpty(pagamento.getIur()))
				oneLine.add(pagamento.getIur());
			else 
				oneLine.add("");

			if(StringUtils.isNotEmpty(pagamento.getCodFlussoRendicontazione()))
				oneLine.add(pagamento.getCodFlussoRendicontazione());
			else 
				oneLine.add("");

			if(StringUtils.isNotEmpty(pagamento.getCodBicRiversamento()))
				oneLine.add(pagamento.getCodBicRiversamento());
			else 
				oneLine.add("");

			if(StringUtils.isNotEmpty(pagamento.getIdRegolamento()))
				oneLine.add(pagamento.getIdRegolamento());	
			else 
				oneLine.add("");


			dataSource.add(oneLine.toArray(new Object[oneLine.size()]));
		}

		totale.add(tot);
		return dataSource;
	}


}
