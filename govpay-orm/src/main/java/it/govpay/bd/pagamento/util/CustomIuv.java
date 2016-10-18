package it.govpay.bd.pagamento.util;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.Applicazione;
import it.govpay.model.Dominio;

public abstract class CustomIuv {
	
	public abstract long buildIuvNumerico(Applicazione applicazione, Dominio dominio, long prg) throws ServiceException;
	
	public String buildIuvNumerico(Applicazione applicazione, Dominio dominio, long prg, int auxDigit, int applicationCode) throws ServiceException {
		
		long newprg = buildIuvNumerico(applicazione, dominio, prg);
		
		if(newprg > 9999999999999l) {
			throw new ServiceException("Il generatore IUV custom ha prodotto uno IUV superiore alle 13 cifre consentite [" + newprg + "]"); 
		}
		
		return IuvUtils.buildIuvNumerico(newprg, auxDigit, applicationCode);
	}
	
}
