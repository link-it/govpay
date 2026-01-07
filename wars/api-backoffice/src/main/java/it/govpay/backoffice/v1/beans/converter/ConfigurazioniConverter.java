/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.utils.jaxrs.RawObject;
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
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.anagrafica.utils.UtenzaPatchUtils;
import it.govpay.core.dao.configurazione.ConfigurazioneDAO;
import it.govpay.core.dao.configurazione.dto.PutConfigurazioneDTO;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.PatchOp;
import it.govpay.model.configurazione.KeyStore;

public class ConfigurazioniConverter {

	private ConfigurazioniConverter() {}

	public static final String PATH_GIORNALE_EVENTI = ConfigurazioneDAO.PATH_GIORNALE_EVENTI;
	public static final String PATH_SERVIZO_GDE = ConfigurazioneDAO.PATH_SERVIZO_GDE;
	public static final String PATH_TRACCIATO_CSV = ConfigurazioneDAO.PATH_TRACCIATO_CSV;
	public static final String PATH_HARDENING = ConfigurazioneDAO.PATH_HARDENING;
	public static final String PATH_MAIL_BATCH = ConfigurazioneDAO.PATH_MAIL_BATCH;
	public static final String PATH_APP_IO_BATCH = ConfigurazioneDAO.PATH_APP_IO_BATCH;
	public static final String PATH_AVVISATURA_MAIL = ConfigurazioneDAO.PATH_AVVISATURA_MAIL;
	public static final String PATH_AVVISATURA_APP_IO = ConfigurazioneDAO.PATH_AVVISATURA_APP_IO;

