package it.govpay.bd.model;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;

public class Applicazione extends it.govpay.model.Applicazione{

private static final long serialVersionUID = 1L;
	
	public Applicazione() {
		super();
	}
	
	private transient Utenza utenza;
	
	public Applicazione(BDConfigWrapper configWrapper, long idUtenza) throws ServiceException {
		super();
		this.setIdUtenza(idUtenza); 
		try {
			this.setUtenza(AnagraficaManager.getUtenza(configWrapper, this.getIdUtenza()));
		} catch (NotFoundException e) {
		}
	}


	public Utenza getUtenza() {
		return this.utenza;
	}


	public void setUtenza(Utenza utenza) {
		this.utenza = utenza;
	}

	public String getPrincipal() {
		return this.utenza != null ? this.utenza.getPrincipal() : null;
	}

}
