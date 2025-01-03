/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model;

import java.util.Arrays;
import java.util.Properties;

import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.configurazione.AppIOBatch;
import it.govpay.model.configurazione.AvvisaturaViaAppIo;
import it.govpay.model.configurazione.AvvisaturaViaMail;
import it.govpay.model.configurazione.Giornale;
import it.govpay.model.configurazione.Hardening;
import it.govpay.model.configurazione.MailBatch;
import it.govpay.model.configurazione.TracciatoCsv;

public class Configurazione extends it.govpay.model.Configurazione {

	public static final String KEY_GIORNALE_EVENTI = "giornale_eventi";
	public static final String KEY_TRACCIATO_CSV = "tracciato_csv";
	public static final String KEY_HARDENING = "hardening";
	public static final String KEY_MAIL_BATCH = "mail_batch";
	public static final String KEY_APP_IO_BATCH = "app_io_batch";
	public static final String KEY_AVVISATURA_MAIL = "avvisatura_mail";
	public static final String KEY_AVVISATURA_APP_IO = "avvisatura_app_io";


	private Properties properties = new Properties();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Giornale giornale;
	private TracciatoCsv tracciatoCsv;
	private Hardening hardening;
	private MailBatch batchSpedizioneMail;
	private AvvisaturaViaMail avvisaturaViaMail;
	private AvvisaturaViaAppIo avvisaturaViaAppIo;
	private AppIOBatch batchSpedizioneAppIo;

	public Giornale getGiornale() throws IOException {
		if(this.giornale == null) {
			this.giornale = this._getFromJson(this.getGiornaleEventi(), Giornale.class);
		}

		return giornale;
	}

	public void setGiornale(Giornale giornale) {
		this.giornale = giornale;
	}

	public String getGiornaleJson() throws IOException {
		return this._getJson(this.getGiornale());
	}

	public TracciatoCsv getConfigurazioneTracciatoCsv() throws IOException {
		if(this.tracciatoCsv == null) {
			this.tracciatoCsv = this._getFromJson(this.getTracciatoCSV(), TracciatoCsv.class);
		}
		return tracciatoCsv;
	}

	public void setConfigurazioneTracciatoCsv(TracciatoCsv tracciatoCsv) {
		this.tracciatoCsv = tracciatoCsv;
	}

	public String getTracciatoCsvJson() throws IOException {
		return this._getJson(this.getConfigurazioneTracciatoCsv());
	}

	public Hardening getHardening() throws IOException {
		if(this.hardening == null) {
			this.hardening = this._getFromJson(this.getConfHardening(), Hardening.class);
		}
		return hardening;
	}

	public void setHardening(Hardening hardening) {
		this.hardening = hardening;
	}

	public String getHardeningJson() throws IOException {
		return this._getJson(this.getHardening());
	}

	private <T> T _getFromJson(String jsonString, Class<T> tClass) throws IOException {
		if(jsonString != null) {
			try {
				SerializationConfig serializationConfig = new SerializationConfig();
				serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
				serializationConfig.setIgnoreNullValues(true);
				IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
				return tClass.cast(deserializer.getObject(jsonString, tClass));
			} catch(org.openspcoop2.utils.serialization.IOException e) {
				throw new IOException(e.getMessage(), e);
			}
		}

		return null;
	}

	private String _getJson(Object objToSerialize) throws IOException {
		try {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return serializer.getObject(objToSerialize); 
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public MailBatch getBatchSpedizioneEmail() throws it.govpay.core.exceptions.IOException {
		if(this.batchSpedizioneMail == null) {
			try {
				this.batchSpedizioneMail = this._getFromJson(this.getMailBatch(), MailBatch.class);
			} catch (IOException e) {
				throw new it.govpay.core.exceptions.IOException(e);
			}

		}

		return batchSpedizioneMail;
	}

	public void setBatchSpedizioneEmail(MailBatch batchSpedizioneMail) {
		this.batchSpedizioneMail = batchSpedizioneMail;
	}

	public String getBatchSpedizioneEmailJson() throws IOException {
		return this._getJson(this.getBatchSpedizioneEmail());
	}

	public AppIOBatch getBatchSpedizioneAppIo() throws IOException {
		if(this.batchSpedizioneAppIo == null) {
			this.batchSpedizioneAppIo = this._getFromJson(this.getAppIOBatch(), AppIOBatch.class);
		}

		return batchSpedizioneAppIo;
	}

	public void setBatchSpedizioneAppIo(AppIOBatch batchSpedizioneAppIo) {
		this.batchSpedizioneAppIo = batchSpedizioneAppIo;
	}

	public String getBatchSpedizioneAppIoJson() throws IOException {
		return this._getJson(this.getBatchSpedizioneAppIo());
	}

	public AvvisaturaViaMail getAvvisaturaViaMail()  throws IOException {
		if(this.avvisaturaViaMail == null) {
			this.avvisaturaViaMail = this._getFromJson(this.getAvvisaturaMail(), AvvisaturaViaMail.class);
		}
		return avvisaturaViaMail;
	}

	public void setAvvisaturaViaMail(AvvisaturaViaMail avvisaturaViaMail) {
		this.avvisaturaViaMail = avvisaturaViaMail;
	}

	public String getAvvisaturaViaMailJson() throws IOException {
		return this._getJson(this.getAvvisaturaViaMail());
	}

	public AvvisaturaViaAppIo getAvvisaturaViaAppIo() throws IOException {
		if(this.avvisaturaViaAppIo == null) {
			this.avvisaturaViaAppIo = this._getFromJson(this.getAvvisaturaAppIo(), AvvisaturaViaAppIo.class);
		}
		return avvisaturaViaAppIo;
	}

	public void setAvvisaturaViaAppIo(AvvisaturaViaAppIo avvisaturaViaAppIo) {
		this.avvisaturaViaAppIo = avvisaturaViaAppIo;
	}

	public String getAvvisaturaViaAppIoJson() throws IOException {
		return this._getJson(this.getAvvisaturaViaAppIo());
	}
}
