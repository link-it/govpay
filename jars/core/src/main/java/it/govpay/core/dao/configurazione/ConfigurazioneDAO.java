package it.govpay.core.dao.configurazione;

import java.text.MessageFormat;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.anagrafica.AnagraficaManager;
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
import it.govpay.core.exceptions.ValidationException;
import it.govpay.model.PatchOp;
import it.govpay.model.configurazione.AppIOBatch;
import it.govpay.model.configurazione.AvvisaturaViaAppIo;
import it.govpay.model.configurazione.AvvisaturaViaMail;
import it.govpay.model.configurazione.Giornale;
import it.govpay.model.configurazione.Hardening;
import it.govpay.model.configurazione.MailBatch;
import it.govpay.model.configurazione.TracciatoCsv;

public class ConfigurazioneDAO extends BaseDAO{
	
	public static final String PATH_GIORNALE_EVENTI = "/giornaleEventi";
	public static final String PATH_TRACCIATO_CSV = "/tracciatoCsv";
	public static final String PATH_HARDENING = "/hardening";
	public static final String PATH_MAIL_BATCH = "/mailBatch";
	public static final String PATH_APP_IO_BATCH = "/appIOBatch";
	public static final String PATH_AVVISATURA_MAIL = "/avvisaturaMail";
	public static final String PATH_AVVISATURA_APP_IO = "/avvisaturaAppIO";
	
	public ConfigurazioneDAO() {
		super();
	}
	
	public ConfigurazioneDAO(boolean useCacheData) {
		super(useCacheData);
	}
	
	public LeggiConfigurazioneDTOResponse getConfigurazione(LeggiConfigurazioneDTO leggiConfigurazioneDTO) throws ConfigurazioneNonTrovataException, NotAuthorizedException, ServiceException, NotAuthenticatedException {
//		BasicBD bd = null;

		try {
//			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			it.govpay.core.business.Configurazione configurazioneBD = new it.govpay.core.business.Configurazione();
			return new LeggiConfigurazioneDTOResponse(configurazioneBD.getConfigurazione());
		} finally {
//			if(bd != null)
//				bd.closeConnection();
		}
	}


	public PutConfigurazioneDTOResponse salvaConfigurazione(PutConfigurazioneDTO putConfigurazioneDTO) throws ConfigurazioneNonTrovataException, ServiceException, NotAuthorizedException, NotAuthenticatedException, UnprocessableEntityException {  
		PutConfigurazioneDTOResponse putConfigurazioneDTOResponse = new PutConfigurazioneDTOResponse();
//		BasicBD bd = null;

		try {
//			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			it.govpay.core.business.Configurazione configurazioneBD = new it.govpay.core.business.Configurazione();
			
			boolean created = false;
			// salvo l'intero oggetto in blocco
			Configurazione configurazione = putConfigurazioneDTO.getConfigurazione();
			
			// flag creazione o update
			putConfigurazioneDTOResponse.setCreated(created);
			configurazioneBD.salvaConfigurazione(configurazione);
			
			// elimino la entry in cache
			AnagraficaManager.removeFromCache(configurazione);
		} finally {
//			if(bd != null) 
//				bd.closeConnection();
		}
		return putConfigurazioneDTOResponse;
	}

	public LeggiConfigurazioneDTOResponse patchConfigurazione(PatchConfigurazioneDTO patchConfigurazioneDTO) throws ServiceException, ValidationException { 
//		BasicBD bd = null;

		try {
//			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			it.govpay.core.business.Configurazione configurazioneBD = new it.govpay.core.business.Configurazione();
			
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
				} else if(PATH_AVVISATURA_MAIL.equals(op.getPath())) {
					AvvisaturaViaMail avvisaturaMail = (AvvisaturaViaMail) op.getValue();
					configurazione.setAvvisaturaViaMail(avvisaturaMail);
				} else if(PATH_AVVISATURA_APP_IO.equals(op.getPath())) {
					AvvisaturaViaAppIo avvisaturaAppIo = (AvvisaturaViaAppIo) op.getValue();
					configurazione.setAvvisaturaViaAppIo(avvisaturaAppIo);
				} else if(PATH_APP_IO_BATCH.equals(op.getPath())) {
					AppIOBatch batchSpedizioneAppIo =  (AppIOBatch) op.getValue();
					configurazione.setBatchSpedizioneAppIo(batchSpedizioneAppIo);
				} else {
					throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.PATH_XX_NON_VALIDO, op.getPath()));
				}
			}
			
			configurazioneBD.salvaConfigurazione(configurazione);
			// elimino la entry in cache
			AnagraficaManager.removeFromCache(configurazione);
			return new LeggiConfigurazioneDTOResponse(configurazioneBD.getConfigurazione());
		} finally {
//			if(bd != null)
//				bd.closeConnection();
		}
	}
}
