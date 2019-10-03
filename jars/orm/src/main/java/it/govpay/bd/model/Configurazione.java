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
import it.govpay.bd.configurazione.model.ReCaptcha;
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
	private ReCaptcha reCaptcha;

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

	public ReCaptcha getReCaptcha() {
		if(this.reCaptcha == null) {
			this.reCaptcha = new ReCaptcha();
			
			String captchaEnabledS = this.getProperties().get(ReCaptcha.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_ENABLED);
			boolean abilitato = (StringUtils.isNotBlank(captchaEnabledS) && Boolean.valueOf(captchaEnabledS))?  true : false;
			this.reCaptcha.setAbilitato(abilitato);
			this.reCaptcha.setSecret(this.getProperties().get(ReCaptcha.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SECRET_KEY));
			this.reCaptcha.setSite(this.getProperties().get(ReCaptcha.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SITE_KEY));
			this.reCaptcha.setServerURL(this.getProperties().get(ReCaptcha.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SERVER_URL));
			this.reCaptcha.setResponseParameter(this.getProperties().get(ReCaptcha.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_RESPONSE_PARAMETER));
			
			String sogliaS = this.getProperties().get(ReCaptcha.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SOGLIA);
			try {
				this.reCaptcha.setSoglia(Double.parseDouble(sogliaS));
			}catch (Exception e) {
				this.reCaptcha.setSoglia(0.7d);
			}
		}
		
		return reCaptcha;
	}

	public void setReCaptcha(ReCaptcha reCaptcha) {
		this.reCaptcha = reCaptcha;
		
		if(this.reCaptcha != null) {
			
			this.getProperties().put(ReCaptcha.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_ENABLED, this.reCaptcha.isAbilitato() + "");
			this.getProperties().put(ReCaptcha.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SECRET_KEY, StringUtils.isBlank(this.reCaptcha.getSecret())? this.reCaptcha.getSecret(): "");
			this.getProperties().put(ReCaptcha.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SITE_KEY, StringUtils.isBlank(this.reCaptcha.getSite())? this.reCaptcha.getSite(): "");
			this.getProperties().put(ReCaptcha.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SERVER_URL, StringUtils.isBlank(this.reCaptcha.getServerURL())? this.reCaptcha.getServerURL(): "");
			this.getProperties().put(ReCaptcha.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_RESPONSE_PARAMETER, StringUtils.isBlank(this.reCaptcha.getResponseParameter())? this.reCaptcha.getResponseParameter(): "");
			this.getProperties().put(ReCaptcha.AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SOGLIA,  this.reCaptcha.getSoglia() + "");
			
		}
	}
	
}
