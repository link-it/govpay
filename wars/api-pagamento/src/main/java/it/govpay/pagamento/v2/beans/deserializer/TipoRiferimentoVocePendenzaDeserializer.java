package it.govpay.pagamento.v2.beans.deserializer;

import java.io.IOException;

import org.openspcoop2.utils.jaxrs.JacksonJsonProvider;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.govpay.pagamento.v2.beans.Bollo;
import it.govpay.pagamento.v2.beans.Entrata;
import it.govpay.pagamento.v2.beans.RiferimentoEntrata;

public class TipoRiferimentoVocePendenzaDeserializer extends JsonDeserializer<Object> {

	@Override
	public Object deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		
		ObjectMapper objectMapper = JacksonJsonProvider.getObjectMapper(true);
		
		ObjectCodec oc = jsonParser.getCodec();
		
		JsonNode node = oc.readTree(jsonParser);
		
		if(node.size() == 3 && node.hasNonNull("tipoBollo") && node.hasNonNull("hashDocumento") && node.hasNonNull("provinciaResidenza")) {
			return objectMapper.treeToValue(node,  Bollo.class);
		} else if(node.size() == 1 && node.hasNonNull("codEntrata")) {
			return objectMapper.treeToValue(node,  RiferimentoEntrata.class);
    	}
		else {
			return objectMapper.treeToValue(node,  Entrata.class);
		}
//
//		
//		
//        Iterator<JsonNode> elements = node.elements();
//        
//        List<Object> pendenze = new ArrayList<Object>();
//        for (; elements.hasNext();) {
//        	JsonNode next = elements.next();
//        	
//        	
//        	if(next.size() == 3 && next.hasNonNull("tipoBollo") && next.hasNonNull("hashDocumento") && next.hasNonNull("provinciaResidenza")) {
//    			pendenze.add(objectMapper.treeToValue(next, Bollo.class));
//    			continue;
//        	}
//        	if(next.size() == 1 && next.hasNonNull("codEntrata")) {
//    			pendenze.add(objectMapper.treeToValue(next, RiferimentoEntrata.class));
//    			continue;
//        	}
        	
//        	pendenze.add(objectMapper.treeToValue(next, Entrata.class));
//        	pendenze.add(objectMapper.treeToValue(next, NuovaVocePendenza.class));
//        }
//        
//        
//        
//		return pendenze;
	}

}