package it.govpay.rs.v1.beans;

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

import it.govpay.rs.BaseRsService;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

@JsonFilter(value="risultati")  
public abstract class JSONSerializable {
	
	@JsonIgnore
	public abstract String getJsonIdFilter();
	
	public String toJSON(String fields) {
		return toJSON(fields, new ObjectMapper());
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
	
	public static Object parse(String json, Class<?> clazz) {
		JSONObject jsonObject = JSONObject.fromObject( json );  
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setRootClass(clazz);
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(BaseRsService.datePatterns.toArray(new String[1])) , true);

		return JSONObject.toBean( jsonObject, jsonConfig );
	}
	
	public static Object parse(String json, Class<?> clazz, JsonConfig jsonConfig) {
		JSONObject jsonObject = JSONObject.fromObject( json );  
		jsonConfig.setRootClass(clazz);
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(BaseRsService.datePatterns.toArray(new String[1])) , true);

		return JSONObject.toBean( jsonObject, jsonConfig );
	}
	


}
