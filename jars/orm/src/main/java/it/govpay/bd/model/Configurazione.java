package it.govpay.bd.model;

import java.util.Arrays;
import java.util.Properties;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.bd.configurazione.model.AppIO;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.configurazione.model.Hardening;
import it.govpay.bd.configurazione.model.Mail;
import it.govpay.bd.configurazione.model.MailBatch;
import it.govpay.bd.configurazione.model.TracciatoCsv;
import it.govpay.core.utils.SimpleDateFormatUtils;

public class Configurazione extends it.govpay.model.Configurazione {

	public static final String GIORNALE_EVENTI = "giornale_eventi";
	public static final String TRACCIATO_CSV = "tracciato_csv";
	public static final String HARDENING = "hardening";
	public static final String MAIL_BATCH = "mail_batch";
	public static final String MAIL_PROMEMORIA = "mail_promemoria";
	public static final String MAIL_RICEVUTA = "mail_ricevuta";
	public static final String APP_IO = "app_io";

	private Properties properties = new Properties();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Giornale giornale;
	private TracciatoCsv tracciatoCsv;
	private Hardening hardening;
	private MailBatch batchSpedizioneMail;
	private Mail promemoriaMail;
	private Mail ricevutaMail;
	private AppIO appIo;

	public Giornale getGiornale() throws ServiceException {
		if(this.giornale == null) {
			try {
				this.giornale = this._getFromJson(this.getGiornaleEventi(), Giornale.class);
			} catch (IOException e) {
				throw new ServiceException(e);
			}
		}

		return giornale;
	}

	public void setGiornale(Giornale giornale) {
		this.giornale = giornale;
	}

	public String getGiornaleJson() throws IOException, ServiceException {
		return this._getJson(this.getGiornale());
	}

	public TracciatoCsv getTracciatoCsv() throws ServiceException {
		if(this.tracciatoCsv == null) {
			try {
				this.tracciatoCsv = this._getFromJson(this.getTracciatoCSV(), TracciatoCsv.class);
			} catch (IOException e) {
				throw new ServiceException(e);
			}
		}
		return tracciatoCsv;
	}

	public void setTracciatoCsv(TracciatoCsv tracciatoCsv) {
		this.tracciatoCsv = tracciatoCsv;
	}

	public String getTracciatoCsvJson() throws IOException, ServiceException {
		return this._getJson(this.getTracciatoCsv());
	}

	public Hardening getHardening() throws ServiceException {
		if(this.hardening == null) {
			try {
				this.hardening = this._getFromJson(this.getConfHardening(), Hardening.class);
			} catch (IOException e) {
				throw new ServiceException(e);
			}
		}
		return hardening;
	}

	public void setHardening(Hardening hardening) {
		this.hardening = hardening;
	}

	public String getHardeningJson() throws IOException, ServiceException {
		return this._getJson(this.getHardening());
	}

	private <T> T _getFromJson(String jsonString, Class<T> tClass) throws IOException {
		if(jsonString != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			serializationConfig.setIgnoreNullValues(true);
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return tClass.cast(deserializer.getObject(jsonString, tClass));
		}

		return null;
	}

	private String _getJson(Object objToSerialize) throws IOException {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
		ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
		return serializer.getObject(objToSerialize); 
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public MailBatch getBatchSpedizioneEmail() throws ServiceException {
		if(this.batchSpedizioneMail == null) {
			try {
				this.batchSpedizioneMail = this._getFromJson(this.getMailBatch(), MailBatch.class);
			} catch (IOException e) {
				throw new ServiceException(e);
			}

		}

		return batchSpedizioneMail;
	}

	public void setBatchSpedizioneEmail(MailBatch batchSpedizioneMail) {
		this.batchSpedizioneMail = batchSpedizioneMail;
	}

	public String getBatchSpedizioneEmailJson() throws IOException, ServiceException {
		return this._getJson(this.getBatchSpedizioneEmail());
	}

	public Mail getPromemoriaMail() throws ServiceException {
		if(this.promemoriaMail == null) {
			try {
				this.promemoriaMail = this._getFromJson(this.getMailPromemoria(), Mail.class);
			} catch (IOException e) {
				throw new ServiceException(e);
			}
		}

		return promemoriaMail;
	}

	public void setPromemoriaEmail(Mail promemoriaMail) {
		this.promemoriaMail = promemoriaMail;
	}

	public String getPromemoriaMailJson() throws IOException, ServiceException {
		return this._getJson(this.getPromemoriaMail());
	}

	public Mail getRicevutaMail() throws ServiceException {
		if(this.ricevutaMail == null) {
			try {
				this.ricevutaMail = this._getFromJson(this.getMailRicevuta(), Mail.class);
			} catch (IOException e) {
				throw new ServiceException(e);
			}
		}

		return ricevutaMail;
	}

	public void setRicevutaEmail(Mail ricevutaMail) {
		this.ricevutaMail = ricevutaMail;
	}

	public String getRicevutaMailJson() throws IOException, ServiceException {
		return this._getJson(this.getRicevutaMail());
	}

	public AppIO getAppIo() throws ServiceException {
		if(this.appIo == null) {
			try {
				this.appIo = this._getFromJson(this.getAppIO(), AppIO.class);
			} catch (IOException e) {
				throw new ServiceException(e);
			}
		}
		
		return appIo;
	}

	public void setAppIo(AppIO appIo) {
		this.appIo = appIo;
	}

	public String getAppIoJson() throws IOException, ServiceException {
		return this._getJson(this.getAppIo());
	}
	
}
