package it.govpay.bd.model;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Utenza;

public class Applicazione extends it.govpay.model.Applicazione{

private static final long serialVersionUID = 1L;
	
	public Applicazione() {
		super();
	}
	
	private transient Utenza utenza;
	
	public Applicazione(BasicBD bd, long idUtenza) throws ServiceException {
		super();
		this.setIdUtenza(idUtenza); 
		this.setUtenza(AnagraficaManager.getUtenza(bd, this.getIdUtenza()));
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
