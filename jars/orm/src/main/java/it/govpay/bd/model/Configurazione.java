package it.govpay.bd.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.configurazione.model.Hardening;
import it.govpay.bd.configurazione.model.Hardening.GoogleCaptcha;
import it.govpay.bd.configurazione.model.TracciatoCsv;
import it.govpay.core.utils.SimpleDateFormatUtils;

public class Configurazione extends it.govpay.model.Configurazione {

	public static final String GIORNALE_EVENTI = "giornale_eventi";
	public static final String TRACCIATO_CSV = "tracciato_csv";

	private Map<String, String> properties = new HashMap<String, String>();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Giornale giornale;
	private TracciatoCsv tracciatoCsv;
	private Hardening hardening;

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

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Hardening getHardening() {
		if(this.hardening == null) {
			this.hardening = new Hardening();

			String captchaEnabledS = this.getProperties().get(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_HARDENING_ENABLED);
			boolean abilitato = true; 
			if((StringUtils.isNotBlank(captchaEnabledS))) {
				abilitato = Boolean.valueOf(captchaEnabledS);
			}
//			boolean abilitato = (StringUtils.isNotBlank(captchaEnabledS) && Boolean.valueOf(captchaEnabledS))?  true : false;
			this.hardening.setAbilitato(abilitato);

			this.hardening.setGoogleCatpcha(new GoogleCaptcha());

			this.hardening.getGoogleCatpcha().setSecretKey(this.getProperties().get(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SECRET_KEY));
			this.hardening.getGoogleCatpcha().setSiteKey(this.getProperties().get(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SITE_KEY));
			this.hardening.getGoogleCatpcha().setServerURL(this.getProperties().get(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SERVER_URL));
			this.hardening.getGoogleCatpcha().setResponseParameter(this.getProperties().get(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_RESPONSE_PARAMETER));


			String denyOnFailS = this.getProperties().get(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_DENY_ON_FAIL);
			boolean denyOnFail = true; 
			if((StringUtils.isNotBlank(denyOnFailS))) {
				denyOnFail = Boolean.valueOf(denyOnFailS);
			}
			this.hardening.getGoogleCatpcha().setDenyOnFail(denyOnFail);

			String connectionTimeoutS = this.getProperties().get(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_CONNECTION_TIMEOUT);
			try {
				this.hardening.getGoogleCatpcha().setConnectionTimeout(Integer.parseInt(connectionTimeoutS));
			}catch (Exception e) {
				this.hardening.getGoogleCatpcha().setConnectionTimeout(5000);
			}

			String readTimeoutS = this.getProperties().get(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_READ_TIMEOUT);
			try {
				this.hardening.getGoogleCatpcha().setReadTimeout(Integer.parseInt(readTimeoutS));
			}catch (Exception e) {
				this.hardening.getGoogleCatpcha().setReadTimeout(5000);
			}

			String sogliaS = this.getProperties().get(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SOGLIA);
			try {
				this.hardening.getGoogleCatpcha().setSoglia(Double.parseDouble(sogliaS));
			}catch (Exception e) {
				this.hardening.getGoogleCatpcha().setSoglia(0.7d);
			}
		}

		return hardening;
	}

	public void setHardening(Hardening hardening) {
		this.hardening = hardening;

		if(this.hardening != null) {

			this.getProperties().put(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_HARDENING_ENABLED, this.hardening.isAbilitato() + "");

			String secretKey = (this.hardening.getGoogleCatpcha() != null &&  StringUtils.isBlank(this.hardening.getGoogleCatpcha().getSecretKey())) ? this.hardening.getGoogleCatpcha().getSecretKey(): "";
			this.getProperties().put(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SECRET_KEY, secretKey);

			String siteKey = (this.hardening.getGoogleCatpcha() != null &&  StringUtils.isBlank(this.hardening.getGoogleCatpcha().getSiteKey())) ? this.hardening.getGoogleCatpcha().getSiteKey(): "";
			this.getProperties().put(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SITE_KEY, siteKey);

			String serverURL = (this.hardening.getGoogleCatpcha() != null &&  StringUtils.isBlank(this.hardening.getGoogleCatpcha().getServerURL())) ? this.hardening.getGoogleCatpcha().getServerURL(): "";
			this.getProperties().put(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SERVER_URL, serverURL);

			String parametro = (this.hardening.getGoogleCatpcha() != null &&  StringUtils.isBlank(this.hardening.getGoogleCatpcha().getResponseParameter())) ? this.hardening.getGoogleCatpcha().getResponseParameter(): "";
			this.getProperties().put(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_RESPONSE_PARAMETER, parametro);

			double soglia = this.hardening.getGoogleCatpcha() != null ? this.hardening.getGoogleCatpcha().getSoglia() : 0.7d;
			this.getProperties().put(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SOGLIA, soglia + "");
			
			int connectionTimeout = this.hardening.getGoogleCatpcha() != null ? this.hardening.getGoogleCatpcha().getConnectionTimeout(): 5000;
			this.getProperties().put(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_RESPONSE_PARAMETER, connectionTimeout + "");
			
			int readTimeout = this.hardening.getGoogleCatpcha() != null ? this.hardening.getGoogleCatpcha().getReadTimeout(): 5000;
			this.getProperties().put(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_RESPONSE_PARAMETER, readTimeout + "");
			
			boolean denyOnFail = this.hardening.getGoogleCatpcha() != null ? this.hardening.getGoogleCatpcha().isDenyOnFail(): true;
			this.getProperties().put(Hardening.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_RESPONSE_PARAMETER, denyOnFail + "");

		}
	}

}
