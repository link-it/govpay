package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.json.ValidationException;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.Configurazione;
import it.govpay.backoffice.v1.beans.ConfigurazioneReCaptcha;
import it.govpay.backoffice.v1.beans.Hardening;
import it.govpay.backoffice.v1.beans.MailBatch;
import it.govpay.backoffice.v1.beans.MailTemplate;
import it.govpay.backoffice.v1.beans.Mailserver;
import it.govpay.backoffice.v1.beans.TracciatoCsv;
import it.govpay.backoffice.v1.beans.TracciatoCsv.TipoEnum;
import it.govpay.core.dao.anagrafica.utils.UtenzaPatchUtils;
import it.govpay.core.dao.configurazione.ConfigurazioneDAO;
import it.govpay.core.dao.configurazione.dto.PutConfigurazioneDTO;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.PatchOp;

public class ConfigurazioniConverter {
	
	public static final String PATH_GIORNALE_EVENTI = ConfigurazioneDAO.PATH_GIORNALE_EVENTI;
	public static final String PATH_TRACCIATO_CSV = ConfigurazioneDAO.PATH_TRACCIATO_CSV;
	public static final String PATH_HARDENING = ConfigurazioneDAO.PATH_HARDENING;
	public static final String PATH_MAIL_BATCH = ConfigurazioneDAO.PATH_MAIL_BATCH;
	public static final String PATH_MAIL_PROMEMORIA = ConfigurazioneDAO.PATH_MAIL_PROMEMORIA;
	public static final String PATH_MAIL_RICEVUTA = ConfigurazioneDAO.PATH_MAIL_RICEVUTA;

	public static PutConfigurazioneDTO getPutConfigurazioneDTO(Configurazione configurazionePost, Authentication user) throws ServiceException,NotAuthorizedException, ValidationException {
		PutConfigurazioneDTO putConfigurazioneDTO = new PutConfigurazioneDTO(user);

		it.govpay.bd.model.Configurazione configurazione = new it.govpay.bd.model.Configurazione();
		if(configurazionePost.getGiornaleEventi() != null)
			configurazione.setGiornale(GiornaleConverter.getGiornaleDTO(configurazionePost.getGiornaleEventi()));
		if(configurazionePost.getTracciatoCsv() != null)
			configurazione.setTracciatoCsv(getTracciatoCsvDTO(configurazionePost.getTracciatoCsv()));
		if(configurazionePost.getHardening() != null)
			configurazione.setHardening(getConfigurazioneHardeningDTO(configurazionePost.getHardening()));
		if(configurazionePost.getMailBatch() != null)
			configurazione.setBatchSpedizioneEmail(getConfigurazioneMailBatchDTO(configurazionePost.getMailBatch()));
		if(configurazionePost.getMailPromemoria() != null)
			configurazione.setPromemoriaEmail(getConfigurazioneMailPromemoriaDTO(configurazionePost.getMailPromemoria()));
		if(configurazionePost.getMailRicevuta() != null)
			configurazione.setRicevutaEmail(getConfigurazioneMailRicevutaDTO(configurazionePost.getMailRicevuta()));

		putConfigurazioneDTO.setConfigurazione(configurazione );

		return putConfigurazioneDTO;
	}
	public static Configurazione toRsModel(it.govpay.bd.model.Configurazione configurazione) throws ServiceException {
		Configurazione rsModel = new Configurazione();

		if(configurazione.getGiornale() != null) {
			rsModel.setGiornaleEventi(GiornaleConverter.toRsModel(configurazione.getGiornale()));
		}
		if(configurazione.getTracciatoCsv() != null) {
			rsModel.setTracciatoCsv(toTracciatoRsModel(configurazione.getTracciatoCsv()));
		}
		if(configurazione.getHardening() != null) {
			rsModel.setHardening(toConfigurazioneHardeningRsModel(configurazione.getHardening()));
		}
		if(configurazione.getBatchSpedizioneEmail() != null) {
			rsModel.setMailBatch(toConfigurazioneMailBatchRsModel(configurazione.getBatchSpedizioneEmail()));
		}
		if(configurazione.getPromemoriaMail() != null) {
			rsModel.setMailPromemoria(toConfigurazioneMailPromemoriaRsModel(configurazione.getPromemoriaMail()));
		}
		if(configurazione.getRicevutaMail() != null) {
			rsModel.setMailRicevuta(toConfigurazioneMailRicevutaRsModel(configurazione.getRicevutaMail()));
		}
		

		return rsModel;
	}

