package it.govpay.core.utils.pdf;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.tableOfContentsCustomizer;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.iban4j.Iban;

import it.govpay.bd.BasicBD;
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
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.ComponentPositionType;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
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

	static {
		rootFont = stl.font().setFontSize(10);
		rootStyle = stl.style().setPadding(2).setFont(rootFont);
		fontStyle8 = stl.style().setFont(rootFont).setFontSize(8);
		fontStyle9 = stl.style().setFont(rootFont).setFontSize(9);
		fontStyle12 = stl.style(rootStyle).setFontSize(12);
		fontStyle16 = stl.style(rootStyle).setFontSize(16);
		fontStyle18 = stl.style(rootStyle).setFontSize(18);
		fontStyle22 = stl.style(rootStyle).setFontSize(22);
		boldStyle           = stl.style(rootStyle).bold();
		boldStyle8           = stl.style(fontStyle8).bold();
		boldStyle9           = stl.style(fontStyle9).bold();
		boldStyle16           = stl.style(fontStyle16).bold();
		italicStyle         = stl.style(rootStyle).italic();
		italicStyle18         = stl.style(fontStyle18).italic();
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
		boldLeftStyle   = stl.style(boldStyle)
				.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
		bold12LeftStyle = stl.style(boldLeftStyle)
				.setFontSize(12);
		bold18LeftStyle = stl.style(boldLeftStyle)
				.setFontSize(18);
		bold22LeftStyle = stl.style(boldLeftStyle)
				.setFontSize(20);
		columnStyle         = stl.style(fontStyle9).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
		columnTitleStyle    = stl.style(rootStyle)
				.setBorder(stl.pen1Point())
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
				.setBackgroundColor(Color.LIGHT_GRAY)
				.bold();
		groupStyle          = stl.style(boldStyle)
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
	public static ComponentBuilder<?, ?> createTitleComponent(String dataInizio, String dataFine,Logger log) {
		try{

			List<ComponentBuilder<?, ?>> lst = new ArrayList<ComponentBuilder<?,?>>();
			// caricamento del logo PagoPA
			String pathLoghi = GovpayConfig.getInstance().getPathEstrattoContoPdfLoghi();
			String logoPagoPA = GovpayConfig.getInstance().getPagoPALogo();

			File fPagoPa = new File(pathLoghi+"/"+logoPagoPA);
			if(fPagoPa.exists()){
				InputStream resourceLogoPagoPa = new FileInputStream(fPagoPa);
				lst.add(cmp.image(resourceLogoPagoPa).setFixedDimension(90, 90));

			}

			String titoloReport =Costanti.TITOLO_REPORT;
			String periodoOsservazione = MessageFormat.format(Costanti.TITOLO_PERIODO, dataInizio,dataFine);
			lst.add(cmp.verticalList(
					cmp.text(titoloReport).setStyle(bold18LeftStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
					cmp.text(periodoOsservazione).setStyle(fontStyle16).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
					));

			return cmp.horizontalList(lst.toArray(new ComponentBuilder[lst.size()]))
					.newRow()
					.add(cmp.verticalGap(20))
					.newRow()
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
	public static ComponentBuilder<?, ?> createRiepilogoComponent(BasicBD bd, Dominio dominio, String ibanAccredito, List<it.govpay.bd.model.EstrattoConto> estrattoContoList,Double totale,Logger log) {
		try{
			return cmp.horizontalList(
					createRiepilogoGenerale(bd, dominio, ibanAccredito, estrattoContoList,totale,log),
					createDatiDominio(bd, dominio, log) 
					)
					.newRow()
					.add(cmp.verticalGap(30))
					.newRow()
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
			HorizontalListBuilder listDominio = cmp.horizontalList().setBaseStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle12).setLeftPadding(10));

			// caricamento del logo PagoPA
			String pathLoghi = GovpayConfig.getInstance().getPathEstrattoContoPdfLoghi();
			String logoDominio = dominio.getCodDominio() + ".png";

			File fLogoDominio = new File(pathLoghi+"/"+logoDominio);
			if(fLogoDominio.exists()){
				InputStream resourceLogoDominio = new FileInputStream(fLogoDominio);
				listDominio.add(cmp.image(resourceLogoDominio).setFixedDimension(80, 80).setHorizontalImageAlignment(HorizontalImageAlignment.CENTER)).newRow();
			}

			String denominazioneDominio = dominio.getRagioneSociale();
			String periodoOsservazione = MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE, Costanti.LABEL_P_IVA, dominio.getCodDominio());

			listDominio.add(cmp.text(denominazioneDominio).setStyle(bold18LeftStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)).newRow();
			listDominio.add(cmp.text(periodoOsservazione).setStyle(boldStyle16).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

			listDominio.setFixedWidth(400);
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

			listRiepilogo.add(cmp.text(Costanti.LABEL_IBAN_ACCREDITO).setStyle(boldLeftStyle));
			listRiepilogo.add(splitIban( ibanAccredito)).newRow();
			addElementoLista(listRiepilogo, Costanti.LABEL_NUMERO_PAGAMENTI , "" + estrattoContoList.size(), true, false, false);
			addElementoLista(listRiepilogo, null, Costanti.LABEL_IMPORTO_TOTALE ,false,true,false);
			String tot = Costanti.LABEL_EURO + " " + String.format("%.2f", (double)totale.doubleValue()); 
			addElementoLista(listRiepilogo, null ,tot, true, false, false);

			String titoloRiepilogo = Costanti.TITOLO_RIEPILOGO;
			return cmp.verticalList(cmp.text(titoloRiepilogo).setStyle(italicStyle18.bold()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
					listRiepilogo)
					//					.add(cmp.verticalGap(30))
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
		ComponentColumnBuilder componentColumnSx = col.componentColumn(Costanti.LABEL_ULTERIORI_INFORMAZIONI, getContenutoCellaSx()).setWidth(30);
		ComponentColumnBuilder componentColumnDx = col.componentColumn(Costanti.LABEL_ULTERIORI_INFORMAZIONI ,getContenutoCellaDx()).setWidth(35); 

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
				.title(cmp.text(titoloTabella).setStyle(TemplateEstrattoContoPagamenti.bold18CenteredStyle),cmp.verticalGap(20))
				.columns(colonne.toArray(new ColumnBuilder[colonne.size()]))
				.setDataSource(dataSource)
				//.pageFooter(Templates.footerComponent)
				//.setTableOfContents(true)
				);
	}

	private static HorizontalListBuilder splitIban(String ibanAccredito) {
		HorizontalListBuilder listIban = cmp.horizontalList().setBaseStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle12));//.setLeftPadding(10));

		Iban iban = Iban.valueOf(ibanAccredito);


		if(iban != null){
			String paese = iban.getCountryCode().getAlpha2();

			HorizontalListBuilder listPaese = 
					cmp.horizontalList().setBaseStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle12).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)); 
			listPaese.add(cmp.text(Costanti.LABEL_PAESE).setStyle(TemplateEstrattoContoPagamenti.boldStyle)).newRow();
			listPaese.add(cmp.text(paese));

			String eur = iban.getCheckDigit();
			HorizontalListBuilder listEur = 
					cmp.horizontalList().setBaseStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle12).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
			listEur.add(cmp.text(Costanti.LABEL_EUR).setStyle(TemplateEstrattoContoPagamenti.boldStyle)).newRow();
			listEur.add(cmp.text(eur));

			String cin = iban.getNationalCheckDigit();
			HorizontalListBuilder listCin = 
					cmp.horizontalList().setBaseStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle12).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
			listCin.add(cmp.text(Costanti.LABEL_CIN).setStyle(TemplateEstrattoContoPagamenti.boldStyle)).newRow();
			listCin.add(cmp.text(cin));

			String abi = iban.getBankCode();
			HorizontalListBuilder listAbi = 
					cmp.horizontalList().setBaseStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle12).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
			listAbi.add(cmp.text(Costanti.LABEL_ABI).setStyle(TemplateEstrattoContoPagamenti.boldStyle)).newRow();
			listAbi.add(cmp.text(abi));

			String cab = iban.getBranchCode();
			HorizontalListBuilder listCab = 
					cmp.horizontalList().setBaseStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle12).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
			listCab.add(cmp.text(Costanti.LABEL_CAB).setStyle(TemplateEstrattoContoPagamenti.boldStyle)).newRow();
			listCab.add(cmp.text(cab));

			String conto = iban.getAccountNumber();
			HorizontalListBuilder listConto = 
					cmp.horizontalList().setBaseStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle12).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
			listConto.add(cmp.text(Costanti.LABEL_NUMERO_CONTO).setStyle(TemplateEstrattoContoPagamenti.boldStyle)).newRow();
			listConto.add(cmp.text(conto));

			listIban.add(listPaese,listEur,listCin,listAbi,listCab,listConto).setWidth(200); 
		}
		return listIban;
	}


	private static void addElementoLista(HorizontalListBuilder list, String label, String value, boolean newRow) {
		addElementoLista(list, label, value, newRow, false,true);
	}

	private static void addElementoLista(HorizontalListBuilder list, String label, String value, boolean newRow, boolean bold, boolean dots) {
		if (value != null) {
			TextFieldBuilder<String> text = cmp.text(value);
			if(bold)
				text.setStyle(TemplateEstrattoContoPagamenti.boldStyle);

			if(label != null) { //.setFixedColumns(8)
				String labelDots = (label.length() > 0 && dots) ? (label + Costanti.LABEL_DUE_PUNTI) : label;

				if(newRow)
					list.add(cmp.text(labelDots).setStyle(TemplateEstrattoContoPagamenti.boldStyle), text).newRow();
				else
					list.add(cmp.text(labelDots).setStyle(TemplateEstrattoContoPagamenti.boldStyle), text);
			}else {
				if(newRow)
					list.add(text).newRow();
				else
					list.add(text);
			}
		}
	}

	private static void addElementoLista(HorizontalListBuilder list, String label, AbstractSimpleExpression<String> value, boolean newRow, boolean bold, boolean dots) {
		if (value != null) {
			TextFieldBuilder<String> text = cmp.text(value);
			if(bold)
				text.setStyle(TemplateEstrattoContoPagamenti.boldStyle9);

			if(label != null) { //.setFixedColumns(8)
				String labelDots = (label.length() > 0 && dots) ? (label + Costanti.LABEL_DUE_PUNTI) : label;

				if(newRow)
					list.add(cmp.text(labelDots).setStyle(TemplateEstrattoContoPagamenti.boldStyle9), text).newRow();
				else
					list.add(cmp.text(labelDots).setStyle(TemplateEstrattoContoPagamenti.boldStyle9), text);
			}else {
				if(newRow)
					list.add(text).newRow();
				else
					list.add(text);
			}
		}
	}

	private static HorizontalListBuilder getContenutoCellaDx() {
		HorizontalListBuilder itemComponent = cmp.horizontalList().setBaseStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle8)); //.setLeftPadding(10)); 

