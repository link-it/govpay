package it.govpay.stampe.pdf.rt;

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
import org.apache.logging.log4j.Logger;

import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.govpay.stampe.pdf.Costanti;
import it.govpay.stampe.pdf.TemplateBase;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;

public class TemplateRt {

	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createTitleComponent(String pathLoghi, CtRicevutaTelematica rt,List<String> errList,Logger log) {
		try{
			StringBuilder errMsg = new StringBuilder();
			List<ComponentBuilder<?, ?>> lst = new ArrayList<ComponentBuilder<?,?>>();
			InputStream resourceLogoPagoPa = new ByteArrayInputStream(Base64.decodeBase64(Costanti.logoPagoPa));
			CtEnteBeneficiario enteBeneficiario = rt.getEnteBeneficiario();
			String denominazioneDominio = enteBeneficiario.getDenominazioneBeneficiario();
			
			String logoDominio = enteBeneficiario.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco() + ".png";
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
			
			String pIvaDominio = MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE, Costanti.LABEL_P_IVA, enteBeneficiario.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco());
			
			String indirizzo = StringUtils.isNotEmpty(enteBeneficiario.getIndirizzoBeneficiario()) ? enteBeneficiario.getIndirizzoBeneficiario() : "";
			String civico = StringUtils.isNotEmpty(enteBeneficiario.getCivicoBeneficiario()) ? enteBeneficiario.getCivicoBeneficiario() : "";
			String cap = StringUtils.isNotEmpty(enteBeneficiario.getCapBeneficiario()) ? enteBeneficiario.getCapBeneficiario() : "";
			String localita = StringUtils.isNotEmpty(enteBeneficiario.getLocalitaBeneficiario()) ? enteBeneficiario.getLocalitaBeneficiario() : "";
			String provincia = StringUtils.isNotEmpty(enteBeneficiario.getProvinciaBeneficiario()) ? (" (" +enteBeneficiario.getProvinciaBeneficiario() +")" ) : "";


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
			log.error(e,e);
			
		}
		return null;
	}
}
