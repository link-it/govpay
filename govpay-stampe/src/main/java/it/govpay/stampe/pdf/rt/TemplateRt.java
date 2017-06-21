package it.govpay.stampe.pdf.rt;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import it.govpay.model.RicevutaPagamento;
import it.govpay.stampe.pdf.Costanti;
import it.govpay.stampe.pdf.TemplateBase;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;

public class TemplateRt {

	/**
	 * 
	 * TITOLO
	 * 
	 * TITOLO RICEVUTA sx Logo PAGOPA dx
	 * 
	 * @param pathLoghi
	 * @param avviso
	 * @param errList
	 * @param log
	 * @return
	 */
	public static ComponentBuilder<?, ?> createTitleComponent(RicevutaPagamento ricevuta,  List<String> errList, Logger log){
		String sezione = "Sezione Titolo";
		try{
			List<ComponentBuilder<?, ?>> lst = new ArrayList<ComponentBuilder<?,?>>();
			InputStream resourceLogoPagoPa = new ByteArrayInputStream(Base64.decodeBase64(Costanti.logoPagoPa));
			StyleBuilder headerStyle = stl.style(TemplateBase.bold18LeftStyle); 
			
			if(ricevuta.getLogoDominioCreditore()!=null && ricevuta.getLogoDominioCreditore().length > 0){
				lst.add(cmp.image(new ByteArrayInputStream(ricevuta.getLogoDominioCreditore())).setFixedDimension(90, 90).setHorizontalImageAlignment(HorizontalImageAlignment.LEFT)); 
			}else {
				lst.add(cmp.text("   ").setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setStyle(headerStyle.setLeftPadding(10)));
			}

			HorizontalTextAlignment horizontalTextAlignment = HorizontalTextAlignment.CENTER;

			lst.add(cmp.text(Costanti.LABEL_RICEVUTA_PAGAMENTO).setHorizontalTextAlignment(horizontalTextAlignment).setStyle(headerStyle)); 
			lst.add(cmp.image(resourceLogoPagoPa).setFixedDimension(90, 90).setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT));

			return cmp.horizontalList(lst.toArray(new ComponentBuilder[lst.size()])).newRow().add(cmp.verticalGap(20)).newRow();

		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
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
	public static ComponentBuilder<?, ?> createSezioneCreditore(RicevutaPagamento ricevuta,  List<String> errList, Logger log){
		String sezione = "Sezione Creditore";
		try{
			HorizontalListBuilder listRiepilogo = cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.fontStyle12).setLeftPadding(10).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)); 

