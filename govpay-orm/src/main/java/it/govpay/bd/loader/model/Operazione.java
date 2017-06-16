package it.govpay.bd.loader.model;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.model.Applicazione;

public class Operazione extends it.govpay.model.loader.Operazione{

	// BUSINESS
	private transient Applicazione applicazione;
	
	public Applicazione getApplicazione(BasicBD bd) throws ServiceException {
		if(applicazione == null) {
			applicazione = AnagraficaManager.getApplicazione(bd, getIdApplicazione());
		} 
		return applicazione;
	}
	
	public void setApplicazione(String codApplicazione, BasicBD bd) throws ServiceException, NotFoundException {
		applicazione = AnagraficaManager.getApplicazione(bd, codApplicazione);
		this.setIdApplicazione(applicazione.getId());
	}
}