//		if(StringUtils.isNotEmpty(pag.getIur()))
		addElementoLista(itemComponent, Costanti.LABEL_ID_RIVERSAMENTO,  new TemplateEstrattoContoPagamenti().new IdRiversamentoExpression(), true,false,true);
		//		if(StringUtils.isNotEmpty(pag.getCodBicRiversamento()))
		addElementoLista(itemComponent, Costanti.LABEL_BIC_RIVERSAMENTO,  new TemplateEstrattoContoPagamenti().new BicRiversamentoExpression(), true,false,true);
		//		if(StringUtils.isNotEmpty(pag.getIdRegolamento()))
		addElementoLista(itemComponent, Costanti.LABEL_ID_REGOLAMENTO,  new TemplateEstrattoContoPagamenti().new IdRegolamentoExpression(), true,false,true);

		return itemComponent;
	}

	private static HorizontalListBuilder getContenutoCellaSx() {
		HorizontalListBuilder itemComponent = cmp.horizontalList().setBaseStyle(stl.style(TemplateEstrattoContoPagamenti.fontStyle8)); //.setLeftPadding(10)); 

//		if(StringUtils.isNotEmpty(pag.getCodSingoloVersamentoEnte()))
		addElementoLista(itemComponent, Costanti.LABEL_CODICE_VERSAMENTO_ENTE, new TemplateEstrattoContoPagamenti().new CodSingoloVersamentoEnteExpression(), true,false,true);
		//		if(pag.getImportoPagato() != null)
		addElementoLista(itemComponent, Costanti.LABEL_IMPORTO,  new TemplateEstrattoContoPagamenti().new ImportoPagatoExpression(), true,false,true);
		//		if(pag.getDataPagamento() != null)
		addElementoLista(itemComponent, Costanti.LABEL_DATA_PAGAMENTO,  new TemplateEstrattoContoPagamenti().new DataPagamentoExpression(), true,false,true);

		return itemComponent;
	}

	public class CodSingoloVersamentoEnteExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public String evaluate(ReportParameters reportParameters) {
			return reportParameters.getValue(Costanti.IDENTIFICATIVO_VERSAMENTO_COL);
		}
	}

	public class BicRiversamentoExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public String evaluate(ReportParameters reportParameters) {
			return reportParameters.getValue(Costanti.BIC_RIVERSAMENTO_COL);
		}
	}

	public class IdRegolamentoExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public String evaluate(ReportParameters reportParameters) {
			return reportParameters.getValue(Costanti.ID_REGOLAMENTO_COL);
		}
	}

	public class ImportoPagatoExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public String evaluate(ReportParameters reportParameters) {
			return reportParameters.getValue(Costanti.IMPORTO_PAGATO_COL);
		}
	}

	public class IdRiversamentoExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public String evaluate(ReportParameters reportParameters) {
			return reportParameters.getValue(Costanti.CODICE_RIVERSAMENTO_COL);
		}
	}

	public class DataPagamentoExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public String evaluate(ReportParameters reportParameters) {
			return reportParameters.getValue(Costanti.DATA_PAGAMENTO_COL);
		}
	}

	private static DRDataSource createDataSource(List<EstrattoConto> list,List<Double> totale ) {
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
