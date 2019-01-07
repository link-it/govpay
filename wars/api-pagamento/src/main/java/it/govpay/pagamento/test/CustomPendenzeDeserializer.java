package it.govpay.pagamento.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openspcoop2.utils.jaxrs.JacksonJsonProvider;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.govpay.pagamento.test.NuovaPendenza;
import it.govpay.pagamento.test.RiferimentoAvviso;
import it.govpay.pagamento.test.RiferimentoPendenza;

public class CustomPendenzeDeserializer extends JsonDeserializer<Object> {

	@Override
	public List<Object> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		
		ObjectMapper objectMapper = JacksonJsonProvider.getObjectMapper();
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
        Iterator<JsonNode> elements = node.elements();
        
        List<Object> pendenze = new ArrayList<Object>();
        for (; elements.hasNext();) {
        	JsonNode next = elements.next();
        	
        	if(next.size() == 2 && next.hasNonNull("idDominio") && next.hasNonNull("numeroAvviso")) {
    			pendenze.add(objectMapper.treeToValue(next, RiferimentoAvviso.class));
    			continue;
        	}
        	if(next.size() == 2 && next.hasNonNull("idA2A") && next.hasNonNull("idPendenza")) {
    			pendenze.add(objectMapper.treeToValue(next, RiferimentoPendenza.class));
    			continue;
        	}
        	
        	pendenze.add(objectMapper.treeToValue(next, NuovaPendenza.class));
        }
		return pendenze;
	}

}
