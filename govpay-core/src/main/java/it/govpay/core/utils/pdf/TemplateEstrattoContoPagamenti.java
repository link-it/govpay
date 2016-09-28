package it.govpay.core.utils.pdf;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.tableOfContentsCustomizer;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import static net.sf.dynamicreports.report.builder.DynamicReports.grid;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.EstrattoConto;
import it.govpay.core.utils.GovpayConfig;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.ComponentColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.grid.ColumnTitleGroupBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.Markup;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;

public class TemplateEstrattoContoPagamenti {
	public static final StyleBuilder rootStyle;
	public static final StyleBuilder fontStyle8;
	public static final StyleBuilder fontStyle9;
	public static final StyleBuilder fontStyle12;
	public static final StyleBuilder fontStyle16;
	public static final StyleBuilder fontStyle18;
	public static final StyleBuilder fontStyle22;
	public static final StyleBuilder boldStyle;
	public static final StyleBuilder boldStyle8;
	public static final StyleBuilder boldStyle9;
	public static final StyleBuilder boldStyle12;
	public static final StyleBuilder boldStyle16;
	public static final StyleBuilder italicStyle;
	public static final StyleBuilder italicStyle18;
	public static final StyleBuilder boldCenteredStyle;
	public static final StyleBuilder bold12CenteredStyle;
	public static final StyleBuilder bold16CenteredStyle;
	public static final StyleBuilder bold18CenteredStyle;
	public static final StyleBuilder bold22CenteredStyle;
	public static final StyleBuilder boldLeftStyle;
	public static final StyleBuilder bold12LeftStyle;
	public static final StyleBuilder bold16LeftStyle;
	public static final StyleBuilder bold18LeftStyle;
	public static final StyleBuilder bold22LeftStyle;
	public static final StyleBuilder columnStyle;
	public static final StyleBuilder columnTitleStyle;
	public static final StyleBuilder groupStyle;
	public static final StyleBuilder subtotalStyle;
	public static final StyleBuilder centeredStyle;

	public static final FontBuilder rootFont;

	public static final ReportTemplateBuilder reportTemplate;
	public static final CurrencyType currencyType;
	public static final ComponentBuilder<?, ?> footerComponent;

	public static final String logoPagoPa = "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";

