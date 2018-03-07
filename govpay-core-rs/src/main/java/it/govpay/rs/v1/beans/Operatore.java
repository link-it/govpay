package it.govpay.rs.v1.beans;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Versamento;

public class Operatore  extends it.govpay.rs.v1.beans.base.Operatore{

	public Operatore() {}

	@Override
	public String getJsonIdFilter() {
		return "operatore";
	}

	public static Operatore parse(String json) {
		return (Operatore) parse(json, Operatore.class);
	}


	public Operatore(it.govpay.bd.model.Operatore operatore) throws ServiceException {
		this.abilitato(operatore.getUtenza().isAbilitato())
		.principal(operatore.getUtenza().getPrincipal())
		.ragioneSociale(operatore.getNome());
		
	}

}
