package it.govpay.stampe.pdf.er;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtEsitoRevoca;
import it.govpay.model.Anagrafica;
import it.govpay.model.Dominio;
import it.govpay.stampe.pdf.Costanti;
import it.govpay.stampe.pdf.TemplateBase;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;

public class TemplateEr {

	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createTitleComponent(String pathLoghi, CtEsitoRevoca er,Dominio dominio, Anagrafica anagraficaDominio, List<String> errList,Logger log) {
		try{
			StringBuilder errMsg = new StringBuilder();
			List<ComponentBuilder<?, ?>> lst = new ArrayList<>();
			InputStream resourceLogoPagoPa = new ByteArrayInputStream(Base64.decodeBase64(Costanti.logoPagoPa));
			String denominazioneDominio = dominio.getRagioneSociale();
			
			String logoDominio = dominio.getCodDominio() + ".png";
			File fEnte = new File(pathLoghi+"/"+logoDominio);
			
			
			if(fEnte.exists()){
				InputStream resourceLogoEnte = new FileInputStream(fEnte);
				lst.add(cmp.image(resourceLogoEnte).setFixedDimension(90, 90));
			}else {
				if(errMsg.length() >0)
					errMsg.append(", ");

				errMsg.append(" l'estratto conto non contiene il logo del dominio poiche' il file ["+logoDominio+"] non e' stato trovato nella directory dei loghi");
			}

			if(errMsg.length() >0){
				errList.add(errMsg.toString());
			}
			
			String pIvaDominio = MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE, Costanti.LABEL_P_IVA, dominio.getCodDominio());
			
			 
			String indirizzo = StringUtils.isNotEmpty(anagraficaDominio.getIndirizzo()) ? anagraficaDominio.getIndirizzo() : "";
			String civico = StringUtils.isNotEmpty(anagraficaDominio.getCivico()) ? anagraficaDominio.getCivico() : "";
			String cap = StringUtils.isNotEmpty(anagraficaDominio.getCap()) ? anagraficaDominio.getCap() : "";
			String localita = StringUtils.isNotEmpty(anagraficaDominio.getLocalita()) ? anagraficaDominio.getLocalita() : "";
			String provincia = StringUtils.isNotEmpty(anagraficaDominio.getProvincia()) ? (" (" +anagraficaDominio.getProvincia() +")" ) : "";


			String indirizzoCivico = indirizzo + " " + civico;
			String capCitta = cap + " " + localita + provincia;
			
			lst.add(cmp.verticalList(
					cmp.text(denominazioneDominio).setStyle(TemplateBase.bold18LeftStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
					cmp.text(pIvaDominio).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
					cmp.text(indirizzoCivico).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
					cmp.text(capCitta).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
					));
			
			return cmp.horizontalList()
					.add(cmp.horizontalList(lst.toArray(new ComponentBuilder[lst.size()])),
							cmp.image(resourceLogoPagoPa).setFixedDimension(90, 90),
								cmp.verticalGap(20))
					.newRow()
					.add(cmp.line())
					.newRow()
							;
		}catch(Exception e){
			log.error(e.getMessage(),e);
			
		}
		return null;
	}
}
