package it.govpay.stampe.pdf.rt;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.govpay.bd.BasicBD;
import it.govpay.model.Anagrafica;
import it.govpay.model.Dominio;
import it.govpay.stampe.pdf.Costanti;
import it.govpay.stampe.pdf.TemplateBase;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;

public class TemplateRt {

	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createTitleComponent(BasicBD bd, String pathLoghi, CtRicevutaTelematica rt) {
		try{
			InputStream resourceLogoPagoPa = new ByteArrayInputStream(Base64.decodeBase64(Costanti.logoPagoPa));
			CtEnteBeneficiario enteBeneficiario = rt.getEnteBeneficiario();
			String denominazioneDominio = enteBeneficiario.getDenominazioneBeneficiario();
			
			String logoDominio = enteBeneficiario.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco() + ".png";
			File fEnte = new File(pathLoghi+logoDominio);
			InputStream resourceLogoEnte = new FileInputStream(fEnte);
			
			String pIvaDominio = MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE, Costanti.LABEL_P_IVA, enteBeneficiario.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco());
			
			String indirizzo = StringUtils.isNotEmpty(enteBeneficiario.getIndirizzoBeneficiario()) ? enteBeneficiario.getIndirizzoBeneficiario() : "";
			String civico = StringUtils.isNotEmpty(enteBeneficiario.getCivicoBeneficiario()) ? enteBeneficiario.getCivicoBeneficiario() : "";
			String cap = StringUtils.isNotEmpty(enteBeneficiario.getCapBeneficiario()) ? enteBeneficiario.getCapBeneficiario() : "";
			String localita = StringUtils.isNotEmpty(enteBeneficiario.getLocalitaBeneficiario()) ? enteBeneficiario.getLocalitaBeneficiario() : "";
			String provincia = StringUtils.isNotEmpty(enteBeneficiario.getProvinciaBeneficiario()) ? (" (" +enteBeneficiario.getProvinciaBeneficiario() +")" ) : "";


			String indirizzoCivico = indirizzo + " " + civico;
			String capCitta = cap + " " + localita + provincia;
			
			return cmp.horizontalList()
					.add(
						cmp.horizontalList(
								cmp.image(resourceLogoEnte).setFixedDimension(90, 90),
								cmp.verticalList(
										cmp.text(denominazioneDominio).setStyle(TemplateBase.bold18LeftStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
										cmp.text(pIvaDominio).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
										cmp.text(indirizzoCivico).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
										cmp.text(capCitta).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
										)),
									cmp.image(resourceLogoPagoPa).setFixedDimension(90, 90)
								,
								cmp.verticalGap(20))
					.newRow()
					.add(cmp.line())
					.newRow()
							;
		}catch(Exception e){

		}
		return null;
	}
}
