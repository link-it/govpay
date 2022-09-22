package it.govpay.core.beans;

import java.io.InputStream;
import java.util.Arrays;

import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.SimpleDateFormatUtils;


@JsonFilter(value="risultati")  
public abstract class JSONSerializable {
	
	@JsonIgnore
	public abstract String getJsonIdFilter();
	
	public String toJSON(String fields) throws IOException {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return this.toJSON(fields, serializationConfig);
	}
	
	public String toJSON(String fields, SerializationConfig serializationConfig) throws IOException {
		try {
			if(fields != null && !fields.isEmpty()) {
				serializationConfig.setIncludes(Arrays.asList(fields.split(",")));
				serializationConfig.setExcludes(null); 
			}
			ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return serializer.getObject(this);
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new IOException("Errore nella serializzazione della risposta.", e);
		}
	}
	
	public static <T> T parse(String jsonString, Class<T> t) throws IOException  {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		serializationConfig.setIgnoreNullValues(true);
		return parse(jsonString, t, serializationConfig);
	}
	
	public static <T> T parse(String jsonString, Class<T> t, SerializationConfig serializationConfig) throws IOException  {
		try {
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			
			@SuppressWarnings("unchecked")
			T object = (T) deserializer.getObject(jsonString, t);
			return object;
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new IOException(e.getMessage(), e);
		}
	}


	public static <T> T read(InputStream jsonIS, Class<T> t) throws IOException  {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		serializationConfig.setIgnoreNullValues(true);
		return read(jsonIS, t, serializationConfig);
	}
	
	public static <T> T read(InputStream jsonIS, Class<T> t, SerializationConfig serializationConfig) throws IOException  {
		try {
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			
			@SuppressWarnings("unchecked")
			T object = (T) deserializer.readObject(jsonIS, t);
			return object;
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
}
