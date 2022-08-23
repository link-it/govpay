package it.govpay.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Versamento;

public class CheckoutUtils {

	public class HiddenParameter  {
		private String name = null;
		private Object value = null;
		private String type = null;
		private Integer arrayElementPos = null;
		private String arrayElementName = null;

		public HiddenParameter(String name, Object value) {
			this.name = name;
			this.value = value;
			this.type = "hidden";
		}
		
		public HiddenParameter(String name, Object value, String arrayElementName, Integer arrayElementPos) {
			this.name = name;
			this.value = value;
			this.type = "hidden";
			this.arrayElementName = arrayElementName;
			this.arrayElementPos = arrayElementPos;
		}

		public String toHtml() {
			StringBuilder sb = new StringBuilder();

				sb.append("<input type=\"").append(this.type).append("\" ")
				.append("name=\"").append(this.name);
				
				//array element
				if(this.arrayElementName != null && this.arrayElementPos != null) {
					sb.append("[").append(this.arrayElementPos).append("]").append("[").append(this.arrayElementName).append("]");
				}
				
				sb.append("\" ")
				.append("value=\"").append(this.value).append("\" ")
				.append("/>"); 
			

			return sb.toString();
		}
	}

	public static HiddenParameter createHiddenParameter(String name, Object value) {
		return new CheckoutUtils().new HiddenParameter(name, value);
	}
	
	public static HiddenParameter createHiddenParameter(String name, Object value, String arrayElementName, Integer arrayElementPos) {
		return new CheckoutUtils().new HiddenParameter(name, value, arrayElementName, arrayElementPos);
	}
	
	public static final String KEY_PARAMETRO_URLRETURN = "returnUrl";
	public static final String KEY_PARAMETRO_FISCALCODE_PA = "fiscalCodePA";
	public static final String KEY_PARAMETRO_LANG = "lang";
	public static final String KEY_PARAMETRO_FISCALCODE = "fiscalCode";
	public static final String KEY_PARAMETRO_AGREEMENT = "agreement";
	public static final String KEY_PARAMETRO_AMOUNT = "amount";
	public static final String KEY_PARAMETRO_NOTICENUMBER = "noticeNumber";
	public static final String KEY_PARAMETRO_AVVISI = "avvisi";
	
	public static final String KEY_PARAMETRI = "@LISTA_INPUT@";
	public static final String KEY_URL_CHECKOUT = "@URL_CHECKOUT@";

	public static final String TEMPLATE_CHECKOUT_HTML = "/checkout.html";
	
	public static List<HiddenParameter> creaListaParametriCheckout(Logger log, BDConfigWrapper configWrapper,  String returnUrl, String lang, String fiscalCodePA, List<Versamento> versamenti, String codiceConvenzione) throws ServiceException{
		List<HiddenParameter> listaParametri = new ArrayList<HiddenParameter>();
		
		log.debug("=== Richiesta Modello 1 SANP 3.1.0 ===");

		// urlreturn
		log.debug("ReturnUrl: ["+returnUrl+"]");
		listaParametri.add(CheckoutUtils.createHiddenParameter(KEY_PARAMETRO_URLRETURN, returnUrl));
		
		//lang
		if(lang != null) {
			log.debug("Lingua: ["+lang+"]");
			listaParametri.add(CheckoutUtils.createHiddenParameter(KEY_PARAMETRO_LANG, lang));
		}

		// fiscalcodepa
		log.debug("FiscalCodePA: ["+fiscalCodePA+"]");
		listaParametri.add(CheckoutUtils.createHiddenParameter(KEY_PARAMETRO_FISCALCODE_PA, fiscalCodePA));
		
		log.debug("\t=== Elenco Avvisi ===");
		
		for (int i = 0; i < versamenti.size(); i++) {
			Versamento vTmp = versamenti.get(i);
			
			String idDominio = vTmp.getDominio(configWrapper).getCodDominio();
			String numeroAvviso = vTmp.getNumeroAvviso();
			BigDecimal importoTotale = vTmp.getImportoTotale();
			
			//fiscalcode
			log.debug("FiscalCode: ["+idDominio+"]");
			listaParametri.add(CheckoutUtils.createHiddenParameter(KEY_PARAMETRO_AVVISI, idDominio,  KEY_PARAMETRO_FISCALCODE, i));
			
			//noticenumber
			log.debug("NoticeNumber: ["+numeroAvviso+"]");
			listaParametri.add(CheckoutUtils.createHiddenParameter(KEY_PARAMETRO_AVVISI, numeroAvviso, KEY_PARAMETRO_NOTICENUMBER, i));
			
			// importo
			log.debug("amount: ["+importoTotale+"]");
			listaParametri.add(CheckoutUtils.createHiddenParameter(KEY_PARAMETRO_AVVISI, importoTotale, KEY_PARAMETRO_AMOUNT, i));
			
			//lang
			if(codiceConvenzione != null) {
				log.debug("agreement: ["+codiceConvenzione+"]");
				listaParametri.add(CheckoutUtils.createHiddenParameter(KEY_PARAMETRO_AVVISI, codiceConvenzione, KEY_PARAMETRO_AGREEMENT, i));
			}
		}
		
		log.debug("=== Fine Richiesta Modello 1 SANP 3.1.0 ===");
		
		return listaParametri;
	}

	public static String creaListaParametriCheckoutAsString(Logger log, BDConfigWrapper configWrapper,  String returnUrl, String lang, String fiscalCodePA, List<Versamento> versamenti, String codiceConvenzione) throws ServiceException{
		List<HiddenParameter> listaParametri = creaListaParametriCheckout(log, configWrapper, returnUrl, lang, fiscalCodePA, versamenti, codiceConvenzione);
		StringBuilder sb = new StringBuilder();
		for (HiddenParameter hiddenParameter : listaParametri) {
			sb.append(" ");
			sb.append(hiddenParameter.toHtml());
		}
		return sb.toString();
	}

	public static String getCheckoutHtml(Logger log, String urlCheckout, String template, BDConfigWrapper configWrapper,  String returnUrl, String lang, String fiscalCodePA, List<Versamento> versamenti, String codiceConvenzione) throws ServiceException{
		String parametri = creaListaParametriCheckoutAsString(log, configWrapper, returnUrl, lang, fiscalCodePA, versamenti, codiceConvenzione);

		String tmp = template.replace(KEY_PARAMETRI, parametri);
		return tmp.replace(KEY_URL_CHECKOUT, urlCheckout);
	}

	public static String readTemplate() throws ServiceException{
		// Recupero il property all'interno dell'EAR
		String template = null;
		try (InputStream is = CheckoutUtils.class.getResourceAsStream(TEMPLATE_CHECKOUT_HTML);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();	){

			IOUtils.copy(is, baos);
			template = baos.toString();
		}catch(Exception e) {
			throw new ServiceException(e);
		} finally {
		}

		return template;

	}
}