	static {
		rootFont = stl.font().setFontSize(10);
		rootStyle = stl.style().setPadding(2).setFont(rootFont);
		fontStyle8 = stl.style().setFont(rootFont).setFontSize(8);
		fontStyle9 = stl.style().setPadding(1).setFont(rootFont).setFontSize(9);
		fontStyle12 = stl.style(rootStyle).setFontSize(12);
		fontStyle16 = stl.style(rootStyle).setFontSize(16);
		fontStyle18 = stl.style(rootStyle).setFontSize(18);
		fontStyle22 = stl.style(rootStyle).setFontSize(22);
		boldStyle = stl.style(rootStyle).bold();
		boldStyle8 = stl.style(fontStyle8).bold();
		boldStyle9 = stl.style(fontStyle9).bold();
		boldStyle12 = stl.style(fontStyle12).bold();
		boldStyle16 = stl.style(fontStyle16).bold();
		italicStyle = stl.style(rootStyle).italic();
		italicStyle18 = stl.style(fontStyle18).italic();
		centeredStyle   = stl.style(rootStyle).setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
		boldCenteredStyle   = stl.style(boldStyle)
				.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
		bold12CenteredStyle = stl.style(boldCenteredStyle)
				.setFontSize(12);
		bold16CenteredStyle = stl.style(boldCenteredStyle)
				.setFontSize(16);
		bold18CenteredStyle = stl.style(boldCenteredStyle)
				.setFontSize(18);
		bold22CenteredStyle = stl.style(boldCenteredStyle)
				.setFontSize(22);
		boldLeftStyle = stl.style(boldStyle)
				.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
		bold12LeftStyle = stl.style(boldLeftStyle)
				.setFontSize(12);
		bold16LeftStyle = stl.style(boldLeftStyle)
				.setFontSize(16);
		bold18LeftStyle = stl.style(boldLeftStyle)
				.setFontSize(18);
		bold22LeftStyle = stl.style(boldLeftStyle)
				.setFontSize(20);
		columnStyle = stl.style(fontStyle9).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
		columnTitleStyle = stl.style(rootStyle)
				.setBorder(stl.pen1Point())
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
				.setBackgroundColor(Color.LIGHT_GRAY)
				.bold();
		groupStyle= stl.style(boldStyle)
				.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
		subtotalStyle       = stl.style(boldStyle)
				.setTopBorder(stl.pen1Point());

		StyleBuilder crosstabGroupStyle      = stl.style(columnTitleStyle);
		StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)
				.setBackgroundColor(new Color(170, 170, 170));
		StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)
				.setBackgroundColor(new Color(140, 140, 140));
		StyleBuilder crosstabCellStyle       = stl.style(columnStyle)
				.setBorder(stl.pen1Point());

		TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
				.setHeadingStyle(0, stl.style(rootStyle).bold());

		reportTemplate = template()
				.setLocale(Locale.ITALIAN)
				.setColumnStyle(columnStyle)
				.setColumnTitleStyle(columnTitleStyle)
				.setGroupStyle(groupStyle)
				.setGroupTitleStyle(groupStyle)
				.setSubtotalStyle(subtotalStyle)
				.highlightDetailEvenRows()
				.crosstabHighlightEvenRows()
				.setCrosstabGroupStyle(crosstabGroupStyle)
				.setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
				.setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
				.setCrosstabCellStyle(crosstabCellStyle)
				.setTableOfContentsCustomizer(tableOfContentsCustomizer);

		currencyType = new CurrencyType();

		footerComponent = cmp.pageXofY()
				.setStyle(
						stl.style(boldCenteredStyle)
						.setTopBorder(stl.pen1Point()));
	}

	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createTitleComponent(BasicBD bd, Dominio dominio, String dataInizio, String dataFine,Logger log,List<String> errList) {
		try{

			StringBuilder errMsg = new StringBuilder();
			List<ComponentBuilder<?, ?>> lst = new ArrayList<ComponentBuilder<?,?>>();
			// caricamento del logo PagoPA
			String pathLoghi = GovpayConfig.getInstance().getPathEstrattoContoPdfLoghi();

			InputStream resourceLogoPagoPa = new ByteArrayInputStream(Base64.decodeBase64(logoPagoPa));
					lst.add(cmp.image(resourceLogoPagoPa).setFixedDimension(90, 90));

			List<ComponentBuilder<?, ?>> lstTitolo = new ArrayList<ComponentBuilder<?,?>>();
			String titoloReport =Costanti.TITOLO_REPORT;
			
			lstTitolo.add(cmp.text(titoloReport).setStyle(bold18LeftStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
			
			//Intervallo date Opzionale
			if(StringUtils.isNotEmpty(dataInizio) && StringUtils.isNotEmpty(dataFine)) {
				String periodoOsservazione = MessageFormat.format(Costanti.TITOLO_PERIODO, dataInizio,dataFine);
				lstTitolo.add(cmp.text(periodoOsservazione).setStyle(fontStyle16).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
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
	public static ComponentBuilder<?, ?> createRiepilogoComponent(BasicBD bd, Dominio dominio, String ibanAccredito, List<it.govpay.bd.model.EstrattoConto> estrattoContoList,Double totale,Logger log) {
		try{
			return cmp.horizontalList(
					createRiepilogoGenerale(bd, dominio, ibanAccredito, estrattoContoList,totale,log),
					createDatiDominio(bd, dominio, log)
					)
					.newRow()
					//					.add(cmp.verticalGap(30))
					//					.newRow()
					//					.add(cmp.verticalGap(20))
					;
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createDatiDominio(BasicBD bd, Dominio dominio, Logger log) {
		try{
			VerticalListBuilder listDominio = cmp.verticalList().setStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle12).setLeftPadding(10)
					.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setHorizontalImageAlignment(HorizontalImageAlignment.CENTER)); 

			String denominazioneDominio = dominio.getRagioneSociale();
			String pIvaDominio = MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE, Costanti.LABEL_P_IVA, dominio.getCodDominio());
			Anagrafica anagrafica = dominio.getAnagrafica(bd);

			String indirizzo = StringUtils.isNotEmpty(anagrafica.getIndirizzo()) ? anagrafica.getIndirizzo() : "";
			String civico = StringUtils.isNotEmpty(anagrafica.getCivico()) ? anagrafica.getCivico() : "";
			String cap = StringUtils.isNotEmpty(anagrafica.getCap()) ? anagrafica.getCap() : "";
			String localita = StringUtils.isNotEmpty(anagrafica.getLocalita()) ? anagrafica.getLocalita() : "";
			String provincia = StringUtils.isNotEmpty(anagrafica.getProvincia()) ? (" (" +anagrafica.getProvincia() +")" ) : "";


			String indirizzoCivico = indirizzo + " " + civico;
			String capCitta = cap + " " + localita + provincia      ;


			listDominio.add(cmp.text(denominazioneDominio).setStyle(bold18LeftStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));//.newRow();
			listDominio.add(cmp.text(pIvaDominio).setStyle(boldStyle12).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
			if(StringUtils.isNotEmpty(indirizzoCivico))
				listDominio.add(cmp.text(indirizzoCivico).setStyle(fontStyle12).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
			if(StringUtils.isNotEmpty(capCitta))
				listDominio.add(cmp.text(capCitta).setStyle(fontStyle12).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

			//			listDominio.setFixedWidth(400);
			//			listDominio.setWidth(400);


			return listDominio;
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createRiepilogoGenerale(BasicBD bd, Dominio dominio,String ibanAccredito, List<it.govpay.bd.model.EstrattoConto> estrattoContoList,Double totale,Logger log) {
		try{
			HorizontalListBuilder listRiepilogo = cmp.horizontalList().setBaseStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle12).setLeftPadding(10).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)); 

			//			listRiepilogo.add(cmp.text(Costanti.LABEL_IBAN_ACCREDITO).setStyle(boldLeftStyle));
			//			listRiepilogo.add(splitIban( ibanAccredito)).newRow();

			String titoloRiepilogo = Costanti.TITOLO_RIEPILOGO;
			listRiepilogo.add(cmp.text(titoloRiepilogo).setStyle(boldStyle16.italic()).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)).newRow();
			addElementoLista2(listRiepilogo, Costanti.LABEL_IBAN_ACCREDITO , ibanAccredito, true, false, false);
			addElementoLista2(listRiepilogo, Costanti.LABEL_NUMERO_PAGAMENTI , "" + estrattoContoList.size(), true, false, false);
			String tot = Costanti.LABEL_EURO + " " + String.format("%.2f", (double)totale.doubleValue());
			addElementoLista2(listRiepilogo, Costanti.LABEL_IMPORTO_TOTALE ,tot, true, false, false);


			return 
					//					cmp.verticalList(cmp.text(titoloRiepilogo).setStyle(boldStyle16.italic()).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
					//					listRiepilogo)
					//					.add(cmp.verticalGap(30))
					listRiepilogo
					;
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return null;
	}

	public static SubreportBuilder getTabellaDettaglioPagamenti(BasicBD bd,  List<it.govpay.bd.model.EstrattoConto> estrattoContoList,List<Double> totale ,Logger log) throws Exception{

		// Scittura Intestazione
		List<ColumnBuilder<?, ?>> colonne = new ArrayList<ColumnBuilder<?, ?>>();

		TextColumnBuilder<String> idFlussoColumn = col.column(Costanti.LABEL_ID_FLUSSO_RENDICONTAZIONE, Costanti.CODICE_RENDICONTAZIONE_COL, type.stringType()).setStyle(fontStyle9)
				.setWidth(20).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		TextColumnBuilder<String> iuvColumn  = col.column(Costanti.LABEL_IUV, Costanti.IUV_COL, type.stringType()).setStyle(fontStyle9)
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
				.setTemplate(TemplateEstrattoContoPagamenti.reportTemplate)
				.fields(fields.toArray(new FieldBuilder[fields.size()])) 
				.title(cmp.text(titoloTabella).setStyle(TemplateEstrattoContoPagamenti.bold18CenteredStyle.italic()),cmp.verticalGap(20))
				.columnGrid(idFlussoColumn,iuvColumn,titleGroup1)
				.columns(colonne.toArray(new ColumnBuilder[colonne.size()]))
				.setDataSource(dataSource)
				.pageFooter(TemplateEstrattoContoPagamenti.footerComponent)
				//.setTableOfContents(true)
				);
	}

	private static void addElementoLista2(HorizontalListBuilder list, String label, String value, boolean newRow, boolean bold, boolean dots) {
		if (value != null) {
			String v = value;

			if(label != null) { //.setFixedColumns(8)
				String labelDots = (label.length() > 0 && dots) ? (label + Costanti.LABEL_DUE_PUNTI) : label;
				v = "<b>" + labelDots + "</b> " + value;
			} 

			TextFieldBuilder<String> text = cmp.text(v).setMarkup(Markup.STYLED).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);

			if(bold)
				text.setStyle(TemplateEstrattoContoPagamenti.boldStyle);

			if(newRow)
				list.add(text).newRow();
			else
				list.add(text);

		}
	}

	private static void addElementoLista2(HorizontalListBuilder list, String label, AbstractSimpleExpression<String> value, boolean newRow, boolean bold, boolean dots) {
		if (value != null) {

			TextFieldBuilder<String> text = cmp.text(value).setMarkup(Markup.STYLED).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);

			if(bold)
				text.setStyle(TemplateEstrattoContoPagamenti.boldStyle);

			if(newRow)
				list.add(text).newRow();
			else
				list.add(text);
		}
	}

	public static HorizontalListBuilder getContenutoCellaDx() {
		HorizontalListBuilder itemComponent = cmp.horizontalList().setBaseStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle9).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)); //.setLeftPadding(10));

		//		if(StringUtils.isNotEmpty(pag.getIur()))
		addElementoLista2(itemComponent, Costanti.LABEL_ID_RIVERSAMENTO,  new TemplateEstrattoContoPagamenti().new IdRiversamentoExpression(), true,false,true);
		//		if(StringUtils.isNotEmpty(pag.getCodBicRiversamento()))
		addElementoLista2(itemComponent, Costanti.LABEL_BIC_RIVERSAMENTO,  new TemplateEstrattoContoPagamenti().new BicRiversamentoExpression(), true,false,true);
		//		if(StringUtils.isNotEmpty(pag.getIdRegolamento()))
		addElementoLista2(itemComponent, Costanti.LABEL_ID_REGOLAMENTO,  new TemplateEstrattoContoPagamenti().new IdRegolamentoExpression(), true,false,true);

		return itemComponent;
	}

	public static HorizontalListBuilder getContenutoCellaSx() {
		HorizontalListBuilder itemComponent = cmp.horizontalList().setBaseStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle9).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)); //.setLeftPadding(10)); 

		//		if(StringUtils.isNotEmpty(pag.getCodSingoloVersamentoEnte()))
		addElementoLista2(itemComponent, Costanti.LABEL_CODICE_VERSAMENTO_ENTE, new TemplateEstrattoContoPagamenti().new CodSingoloVersamentoEnteExpression(), true,false,true);
		//		if(pag.getImportoPagato() != null)
		addElementoLista2(itemComponent, Costanti.LABEL_IMPORTO,  new TemplateEstrattoContoPagamenti().new ImportoPagatoExpression(), true,false,true);
		//		if(pag.getDataPagamento() != null)
		addElementoLista2(itemComponent, Costanti.LABEL_DATA_PAGAMENTO,  new TemplateEstrattoContoPagamenti().new DataPagamentoExpression(), true,false,true);

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
				oneLine.add(EstrattoContoPdf.sdf.format(pagamento.getDataPagamento()));
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



	public static CurrencyValueFormatter createCurrencyValueFormatter(String label) {
		return new CurrencyValueFormatter(label);
	}

	public static class CurrencyType extends BigDecimalType {
		private static final long serialVersionUID = 1L;

		@Override
		public String getPattern() {
			return "€ #,###.00";
		}
	}

	private static class CurrencyValueFormatter extends AbstractValueFormatter<String, Number> {
		private static final long serialVersionUID = 1L;

		private String label;

		public CurrencyValueFormatter(String label) {
			this.label = label;
		}

		@Override
		public String format(Number value, ReportParameters reportParameters) {
			return this.label + currencyType.valueToString(value, reportParameters.getLocale());
		}
	}


}