	private static it.govpay.bd.configurazione.model.TracciatoCsv getTracciatoCsvDTO(TracciatoCsv tracciatoCsv) throws ServiceException, ValidationException {
		it.govpay.bd.configurazione.model.TracciatoCsv dto = new it.govpay.bd.configurazione.model.TracciatoCsv();

		dto.setTipo(tracciatoCsv.getTipo());
		
		if(tracciatoCsv.getTipo() != null) {
			// valore tipo contabilita non valido
			if(TipoEnum.fromValue(tracciatoCsv.getTipo()) == null) {
				throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" + tracciatoCsv.getTipo() + "] valori possibili " + ArrayUtils.toString(TipoEnum.values()));
			}
		}
		
		dto.setHeaderRisposta(tracciatoCsv.getIntestazione());
		dto.setTrasformazioneRichiesta((ConverterUtils.toJSON(tracciatoCsv.getRichiesta(),null)));
		dto.setTrasformazioneRisposta(ConverterUtils.toJSON(tracciatoCsv.getRisposta(),null));

		return dto;
	}
	
	private static it.govpay.bd.configurazione.model.TracciatoCsv getTracciatoCsvDTOPatch(TracciatoCsv tracciatoCsv) throws ServiceException, ValidationException {
		it.govpay.bd.configurazione.model.TracciatoCsv dto = new it.govpay.bd.configurazione.model.TracciatoCsv();

		dto.setTipo(tracciatoCsv.getTipo());
		
		if(tracciatoCsv.getTipo() != null) {
			// valore tipo contabilita non valido
			if(TipoEnum.fromValue(tracciatoCsv.getTipo()) == null) {
				throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" + tracciatoCsv.getTipo() + "] valori possibili " + ArrayUtils.toString(TipoEnum.values()));
			}
		}
		
		dto.setHeaderRisposta(tracciatoCsv.getIntestazione());
//		if(tracciatoCsv.getRichiesta() != null && tracciatoCsv.getRichiesta() instanceof String)
//			dto.setTrasformazioneRichiesta((String) tracciatoCsv.getRichiesta());
//		else 
			dto.setTrasformazioneRichiesta((ConverterUtils.toJSON(tracciatoCsv.getRichiesta(),null)));
		
