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

import it.govpay.backoffice.v1.beans.AppIOBatch;
import it.govpay.backoffice.v1.beans.Configurazione;
import it.govpay.backoffice.v1.beans.ConfigurazioneAvvisaturaAppIO;
import it.govpay.backoffice.v1.beans.ConfigurazioneAvvisaturaMail;
import it.govpay.backoffice.v1.beans.ConfigurazioneReCaptcha;
import it.govpay.backoffice.v1.beans.Hardening;
import it.govpay.backoffice.v1.beans.Keystore;
import it.govpay.backoffice.v1.beans.MailBatch;
import it.govpay.backoffice.v1.beans.Mailserver;
import it.govpay.backoffice.v1.beans.SslConfig;
import it.govpay.backoffice.v1.beans.TemplateMailPromemoriaAvviso;
import it.govpay.backoffice.v1.beans.TemplateMailPromemoriaRicevuta;
import it.govpay.backoffice.v1.beans.TemplatePromemoriaAvvisoBase;
import it.govpay.backoffice.v1.beans.TemplatePromemoriaRicevutaBase;
import it.govpay.backoffice.v1.beans.TemplatePromemoriaScadenza;
import it.govpay.backoffice.v1.beans.TipoTemplateTrasformazione;
import it.govpay.backoffice.v1.beans.TracciatoCsv;
import it.govpay.bd.configurazione.model.KeyStore;
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
	public static final String PATH_APP_IO_BATCH = ConfigurazioneDAO.PATH_APP_IO_BATCH;
	public static final String PATH_AVVISATURA_MAIL = ConfigurazioneDAO.PATH_AVVISATURA_MAIL;
	public static final String PATH_AVVISATURA_APP_IO = ConfigurazioneDAO.PATH_AVVISATURA_APP_IO;

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
		if(configurazionePost.getAvvisaturaMail() != null)
			configurazione.setAvvisaturaViaMail(getConfigurazioneAvvisaturaMailDTO(configurazionePost.getAvvisaturaMail()));
		if(configurazionePost.getAvvisaturaAppIO() != null)
			configurazione.setAvvisaturaViaAppIo(getConfigurazioneAvvisaturaAppIoDTO(configurazionePost.getAvvisaturaAppIO()));
		if(configurazionePost.getAppIOBatch() != null)
			configurazione.setBatchSpedizioneAppIo(getConfigurazioneAppIOBatchDTO(configurazionePost.getAppIOBatch()));

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
		if(configurazione.getBatchSpedizioneAppIo() != null) {
			rsModel.setAppIOBatch(toConfigurazioneAppIOBatchRsModel(configurazione.getBatchSpedizioneAppIo()));
		}
		if(configurazione.getBatchSpedizioneAppIo() != null) {
			rsModel.setAppIOBatch(toConfigurazioneAppIOBatchRsModel(configurazione.getBatchSpedizioneAppIo()));
		}
		if(configurazione.getAvvisaturaViaMail() != null) {
			rsModel.setAvvisaturaMail(toConfigurazioneAvvisaturaMailRsModel(configurazione.getAvvisaturaViaMail()));
		}
		if(configurazione.getAvvisaturaViaAppIo() != null) {
			rsModel.setAvvisaturaAppIO(toConfigurazioneAvvisaturaAppIoRsModel(configurazione.getAvvisaturaViaAppIo()));
		}
		if(configurazione.getBatchSpedizioneAppIo() != null) {
			rsModel.setAppIOBatch(toConfigurazioneAppIOBatchRsModel(configurazione.getBatchSpedizioneAppIo()));
		}

		return rsModel;
	}

	private static it.govpay.bd.configurazione.model.TracciatoCsv getTracciatoCsvDTO(TracciatoCsv tracciatoCsv) throws ServiceException, ValidationException {
		it.govpay.bd.configurazione.model.TracciatoCsv dto = new it.govpay.bd.configurazione.model.TracciatoCsv();

		dto.setTipo(tracciatoCsv.getTipo());

		if(tracciatoCsv.getTipo() != null) {
			// valore tipo contabilita non valido
			if(TipoTemplateTrasformazione.fromValue(tracciatoCsv.getTipo()) == null) {
				throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" + tracciatoCsv.getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
			}
		}

		dto.setIntestazione(tracciatoCsv.getIntestazione());
		dto.setRichiesta((ConverterUtils.toJSON(tracciatoCsv.getRichiesta(),null)));
		dto.setRisposta(ConverterUtils.toJSON(tracciatoCsv.getRisposta(),null));

		return dto;
	}

	private static it.govpay.bd.configurazione.model.TracciatoCsv getTracciatoCsvDTOPatch(TracciatoCsv tracciatoCsv) throws ServiceException, ValidationException {
		it.govpay.bd.configurazione.model.TracciatoCsv dto = new it.govpay.bd.configurazione.model.TracciatoCsv();

		dto.setTipo(tracciatoCsv.getTipo());

		if(tracciatoCsv.getTipo() != null) {
			// valore tipo contabilita non valido
			if(TipoTemplateTrasformazione.fromValue(tracciatoCsv.getTipo()) == null) {
				throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" + tracciatoCsv.getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
			}
		}

		dto.setIntestazione(tracciatoCsv.getIntestazione());
		dto.setRichiesta((ConverterUtils.toJSON(tracciatoCsv.getRichiesta(),null)));
		dto.setRisposta(ConverterUtils.toJSON(tracciatoCsv.getRisposta(),null));

		return dto;
	}

	private static TracciatoCsv toTracciatoRsModel(it.govpay.bd.configurazione.model.TracciatoCsv tracciatoCsv) { 
		TracciatoCsv rsModel = new TracciatoCsv();

		rsModel.setTipo(tracciatoCsv.getTipo());
		rsModel.setIntestazione(tracciatoCsv.getIntestazione());
		rsModel.setRichiesta(new RawObject(tracciatoCsv.getRichiesta()));
		rsModel.setRisposta(new RawObject(tracciatoCsv.getRisposta()));

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
			} else if(PATH_AVVISATURA_MAIL.equals(op.getPath())) {
				ConfigurazioneAvvisaturaMail configurazioneAvvisaturaMail = ConfigurazioneAvvisaturaMail.parse(ConverterUtils.toJSON(op.getValue(),null));
				configurazioneAvvisaturaMail.validate();
				e.setValue(getConfigurazioneAvvisaturaMailDTO(configurazioneAvvisaturaMail));
			} else if(PATH_AVVISATURA_APP_IO.equals(op.getPath())) {
				ConfigurazioneAvvisaturaAppIO configurazioneAvvisaturaAppIo = ConfigurazioneAvvisaturaAppIO.parse(ConverterUtils.toJSON(op.getValue(),null));
				configurazioneAvvisaturaAppIo.validate();
				e.setValue(getConfigurazioneAvvisaturaAppIoDTO(configurazioneAvvisaturaAppIo));
			} else if(PATH_APP_IO_BATCH.equals(op.getPath())) {
				AppIOBatch configurazioneAppIO = AppIOBatch.parse(ConverterUtils.toJSON(op.getValue(),null));
				configurazioneAppIO.validate();
				e.setValue(getConfigurazioneAppIOBatchDTO(configurazioneAppIO	));
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

	private static it.govpay.bd.configurazione.model.AvvisaturaViaMail getConfigurazioneAvvisaturaMailDTO(ConfigurazioneAvvisaturaMail avvisaturaMail) throws ServiceException, ValidationException{
		it.govpay.bd.configurazione.model.AvvisaturaViaMail dto = new it.govpay.bd.configurazione.model.AvvisaturaViaMail();

		if(avvisaturaMail.getPromemoriaAvviso() != null) {
			it.govpay.bd.configurazione.model.PromemoriaAvviso promemoriaAvviso = new it.govpay.bd.configurazione.model.PromemoriaAvviso();

			promemoriaAvviso.setAllegaPdf(avvisaturaMail.getPromemoriaAvviso().AllegaPdf());
			promemoriaAvviso.setTipo(avvisaturaMail.getPromemoriaAvviso().getTipo());
			if(avvisaturaMail.getPromemoriaAvviso().getTipo() != null) {
				// valore tipo contabilita non valido
				if(TipoTemplateTrasformazione.fromValue(avvisaturaMail.getPromemoriaAvviso().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
							avvisaturaMail.getPromemoriaAvviso().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
				}
			}
			promemoriaAvviso.setMessaggio((ConverterUtils.toJSON(avvisaturaMail.getPromemoriaAvviso().getMessaggio(),null)));
			promemoriaAvviso.setOggetto(ConverterUtils.toJSON(avvisaturaMail.getPromemoriaAvviso().getOggetto(),null));		

			dto.setPromemoriaAvviso(promemoriaAvviso);
		}

		if(avvisaturaMail.getPromemoriaRicevuta() != null) {
			it.govpay.bd.configurazione.model.PromemoriaRicevuta promemoriaRicevuta = new it.govpay.bd.configurazione.model.PromemoriaRicevuta();
			
			promemoriaRicevuta.setSoloEseguiti(avvisaturaMail.getPromemoriaRicevuta().SoloEseguiti());
			promemoriaRicevuta.setAllegaPdf(avvisaturaMail.getPromemoriaRicevuta().AllegaPdf());
			promemoriaRicevuta.setTipo(avvisaturaMail.getPromemoriaRicevuta().getTipo());
			if(avvisaturaMail.getPromemoriaRicevuta().getTipo() != null) {
				// valore tipo contabilita non valido
				if(TipoTemplateTrasformazione.fromValue(avvisaturaMail.getPromemoriaRicevuta().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
							avvisaturaMail.getPromemoriaRicevuta().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
				}
			}
			promemoriaRicevuta.setMessaggio((ConverterUtils.toJSON(avvisaturaMail.getPromemoriaRicevuta().getMessaggio(),null)));
			promemoriaRicevuta.setOggetto(ConverterUtils.toJSON(avvisaturaMail.getPromemoriaRicevuta().getOggetto(),null));		
			
			dto.setPromemoriaRicevuta(promemoriaRicevuta);
		}

		if(avvisaturaMail.getPromemoriaScadenza() != null) {
			it.govpay.bd.configurazione.model.PromemoriaScadenza promemoriaScadenza = new it.govpay.bd.configurazione.model.PromemoriaScadenza();
			
			if(avvisaturaMail.getPromemoriaScadenza().getPreavviso() != null)
			promemoriaScadenza.setPreavviso(avvisaturaMail.getPromemoriaScadenza().getPreavviso().intValue());
			promemoriaScadenza.setTipo(avvisaturaMail.getPromemoriaScadenza().getTipo());
			if(avvisaturaMail.getPromemoriaScadenza().getTipo() != null) {
				// valore tipo contabilita non valido
				if(TipoTemplateTrasformazione.fromValue(avvisaturaMail.getPromemoriaScadenza().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
							avvisaturaMail.getPromemoriaScadenza().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
				}
			}
			promemoriaScadenza.setMessaggio((ConverterUtils.toJSON(avvisaturaMail.getPromemoriaScadenza().getMessaggio(),null)));
			promemoriaScadenza.setOggetto(ConverterUtils.toJSON(avvisaturaMail.getPromemoriaScadenza().getOggetto(),null));		
			
			dto.setPromemoriaScadenza(promemoriaScadenza);
		}


		return dto;
	}
	
	
	private static it.govpay.bd.configurazione.model.AvvisaturaViaAppIo getConfigurazioneAvvisaturaAppIoDTO(ConfigurazioneAvvisaturaAppIO avvisaturaAppIo) throws ServiceException, ValidationException{
		it.govpay.bd.configurazione.model.AvvisaturaViaAppIo dto = new it.govpay.bd.configurazione.model.AvvisaturaViaAppIo();

		if(avvisaturaAppIo.getPromemoriaAvviso() != null) {
			it.govpay.bd.configurazione.model.PromemoriaAvvisoBase promemoriaAvviso = new it.govpay.bd.configurazione.model.PromemoriaAvvisoBase();

			promemoriaAvviso.setTipo(avvisaturaAppIo.getPromemoriaAvviso().getTipo());
			if(avvisaturaAppIo.getPromemoriaAvviso().getTipo() != null) {
				// valore tipo contabilita non valido
				if(TipoTemplateTrasformazione.fromValue(avvisaturaAppIo.getPromemoriaAvviso().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
							avvisaturaAppIo.getPromemoriaAvviso().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
				}
			}
			promemoriaAvviso.setMessaggio((ConverterUtils.toJSON(avvisaturaAppIo.getPromemoriaAvviso().getMessaggio(),null)));
			promemoriaAvviso.setOggetto(ConverterUtils.toJSON(avvisaturaAppIo.getPromemoriaAvviso().getOggetto(),null));		

			dto.setPromemoriaAvviso(promemoriaAvviso);
		}

		if(avvisaturaAppIo.getPromemoriaRicevuta() != null) {
			it.govpay.bd.configurazione.model.PromemoriaRicevutaBase promemoriaRicevuta = new it.govpay.bd.configurazione.model.PromemoriaRicevutaBase();
			
			promemoriaRicevuta.setSoloEseguiti(avvisaturaAppIo.getPromemoriaRicevuta().SoloEseguiti());
			promemoriaRicevuta.setTipo(avvisaturaAppIo.getPromemoriaRicevuta().getTipo());
			if(avvisaturaAppIo.getPromemoriaRicevuta().getTipo() != null) {
				// valore tipo contabilita non valido
				if(TipoTemplateTrasformazione.fromValue(avvisaturaAppIo.getPromemoriaRicevuta().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
							avvisaturaAppIo.getPromemoriaRicevuta().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
				}
			}
			promemoriaRicevuta.setMessaggio((ConverterUtils.toJSON(avvisaturaAppIo.getPromemoriaRicevuta().getMessaggio(),null)));
			promemoriaRicevuta.setOggetto(ConverterUtils.toJSON(avvisaturaAppIo.getPromemoriaRicevuta().getOggetto(),null));		
			
			dto.setPromemoriaRicevuta(promemoriaRicevuta);
		}

		if(avvisaturaAppIo.getPromemoriaScadenza() != null) {
			it.govpay.bd.configurazione.model.PromemoriaScadenza promemoriaScadenza = new it.govpay.bd.configurazione.model.PromemoriaScadenza();
			
			if(avvisaturaAppIo.getPromemoriaScadenza().getPreavviso() != null)
			promemoriaScadenza.setPreavviso(avvisaturaAppIo.getPromemoriaScadenza().getPreavviso().intValue());
			promemoriaScadenza.setTipo(avvisaturaAppIo.getPromemoriaScadenza().getTipo());
			if(avvisaturaAppIo.getPromemoriaScadenza().getTipo() != null) {
				// valore tipo contabilita non valido
				if(TipoTemplateTrasformazione.fromValue(avvisaturaAppIo.getPromemoriaScadenza().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
							avvisaturaAppIo.getPromemoriaScadenza().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
				}
			}
			promemoriaScadenza.setMessaggio((ConverterUtils.toJSON(avvisaturaAppIo.getPromemoriaScadenza().getMessaggio(),null)));
			promemoriaScadenza.setOggetto(ConverterUtils.toJSON(avvisaturaAppIo.getPromemoriaScadenza().getOggetto(),null));		
			
			dto.setPromemoriaScadenza(promemoriaScadenza);
		}


		return dto;
	}

	private static ConfigurazioneAvvisaturaMail toConfigurazioneAvvisaturaMailRsModel(it.govpay.bd.configurazione.model.AvvisaturaViaMail avvisaturaMail) {
		ConfigurazioneAvvisaturaMail rsModel = new ConfigurazioneAvvisaturaMail();
		
		if(avvisaturaMail.getPromemoriaAvviso() != null) {
			TemplateMailPromemoriaAvviso promemoriaAvviso = new TemplateMailPromemoriaAvviso();
			
			promemoriaAvviso.setAllegaPdf(avvisaturaMail.getPromemoriaAvviso().isAllegaPdf());
			promemoriaAvviso.setTipo(avvisaturaMail.getPromemoriaAvviso().getTipo());
			promemoriaAvviso.setMessaggio(new RawObject(avvisaturaMail.getPromemoriaAvviso().getMessaggio()));
			promemoriaAvviso.setOggetto(new RawObject(avvisaturaMail.getPromemoriaAvviso().getOggetto()));
			
			rsModel.setPromemoriaAvviso(promemoriaAvviso);
		}
		
		if(avvisaturaMail.getPromemoriaRicevuta() != null) {
			TemplateMailPromemoriaRicevuta promemoriaRicevuta = new TemplateMailPromemoriaRicevuta();
			
			promemoriaRicevuta.setSoloEseguiti(avvisaturaMail.getPromemoriaRicevuta().isSoloEseguiti());
			promemoriaRicevuta.setAllegaPdf(avvisaturaMail.getPromemoriaRicevuta().isAllegaPdf());
			promemoriaRicevuta.setTipo(avvisaturaMail.getPromemoriaRicevuta().getTipo());
			promemoriaRicevuta.setMessaggio(new RawObject(avvisaturaMail.getPromemoriaRicevuta().getMessaggio()));
			promemoriaRicevuta.setOggetto(new RawObject(avvisaturaMail.getPromemoriaRicevuta().getOggetto()));
			
			rsModel.setPromemoriaRicevuta(promemoriaRicevuta);
		}
		
		if(avvisaturaMail.getPromemoriaScadenza() != null) {
			TemplatePromemoriaScadenza promemoriaScadenza = new TemplatePromemoriaScadenza();
			
			if(avvisaturaMail.getPromemoriaScadenza().getPreavviso() != null)
			promemoriaScadenza.setPreavviso(new BigDecimal(avvisaturaMail.getPromemoriaScadenza().getPreavviso()));
			promemoriaScadenza.setTipo(avvisaturaMail.getPromemoriaScadenza().getTipo());
			promemoriaScadenza.setMessaggio(new RawObject(avvisaturaMail.getPromemoriaScadenza().getMessaggio()));
			promemoriaScadenza.setOggetto(new RawObject(avvisaturaMail.getPromemoriaScadenza().getOggetto()));
			
			rsModel.setPromemoriaScadenza(promemoriaScadenza);
		}
		
		return rsModel;
	}
	
	private static ConfigurazioneAvvisaturaAppIO toConfigurazioneAvvisaturaAppIoRsModel(it.govpay.bd.configurazione.model.AvvisaturaViaAppIo avvisaturaMail) {
		ConfigurazioneAvvisaturaAppIO rsModel = new ConfigurazioneAvvisaturaAppIO();
		
		if(avvisaturaMail.getPromemoriaAvviso() != null) {
			TemplatePromemoriaAvvisoBase promemoriaAvviso = new TemplatePromemoriaAvvisoBase();
			
			promemoriaAvviso.setTipo(avvisaturaMail.getPromemoriaAvviso().getTipo());
			promemoriaAvviso.setMessaggio(new RawObject(avvisaturaMail.getPromemoriaAvviso().getMessaggio()));
			promemoriaAvviso.setOggetto(new RawObject(avvisaturaMail.getPromemoriaAvviso().getOggetto()));
			
			rsModel.setPromemoriaAvviso(promemoriaAvviso);
		}
		
		if(avvisaturaMail.getPromemoriaRicevuta() != null) {
			TemplatePromemoriaRicevutaBase promemoriaRicevuta = new TemplatePromemoriaRicevutaBase();
			
			promemoriaRicevuta.setSoloEseguiti(avvisaturaMail.getPromemoriaRicevuta().isSoloEseguiti());
			promemoriaRicevuta.setTipo(avvisaturaMail.getPromemoriaRicevuta().getTipo());
			promemoriaRicevuta.setMessaggio(new RawObject(avvisaturaMail.getPromemoriaRicevuta().getMessaggio()));
			promemoriaRicevuta.setOggetto(new RawObject(avvisaturaMail.getPromemoriaRicevuta().getOggetto()));
			
			rsModel.setPromemoriaRicevuta(promemoriaRicevuta);
		}
		
		if(avvisaturaMail.getPromemoriaScadenza() != null) {
			TemplatePromemoriaScadenza promemoriaScadenza = new TemplatePromemoriaScadenza();
			
			if(avvisaturaMail.getPromemoriaScadenza().getPreavviso() != null)
			promemoriaScadenza.setPreavviso(new BigDecimal(avvisaturaMail.getPromemoriaScadenza().getPreavviso()));
			promemoriaScadenza.setTipo(avvisaturaMail.getPromemoriaScadenza().getTipo());
			promemoriaScadenza.setMessaggio(new RawObject(avvisaturaMail.getPromemoriaScadenza().getMessaggio()));
			promemoriaScadenza.setOggetto(new RawObject(avvisaturaMail.getPromemoriaScadenza().getOggetto()));
			
			rsModel.setPromemoriaScadenza(promemoriaScadenza);
		}
		
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
			mailServerDTO.setSslConfig(getConfigurazioneSslConfigDTO(mailBatch.getMailserver().getSslConfig()));
		}
		dto.setMailserver(mailServerDTO);

		return dto;
	}
	
	private static it.govpay.bd.configurazione.model.SslConfig getConfigurazioneSslConfigDTO(SslConfig sslConfig) {
		it.govpay.bd.configurazione.model.SslConfig dto = new it.govpay.bd.configurazione.model.SslConfig();
		
		dto.setAbilitato(sslConfig.Abilitato());
		dto.setHostnameVerifier(false);
		if(sslConfig.Abilitato()) {
			if(sslConfig.HostnameVerifier() != null)
				dto.setHostnameVerifier(sslConfig.HostnameVerifier());
			dto.setType(sslConfig.getType());
			dto.setKeyStore(getConfigurazioneKeyStoreDTO(sslConfig.getKeystore()));
			dto.setTrustStore(getConfigurazioneKeyStoreDTO(sslConfig.getTruststore()));
		}
				
		return dto;	
	}

	private static it.govpay.bd.configurazione.model.KeyStore getConfigurazioneKeyStoreDTO(Keystore keystore) {
		
		it.govpay.bd.configurazione.model.KeyStore dto = null;
		
		if(keystore != null) {
			dto = new it.govpay.bd.configurazione.model.KeyStore();
			
			dto.setLocation(keystore.getLocation());
			dto.setManagementAlgorithm(keystore.getManagementAlgorithm());
			dto.setPassword(keystore.getPassword());
			dto.setType(keystore.getType());
		}
		
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
			mailServerRsModel.setSslConfig(toConfigurazioneMailSslConfigRsModel(batchSpedizioneEmail.getMailserver().getSslConfig()));
		}
		rsModel.setMailserver(mailServerRsModel);


		return rsModel;
	}

	private static SslConfig toConfigurazioneMailSslConfigRsModel(it.govpay.bd.configurazione.model.SslConfig sslConfig) {
		SslConfig rsModel = new SslConfig();
		
		rsModel.setAbilitato(sslConfig.isAbilitato());
		if(sslConfig.isAbilitato()) {
			rsModel.setHostnameVerifier(sslConfig.isHostnameVerifier());
			rsModel.setType(sslConfig.getType());
			rsModel.setKeystore(toConfigurazioneMailKeystoreRsModel(sslConfig.getKeyStore()));
			rsModel.setTruststore(toConfigurazioneMailKeystoreRsModel(sslConfig.getTrustStore()));
		}
		
		return rsModel;
	}
	private static Keystore toConfigurazioneMailKeystoreRsModel(KeyStore keystore) {
		Keystore rsModel = null;
		
		if(keystore != null) {
			rsModel = new Keystore();
			
			rsModel.setLocation(keystore.getLocation());
			rsModel.setManagementAlgorithm(keystore.getManagementAlgorithm());
			rsModel.setPassword(keystore.getPassword());
			rsModel.setType(keystore.getType());
		}
		
		return rsModel;
	}
	private static it.govpay.bd.configurazione.model.AppIOBatch getConfigurazioneAppIOBatchDTO(AppIOBatch appIoBatch) {
		it.govpay.bd.configurazione.model.AppIOBatch dto = new it.govpay.bd.configurazione.model.AppIOBatch();

		dto.setAbilitato(appIoBatch.Abilitato());
		dto.setUrl(appIoBatch.getUrl());
		dto.setTimeToLive(appIoBatch.getTimeToLive());

		return dto;
	}

	private static AppIOBatch toConfigurazioneAppIOBatchRsModel(it.govpay.bd.configurazione.model.AppIOBatch batchSpedizioneAppIo) {
		AppIOBatch rsModel = new AppIOBatch();

		rsModel.setAbilitato(batchSpedizioneAppIo.isAbilitato());
		rsModel.setUrl(batchSpedizioneAppIo.getUrl());
		rsModel.setTimeToLive(batchSpedizioneAppIo.getTimeToLive());

		return rsModel;
	}
}
