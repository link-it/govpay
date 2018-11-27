package it.govpay.core.business;

import java.util.List;
import java.util.regex.Pattern;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.proxy.Actor;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Utenza;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.CredentialUtils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;

public class Applicazione extends BasicBD{

	public Applicazione(BasicBD basicBD) {
		super(basicBD);
	}

	
	public it.govpay.bd.model.Applicazione getApplicazioneAutenticata(Utenza user) throws GovPayException, ServiceException {
		if(user == null || user.getPrincipal() == null) {
			throw new GovPayException(EsitoOperazione.AUT_000);
		}
		
		it.govpay.bd.model.Applicazione applicazione = null;
		try {
			applicazione = CredentialUtils.getApplicazione(this, user);  
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.AUT_001, user.getPrincipal());
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
	
	public it.govpay.bd.model.Applicazione getApplicazioneDominio(Dominio dominio,String iuv) throws GovPayException, ServiceException {
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
		
		throw new GovPayException(EsitoOperazione.INTERNAL, "Nessuna applicazione trovata per gestire lo IUV ["+iuv+"] per il dominio ["+dominio.getCodDominio()+"]");
	}
}
