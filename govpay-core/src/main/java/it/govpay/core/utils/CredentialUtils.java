package it.govpay.core.utils;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.Utilities;
import org.openspcoop2.utils.transport.http.HttpServletCredential;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Utenza;

public class CredentialUtils {
	
	public static Utenza getUser(HttpServletRequest request, Logger log) {
		HttpServletCredential credential = new HttpServletCredential(request, log);
		return getUser(credential); 
	}

	public static Utenza getUser(HttpServletCredential credential) {
		Utenza user = new Utenza();
		
		if(credential.getSubject() != null) {
			user.setPrincipal(credential.getSubject());
			user.setCheckSubject(true); 
		} else if(credential.getPrincipal() != null) {
			user.setPrincipal(credential.getPrincipal());
			user.setCheckSubject(false); 
		} else {
			user.setPrincipal(null);
		}
		
		return user;
	}
	
	public static Applicazione getApplicazione(HttpServletRequest request, Logger log,BasicBD bd) throws ServiceException, NotFoundException {
		HttpServletCredential credential = new HttpServletCredential(request, log);
		Utenza user = getUser(credential); 
		return getApplicazione(bd, user);
	}

	public static Applicazione getApplicazione(BasicBD bd, Utenza user) throws ServiceException, NotFoundException {
		return  user.isCheckSubject() ? 
				AnagraficaManager.getApplicazioneBySubject(bd, user.getPrincipal())
				: AnagraficaManager.getApplicazioneByPrincipal(bd, user.getPrincipal());
	}
	
	public static boolean checkSubject(String principalToCheck, String principalFromRequest) throws Exception{
		boolean ok = true;
		
		principalFromRequest = Utilities.formatSubject(principalFromRequest);
		principalToCheck = Utilities.formatSubject(principalToCheck);
		
		Hashtable<String, String> hashSubject = Utilities.getSubjectIntoHashtable(principalFromRequest);
		Enumeration<String> keys = hashSubject.keys();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			String value = hashSubject.get(key);
			ok = ok && principalToCheck.contains("/"+Utilities.formatKeySubject(key)+"="+Utilities.formatValueSubject(value)+"/");
		}
		
		return ok;
	}
}
