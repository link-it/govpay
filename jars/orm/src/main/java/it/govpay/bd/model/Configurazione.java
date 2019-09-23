package it.govpay.bd.model;

import java.util.Arrays;

import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.configurazione.model.TracciatoCsv;
import it.govpay.core.utils.SimpleDateFormatUtils;

public class Configurazione extends it.govpay.model.Configurazione {
	
	public static final String GIORNALE_EVENTI = "giornale_eventi";
	public static final String TRACCIATO_CSV = "tracciato_csv";
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Giornale giornale;
	private TracciatoCsv tracciatoCsv;

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
}
