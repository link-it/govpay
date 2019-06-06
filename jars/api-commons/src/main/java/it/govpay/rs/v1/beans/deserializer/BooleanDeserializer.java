package it.govpay.rs.v1.beans.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;

public class BooleanDeserializer extends JsonDeserializer<Boolean> {

	
	@Override
	public Boolean deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
		
		if(node instanceof BooleanNode) {
			BooleanNode bNode = (BooleanNode) node;
			
			return bNode.booleanValue();
		}
		
		throw new JsonParseException(jsonParser, "il field " + jsonParser.getCurrentName() + " non e' di tipo " + Boolean.class.getName() + ".");
	}
}
