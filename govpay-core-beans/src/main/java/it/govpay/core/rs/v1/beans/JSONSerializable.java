package it.govpay.core.rs.v1.beans;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.core.utils.SimpleDateFormatUtils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@JsonFilter(value="risultati")  
public abstract class JSONSerializable {
	
	@JsonIgnore
	public abstract String getJsonIdFilter();
	
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		return toJSON(fields, mapper);
	}
	
	public String toJSON(String fields,ObjectMapper mapper) {
		try {
		SimpleFilterProvider filters = new SimpleFilterProvider();
		
		if(fields != null && !fields.isEmpty()) {
			Map<String, Set<String>> filterMap = new HashMap<String, Set<String>>();
			
			boolean rootFilter = false;
			for(String field : fields.split(",")) {
				
				if(!field.contains(".")) {
					Set<String> filterSet = filterMap.get(getJsonIdFilter());
					if(filterSet == null) filterSet = new HashSet<String>();
					filterSet.add(field);
					filterMap.put(getJsonIdFilter(), filterSet);
					rootFilter = true;
				} else {
					String[] split = field.split("\\.");
					
					Set<String> filterSet = filterMap.get(getJsonIdFilter());
					if(filterSet == null) filterSet = new HashSet<String>();
					filterSet.add(split[0]);
					filterMap.put(getJsonIdFilter(), filterSet);
					
					filterSet = filterMap.get(split[0]);
					if(filterSet == null) filterSet = new HashSet<String>();
					filterSet.add(split[1]);
					filterMap.put(split[0], filterSet);
				} 	
			}
			if(!rootFilter) filterMap.remove(getJsonIdFilter());
			for(String key : filterMap.keySet()) {
				filters = filters.addFilter(key, SimpleBeanPropertyFilter.filterOutAllExcept(filterMap.get(key)));
			}
		}
		filters = filters.setFailOnUnknownId(false);
		return mapper.writer(filters).writeValueAsString(this);
		} catch(IOException e) {
			return "";
		}
		
	}
	
	
	public static <T> T parse(String jsonString, Class<T> t) throws ServiceException  {
		try {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
			serializationConfig.setIgnoreNullValues(true);
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			
			@SuppressWarnings("unchecked")
			T object = (T) deserializer.getObject(jsonString, t);
			return object;
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new ServiceException("Errore nella deserializzazione dei dati - " + t.getSimpleName());
		}
	}

	public static Object parse(String json, Class<?> clazz, JsonConfig jsonConfig) throws ServiceException {
		return parse(json, clazz);
	}
	
	public static Object parse(JSONObject jsonObject, Class<?> clazz, JsonConfig jsonConfig) throws ServiceException {
		
		return parse(jsonObject.toString(), clazz);
	}


}
