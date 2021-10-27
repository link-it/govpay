package it.govpay.core.serialization;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.SerializationConfig;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.rawutils.DateFormatUtils;
import it.govpay.core.utils.rawutils.DateModule;

public class JsonJacksonDeserializer implements IDeserializer{
	
	private static class BeanDeserializerModifierForIgnorables extends BeanDeserializerModifier {

        private List<String> ignorables;

        public BeanDeserializerModifierForIgnorables(List<String> properties) {
        	if(properties!=null)
        		this.ignorables = properties;
        	else
        		this.ignorables = new ArrayList<String>();
        }

        @Override
        public BeanDeserializerBuilder updateBuilder(
                DeserializationConfig config, BeanDescription beanDesc,
                BeanDeserializerBuilder builder) {

            for(String ignorable : this.ignorables) {
                builder.addIgnorable(ignorable);                
            }

            return builder;
        }

        @Override
        public List<BeanPropertyDefinition> updateProperties(
                DeserializationConfig config, BeanDescription beanDesc,
                List<BeanPropertyDefinition> propDefs) {

            List<BeanPropertyDefinition> newPropDefs = new ArrayList<>();
            for(BeanPropertyDefinition propDef : propDefs) {
                if(!this.ignorables.contains(propDef.getName())) {
                    newPropDefs.add(propDef);
                }
            }
            return newPropDefs;
        }
    }

	private ObjectMapper mapper;
	
	
	public JsonJacksonDeserializer () {
		this(new SerializationConfig());
	}
	public JsonJacksonDeserializer(SerializationConfig config) {
		BeanDeserializerModifier modifier = new BeanDeserializerModifierForIgnorables(config.getExcludes());
		DeserializerFactory dFactory = BeanDeserializerFactory.instance.withDeserializerModifier(modifier);

		this.mapper = new ObjectMapper(null, null, new DefaultDeserializationContext.Impl(dFactory));
		this.mapper.setDateFormat(config.getDf());
		if(config.isSerializeEnumAsString())
			this.mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

		mapper = new ObjectMapper();
		mapper.registerModule(new JaxbAnnotationModule());
		mapper.registerModule(new DateModule());
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setDateFormat(DateFormatUtils.newSimpleDateFormatSoloData());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
	}
	
	@Override
	public Object getObject(String s, Class<?> classType) throws IOException{
		try {
			return this.mapper.readValue(s, classType);
		} catch (java.io.IOException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Object readObject(InputStream is, Class<?> classType) throws IOException {
		try {
			return this.mapper.readValue(is, classType);
		} catch (java.io.IOException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Object readObject(Reader reader, Class<?> classType) throws IOException {
		try {
			return this.mapper.readValue(reader, classType);
		} catch (java.io.IOException e) {
			throw new IOException(e);
		}
	}
	
	
	// utility
	
	public static <T> T parse(String jsonString, Class<T> t) throws ServiceException, ValidationException  {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		serializationConfig.setIgnoreNullValues(true);
		return parse(jsonString, t, serializationConfig);
	}
	
	public static <T> T parse(String jsonString, Class<T> t, SerializationConfig serializationConfig) throws ServiceException, ValidationException  {
		try {
			IDeserializer deserializer = new JsonJacksonDeserializer(serializationConfig);
			
			@SuppressWarnings("unchecked")
			T object = (T) deserializer.getObject(jsonString, t);
			return object;
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new ValidationException(e.getMessage(), e);
		}
	}


	public static <T> T read(InputStream jsonIS, Class<T> t) throws ServiceException, ValidationException  {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		serializationConfig.setIgnoreNullValues(true);
		return read(jsonIS, t, serializationConfig);
	}
	
	public static <T> T read(InputStream jsonIS, Class<T> t, SerializationConfig serializationConfig) throws ServiceException, ValidationException  {
		try {
			IDeserializer deserializer = new JsonJacksonDeserializer(serializationConfig);
			
			@SuppressWarnings("unchecked")
			T object = (T) deserializer.readObject(jsonIS, t);
			return object;
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new ValidationException(e.getMessage(), e);
		}
	}
}
