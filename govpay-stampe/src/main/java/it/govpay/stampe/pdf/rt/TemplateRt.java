package it.govpay.stampe.pdf.rt;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Dominio;
import it.govpay.stampe.pdf.Costanti;
import it.govpay.stampe.pdf.TemplateBase;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;

public class TemplateRt {

	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createTitleComponent(BasicBD bd, String pathLoghi, Dominio dominio) {
		try{

			String logoDominio = dominio.getCodDominio() + ".png";
			File fEnte = new File(pathLoghi+logoDominio);
			InputStream resourceLogoEnte = new FileInputStream(fEnte);
			InputStream resourceLogoPagoPa = new ByteArrayInputStream(Base64.decodeBase64(Costanti.logoPagoPa));
			
			String denominazioneDominio = dominio.getRagioneSociale();
			String pIvaDominio = MessageFormat.format(Costanti.PATTERN_NOME_DUE_PUNTI_VALORE, Costanti.LABEL_P_IVA, dominio.getCodDominio());
			Anagrafica anagrafica = dominio.getAnagrafica(bd);

			String indirizzo = StringUtils.isNotEmpty(anagrafica.getIndirizzo()) ? anagrafica.getIndirizzo() : "";
			String civico = StringUtils.isNotEmpty(anagrafica.getCivico()) ? anagrafica.getCivico() : "";
			String cap = StringUtils.isNotEmpty(anagrafica.getCap()) ? anagrafica.getCap() : "";
			String localita = StringUtils.isNotEmpty(anagrafica.getLocalita()) ? anagrafica.getLocalita() : "";
			String provincia = StringUtils.isNotEmpty(anagrafica.getProvincia()) ? (" (" +anagrafica.getProvincia() +")" ) : "";


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
