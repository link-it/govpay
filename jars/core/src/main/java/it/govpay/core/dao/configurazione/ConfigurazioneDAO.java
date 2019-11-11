package it.govpay.core.dao.configurazione;

import java.text.MessageFormat;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.configurazione.model.Hardening;
import it.govpay.bd.configurazione.model.Mail;
import it.govpay.bd.configurazione.model.MailBatch;
import it.govpay.bd.configurazione.model.TracciatoCsv;
import it.govpay.bd.model.Configurazione;
import it.govpay.core.dao.anagrafica.utils.UtenzaPatchUtils;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.configurazione.dto.LeggiConfigurazioneDTO;
import it.govpay.core.dao.configurazione.dto.LeggiConfigurazioneDTOResponse;
import it.govpay.core.dao.configurazione.dto.PatchConfigurazioneDTO;
import it.govpay.core.dao.configurazione.dto.PutConfigurazioneDTO;
import it.govpay.core.dao.configurazione.dto.PutConfigurazioneDTOResponse;
import it.govpay.core.dao.configurazione.exception.ConfigurazioneNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.model.PatchOp;

public class ConfigurazioneDAO extends BaseDAO{
	
	public static final String PATH_GIORNALE_EVENTI = "/giornaleEventi";
	public static final String PATH_TRACCIATO_CSV = "/tracciatoCsv";
	public static final String PATH_HARDENING = "/hardening";
	public static final String PATH_MAIL_BATCH = "/mailBatch";
	public static final String PATH_MAIL_PROMEMORIA = "/mailPromemoria";
	public static final String PATH_MAIL_RICEVUTA = "/mailRicevuta";
	
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
			it.govpay.core.business.Configurazione configurazioneBD = new it.govpay.core.business.Configurazione(bd);
			return new LeggiConfigurazioneDTOResponse(configurazioneBD.getConfigurazione());
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}


	public PutConfigurazioneDTOResponse salvaConfigurazione(PutConfigurazioneDTO putConfigurazioneDTO) throws ConfigurazioneNonTrovataException, ServiceException, NotAuthorizedException, NotAuthenticatedException, UnprocessableEntityException {  
		PutConfigurazioneDTOResponse putConfigurazioneDTOResponse = new PutConfigurazioneDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			it.govpay.core.business.Configurazione configurazioneBD = new it.govpay.core.business.Configurazione(bd);
			
			boolean created = false;
			// salvo l'intero oggetto in blocco
			Configurazione configurazione = putConfigurazioneDTO.getConfigurazione();
			
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

	public LeggiConfigurazioneDTOResponse patchConfigurazione(PatchConfigurazioneDTO patchConfigurazioneDTO) throws ServiceException, ValidationException { 
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			it.govpay.core.business.Configurazione configurazioneBD = new it.govpay.core.business.Configurazione(bd);
			
			Configurazione configurazione = configurazioneBD.getConfigurazione();
			
			for(PatchOp op: patchConfigurazioneDTO.getOp()) {
				if(PATH_GIORNALE_EVENTI.equals(op.getPath())) {
					Giornale giornale = (Giornale) op.getValue();
					configurazione.setGiornale(giornale);
				} else if(PATH_TRACCIATO_CSV.equals(op.getPath())) {
					TracciatoCsv tracciatoCsv = (TracciatoCsv) op.getValue();
					configurazione.setTracciatoCsv(tracciatoCsv);
				} else if(PATH_HARDENING.equals(op.getPath())) {
					Hardening hardening = (Hardening) op.getValue();
					configurazione.setHardening(hardening);
				} else if(PATH_MAIL_BATCH.equals(op.getPath())) {
					MailBatch batchSpedizioneMail = (MailBatch) op.getValue();
					configurazione.setBatchSpedizioneEmail(batchSpedizioneMail);
				} else if(PATH_MAIL_PROMEMORIA.equals(op.getPath())) {
					Mail promemoriaMail = (Mail) op.getValue();
					configurazione.setPromemoriaEmail(promemoriaMail);
				} else if(PATH_MAIL_RICEVUTA.equals(op.getPath())) {
					Mail ricevutaMail = (Mail) op.getValue();
					configurazione.setRicevutaEmail(ricevutaMail);
				} else {
					throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.PATH_XX_NON_VALIDO, op.getPath()));
				}
			}
			
			configurazioneBD.salvaConfigurazione(configurazione);
			// elimino la entry in cache
			AnagraficaManager.removeFromCache(configurazione);
			return new LeggiConfigurazioneDTOResponse(configurazioneBD.getConfigurazione());
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
}
