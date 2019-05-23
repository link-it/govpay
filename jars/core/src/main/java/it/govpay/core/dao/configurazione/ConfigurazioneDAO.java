package it.govpay.core.dao.configurazione;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.configurazione.ConfigurazioneBD;
import it.govpay.bd.model.Configurazione;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.configurazione.dto.LeggiConfigurazioneDTO;
import it.govpay.core.dao.configurazione.dto.LeggiConfigurazioneDTOResponse;
import it.govpay.core.dao.configurazione.dto.PutConfigurazioneDTO;
import it.govpay.core.dao.configurazione.dto.PutConfigurazioneDTOResponse;
import it.govpay.core.dao.configurazione.exception.ConfigurazioneNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;

public class ConfigurazioneDAO extends BaseDAO{
	
	public ConfigurazioneDAO() {
		super();
	}
	
	public ConfigurazioneDAO(boolean useCacheData) {
		super(useCacheData);
	}
	
	public LeggiConfigurazioneDTOResponse getConfigurazione(LeggiConfigurazioneDTO leggiConfigurazioneDTO) throws ConfigurazioneNonTrovataException, NotAuthorizedException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			return new LeggiConfigurazioneDTOResponse(AnagraficaManager.getConfigurazione(bd));
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new ConfigurazioneNonTrovataException("Configurazione Govpay non definita.");
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}


	public PutConfigurazioneDTOResponse salvaConfigurazioneGiornaleEventi(PutConfigurazioneDTO putConfigurazioneDTO) throws ConfigurazioneNonTrovataException, ServiceException, NotAuthorizedException, NotAuthenticatedException, UnprocessableEntityException {  
		PutConfigurazioneDTOResponse putConfigurazioneDTOResponse = new PutConfigurazioneDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			ConfigurazioneBD configurazioneBD = new ConfigurazioneBD(bd);
			
			Configurazione configurazione = null;
			boolean created = false;
			try {
				configurazione = configurazioneBD.getConfigurazione();
			}catch(NotFoundException e) {
				configurazione = new Configurazione();
				created = true;
			}
			
			// aggiorno configurazione del giornale eventi
			configurazione.setGiornale(putConfigurazioneDTO.getGiornale());
			
			// flag creazione o update
			putConfigurazioneDTOResponse.setCreated(created);
			configurazioneBD.salvaConfigurazione(configurazione);
			
			// elimino la entry in cache
			AnagraficaManager.removeFromCache(configurazione);
		} finally {
			if(bd != null) 
				bd.closeConnection();
		}
		return putConfigurazioneDTOResponse;
	}
}
