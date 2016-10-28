package it.govpay.bd.pagamento.util;

import org.apache.commons.lang.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.Applicazione;
import it.govpay.model.Dominio;

public class CustomIuv {
	
	public final String buildIuvNumerico(Applicazione applicazione, Dominio dominio, long prg, int auxDigit, int applicationCode) throws ServiceException {
		long newprg = 0;
		
		try {
			newprg = buildIuvNumerico(applicazione, dominio, prg);
		} catch (NotImplementedException e) {
			newprg = prg;
		}
		
		if(newprg > 9999999999999l) {
			throw new ServiceException("Il generatore IUV custom ha prodotto uno IUV superiore alle 13 cifre consentite [" + newprg + "]"); 
		}
		
		return IuvUtils.buildIuvNumerico(newprg, auxDigit, applicationCode);
	}
	
	public final String getCodApplicazione(Dominio dominio, String iuv, Applicazione applicazioneDefault) throws ServiceException {
		try {
			return getCodApplicazione(dominio.getCodDominio(), iuv);
		} catch (NotImplementedException e){
			return applicazioneDefault != null ? applicazioneDefault.getCodApplicazione() : null;
		}
	}
	
	protected String getCodApplicazione(String dominio, String iuv) throws ServiceException, NotImplementedException {
		throw new NotImplementedException();
	}
	
	public long buildIuvNumerico(Applicazione applicazione, Dominio dominio, long prg) throws ServiceException, NotImplementedException {
		throw new NotImplementedException();
	}

}