//		if(tracciatoCsv.getRisposta() != null && tracciatoCsv.getRisposta() instanceof String)
//			dto.setTrasformazioneRisposta((String) tracciatoCsv.getRisposta());
//		else 
			dto.setTrasformazioneRisposta(ConverterUtils.toJSON(tracciatoCsv.getRisposta(),null));

		return dto;
	}

	private static TracciatoCsv toTracciatoRsModel(it.govpay.bd.configurazione.model.TracciatoCsv tracciatoCsv) { 
		TracciatoCsv rsModel = new TracciatoCsv();

		rsModel.setTipo(tracciatoCsv.getTipo());
		rsModel.setIntestazione(tracciatoCsv.getHeaderRisposta());
		rsModel.setRichiesta(new RawObject(tracciatoCsv.getTrasformazioneRichiesta()));
		rsModel.setRisposta(new RawObject(tracciatoCsv.getTrasformazioneRisposta()));

		return rsModel;
	}

	public static List<PatchOp> toModel(List<it.govpay.backoffice.v1.beans.PatchOp> lstOp) throws ValidationException, ServiceException {
		List<PatchOp> list = new ArrayList<>();
		for(it.govpay.backoffice.v1.beans.PatchOp op : lstOp) {
			PatchOp e = new PatchOp();
			e.setOp(PatchOp.OpEnum.fromValue(op.getOp().name()));
			e.setPath(op.getPath());
			
			if(PATH_GIORNALE_EVENTI.equals(op.getPath())) {
				it.govpay.backoffice.v1.beans.Giornale giornalePost = it.govpay.backoffice.v1.beans.Giornale.parse(ConverterUtils.toJSON(op.getValue(),null));
				giornalePost.validate();
				e.setValue(GiornaleConverter.getGiornaleDTO(giornalePost ));
			} else if(PATH_TRACCIATO_CSV.equals(op.getPath())) {
				TracciatoCsv tracciatoCsv = TracciatoCsv.parse(ConverterUtils.toJSON(op.getValue(),null));
				tracciatoCsv.validate();
				e.setValue(getTracciatoCsvDTOPatch(tracciatoCsv ));
			} else if(PATH_HARDENING.equals(op.getPath())) {
				Hardening configurazioneHardening = Hardening.parse(ConverterUtils.toJSON(op.getValue(),null));
				configurazioneHardening.validate();
				e.setValue(getConfigurazioneHardeningDTO(configurazioneHardening ));
			} else if(PATH_MAIL_BATCH.equals(op.getPath())) {
				MailBatch configurazioneMailBatch = MailBatch.parse(ConverterUtils.toJSON(op.getValue(),null));
				configurazioneMailBatch.validate();
				e.setValue(getConfigurazioneMailBatchDTO(configurazioneMailBatch));
			} else if(PATH_MAIL_PROMEMORIA.equals(op.getPath())) {
				MailTemplate configurazioneMailPromemoria = MailTemplate.parse(ConverterUtils.toJSON(op.getValue(),null));
				configurazioneMailPromemoria.validate();
				e.setValue(getConfigurazioneMailPromemoriaDTOPatch(configurazioneMailPromemoria ));
			} else if(PATH_MAIL_RICEVUTA.equals(op.getPath())) {
				MailTemplate configurazioneMailRicevuta = MailTemplate.parse(ConverterUtils.toJSON(op.getValue(),null));
				configurazioneMailRicevuta.validate();
				e.setValue(getConfigurazioneMailRicevutaDTOPatch(configurazioneMailRicevuta ));
			} else {
				throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.PATH_XX_NON_VALIDO, op.getPath()));
			}
			
			list.add(e);
		}
		return list;
	}

	private static it.govpay.bd.configurazione.model.Hardening getConfigurazioneHardeningDTO(Hardening configurazioneHardening) {
		it.govpay.bd.configurazione.model.Hardening dto = new it.govpay.bd.configurazione.model.Hardening();
		
		dto.setAbilitato(configurazioneHardening.Abilitato());
		if(configurazioneHardening.getCaptcha() != null) {
			dto.setGoogleCatpcha(new it.govpay.bd.configurazione.model.GoogleCaptcha());
			dto.getGoogleCatpcha().setResponseParameter(configurazioneHardening.getCaptcha().getParametro());
			dto.getGoogleCatpcha().setSecretKey(configurazioneHardening.getCaptcha().getSecretKey());
			dto.getGoogleCatpcha().setServerURL(configurazioneHardening.getCaptcha().getServerURL());
			dto.getGoogleCatpcha().setSiteKey(configurazioneHardening.getCaptcha().getSiteKey());
			dto.getGoogleCatpcha().setSoglia(configurazioneHardening.getCaptcha().getSoglia().doubleValue());
			dto.getGoogleCatpcha().setDenyOnFail(configurazioneHardening.getCaptcha().DenyOnFail());
			dto.getGoogleCatpcha().setConnectionTimeout(configurazioneHardening.getCaptcha().getConnectionTimeout().intValue());
			dto.getGoogleCatpcha().setReadTimeout(configurazioneHardening.getCaptcha().getReadTimeout().intValue());
		}
		
		return dto;
	}
	
	private static Hardening toConfigurazioneHardeningRsModel(it.govpay.bd.configurazione.model.Hardening configurazioneHardening) {
		Hardening rsModel = new Hardening();
		rsModel.setAbilitato(configurazioneHardening.isAbilitato());
		ConfigurazioneReCaptcha captchaRsModel = null;;
		
		if(configurazioneHardening.getGoogleCatpcha() != null) {
			captchaRsModel = new ConfigurazioneReCaptcha();
			
			captchaRsModel.setParametro(configurazioneHardening.getGoogleCatpcha().getResponseParameter());
			captchaRsModel.setSecretKey(configurazioneHardening.getGoogleCatpcha().getSecretKey());
			captchaRsModel.setServerURL(configurazioneHardening.getGoogleCatpcha().getServerURL());
			captchaRsModel.setSiteKey(configurazioneHardening.getGoogleCatpcha().getSiteKey());
			captchaRsModel.setSoglia(new BigDecimal(configurazioneHardening.getGoogleCatpcha().getSoglia()));
			captchaRsModel.setDenyOnFail(configurazioneHardening.getGoogleCatpcha().isDenyOnFail());
			captchaRsModel.setConnectionTimeout(new BigDecimal(configurazioneHardening.getGoogleCatpcha().getConnectionTimeout()));
			captchaRsModel.setReadTimeout(new BigDecimal(configurazioneHardening.getGoogleCatpcha().getReadTimeout()));
		}
		rsModel.setCaptcha(captchaRsModel);

		return rsModel;
	}
	
	private static it.govpay.bd.configurazione.model.Mail getConfigurazioneMailRicevutaDTO(MailTemplate mailRicevuta) throws ServiceException, ValidationException{
		it.govpay.bd.configurazione.model.Mail dto = new it.govpay.bd.configurazione.model.Mail();
		
		dto.setAllegaPdf(mailRicevuta.AllegaPdf());
		dto.setTipo(mailRicevuta.getTipo());
		if(mailRicevuta.getTipo() != null) {
			// valore tipo contabilita non valido
			if(it.govpay.backoffice.v1.beans.MailTemplate.TipoEnum.fromValue(mailRicevuta.getTipo()) == null) {
				throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
						mailRicevuta.getTipo() + "] valori possibili " + ArrayUtils.toString(it.govpay.backoffice.v1.beans.MailTemplate.TipoEnum.values()));
			}
		}
		dto.setMessaggio((ConverterUtils.toJSON(mailRicevuta.getMessaggio(),null)));
		dto.setOggetto(ConverterUtils.toJSON(mailRicevuta.getOggetto(),null));
		
		return dto;
	}
	
	private static it.govpay.bd.configurazione.model.Mail getConfigurazioneMailRicevutaDTOPatch(MailTemplate mailRicevuta) throws ServiceException, ValidationException{
		it.govpay.bd.configurazione.model.Mail dto = new it.govpay.bd.configurazione.model.Mail();
		
		dto.setAllegaPdf(mailRicevuta.AllegaPdf());
		dto.setTipo(mailRicevuta.getTipo());
		
		if(mailRicevuta.getTipo() != null) {
			// valore tipo contabilita non valido
			if(it.govpay.backoffice.v1.beans.MailTemplate.TipoEnum.fromValue(mailRicevuta.getTipo()) == null) {
				throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
						mailRicevuta.getTipo() + "] valori possibili " + ArrayUtils.toString(it.govpay.backoffice.v1.beans.MailTemplate.TipoEnum.values()));
			}
		}
		
