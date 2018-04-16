package it.govpay.core.utils;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
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
}
