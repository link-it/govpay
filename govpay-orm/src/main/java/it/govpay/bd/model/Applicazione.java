package it.govpay.bd.model;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.model.Tributo;
import it.govpay.model.Utenza;

public class Applicazione extends it.govpay.model.Applicazione{

private static final long serialVersionUID = 1L;
	
	public Applicazione() {
		super();
	}
	
	private transient List<Dominio> domini;
	private transient List<Tributo> tributi;
	private transient Utenza utenza;
	
	public Applicazione(BasicBD bd, long idUtenza) throws ServiceException {
		super();
		this.setIdUtenza(idUtenza); 
		this.setUtenza(AnagraficaManager.getUtenza(bd, this.getIdUtenza())); 
	}


	public Utenza getUtenza() {
		return utenza;
	}


	public void setUtenza(Utenza utenza) {
		this.utenza = utenza;
	}

	public String getPrincipal() {
		return this.utenza != null ? this.utenza.getPrincipal() : null;
	}
}