//		if(mailRicevuta.getMessaggio() != null && mailRicevuta.getMessaggio() instanceof String)
//			dto.setMessaggio((String) mailRicevuta.getMessaggio());
//		else 
			dto.setMessaggio((ConverterUtils.toJSON(mailRicevuta.getMessaggio(),null)));
//		if(mailRicevuta.getOggetto() != null && mailRicevuta.getOggetto() instanceof String)
//			dto.setOggetto((String) mailRicevuta.getOggetto());
//		else 
			dto.setOggetto((ConverterUtils.toJSON(mailRicevuta.getOggetto(),null)));
		
		return dto;
	}
	
	private static MailTemplate toConfigurazioneMailRicevutaRsModel(it.govpay.bd.configurazione.model.Mail ricevutaMail) {
		MailTemplate rsModel = new MailTemplate();
		
		rsModel.setTipo(ricevutaMail.getTipo());
		rsModel.setAllegaPdf(ricevutaMail.isAllegaPdf());
		rsModel.setMessaggio(new RawObject(ricevutaMail.getMessaggio()));
		rsModel.setOggetto(new RawObject(ricevutaMail.getOggetto()));
		
		return rsModel;
	}

	private static it.govpay.bd.configurazione.model.Mail getConfigurazioneMailPromemoriaDTO(MailTemplate mailPromemoria) throws ServiceException, ValidationException{
		it.govpay.bd.configurazione.model.Mail dto = new it.govpay.bd.configurazione.model.Mail();
		
		dto.setAllegaPdf(mailPromemoria.AllegaPdf());
		dto.setTipo(mailPromemoria.getTipo());
		if(mailPromemoria.getTipo() != null) {
			// valore tipo contabilita non valido
			if(it.govpay.backoffice.v1.beans.MailTemplate.TipoEnum.fromValue(mailPromemoria.getTipo()) == null) {
				throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
						mailPromemoria.getTipo() + "] valori possibili " + ArrayUtils.toString(it.govpay.backoffice.v1.beans.MailTemplate.TipoEnum.values()));
			}
		}
		
		dto.setMessaggio((ConverterUtils.toJSON(mailPromemoria.getMessaggio(),null)));
		dto.setOggetto(ConverterUtils.toJSON(mailPromemoria.getOggetto(),null));
		
		return dto;
	}

	private static it.govpay.bd.configurazione.model.Mail getConfigurazioneMailPromemoriaDTOPatch(MailTemplate mailPromemoria) throws ServiceException, ValidationException{
		it.govpay.bd.configurazione.model.Mail dto = new it.govpay.bd.configurazione.model.Mail();
		
		dto.setAllegaPdf(mailPromemoria.AllegaPdf());
		dto.setTipo(mailPromemoria.getTipo());
		if(mailPromemoria.getTipo() != null) {
			// valore tipo contabilita non valido
			if(it.govpay.backoffice.v1.beans.MailTemplate.TipoEnum.fromValue(mailPromemoria.getTipo()) == null) {
				throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
						mailPromemoria.getTipo() + "] valori possibili " + ArrayUtils.toString(it.govpay.backoffice.v1.beans.MailTemplate.TipoEnum.values()));
			}
		}
		
