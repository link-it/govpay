/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.core.utils.trasformazioni;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.json.JsonPathException;
import org.openspcoop2.utils.json.JsonPathNotFoundException;
import org.openspcoop2.utils.json.JsonPathNotValidException;
import org.openspcoop2.utils.json.JsonPathReturnType;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.cache.CacheProvider;
import com.jayway.jsonpath.spi.cache.NOOPCache;

import it.govpay.core.utils.rawutils.JSONUtils;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

/**	
 * JsonPathExpressionEngine {@link org.openspcoop2.utils.json.JsonPathExpressionEngine} eliminando pero' la dipendenza da JODA.
 *
 * @author Poli Andrea (apoli@link.it)
 * @author Pintori Giuliano (giuliano.pintori@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JsonPathExpressionEngine {

	private static final String NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0 = "Nessun match trovato per l''espressione jsonPath [{0}]";
	private static final String JSON_PATH_RETURN_TYPE_IS_NULL = "JsonPathReturnType is null";
	private static final String PATTERN_IS_NULL = "Pattern is null";
	private static final String DOCUMENT_JSON_OBJECT_IS_NULL = "Document (JSONObject) is null";
	private static final String DOCUMENT_JSON_NODE_IS_NULL = "Document (JsonNode) is null";
	private static final String DOCUMENT_STRING_IS_NULL = "Document (String) is null";
	private static final String DOCUMENT_INPUT_STREAM_IS_NULL = "Document (InputStream) is null";
	
	public static void disableCacheJsonPathEngine() {
		CacheProvider.setCache(new NOOPCache());
	}
	
	public static String getAsString(JsonNode element) {
		try{
			return getJsonUtils().toString(element);
		}catch(Exception e){
			return null;
		}
	}

	private static JSONUtils jsonUtils;
	public static synchronized void initJsonUtils() {
		if(jsonUtils == null) {
			jsonUtils = JSONUtils.getInstance();
		}
	}
	public static JSONUtils getJsonUtils() {
		if(jsonUtils == null) { 
			initJsonUtils();
		}
		return jsonUtils;
	}
	
	
	public static JSONObject getJSONObject(InputStream is) throws JsonPathException {
		if(is == null)
			throw new JsonPathException(DOCUMENT_INPUT_STREAM_IS_NULL);

		try {
			return getJSONParser().parse(is, JSONObject.class);
		} catch (UnsupportedEncodingException e) {
			throw new JsonPathException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	public static JSONArray getJSONArray(InputStream is) throws JsonPathException {
		if(is == null)
			throw new JsonPathException(DOCUMENT_INPUT_STREAM_IS_NULL);

		try {
			return getJSONParser().parse(is, JSONArray.class);
		} catch (UnsupportedEncodingException e) {
			throw new JsonPathException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}

	public static JSONObject getJSONObject(String contenuto) throws JsonPathException {
		if(contenuto == null)
			throw new JsonPathException(DOCUMENT_STRING_IS_NULL);

		try {
			return getJSONParser().parse(contenuto, JSONObject.class);
		} catch (ParseException e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	public static JSONArray getJSONArray(String contenuto) throws JsonPathException {
		if(contenuto == null)
			throw new JsonPathException(DOCUMENT_STRING_IS_NULL);

		try {
			return getJSONParser().parse(contenuto, JSONArray.class);
		} catch (ParseException e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}

	public static JSONObject getJSONObject(JsonNode document) throws JsonPathException {
		if(document == null)
			throw new JsonPathException(DOCUMENT_JSON_NODE_IS_NULL);

		try {
			return getJSONParser().parse(getAsString(document), JSONObject.class);
		} catch (ParseException e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	public static JSONArray getJSONArray(JsonNode document) throws JsonPathException {
		if(document == null)
			throw new JsonPathException(DOCUMENT_JSON_NODE_IS_NULL);

		try {
			return getJSONParser().parse(getAsString(document), JSONArray.class);
		} catch (ParseException e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}

	public static JSONParser getJSONParser() {
		return new JSONParser(JSONParser.MODE_PERMISSIVE); 
	}

	/* ---------- METODI RITORNANO STRINGHE -------------- */
	
	public List<String> getStringMatchPattern(JSONObject input, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(input == null)
			throw new JsonPathException(DOCUMENT_JSON_OBJECT_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			List<String> l = new ArrayList<>();
			Object o = JsonPath.read(input, pattern);
			_parseStringMatchPatternResult(o, l);
			if(l==null || l.isEmpty()) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return l;
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		} catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}

	public List<String> getStringMatchPattern(JsonNode document, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		
		if(document == null)
				throw new JsonPathException(DOCUMENT_JSON_NODE_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			List<String> l = new ArrayList<>();
			Object o = JsonPath.read(getAsString(document), pattern);
			_parseStringMatchPatternResult(o, l);
			if(l==null || l.isEmpty()) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return l;
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		} catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	
	public List<String> getStringMatchPattern(InputStream is, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(is == null)
			throw new JsonPathException(DOCUMENT_INPUT_STREAM_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			List<String> l = new ArrayList<>();
			Object o = JsonPath.read(is, pattern);
			_parseStringMatchPatternResult(o, l);
			if(l==null || l.isEmpty()) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return l;
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		} catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	
	public List<String> getStringMatchPattern(String contenuto, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(contenuto == null)
			throw new JsonPathException(DOCUMENT_STRING_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			List<String> l = new ArrayList<>();
			Object o = JsonPath.read(contenuto, pattern);
			_parseStringMatchPatternResult(o, l);
			if(l==null || l.isEmpty()) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return l;
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		} catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	private void _parseStringMatchPatternResult(Object o, List<String> l) throws Exception {
		if(o!=null) {
			if(o instanceof Map<?, ?>) {
				// Lasciare questo if per formattare correttamente i nomi degli elementi tramite le utility (vedi test org.openspcoop2.pdd.core.trasformazioni.Test)
				throw new Exception("Unexpected type '"+o.getClass().getName()+"' (is instanceof Map)");
			}
			else if(o instanceof List) {
				List<?> lO = (List<?>) o;
				if(!lO.isEmpty()) {
					int position = 0;
					for (Object object : lO) {
						if(object!=null) {
							if(object instanceof String) {
								l.add((String)object);
							}
							else if(object instanceof Number) {
								l.add(object.toString());
							}
							else if(object instanceof Boolean) {
								l.add(object.toString());
							}
							else{
								throw new Exception("Unexpected type '"+object.getClass().getName()+"' at position "+position);
							}
						}
						position++;
					}
				}
			}
			else if(o instanceof String) {
				l.add((String)o);
			}
			else if(o instanceof Number) {
				l.add(o.toString());
			}
			else if(o instanceof Boolean) {
				l.add(o.toString());
			}
			else {
				throw new Exception("Unexpected type '"+o.getClass().getName()+"'");
			}
		}
	}
	
	
	/* ---------- METODI RITORNANO NUMBER -------------- */

	public List<Number> getNumberMatchPattern(JSONObject input, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(input == null)
			throw new JsonPathException(DOCUMENT_JSON_OBJECT_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			List<Number> l = new ArrayList<>();
			Object o = JsonPath.read(input, pattern);
			_parseNumberMatchPatternResult(o, l);
			if(l==null || l.isEmpty()) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return l;
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		} catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}

	public List<Number> getNumberMatchPattern(JsonNode document, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(document == null)
			throw new JsonPathException(DOCUMENT_JSON_NODE_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			List<Number> l = new ArrayList<>();
			Object o = JsonPath.read(getAsString(document), pattern);
			_parseNumberMatchPatternResult(o, l);
			if(l==null || l.isEmpty()) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return l;
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		} catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	
	public List<Number> getNumberMatchPattern(InputStream is, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(is == null)
			throw new JsonPathException(DOCUMENT_INPUT_STREAM_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			List<Number> l = new ArrayList<>();
			Object o = JsonPath.read(is, pattern);
			_parseNumberMatchPatternResult(o, l);
			if(l==null || l.isEmpty()) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return l;
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		} catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	
	public List<Number> getNumberMatchPattern(String contenuto, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(contenuto == null)
			throw new JsonPathException(DOCUMENT_STRING_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			List<Number> l = new ArrayList<>();
			Object o = JsonPath.read(contenuto, pattern);
			_parseNumberMatchPatternResult(o, l);
			if(l==null || l.isEmpty()) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return l;
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		}catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	
	private void _parseNumberMatchPatternResult(Object o, List<Number> l) throws Exception {
		if(o!=null) {
			if(o instanceof List) {
				List<?> lO = (List<?>) o;
				if(!lO.isEmpty()) {
					int position = 0;
					for (Object object : lO) {
						if(object!=null) {
							if(object instanceof Number) {
								l.add((Number)object);
							}
							else{
								throw new Exception("Unexpected type '"+object.getClass().getName()+"' at position "+position);
							}
						}
						position++;
					}
				}
			}
			else if(o instanceof Number) {
				l.add((Number)o);
			}
			else {
				throw new Exception("Unexpected type '"+o.getClass().getName()+"'");
			}
		}
	}
	

	/* ---------- METODI RITORNANO BOOLEAN -------------- */

	public List<Boolean> getBooleanMatchPattern(JSONObject input, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(input == null)
			throw new JsonPathException(DOCUMENT_JSON_OBJECT_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			List<Boolean> l = new ArrayList<>();
			Object o = JsonPath.read(input, pattern);
			_parseBooleanMatchPatternResult(o, l);
			if(l==null || l.isEmpty()) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return l;
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		}catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
		
	}

	public List<Boolean> getBooleanMatchPattern(JsonNode document, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(document == null)
			throw new JsonPathException(DOCUMENT_JSON_NODE_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			List<Boolean> l = new ArrayList<>();
			Object o = JsonPath.read(getAsString(document), pattern);
			_parseBooleanMatchPatternResult(o, l);
			if(l==null || l.isEmpty()) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return l;
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		}catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	
	public List<Boolean> getBooleanMatchPattern(InputStream is, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(is == null)
			throw new JsonPathException(DOCUMENT_INPUT_STREAM_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			List<Boolean> l = new ArrayList<>();
			Object o = JsonPath.read(is, pattern);
			_parseBooleanMatchPatternResult(o, l);
			if(l==null || l.isEmpty()) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return l;
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		} catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	
	public List<Boolean> getBooleanMatchPattern(String contenuto, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(contenuto == null)
			throw new JsonPathException(DOCUMENT_STRING_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			List<Boolean> l = new ArrayList<>();
			Object o = JsonPath.read(contenuto, pattern);
			_parseBooleanMatchPatternResult(o, l);
			if(l==null || l.isEmpty()) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return l;
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		}catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	
	private void _parseBooleanMatchPatternResult(Object o, List<Boolean> l) throws Exception {
		if(o!=null) {
			if(o instanceof List) {
				List<?> lO = (List<?>) o;
				if(!lO.isEmpty()) {
					int position = 0;
					for (Object object : lO) {
						if(object!=null) {
							if(object instanceof Boolean) {
								l.add((Boolean)object);
							}
							else{
								throw new Exception("Unexpected type '"+object.getClass().getName()+"' at position "+position);
							}
						}
						position++;
					}
				}
			}
			else if(o instanceof Boolean) {
				l.add((Boolean)o);
			}
			else {
				throw new Exception("Unexpected type '"+o.getClass().getName()+"'");
			}
		}
	}
	

	/* ---------- METODI RITORNANO JSON NODE -------------- */

	public JsonNode getJsonNodeMatchPattern(JSONObject input, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(input == null)
			throw new JsonPathException(DOCUMENT_JSON_OBJECT_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			Object object = JsonPath.read(input, pattern);
			if(object==null) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return this.convertToJsonNode(object);
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		}catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}

	public JsonNode getJsonNodeMatchPattern(JsonNode document, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(document == null)
			throw new JsonPathException(DOCUMENT_JSON_NODE_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);
		
		this.validate(pattern);
		
		try {
			String inputString = getAsString(document);
			Object object = JsonPath.read(inputString, pattern);
			if(object==null) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return this.convertToJsonNode(object);
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		}catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	
	public JsonNode getJsonNodeMatchPattern(InputStream is, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(is == null)
			throw new JsonPathException(DOCUMENT_INPUT_STREAM_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);
		
		this.validate(pattern);
		
		try {
			Object object = JsonPath.read(is, pattern);
			if(object==null) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return this.convertToJsonNode(object);
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		}catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}
	}
	
	public JsonNode getJsonNodeMatchPattern(String contenuto, String pattern) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(contenuto == null)
			throw new JsonPathException(DOCUMENT_STRING_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		try {
			Object object = JsonPath.read(contenuto, pattern);
			if(object==null) {
				throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
			}
			return this.convertToJsonNode(object);
		}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
			throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
		}catch(Exception e) {
			throw new JsonPathException(e.getMessage(), e);
		}

	}
	private JsonNode convertToJsonNode(Object object) throws UtilsException {
		if(object instanceof String) {
			return getJsonUtils().getAsNode("\""+((String) object)+"\"");
		}
		else  if(object instanceof JSONArray) {
			JSONArray jsonObject = (JSONArray) object;
			return getJsonUtils().getAsNode(jsonObject.toString());
		}
		else if(object instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) object;
			return getJsonUtils().getAsNode(jsonObject.toString());
		}
		else if(object instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) object;
			return getJsonUtils().getAsNode(map);
		}
		else {
			return getJsonUtils().getAsNode(object.toString());
		}
	}
	

	/* ---------- METODI RITORNANO OGGETTI -------------- */

	public Object getMatchPattern(JSONObject input, String pattern, JsonPathReturnType returnType) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(input == null)
			throw new JsonPathException(DOCUMENT_JSON_OBJECT_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		if(returnType == null)
			throw new JsonPathException(JSON_PATH_RETURN_TYPE_IS_NULL);

		Object risposta = null;
		switch(returnType) {
		case STRING: 
			risposta = getStringMatchPattern(input, pattern); 
			break;
		case NUMBER: 
			risposta = getNumberMatchPattern(input, pattern);
			break;
		case BOOLEAN: 
			risposta = getBooleanMatchPattern(input, pattern);
			break;
		case NODE: 
			risposta = getJsonNodeMatchPattern(input, pattern);
			break;
		default:
			try {
				risposta = JsonPath.read(input, pattern);
			}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
				throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
			}
			break;
		}
		if(risposta==null) {
			throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
		}
		return risposta;
	}
	

	public Object getMatchPattern(JsonNode input, String pattern, JsonPathReturnType returnType) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(input == null)
			throw new JsonPathException("Document (JsonNode)is null");

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		if(returnType == null)
			throw new JsonPathException(JSON_PATH_RETURN_TYPE_IS_NULL);

		Object risposta = null;
		String inputString = getAsString(input);
		switch(returnType) {
		case STRING: 
			risposta = getStringMatchPattern(inputString, pattern);
			break;
		case NUMBER: 
			risposta = getNumberMatchPattern(inputString, pattern);
			break;
		case BOOLEAN: 
			risposta = getBooleanMatchPattern(inputString, pattern);
			break;
		case NODE: 
			risposta = getJsonNodeMatchPattern(inputString, pattern);
			break;
		default:
			try {
				risposta = JsonPath.read(input, pattern);
			}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
				throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
			}
			break;
		}
		if(risposta==null) {
			throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
		}
		return risposta;
	}
	
	public Object getMatchPattern(InputStream input, String pattern, JsonPathReturnType returnType)throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(input == null)
			throw new JsonPathException(DOCUMENT_INPUT_STREAM_IS_NULL);

		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		if(returnType == null)
			throw new JsonPathException(JSON_PATH_RETURN_TYPE_IS_NULL);

		Object risposta = null;
		switch(returnType) {
		case STRING: 
			risposta = getStringMatchPattern(input, pattern);
			break;
		case NUMBER: 
			risposta = getNumberMatchPattern(input, pattern);
			break;
		case BOOLEAN: 
			risposta = getBooleanMatchPattern(input, pattern);
			break;
		case NODE: 
			risposta = getJsonNodeMatchPattern(input, pattern);
			break;
		default:
			try {
				risposta = JsonPath.read(input, pattern);
			}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
				throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
			}catch(Exception e) {
				throw new JsonPathException(e.getMessage(), e);
			}
			break;
		}
		if(risposta==null) {
			throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
		}
		return risposta;
	}
	
	public Object getMatchPattern(String input, String pattern, JsonPathReturnType returnType) throws JsonPathException, JsonPathNotFoundException, JsonPathNotValidException {
		if(input == null)
			throw new JsonPathException(DOCUMENT_STRING_IS_NULL);
		
		if(pattern == null)
			throw new JsonPathException(PATTERN_IS_NULL);

		this.validate(pattern);
		
		if(returnType == null)
			throw new JsonPathException(JSON_PATH_RETURN_TYPE_IS_NULL);

		Object risposta = null;
		switch(returnType) {
		case STRING: 
			risposta = getStringMatchPattern(input, pattern);
			break;
		case NUMBER: 
			risposta = getNumberMatchPattern(input, pattern);
			break;
		case BOOLEAN: 
			risposta = getBooleanMatchPattern(input, pattern);
			break;
		case NODE: 
			risposta = getJsonNodeMatchPattern(input, pattern);
			break;
		default:
			try {
				risposta = JsonPath.read(input, pattern);
			}catch(com.jayway.jsonpath.PathNotFoundException notFound) {
				throw new JsonPathNotFoundException(notFound.getMessage(),notFound);
			}
			break;
		}
		if(risposta==null) {
			throw new com.jayway.jsonpath.PathNotFoundException(MessageFormat.format(NESSUN_MATCH_TROVATO_PER_L_ESPRESSIONE_JSON_PATH_0, pattern));
		}
		return risposta; 
	}
	
	
	/* ---------- VALIDATORE -------------- */
	
	public void validate(String path) throws JsonPathNotValidException{
		try {
			JsonPath.compile(path);
		} catch(Exception e) {
			throw new JsonPathNotValidException("Validazione del jsonPath indicato ["+path+"] fallita: "+e.getMessage(),e);
		}
	}

	
	public static String extractAndConvertResultAsString(String elementJson, String pattern, Logger log) throws Exception {
		List<String> l = _extractAndConvertResultAsString(elementJson, pattern, log, false);
		if(l!=null && !l.isEmpty()) {
			return l.get(0);
		}
		else {
			return null;
		}
	}
	public static List<String> extractAndConvertResultAsList(String elementJson, String pattern, Logger log) throws Exception {
		return  _extractAndConvertResultAsString(elementJson, pattern, log, true);
	}
	private static List<String> _extractAndConvertResultAsString(String elementJson, String pattern, Logger log, boolean returnAsList) throws Exception {
		
		List<String> lReturn = new ArrayList<>();
		
		JsonPathExpressionEngine engine = new JsonPathExpressionEngine();
		
		Exception exceptionNodeSet = null;
		try{
			List<?> l = engine.getStringMatchPattern(elementJson, pattern);
			if(l!=null && !l.isEmpty()) {
				if(returnAsList) {
					for (Object s : l) {
						if(s instanceof String) {
							lReturn.add((String)s);
						}
						else if(s instanceof Map<?, ?>) {
							try {
								Map<?,?> map = (Map<?,?>) s;
								if(!map.isEmpty()) {
									StringBuilder sb = new StringBuilder("{");
									for (Object keyO : map.keySet()) {
										if(sb.length()>1) {
											sb.append(",");
										}
										String key = (String) keyO;
										sb.append("\"").append(key).append("\": ");
										Object valueO = map.get(keyO);
										if(valueO instanceof Boolean || 
												valueO instanceof Short ||
												valueO instanceof Integer ||
												valueO instanceof Long ||
												valueO instanceof Double ||
												valueO instanceof Float) {
											sb.append(valueO);
										}
										else {
											sb.append("\"").append(valueO).append("\"");
										}
									}
									sb.append("}");
									lReturn.add(sb.toString());
								}
							}catch(Throwable t) {
								lReturn.add(s.toString());
							}
						}
						else {
							lReturn.add(s.toString());
						}
					}
				}
				else {
					StringBuilder sbReturn = new StringBuilder();
					
					boolean first = true;
					for (Object s : l) {
						if(!first) {
							sbReturn.append(",");	
						}
						if(s instanceof String) {
							sbReturn.append((String)s);
						}
						else if(s instanceof Map<?, ?>) {
							try {
								Map<?,?> map = (Map<?,?>) s;
								if(!map.isEmpty()) {
									StringBuilder sb = new StringBuilder("{");
									for (Object keyO : map.keySet()) {
										if(sb.length()>1) {
											sb.append(",");
										}
										String key = (String) keyO;
										sb.append("\"").append(key).append("\": ");
										Object valueO = map.get(keyO);
										if(valueO instanceof Boolean || 
												valueO instanceof Short ||
												valueO instanceof Integer ||
												valueO instanceof Long ||
												valueO instanceof Double ||
												valueO instanceof Float) {
											sb.append(valueO);
										}
										else {
											sb.append("\"").append(valueO).append("\"");
										}
									}
									sb.append("}");
									sbReturn.append(sb.toString());
								}
							}catch(Throwable t) {
								sbReturn.append(s.toString());
							}
						}
						else {
							sbReturn.append(s.toString());
						}
						first=false;
					}
					
					lReturn.add(sbReturn.toString());
				}
			}
						
		}catch(Exception e){
			exceptionNodeSet = e;
		}
			
		if(lReturn.isEmpty()){
			
			JsonNode obj = engine.getJsonNodeMatchPattern(elementJson, pattern);
			String risultato = null;
			if(obj!=null) {
				if(obj instanceof TextNode text) {
					risultato = text.asText();
				}
				else {
					risultato = obj.toString();
				}
			}
			if(risultato!=null && risultato.startsWith("[") && risultato.endsWith("]")) {
				risultato = risultato.substring(1, risultato.length()-1);
			}
			if(risultato!=null && !"".equals(risultato)) {
				lReturn.add(risultato);
			}
			
			if(exceptionNodeSet!=null){
				log.debug("Non sono stati trovati risultati tramite l'invocazione del metodo getStringMatchPattern({}) invocato in seguito all'errore dell'invocazione getJsonNodeMatchPattern({},NODESET): {}",pattern, pattern, exceptionNodeSet.getMessage(),exceptionNodeSet);
			}
			
		}
		
		return lReturn;
	}
}