			if(ricevuta.getAnagraficaDebitore() != null){
				StringBuffer sb = new StringBuffer();
				if(StringUtils.isNotEmpty(ricevuta.getAnagraficaDebitore().getRagioneSociale()))
					sb.append(ricevuta.getAnagraficaDebitore().getRagioneSociale());
				if(StringUtils.isNotEmpty(ricevuta.getAnagraficaDebitore().getCodUnivoco())){
					if(sb.length() > 0)
						sb.append(" (").append(ricevuta.getAnagraficaDebitore().getCodUnivoco()).append(")");
					else 
						sb.append(ricevuta.getAnagraficaDebitore().getCodUnivoco());
				}

				TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_DEBITORE, sb.toString(), true, false, true);
			}
			
			if(ricevuta.getAnagraficaCreditore() != null){
				StringBuffer sb = new StringBuffer();
				if(StringUtils.isNotEmpty(ricevuta.getAnagraficaCreditore().getRagioneSociale()))
					sb.append(ricevuta.getAnagraficaCreditore().getRagioneSociale());
				if(StringUtils.isNotEmpty(ricevuta.getAnagraficaCreditore().getCodUnivoco())){
					if(sb.length() > 0)
						sb.append(" (").append(ricevuta.getAnagraficaCreditore().getCodUnivoco()).append(")");
					else 
						sb.append(ricevuta.getAnagraficaCreditore().getCodUnivoco());
				}

				TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_CREDITORE, sb.toString(), true, false, true);
			}
			if(ricevuta.getAnagraficaAttestante() != null){
				StringBuffer sb = new StringBuffer();
				if(StringUtils.isNotEmpty(ricevuta.getAnagraficaAttestante().getRagioneSociale()))
					sb.append(ricevuta.getAnagraficaAttestante().getRagioneSociale());
				if(StringUtils.isNotEmpty(ricevuta.getAnagraficaAttestante().getCodUnivoco())){
					if(sb.length() > 0)
						sb.append(" (").append(ricevuta.getAnagraficaAttestante().getCodUnivoco()).append(")");
					else 
						sb.append(ricevuta.getAnagraficaAttestante().getCodUnivoco());
				}

				TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_ATTESTANTE, sb.toString(), true, false, true);
			}

			if(ricevuta.getAnagraficaVersante() != null){
				StringBuffer sb = new StringBuffer();
				if(StringUtils.isNotEmpty(ricevuta.getAnagraficaVersante().getRagioneSociale()))
					sb.append(ricevuta.getAnagraficaVersante().getRagioneSociale());
				if(StringUtils.isNotEmpty(ricevuta.getAnagraficaVersante().getCodUnivoco())){
					if(sb.length() > 0)
						sb.append(" (").append(ricevuta.getAnagraficaVersante().getCodUnivoco()).append(")");
					else 
						sb.append(ricevuta.getAnagraficaVersante().getCodUnivoco());
				}

				TemplateBase.addElementoLista(listRiepilogo, Costanti.LABEL_VERSANTE, sb.toString(), true, false, true);
			}

			return listRiepilogo.newRow().add(cmp.verticalGap(20)).newRow()
					;

		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
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
	public static ComponentBuilder<?, ?> createSezioneDebitore(RicevutaPagamento ricevuta,  List<String> errList, Logger log){
		String sezione = "Sezione Debitore";
		try{
			List<ComponentBuilder<?, ?>> lst = new ArrayList<ComponentBuilder<?,?>>();
			lst.add(cmp.text(" "));
			lst.add(TemplateBase.createDatiDebitore(ricevuta.getAnagraficaDebitore(), false, false, stl.style(TemplateBase.rootStyle).setPadding(0), HorizontalTextAlignment.LEFT ,log));

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
	public static ComponentBuilder<?, ?> createSezioneTitoloRicevuta(RicevutaPagamento ricevuta,  List<String> errList, Logger log){
		String sezione = "Sezione Titolo Ricevuta";
		try{
			HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.fontStyle16)
					.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE));
			String label = MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_RICEVUTA_PAGAMENTO_UPPER_CASE, ricevuta.getCodAvviso());
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

	/**
	 * 
	 * sx causale, dx importi
	 * 
	 * @param avviso
	 * @param errList
	 * @param log
	 * @return
	 */
	public static ComponentBuilder<?, ?> createSezionePagamento(RicevutaPagamento ricevuta,  List<String> errList, Logger log){
		String sezione = "Sezione Pagamento";
		try{
			//			VerticalTextAlignment verticalTextAlignment = VerticalTextAlignment.TOP;
			//			StyleBuilder style = stl.style(TemplateBase.rootFont).setVerticalTextAlignment(verticalTextAlignment);;  
			return cmp.horizontalList().add(
					cmp.hListCell(createSezioneCausale(ricevuta, errList, log)).heightFixedOnTop(),
					cmp.hListCell(createSezioneImporti(ricevuta, errList, log)).heightFixedOnTop()				
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
	public static ComponentBuilder<?, ?> createSezioneCausale(RicevutaPagamento ricevuta, List<String> errList, Logger log){
		String sezione = "Sezione Causale";
		try{
			HorizontalTextAlignment horizontalTextAlignment = HorizontalTextAlignment.LEFT;
			StyleBuilder style = stl.style(TemplateBase.rootStyle).setPadding(0).setHorizontalTextAlignment(horizontalTextAlignment).setVerticalTextAlignment(VerticalTextAlignment.TOP);
			VerticalListBuilder verticalList = cmp.verticalList().setStyle(style);

			verticalList.add(cmp.verticalGap(20));
			verticalList.add(cmp.text(ricevuta.getCausale()).setStyle(style).setHorizontalTextAlignment(horizontalTextAlignment));
			verticalList.add(cmp.text(Costanti.LABEL_INTESTATO_A).setStyle(style).setHorizontalTextAlignment(horizontalTextAlignment));
			TemplateBase.createDatiDebitore(verticalList,ricevuta.getAnagraficaDebitore(), true, false, style, horizontalTextAlignment ,log);
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
	public static ComponentBuilder<?, ?> createSezioneImporti(RicevutaPagamento ricevuta,  List<String> errList, Logger log){
		String sezione = "Sezione Importi";
		try{
			StyleBuilder style = stl.style(TemplateBase.rootStyle).setPadding(0).setLeftPadding(10); 
			HorizontalTextAlignment horizontalTextAlignment = HorizontalTextAlignment.LEFT;
			VerticalListBuilder verticalList = cmp.verticalList().setStyle(stl.style(style)
					.setHorizontalTextAlignment(horizontalTextAlignment).setVerticalTextAlignment(VerticalTextAlignment.TOP)); 

			StyleBuilder columnStyle = stl.style(TemplateBase.columnBorderStyle).setLeftPadding(5).setRightPadding(0).setTopPadding(5).setBottomPadding(5);

			List<String> values = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			boolean addBreak = false;
			if(ricevuta.getImportoDovuto() != null) {
				String importoDovutoAsString = "‎€"+ ricevuta.getImportoDovuto().doubleValue();
				sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_IMPORTO_DOVUTO, importoDovutoAsString));
				addBreak = true;
			}

			if(ricevuta.getDataScadenza() != null){
				if(addBreak)
					sb.append("<br/>");
				sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_DATA_SCADENZA, TemplateBase.sdf_ddMMyyyy.format(ricevuta.getDataScadenza())));
				addBreak = true;
			}
			String importoPagatoAsString = "‎€"+ ricevuta.getImportoPagato().doubleValue(); 
			if(addBreak) 
				sb.append("<br/>");

			sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_IMPORTO_PAGATO, importoPagatoAsString));
			if(ricevuta.getCommissioni() != null){
				String commissioniAsString = "‎€"+ ricevuta.getCommissioni().doubleValue(); 
				sb.append("<br/>");
				sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_COMMISSIONI_PSP, commissioniAsString));
			}
			if(ricevuta.getDataPagamento() != null){
				sb.append("<br/>");
				sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_DATA_PAGAMENTO, TemplateBase.sdf_ddMMyyyy.format(ricevuta.getDataPagamento())));
			}
			if(StringUtils.isNotEmpty(ricevuta.getIuv())){
				sb.append("<br/>");
				sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_IUV, ricevuta.getIuv()));
			}
			if(StringUtils.isNotEmpty(ricevuta.getCcp())){
				sb.append("<br/>");
				sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_CCP, ricevuta.getCcp()));
			}
			if(StringUtils.isNotEmpty(ricevuta.getIdRiscossione())){
				sb.append("<br/>");
				sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_ID_RISCOSSIONE, ricevuta.getIdRiscossione()));
			}
			//			sb.append("<br/>");
			//			sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_PSP, ricevuta.getPsp()));
			if(StringUtils.isNotEmpty(ricevuta.getCausale())){
				sb.append("<br/>");
				sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_CAUSALE, ricevuta.getCausale()));
			}
			if(StringUtils.isNotEmpty(ricevuta.getDescrizioneCausale())){
				sb.append("<br/>");
				sb.append(MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE,Costanti.LABEL_DESCRIZIONE_CAUSALE, ricevuta.getDescrizioneCausale()));
			}

			values.add(sb.toString());
			verticalList.add(TemplateBase.getTabella(Costanti.LABEL_ESTREMI_DI_PAGAMENTO,values, errList,150, columnStyle,horizontalTextAlignment,log));

			return verticalList; //.setFixedWidth(200);//.setFixedHeight(90)	; 

		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
	}
}
