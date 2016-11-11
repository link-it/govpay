package it.govpay.stampe.pdf.avvisoPagamento;

import static net.sf.dynamicreports.report.builder.DynamicReports.bcode;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;

public class TemplateAvvisoPagamento {

	/**
	 * 
	 * TITOLO
	 * 
	 * IMG ENTE sx           IMG PAGAPA dx
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
			
			
			lstTitolo.add(TemplateBase.createDatiDominio(avviso.getDominioCreditore(), avviso.getAnagraficaCreditore(), log));
//			lstTitolo.add(cmp.text(" ").setStyle(TemplateBase.bold18LeftStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));


			lst.add(cmp.verticalList(lstTitolo.toArray(new ComponentBuilder[lstTitolo.size()])));
			lst.add(cmp.image(resourceLogoPagoPa).setFixedDimension(90, 90).setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT));

			return cmp.horizontalList(lst.toArray(new ComponentBuilder[lst.size()])).newRow().add(cmp.verticalGap(20)).newRow().add(cmp.line()).newRow();

		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
	}

	/***
	 * Elenco delle informazioni sul dovuto:
	 * 
	 * 1 CF Creditore
	 * 2 Codice Avviso Pagamento
	 * 3 Importo
	 * 4 Data Scadenza se presente
	 * 5 IUV
	 * 
	 * @param avviso
	 * @param errList
	 * @param log
	 * @return
	 */
	public static ComponentBuilder<?, ?> createSezioneDovuto(AvvisoPagamento avviso,  List<String> errList, Logger log){
		String sezione = "Sezione Dovuto";
		try{
			HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.fontStyle12)
					.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE).setLeftPadding(15).setRightPadding(15).setTopPadding(10)); 

			Dominio dominioCreditore = avviso.getDominioCreditore();
			String codDominio = dominioCreditore.getCodDominio();

			TemplateBase.addElementoLista(list, Costanti.LABEL_CODICE_FISCALE_CREDITORE, codDominio,true, false, false);
			TemplateBase.addElementoLista(list, Costanti.LABEL_CODICE_AVVISO_PAGAMENTO, avviso.getCodiceAvviso(),true, false, false);
			TemplateBase.addElementoLista(list, Costanti.LABEL_IMPORTO, (avviso.getImporto().doubleValue()+ "‎€"),true, false, false);
			if(avviso.getDataScadenza() != null)
				TemplateBase.addElementoLista(list, Costanti.LABEL_DATA_SCADENZA, TemplateBase.sdf_ddMMyyyy.format(avviso.getDataScadenza()),true, false, false); 
			TemplateBase.addElementoLista(list, Costanti.LABEL_IUV, avviso.getIuv(),true, false, false);

			TreeMap<String, String> contenutoStaticoSezioneDovuto = avviso.getContenutoStatico().get(AvvisoPagamentoCostanti.SEZIONE_DOVUTO_KEY);
			if(contenutoStaticoSezioneDovuto != null && !contenutoStaticoSezioneDovuto.isEmpty()){
				list.add(cmp.verticalGap(10));
				list.newRow();
				for (String contenutoKey : contenutoStaticoSezioneDovuto.keySet()) {
					String value = contenutoStaticoSezioneDovuto.get(contenutoKey);
					if(StringUtils.isNotEmpty(value))
						TemplateBase.addElementoContenutoStaticoLista(list, value, true, false, HorizontalTextAlignment.JUSTIFIED); 
				}
			}

			list.add(cmp.verticalGap(20));
			list.newRow();

			return list;//.setStyle(TemplateBase.centeredStyle);

		}catch(Exception e){
			log.error("Impossibile completare la costruzione della " + sezione +": "+ e.getMessage(),e);
			errList.add(0,"Impossibile completare la costruzione della " + sezione +": "+ e.getMessage());
		}
		return null;
	}

	public static ComponentBuilder<?, ?> createSezioneDisponibilita(AvvisoPagamento avviso,  List<String> errList, Logger log){
		String sezione = "Sezione Disponibilita";
		try{

			HorizontalListBuilder list =  cmp.horizontalList().setBaseStyle(stl.style(TemplateBase.fontStyle12)
					.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE).setLeftPadding(15).setRightPadding(15));

			Dominio dominio = avviso.getDominioCreditore();
			Anagrafica anagrafica = avviso.getAnagraficaCreditore();
			TreeMap<String, String> contenutoStaticoDisponibilita = avviso.getContenutoStatico().get(AvvisoPagamentoCostanti.SEZIONE_DISPONIBILITA_KEY);
			if(contenutoStaticoDisponibilita != null && !contenutoStaticoDisponibilita.isEmpty()){
				list.add(cmp.verticalGap(10));
				list.newRow();
				for (String contenutoKey : contenutoStaticoDisponibilita.keySet()) {
					String value = contenutoStaticoDisponibilita.get(contenutoKey);
					value = value.replace(AvvisoPagamentoCostanti.ENTE_CREDITORE_KEY, dominio.getRagioneSociale());
					
					String urlSitoWeb = StringUtils.isNotEmpty(anagrafica.getUrlSitoWeb()) ? anagrafica.getUrlSitoWeb() : "";
					
					if(StringUtils.isEmpty(anagrafica.getUrlSitoWeb())){
						errList.add("Url sito web per l'ente creditore ["+dominio.getCodDominio()+"] non presente nel documento poiche' non trovata nei dati avviso.");
					}
					
					value = value.replace(AvvisoPagamentoCostanti.URL_ENTE_CREDITORE_KEY, urlSitoWeb);
					if(StringUtils.isNotEmpty(value))
						TemplateBase.addElementoContenutoStaticoLista(list, value, true, false, HorizontalTextAlignment.JUSTIFIED); 
				}
			}

			list.add(cmp.verticalGap(20));
			list.newRow();

			return list; //cmp.verticalList(list).setStyle(TemplateBase.centeredStyle);
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
