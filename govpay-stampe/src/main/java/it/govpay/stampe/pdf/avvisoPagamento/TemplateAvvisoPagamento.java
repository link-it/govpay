package it.govpay.stampe.pdf.avvisoPagamento;

import static net.sf.dynamicreports.report.builder.DynamicReports.bcode;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import it.govpay.model.Anagrafica;
import it.govpay.model.AvvisoPagamento;
import it.govpay.model.Dominio;
import it.govpay.stampe.pdf.Costanti;
import it.govpay.stampe.pdf.TemplateBase;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.ComponentColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.Markup;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;

public class TemplateAvvisoPagamento {

	/**
	 * 
	 * TITOLO
	 * 
	 * IMG ENTE sx  Dati Ente Centro IMG PAGAPA dx
	 * 
	 * @param pathLoghi
	 * @param avviso
	 * @param errList
	 * @param log
	 * @return
	 */
	public static ComponentBuilder<?, ?> createTitleComponent(String pathLoghi, AvvisoPagamento avviso,  List<String> errList, Logger log){
		String sezione = "Sezione Titolo";
		try{
			StringBuilder errMsg = new StringBuilder();
			List<ComponentBuilder<?, ?>> lst = new ArrayList<ComponentBuilder<?,?>>();
			InputStream resourceLogoPagoPa = new ByteArrayInputStream(Base64.decodeBase64(Costanti.logoPagoPa));
			Dominio dominio = avviso.getDominioCreditore();
			String logoDominio = dominio.getCodDominio() + ".png";
			File fEnte = new File(pathLoghi+"/"+logoDominio);


			if(fEnte.exists()){
				InputStream resourceLogoEnte = new FileInputStream(fEnte);
				lst.add(cmp.image(resourceLogoEnte).setFixedDimension(90, 90).setHorizontalImageAlignment(HorizontalImageAlignment.LEFT));
			}else {
				if(errMsg.length() >0)
					errMsg.append(", ");

				errMsg.append("Il PDF non contiene il logo del dominio poiche' il file ["+logoDominio+"] non e' stato trovato nella directory dei loghi");
			}

			if(errMsg.length() >0){
				errList.add(errMsg.toString());
			}

			List<ComponentBuilder<?, ?>> lstTitolo = new ArrayList<ComponentBuilder<?,?>>();

			HorizontalTextAlignment horizontalTextAlignment = HorizontalTextAlignment.CENTER;
			VerticalTextAlignment verticalTextAlignment = VerticalTextAlignment.TOP;
			StyleBuilder style = stl.style(TemplateBase.fontStyle12).setVerticalTextAlignment(verticalTextAlignment);  
			StyleBuilder headerStyle = stl.style(TemplateBase.bold16LeftStyle); 

			ComponentBuilder<?, ?> createDatiDominio = TemplateBase.createDatiDominio(avviso.getDominioCreditore(), avviso.getAnagraficaCreditore(), horizontalTextAlignment,
					verticalTextAlignment, headerStyle, style, log);
			lstTitolo.add(createDatiDominio);
			lst.add(cmp.verticalList(lstTitolo.toArray(new ComponentBuilder[lstTitolo.size()])));
			lst.add(cmp.image(resourceLogoPagoPa).setFixedDimension(90, 90).setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT));

			return cmp.horizontalList(lst.toArray(new ComponentBuilder[lst.size()])).newRow().add(cmp.verticalGap(20)).newRow()
					//					.add(cmp.line()).newRow()
					;

		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * sx causale, dx importi
	 * 
	 * @param avviso
	 * @param errList
	 * @param log
	 * @return
	 */
	public static ComponentBuilder<?, ?> createSezionePagamento(AvvisoPagamento avviso,  List<String> errList, Logger log){
		String sezione = "Sezione Pagamento";
		try{
			VerticalTextAlignment verticalTextAlignment = VerticalTextAlignment.TOP;
			StyleBuilder style = stl.style(TemplateBase.rootFont).setVerticalTextAlignment(verticalTextAlignment);;  
			ComponentBuilder<?, ?> createSezioneComunicazioni = TemplateAvvisoPagamento.createSezioneComunicazioni(avviso,errList,log);
			if(createSezioneComunicazioni!= null){

				return cmp.verticalList().add(
						cmp.horizontalList().add(
								cmp.hListCell(createSezioneCausale(avviso, errList, log)
										).heightFixedOnTop(),
								cmp.hListCell(createSezioneImporti(avviso, errList, log)).heightFixedOnTop()				
								)).add(cmp.verticalGap(20))
								.add(			createSezioneComunicazioni).setStyle(style);//.setStyle(style);
			} else 
				return cmp.horizontalList().add(
						cmp.hListCell(createSezioneCausale(avviso, errList, log)).heightFixedOnTop(),
						cmp.hListCell(createSezioneImporti(avviso, errList, log)).heightFixedOnTop()				
						);//.setStyle(style);
		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
	}


	/**
	 * 
	 * linea 1: causale
	 * linea 2: debitore
	 * 
	 * @param avviso
	 * @param errList
	 * @param log
	 * @return
	 */
	public static ComponentBuilder<?, ?> createSezioneCausale(AvvisoPagamento avviso,  List<String> errList, Logger log){
		String sezione = "Sezione Causale";
		try{
			HorizontalTextAlignment horizontalTextAlignment = HorizontalTextAlignment.LEFT;
			StyleBuilder style = stl.style(TemplateBase.rootStyle).setPadding(0).setHorizontalTextAlignment(horizontalTextAlignment).setVerticalTextAlignment(VerticalTextAlignment.TOP);
			VerticalListBuilder verticalList = cmp.verticalList().setStyle(style);

			verticalList.add(cmp.verticalGap(20));
			verticalList.add(cmp.text(avviso.getCausale()).setStyle(style).setHorizontalTextAlignment(horizontalTextAlignment));
			verticalList.add(cmp.text(Costanti.LABEL_INTESTATO_A).setStyle(style).setHorizontalTextAlignment(horizontalTextAlignment));
			TemplateBase.createDatiDebitore(verticalList,avviso.getAnagraficaDebitore(), true, false, style, horizontalTextAlignment ,log);
			verticalList.add(cmp.verticalGap(20));

			return verticalList; 

		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * linea 1: tabella importo
	 * linea 2: tabella scadenza
	 * linea 3: tabella riferimenti
	 * 
	 * @param avviso
	 * @param errList
	 * @param log
	 * @return
	 */
	public static ComponentBuilder<?, ?> createSezioneImporti(AvvisoPagamento avviso,  List<String> errList, Logger log){
		String sezione = "Sezione Importi";
		try{
			StyleBuilder style = stl.style(TemplateBase.rootStyle).setPadding(0);
			HorizontalTextAlignment horizontalTextAlignment = HorizontalTextAlignment.LEFT;
			VerticalListBuilder verticalList = cmp.verticalList().setStyle(stl.style(style)
					.setHorizontalTextAlignment(horizontalTextAlignment).setVerticalTextAlignment(VerticalTextAlignment.TOP)); 

			StyleBuilder columnStyle = stl.style(TemplateBase.columnBorderStyle).setLeftPadding(5).setRightPadding(0).setTopPadding(5).setBottomPadding(5);

			List<String> values = new ArrayList<String>();
			String importoAsString = "‎€"+ avviso.getImporto().doubleValue(); 
			StringBuilder sb = new StringBuilder();
			sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_IMPORTO_DA_PAGARE, importoAsString));
			if(avviso.getDataScadenza() != null){
				sb.append("<br/>");
				sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_DATA_SCADENZA, TemplateBase.sdf_ddMMyyyy.format(avviso.getDataScadenza())));
			}
			sb.append("<br/>");
			sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_IUV, avviso.getIuv()));
			values.add(sb.toString());
			verticalList.add(getTabella(Costanti.LABEL_ESTREMI_DI_PAGAMENTO,values, errList,150, columnStyle,horizontalTextAlignment,log));

