package it.govpay.web.rs.v1.beans;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

public abstract class JSONSerializable {
	
	@JsonIgnore
	public abstract String getJsonIdFilter();
	
	public String toJSON(String fields) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
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
	}

}