	public static PutConfigurazioneDTO getPutConfigurazioneDTO(Configurazione configurazionePost, Authentication user) throws IOException, ValidationException {
		PutConfigurazioneDTO putConfigurazioneDTO = new PutConfigurazioneDTO(user);

		it.govpay.bd.model.Configurazione configurazione = new it.govpay.bd.model.Configurazione();
		if(configurazionePost.getGiornaleEventi() != null)
			configurazione.setGiornale(GiornaleConverter.getGiornaleDTO(configurazionePost.getGiornaleEventi()));
		if(configurazionePost.getServizioGDE() != null)
			configurazione.setServizioGDE(getConnettoreGdeDTO(configurazionePost.getServizioGDE()));
		if(configurazionePost.getTracciatoCsv() != null)
			configurazione.setConfigurazioneTracciatoCsv(getTracciatoCsvDTO(configurazionePost.getTracciatoCsv()));
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
	public static Configurazione toRsModel(it.govpay.bd.model.Configurazione configurazione) throws IOException {
		Configurazione rsModel = new Configurazione();

		if(configurazione.getGiornale() != null) {
			rsModel.setGiornaleEventi(GiornaleConverter.toRsModel(configurazione.getGiornale()));
		}
		if(configurazione.getServizioGDE() != null) {
			rsModel.setServizioGDE(toConnettoreGdeRsModel(configurazione.getServizioGDE()));
		}
		if(configurazione.getConfigurazioneTracciatoCsv() != null) {
			rsModel.setTracciatoCsv(toTracciatoRsModel(configurazione.getConfigurazioneTracciatoCsv()));
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

	private static it.govpay.model.configurazione.TracciatoCsv getTracciatoCsvDTO(TracciatoCsv tracciatoCsv) throws IOException, ValidationException {
		it.govpay.model.configurazione.TracciatoCsv dto = new it.govpay.model.configurazione.TracciatoCsv();

		dto.setTipo(tracciatoCsv.getTipo());

		if(tracciatoCsv.getTipo() != null && TipoTemplateTrasformazione.fromValue(tracciatoCsv.getTipo()) == null) {
			throw new ValidationException(Costanti.LABEL_TIPO_TRASFORMAZIONE, tracciatoCsv.getTipo(), ArrayUtils.toString(TipoTemplateTrasformazione.values()));
		}

		dto.setIntestazione(tracciatoCsv.getIntestazione());
		dto.setRichiesta((ConverterUtils.toJSON(tracciatoCsv.getRichiesta())));
		dto.setRisposta(ConverterUtils.toJSON(tracciatoCsv.getRisposta()));

		return dto;
	}

	private static it.govpay.model.configurazione.TracciatoCsv getTracciatoCsvDTOPatch(TracciatoCsv tracciatoCsv) throws IOException, ValidationException {
		return getTracciatoCsvDTO(tracciatoCsv);
	}

	private static TracciatoCsv toTracciatoRsModel(it.govpay.model.configurazione.TracciatoCsv tracciatoCsv) {
		TracciatoCsv rsModel = new TracciatoCsv();

		rsModel.setTipo(tracciatoCsv.getTipo());
		rsModel.setIntestazione(tracciatoCsv.getIntestazione());
		rsModel.setRichiesta(new RawObject(tracciatoCsv.getRichiesta()));
		rsModel.setRisposta(new RawObject(tracciatoCsv.getRisposta()));

		return rsModel;
	}

	public static List<PatchOp> toModel(List<it.govpay.backoffice.v1.beans.PatchOp> lstOp) throws ValidationException, IOException {
		List<PatchOp> list = new ArrayList<>();
		for(it.govpay.backoffice.v1.beans.PatchOp op : lstOp) {
			PatchOp e = new PatchOp();
			e.setOp(PatchOp.OpEnum.fromValue(op.getOp().name()));
			e.setPath(op.getPath());

			if(PATH_GIORNALE_EVENTI.equals(op.getPath())) {
				it.govpay.backoffice.v1.beans.Giornale giornalePost = it.govpay.backoffice.v1.beans.Giornale.parse(ConverterUtils.toJSON(op.getValue()));
				giornalePost.validate();
				e.setValue(GiornaleConverter.getGiornaleDTO(giornalePost ));
			} else if(PATH_SERVIZO_GDE.equals(op.getPath())) {
				it.govpay.backoffice.v1.beans.ConnettoreGde connettoreGde = it.govpay.backoffice.v1.beans.ConnettoreGde.parse(ConverterUtils.toJSON(op.getValue()));
				connettoreGde.validate();
				e.setValue(getConnettoreGdeDTO(connettoreGde));
			} else if(PATH_TRACCIATO_CSV.equals(op.getPath())) {
				TracciatoCsv tracciatoCsv = TracciatoCsv.parse(ConverterUtils.toJSON(op.getValue()));
				tracciatoCsv.validate();
				e.setValue(getTracciatoCsvDTOPatch(tracciatoCsv ));
			} else if(PATH_HARDENING.equals(op.getPath())) {
				Hardening configurazioneHardening = Hardening.parse(ConverterUtils.toJSON(op.getValue()));
				configurazioneHardening.validate();
				e.setValue(getConfigurazioneHardeningDTO(configurazioneHardening ));
			} else if(PATH_MAIL_BATCH.equals(op.getPath())) {
				MailBatch configurazioneMailBatch = MailBatch.parse(ConverterUtils.toJSON(op.getValue()));
				configurazioneMailBatch.validate();
				e.setValue(getConfigurazioneMailBatchDTO(configurazioneMailBatch));
			} else if(PATH_AVVISATURA_MAIL.equals(op.getPath())) {
				ConfigurazioneAvvisaturaMail configurazioneAvvisaturaMail = ConfigurazioneAvvisaturaMail.parse(ConverterUtils.toJSON(op.getValue()));
				configurazioneAvvisaturaMail.validate();
				e.setValue(getConfigurazioneAvvisaturaMailDTO(configurazioneAvvisaturaMail));
			} else if(PATH_AVVISATURA_APP_IO.equals(op.getPath())) {
				ConfigurazioneAvvisaturaAppIO configurazioneAvvisaturaAppIo = ConfigurazioneAvvisaturaAppIO.parse(ConverterUtils.toJSON(op.getValue()));
				configurazioneAvvisaturaAppIo.validate();
				e.setValue(getConfigurazioneAvvisaturaAppIoDTO(configurazioneAvvisaturaAppIo));
			} else if(PATH_APP_IO_BATCH.equals(op.getPath())) {
				AppIOBatch configurazioneAppIO = AppIOBatch.parse(ConverterUtils.toJSON(op.getValue()));
				configurazioneAppIO.validate();
				e.setValue(getConfigurazioneAppIOBatchDTO(configurazioneAppIO	));
			} else {
				throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.PATH_XX_NON_VALIDO, op.getPath()));
			}

			list.add(e);
		}
		return list;
	}

	private static it.govpay.model.configurazione.Hardening getConfigurazioneHardeningDTO(Hardening configurazioneHardening) {
		it.govpay.model.configurazione.Hardening dto = new it.govpay.model.configurazione.Hardening();

		dto.setAbilitato(configurazioneHardening.getAbilitato());
		if(configurazioneHardening.getCaptcha() != null) {
			dto.setGoogleCatpcha(new it.govpay.model.configurazione.GoogleCaptcha());
			dto.getGoogleCatpcha().setResponseParameter(configurazioneHardening.getCaptcha().getParametro());
			dto.getGoogleCatpcha().setSecretKey(configurazioneHardening.getCaptcha().getSecretKey());
			dto.getGoogleCatpcha().setServerURL(configurazioneHardening.getCaptcha().getServerURL());
			dto.getGoogleCatpcha().setSiteKey(configurazioneHardening.getCaptcha().getSiteKey());
			dto.getGoogleCatpcha().setSoglia(configurazioneHardening.getCaptcha().getSoglia().doubleValue());
			dto.getGoogleCatpcha().setDenyOnFail(configurazioneHardening.getCaptcha().getDenyOnFail());
			dto.getGoogleCatpcha().setConnectionTimeout(configurazioneHardening.getCaptcha().getConnectionTimeout().intValue());
			dto.getGoogleCatpcha().setReadTimeout(configurazioneHardening.getCaptcha().getReadTimeout().intValue());
		}

		return dto;
	}

	private static Hardening toConfigurazioneHardeningRsModel(it.govpay.model.configurazione.Hardening configurazioneHardening) {
		Hardening rsModel = new Hardening();
		rsModel.setAbilitato(configurazioneHardening.isAbilitato());
		ConfigurazioneReCaptcha captchaRsModel = null;

		if(configurazioneHardening.getGoogleCatpcha() != null) {
			captchaRsModel = new ConfigurazioneReCaptcha();

			captchaRsModel.setParametro(configurazioneHardening.getGoogleCatpcha().getResponseParameter());
			captchaRsModel.setSecretKey(configurazioneHardening.getGoogleCatpcha().getSecretKey());
			captchaRsModel.setServerURL(configurazioneHardening.getGoogleCatpcha().getServerURL());
			captchaRsModel.setSiteKey(configurazioneHardening.getGoogleCatpcha().getSiteKey());
			captchaRsModel.setSoglia(BigDecimal.valueOf(configurazioneHardening.getGoogleCatpcha().getSoglia()));
			captchaRsModel.setDenyOnFail(configurazioneHardening.getGoogleCatpcha().isDenyOnFail());
			captchaRsModel.setConnectionTimeout(new BigDecimal(configurazioneHardening.getGoogleCatpcha().getConnectionTimeout()));
			captchaRsModel.setReadTimeout(new BigDecimal(configurazioneHardening.getGoogleCatpcha().getReadTimeout()));
		}
		rsModel.setCaptcha(captchaRsModel);

		return rsModel;
	}

	private static it.govpay.model.configurazione.AvvisaturaViaMail getConfigurazioneAvvisaturaMailDTO(ConfigurazioneAvvisaturaMail avvisaturaMail) throws IOException, ValidationException{
		it.govpay.model.configurazione.AvvisaturaViaMail dto = new it.govpay.model.configurazione.AvvisaturaViaMail();

		if(avvisaturaMail.getPromemoriaAvviso() != null) {
			it.govpay.model.configurazione.PromemoriaAvviso promemoriaAvviso = new it.govpay.model.configurazione.PromemoriaAvviso();

			promemoriaAvviso.setAllegaPdf(avvisaturaMail.getPromemoriaAvviso().getAllegaPdf());
			promemoriaAvviso.setTipo(avvisaturaMail.getPromemoriaAvviso().getTipo());
			if(avvisaturaMail.getPromemoriaAvviso().getTipo() != null && TipoTemplateTrasformazione.fromValue(avvisaturaMail.getPromemoriaAvviso().getTipo()) == null) {
				throw new ValidationException(Costanti.LABEL_TIPO_TRASFORMAZIONE, avvisaturaMail.getPromemoriaAvviso().getTipo(), ArrayUtils.toString(TipoTemplateTrasformazione.values()));
			}
			promemoriaAvviso.setMessaggio((ConverterUtils.toJSON(avvisaturaMail.getPromemoriaAvviso().getMessaggio())));
			promemoriaAvviso.setOggetto(ConverterUtils.toJSON(avvisaturaMail.getPromemoriaAvviso().getOggetto()));

			dto.setPromemoriaAvviso(promemoriaAvviso);
		}

		if(avvisaturaMail.getPromemoriaRicevuta() != null) {
			it.govpay.model.configurazione.PromemoriaRicevuta promemoriaRicevuta = new it.govpay.model.configurazione.PromemoriaRicevuta();

			promemoriaRicevuta.setSoloEseguiti(avvisaturaMail.getPromemoriaRicevuta().getSoloEseguiti());
			promemoriaRicevuta.setAllegaPdf(avvisaturaMail.getPromemoriaRicevuta().getAllegaPdf());
			promemoriaRicevuta.setTipo(avvisaturaMail.getPromemoriaRicevuta().getTipo());
			if(avvisaturaMail.getPromemoriaRicevuta().getTipo() != null && TipoTemplateTrasformazione.fromValue(avvisaturaMail.getPromemoriaRicevuta().getTipo()) == null) {
				throw new ValidationException(Costanti.LABEL_TIPO_TRASFORMAZIONE, avvisaturaMail.getPromemoriaRicevuta().getTipo(), ArrayUtils.toString(TipoTemplateTrasformazione.values()));
			}
			promemoriaRicevuta.setMessaggio((ConverterUtils.toJSON(avvisaturaMail.getPromemoriaRicevuta().getMessaggio())));
			promemoriaRicevuta.setOggetto(ConverterUtils.toJSON(avvisaturaMail.getPromemoriaRicevuta().getOggetto()));

			dto.setPromemoriaRicevuta(promemoriaRicevuta);
		}

		if(avvisaturaMail.getPromemoriaScadenza() != null) {
			it.govpay.model.configurazione.PromemoriaScadenza promemoriaScadenza = new it.govpay.model.configurazione.PromemoriaScadenza();

			if(avvisaturaMail.getPromemoriaScadenza().getPreavviso() != null) {
				promemoriaScadenza.setPreavviso(avvisaturaMail.getPromemoriaScadenza().getPreavviso().intValue());
			}
			promemoriaScadenza.setTipo(avvisaturaMail.getPromemoriaScadenza().getTipo());
			if(avvisaturaMail.getPromemoriaScadenza().getTipo() != null && TipoTemplateTrasformazione.fromValue(avvisaturaMail.getPromemoriaScadenza().getTipo()) == null) {
				throw new ValidationException(Costanti.LABEL_TIPO_TRASFORMAZIONE, avvisaturaMail.getPromemoriaScadenza().getTipo(), ArrayUtils.toString(TipoTemplateTrasformazione.values()));
			}
			promemoriaScadenza.setMessaggio((ConverterUtils.toJSON(avvisaturaMail.getPromemoriaScadenza().getMessaggio())));
			promemoriaScadenza.setOggetto(ConverterUtils.toJSON(avvisaturaMail.getPromemoriaScadenza().getOggetto()));

			dto.setPromemoriaScadenza(promemoriaScadenza);
		}


		return dto;
	}


	private static it.govpay.model.configurazione.AvvisaturaViaAppIo getConfigurazioneAvvisaturaAppIoDTO(ConfigurazioneAvvisaturaAppIO avvisaturaAppIo) throws IOException, ValidationException{
		it.govpay.model.configurazione.AvvisaturaViaAppIo dto = new it.govpay.model.configurazione.AvvisaturaViaAppIo();

		if(avvisaturaAppIo.getPromemoriaAvviso() != null) {
			it.govpay.model.configurazione.PromemoriaAvvisoBase promemoriaAvviso = new it.govpay.model.configurazione.PromemoriaAvvisoBase();

			promemoriaAvviso.setTipo(avvisaturaAppIo.getPromemoriaAvviso().getTipo());
			if(avvisaturaAppIo.getPromemoriaAvviso().getTipo() != null && TipoTemplateTrasformazione.fromValue(avvisaturaAppIo.getPromemoriaAvviso().getTipo()) == null) {
				throw new ValidationException(Costanti.LABEL_TIPO_TRASFORMAZIONE, avvisaturaAppIo.getPromemoriaAvviso().getTipo(), ArrayUtils.toString(TipoTemplateTrasformazione.values()));
			}
			promemoriaAvviso.setMessaggio((ConverterUtils.toJSON(avvisaturaAppIo.getPromemoriaAvviso().getMessaggio())));
			promemoriaAvviso.setOggetto(ConverterUtils.toJSON(avvisaturaAppIo.getPromemoriaAvviso().getOggetto()));

			dto.setPromemoriaAvviso(promemoriaAvviso);
		}

		if(avvisaturaAppIo.getPromemoriaRicevuta() != null) {
			it.govpay.model.configurazione.PromemoriaRicevutaBase promemoriaRicevuta = new it.govpay.model.configurazione.PromemoriaRicevutaBase();

			promemoriaRicevuta.setSoloEseguiti(avvisaturaAppIo.getPromemoriaRicevuta().getSoloEseguiti());
			promemoriaRicevuta.setTipo(avvisaturaAppIo.getPromemoriaRicevuta().getTipo());
			if(avvisaturaAppIo.getPromemoriaRicevuta().getTipo() != null && TipoTemplateTrasformazione.fromValue(avvisaturaAppIo.getPromemoriaRicevuta().getTipo()) == null) {
				throw new ValidationException(Costanti.LABEL_TIPO_TRASFORMAZIONE, avvisaturaAppIo.getPromemoriaRicevuta().getTipo(), ArrayUtils.toString(TipoTemplateTrasformazione.values()));
			}
			promemoriaRicevuta.setMessaggio((ConverterUtils.toJSON(avvisaturaAppIo.getPromemoriaRicevuta().getMessaggio())));
			promemoriaRicevuta.setOggetto(ConverterUtils.toJSON(avvisaturaAppIo.getPromemoriaRicevuta().getOggetto()));

			dto.setPromemoriaRicevuta(promemoriaRicevuta);
		}

		if(avvisaturaAppIo.getPromemoriaScadenza() != null) {
			it.govpay.model.configurazione.PromemoriaScadenza promemoriaScadenza = new it.govpay.model.configurazione.PromemoriaScadenza();

			if(avvisaturaAppIo.getPromemoriaScadenza().getPreavviso() != null) {
				promemoriaScadenza.setPreavviso(avvisaturaAppIo.getPromemoriaScadenza().getPreavviso().intValue());
			}
			promemoriaScadenza.setTipo(avvisaturaAppIo.getPromemoriaScadenza().getTipo());
			if(avvisaturaAppIo.getPromemoriaScadenza().getTipo() != null && TipoTemplateTrasformazione.fromValue(avvisaturaAppIo.getPromemoriaScadenza().getTipo()) == null) {
				throw new ValidationException(Costanti.LABEL_TIPO_TRASFORMAZIONE, avvisaturaAppIo.getPromemoriaScadenza().getTipo(), ArrayUtils.toString(TipoTemplateTrasformazione.values()));
			}
			promemoriaScadenza.setMessaggio((ConverterUtils.toJSON(avvisaturaAppIo.getPromemoriaScadenza().getMessaggio())));
			promemoriaScadenza.setOggetto(ConverterUtils.toJSON(avvisaturaAppIo.getPromemoriaScadenza().getOggetto()));

			dto.setPromemoriaScadenza(promemoriaScadenza);
		}


		return dto;
	}

	private static ConfigurazioneAvvisaturaMail toConfigurazioneAvvisaturaMailRsModel(it.govpay.model.configurazione.AvvisaturaViaMail avvisaturaMail) {
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

			if(avvisaturaMail.getPromemoriaScadenza().getPreavviso() != null) {
				promemoriaScadenza.setPreavviso(new BigDecimal(avvisaturaMail.getPromemoriaScadenza().getPreavviso()));
			}
			promemoriaScadenza.setTipo(avvisaturaMail.getPromemoriaScadenza().getTipo());
			promemoriaScadenza.setMessaggio(new RawObject(avvisaturaMail.getPromemoriaScadenza().getMessaggio()));
			promemoriaScadenza.setOggetto(new RawObject(avvisaturaMail.getPromemoriaScadenza().getOggetto()));

			rsModel.setPromemoriaScadenza(promemoriaScadenza);
		}

		return rsModel;
	}

