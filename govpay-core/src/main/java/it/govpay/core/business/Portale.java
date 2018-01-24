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

public class Portale extends BasicBD{

	public Portale(BasicBD basicBD) {
		super(basicBD);
	}

	
	public it.govpay.model.Portale getPortaleAutenticato(String principal) throws GovPayException, ServiceException {
		if(principal == null) {
			throw new GovPayException(EsitoOperazione.AUT_000);
		}
		
		it.govpay.model.Portale prt = null;
		try {
			prt =  AnagraficaManager.getPortaleByPrincipal(this, principal);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.AUT_002, principal);
		}
		
		if(prt != null) {
			Actor from = new Actor();
			from.setName(prt.getCodPortale());
			from.setType(GpContext.TIPO_SOGGETTO_PRT);
			GpThreadLocal.get().getTransaction().setFrom(from);
			GpThreadLocal.get().getTransaction().getClient().setName(prt.getCodPortale());
		}
		
		return prt;
	}
	
	public void autorizzaPortale(String codPortale, it.govpay.model.Portale portaleAutenticato, BasicBD bd) throws GovPayException, ServiceException {
		it.govpay.model.Portale portale = null;
		try {
			portale = AnagraficaManager.getPortale(bd, codPortale);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.PRT_000, codPortale);
		}

		if(!portale.isAbilitato())
			throw new GovPayException(EsitoOperazione.PRT_001, codPortale);

		if(!portale.getCodPortale().equals(portaleAutenticato.getCodPortale()))
			throw new GovPayException(EsitoOperazione.PRT_002, portaleAutenticato.getCodPortale(), codPortale);
	}
}
