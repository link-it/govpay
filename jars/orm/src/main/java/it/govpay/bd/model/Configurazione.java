package it.govpay.bd.model;

import java.util.Arrays;

import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.core.utils.SimpleDateFormatUtils;

public class Configurazione extends it.govpay.model.Configurazione {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Giornale giornale;

	public Giornale getGiornale() {
		if(this.giornale == null) {
			try {
				this.giornale = getGiornaleObject();
			} catch (IOException e) {
			}
		}
		
		return giornale;
	}

	public void setGiornale(Giornale giornale) {
		this.giornale = giornale;
	}
	
	public String getGiornaleJson() throws IOException {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
		ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
		return serializer.getObject(this.giornale); 
	}
	
	
	public Giornale getGiornaleObject() throws IOException {
		if(this.getGiornaleEventi() != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			serializationConfig.setIgnoreNullValues(true);
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return (Giornale) deserializer.getObject(this.getGiornaleEventi(), Giornale.class);
		}

		return null;
	}
}
