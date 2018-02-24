package it.govpay.rs.v1.beans;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.rs.v1.beans.base.CodificaAvvisi;

public class Applicazione extends it.govpay.rs.v1.beans.base.Applicazione {

	@Override
	public String getJsonIdFilter() {
		return "applicazione";
	}
	
	public static Applicazione parse(String json) {
		return (Applicazione) parse(json, Applicazione.class);
	}
	
	public Applicazione(it.govpay.model.Applicazione applicazione) throws ServiceException {
		this.setAbilitato(applicazione.isAbilitato());
		
		CodificaAvvisi codificaAvvisi = new CodificaAvvisi();
		codificaAvvisi.setCodificaIUV(applicazione.getCodApplicazioneIuv());
		codificaAvvisi.setRegExp(applicazione.getRegExp());
		this.setCodificaAvvisi(codificaAvvisi);
		
		this.setIdA2A(applicazione.getCodApplicazione());
		this.setPrincipal(applicazione.getPrincipal());
		this.setServizioNotifica(new Connector(applicazione.getConnettoreNotifica()));
		this.setServizioVerifica(new Connector(applicazione.getConnettoreVerifica()));
		this.setVersioneApi(VersioneApiEnum.fromValue(applicazione.getVersione().toString()));
		
	}
}
