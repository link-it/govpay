package it.govpay.core.business;

import java.util.List;
import java.util.regex.Pattern;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.model.Dominio;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;

public class Applicazione extends BasicBD{

	public Applicazione(BasicBD basicBD) {
		super(basicBD);
	}
	
	public it.govpay.bd.model.Applicazione getApplicazioneDominio(Dominio dominio,String iuv) throws GovPayException, ServiceException {
		return getApplicazioneDominio(dominio, iuv, true);
	}
	
	public it.govpay.bd.model.Applicazione getApplicazioneDominio(Dominio dominio,String iuv, boolean throwException) throws GovPayException, ServiceException {
	
		ApplicazioniBD applicazioniBD = new ApplicazioniBD(this);
		List<it.govpay.bd.model.Applicazione> listaApplicazioni = applicazioniBD.findAll(applicazioniBD.newFilter());
		
		// restituisco la prima applicazione che gestisce il dominio passato
		for (it.govpay.bd.model.Applicazione applicazione : listaApplicazioni) {
			if(applicazione.getUtenza().getIdDomini().contains(dominio.getId())) {
				Pattern pIuv = Pattern.compile(applicazione.getRegExp());
				if(pIuv.matcher(iuv).matches())
					return applicazione;
			}
		}
		
		// restituisco la prima applicazione che gestisce il pattern dello iuv passato
		for (it.govpay.bd.model.Applicazione applicazione : listaApplicazioni) {
			Pattern pIuv = Pattern.compile(applicazione.getRegExp());
			if(pIuv.matcher(iuv).matches())
				return applicazione;
		}
		
		if(throwException)
			throw new GovPayException(EsitoOperazione.APP_006, iuv, dominio.getCodDominio());
		 
		return null;
	}
	
	
}
