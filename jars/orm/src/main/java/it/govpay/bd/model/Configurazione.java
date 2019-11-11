package it.govpay.bd.model;

import java.util.Arrays;
import java.util.Properties;

import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

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

	public Giornale getGiornale() {
		if(this.giornale == null) {
			try {
				this.giornale = this._getFromJson(this.getGiornaleEventi(), Giornale.class);
			} catch (IOException e) {
			}
		}

		return giornale;
	}

	public void setGiornale(Giornale giornale) {
		this.giornale = giornale;
	}

	public String getGiornaleJson() throws IOException {
		return this._getJson(this.getGiornale());
	}

	public TracciatoCsv getTracciatoCsv() {
		if(this.tracciatoCsv == null) {
			try {
				this.tracciatoCsv = this._getFromJson(this.getTracciatoCSV(), TracciatoCsv.class);
			} catch (IOException e) {
			}
		}
		return tracciatoCsv;
	}

	public void setTracciatoCsv(TracciatoCsv tracciatoCsv) {
		this.tracciatoCsv = tracciatoCsv;
	}

	public String getTracciatoCsvJson() throws IOException {
		return this._getJson(this.getTracciatoCsv());
	}
	
	public Hardening getHardening() {
		if(this.hardening == null) {
			try {
				this.hardening = this._getFromJson(this.getConfHardening(), Hardening.class);
			} catch (IOException e) {
			}
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
	
	public MailBatch getBatchSpedizioneEmail() {
		if(this.batchSpedizioneMail == null) {
			try {
				this.batchSpedizioneMail = this._getFromJson(this.getMailBatch(), MailBatch.class);
			} catch (IOException e) {
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
	
	public Mail getPromemoriaMail() {
		if(this.promemoriaMail == null) {
			try {
				this.promemoriaMail = this._getFromJson(this.getMailPromemoria(), Mail.class);
			} catch (IOException e) {
			}
		}

		return promemoriaMail;
	}

	public void setPromemoriaEmail(Mail promemoriaMail) {
		this.promemoriaMail = promemoriaMail;
	}

	public String getPromemoriaMailJson() throws IOException {
		return this._getJson(this.getPromemoriaMail());
	}
	
	public Mail getRicevutaMail() {
		if(this.ricevutaMail == null) {
			try {
				this.ricevutaMail = this._getFromJson(this.getMailRicevuta(), Mail.class);
			} catch (IOException e) {
			}
		}

		return ricevutaMail;
	}

	public void setRicevutaEmail(Mail ricevutaMail) {
		this.ricevutaMail = ricevutaMail;
	}

	public String getRicevutaMailJson() throws IOException {
		return this._getJson(this.getRicevutaMail());
	}

//	public Hardening getHardening() {
//		if(this.hardening == null) {
//			String hardeningEnabledS = this.properties.getProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_HARDENING_ENABLED);
//			boolean abilitato = true; 
//			if((StringUtils.isNotBlank(hardeningEnabledS))) {
//				abilitato = Boolean.valueOf(hardeningEnabledS);
//			}
//
//			this.hardening = getHardening(this.getProperties(), abilitato);
//		}
//		return hardening;
//	}
//
//
//	public static Hardening getHardening(Properties properties, boolean abilitato) {
//		Hardening hardening = new Hardening();
//
//		hardening.setAbilitato(abilitato);
//		hardening.setGoogleCatpcha(new GoogleCaptcha());
//
//		hardening.getGoogleCatpcha().setSecretKey(properties.getProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SECRET_KEY));
//		hardening.getGoogleCatpcha().setSiteKey(properties.getProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SITE_KEY));
//		hardening.getGoogleCatpcha().setServerURL(properties.getProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SERVER_URL));
//		hardening.getGoogleCatpcha().setResponseParameter(properties.getProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_RESPONSE_PARAMETER));
//
//
//		String denyOnFailS = properties.getProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_DENY_ON_FAIL);
//		boolean denyOnFail = true; 
//		if((StringUtils.isNotBlank(denyOnFailS))) {
//			denyOnFail = Boolean.valueOf(denyOnFailS);
//		}
//		hardening.getGoogleCatpcha().setDenyOnFail(denyOnFail);
//
//		String connectionTimeoutS = properties.getProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_CONNECTION_TIMEOUT);
//		try {
//			hardening.getGoogleCatpcha().setConnectionTimeout(Integer.parseInt(connectionTimeoutS));
//		}catch (Exception e) {
//			hardening.getGoogleCatpcha().setConnectionTimeout(5000);
//		}
//
//		String readTimeoutS = properties.getProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_READ_TIMEOUT);
//		try {
//			hardening.getGoogleCatpcha().setReadTimeout(Integer.parseInt(readTimeoutS));
//		}catch (Exception e) {
//			hardening.getGoogleCatpcha().setReadTimeout(5000);
//		}
//
//		String sogliaS = properties.getProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SOGLIA);
//		try {
//			hardening.getGoogleCatpcha().setSoglia(Double.parseDouble(sogliaS));
//		}catch (Exception e) {
//			hardening.getGoogleCatpcha().setSoglia(0.7d);
//		}
//
//		return hardening;
//	}
//
//	public void setHardening(Hardening hardening) {
//		this.hardening = hardening;
//	}
//
//	public void preparaSalvataggioConfigurazione() {
//		if(this.hardening != null) {
//			this.getProperties().setProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_HARDENING_ENABLED, this.hardening.isAbilitato() + "");
//
//			String secretKey = (this.hardening.getGoogleCatpcha() != null &&  StringUtils.isNotBlank(this.hardening.getGoogleCatpcha().getSecretKey())) ? this.hardening.getGoogleCatpcha().getSecretKey(): "";
//			this.getProperties().setProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SECRET_KEY, secretKey);
//
//			String siteKey = (this.hardening.getGoogleCatpcha() != null &&  StringUtils.isNotBlank(this.hardening.getGoogleCatpcha().getSiteKey())) ? this.hardening.getGoogleCatpcha().getSiteKey(): "";
//			this.getProperties().setProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SITE_KEY, siteKey);
//
//			String serverURL = (this.hardening.getGoogleCatpcha() != null &&  StringUtils.isNotBlank(this.hardening.getGoogleCatpcha().getServerURL())) ? this.hardening.getGoogleCatpcha().getServerURL(): "";
//			this.getProperties().setProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SERVER_URL, serverURL);
//
//			String parametro = (this.hardening.getGoogleCatpcha() != null &&  StringUtils.isNotBlank(this.hardening.getGoogleCatpcha().getResponseParameter())) ? this.hardening.getGoogleCatpcha().getResponseParameter(): "";
//			this.getProperties().setProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_RESPONSE_PARAMETER, parametro);
//
//			double soglia = this.hardening.getGoogleCatpcha() != null ? this.hardening.getGoogleCatpcha().getSoglia() : 0.7d;
//			this.getProperties().setProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SOGLIA, soglia + "");
//
//			int connectionTimeout = this.hardening.getGoogleCatpcha() != null ? this.hardening.getGoogleCatpcha().getConnectionTimeout(): 5000;
//			this.getProperties().setProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_CONNECTION_TIMEOUT, connectionTimeout + "");
//
//			int readTimeout = this.hardening.getGoogleCatpcha() != null ? this.hardening.getGoogleCatpcha().getReadTimeout(): 5000;
//			this.getProperties().setProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_READ_TIMEOUT, readTimeout + "");
//
//			boolean denyOnFail = this.hardening.getGoogleCatpcha() != null ? this.hardening.getGoogleCatpcha().isDenyOnFail(): true;
//			this.getProperties().setProperty(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_DENY_ON_FAIL, denyOnFail + "");
//
//		}
//	}
}
