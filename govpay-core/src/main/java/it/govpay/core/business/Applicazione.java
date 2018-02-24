package it.govpay.core.business;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.proxy.Actor;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.servizi.commons.EsitoOperazione;

public class Applicazione extends BasicBD{

	public Applicazione(BasicBD basicBD) {
		super(basicBD);
	}

	
	public it.govpay.model.Applicazione getApplicazioneAutenticata(String principal) throws GovPayException, ServiceException {
		if(principal == null) {
			throw new GovPayException(EsitoOperazione.AUT_000);
		}
		
		it.govpay.model.Applicazione applicazione = null;
		try {
			applicazione =  AnagraficaManager.getApplicazioneByPrincipal(this, principal);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.AUT_002, principal);
		}
		
		if(applicazione != null) {
			Actor from = new Actor();
			from.setName(applicazione.getCodApplicazione());
			from.setType(GpContext.TIPO_SOGGETTO_PRT);
			GpThreadLocal.get().getTransaction().setFrom(from);
			GpThreadLocal.get().getTransaction().getClient().setName(applicazione.getCodApplicazione());
		}
		
		return applicazione;
	}
	
	public void autorizzaApplicazione(String codApplicazione, it.govpay.model.Applicazione applicazioneAutenticata, BasicBD bd) throws GovPayException, ServiceException {
		it.govpay.model.Applicazione applicazione = null;
		try {
			applicazione = AnagraficaManager.getApplicazione(bd, codApplicazione);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.APP_000, codApplicazione);
		}

		if(!applicazione.isAbilitato())
			throw new GovPayException(EsitoOperazione.APP_001, codApplicazione);

		if(!applicazione.getCodApplicazione().equals(applicazioneAutenticata.getCodApplicazione()))
			throw new GovPayException(EsitoOperazione.APP_002, applicazioneAutenticata.getCodApplicazione(), codApplicazione);
	}
}