//		if(mailPromemoria.getMessaggio() != null && mailPromemoria.getMessaggio() instanceof String)
//			dto.setMessaggio((String) mailPromemoria.getMessaggio());
//		else 
			dto.setMessaggio((ConverterUtils.toJSON(mailPromemoria.getMessaggio(),null)));
//		if(mailPromemoria.getOggetto() != null && mailPromemoria.getOggetto() instanceof String)
//			dto.setOggetto((String) mailPromemoria.getOggetto());
//		else 
			dto.setOggetto((ConverterUtils.toJSON(mailPromemoria.getOggetto(),null)));
		
		return dto;
	}
	
	private static MailTemplate toConfigurazioneMailPromemoriaRsModel(it.govpay.bd.configurazione.model.Mail promemoriaMail) {
		MailTemplate rsModel = new MailTemplate();
		
		rsModel.setTipo(promemoriaMail.getTipo());
		rsModel.setAllegaPdf(promemoriaMail.isAllegaPdf());
		rsModel.setMessaggio(new RawObject(promemoriaMail.getMessaggio()));
		rsModel.setOggetto(new RawObject(promemoriaMail.getOggetto()));
		
		return rsModel;
	}
	

	private static it.govpay.bd.configurazione.model.MailBatch getConfigurazioneMailBatchDTO(MailBatch mailBatch) {
		it.govpay.bd.configurazione.model.MailBatch dto = new it.govpay.bd.configurazione.model.MailBatch();
		
		dto.setAbilitato(mailBatch.Abilitato());
		it.govpay.bd.configurazione.model.MailServer mailServerDTO = null;;
		
		if(mailBatch.getMailserver() != null) {
			mailServerDTO = new it.govpay.bd.configurazione.model.MailServer();
			
			mailServerDTO.setHost(mailBatch.getMailserver().getHost());
			mailServerDTO.setPort(mailBatch.getMailserver().getPort().intValue());
			mailServerDTO.setUsername(mailBatch.getMailserver().getUsername());
			mailServerDTO.setPassword(mailBatch.getMailserver().getPassword());
			mailServerDTO.setFrom(mailBatch.getMailserver().getFrom());
			mailServerDTO.setConnectionTimeout(mailBatch.getMailserver().getConnectionTimeout().intValue());
			mailServerDTO.setReadTimeout(mailBatch.getMailserver().getReadTimeout().intValue());
		}
		dto.setMailserver(mailServerDTO);
		
		return dto;
	}

	private static MailBatch toConfigurazioneMailBatchRsModel(it.govpay.bd.configurazione.model.MailBatch batchSpedizioneEmail) {
		MailBatch rsModel = new MailBatch();
		
		rsModel.setAbilitato(batchSpedizioneEmail.isAbilitato());
		Mailserver mailServerRsModel = null;;
		
		if(batchSpedizioneEmail.getMailserver() != null) {
			mailServerRsModel = new Mailserver();
			
			mailServerRsModel.setHost(batchSpedizioneEmail.getMailserver().getHost());
			mailServerRsModel.setPort(new BigDecimal(batchSpedizioneEmail.getMailserver().getPort()));
			mailServerRsModel.setUsername(batchSpedizioneEmail.getMailserver().getUsername());
			mailServerRsModel.setPassword(batchSpedizioneEmail.getMailserver().getPassword());
			mailServerRsModel.setFrom(batchSpedizioneEmail.getMailserver().getFrom());
			mailServerRsModel.setConnectionTimeout(new BigDecimal(batchSpedizioneEmail.getMailserver().getConnectionTimeout()));
			mailServerRsModel.setReadTimeout(new BigDecimal(batchSpedizioneEmail.getMailserver().getReadTimeout()));
		}
		rsModel.setMailserver(mailServerRsModel);
		
		
		return rsModel;
	}
}
