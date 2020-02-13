package it.govpay.core.business;

import java.util.List;
import java.util.regex.Pattern;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
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
			if(applicazione.getUtenza().isAutorizzazioneDominiStar() || applicazione.getUtenza().isIdDominioAutorizzato(dominio.getId())) {
				if(applicazione.getRegExp() != null) {
					Pattern pIuv = Pattern.compile(applicazione.getRegExp());
					if(pIuv.matcher(iuv).matches())
						return applicazione;
				}
			}
		}
		
		if(throwException)
			throw new GovPayException(EsitoOperazione.APP_006, iuv, dominio.getCodDominio());
		 
		return null;
	}
	
	public void autorizzaApplicazione(String codApplicazione, it.govpay.bd.model.Applicazione applicazioneAutenticata, BasicBD bd) throws GovPayException, ServiceException {
		it.govpay.bd.model.Applicazione applicazione = null;
		try {
			applicazione = AnagraficaManager.getApplicazione(bd, codApplicazione);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.APP_000, codApplicazione);
		}

		if(!applicazione.getUtenza().isAbilitato())
			throw new GovPayException(EsitoOperazione.APP_001, codApplicazione);

		if(!applicazione.getCodApplicazione().equals(applicazioneAutenticata.getCodApplicazione()))
			throw new GovPayException(EsitoOperazione.APP_002, applicazioneAutenticata.getCodApplicazione(), codApplicazione);
	}
}