	private static ConfigurazioneAvvisaturaAppIO toConfigurazioneAvvisaturaAppIoRsModel(it.govpay.model.configurazione.AvvisaturaViaAppIo avvisaturaMail) {
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

			if(avvisaturaMail.getPromemoriaScadenza().getPreavviso() != null) {
				promemoriaScadenza.setPreavviso(new BigDecimal(avvisaturaMail.getPromemoriaScadenza().getPreavviso()));
			}
			promemoriaScadenza.setTipo(avvisaturaMail.getPromemoriaScadenza().getTipo());
			promemoriaScadenza.setMessaggio(new RawObject(avvisaturaMail.getPromemoriaScadenza().getMessaggio()));
			promemoriaScadenza.setOggetto(new RawObject(avvisaturaMail.getPromemoriaScadenza().getOggetto()));

			rsModel.setPromemoriaScadenza(promemoriaScadenza);
		}

		return rsModel;
	}


	private static it.govpay.model.configurazione.MailBatch getConfigurazioneMailBatchDTO(MailBatch mailBatch) {
		it.govpay.model.configurazione.MailBatch dto = new it.govpay.model.configurazione.MailBatch();

		dto.setAbilitato(mailBatch.getAbilitato());
		it.govpay.model.configurazione.MailServer mailServerDTO = null;

		if(mailBatch.getMailserver() != null) {
			mailServerDTO = new it.govpay.model.configurazione.MailServer();

			mailServerDTO.setHost(mailBatch.getMailserver().getHost());
			mailServerDTO.setPort(mailBatch.getMailserver().getPort().intValue());
			mailServerDTO.setUsername(mailBatch.getMailserver().getUsername());
			mailServerDTO.setPassword(mailBatch.getMailserver().getPassword());
			mailServerDTO.setFrom(mailBatch.getMailserver().getFrom());
			mailServerDTO.setConnectionTimeout(mailBatch.getMailserver().getConnectionTimeout().intValue());
			mailServerDTO.setReadTimeout(mailBatch.getMailserver().getReadTimeout().intValue());
			if(mailBatch.getMailserver().getSslConfig() != null) {
				mailServerDTO.setSslConfig(getConfigurazioneSslConfigDTO(mailBatch.getMailserver().getSslConfig()));
			}
			mailServerDTO.setStartTls(mailBatch.getMailserver().getStartTls());
		}
		dto.setMailserver(mailServerDTO);

		return dto;
	}

	private static it.govpay.model.configurazione.SslConfig getConfigurazioneSslConfigDTO(SslConfig sslConfig) {
		it.govpay.model.configurazione.SslConfig dto = new it.govpay.model.configurazione.SslConfig();

		dto.setAbilitato(sslConfig.getAbilitato());
		dto.setHostnameVerifier(false);
		if(sslConfig.getHostnameVerifier() != null)
			dto.setHostnameVerifier(sslConfig.getHostnameVerifier());
		dto.setType(sslConfig.getType());
		dto.setKeyStore(getConfigurazioneKeyStoreDTO(sslConfig.getKeystore()));
		dto.setTrustStore(getConfigurazioneKeyStoreDTO(sslConfig.getTruststore()));

		return dto;
	}

	private static it.govpay.model.configurazione.KeyStore getConfigurazioneKeyStoreDTO(Keystore keystore) {

		it.govpay.model.configurazione.KeyStore dto = null;

		if(keystore != null &&
			(StringUtils.isNotEmpty(keystore.getType()) || StringUtils.isNotEmpty(keystore.getLocation()) || StringUtils.isNotEmpty(keystore.getPassword()) || StringUtils.isNotEmpty(keystore.getManagementAlgorithm()))
				) {
			dto = new it.govpay.model.configurazione.KeyStore();

			dto.setLocation(keystore.getLocation());
			dto.setManagementAlgorithm(keystore.getManagementAlgorithm());
			dto.setPassword(keystore.getPassword());
			dto.setType(keystore.getType());
		}

		return dto;
	}
	private static MailBatch toConfigurazioneMailBatchRsModel(it.govpay.model.configurazione.MailBatch batchSpedizioneEmail) {
		MailBatch rsModel = new MailBatch();

		rsModel.setAbilitato(batchSpedizioneEmail.isAbilitato());
		Mailserver mailServerRsModel = null;

		if(batchSpedizioneEmail.getMailserver() != null) {
			mailServerRsModel = new Mailserver();

			mailServerRsModel.setHost(batchSpedizioneEmail.getMailserver().getHost());
			mailServerRsModel.setPort(new BigDecimal(batchSpedizioneEmail.getMailserver().getPort()));
			mailServerRsModel.setUsername(batchSpedizioneEmail.getMailserver().getUsername());
			mailServerRsModel.setPassword(batchSpedizioneEmail.getMailserver().getPassword());
			mailServerRsModel.setFrom(batchSpedizioneEmail.getMailserver().getFrom());
			mailServerRsModel.setConnectionTimeout(new BigDecimal(batchSpedizioneEmail.getMailserver().getConnectionTimeout()));
			mailServerRsModel.setReadTimeout(new BigDecimal(batchSpedizioneEmail.getMailserver().getReadTimeout()));
			if(batchSpedizioneEmail.getMailserver().getSslConfig() != null) {
				mailServerRsModel.setSslConfig(toConfigurazioneMailSslConfigRsModel(batchSpedizioneEmail.getMailserver().getSslConfig()));
			}
			mailServerRsModel.setStartTls(batchSpedizioneEmail.getMailserver().isStartTls());
		}
		rsModel.setMailserver(mailServerRsModel);


		return rsModel;
	}

	private static SslConfig toConfigurazioneMailSslConfigRsModel(it.govpay.model.configurazione.SslConfig sslConfig) {
		SslConfig rsModel = new SslConfig();

		rsModel.setAbilitato(sslConfig.isAbilitato());
		rsModel.setHostnameVerifier(sslConfig.isHostnameVerifier());
		rsModel.setType(sslConfig.getType());
		rsModel.setKeystore(toConfigurazioneMailKeystoreRsModel(sslConfig.getKeyStore()));
		rsModel.setTruststore(toConfigurazioneMailKeystoreRsModel(sslConfig.getTrustStore()));

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
	private static it.govpay.model.configurazione.AppIOBatch getConfigurazioneAppIOBatchDTO(AppIOBatch appIoBatch) {
		it.govpay.model.configurazione.AppIOBatch dto = new it.govpay.model.configurazione.AppIOBatch();

		ConnettoriConverter.setAutenticazione(dto, appIoBatch.getAuth());

		dto.setAbilitato(appIoBatch.getAbilitato());
		dto.setUrl(appIoBatch.getUrl());
		dto.setTimeToLive(appIoBatch.getTimeToLive());


		return dto;
	}

	private static AppIOBatch toConfigurazioneAppIOBatchRsModel(it.govpay.model.configurazione.AppIOBatch batchSpedizioneAppIo) {
		AppIOBatch rsModel = new AppIOBatch();

		rsModel.setAbilitato(batchSpedizioneAppIo.isAbilitato());
		rsModel.setUrl(batchSpedizioneAppIo.getUrl());
		rsModel.setTimeToLive(batchSpedizioneAppIo.getTimeToLive());

		if(batchSpedizioneAppIo.getTipoAutenticazione()!=null && !batchSpedizioneAppIo.getTipoAutenticazione().equals(EnumAuthType.NONE))
			rsModel.setAuth(ConnettoriConverter.toTipoAutenticazioneRsModel(batchSpedizioneAppIo));

		return rsModel;
	}

	private static it.govpay.model.Connettore getConnettoreGdeDTO(it.govpay.backoffice.v1.beans.ConnettoreGde connettoreGdePost) {
		it.govpay.model.Connettore connettoreGde = new it.govpay.model.Connettore();

		connettoreGde.setIdConnettore(it.govpay.bd.model.Configurazione.COD_CONNETTORE_GDE);
		connettoreGde.setAbilitato(connettoreGdePost.getAbilitato());
		connettoreGde.setUrl(connettoreGdePost.getUrl());

		ConnettoriConverter.setAutenticazione(connettoreGde, connettoreGdePost.getAuth());

		return connettoreGde;
	}

	private static it.govpay.backoffice.v1.beans.ConnettoreGde toConnettoreGdeRsModel(it.govpay.model.Connettore connettoreGde) {
		it.govpay.backoffice.v1.beans.ConnettoreGde rsModel = new it.govpay.backoffice.v1.beans.ConnettoreGde();

		rsModel.setAbilitato(connettoreGde.isAbilitato());
		rsModel.setUrl(connettoreGde.getUrl());

		if(connettoreGde.getTipoAutenticazione() != null && !connettoreGde.getTipoAutenticazione().equals(EnumAuthType.NONE)) {
			rsModel.setAuth(ConnettoriConverter.toTipoAutenticazioneRsModel(connettoreGde));
		}

		return rsModel;
	}
}