			return verticalList.setFixedWidth(200);//.setFixedHeight(90)	; 

		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * linea 1: tabella importo
	 * linea 2: tabella scadenza
	 * linea 3: tabella riferimenti
	 * 
	 * @param avviso
	 * @param errList
	 * @param log
	 * @return
	 */
	public static ComponentBuilder<?, ?> createSezioneImportiOld(AvvisoPagamento avviso,  List<String> errList, Logger log){
		String sezione = "Sezione Importi";
		try{
			StyleBuilder style = stl.style(TemplateBase.rootStyle).setPadding(0);
			HorizontalTextAlignment horizontalTextAlignment = HorizontalTextAlignment.LEFT;
			VerticalListBuilder verticalList = cmp.verticalList().setStyle(stl.style(style)
					.setHorizontalTextAlignment(horizontalTextAlignment).setVerticalTextAlignment(VerticalTextAlignment.TOP)); 

			StyleBuilder columnStyle = stl.style(TemplateBase.columnBorderStyle).setLeftPadding(5).setRightPadding(0).setTopPadding(5).setBottomPadding(5);

			List<String> values = new ArrayList<String>();
			String importoAsString = "‎€"+ avviso.getImporto().doubleValue(); 
			values.add(importoAsString);
			verticalList.add(getTabella(Costanti.LABEL_IMPORTO_DA_PAGARE,values, errList,150,columnStyle,horizontalTextAlignment, log));
			verticalList.add(cmp.verticalGap(5));
			if(avviso.getDataScadenza() != null){
				values.clear();
				values.add(importoAsString +" il "+ TemplateBase.sdf_ddMMyyyy.format(avviso.getDataScadenza()));
				verticalList.add(getTabella(Costanti.LABEL_SCADENZA_DEL_PAGAMENTO,values, errList,150, columnStyle,horizontalTextAlignment,log));
				verticalList.add(cmp.verticalGap(5));
			}
			values.clear();
			String string = MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_IUV, avviso.getIuv())
					+"<br/>" + MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_CODICE_AVVISO, avviso.getCodiceAvviso()) 
					+"<br/>" + MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_CODICE_VERSAMENTO, avviso.getCodVersamento());
			values.add(string);
			log.debug(string); 
			verticalList.add(getTabella(Costanti.LABEL_RIFERIMENTI,values, errList,150, columnStyle,horizontalTextAlignment,log));

			return verticalList.setFixedWidth(200).setFixedHeight(90)	; 

		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
	}

	public static SubreportBuilder getTabella(String title, List<String> values, List<String> errList,Integer width,StyleBuilder columnStyle, HorizontalTextAlignment horizontalTextAlignment, Logger log) throws Exception{
		String sezione = "Tabella titolo["+title+"]";
		try{
			boolean showColumnTitle = StringUtils.isNotEmpty(title);
			// Scittura Intestazione
			List<ColumnBuilder<?, ?>> colonne = new ArrayList<ColumnBuilder<?, ?>>();

			TextFieldBuilder<String> componentText = cmp.text(new TemplateAvvisoPagamento().new ColonnaUnoExpression())
					.setMarkup(Markup.HTML).setStyle(columnStyle).setHorizontalTextAlignment(horizontalTextAlignment); 

			ComponentColumnBuilder columnOne = col.componentColumn(title, componentText).setWidth(width);

			colonne.add(columnOne);

			List<FieldBuilder<String>> fields = new ArrayList<FieldBuilder<String>>();

			fields.add(field(Costanti.COL_UNO, String.class));

			List<String> header = new ArrayList<String>();
			header.add(Costanti.COL_UNO);

			DRDataSource dataSource = new DRDataSource(header.toArray(new String[header.size()]));
			for (String value : values) {
				List<String> oneLine = new ArrayList<String>();
				oneLine.add(value);
				dataSource.add(oneLine.toArray(new Object[oneLine.size()]));	
			}

			return cmp.subreport(
					report().setShowColumnTitle(showColumnTitle)
					.setTemplate(TemplateBase.tableTemplate)
					.fields(fields.toArray(new FieldBuilder[fields.size()])) 
					.columns(colonne.toArray(new ColumnBuilder[colonne.size()]))
					.setDataSource(dataSource));

		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
	}

	public class ColonnaUnoExpression extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public String evaluate(ReportParameters reportParameters) {
			return reportParameters.getValue(Costanti.COL_UNO);
		}
	}

	/**
	 * 
	 * sx vuoto, dx dati debitore
	 * 
	 * @param avviso
	 * @param errList
	 * @param log
	 * @return
	 */
	public static ComponentBuilder<?, ?> createSezioneDebitore(AvvisoPagamento avviso,  List<String> errList, Logger log){
		String sezione = "Sezione Debitore";
		try{
			List<ComponentBuilder<?, ?>> lst = new ArrayList<ComponentBuilder<?,?>>();
			lst.add(cmp.text(" "));
			lst.add(TemplateBase.createDatiDebitore(avviso.getAnagraficaDebitore(), false, false, stl.style(TemplateBase.rootStyle).setPadding(0), HorizontalTextAlignment.LEFT ,log));

			return cmp.horizontalList(lst.toArray(new ComponentBuilder[lst.size()])).newRow().add(cmp.verticalGap(20)).newRow()
					;

		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
	}


	/***
	 * Elenco delle informazioni sul titolo avviso:
	 * 
	 * 1 Avviso di pagamento N ...
	 * 
	 * @param avviso
	 * @param errList
	 * @param log
	 * @return
	 */
	public static ComponentBuilder<?, ?> createSezioneTitoloAvviso(AvvisoPagamento avviso,  List<String> errList, Logger log){
		String sezione = "Sezione Titolo Avviso";
		try{
			HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.fontStyle16)
					.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE));
			String label = MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_CODICE_AVVISO_PAGAMENTO_UPPER_CASE, avviso.getCodiceAvviso());
			list.add(cmp.verticalGap(70));
			list.add(cmp.text(label ).setStyle(TemplateBase.bold16CenteredStyle));
			list.add(cmp.verticalGap(50));
			list.newRow();

			return list;

		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
	}

	public static ComponentBuilder<?, ?> createSezioneComunicazioni(AvvisoPagamento avviso,  List<String> errList, Logger log){
		String sezione = "Sezione Comunicazioni";
		try{
			HorizontalListBuilder list =  cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.rootFont).setVerticalTextAlignment(VerticalTextAlignment.TOP));

			StyleBuilder columnStyle = stl.style(TemplateBase.columnBorderStyle).setLeftPadding(10).setRightPadding(10).setTopPadding(10).setBottomPadding(1).setVerticalTextAlignment(VerticalTextAlignment.TOP); 
			HorizontalTextAlignment horizontalTextAlignment = HorizontalTextAlignment.JUSTIFIED;

			Dominio dominio = avviso.getDominioCreditore();
			Anagrafica anagrafica = avviso.getAnagraficaCreditore();
			TreeMap<String, String> contenutoStaticoDisponibilita = avviso.getContenutoStatico().get(AvvisoPagamentoCostanti.SEZIONE_COMUNICAZIONI_KEY);
			if(contenutoStaticoDisponibilita != null && !contenutoStaticoDisponibilita.isEmpty()){
				List<String> values = new ArrayList<String>();
				StringBuilder sb = new StringBuilder();
				for (String contenutoKey : contenutoStaticoDisponibilita.keySet()) {
					if(sb.length() > 0)
						sb.append("<br/>");

					String value = contenutoStaticoDisponibilita.get(contenutoKey);
					value = value.replace(AvvisoPagamentoCostanti.ENTE_CREDITORE_KEY, dominio.getRagioneSociale());

					String urlSitoWeb = StringUtils.isNotEmpty(anagrafica.getUrlSitoWeb()) ? anagrafica.getUrlSitoWeb() : "";

					if(StringUtils.isEmpty(anagrafica.getUrlSitoWeb())){
						errList.add("Url sito web per l'ente creditore ["+dominio.getCodDominio()+"] non presente nel documento poiche' non trovata nei dati avviso.");
					}

					value = value.replace(AvvisoPagamentoCostanti.URL_ENTE_CREDITORE_KEY, urlSitoWeb);

					sb.append(value);

				}
				values.add(sb.toString());
				list.add(getTabella(Costanti.LABEL_COMUNICAZIONI, values, errList,300,columnStyle,horizontalTextAlignment, log));
			}

			// informazioni su pago pa
			TreeMap<String, String> contenutoStaticoPagoPa = avviso.getContenutoStatico().get(AvvisoPagamentoCostanti.SEZIONE_PAGOPA_KEY);
			if(contenutoStaticoPagoPa != null && !contenutoStaticoPagoPa.isEmpty()){
				List<String> values = new ArrayList<String>();
				StringBuilder sb = new StringBuilder();
				for (String contenutoKey : contenutoStaticoPagoPa.keySet()) {
					if(sb.length() > 0)
						sb.append("<br/>");

					String value = contenutoStaticoPagoPa.get(contenutoKey);
					sb.append(value);

				}
				values.add(sb.toString());
				list.newRow(20);
				list.add(getTabella(null, values, errList,300,columnStyle,horizontalTextAlignment, log));
			}

			return list; 
		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
	}

	public static ComponentBuilder<?, ?> createSezioneCodici(AvvisoPagamento avviso,  List<String> errList, Logger log){
		String sezione = "Sezione Codici";
		try{
			if(avviso.getBarCode() == null)
				throw new Exception("BarCode non trovato.");

			if(avviso.getQrCode() == null)
				throw new Exception("QrCode non trovato.");

			String barCode = new String(avviso.getBarCode());

			String qrCode = new String(avviso.getQrCode());

			return cmp.horizontalList().add(
					cmp.hListCell(cmp.horizontalGap(20)).heightFixedOnBottom(),
					cmp.hListCell(bcode.code128(barCode).setFixedDimension(240, 50)).heightFixedOnBottom(),
					cmp.hListCell(cmp.verticalList(cmp.horizontalGap(160),cmp.verticalGap(5))).heightFixedOnBottom(), 
					cmp.hListCell(bcode.qrCode(qrCode).setFixedDimension(110, 110)).heightFixedOnBottom(),
					cmp.hListCell(cmp.horizontalGap(20)).heightFixedOnBottom()
					).newRow().add(cmp.verticalGap(20)); 

		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
	}
}
