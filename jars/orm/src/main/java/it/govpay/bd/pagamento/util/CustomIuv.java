package it.govpay.bd.pagamento.util;

import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;

import it.govpay.bd.model.Applicazione;
import it.govpay.model.Dominio;

public class CustomIuv {

//	public final String getCodApplicazione(Dominio dominio, String iuv, Applicazione applicazioneDefault) throws ServiceException {
//		try {
//			return getCodApplicazione(dominio.getCodDominio(), iuv);
//		} catch (NotImplementedException e){
//			return applicazioneDefault != null ? applicazioneDefault.getCodApplicazione() : null;
//		}
//	}
	public String buildPrefix(Applicazione applicazione, Dominio dominio, Map<String, String> values) {
		return buildPrefix(applicazione, dominio.getIuvPrefix(), values);
	}

	public String buildPrefix(Applicazione applicazione, String prefix, Map<String, String> values) {
		if(prefix == null) return "";
		
		StrSubstitutor sub = new StrSubstitutor(values, "%(", ")");
		String result = sub.replace(prefix);

		return result;
	}

	public boolean isNumericOnly(Applicazione applicazione, it.govpay.bd.model.Dominio dominio, Map<String, String> allIuvProps) {
		return false;
	}
}
